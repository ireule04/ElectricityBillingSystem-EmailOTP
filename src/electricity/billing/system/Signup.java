package electricity.billing.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.CharacterPredicates;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Signup extends JFrame implements ActionListener {

    JButton create, back;
    Choice accountType;
    JTextField meter, username, name, password, email;

    Signup() {
        setBounds(450, 150, 720, 500);
        getContentPane().setBackground(new Color(153,153,153));
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(30, 30, 650, 400);
        panel.setBorder(new TitledBorder(new LineBorder(new Color(41, 128, 185), 2), "New Account", TitledBorder.CENTER,
                TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 16), new Color(41, 128, 185)));
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setForeground(Color.RED);
        add(panel);

        JLabel heading = new JLabel("Create Account");
        heading.setBounds(100, 50, 140, 20);
        heading.setForeground(Color.BLACK);
        heading.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(heading);

        accountType = new Choice();
        accountType.add("Admin");
        accountType.add("Customer");
        accountType.setBounds(260, 50, 150, 20);
        panel.add(accountType);

        JLabel lblmeter = new JLabel("Meter Number");
        lblmeter.setBounds(100, 90, 140, 20);
        lblmeter.setForeground(Color.BLACK);
        lblmeter.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblmeter.setVisible(false);
        panel.add(lblmeter);

        meter = new JTextField();
        meter.setBounds(260, 90, 150, 20);
        meter.setVisible(false);
        panel.add(meter);

        JLabel lblemail = new JLabel("Email");
        lblemail.setBounds(100, 130, 140, 20);
        lblemail.setForeground(Color.BLACK);
        lblemail.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblemail);

        email = new JTextField();
        email.setBounds(260, 130, 150, 20);
        panel.add(email);

        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(100, 170, 140, 20);
        lblusername.setForeground(Color.BLACK);
        lblusername.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblusername);

        username = new JTextField();
        username.setBounds(260, 170, 150, 20);
        panel.add(username);

        JLabel lblname = new JLabel("Name");
        lblname.setBounds(100, 210, 140, 20);
        lblname.setForeground(Color.BLACK);
        lblname.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblname);

        name = new JTextField();
        name.setBounds(260, 210, 150, 20);
        panel.add(name);

        meter.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {}

            @Override
            public void focusLost(FocusEvent fe) {
                try {
                    Conn c  = new Conn();
                    ResultSet rs = c.s.executeQuery("select * from customer where meter_no = '"+meter.getText()+"'");
                    while(rs.next()) {
                        name.setText(rs.getString("name"));
                        email.setText(rs.getString("email"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(100, 250, 140, 20);
        lblpassword.setForeground(Color.BLACK);
        lblpassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblpassword);

        password = new JTextField();
        password.setBounds(260, 250, 150, 20);
        panel.add(password);

        accountType.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ae){
                String user = accountType.getSelectedItem();
                if (user.equals("Customer")){
                    lblmeter.setVisible(true);
                    meter.setVisible(true);
                    name.setEditable(false);
                } else {
                    lblmeter.setVisible(false);
                    meter.setVisible(false);
                    name.setEditable(true);
                }
            }
        });

        create = new JButton("Create");
        create.setBackground(new Color(52, 152, 219));
        create.setForeground(Color.WHITE);
        create.setBounds(100, 340, 120, 25);
        create.addActionListener(this);
        panel.add(create);

        back = new JButton("Back");
        back.setBackground(new Color(231, 76, 60));
        back.setForeground(Color.WHITE);
        back.setBounds(290, 340, 120, 25);
        back.addActionListener(this);
        panel.add(back);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/signin.gif"));
        Image i2 = i1.getImage().getScaledInstance(200, 400, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(410, 30, 250, 300);
        panel.add(image);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == create) {
            String atype = accountType.getSelectedItem();
            String susername = username.getText();
            String sname = name.getText();
            String spassword = password.getText();
            String smeter = meter.getText();
            String semail = email.getText();

            // Check if the user is a customer
            if (atype.equals("Customer")) {
                // Generate OTP using the GenerateOTP class
                String otp = GenerateOTP.generateOTP();

                // Send the OTP to the customer's email
                sendEmail(semail, otp);

                // Ask for OTP confirmation
                String enteredOTP = JOptionPane.showInputDialog("Enter the OTP sent to your email:");

                // Verify OTP
                if (!enteredOTP.equals(otp)) {
                    JOptionPane.showMessageDialog(null, "Incorrect OTP. Please try again.");
                    return; // Do not proceed with account creation
                }
            }

            try {
                Conn c = new Conn();

                String query = null;
                if (atype.equals("Admin")) {
                    query = "insert into login values('" + smeter + "', '" + susername + "', '" + sname + "', '" + spassword + "', '" + atype + "')";
                } else {
                    query = "update login set username = '" + susername + "', password = '" + spassword + "', atype = '" + atype + "' where meter_no = '" + smeter + "'";
                }
                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Account Created Successfully");

                setVisible(false);
                new Login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Login();
        }
    }

    private void sendEmail(String recipient, String otp) {
        // Replace with your email configuration and credentials
        String email = "bijulibattipowerltd@gmail.com";
        String password = "utch alfs hfwh cgbm";

        // Setup mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a Session object with your email credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        try {
            // Create a MimeMessage object
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(email));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            mimeMessage.setSubject("OTP for Signup");
            mimeMessage.setText("Your OTP for signup is: " + otp);

            // Send the email
            Transport.send(mimeMessage);

            System.out.println("OTP email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending OTP email: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Signup();
    }
}
