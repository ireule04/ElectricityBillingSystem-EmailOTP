package electricity.billing.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Login extends JFrame implements ActionListener {
    JButton login, cancel, signup;

    Login() {
        super("Login Page");
        getContentPane().setBackground(new Color(41, 128, 185));
        setLayout(null);

        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(300, 40, 100, 20);
        lblusername.setForeground(Color.WHITE);
        add(lblusername);

        JTextField username = new JTextField();
        username.setBounds(400, 40, 150, 20);
        add(username);

        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(300, 80, 100, 20);
        lblpassword.setForeground(Color.WHITE);
        add(lblpassword);

        JPasswordField password = new JPasswordField();
        password.setBounds(400, 80, 150, 20);
        add(password);

        JLabel loginas = new JLabel("Login as:");
        loginas.setBounds(300, 120, 100, 20);
        loginas.setForeground(Color.WHITE);
        add(loginas);

        JComboBox<String> loginin = new JComboBox<>();
        loginin.addItem("Admin");
        loginin.addItem("Customer");
        loginin.addItem("Reader");
        loginin.setBounds(400, 120, 150, 20);
        add(loginin);

        login = new JButton("Login");
        login.setBounds(320, 170, 100, 25);
        login.setForeground(Color.WHITE);
        login.setBackground(new Color(52, 152, 219));
        login.addActionListener(this);
        add(login);

        cancel = new JButton("Cancel");
        cancel.setBounds(440, 170, 100, 25);
        cancel.setForeground(Color.WHITE);
        cancel.setBackground(new Color(231, 76, 60));
        cancel.addActionListener(this);
        add(cancel);

        signup = new JButton("Signup");
        signup.setBounds(380, 210, 100, 25);
        signup.setForeground(Color.WHITE);
        signup.setBackground(new Color(39, 174, 96));
        signup.addActionListener(this);
        add(signup);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon/loginimg.png"));
        Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        ImageIcon loginIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(loginIcon);
        imageLabel.setBounds(50, 40, 200, 200);
        add(imageLabel);

        setSize(640, 300);
        setLocation(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
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
