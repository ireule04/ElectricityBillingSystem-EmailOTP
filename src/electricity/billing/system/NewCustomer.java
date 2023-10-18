package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher; //email/phone validate
import java.util.regex.Pattern; // email/phone validate

import java.util.*;
import java.awt.event.*;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.ResultSet;          //for checking existing email


public class NewCustomer extends JFrame implements ActionListener {

    JTextField tfname, tfaddress, tfstate, tfcity, tfemail, tfphone; // global declaration of textfield
    JButton next, cancel;
    JLabel lblmeter;
    
    NewCustomer() {

        setSize(700, 500);
        setLocation(400, 200);

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(153,153,153));
        add(p);

        JLabel heading = new JLabel("New Customer");
        heading.setBounds(180, 10, 200, 25);
        heading.setFont(new Font("DialogInput", Font.BOLD, 24));
        p.add(heading);

        JLabel lblname = new JLabel("Customer Name");
        lblname.setBounds(100, 80, 100, 20);
        p.add(lblname);

        tfname = new JTextField();
        tfname.setBounds(240, 80, 200, 20);
        p.add(tfname);

        JLabel lblmeterno = new JLabel("Meter Number");
        lblmeterno.setBounds(100, 120, 100, 20);
        p.add(lblmeterno);

        lblmeter = new JLabel("");
        lblmeter.setBounds(240, 120, 100, 20);
        p.add(lblmeter);

        Random ran = new Random();
        long number = ran.nextLong() % 1000000;
        lblmeter.setText("" + Math.abs(number));

        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(100, 160, 100, 20);
        p.add(lbladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(240, 160, 200, 20);
        p.add(tfaddress);

        JLabel lblstate = new JLabel("State");
        lblstate.setBounds(100, 200, 100, 20);
        p.add(lblstate);

        tfstate = new JTextField();
        tfstate.setBounds(240, 200, 200, 20);
        p.add(tfstate);

        JLabel lblcity = new JLabel("City");
        lblcity.setBounds(100, 240, 100, 20);
        p.add(lblcity);

        tfcity = new JTextField();
        tfcity.setBounds(240, 240, 200, 20);
        p.add(tfcity);

        JLabel lblemail = new JLabel("Email");
        lblemail.setBounds(100, 280, 100, 20);
        p.add(lblemail);

        tfemail = new JTextField();
        tfemail.setBounds(240, 280, 200, 20);
        p.add(tfemail);

        JLabel lblphone = new JLabel("Phone Number");
        lblphone.setBounds(100, 320, 100, 20);
        p.add(lblphone);

        tfphone = new JTextField();
        tfphone.setBounds(240, 320, 200, 20);
        p.add(tfphone);

        next = new JButton("Next");
        next.setBounds(100, 390, 100, 25);
        next.setBackground(Color.green);
        next.setForeground(Color.BLACK);
        next.addActionListener(this);
        p.add(next);

        cancel = new JButton("Cancel");
        cancel.setBounds(340, 390, 100, 25);
        cancel.setBackground(Color.red);
        cancel.setForeground(Color.white);
        cancel.addActionListener(this);
        p.add(cancel);

        setLayout(new BorderLayout());

        add(p, "Center");

        setVisible(true);
    }

public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == next) {
        String name = tfname.getText();
        String meter = lblmeter.getText();
        String address = tfaddress.getText();
        String city = tfcity.getText();
        String state = tfstate.getText();
        String email = tfemail.getText();
        String phone = tfphone.getText();

        // email validation
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email address");
            return;
        }

        // phone number validation
        if (!isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(null, "Invalid phone number");
            return;
        }

        // Check if the email already exists in the database
        if (isEmailExists(email)) {
            JOptionPane.showMessageDialog(null, "Email address already exists");
            return;
        }

        String query1 = "INSERT INTO customer VALUES('" + name + "', '" + meter + "', '" + address + "', '" + city
                + "', '" + state + "', '" + email + "', '" + phone + "')";
        String query2 = "INSERT INTO login VALUES('" + meter + "', '', '" + name + "', '', '')";

        try {
            Conn c = new Conn();
            c.s.executeUpdate(query1);
            c.s.executeUpdate(query2);

            JOptionPane.showMessageDialog(null, "Customer Details Added Successfully!!");
            setVisible(false);

            // Send email to the customer
            sendEmail(meter, email);

            // Start a new frame
            new MeterInfo(meter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        setVisible(false);
    }
}

private boolean isEmailExists(String email) {
    try {
        Conn c = new Conn();
        String query = "SELECT * FROM customer WHERE email = '" + email + "'";
        ResultSet rs = c.s.executeQuery(query);
        return rs.next(); // If there's a row with the email, it already exists
    } catch (Exception e) {
        e.printStackTrace();
        return true; // Assume email exists on error
    }
}
    private boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        String phonePattern = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private void sendEmail(String meter, String customerEmail) {
        // Email configuration
        String toEmail = customerEmail;
        String fromEmail = "bijulibattipowerltd@gmail.com"; // Your email address
        String password = "utch alfs hfwh cgbm"; // Your email ko password
        String host = "smtp.gmail.com";
        String port = "587";

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
            //  default MimeMessage object create garne
            MimeMessage message = new MimeMessage(session);

            // Set From: header field  header ko
            message.setFrom(new InternetAddress(fromEmail));

            // Set To: header field header ko
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            // Set Subject: header field ma
            message.setSubject("Account Created");

            // email sent garne message content
            String emailContent = "Dear Customer,\n\n";
            emailContent += "Your account has been successfully created!\n\n";
            emailContent += "Your meter number is: " + meter + "\n\n";
            emailContent += "Now you can create login credentials through SignUp window.\n\n";
            emailContent += "Thank you for choosing our services.\n\n";
            emailContent += "Sincerely,\nBIJULI BATTI POWER LTD";

            message.setText(emailContent);

            // after email send garepachi
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new NewCustomer();
    }
}
