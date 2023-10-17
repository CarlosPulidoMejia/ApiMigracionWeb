package com.bim.migracion.web.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bim.migracion.web.Request.ContingenciaRequest;
import com.bim.migracion.web.Response.GenerarEnvResponse;
import com.bim.migracion.web.Service.ContingenciasService;

@RestController
@CrossOrigin("*")
public class ContingenciasController {
	
	@Autowired
	private ContingenciasService contingenciasService;

	@PostMapping("/api/bim/contingencia/generarEnv")
	public ResponseEntity<byte[]> generarEnvTest(@RequestBody ContingenciaRequest continRequest) throws IOException {
		GenerarEnvResponse archivo = contingenciasService.generarEnvContingencia(continRequest);
		String contingencia = archivo.getContingencia();
		byte[] result;
		System.out.println("Nombre archivo: " + archivo.getNombreArchivo());
		if(contingencia.equals("COAS")) {
			result = Files.readAllBytes(Paths.get("C:\\MigracionWeb\\Certificaciones\\COAS\\validaciones\\"+archivo.getNombreArchivo()));
		}else {
			result = Files.readAllBytes(Paths.get("C:\\MigracionWeb\\Certificaciones\\POA\\env\\"+archivo.getNombreArchivo()));	
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment",archivo.getNombreArchivo());
		
		return new ResponseEntity<>(result,headers,HttpStatus.OK);
	}
	
	@GetMapping("nombreEnv")
	public ResponseEntity<String> archivosName(){
		
		System.out.println("Imprimiendo nombre");
		String nombre = contingenciasService.nombreArchivoEnv();
		return new ResponseEntity<String>(nombre,HttpStatus.OK);
		//return nombre;
		
	}
	
}
