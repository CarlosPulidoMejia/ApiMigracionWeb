package com.bim.migracion.web.Response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceResponse {
	
	private int id;
	private String ip;
	private String puerto;
	private String base;
	private String usuario;
	private String pass;
	private String descripcion;
	private String clave;
	private int estado;
	
}
