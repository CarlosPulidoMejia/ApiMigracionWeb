package com.bim.migracion.web.Banxico;

import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;




public class ApiBanxico {
	public static ResponseBmx readSeries() throws Exception {

		//La URL a consultar con los parametros de idSerie y fechas 
		URL url = new URL("https://www.banxico.org.mx/SieAPIRest/service/v1/series/SF43936/datos/2011-05-01/2011-05-31");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//Se realiza una petición GET
		conn.setRequestMethod("GET");
		//Se solicita que la respuesta esté en formato JSON
		conn.setRequestProperty("Content-Type", "application/json");
		//Se envía el header Bmx-Token con el token de consulta
		//Modificar por el token de consulta propio
		conn.setRequestProperty("Bmx-Token", "dc025d6c107d96fab34578468dd6c904dd899cbc7951b2355e2567c5ce3763df");

		//En caso de ser exitosa la petición se devuelve un estatus HTTP 200
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("HTTP error code : "+ conn.getResponseCode());
		}

		//Se utiliza Jackson para mapear el JSON a objetos Java
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unused")
		ResponseBmx response=mapper.readValue(conn.getInputStream(), ResponseBmx.class);

		conn.disconnect();

		System.out.println("TEST");
		conn.disconnect();
		return null;

	}

	public static void main(String[] args) {
		try {
			ResponseBmx response=readSeries();
			SerieBmx serie=response.getBmx().getSeries().get(0);
			System.out.println("Serie: "+serie.getTitulo());
			for(DataSerieBmx data:serie.getDatos()){
				//Se omiten las observaciones sin dato (N/E)
				if(data.getDato().equals("N/E")) continue;
				System.out.println("Fecha: "+data.getFecha());
				System.out.println("Dato: "+data.getDato());
			}
			
		} catch(Exception e) {
			System.out.println("ERROR: "+e.getMessage());
		}
	}
}
