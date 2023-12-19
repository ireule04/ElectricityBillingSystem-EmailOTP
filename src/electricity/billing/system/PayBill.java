
package electricity.billing.system;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PayBill extends JFrame implements ActionListener {

    Choice cmonth;
    JButton pay, back;
    String meter;

    JLabel labeltotalbill;

    PayBill(String meter) {
        this.meter = meter;
        setLayout(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setBounds(300, 150, 900, 600);

        JLabel heading = new JLabel("Electricity Bill");
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        heading.setBounds(120, 5, 400, 30);
        add(heading);

        JLabel lblmeternumber = new JLabel("Meter Number");
        lblmeternumber.setBounds(35, 80, 200, 20);
        add(lblmeternumber);

        JLabel meternumber = new JLabel("");
        meternumber.setBounds(300, 80, 200, 20);
        add(meternumber);

        JLabel lblname = new JLabel("Name");
        lblname.setBounds(35, 140, 200, 20);
        add(lblname);

        JLabel labelname = new JLabel("");
        labelname.setBounds(300, 140, 200, 20);
        add(labelname);

        JLabel lblmonth = new JLabel("Month");
        lblmonth.setBounds(35, 200, 200, 20);
        add(lblmonth);

        cmonth = new Choice();
        cmonth.setBounds(300, 200, 200, 20);
        cmonth.add("Baisakh");
        cmonth.add("Jestha");
        cmonth.add("Asar");
        cmonth.add("Shrawan");
        cmonth.add("Bhadra");
        cmonth.add("Ashoj");
        cmonth.add("Kartik");
        cmonth.add("Mangsir");
        cmonth.add("Poush");
        cmonth.add("Magh");
        cmonth.add("Falgun");
        cmonth.add("Chaitra");
        add(cmonth);

        JLabel lblunits = new JLabel("Units");
        lblunits.setBounds(35, 260, 200, 20);
        add(lblunits);

        JLabel labelunits = new JLabel("");
        labelunits.setBounds(300, 260, 200, 20);
        add(labelunits);

        JLabel lbltotalbill = new JLabel("Total Bill");
        lbltotalbill.setBounds(35, 320, 200, 20);
        add(lbltotalbill);

        labeltotalbill = new JLabel("");
        labeltotalbill.setBounds(300, 320, 200, 20);
        add(labeltotalbill);

        JLabel lblstatus = new JLabel("Status");
        lblstatus.setBounds(35, 380, 200, 20);
        add(lblstatus);

        JLabel labelstatus = new JLabel("");
        labelstatus.setBounds(300, 380, 200, 20);
        labelstatus.setForeground(Color.RED);
        add(labelstatus);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from customer where meter_no = '" + meter + "'");
            while (rs.next()) {
                meternumber.setText(meter);
                labelname.setText(rs.getString("name"));
            }

            rs = c.s.executeQuery("select * from bill where meter_no = '" + meter + "' AND month = 'Baisakh'");
            while (rs.next()) {
                labelunits.setText(rs.getString("units"));
                labeltotalbill.setText(rs.getString("totalbill"));
                labelstatus.setText(rs.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cmonth.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ae) {
                try {
                    Conn c = new Conn();
                    ResultSet rs = c.s.executeQuery("select * from bill where meter_no = '" + meter + "' AND month = '"
                            + cmonth.getSelectedItem() + "'");
                    while (rs.next()) {
                        labelunits.setText(rs.getString("units"));
                        labeltotalbill.setText(rs.getString("totalbill"));
                        labelstatus.setText(rs.getString("status"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        pay = new JButton("Pay");
        pay.setBackground(Color.GREEN);
        pay.setForeground(Color.BLACK);
        pay.setBounds(100, 460, 100, 25);
        pay.addActionListener(this);
        add(pay);

        back = new JButton("Back");
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.setBounds(230, 460, 100, 25);
        back.addActionListener(this);
        add(back);

        getContentPane().setBackground(Color.WHITE);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/pay.png"));
        Image i2 = i1.getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(400, 120, 600, 300);
        add(image);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == pay) {
            try {
                int daysElapsed = calculateDaysElapsed();
                determinePaymentCategory(daysElapsed);
                updateDatabase();

                setVisible(false);
                // Assuming there's a KhaltiPay class that you want to instantiate
                new KhaltiPay(meter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }

    private int calculateDaysElapsed() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(
                    "SELECT * FROM bill WHERE meter_no = '" + meter + "' AND month = '" + cmonth.getSelectedItem() + "'");
            if (rs.next()) {
                // Assuming there is no 'month_end_date' column
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date();
                
                // Set due date to the last day of the selected month
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.set(Calendar.MONTH, cmonth.getSelectedIndex());
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date dueDate = calendar.getTime();

                long diffInMilliseconds = currentDate.getTime() - dueDate.getTime();
                return (int) (diffInMilliseconds / (24 * 60 * 60 * 1000));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void determinePaymentCategory(int daysElapsed) {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(
                    "SELECT * FROM bill WHERE meter_no = '" + meter + "' AND month = '" + cmonth.getSelectedItem() + "'");
            if (rs.next()) {
                double totalBill = Double.parseDouble(rs.getString("totalbill"));
                if (daysElapsed <= 7) {
                    totalBill *= 0.9;
                } else if (daysElapsed > 30) {
                    totalBill *= 2.0;
                }
                labeltotalbill.setText(String.valueOf(totalBill));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDatabase() {
        try {
            Conn c = new Conn();
            c.s.executeUpdate(
                    "UPDATE bill SET status = 'Paid' WHERE meter_no = '" + meter + "' AND month='" + cmonth.getSelectedItem() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PayBill("");
    }
}
