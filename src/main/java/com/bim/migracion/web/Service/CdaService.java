package com.bim.migracion.web.Service;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.http.MediaType;

import com.bim.migracion.web.Request.GetCDASRequest;
import com.bim.migracion.web.Response.ResponseGetCdas;

public interface CdaService {
	public List<ResponseGetCdas> getCDAs(GetCDASRequest request);
	public List<ResponseGetCdas> generaArchivos(GetCDASRequest request);
	//public List<ResponseGetCdas> crearArchivo(ArrayList<DatosCdaEntity> datos);
	//public ResponseGetCdas _crearArchivo(DatosCdaEntity datos);
	public MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName);
}
