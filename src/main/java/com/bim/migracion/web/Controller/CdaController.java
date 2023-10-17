package com.bim.migracion.web.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bim.migracion.web.Request.GetCDASRequest;
import com.bim.migracion.web.Response.ResponseGetCdas;
import com.bim.migracion.web.Service.CdaService;

@RestController
@CrossOrigin(origins = "*")
public class CdaController {

	@Autowired
	CdaService service;

	@Autowired
	ServletContext servletContext;

	@PostMapping(value = "/getCDAS")
	public List<ResponseGetCdas> getCDAS(@RequestBody GetCDASRequest request) {
		return service.getCDAs(request);
	}

	@GetMapping("/download/{fileName}")
	public ResponseEntity<InputStreamResource> downloadFile1(@PathVariable String fileName) throws IOException {

		MediaType mediaType = service.getMediaTypeForFileName(this.servletContext, fileName);
		System.out.println("fileName: " + fileName);
		System.out.println("mediaType: " + mediaType);

		File file = new File("archivos/" + fileName);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				// Content-Type
				.contentType(mediaType)
				// Contet-Length
				.contentLength(file.length()) //
				.body(resource);
	}

}
