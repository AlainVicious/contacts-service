package com.mx.independencia.pymefectivo.contactsservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mx.independencia.pymefectivo.contactsservice.email.ContactMail;
import com.mx.independencia.pymefectivo.contactsservice.repository.ContactRepository;
import com.mx.independencia.pymefectivo.contactsservice.models.Contact;

@Service
public class ContactServiceImplementation implements ContactService {
	
	private static final Logger log = LoggerFactory.getLogger(ContactServiceImplementation.class);
	@Autowired
	private ContactRepository repo;
	@Autowired
	private ContactMail mailer;

	@Override
	public String doMagic(Contact data) {
		// if (log.isTraceEnabled()) {
		// 	log.trace(data.toString());
		// }
	// 	repo.create(data);
	// 	mailer.sendMailToGmailSSL(data.getName(), data.getEmail(), data.getTopic(), data.getMessage());
	// 
	String resp = "";
		if (log.isTraceEnabled()) {
			log.trace(data.toString());
		}
		resp = repo.create(data);
		mailer.sendMailToGmailSSL(data.getName(), data.getEmail(), data.getTopic(), data.getMessage());
		return resp;
}
}
