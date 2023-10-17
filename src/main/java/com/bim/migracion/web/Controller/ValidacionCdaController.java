package com.bim.migracion.web.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bim.migracion.web.Service.ValidacionCdaService;

@RestController
public class ValidacionCdaController {

	@Autowired
	private ValidacionCdaService validacionService;

	@GetMapping("cda")
	public void validacion() {
		validacionService.validarCdas();
	}
}
