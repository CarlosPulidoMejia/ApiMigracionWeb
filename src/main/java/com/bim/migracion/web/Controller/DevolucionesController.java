package com.bim.migracion.web.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.bim.migracion.web.Request.GetDevolucionesRequest;
import com.bim.migracion.web.Request.SendDevolucionRequest;
import com.bim.migracion.web.Response.getDevolucionesResponse;
import com.bim.migracion.web.Service.DevolucionesService;


@RestController
@CrossOrigin("*")
public class DevolucionesController {
	
	@Autowired
	private DevolucionesService devolucionesService;
	
	@PostMapping(value="/bim/mw/devoluciones/")
	public List<getDevolucionesResponse> getDevolciones(@RequestBody GetDevolucionesRequest request) {
		return devolucionesService.getDevoluciones(request);
	}
	
	@PostMapping(value="/enviarDevoluciones")
	public void enviarDevoluciones(@RequestBody SendDevolucionRequest devolucionesRequest) {
		//service.sendDevoluciones(request);
		devolucionesService.aplicarDevoluciones(devolucionesRequest);
		//System.out.println(devolucionesRequest.getDatasource().getId());
	}
	
	@PostMapping("api/bim/retornos")
	public List<getDevolucionesResponse> getDevolcionesRet(@RequestBody GetDevolucionesRequest request) {
		return devolucionesService.listaDevoluciones(request);
	}

}
