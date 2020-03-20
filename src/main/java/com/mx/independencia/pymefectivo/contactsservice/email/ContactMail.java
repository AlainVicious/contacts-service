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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mx.independencia.pymefectivo.contactsservice.configuration.EMailParameters;

@Component
public class ContactMail {
	
	private static final Logger log = LoggerFactory.getLogger(ContactMail.class);
	
	@Autowired
    private EMailParameters params;

    public boolean sendMailToGmailSSL(String name, String respondTo, String topic, String question) {
    	Properties prop = new Properties();
		prop.put("mail.smtp.host", params.getHost());
        prop.put("mail.smtp.port", Integer.parseInt(params.getPort()));
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.ssl.checkserveridentity", true);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
        			@Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(params.getUser(), params.getPass());
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(params.getMailTo())
            );
            message.setReplyTo( InternetAddress.parse(respondTo));
            message.setSubject(topic, "UTF-8");
            
            message.setText(("##Name## escribió:\n\n"
            		+ question).replace("##Name##", name), "UTF-8");

            Transport.send(message);

            if (log.isTraceEnabled()) {
				log.trace("Se envió el correo de contacto exitosamente");
			}
        } catch (MessagingException e) {
        	log.warn("No se envió el correo de contacto: {}", e.getMessage());
            return false;
        }
        return true;
    }
}
