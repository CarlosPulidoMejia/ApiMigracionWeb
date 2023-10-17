package com.bim.migracion.web.Request;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MatrizDeCuentasRequest {

	public String tipoCertificacion;
	public int idBase;
	public EncabezadoMatrizRequest encabezadoMatriz;
	public OrdenanteRequest ordenanteMatriz;
	public List<DetalleOrdenRequest> detalleMatriz;
}
