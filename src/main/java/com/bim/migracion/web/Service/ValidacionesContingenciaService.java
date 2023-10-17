package com.bim.migracion.web.Service;

import java.util.ArrayList;
import java.util.Map;

import com.bim.migracion.web.Entity.ReportePoaExcel;
import com.bim.migracion.web.Request.ValidadorPoaRequest;

public interface ValidacionesContingenciaService {

	public void validaFormatoPoa(ValidadorPoaRequest validadorPoaRequest,String fecha);
	
	public void readFilePoa(ValidadorPoaRequest validadorPoaRequest, ArrayList<ReportePoaExcel> listRepotePoaDTO, Map<Integer, String> archivoPoa,String fecha);
	
	public Boolean validaCampo(String datos, String campo, Integer linea, String tipo, String tipoPago, ArrayList<ReportePoaExcel> listRepotePoaDTO);
	
	public boolean validaAlfanumerico(String numIni, String numFin, String campo, String validaAlfanumerico2);

	public boolean validaNumero(String numIni, String numFin, String campo);
	
	public String modificacionCampo(String tipo, String tipoDato, String campo, String validaAlfanumerico2);
	
	public void createFilePoaMap(Map<Integer, String> filePoa,String tipoContingencia);
	
	

}
