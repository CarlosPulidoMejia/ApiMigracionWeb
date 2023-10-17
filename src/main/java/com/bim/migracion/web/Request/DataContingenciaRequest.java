package com.bim.migracion.web.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataContingenciaRequest {

	private int idBase;
	private String isArchivoEnv;
	private String idTipoContin;
	private String fechaCert;
	private int idFirma;
}
