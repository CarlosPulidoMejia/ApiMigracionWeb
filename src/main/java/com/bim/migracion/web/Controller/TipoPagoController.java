package com.bim.migracion.web.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bim.migracion.web.Entity.TipoPagoEntity;
import com.bim.migracion.web.Service.TipoPagoService;

@RestController
@CrossOrigin("*")
public class TipoPagoController {

	@Autowired
	private TipoPagoService pagoService;
	
	@GetMapping("/api/bim/pagos")
	public List<TipoPagoEntity> getAllPagos(){
		return pagoService.listPagos();
	}
}
