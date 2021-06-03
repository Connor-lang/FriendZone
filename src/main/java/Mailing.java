/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Windows 10
 */
public class Mailing {

    private static int code;

    public static void sendMail(String recipient) throws MessagingException {
        Properties properties = new Properties();

//        properties.put("mail.setup.auth","true");
//        properties.put("mail.setup.starttls.enable", "true");
//        properties.put("mail.setup.starttls.host", "localhost");
//        properties.put("mail.setup.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        String myAccountEmail = "friendzoneds2020@gmail.com";
        String myPassword = "friendzone2020ds@";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, myPassword);
            }
        });

        Message message = prepareMessage(session, myAccountEmail, recipient);
        Transport.send(message);
        System.out.println("Message sent successfully");
        System.out.print("Please enter the code :");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        while (input != code) {
            System.out.print("Wrong authentication code, Please Enter again : ");
            input = sc.nextInt();
        }

    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myAccountEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject("From FriendZone customer service");
        Random r = new Random();
        code = 0;
        for (int i = 0; i < 6; i++) {
            int num = r.nextInt(10);
            code = code * 10 + num;

        }
        message.setText("The code to login is :" + code);
        return message;
    }
}
