package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

public class AdminPanel extends JPanel {
    private JTextArea noticeTextArea;
    private JButton postNoticeButton;

    public AdminPanel() {
        setLayout(new BorderLayout());

        // Create a panel for notice posting
        JPanel postingPanel = new JPanel();
        postingPanel.setLayout(new BorderLayout());

        JLabel postLabel = new JLabel("Post Notice:");
        noticeTextArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(noticeTextArea);
        postNoticeButton = new JButton("Post Notice");

        // Add action listener for posting a notice
        postNoticeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String noticeText = noticeTextArea.getText();
                if (!noticeText.isEmpty()) {
                    try {
                        String fileName = generateUniqueFileName();
                        FileWriter writer = new FileWriter("notices/" + fileName + ".txt");
                        writer.write(noticeText);
                        writer.close();
                        JOptionPane.showMessageDialog(null, "Notice posted successfully!");
                        noticeTextArea.setText(""); // Clear the text area after posting
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a notice to post.");
                }
            }
        });

        postingPanel.add(postLabel, BorderLayout.NORTH);
        postingPanel.add(scrollPane, BorderLayout.CENTER);
        postingPanel.add(postNoticeButton, BorderLayout.SOUTH);

        // Add postingPanel to the admin panel
        add(postingPanel, BorderLayout.CENTER);
    }

    private String generateUniqueFileName() {
        long timestamp = System.currentTimeMillis();
        return "notice_" + timestamp;
    }
}
