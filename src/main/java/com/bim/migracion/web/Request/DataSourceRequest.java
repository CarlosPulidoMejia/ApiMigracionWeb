package com.bim.migracion.web.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataSourceRequest {
	
	private int id;
	private String ip;
	private String puerto;
	private String base;
	private String usuario;
	private String pass;
	private String descripcion;
	private String clave;
	private boolean nuevo;
	private boolean activar;
	private int estado;

}
