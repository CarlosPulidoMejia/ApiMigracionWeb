package com.bim.migracion.web.Request;

import java.util.Map;

public class EnvioOrdenesMasivoRequest {
	public int tipoEnvio;
	public String nombreArchivo;
	public DataSourceRequest dataSourceRequest;
	public Map<Integer, String> mapOrdenesRequest;
}
