package com.bim.migracion.web.Response;

import lombok.Data;

@Data
public class DetalleErrorResponse {

	public int numeroLinea;
	public String campoError;
	public String detalleError;
	public String solucion;
}
