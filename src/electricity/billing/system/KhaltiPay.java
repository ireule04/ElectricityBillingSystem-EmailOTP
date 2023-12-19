package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import okhttp3.*;

import okhttp3.MediaType;

public class KhaltiPay extends JFrame implements ActionListener {

    private String meter_no;
    private JButton payButton;
    private JButton back;

    KhaltiPay(String meter) {
        this.meter_no = meter;

        payButton = new JButton("Pay with Khalti");
        payButton.setBounds(300, 300, 200, 50);
        payButton.addActionListener(this);

        back = new JButton("Back");
        back.setBounds(640, 20, 80, 30);
        back.addActionListener(this);

        add(payButton);
        add(back);

        setSize(800, 600);
        setLocation(400, 150);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(back)) {
            setVisible(false);
            PayBill payBill = new PayBill(meter_no);
        } else if (ae.getSource().equals(payButton)) {
            try {
                JSONObject paymentData = new JSONObject();
                paymentData.put("public_key", "test_public_key_e6c4048086fd4fdfa1706c394da206cf"); // Replace with your own public key
                paymentData.put("amount", "20"); // Replace with the actual amount
                paymentData.put("product_identity", "Electricity Bill Payment");
                paymentData.put("product_name", "Electricity Bill");

                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, paymentData.toJSONString());

                Request request = new Request.Builder()
                        .url("https://khalti.com/api/v2/payment/initialize/")
                        .addHeader("Authorization", "test_secret_key_1e31dfeb38274dd79e992b8053878ab9") // Replace with your secret key
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String responseData = response.body().string();

                JSONParser parser = new JSONParser();
                JSONObject jsonResponse = (JSONObject) parser.parse(responseData);

                String paymentURL = (String) jsonResponse.get("redirect");
                Desktop.getDesktop().browse(new URI(paymentURL));

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new KhaltiPay("");
    }
}
