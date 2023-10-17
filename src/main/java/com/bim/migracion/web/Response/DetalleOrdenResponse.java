package com.bim.migracion.web.Response;




import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleOrdenResponse {
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

}
