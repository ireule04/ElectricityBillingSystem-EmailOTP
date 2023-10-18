package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Project extends JFrame implements ActionListener {

    String atype, meter;
    private JPanel photoPanel;
    private JLabel photoLabel;
    private String currentPhotoPath = null;
    
    

    Project(String atype, String meter) {
        getContentPane().setBackground(new Color(102,102,102));
        setLayout(null);
        this.atype = atype;
        this.meter = meter;
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JMenuBar mb = new JMenuBar();
        setJMenuBar(mb);
        
        
        
// Calculate the x-coordinate to place the imageLabel in the top-right corner
//int xCoordinate = getWidth() - 250; // Adjust as needed
//int yCoordinate = 10;
//int labelWidth = 200;
//int labelHeight = 200;
//
//ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon/bblogo.png"));
//Image image = imageIcon.getImage().getScaledInstance(labelWidth, labelHeight, Image.SCALE_DEFAULT);
//ImageIcon loginIcon = new ImageIcon(image);
//JLabel imageLabel = new JLabel(loginIcon);
//
//// Set the bounds for the imageLabel
//imageLabel.setBounds(xCoordinate, yCoordinate, labelWidth, labelHeight);
//
//add(imageLabel); // Add the imageLabel to the frame




        // Admin/master ko POV
        JMenu master = new JMenu("Menu");
        master.setForeground(Color.black);
        
      master.setFont(new Font("Dialog", Font.BOLD, 16));

        JMenuItem newcustomer = new JMenuItem("New Customer");
        newcustomer.setFont(new Font("Dialog", Font.BOLD, 16));
        newcustomer.setMnemonic('N');
        newcustomer.addActionListener(this);
        newcustomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        master.add(newcustomer);

        JMenuItem customerdetails = new JMenuItem("Customer Details");
        customerdetails.setFont(new Font("Dialog", Font.BOLD, 16));
        customerdetails.setMnemonic('C');
        customerdetails.addActionListener(this);
        customerdetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        master.add(customerdetails);

        JMenuItem depositdetails = new JMenuItem("Deposit Details");
        depositdetails.setFont(new Font("Dialog", Font.BOLD, 16));
        depositdetails.setMnemonic('D');
        depositdetails.addActionListener(this);
        depositdetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        master.add(depositdetails);

        JMenuItem calculatebill = new JMenuItem("Calculate Bills");
        calculatebill.setFont(new Font("Dialog", Font.BOLD, 16));
        calculatebill.setMnemonic('B');
        calculatebill.addActionListener(this);
        calculatebill.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        master.add(calculatebill);

        JMenuItem postnotice = new JMenuItem("Post Notice");
        postnotice.setFont(new Font("Dialog", Font.BOLD, 16));
        postnotice.setMnemonic('P');
        postnotice.addActionListener(this);
        postnotice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        master.add(postnotice);

        JMenuItem deleteNotice = new JMenuItem("Delete Notice");
        deleteNotice.setFont(new Font("Dialog", Font.BOLD, 16));
        deleteNotice.setMnemonic('U');
        deleteNotice.addActionListener(this);
        deleteNotice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
        master.add(deleteNotice);

        // Customer Ko POV
        JMenu info = new JMenu("Information");
        info.setForeground(Color.RED);
        info.setFont(new Font("DialogInput", Font.BOLD, 16));
        info.setBounds(320, 160, 100, 25);
        

        JMenuItem updateinformation = new JMenuItem("Update Information");
        updateinformation.setFont(new Font("Dialog", Font.BOLD, 16));
        updateinformation.setMnemonic('U');
        updateinformation.addActionListener(this);
        updateinformation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
        info.add(updateinformation);

        JMenuItem viewinformation = new JMenuItem("View Information");
        viewinformation.setFont(new Font("Dialog", Font.BOLD, 16));
        viewinformation.setMnemonic('I');
        viewinformation.addActionListener(this);
        viewinformation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        info.add(viewinformation);

        JMenu user = new JMenu("User");
        user.setForeground(Color.black);
        user.setFont(new Font("DialogInput", Font.BOLD, 16));

        JMenuItem paybill = new JMenuItem("Pay Bill");
        paybill.setFont(new Font("Dialog", Font.BOLD, 16));
        paybill.setMnemonic('P');
        paybill.addActionListener(this);
        paybill.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        user.add(paybill);

        JMenuItem billdetails = new JMenuItem("Bill Details");
        billdetails.setFont(new Font("Dialog", Font.BOLD, 16));
        billdetails.setMnemonic('H');
        billdetails.addActionListener(this);
        billdetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        user.add(billdetails);

        JMenu report = new JMenu("Report");
        report.setForeground(Color.RED);
        report.setFont(new Font("DialogInput", Font.BOLD, 16));

        JMenuItem generatebill = new JMenuItem("Generate Bill");
        generatebill.setFont(new Font("DialogInput", Font.BOLD, 16));
        generatebill.setMnemonic('G');
        generatebill.addActionListener(this);
        generatebill.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        report.add(generatebill);

        JMenu utility = new JMenu("Others");
        utility.setForeground(Color.BLUE);
        mb.add(utility);
        utility.setFont(new Font("DialogInput", Font.BOLD, 16));

        JMenuItem notepad = new JMenuItem("Note");
        notepad.setFont(new Font("Dialog", Font.BOLD, 16));
        notepad.setMnemonic('O');
        notepad.addActionListener(this);
        notepad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        utility.add(notepad);

        JMenuItem calculator = new JMenuItem("Calculator");
        calculator.setFont(new Font("Dialog", Font.BOLD, 16));
        calculator.setMnemonic('L');
        calculator.addActionListener(this);
        calculator.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        utility.add(calculator);

        JMenu eexit = new JMenu("Exit");
        eexit.setForeground(Color.RED);
        mb.add(eexit);
       
        eexit.setFont(new Font("DialogInput", Font.BOLD, 16));

        JMenuItem exit = new JMenuItem("Exit");
        exit.setFont(new Font("Dialog", Font.BOLD, 16));
        exit.setMnemonic('X');
        exit.addActionListener(this);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        eexit.add(exit);

        if (atype.equals("Admin")) {
            mb.add(master);
        } else {
            mb.add(info);
            mb.add(user);
            mb.add(report);
        }

        mb.add(utility);
        mb.add(eexit);

        createPhotoFrame();
        loadInitialPhoto();

        setLayout(new FlowLayout());
        setVisible(true);
    }

    private void createPhotoFrame() {
        // Create the panel for the photo
        photoPanel = new JPanel();
        photoPanel.setPreferredSize(new Dimension(700, 700));
        photoPanel.setBorder(new TitledBorder(new LineBorder(new Color(51,51,51), 3), "Notice : Bijuli Batti Power Ltd.", TitledBorder.CENTER,
                TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 16), new Color(255,255,255)));
        photoPanel.setBackground(new Color(153,153,153));
        
        
//        JPanel panel = new JPanel();
//        panel.setBounds(30, 30, 650, 400);
//        panel.setBorder(new TitledBorder(new LineBorder(new Color(41, 128, 185), 2), "Notice", TitledBorder.CENTER,
//                TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 16), new Color(41, 128, 185)));
//        panel.setBackground(Color.WHITE);
//        panel.setLayout(null);
//        panel.setForeground(Color.RED);
//        add(panel);
        

        // Create the label for displaying the photo
        photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(600, 600));
        photoPanel.add(photoLabel);

        // Add the photo panel to the main frame
        add(photoPanel);
    }

    private void loadInitialPhoto() {
        // Load the initial photo path from the configuration file
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            currentPhotoPath = properties.getProperty("current_photo_path");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Display the photo if the path is not empty
        if (currentPhotoPath != null && !currentPhotoPath.isEmpty()) {
            displayPhoto(currentPhotoPath);
        }
    }

    private void displayPhoto(String filePath) {
        // Set the new photo
        ImageIcon imageIcon = new ImageIcon(filePath);
        Image image = imageIcon.getImage().getScaledInstance(600, 600, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        photoLabel.setIcon(scaledIcon);

        // Update the current photo path in the configuration file
        updateCurrentPhotoPath(filePath);
    }

    private void updateCurrentPhotoPath(String filePath) {
        // Update the current photo path in the configuration file
        Properties properties = new Properties();
        properties.setProperty("current_photo_path", filePath);
        try (FileOutputStream output = new FileOutputStream("config.properties")) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deletePhoto() {
        if (currentPhotoPath != null) {
            File fileToDelete = new File(currentPhotoPath);
            if (fileToDelete.exists() && fileToDelete.delete()) {
                JOptionPane.showMessageDialog(this, "Photo deleted successfully.");
                photoLabel.setIcon(null);
                currentPhotoPath = null;
                // Update the configuration file to clear the current photo path
                updateCurrentPhotoPath("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the photo.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No photo to delete.");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        String msg = ae.getActionCommand();
        if (msg.equals("New Customer")) {
            new NewCustomer();
        } else if (msg.equals("Customer Details")) {
            new CustomerDetails();
        } else if (msg.equals("Deposit Details")) {
            new DepositDetails();
        } else if (msg.equals("Calculate Bills")) {
            new CalculateBills();
        } else if (msg.equals("View Information")) {
            new ViewInformation(meter);
        } else if (msg.equals("Update Information")) {
            new UpdateInformation(meter);
        } else if (msg.equals("Bill Details")) {
            new BillDetails(meter);
        } else if (msg.equals("Note")) {
            try {
                Runtime.getRuntime().exec("notepad.exe");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (msg.equals("Calculator")) {
            try {
                Runtime.getRuntime().exec("calc.exe");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (msg.equals("Exit")) {
            setVisible(false);
            new Login();
        } else if (msg.equals("Pay Bill")) {
            new PayBill(meter);
        } else if (msg.equals("Generate Bill")) {
            new GenerateBill(meter);
        } else if (msg.equals("Post Notice")) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                displayPhoto(filePath);
            }
        } else if (msg.equals("Delete Notice") && atype.equals("Admin")) {
            deletePhoto();
        }
    }

    public static void main(String[] args) {
        new Project("", "");
    }
}
