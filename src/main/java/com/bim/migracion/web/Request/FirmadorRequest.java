package com.bim.migracion.web.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FirmadorRequest {
	
	private int id;
	private boolean firmar;
	private String ip;
	private String puerto;
	private String usuario;
	private String pass;
	private String descripcion;
	private boolean nuevoFirmador;
	private String token;
	
}
