package electricity.billing.system;


import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class ViewInformation extends JFrame implements ActionListener{
    
    
    JButton cancel;
    
    
      ViewInformation(String meter) {
        setBounds(350, 150, 850, 550);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setLayout(null);
        
        
        JLabel heading = new JLabel("MY INFORMATION");
        heading.setBounds(250, 0, 500, 40);
        heading.setFont(new Font("DialogInput", Font.BOLD, 20));
        Font boldFont = new Font("DialogInput", Font.BOLD, 20);
        heading.setFont(boldFont);
        add(heading);
        
        JLabel lblname = new JLabel("Name");
        lblname.setBounds(70, 80, 100, 20);
        add(lblname);
        
        JLabel name = new JLabel("");
        name.setBounds(250, 80, 150, 20);
        add(name);
        
        JLabel lblmeternumber = new JLabel("Meter Number");
        lblmeternumber.setBounds(70, 140, 100, 20);
        add(lblmeternumber);
        
        JLabel meternumber = new JLabel("");
        meternumber.setBounds(250, 140, 100, 20);
        add(meternumber);
        
        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(70, 200, 100, 20);
        add(lbladdress);
        
        JLabel address = new JLabel("");
        address.setBounds(250, 200, 100, 20);
        add(address);
        
        JLabel lblcity = new JLabel("City");
        lblcity.setBounds(70, 260, 100, 20);
        add(lblcity);
        
        JLabel city = new JLabel("");
        city.setBounds(250, 260, 100, 20);
        add(city);
        
      
        
        JLabel lblphone = new JLabel("Phone");
        lblphone.setBounds(500, 80, 100, 20);
        add(lblphone);
        
        JLabel phone = new JLabel("");
        phone.setBounds(650, 80, 100, 20);
        add(phone);
        
        JLabel lblemail = new JLabel("Email");
        lblemail.setBounds(500, 140, 100, 20);
        add(lblemail);
        
        JLabel email = new JLabel("");
        email.setBounds(650, 140, 200, 20);
        add(email);
        
       


        
        JLabel lblstate = new JLabel("State");
        lblstate.setBounds(500, 200, 100, 20);
        add(lblstate);
        
        JLabel state = new JLabel("");
        state.setBounds(650, 200, 100, 20);
        add(state);
        
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from customer where meter_no = '"+meter+"'");
            while(rs.next()) {
                name.setText(rs.getString("name"));
                address.setText(rs.getString("address"));
                city.setText(rs.getString("city"));
                state.setText(rs.getString("state"));
                email.setText(rs.getString("email"));
                phone.setText(rs.getString("phone"));
                meternumber.setText(rs.getString("meter_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        cancel = new JButton("Exit");
        cancel.setBackground(Color.red);
        cancel.setForeground(Color.white);
        cancel.setBounds(300,340,100,30);
        add(cancel);
        cancel.addActionListener(this);
        
     
        setVisible(true);
    
       }
    
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
    }
    public static void main(String[] args) {
         new ViewInformation("");
         
    }
}
