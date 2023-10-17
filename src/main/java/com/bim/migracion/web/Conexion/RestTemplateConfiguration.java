package com.bim.migracion.web.Conexion;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
	
	@Autowired
	CloseableHttpClient httpClient;
	
	@Bean
	public RestTemplate restTemplate() {
	    RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
	    return restTemplate;
	}
	
	@Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }
}
