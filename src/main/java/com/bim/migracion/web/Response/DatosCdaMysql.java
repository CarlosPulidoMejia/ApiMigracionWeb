package com.bim.migracion.web.Response;

import lombok.Data;

@Data
public class DatosCdaMysql {

	public OrdenanteResponse ordResponse;
	public BeneficiarioResponse benResponse;
	public DatosAdicionalesCdaResponse datosAdResponse;
}
