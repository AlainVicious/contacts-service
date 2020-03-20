package com.mx.independencia.pymefectivo.contactsservice.recaptcha;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReCaptchaService {

	private static final String RECAPTCHA_SERVICE_URL = "https://www.google.com/recaptcha/api/siteverify";
	private static final String SECRET_KEY = "6LeIr88UAAAAAEvW1vEto7gjLNGq6ZpMs2q7BLmV";
	
	private static final Logger log = LoggerFactory.getLogger(ReCaptchaService.class);

	
	private ReCaptchaService() {
		super();
	}

	public static boolean isValid(String clientRecaptchaResponse) throws IOException {
		if (clientRecaptchaResponse == null || clientRecaptchaResponse.trim().equals("")) {
			return false;
		}
		
		URL url = new URL(RECAPTCHA_SERVICE_URL);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		//add client result as post parameter
		String postParams =
				"secret=" + SECRET_KEY +
				"&response=" + clientRecaptchaResponse;

		// send post request to google recaptcha server
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		
		if (log.isTraceEnabled()) {
			log.trace("Post parameters: {}", postParams);
			log.trace("Response Code: {}", responseCode);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		if (log.isTraceEnabled()) {
			log.trace(response.toString());
		}

		//Parse JSON-response
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(response.toString());
		
		Boolean success = json.get("success").asBoolean();
		if (log.isTraceEnabled()) {
			log.trace("success : {}", success);
		}

		//result should be sucessfull and spam score above 0.5
		return (success);
	}
}
