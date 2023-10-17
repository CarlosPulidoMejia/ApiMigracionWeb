package com.bim.migracion.web.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bim.migracion.web.Response.DetalleEscenarioResponse;
import com.bim.migracion.web.Service.EscenariosService;





@RestController
@CrossOrigin("*")
public class EscenariosController {
	
	@Autowired
	private EscenariosService escenariosService;

	@GetMapping(value = "/api/bim/envios/escenarios")
	public List<DetalleEscenarioResponse> lista() {
		return escenariosService.listarEscenarios();
	}
}
