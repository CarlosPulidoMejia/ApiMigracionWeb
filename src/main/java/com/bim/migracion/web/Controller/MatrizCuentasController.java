package com.bim.migracion.web.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.MatrizCuentasRequest;
import com.bim.migracion.web.Response.MatrizResponse;
import com.bim.migracion.web.Service.MatrizCuentasService;

@RestController
@CrossOrigin("*")
public class MatrizCuentasController {

	@Autowired
	private MatrizCuentasService matrizCuentasService; 
	
	@PostMapping("headerexcelBanxico")
	public void headerExcelBanxico(@RequestBody MatrizCuentasRequest matrizCuentasRequest){
		matrizCuentasService.crearEncabezadoFile(matrizCuentasRequest);
	}
	
	@PostMapping("excelBanxico")
	public void importExcelBanxico(@RequestParam("file") MultipartFile files) {
		//ParametrosMCRequest parametrosMC = matrizCuentasService.parametrosMC();
		matrizCuentasService.importExcelPoa(files);
	}
	
	@GetMapping("api/bim/matriz/getname")
	public MatrizResponse getName() {
		MatrizResponse matrizResponse = new MatrizResponse();
		String nameFile = matrizCuentasService.nameFileMatriz();
		matrizResponse.setName("MC_" + nameFile + ".txt");
		return matrizResponse;
	}
	
	@GetMapping("api/bim/matriz/download")
	public ResponseEntity<Resource> download(String param) throws IOException {

	    // ...

		
		String fileMatriz = matrizCuentasService.nameFileMatriz();
		
		System.out.println("Descargar file " +  fileMatriz);
		
		File file = new File("C:\\MigracionWeb\\Certificaciones\\POA\\test matriz\\poa-"+ fileMatriz +".txt");
	    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

	    
	    return ResponseEntity.ok()
	    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
	            .contentLength(file.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	}
	
	@PostMapping(value = "/api/bim/matriz/datos")
	public void subirInfo(@RequestParam("dataMatriz") String user,@RequestPart("file") MultipartFile file) {
		
		System.out.println("TEST123");
		matrizCuentasService.dataMatriz(user, file);
		
	}
	
	
	@PostMapping(value = "/api/bim/matriz/datos1")
	public List<DetalleOrdenRequest> getFoos(@RequestParam List<DetalleOrdenRequest> id) {
		
		return id;
	}
}
