package com.bim.migracion.web.Banxico;

import java.net.HttpURLConnection;
import java.net.URL;

public class ApiBanxico2 {

	public static void main(String[] args) {
		System.out.println("123");
		try {
			URL url = new URL(
					"https://www.banxico.org.mx/SieAPIRest/service/v1/series/SF43936/datos/2011-05-01/2011-05-31");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// Se realiza una petición GET
			conn.setRequestMethod("GET");
			// Se solicita que la respuesta esté en formato JSON
			conn.setRequestProperty("Content-Type", "application/json");
			// Se envía el header Bmx-Token con el token de consulta
			// Modificar por el token de consulta propio
			conn.setRequestProperty("Bmx-Token", "ca4f34a0a65b494a22bfc03f7089cad5a2315e71d422e5a8b5b21bb15d650851");

			// En caso de ser exitosa la petición se devuelve un estatus HTTP 200
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
			}

			System.out.println(conn.getResponseCode());

			// Se utiliza Jackson para mapear el JSON a objetos Java
//                ObjectMapper mapper = new ObjectMapper();
			// TasaInteresBancariaResponse response=mapper.readValue(conn.getInputStream(),
			// TasaInteresBancariaResponse.class);

			conn.disconnect();

		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}

}
