package electricity.billing.system;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CalculateBills extends JFrame implements ActionListener {
    JTextField tfunits;
    JButton next, cancel;
    JLabel lblname, labeladdress, labelemail, labelbtype;
    Choice meternumber, cmonth;

    CalculateBills() {
        setSize(700, 500);
        setLocation(400, 150);

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(153, 153,153));
        add(p);

        JLabel heading = new JLabel("Calculate Electricity Bill");
        heading.setBounds(100, 10, 400, 25);
        heading.setFont(new Font("DialogInput", Font.BOLD, 24));
        p.add(heading);

        JLabel lblmeternumber = new JLabel("Meter Number");
        lblmeternumber.setBounds(100, 80, 100, 20);
        p.add(lblmeternumber);

        meternumber = new Choice();

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from customer");
            while (rs.next()) {
                meternumber.add(rs.getString("meter_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        meternumber.setBounds(240, 80, 200, 20);
        p.add(meternumber);

        JLabel lblmeterno = new JLabel("Name");
        lblmeterno.setBounds(100, 120, 100, 20);
        p.add(lblmeterno);

        lblname = new JLabel("");
        lblname.setBounds(240, 120, 200, 20);
        p.add(lblname);

        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(100, 160, 100, 20);
        p.add(lbladdress);

        labeladdress = new JLabel();
        labeladdress.setBounds(240, 160, 200, 20);
        p.add(labeladdress);

        JLabel lblemail = new JLabel("Email");
        lblemail.setBounds(100, 200, 100, 20);
        p.add(lblemail);

        labelemail = new JLabel();
        labelemail.setBounds(240, 200, 200, 20);
        p.add(labelemail);

        JLabel lblbtype = new JLabel("Bill Type");
        lblbtype.setBounds(100, 240, 100, 20);
        p.add(lblbtype);

        labelbtype = new JLabel();
        labelbtype.setBounds(240, 240, 200, 20);
        p.add(labelbtype);

        meternumber.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                updateCustomerInfo();
            }
        });

        JLabel lblunit = new JLabel("Units Consumed");
        lblunit.setBounds(100, 280, 100, 20);
        p.add(lblunit);

        tfunits = new JTextField();
        tfunits.setBounds(240, 280, 240, 20);
        p.add(tfunits);

        JLabel lblstate = new JLabel("Month");
        lblstate.setBounds(100, 320, 100, 20);
        p.add(lblstate);

        cmonth = new Choice();
        cmonth.setBounds(240, 320, 240, 20);
        cmonth.add("Baisakh");
        cmonth.add("Jestha");
        cmonth.add("Asar");
        cmonth.add("Srawan");
        cmonth.add("Bhadra");
        cmonth.add("Ashoj");
        cmonth.add("Kartik");
        cmonth.add("Mangsir");
        cmonth.add("Poush");
        cmonth.add("Magh");
        cmonth.add("Falgun");
        cmonth.add("Chaitra");
        p.add(cmonth);

        next = new JButton("Submit");
        next.setBounds(120, 380, 100, 25);
        next.setBackground(new Color(0,153,0));
        next.setForeground(Color.WHITE);
        next.addActionListener(this);
        p.add(next);

        cancel = new JButton("Cancel");
        cancel.setBounds(250, 380, 100, 25);
        cancel.setBackground(Color.RED);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        p.add(cancel);

        setLayout(new BorderLayout());
        add(p, "Center");
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == next) {
            String meter = meternumber.getSelectedItem();
            String units = tfunits.getText();
            String month = cmonth.getSelectedItem();

            int totalbill = 0;
            int unit_consumed = Integer.parseInt(units);

            String query = "select * from tax";

            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery(query);

                while (rs.next()) {
                    totalbill += unit_consumed * Integer.parseInt(rs.getString("cost_per_unit"));
                    totalbill += Integer.parseInt(rs.getString("meter_rent"));
                    totalbill += Integer.parseInt(rs.getString("service_charge"));
                    totalbill += Integer.parseInt(rs.getString("service_tax"));
                    totalbill += Integer.parseInt(rs.getString("energy_charge"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String query2 = "insert into bill values('" + meter + "', '" + month + "', '" + units + "', '" + totalbill + "', 'Not Paid')";

            try {
                Conn c = new Conn();
                c.s.executeUpdate(query2);

                // Send email to the consumer
                sendEmail(meter, totalbill);

                JOptionPane.showMessageDialog(null, "Bill Successfully Updated");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }

    private void updateCustomerInfo() {
        try {
            Conn c = new Conn();
            String meterNumber = meternumber.getSelectedItem();
            String query = "SELECT customer.name, customer.address, customer.Email, meter_info.bill_type " +
                           "FROM customer " +
                           "INNER JOIN meter_info ON customer.meter_no = meter_info.meter_no " +
                           "WHERE customer.meter_no = '" + meterNumber + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                lblname.setText(rs.getString("name"));
                labeladdress.setText(rs.getString("address"));
                labelemail.setText(rs.getString("Email"));
                labelbtype.setText(rs.getString("bill_type"));
            } else {
                // Handle the case when no data is found  selected meter number ma
                lblname.setText("");
                labeladdress.setText("");
                labelemail.setText("");
                labelbtype.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String meter, int totalbill) {
        // Email configuration
        String toEmail = labelemail.getText(); // Consumer ko email address
        String fromEmail = "bijulibattipowerltd@gmail.com"; // send garne email address
        String password = "utch alfs hfwh cgbm"; //app email ko password
        String host = "smtp.gmail.com";
        String port = "587";

        String month = cmonth.getSelectedItem();

        // Email properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a session
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header
            message.setFrom(new InternetAddress(fromEmail));

            // Set To: header field of the header
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            // Set Subject: header field
            message.setSubject("Your Electricity Bill");

            // Set the email message content
            String emailContent = "Dear Customer,\n\n";
            emailContent += "Your electricity bill for the month of " + month + " is Rs. " + totalbill + "\n\n";

            emailContent += "Now you can generate bill in the generate bill section.\n\n";
            emailContent += "Paying the bill on time can avoid late fees.\n\n";
            emailContent += "Thank you for using our services.\n\n";
            emailContent += "Sincerely,\nBIJULI BATTI POWER LTD";

            message.setText(emailContent);

            // Send the email meaassage
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CalculateBills();
    }
}
