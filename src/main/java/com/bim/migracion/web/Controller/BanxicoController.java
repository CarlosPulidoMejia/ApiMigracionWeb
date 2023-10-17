package com.bim.migracion.web.Controller;

import javax.net.ssl.SSLHandshakeException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
public class BanxicoController {

	@GetMapping("test")
    public ResponseEntity<String> callExternalApi() throws SSLHandshakeException {
		ResponseEntity<String> response = null;
		String theUrl = "https://www.banxico.org.mx/SieAPIRest/service/v1/series/SF43773/datos/oportuno";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Bmx-Token", "ca4f34a0a65b494a22bfc03f7089cad5a2315e71d422e5a8b5b21bb15d650851");
		HttpEntity<Object> entity=new HttpEntity<Object>(headers);

		
		RestTemplate restTemplate = new RestTemplate();
		response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, String.class);
		

        return response;
    }
}
