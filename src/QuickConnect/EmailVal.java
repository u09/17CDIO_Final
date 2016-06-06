package QuickConnect;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailVal {
    
//    public static void main(String[] args){
//    	sendFromGMail("samilesma", Kode, "samilesma@gmail.com", "Sa","Sa");
//    }

    public String getCode() {
    	int digit1 = (int) ((Math.random()*10));
    	int digit2 = (int) ((Math.random()*10));
    	int digit3 = (int) ((Math.random()*10));
    	int digit4 = (int) ((Math.random()*10));
    	String code = ""+digit1+""+digit2+""+digit3+""+digit4; 
    	return code;
    }

    public void sendFromGMail(String from, String pass, String to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gigahost.dk";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
        	message.setFrom(new InternetAddress(from));
        	InternetAddress[] toAddress = new InternetAddress[1];
        	toAddress[0] = new InternetAddress(to);
        	message.addRecipient(Message.RecipientType.TO, toAddress[0]);

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
    
    public String getMail(){
    	return mail;
    }
    public String getPass(){
    	return pass;
    }
}


