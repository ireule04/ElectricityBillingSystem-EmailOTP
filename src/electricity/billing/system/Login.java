    package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JButton login, cancel, signup;
    JTextField username;
    JPasswordField password;
    Choice loginin;
    JCheckBox showPasswordCheckBox;

    Login() {
        super("Login Page");
        getContentPane().setBackground(new Color(115,115,110));
        setLayout(null);

        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(300, 40, 100, 20);
        lblusername.setForeground(Color.BLACK);
        add(lblusername);

        username = new JTextField();
        username.setBounds(400, 40, 150, 20);
        add(username);

        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(300, 80, 100, 20);
        lblpassword.setForeground(Color.BLACK);
        add(lblpassword);

        password = new JPasswordField();
        password.setBounds(400, 80, 130, 20); //  length of the password field
        add(password);

        showPasswordCheckBox = new JCheckBox();
        showPasswordCheckBox.setBounds(530, 80, 20, 20); // checkbox next side of password field
        add(showPasswordCheckBox);

        JLabel loginas = new JLabel("Login as:");
        loginas.setBounds(300, 120, 100, 20);
        loginas.setForeground(Color.BLACK);
        add(loginas);

        loginin = new Choice(); // choice global declaration
        loginin.addItem("Admin");
        loginin.addItem("Customer");
       // loginin.addItem("Reader");
        loginin.setBounds(400, 120, 150, 20);
        add(loginin);

        login = new JButton("Login");
        login.setBounds(320, 160, 100, 25);
        login.setForeground(Color.WHITE);
        login.setBackground(new Color(52, 152, 219));
        login.addActionListener(this);
        add(login);

        cancel = new JButton("Cancel");
        cancel.setBounds(440, 160, 100, 25);
        cancel.setForeground(Color.WHITE);
        cancel.setBackground(new Color(231, 76, 60));
        cancel.addActionListener(this);
        add(cancel);

        signup = new JButton("Signup");
        signup.setBounds(380, 200, 100, 25);
        signup.setForeground(Color.WHITE);
        signup.setBackground(new Color(39, 174, 96));
        signup.addActionListener(this);
        add(signup);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon/login02.gif"));
        Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT);
        ImageIcon loginIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(loginIcon);
        imageLabel.setBounds(20, 20, 300, 240);
        add(imageLabel);
        
        

        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                password.setEchoChar(checkBox.isSelected() ? '\0' : '•'); // Show/hide password
            }
        });

        setSize(640, 300); //  the frame height
        setLocation(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String susername = username.getText();
            String spassword = new String(password.getPassword()); // Get password string
            String user = loginin.getSelectedItem();

            try {
                Conn c = new Conn();
                String query = "SELECT * FROM login WHERE username='" + susername + "' and password='" + spassword
                        + "' and atype='" + user + "'";

                ResultSet rs = c.s.executeQuery(query);

                if (rs.next()) {
                    String meter = rs.getString("meter_no");
                    setVisible(false);
                    new Project(user, meter); // forward to user and meter

                } else {
                    JOptionPane.showMessageDialog(null, "Username or Password doesn't match!");
                    username.setText(""); // clears field after error
                    password.setText(""); // clears field after error
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Handle login action
        } else if (ae.getSource() == cancel) {
            setVisible(false);
        } else if (ae.getSource() == signup) {
            setVisible(false);
            new Signup();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
