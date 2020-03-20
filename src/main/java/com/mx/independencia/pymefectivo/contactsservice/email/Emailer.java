package com.mx.independencia.pymefectivo.contactsservice.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.google.common.net.MediaType;

import com.mx.independencia.pymefectivo.contactsservice.configuration.EMailParameters;

@Component
public class Emailer {
	
	private static final Logger log = LoggerFactory.getLogger(Emailer.class);
	private static final String SEND_REPORT_TEXT_BODY = "Se anexa la lista de registros generados.";
	private static final String ENCODING = "UTF-8";

	@Autowired
    private EMailParameters params;
	
	private Session initConection() {
    	Properties prop = new Properties();
		prop.put("mail.smtp.host", params.getHost());
        prop.put("mail.smtp.port", Integer.parseInt(params.getPort()));
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.ssl.checkserveridentity", true);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return Session.getInstance(prop,
                new javax.mail.Authenticator() {
        			@Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(params.getUser(), params.getPass());
                    }
                });
	}

    public boolean sendMailToGmailSSL(String name, String respondTo, String topic, String question) {
        
        MimeMessage message = new MimeMessage(initConection());
        try {
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(params.getMailTo())
            );
            message.setReplyTo( InternetAddress.parse(respondTo));
            message.setSubject(topic, ENCODING);
            
            message.setText(("##Name## escribió:\n\n"
            		+ question).replace("##Name##", name), ENCODING);

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
    
    public boolean sendNewRegisterMail(String name, String email, String telefono, String cp) {
        MimeMessage message = new MimeMessage(initConection());
        try {
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(params.getMailTo())
            );
            message.setSubject("Un nuevo registro", ENCODING);
            message.setText(("Se creó el siguiente registro:\n\n"
            		+ "Nombre: ##NAME##\n"
            		+ "E-mail: ##EMAIL##\n"
            		+ "Teléfono: ##TEL##\n"
            		+ "Código postal: ##CP##")
            		.replace("##NAME##", name)
            		.replace("##EMAIL##", email)
            		.replace("##TEL##", telefono)
            		.replace("##CP##", cp), ENCODING
            		);
            Transport.send(message);

            if (log.isTraceEnabled()) {
				log.trace("Se envió el correo de registro exitosamente");
			}
        } catch (MessagingException e) {
        	log.warn("No se envió el correo de registro: {}", e.getMessage());
            return false;
        }
        return true;
    }

    
    public boolean sendLeadsReport(ByteArrayOutputStream attachment) {
        
        MimeMessage message = new MimeMessage(initConection());
        InputStream file = new ByteArrayInputStream(attachment.toByteArray());
        try {
            MimeMessageHelper builder = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
        	builder.setTo(InternetAddress.parse(params.getMailTo()));
        	builder.setSubject("Listado de registros");
        	builder.setText(SEND_REPORT_TEXT_BODY);        	
//        	builder.addAttachment("file.csv", new ByteArrayDataSource(file, MediaType.PLAIN_TEXT_UTF_8.toString()));
            message.setSubject("Listado de registros", ENCODING);
//            
//            message.setText(SEND_REPORT_TEXT_BODY, ENCODING);
//            System.out.println("======================= Attachment =================================");
//            System.out.println(attachment.toString());
//            System.out.println("======================= Fin =================================\n\n");
        	builder.addAttachment("file.csv", new ByteArrayDataSource(file, MediaType.PLAIN_TEXT_UTF_8.toString()));
          
        	Transport.send(message);
            if (log.isTraceEnabled()) {
				log.trace("Se envió el correo de contacto exitosamente");
			}
        } catch (MessagingException e) {
        	log.warn("No se envió el correo de contacto: {}", e.getMessage());
            return false;
        } catch (IOException e) {
        	log.warn("No se envió el correo, error en attachment: {}", e.getMessage());
		}
        return true;
    }

}
