package com.bim.migracion.web.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleEscenarioResponse {
	private int idEscenario;
	private int idRelacion;
	private String nombreEscenario;
	private BeneficiarioResponse envios;
	private OrdenanteResponse ordenanteResponse;
	private BeneficiarioResponse beneficiarioResponse;
	private DetalleOrdenResponse detalleOrdenResponse;
	private DataSourceResponse dataSourceResponse;
	private FirmadorResponse firmadorResponse;
}
