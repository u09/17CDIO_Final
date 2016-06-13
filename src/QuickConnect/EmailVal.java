package QuickConnect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailVal {

	private String mail;
	private String pass;

	private String server(int i) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("mail.txt"));
			mail = br.readLine();
			pass = br.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null)
					br.close();
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		if(i == 0)
			return mail;
		else return pass;
	}

	public String getCode() {
		int digit1 = (int) ((Math.random() * 10));
		int digit2 = (int) ((Math.random() * 10));
		int digit3 = (int) ((Math.random() * 10));
		int digit4 = (int) ((Math.random() * 10));
		String code = "" + digit1 + "" + digit2 + "" + digit3 + "" + digit4;
		return code;
	}

	public void sendMail(String from, String pass, String to, String subject, String body) {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
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
		} catch(AddressException ae) {
			ae.printStackTrace();
		} catch(MessagingException me) {
			me.printStackTrace();
		}
	}

	public String getMail() {
		return server(0);
	}

	public String getPass() {
		return server(1);
	}

}