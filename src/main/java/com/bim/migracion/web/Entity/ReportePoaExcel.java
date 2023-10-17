package com.bim.migracion.web.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReportePoaExcel {

	private int linea;
	private String detalleError;
	private String detalleSolucion;
	private String institucion;
}
