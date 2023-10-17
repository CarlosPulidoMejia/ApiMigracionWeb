package com.bim.migracion.web.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.InputStreamResource;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Request.ContingenciaRequest;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Response.ContingenciaResponse;
import com.bim.migracion.web.Response.GenerarEnvResponse;
import com.bim.migracion.web.Service.ContingenciaService;

@RestController
@CrossOrigin("*")
public class ContingenciaController {
	
	@Autowired
	private ContingenciaService contingenciaService;

	@PostMapping("/api/bim/contingencia/consultar")
	public ContingenciaResponse consultarContingencia(@RequestBody ContingenciaRequest contingenciaRequest) {
		return contingenciaService.consultarContingencia(contingenciaRequest);
	}
	
	@PostMapping("/api/bim/contingencia/activar")
	public void activarContingencia(@RequestBody ContingenciaRequest contingenciaRequest) {
		contingenciaService.activarContingencia(contingenciaRequest);
	}
	
	@PostMapping("/api/bim/contingencia/off")
	public void offContingencia(@RequestBody ContingenciaRequest contingenciaRequest) {
		contingenciaService.offContingencia(contingenciaRequest);
	}
	
	@PostMapping(value = "/api/bim/contingencia/datos")
	public void subirInfo(@RequestParam("dataContin") String user,@RequestPart("file") MultipartFile file) {
		//String mensaje = contingenciaService.uploadFileContingencia(user, file);
		
		//System.out.println("Mensaje service: " + mensaje);
		
	}
	
	@PostMapping(value = "/api/bim/contingencia/datosEnv")
	public ResponseEntity<byte[]> generarMCTest(@RequestParam("dataContin") String user,@RequestPart("file") MultipartFile file) throws IOException{
		
		
		GenerarEnvResponse archivo = contingenciaService.uploadFileContingencia(user, file);
		
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
		
		//return ResponseEntity.ok().headers(headers).body(result);

		
	}

	/*
	@PostMapping(value = "/api/bim/contingencia/datosEnvt")
	public ResponseEntity<InputStreamResource> generarMCTest1(@RequestParam("dataContin") String user,@RequestPart("file") MultipartFile file,HttpServletResponse response) throws IOException{
		
		
		String archivo = contingenciaService.uploadFileContingencia(user, file);
		
		
		File files = new File("C:\\MigracionWeb\\Certificaciones\\POA\\validaciones\\"+ archivo);
	    InputStreamResource resource = new InputStreamResource(new FileInputStream(files));
	    
	    //return ResponseEntity.ok("Archivo generado correctamente");
	    HttpHeaders headers = new HttpHeaders();
	    //headers.setContentDispositionFormData("attachment",archivo);
	    headers.setContentDisposition(ContentDisposition.builder("attachment").filename("123.txt").build());
	    
	    //response.setHeader("Content-Disposition", "attachment; filename=123.txt");
		
	   // return new ResponseEntity<>(resource,headers,HttpStatus.OK);
	    
	    /*return ResponseEntity.ok()
	    		.headers(headers)
	            .contentLength(files.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);*/
	    
	   // return ResponseEntity.ok().headers(headers).body(resource);
		/*System.out.println("Conectado a la API");*/
		//return ResponseEntity.ok("Conectado a la API");
	            
	   /* return ResponseEntity.ok()
	            .header("Content-Disposition", "attachment; filename=" + resourceName)
	            .contentLength(resourceLength)
	            .lastModified(lastModified)
	            .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
	            .body(resource);*/
	
		
	//}
	
	@PostMapping("/api/bim/contingencia/carga")
	public void cargarArchivo(@RequestParam("dataContin") String user,@RequestPart("file") MultipartFile file,HttpServletResponse response) throws IOException{
		contingenciaService.uploadFileContingencia(user, file);
	}

	
	@PostMapping("/api/bim/contingencia/validacion")
	public void uploadFile(@RequestParam("file") MultipartFile[] files) {
		contingenciaService.validarFilesContingencia(files);
		
		//List<String> fileNames = new ArrayList<>();

	      //Arrays.asList(files).stream().forEach(file -> {
	    	  //System.out.println(file.getOriginalFilename());
	        //fileNames.add(file.getOriginalFilename());
	     // });
		//System.out.println("Hola mundo");
	      
	    //return poaService.listErrores();
	}
	
	@PostMapping("/api/bim/contingencia/validacionTest")
	public ResponseEntity<byte[]> uploadFileTest(@RequestParam("file") MultipartFile[] files) throws IOException {
		String archivo = contingenciaService.validarFilesContingencia(files);
		
		 System.out.println("Nombre archivo: " + archivo);
		    byte[] result = Files.readAllBytes(Paths.get("C:\\MigracionWeb\\Certificaciones\\POA\\validaciones\\"+archivo));
		    
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment",archivo);
			
			return new ResponseEntity<>(result,headers,HttpStatus.OK);
		
		//List<String> fileNames = new ArrayList<>();

	      //Arrays.asList(files).stream().forEach(file -> {
	    	  //System.out.println(file.getOriginalFilename());
	        //fileNames.add(file.getOriginalFilename());
	     // });
		//System.out.println("Hola mundo");
	      
	    //return poaService.listErrores();
	}
	
	@PostMapping("/api/bim/contingencia/generar")
	public void generarEnv(@RequestBody DataSourceRequest dataRequest) {
		contingenciaService.generarEnvContingencia(dataRequest);
	}
	
	
	@PostMapping("/api/bim/contingencia/generarTest")
	public ResponseEntity<byte[]> generarEnvTest(@RequestBody DataSourceRequest dataRequest) throws IOException {
		String archivo = contingenciaService.generarEnvContingencia(dataRequest);
		
		System.out.println("Nombre archivo: " + archivo);
	    byte[] result = Files.readAllBytes(Paths.get("C:\\MigracionWeb\\Certificaciones\\POA\\validaciones\\"+archivo));
	    
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment",archivo);
		
		return new ResponseEntity<>(result,headers,HttpStatus.OK);
	}
	
	
	/*Validaciones archivo*/
	@GetMapping("archivo")
	public ResponseEntity<InputStreamResource> archivos(HttpServletResponse response) throws FileNotFoundException{
		
		File file = new File("C:\\MigracionWeb\\Certificaciones\\POA\\validaciones\\123.txt");
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				// Content-Type

				// Contet-Length
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.contentLength(file.length()) //

				.body(resource);
		
	}
	
	@GetMapping("nombre")
	public ResponseEntity<String> archivosName(){
		
		System.out.println("Imprimiendo nombre");
		String nombre = contingenciaService.nombreArchivoEnv();
		return new ResponseEntity<String>(nombre,HttpStatus.OK);
		//return nombre;
		
	}
}
