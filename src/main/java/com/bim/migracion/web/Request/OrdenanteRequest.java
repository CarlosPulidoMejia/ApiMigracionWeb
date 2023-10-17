package com.bim.migracion.web.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrdenanteRequest {
	
	private int idOrdenante;
	private String banco;
	private String tipoCuenta;
	private String cuenta;
	private String nombre;
	private String rfc;

}
