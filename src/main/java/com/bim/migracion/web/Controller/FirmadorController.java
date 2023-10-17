package com.bim.migracion.web.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bim.migracion.web.Entity.FirmadorEntity;
import com.bim.migracion.web.Service.FirmadorService;

@RestController
@CrossOrigin("*")
public class FirmadorController {

	@Autowired
	private FirmadorService firmadorService;
	
	@GetMapping("bim/mw/firmador")
	public List<FirmadorEntity> listaFirmadorAPI(){
		return firmadorService.listFirmador();
	}
}
