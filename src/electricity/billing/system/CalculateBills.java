package electricity.billing.system;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Arrays;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class CalculateBills extends JFrame implements ActionListener {
 
        int threshold = 500; // Adjust the threshold value as needed
    int regularConsumption = 250; // Replace with the customer's regular consumption

    private Session emailSession; 
    
    JTextField tfunits;
    JButton next, cancel;
    JLabel lblname, labeladdress, labelemail, labelbtype;
    Choice meternumber, cmonth;

    public CalculateBills() {
        setSize(700, 500);
        setLocation(400, 150);

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(153, 153, 153));
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
        next.setBackground(new Color(0, 153, 0));
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

            // Check for errors before proceeding
            String error = checkForErrors();
            
            
            if (error != null) {
                // Send an error email to the customer
                sendErrorEmail(error);
                JOptionPane.showMessageDialog(null, error);
            } else {
                
                           int unitConsumed = Integer.parseInt(units);
                           
                                           if (doesBillExist(meter, month)) {
                    // A bill for the same meter and month already exists, show an error message
                    String errorMessage = "A bill for the same meter and month already exists.";
                    sendErrorEmail(errorMessage);
                    JOptionPane.showMessageDialog(null, errorMessage);
                } else {
                    // Calculate the bill and proceed as before
                    int totalbill = 0;
                    int unit_consumed = Integer.parseInt(units);

                    String query = "select * from tax";

                // Check if consumption exceeds the threshold
                if (unitConsumed > threshold) {
                    // Check if consumption is significantly higher than regular
                    if (unitConsumed > 1.2 * regularConsumption) {
                        // Send a warning email
                        sendWarningEmail(meter, unitConsumed, cmonth.getSelectedItem());

                    }
                }



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
                }
            }
        } else {
            setVisible(false);
        }
    }

    private boolean doesBillExist(String meter, String month) {
        String query = "SELECT * FROM bill WHERE meter_no = '" + meter + "' AND month = '" + month + "'";
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(query);
            return rs.next(); // If a row is returned, a bill already exists
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // In case of an error, assume no bill exists
    }

    private String checkForErrors() {
        String meter = meternumber.getSelectedItem();
        String units = tfunits.getText();
        String month = cmonth.getSelectedItem();

        if (meter.isEmpty() || units.isEmpty() || month.isEmpty()) {
            return "Please fill in all required fields.";
        }

        try {
            int unit_consumed = Integer.parseInt(units);
            if (unit_consumed < 0) {
                return "Invalid units consumed. Please enter a positive value.";
            }
        } catch (NumberFormatException e) {
            return "Invalid units consumed. Please enter a numeric value.";
        }

        return null;  // No errors found
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
        String toEmail = labelemail.getText();
        String fromEmail = "";
        String password = "";
        String host = "smtp.gmail.com";
        String port = "587";

        String month = cmonth.getSelectedItem();

        // Email properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Your Electricity Bill");

            String emailContent = "Dear Customer,\n\n";
            emailContent += "Your electricity bill for the month of " + month + " is Rs. " + totalbill + "\n\n";
            emailContent += "Now you can generate the bill in the generate bill section.\n\n";
            emailContent += "Paying the bill on time can avoid late fees.\n\n";
            emailContent += "Thank you for using our services.\n\n";
            emailContent += "Sincerely,\nBIJULI BATTI POWER LTD";

            message.setText(emailContent);

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private void sendErrorEmail(String error) {
        String toEmail = labelemail.getText();
        String fromEmail = "";
        String password = "";
        String host = "smtp.gmail.com";
        String port = "587";

        // Rest of the email sending logic to send error messages to the customer
        // Customize the email content to inform the customer about the error
    }
private void sendWarningEmail(String meter, int unitConsumed, String month) {
    String customerName = lblname.getText();
    String toEmail = labelemail.getText();
    String billType = labelbtype.getText();

    // Check if the bill type is "Household" before sending a warning
    if ("Household".equals(billType)) {
        String fromEmail = "";
        String password = "";
        String host = "smtp.gmail.com";
        String port = "587";

        // Email properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        emailSession = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(emailSession);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Electricity Consumption Warning ⚠️❗⚠️");

            String emailContent = "Dear " + customerName + ",\n\n";
            emailContent += "Your electricity consumption for the month of " + month + " is significantly higher than your regular usage.\n";
            emailContent += "Current month's usage: " + unitConsumed + " units.\n";

            // Calculate and include the growth rate compared to the previous month
//            int previousMonthConsumption = getPreviousMonthConsumption(meter, month);
//            double growthRate = ((double)(unitConsumed - previousMonthConsumption) / previousMonthConsumption) * 100;
//            emailContent += "Growth rate of consumption compared to the previous month: " + growthRate + "%\n";

            
              int previousMonthConsumption = getPreviousMonthConsumption(meter, month);
              double growthRate = ((double) (unitConsumed - previousMonthConsumption) / previousMonthConsumption) * 100;

                // Format growthRate to display only one decimal place
              String formattedGrowthRate = String.format("%.1f%%", growthRate);

              emailContent += "Growth rate of consumption compared to the previous month: " + formattedGrowthRate + "\n";


                
                
                
                
            emailContent += "Please review your consumption habits to avoid high bills.\n";
            emailContent += "Thank you for using our service.\n";

            message.setText(emailContent);

            Transport.send(message);
            System.out.println("Warning email sent successfully to " + toEmail);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}

// Method to retrieve the previous month's consumption
private int getPreviousMonthConsumption(String meter, String currentMonth) {
    int previousMonthConsumption = 0;

    // Calculate the previous month based on the current month
    String[] months = {"Baisakh", "Jestha", "Asar", "Srawan", "Bhadra", "Ashoj", "Kartik", "Mangsir", "Poush", "Magh", "Falgun", "Chaitra"};
    int currentMonthIndex = Arrays.asList(months).indexOf(currentMonth);
    int previousMonthIndex = (currentMonthIndex - 1 + 12) % 12; // Handle wraparound

    // Query the database for previous month's consumption
    String previousMonth = months[previousMonthIndex];
    String query = "SELECT units FROM bill WHERE meter_no = '" + meter + "' AND month = '" + previousMonth + "'";

    try {
        Conn c = new Conn();
        ResultSet rs = c.s.executeQuery(query);
        if (rs.next()) {
            previousMonthConsumption = rs.getInt("units");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return previousMonthConsumption;
}


    public static void main(String[] args) {
        new CalculateBills();
    }
}
