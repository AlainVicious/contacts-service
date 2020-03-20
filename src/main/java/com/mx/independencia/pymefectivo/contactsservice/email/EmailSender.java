package com.mx.independencia.pymefectivo.contactsservice.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailSender {
	
	private static final Logger log = LoggerFactory.getLogger(EmailSender.class);

	private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "atn_servpyme@independencia.com.mx";
    private static final String PASSWORD = "Findep200201";

    private static final String EMAIL_TO = "out_fcamachol@independencia.com.mx, out_rguerrerop@independencia.com.mx, out_djimenezj@masnomina.com.mx, out_lpisilc@independencia.com.mx";
    // private static final String EMAIL_TO = "sruiz@ia.com.mx";

    private static final String EMAIL_SUBJECT = "Testing Gmail TLS";
    private static final String EMAIL_TEXT = "Dear Mail Crawler,"
            + "\n\n Please do not spam my email!";
    
    public boolean sendMailToGmailTLS() {
    	Properties prop = new Properties();
		prop.put("mail.smtp.host", SMTP_SERVER);
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
        			@Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO)
            );
            message.setSubject(EMAIL_SUBJECT);
            message.setText(EMAIL_TEXT);

            Transport.send(message);

            if (log.isTraceEnabled()) {
				log.trace("Done");
			}
        } catch (MessagingException e) {
        	log.warn("No se envió el correo: {}", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean sendMailToGmailSSL() {
    	Properties prop = new Properties();
		prop.put("mail.smtp.host", SMTP_SERVER);
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.ssl.checkserveridentity", true);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
        			@Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO)
            );
            message.setSubject(EMAIL_SUBJECT);
            message.setText(EMAIL_TEXT);

            Transport.send(message);

            if (log.isTraceEnabled()) {
				log.trace("Done");
			}
        } catch (MessagingException e) {
        	log.warn("No se envió el correo: {}", e.getMessage());
            return false;
        }
        return true;
    }
}
