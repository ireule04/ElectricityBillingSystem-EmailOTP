package electricity.billing.system;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class OutlineDetection {

    private Connection connection;
    private Statement statement;
    private JTable table;

    public OutlineDetection() throws SQLException {
        // Connect to the database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebs", "root", "");

        // Create a statement object
        statement = connection.createStatement();

        // Create a JTable object to display the results of the query
        table = new JTable();

        // Add the JTable object to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Create a JButton object to start the outline detection process
        JButton startButton = new JButton("Start Outline Detection");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the outline detection process
                detectOutlineChanges();
            }
        });

        // Add the JButton object to the frame
        JFrame frame = new JFrame("Outline Detection");
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(startButton, BorderLayout.SOUTH);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

private void detectOutlineChanges() {
    try {
        // Query the database for all of the customers
        ResultSet resultSet = statement.executeQuery("SELECT * FROM customer");

        // Iterate over the results of the query
        while (resultSet.next()) {
            // Get the customer's meter number
            String meterNumber = resultSet.getString("meter_no");

            // Get the customer's old address
            String oldAddress = getOldAddress(meterNumber);

            // Check if the customer's address has changed
            String newAddress = resultSet.getString("address");
            if (!newAddress.equals(oldAddress)) {
                // The customer's address has changed, so generate an alert
                System.out.println("Alert: Customer " + meterNumber + " has moved to a new address.");
            }
        }

        // Close the ResultSet object after the loop
        resultSet.close();
    } catch (SQLException e) {
        // Handle the SQLException exception
        System.out.println("Error: " + e.getMessage());
    } finally {
        // Close the database connection
        try {
            connection.close();
        } catch (SQLException e) {
            // Ignore the exception
        }
    }
}


  private String getOldAddress(String meterNumber) throws SQLException {
    // Query the database for the customer's old address
    ResultSet resultSet = statement.executeQuery("SELECT address FROM customer WHERE meter_no = '" + meterNumber + "'");

    // Get the customer's old address
    String oldAddress = "";
    if (resultSet.next()) {
        oldAddress = resultSet.getString("address");
    }

    // Do NOT close the ResultSet object here

    return oldAddress;
}



    public static void main(String[] args) throws SQLException {
        new OutlineDetection();
    }
}
