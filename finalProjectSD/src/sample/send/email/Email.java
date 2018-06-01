package sample.send.email;

import data.server.model.Driver;
import data.server.model.Fine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by plesha on 16-May-18.
 */
public class Email {

    private static final String username = "fines.management1@gmail.com";
    private static final String password = "Management1";

    public static void sendEmail(Driver driver, Fine fine){

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("fines.management1@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse( driver.getEmail() )); // address to send

            message.setSubject("Fine payment registered !");
            message.setText("Dear " + driver.getName()+ ","
                    + "\n\nYour fine payment was successfully registered ! \n"+
                    "Next time please be more careful. \n\n\n"+
                    "Fine Details_________________________\n" +
                    "Date Crime Committed: " + fine.getDateFineCommited() + "\n"+
                    "Payment Deadline Date: " + fine.getDeadlineDate() + "\n" +
                    "Crime Committed: " + fine.getCrimeType()+ "\n"+
                    "Fine price: " + fine.getPrice() + " lei\n"+
                    "Fine points penalty: " + fine.getPoints() + "\n" +
                    "____________________________________\n\n\n" +
                    "Drive carefully, Police Office Employee" );

            Transport.send(message);

            System.out.println("E-mail sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
