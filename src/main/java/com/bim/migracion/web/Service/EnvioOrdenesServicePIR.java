package com.bim.migracion.web.Service;


import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.EnvioOrdenesRequest;
import com.bim.migracion.web.Request.FirmadorRequest;

public interface EnvioOrdenesServicePIR {

	public void generaOrdenes(EnvioOrdenesRequest request);
	
	public void insertaOrdenes(DetalleOrdenRequest orden, FirmadorRequest firmador, DataSourceRequest datasource);
	
	public void insertaOrden(String query, DetalleOrdenRequest orden, FirmadorRequest firmador);
	
	public void firmarOrden(DetalleOrdenRequest orden, FirmadorRequest firmador, String numero);
	
	public void autorizarOrden(String numero, String tipPag);
	
	public String generaQueryPago(DetalleOrdenRequest orden);
	
	//public void ingresarRegistros()
}
