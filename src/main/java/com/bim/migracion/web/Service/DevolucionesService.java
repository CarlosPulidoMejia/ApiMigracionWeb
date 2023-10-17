package com.bim.migracion.web.Service;

import java.util.List;

import com.bim.migracion.web.Request.GetDevolucionesRequest;
import com.bim.migracion.web.Request.SendDevolucionRequest;
import com.bim.migracion.web.Response.getDevolucionesResponse;



public interface DevolucionesService {
	public List<getDevolucionesResponse> getDevoluciones(GetDevolucionesRequest request);
	
	public void aplicarDevoluciones(SendDevolucionRequest devolucionesRequest);
	
	public List<getDevolucionesResponse> listaDevoluciones(GetDevolucionesRequest request);
}
