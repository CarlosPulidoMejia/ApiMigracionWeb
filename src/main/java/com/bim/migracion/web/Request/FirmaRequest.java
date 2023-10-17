package com.bim.migracion.web.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FirmaRequest {
	private int idFirma;
	private String firma;
	private String numero;
	private String descripcion;
}
