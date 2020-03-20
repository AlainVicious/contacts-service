package com.mx.independencia.pymefectivo.contactsservice.models;

import java.util.regex.Pattern;

import com.mx.independencia.pymefectivo.contactsservice.models.Contact;

public class ContactRequest extends Contact {

	private static final long serialVersionUID = -7306024557450469004L;
	private String gRecaptcha;

	public ContactRequest() {
		super();
	}
	public String getgRecaptcha() {
		return gRecaptcha;
	}
	public void setgRecaptcha(String gRecaptcha) {
		this.gRecaptcha = gRecaptcha;
	}
	@Override
	public String toString() {
		return super.toString() + "\nContactRequest [gRecaptcha=" + gRecaptcha + "]";
	}
	public boolean isValid() {
		return super.getName() != null &&
				Pattern.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ ]{1,250}", getName()) &&
				getEmail() != null &&
				Pattern.matches("([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}){1,250}", getEmail()) &&
				getTopic() != null &&
				Pattern.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ ]{1,250}", getTopic()) &&
				getMessage() != null &&
				Pattern.matches(".{1,5000}", super.getMessage()) &&
				getgRecaptcha() != null;
	}
}