package com.bim.migracion.web.Request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EncabezadoFileRequest {

	private int numCiclos;
	private int segundos;
	private OrdenanteRequest ordenanteRequest;
	private List<DetalleOrdenRequest> detalleOrdenRequest;
}
