package com.bim.migracion.web.Utilidades;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Request.EnvioOrdenesRequest;

public interface Utilidades {
	public String obtenerFecha();
	public boolean validarCampo(String campo, int longMinima,int longMaxima);
	public boolean validarCampoNum(String campo, int longMinima,int longMaxima);
	public Map<Integer, String> leerArchivo(MultipartFile archivo);
	public EnvioOrdenesRequest envioOrdenesArchivo(Map<Integer,String> mapaArchivo);
	public Map<Integer, String> leerArchivos(String idConexion, MultipartFile[] archivos);
}
