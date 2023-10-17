package com.bim.migracion.web.ControllerTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.bim.migracion.web.Service.MatrizDeCuentasService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@CrossOrigin("*")
public class MatrizDeCuentasController {

	@Autowired
	MatrizDeCuentasService matrizService;

	@PostMapping("/api/bim/poa/generarMC")
	public ResponseEntity<Resource> generarMC(@RequestParam("file") MultipartFile file,@RequestParam("dataMatriz") String data) throws JsonMappingException, JsonProcessingException, FileNotFoundException{

		String archivo = matrizService.generarMatriz(file, data);
		
		
		File files = new File("C:\\MigracionWeb\\Certificaciones\\POA\\insumos\\"+ archivo +".txt");
	    InputStreamResource resource = new InputStreamResource(new FileInputStream(files));
	    
	    //return ResponseEntity.ok("Archivo generado correctamente");
		
		return ResponseEntity.ok()
	    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + files.getName())
	            .contentLength(files.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
		/*System.out.println("Conectado a la API");*/
		//return ResponseEntity.ok("Conectado a la API");
	}

	@PostMapping("/api/bim/poa/generarMCTest")
	public ResponseEntity<byte[]> generarMCTest(@RequestParam("file") MultipartFile file,@RequestParam("dataMatriz") String data) throws IOException{

		
		
		String archivo = matrizService.generarMatriz(file, data);
		
		
		
	    
	    byte[] result = Files.readAllBytes(Paths.get("C:\\MigracionWeb\\Certificaciones\\POA\\"+archivo+".txt"));
	    
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename("123.txt").build());
		
		return new ResponseEntity<>(result,headers,HttpStatus.OK);
	    
	    //return ResponseEntity.ok("Archivo generado correctamente");
		
		/*return ResponseEntity.ok()
	    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + files.getName())
	            .contentLength(files.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);*/
		/*System.out.println("Conectado a la API");*/
		//return ResponseEntity.ok("Conectado a la API");
	}

	
	@GetMapping("/api/bim/poa/generarMCFile")
	public ResponseEntity<Resource> descargar() throws IOException{
		
		String archivo = "123";
		
		

		/*
		InputStreamResource resource = new InputStreamResource(new FileInputStream(files));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getName() + "\"");
		
		return ResponseEntity.ok()
	    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getName() + "\"")
	            .contentLength(files.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);*/
		
		Resource archivo1 = new FileSystemResource("C:\\MigracionWeb\\Certificaciones\\POA\\insumos\\"+ archivo +".txt");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment","Hola mundo.txt");
		//headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo1.getFilename() + "\"");
		//headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Paths.get("C:\\MigracionWeb\\Certificaciones\\POA\\insumos\\"+ archivo +".txt")));
		
		System.out.println("Test123" +  archivo1.getFilename());
		
		System.out.println(headers);
		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(archivo1.contentLength())
				.contentType(MediaType.parseMediaType(Files.probeContentType(Paths.get("C:\\MigracionWeb\\Certificaciones\\POA\\insumos\\"+ archivo +".txt"))))
				.body(archivo1);
		
		
		
		
		/*System.out.println("Conectado a la API");
		return ResponseEntity.ok("Conectado a la API");*/
	}
}
