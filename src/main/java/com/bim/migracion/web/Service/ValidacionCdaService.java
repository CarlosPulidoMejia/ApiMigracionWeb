package com.bim.migracion.web.Service;

import java.util.List;
import java.util.Map;

import com.bim.migracion.web.Response.DatosCdaMysql;
import com.bim.migracion.web.Response.DatosCdaSybase;


public interface ValidacionCdaService {

	public void buscarRegistros();
	
	public void cadenacampoValidar(String[] cadenacampoValidar);
	
	public void validarCdas();
	
	public Map<String, DatosCdaSybase> buscarCdasSybase();
	
	public Map<String, DatosCdaMysql> buscarCdasMysql(StringBuffer claRas);
	
	public Map<Integer, String> validarCda(List<String> lista);
	
	public String validaCda(String validaCda,int lineaLeida);
	
	//public Map<Integer, String> validarCda()
}
