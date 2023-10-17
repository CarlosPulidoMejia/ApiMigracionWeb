package com.bim.migracion.web.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContingenciaRequest {

	private int tipoContingencia;
	private int idBase;
	private String fechaCert;
	/*public ContingenciaRequest(int tipoContingencia, int idBase, String fechaCert) {
		super();
		this.tipoContingencia = tipoContingencia;
		this.idBase = idBase;
		this.fechaCert = fechaCert;
	}*/

	
	
	
	
}
