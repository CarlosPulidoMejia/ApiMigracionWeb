package com.bim.migracion.web.Response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenanteResponse {
	private int idOrdenante;
	private String banco;
	private String tipoCuenta;
	private String cuenta;
	private String nombre;
	private String rfc;
}
