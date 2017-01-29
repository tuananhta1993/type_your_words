package wad.service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import org.springframework.stereotype.Service;

@Service
public class SendHTMLEmail {
    private String host;
    private String user;
    private String pass;
    private String port;

    public SendHTMLEmail() {
        this.host = "smtp.gmail.com";
        this.user = "tatuananh.fi@gmail.com";
        this.pass = "lkkszsdrqmspfxnd";
        this.port = "587";
    }
    
    public void send_mail(String from, String to, String subject, String body) {
      // Get system properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
          });

      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
         message.setSubject(subject);

         // Send the actual HTML message, as big as you like
         message.setContent(body, "text/html");

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }
   }
}