package electricity.billing.system;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    private static final int THRESHOLD = 100; // Set your own threshold

    public void enterData(String userEmail, int units) {
        if (units > THRESHOLD) {
            sendEmail(userEmail);
        }
    }

    private void sendEmail(String to) {
        String from = "mirazeacademy@gmail.com";
        String host = "imap.gmail.com"; // Your SMTP server

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("High Electricity Consumption Warning");
            message.setText("Your electricity consumption is higher than usual.");

            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
