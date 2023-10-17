package com.bim.migracion.web.Request;
import java.util.List;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SendDevolucionRequest {

	private List<DevolucionesRequest> devoluciones;
	private DataSourceRequest datasource;
}
