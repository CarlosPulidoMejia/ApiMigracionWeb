package com.bim.migracion.web.Service;

import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Request.ParametrosMCRequest;

public interface MatrizDeCuentasService {

	public String generarMatriz(MultipartFile file,String datos);
	
	public ParametrosMCRequest parametros(MultipartFile file);
}
