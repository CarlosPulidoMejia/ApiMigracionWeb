package com.bim.migracion.web.Request;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DevolucionesRequest {

	private boolean seleccion;
	private int row;
	private String numero;
	private String tipoPago;
	private double cantidad;
	private String claveRastreo;
	private String cuentaBeneficiaria;
	private String institucionEmisora;
	private String conceptoPago;
	private String fecha;
	private String tipoCuentaBeneficiaria;
	private String tipoCuentaOrdenante;
	private String cuentaOrdenante;
	private String institucionReceptora;
	private int instancia;
	private String pagoDev;
	//private List<TipoPagoResponse> pagos;
}
