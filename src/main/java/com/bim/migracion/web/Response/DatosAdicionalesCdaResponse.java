package com.bim.migracion.web.Response;

import lombok.Data;

@Data
public class DatosAdicionalesCdaResponse {

	public String tipoPago;
	public String concepto;
	public int monto;
	public String nombreBenAdi;
	public String rfcBenAdi;
	public String tipoCuentaBenAdi;
	public String cuentaBenAdi;
}
