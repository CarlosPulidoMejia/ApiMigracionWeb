package com.bim.migracion.web.Request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MatrizCuentasRequest {

	private EncabezadoFileRequest encabezadoFileRequest;
	private List<DetalleOrdenRequest> detalleOrdenRequest;
	private String tipoCertificacion;
	private int idBase;
}
