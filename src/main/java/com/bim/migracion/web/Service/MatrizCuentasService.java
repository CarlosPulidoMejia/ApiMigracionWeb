package com.bim.migracion.web.Service;

import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Request.MatrizCuentasRequest;
import com.bim.migracion.web.Request.ParametrosMCRequest;

public interface MatrizCuentasService {

	public void crearEncabezadoFile(MatrizCuentasRequest matrizCuentasRequest);
	
	public String importExcelPoa(MultipartFile files);
	
	public void crearEncabezadoFileUpdate(int numCiclo2, int segundos2, String banco2, String tipoCuenta2, String cuenta2, String nombre2, String rfc2);
	
	public void leerMatrizCuentasSpeiPOA(MultipartFile files, int cantidadFile, String tipoPagoFile, double montoFile,
			String statusFile, String moduloFile,ParametrosMCRequest parametroMC);
	
	public void datosFileUpdate(String archivo2, int cantidad, String tipoPago, double monto, int clabeSpei, String cuentaCliente,
			String tipoCuentaCliente, String nombreCliente, String rfcCliente, String status, String j, int k);
	
	public String nameFileMatriz();
	
	public int searchInstit(int numeroClabe);
	
	/*
	 * Nueva matriz de cuentas
	 */
	public void dataMatriz(String data,MultipartFile file);
	
	public ParametrosMCRequest parametrosMC();
}
