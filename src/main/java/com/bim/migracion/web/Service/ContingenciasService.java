package com.bim.migracion.web.Service;

import java.util.Map;

import com.bim.migracion.web.Request.ContingenciaRequest;
import com.bim.migracion.web.Response.ContingenciaResponse;
import com.bim.migracion.web.Response.GenerarEnvResponse;

public interface ContingenciasService {
	
	public ContingenciaResponse consultarContingencia(ContingenciaRequest contingenciaRequest);

	public void activarContingencia(ContingenciaRequest contingenciaRequest);
	
	public void desactivarContingencia(ContingenciaRequest contingenciaRequest);
	
	public GenerarEnvResponse generarEnvContingencia(ContingenciaRequest contingenciaRequest);
	
	public Map<Integer, String> generarFileContingencia();
	
	public String nombreArchivoEnv();
}
