package com.bim.migracion.web.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DetalleOrdenRequest {
	
	private int id;
	private int cantidad;
	private String tipoPago;
	private double monto;
	private String concepto;
	private String modulo;
	private Long segundos;
	private boolean registro;
	private boolean ampliado;
	private boolean firmador;
	private int id_Escenario;
	private int id_Cert;
	private int idDetalle;
	private boolean certificacion;
	private String pagoOrigen;
	private int cantidadOrigen;
	private OrdenanteRequest ordenante;
	private OrdenantePirRequest ordenantePir;
	private BeneficiarioRequest beneficiario;
	private String status;
	
}
