package com.mx.independencia.pymefectivo.contactsservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import com.mx.independencia.pymefectivo.contactsservice.models.*;
import com.mx.independencia.pymefectivo.contactsservice.recaptcha.ReCaptchaService;
import com.mx.independencia.pymefectivo.contactsservice.service.ContactService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class WebController {

	private static final Logger log = LoggerFactory.getLogger(WebController.class);

	@Autowired
	ContactService contactService;

	@PostMapping(value = "/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> contactForm(@RequestBody ContactRequest data) {
		// try {
		// 	if (data.isValid() && ReCaptchaService.isValid(data.getgRecaptcha())) {
				try {
					String resp = contactService.doMagic(data);

					if (resp != "ok") {
						return new ResponseEntity<>("{\"status\":\"" + resp + "\"}", HttpStatus.BAD_REQUEST);
					} else {
						return new ResponseEntity<>("{\"status\":\"Ok\"}", HttpStatus.OK);
					}
				} catch (Exception e) {
					return new ResponseEntity<>("{\"status\":\"Errror\", \"mensaje\":\"" + e.getMessage() + "\"}",
							HttpStatus.BAD_REQUEST);
				}
		// 	}
		// } catch (

		// IOException e) {
		// 	log.warn("Error al validar reCaptcha");
		// 	return new ResponseEntity<>("{\"status\":\"Server error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		// }
		// return new ResponseEntity<>("{\"status\":\"Error en el envío de parámetros, parámetros erroneos\"}",
		// 		HttpStatus.BAD_REQUEST);
	}

}
