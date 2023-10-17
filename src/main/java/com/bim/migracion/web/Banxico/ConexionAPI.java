package com.bim.migracion.web.Banxico;

import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class ConexionAPI {

	private static HttpHeaders createHttpHeaders(String user, String password)
	{
	    String notEncoded = user + ":" + password;
	    String encodedAuth = Base64.getEncoder().encodeToString(notEncoded.getBytes());
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("Authorization", "Basic " + encodedAuth);
	    return headers;
	}

	private static void doYourThing() 
	{
	    String theUrl = "https://www.banxico.org.mx/SieAPIRest/service/v1/series/SF43773/datos/oportuno";
	    RestTemplate restTemplate = new RestTemplate();
	    try {
	        HttpHeaders headers = createHttpHeaders("Bmx-Token","ca4f34a0a65b494a22bfc03f7089cad5a2315e71d422e5a8b5b21bb15d650851");
	        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	        ResponseEntity<String> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, String.class);
	        System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
	    }
	    catch (Exception eek) {
	        System.out.println("** Exception: "+ eek.getMessage());
	    }
	}
	
	public static void main(String args[]) {
		doYourThing();
	}
	
}
