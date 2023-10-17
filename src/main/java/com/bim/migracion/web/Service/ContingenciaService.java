package com.bim.migracion.web.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Request.ContingenciaRequest;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.EnvioOrdenesRequest;
import com.bim.migracion.web.Response.ContingenciaResponse;
import com.bim.migracion.web.Response.GenerarEnvResponse;

public interface ContingenciaService {
	
	public ContingenciaResponse consultarContingencia(ContingenciaRequest contingenciaRequest);

	public void activarContingencia(ContingenciaRequest contingenciaRequest);
	
	public void offContingencia(ContingenciaRequest contingenciaRequest);
	
	public GenerarEnvResponse uploadFileContingencia(String data,MultipartFile file);
	
	public void readFileContingencia(BufferedReader reader);
	
	public void leerArchivo(MultipartFile file);
	
	public void mapFileContingencia(Map<Integer, String> mapaArchivo);
	
	public void cantidadFileContingencia(int numeroArchivos,EnvioOrdenesRequest envioOrdenesRequest);
	
	public void envioOrdenesContingencia(DetalleOrdenRequest detalleOrdenRequest);
	
	public String generarEnvContingencia(DataSourceRequest dataSourceRequest);
	
	public Map<Integer, String> generarFileContingencia();
	
	public String validarFilesContingencia(MultipartFile[] files);
	
	public void readFileValidacionContingencia(InputStream inputStream, String nameFile);
	
	public void renameFile(String fileName);
	
	public String nombreArchivoEnv();
}
