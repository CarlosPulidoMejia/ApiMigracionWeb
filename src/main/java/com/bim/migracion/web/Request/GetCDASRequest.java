package com.bim.migracion.web.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GetCDASRequest {
	
	private DataSourceRequest datasource;
	private String cer;
	private String nameCer;
	private String cve;
	private String nameCve;
	private String crt;
	private String nameCrt;
	private String password;
	private String orpNumIni;
	private String orpNumFin;
	private String tipoBusqueda;

}
