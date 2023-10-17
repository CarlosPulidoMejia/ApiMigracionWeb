package com.bim.migracion.web.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Request.ArchivoRequest;
import com.bim.migracion.web.Request.BeneficiarioRequest;
import com.bim.migracion.web.Request.ParametrosMCRequest;
import com.bim.migracion.web.Response.ComparacionCdaResponse;
import com.bim.migracion.web.Response.DetalleErrorResponse;
import com.bim.migracion.web.Response.ValidacionCdaResponse;

public interface ArchivosService {

	public List<ArchivoRequest> leerArchvivo(File archivo);
	public void crearArchivo(StringBuffer texto,String nombreArchivo);
	public List<BeneficiarioRequest> leerArchivoExcel(MultipartFile file,ParametrosMCRequest parametros);
	public List<BeneficiarioRequest> leerArchivoExcel2(MultipartFile file,List<BeneficiarioRequest> ben,ParametrosMCRequest parametros);
	public Boolean generarExcel(ValidacionCdaResponse validacionCda,String nombreArchivo) throws FileNotFoundException ;
	public void generarCsv(Map<Integer, String> mapaCda);
	public void generarExcelErrores(List<DetalleErrorResponse> detalleError);
	public void generarExcelComparacion(List<ComparacionCdaResponse> listaComparacion);
}
