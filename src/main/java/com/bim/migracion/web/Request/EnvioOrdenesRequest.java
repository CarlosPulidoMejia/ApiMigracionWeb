package com.bim.migracion.web.Request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EnvioOrdenesRequest {
	
	private DataSourceRequest datasource;
	private FirmadorRequest firmador;
	private DetalleOrdenRequest ordenMig;
	private List<DetalleOrdenRequest> ordenes;
	private int numArchivos;
	
}
