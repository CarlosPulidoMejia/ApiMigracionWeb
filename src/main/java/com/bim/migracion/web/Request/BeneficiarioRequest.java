package com.bim.migracion.web.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BeneficiarioRequest {
	private int idBeneficiario;
	private String banco;
	private String tipoCuenta;
	private String cuenta;
	private String nombre;
	private String rfc;
	private String modulo;
	private String statusBen;

}
