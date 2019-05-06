package HybridFramework.mail;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {

	Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;
	String emailPort;
	String emailHost;
	static String[] toEmails = {"ramis_tahmasebi@outlook.com"}; //{ "its.thakur@gmail.com","sejalbhayana@gmail.com" }; //who we sending email
	static String fromUser = "testingzalt@gmail.com";//just the id alone without @gmail.com
	static String password = "Salty123";
	
	public void setMailServerProperties() {

		emailPort = "587";//gmail's smtp port
		emailHost = "smtp.gmail.com";
		emailProperties = System.getProperties();
//		
		
		emailProperties.put("mail.smtp.starttls.enable", "true");
		emailProperties.put("mail.smtp.host", emailHost);
		emailProperties.put("mail.smtp.user", fromUser);
		emailProperties.put("mail.smtp.password", password);
		emailProperties.put("mail.smtp.port", emailPort); 
		emailProperties.put("mail.smtp.auth", "true");
		
//		mailSession = Session.getInstance(emailProperties,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(fromUser, password);
//                    }
//                });

//		emailProperties.put("mail.smtp.port", emailPort);
////		emailProperties.put("mail.smtp.socketFactory.port", emailPort);
////		emailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//
//		emailProperties.put("mail.smtp.auth", "true");
//		emailProperties.put("mail.smtp.starttls.enable", "true");
//		
//		emailProperties.put("mail.smtp.auth", "true");
//		emailProperties.put("mail.smtp.starttls.enable", "true");
//		emailProperties.put("mail.smtp.host", "smtp.gmail.com");
//		emailProperties.put("mail.smtp.port", "587"); 



	}

	public void createEmailMessage(
			String emailSubject,
			String emailBody,
			String attachmentPath,
			String attachmentName,
			String toEmails[]
			) throws AddressException,
			MessagingException {
		
		//String emailSubject = "Java Email";
		//String emailBody = "Please find the reports in attachment.";

		this.mailSession = Session.getDefaultInstance(emailProperties);
		this.mailSession.setDebug(true);
		this.emailMessage = new MimeMessage(mailSession);

		for (int i = 0; i < toEmails.length; i++) {
			this.emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
		}

		this.emailMessage.setSubject(emailSubject);
		DataSource source = new FileDataSource(attachmentPath);
		this.emailMessage.setDataHandler(new DataHandler(source));
		this.emailMessage.setFileName(attachmentName);
  //      emailMessage.setText(emailBody);// for a text email

	}

	public void sendEmail(String fromUser,String fromUserEmailPassword) throws AddressException, MessagingException {

		 
		

		Transport transport = mailSession.getTransport("smtp");

		transport.connect(emailHost, fromUser, fromUserEmailPassword);
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully.");
	}

}