package electricity.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.ArrayList;
import net.proteanit.sql.DbUtils;

public class CustomerDetails extends JFrame implements ActionListener {

    private JTable table;
    private JButton print, delete;

    public CustomerDetails() {
        super("Customer Details");
        setSize(1200, 650);
        setLocation(200, 150);
        
        getContentPane().setBackground(Color.GRAY);

        table = new JTable();

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM customer");

            table.setModel(DbUtils.resultSetToTableModel(rs));

            // Add a column for the delete button
            TableColumn col = new TableColumn();
            col.setHeaderValue("Delete");
            table.addColumn(col);
            table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new ButtonEditor(new JCheckBox()));

            // sort function
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);

            // ascending order name-wise sort
            ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane sp = new JScrollPane(table);
        add(sp);

        print = new JButton("Print Customer List");
        print.addActionListener(this);
        add(print, BorderLayout.SOUTH);

//        delete = new JButton("Delete Customer");
//        delete.addActionListener(this);
//        add(delete, BorderLayout.SOUTH);
//
     setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == delete) {
            int row = table.getSelectedRow();
            if (row != -1) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this customer?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        String meter_no = table.getValueAt(row, 1).toString(); // Assuming meter_no is in the second column
                        Conn c = new Conn();
                        String query = "DELETE FROM customer WHERE meter_no = ?";
                        PreparedStatement pstmt = c.c.prepareStatement(query);
                        pstmt.setString(1, meter_no);
                        int deletedRows = pstmt.executeUpdate();
                        if (deletedRows > 0) {
                            DefaultTableModel model = (DefaultTableModel) table.getModel();
                            model.removeRow(row);
                            JOptionPane.showMessageDialog(null, "Customer deleted successfully.");
                            pstmt.close(); // Close the prepared statement
                            c.c.close(); // Close the database connection
                        } else {
                            JOptionPane.showMessageDialog(null, "Customer Deletion Failed!!");
                            pstmt.close(); // Close the prepared statement
                            c.c.close(); // Close the database connection
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a customer to delete.");
            }
        }
    }

    // Inner class for rendering and editing delete button
    class ButtonRenderer extends DefaultTableCellRenderer {
        private JButton deleteButton;

        public ButtonRenderer() {
            deleteButton = new JButton("Delete");
            deleteButton.setOpaque(true);
            deleteButton.setForeground(Color.RED);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            return deleteButton;
        }
    }

   class ButtonEditor extends DefaultCellEditor {
    private JButton deleteButton;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                if (row != -1) {
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this customer?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        try {
                            String meter_no = table.getModel().getValueAt(row, 1).toString(); //  meter_no is in the second column
                            Conn c = new Conn();
                            String query = "DELETE FROM customer WHERE meter_no = ?"; // select table from ebs
                            PreparedStatement pstmt = c.c.prepareStatement(query);
                            pstmt.setString(1, meter_no);
                            int deletedRows = pstmt.executeUpdate();
                            if (deletedRows > 0) {
                                DefaultTableModel model = (DefaultTableModel) table.getModel();
                                model.removeRow(row);
                                JOptionPane.showMessageDialog(null, "Customer deleted successfully.");
                                pstmt.close(); // Close the prepared statement
                                c.c.close(); // Close the database connection
                            } else {
                                JOptionPane.showMessageDialog(null, "Customer Deletion Failed!!");
                                pstmt.close(); // Close the prepared statement
                                c.c.close(); // Close the database connection
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a customer to delete.");
                }
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return deleteButton;
    }
}

    

    public static void main(String[] args) {
        new CustomerDetails();
    }
}
