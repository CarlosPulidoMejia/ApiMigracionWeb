package com.bim.migracion.web.Request;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetDevolucionesRequest {
	
	private DataSourceRequest datasource;
	private String tipo;
	private int numeroRegistros;
	private String tipoPago;
	private String concepto;
	private String instancia;
	private String fechaInicio;
	private String fechaFin;
	private String orpNumeroDev;
	private int montoDev;

}
