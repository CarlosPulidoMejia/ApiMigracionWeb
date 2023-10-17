package com.bim.migracion.web.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Request.EnvioOrdenesRequest;
import com.bim.migracion.web.Service.CargaMasivaService;
import com.bim.migracion.web.Service.EnvioOrdenesService;
import com.bim.migracion.web.Service.EnvioOrdenesServicePIR;



@CrossOrigin(origins = "*")
@RestController
public class EnvioOrdenesController {
	
	@Autowired
	private EnvioOrdenesService envioService;
	
	@Autowired
	private EnvioOrdenesServicePIR envioServicepir;
	
	@Autowired
	private CargaMasivaService cargaMasiva;
	
	/*@Autowired
	EnvioOrdenesService service;
	
	@Autowired
	private CargaMasivService cargaMasivaServ;
	
	@PostMapping(value="/postEnvioOrden")
	public void envioOrden(@RequestBody EnvioOrdenesRequest request) {		
		service.generaOrdenes(request);
	}
	
	@PostMapping(value = "/postFiles")
	public void subirInfo(@RequestParam("users") String user,@RequestPart("file") MultipartFile file) {

		cargaMasivaServ.cargaMasiva(user, file,null);
		
	}
	*/
	
	@PostMapping("/api/bim/envio/ordenes")
	public void enviarOrdenes(@RequestBody EnvioOrdenesRequest envioOrdenesRequest) {
		envioService.generaOrdenes(envioOrdenesRequest);
	}
	
	
	@PostMapping("/api/bim/envio/ordenespir")
	public void enviarOrdenespir(@RequestBody EnvioOrdenesRequest envioOrdenesRequest) {
		envioServicepir.generaOrdenes(envioOrdenesRequest);
	}
	
	@PostMapping(value = "/api/bim/envio/carga-masiva")
	public void subirInfo(@RequestParam("dataMasiva") String user,@RequestPart("file") MultipartFile file) {
		cargaMasiva.cargaMasiva(user, file);
		//matrizCuentasService.dataMatriz(user, file);
		
	}
	

}
