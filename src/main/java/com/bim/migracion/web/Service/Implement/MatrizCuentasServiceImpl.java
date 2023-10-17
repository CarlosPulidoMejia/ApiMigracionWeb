package com.bim.migracion.web.Service.Implement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.MatrizCuentasRequest;
import com.bim.migracion.web.Request.ParametrosMCRequest;
import com.bim.migracion.web.Service.DatasourceService;
import com.bim.migracion.web.Service.MatrizCuentasService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MatrizCuentasServiceImpl implements MatrizCuentasService {

	static int numCiclo, segundos, cantidad,idBase;
	static double monto;
	static String tipoCertificacion, archivo, banco, tipoCuenta, cuenta, nombre, rfc, tipoPago, statusConcepto, modulo;
	static List<DetalleOrdenRequest> listDetalleOrdenRequests = new ArrayList<DetalleOrdenRequest>();

	static int numeroId = 0;
	
	@Autowired
	private PropiedadesSybase propiedadesSybase;
	
	@Autowired
	private DatasourceService dataService;
	
	/*@Autowired
	private ArchivosService archivoService;*/
	
	DataSourceRequest dataRequest = new DataSourceRequest();

	@Override
	public void crearEncabezadoFile(MatrizCuentasRequest matrizCuentasRequest) {
		// TODO Auto-generated method stub
		listDetalleOrdenRequests.clear();
		tipoCertificacion = matrizCuentasRequest.getTipoCertificacion();

		numCiclo = matrizCuentasRequest.getEncabezadoFileRequest().getNumCiclos();
		segundos = matrizCuentasRequest.getEncabezadoFileRequest().getSegundos();

		banco = matrizCuentasRequest.getEncabezadoFileRequest().getOrdenanteRequest().getBanco();
		tipoCuenta = matrizCuentasRequest.getEncabezadoFileRequest().getOrdenanteRequest().getTipoCuenta();
		cuenta = matrizCuentasRequest.getEncabezadoFileRequest().getOrdenanteRequest().getCuenta();
		nombre = matrizCuentasRequest.getEncabezadoFileRequest().getOrdenanteRequest().getNombre();
		rfc = matrizCuentasRequest.getEncabezadoFileRequest().getOrdenanteRequest().getRfc();
		idBase = matrizCuentasRequest.getIdBase();

		for (DetalleOrdenRequest det : matrizCuentasRequest.getDetalleOrdenRequest()) {
			DetalleOrdenRequest dets = new DetalleOrdenRequest();

			dets.setCantidad(det.getCantidad());
			dets.setTipoPago(det.getTipoPago());
			dets.setMonto(det.getMonto());
			dets.setConcepto(det.getConcepto());
			dets.setModulo(det.getModulo());
			
			listDetalleOrdenRequests.add(dets);
		}
		
		
	}

	@Override
	public String importExcelPoa(MultipartFile files) {
		// TODO Auto-generated method stub
		crearEncabezadoFileUpdate(numCiclo, segundos, banco, tipoCuenta, cuenta, nombre, rfc);
		// leerMatrizCuentasSpeiCero(files);
		dataRequest = dataService.findByIdSbe(idBase);
		DataSourceSybase.setConexion(dataRequest.getIp(), dataRequest.getPuerto(), dataRequest.getBase(),dataRequest.getUsuario() , dataRequest.getPass());
		ParametrosMCRequest parametros = new ParametrosMCRequest();
		parametros = parametrosMC();
		for (int i = 0; i < listDetalleOrdenRequests.size(); i++) {
			int cantidadFile = 0;
			String tipoPagoFile = "";
			double montoFile = 0;
			String statusFile = "";
			String moduloFile = "";
			cantidadFile = listDetalleOrdenRequests.get(i).getCantidad();
			tipoPagoFile = listDetalleOrdenRequests.get(i).getTipoPago();
			montoFile = listDetalleOrdenRequests.get(i).getMonto();
			statusFile = listDetalleOrdenRequests.get(i).getConcepto();
			moduloFile = listDetalleOrdenRequests.get(i).getModulo();
			
			leerMatrizCuentasSpeiPOA(files, cantidadFile, tipoPagoFile, montoFile, statusFile, moduloFile,parametros);
		}
		listDetalleOrdenRequests.clear();
		
		DataSourceSybase.cerrarConexion();
		
		return archivo;
	}

	@Override
	public void crearEncabezadoFileUpdate(int numCiclo2, int segundos2, String banco2, String tipoCuenta2,
			String cuenta2, String nombre2, String rfc2) {
		// TODO Auto-generated method stub
		String pattern = "ddMMhhmmss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String date = simpleDateFormat.format(new Date());
		archivo = date;
		
		String ruta = "";
		/*if (tipoCertificacion.equals("cero")) {
			ruta = "C:\\MigracionWeb\\Certificaciones\\Cero\\" + tipoCertificacion + "-" + date + ".txt";
		} else {
			ruta = "C:\\MigracionWeb\\Certificaciones\\POA\\" + tipoCertificacion + "-" + date + ".txt";
		}*/
		
		ruta = "C:\\MigracionWeb\\Certificaciones\\POA\\test matriz\\" + tipoCertificacion + "-" + date + ".txt";

		// String contenido1 = ciclo + "-" + segundo;
		String contenido1 = numCiclo + "-" + segundos;
		String contenido2 = banco2 + "-" + cuenta2 + "-" + tipoCuenta2 + "-" + nombre2 + "-" + rfc2;

		File file = new File(ruta);
		// Si el archivo no existe es creado
		try {
			// file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(contenido1);
			bw.newLine();
			bw.write(contenido2);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error", e);
			e.printStackTrace();
		}
	}

	@Override
	public void leerMatrizCuentasSpeiPOA(MultipartFile files, int cantidadFile, String tipoPagoFile, double montoFile,
			String statusFile, String moduloFile, ParametrosMCRequest parametroMC) {
		// TODO Auto-generated method stub
		XSSFWorkbook workbook;
		int existeCuenta;
		try {
			
			
			workbook = new XSSFWorkbook(files.getInputStream());
			XSSFSheet worksheet = workbook.getSheetAt(0);

			XSSFSheet worksheet1 = workbook.getSheetAt(1);

			DateFormat dateFormatter = new SimpleDateFormat("HH-mm-ss");
			@SuppressWarnings("unused")
			String currentDateTime = dateFormatter.format(new Date());


			/*
			 * if (tipoCuenta.equals("CI")) { ciclo = 1; segundo = 1;
			 * 
			 * } else { ciclo = 2; segundo = 1;
			 * 
			 * }
			 */
			// crearHeader(ciclo, segundo, cantidad, tipoPago, currentDateTime);

			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Numero filas: " + worksheet.getPhysicalNumberOfRows(), null);
			
			System.out.println("Numero filas: "+ worksheet.getPhysicalNumberOfRows());
			for (int index = 4; index <= worksheet.getPhysicalNumberOfRows(); index++) {

				// System.out.println("Numero de filas excel " +
				// worksheet.getPhysicalNumberOfRows());

				XSSFRow row = worksheet.getRow(index);
				numeroId = 0;
				int clave = (int) row.getCell(1).getNumericCellValue();
				System.out.println("Clave MC123: " + clave);
				if (clave > 0) {
					// System.out.println("IF");
					
					for (int index1 = 1; index1 < worksheet1.getPhysicalNumberOfRows(); index1++) {

						GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Linea excel leida: " + index1, null);
						
						
						// System.out.println("Numero2 de filas excel " +
						// worksheet1.getPhysicalNumberOfRows() + "---" + clave);
						// System.out.println("ciclo " + index1);
						XSSFRow row1 = worksheet1.getRow(index1);
						//int numeroClabeSpei = (int) row1.getCell(0).getNumericCellValue();
						//numeroId = numeroClabeSpei;

						//if (numeroId > numeroClabeSpei) {
						//	System.out.println("Hay un registro mayor");
						//}
						int clabeSpei = (int) row1.getCell(1).getNumericCellValue();
						String status = row1.getCell(6).getStringCellValue();
						String cuentaCliente = row1.getCell(3).getStringCellValue().trim();
						cuentaCliente = cuentaCliente.replaceAll("^\\s*", "");
						//Se comenta para hacer 2 validaciones en nombre y RFC, se pasaran de momento a genericos
						//String nombreCliente = row1.getCell(6).getStringCellValue().trim().toUpperCase();
						String nombreCliente = "Cliente " + clabeSpei;
						//RFC GENERICO
						//String rfcCliente = row1.getCell(7).getStringCellValue().trim();
						String rfcCliente="XXXX0000009K1";
						if (nombreCliente.equals("")) {
							nombreCliente = "NA";
						}

						if (rfcCliente.equals("")) {
							rfcCliente = "NA";
						}

						String cadenaNormalize = Normalizer.normalize(nombreCliente, Normalizer.Form.NFD);
						nombreCliente = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");

						rfcCliente = rfcCliente.replaceAll("[^\\dA-Za-z]", "");
						// System.out.println("Resultado: " + nombreCliente);

						if (clabeSpei == clave) {
							existeCuenta=searchInstit(clabeSpei);
							
							if(existeCuenta>0) {
							// System.out.println("Clave:" + clave);

							// System.out.println("ciclo " + numeroClabeSpei + "-" + cuentaCliente);
								
							if (statusFile.equals("CV")) {
								// System.out.println("Valida");
								//System.out.println("0:" + clabeSpei + "-" + cuentaCliente);
								if (clabeSpei == 40150) {
									clabeSpei = 450;
								}
								if (status.equals("Válida")) {
									cuentaCliente = cuentaCliente.replaceAll("[^\\dA-Za-z]", "");

								}

								if (cuentaCliente.length() == 18) {
									// System.out.println("Correcto " + clave);
									/*
									 * if(nombreCliente.charAt(0) == ' ') { nombreCliente =
									 * nombreCliente.substring(1); }
									 */

									// System.out.println("Nombre Cliente:" + nombreCliente +"---" + rfcCliente);
								} else {
									cuentaCliente = "Error en Matriz de cuentas -" + cuentaCliente.length();
									System.out.println("Error123: " + clave + "---" + cuentaCliente);
									System.out.println(cuentaCliente);
									System.out.println("TEST123");
									continue;

									/*
									 * boolean isNumeric = cuentaCliente.matches("[+-]?\\d*(\\.\\d+)?");
									 * if(isNumeric) { System.out.println("Es valor numerico"); }else {
									 * //if(cuentaCliente.substring(0)) System.out.println("NO es valor numerico");
									 * }
									 */
								}

								datosFileUpdate(archivo, cantidadFile, tipoPagoFile, montoFile, clabeSpei,
										cuentaCliente, "40", nombreCliente, rfcCliente, statusFile, moduloFile, 0);
								break;

							} else {
								System.out.println("Invalida");
								if (status.equals("Inválida")) {
									if (clabeSpei == 40150) {
										clabeSpei = 450;
									}

									System.out.println("long " + cuentaCliente.length());
									cuentaCliente = cuentaCliente.replaceAll("[^\\dA-Za-z]", "");

									if (cuentaCliente.length() == 18) {
										// System.out.println("Correcto " + clave);
										/*
										 * if(nombreCliente.charAt(0) == ' ') { nombreCliente =
										 * nombreCliente.substring(1); }
										 */

										// System.out.println("Nombre Cliente:" + nombreCliente +"---" + rfcCliente);
									} else {
										cuentaCliente = "Error en Matriz de cuentas -" + cuentaCliente.length();
										System.out.println("Error123: " + clave + "---" + cuentaCliente);
										System.out.println(cuentaCliente);
										System.out.println("TEST123");
										continue;

										/*
										 * boolean isNumeric = cuentaCliente.matches("[+-]?\\d*(\\.\\d+)?");
										 * if(isNumeric) { System.out.println("Es valor numerico"); }else {
										 * //if(cuentaCliente.substring(0)) System.out.println("NO es valor numerico");
										 * }
										 */
									}
									// System.out.println("long2 " + nuevacuentaCliente.length());
									/*
									 * String nuevoNombre = nombreCliente.substring(nombreCliente.length()-1);
									 * if(nuevoNombre.equals(" ")) { System.out.println("xcv---" + clave); }
									 */

									datosFileUpdate(archivo, cantidadFile, tipoPagoFile, montoFile, clabeSpei,
											cuentaCliente, "40", nombreCliente, rfcCliente, statusFile, moduloFile, 0);
									break;
								}
							}
							
							}//termina if de consulta sybase
							else {
								System.out.println("No hya registro en BD:" + clabeSpei);
								break;
							}

						}//Termina if de cuenta = clabe//sintonia las bases de datos de certid

					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error", e);
			e.printStackTrace();
		}
	}

	@Override
	public void datosFileUpdate(String archivo2, int cantidad, String tipoPago, double monto, int clabeSpei,
			String cuentaCliente, String tipoCuentaCliente, String nombreCliente, String rfcCliente, String status,
			String j, int k) {
		// TODO Auto-generated method stub
		String contenido1 = cantidad + "-" + tipoPago + "-" + monto + "-" + clabeSpei + "-" + cuentaCliente + "-"
				+ tipoCuentaCliente + "-" + nombreCliente + "-" + rfcCliente + "-" + status + "-" + j + "-" + k;
//System.out.println("Archivo:---" + archivo2);
//System.out.println(contenido1);
		String ruta = "";
		/*if (tipoCertificacion.equals("cero")) {
			ruta = "C:\\MigracionWeb\\Certificaciones\\Cero\\" + tipoCertificacion + "-" + archivo2 + ".txt";
		} else {
			ruta = "C:\\MigracionWeb\\Certificaciones\\POA\\" + tipoCertificacion + "-" + archivo2 + ".txt";
		}*/
		
		ruta = "C:\\MigracionWeb\\Certificaciones\\POA\\test matriz\\" + tipoCertificacion + "-" + archivo2 + ".txt";
		
		File file = new File(ruta);
// Si el archivo no existe es creado
		try {
			// file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(contenido1);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error", e);
			e.printStackTrace();
		}
	}

	@Override
	public String nameFileMatriz() {
		// TODO Auto-generated method stub
		return archivo;
	}

	@Override
	public int searchInstit(int numeroClabe) {
		// TODO Auto-generated method stub
		dataRequest = dataService.findByIdSbe(87);
		DataSourceSybase.setConexion(dataRequest.getIp(), dataRequest.getPuerto(), dataRequest.getBase(),dataRequest.getUsuario() , dataRequest.getPass());
		
		DataSourceSybase.getConnection();
		
		int valida=0;
		try {
			String query = "SELECT COUNT(*) AS NumReg FROM SPINSTIT WHERE Ins_Clave='"+ numeroClabe +"'";
			ResultSet rs = propiedadesSybase.getResultsetGenerico(query);
			while(rs.next()) {
				valida=rs.getInt("NumReg");
			}
			
			
			if(valida>0) {
				valida=1;
			}else {
				valida=0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//DataSourceSybase.cerrarConexion();
		return valida;
	}

	/*******
	 * Nueva matriz de cuentas
	 */
	@Override
	public void dataMatriz(String data, MultipartFile file) {
		// TODO Auto-generated method stub
		MatrizCuentasRequest matrizCuentasRequest;
		System.out.println("UPLOAD CONTINGENCIA");

		ObjectMapper mapper = new ObjectMapper();

		try {
			matrizCuentasRequest = mapper.readValue(data, MatrizCuentasRequest.class);
			
			System.out.println(matrizCuentasRequest.getEncabezadoFileRequest().getNumCiclos());
			System.out.println(matrizCuentasRequest.getEncabezadoFileRequest().getSegundos());
			System.out.println(matrizCuentasRequest.getEncabezadoFileRequest().getOrdenanteRequest().getBanco());
			
			for (DetalleOrdenRequest det : matrizCuentasRequest.getDetalleOrdenRequest()) {
				DetalleOrdenRequest dets = new DetalleOrdenRequest();

				dets.setCantidad(det.getCantidad());
				dets.setTipoPago(det.getTipoPago());
				dets.setMonto(det.getMonto());
				dets.setConcepto(det.getConcepto());
				dets.setModulo(det.getModulo());
				System.out.println(dets.getConcepto()+"-" + dets.getTipoPago());
				//listDetalleOrdenRequests.add(dets);
			}
			
			//System.out.println(matrizCuentasRequest.getEncabezadoFileRequest().getOrdenanteRequest().getBanco());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public ParametrosMCRequest parametrosMC() {
		// TODO Auto-generated method stub
		/*String archivo = "C:\\MigracionWeb\\Certificaciones\\POA\\MC\\ParametrosMC.txt";
		File fileArch = new File(archivo);
		List<ArchivoRequest> listArch = archivoService.leerArchvivo(fileArch);
		
		ParametrosMCRequest parametrosMC = new ParametrosMCRequest();
		String[] datos;
		for(ArchivoRequest arch: listArch) {
			System.out.println("Linea numero: " +arch.getNumeroLinea());
			System.out.println("Dato archivo: " + arch.getDatoLinea());
			datos = arch.getDatoLinea().split("-");
			//numero= datos[1];
			
			System.out.println("Valor: " + datos[1]);
			
			
			if(arch.getNumeroLinea() == 0) {
				parametrosMC.setInicio1(Integer.parseInt(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 1) {
				parametrosMC.setInicio2(Integer.parseInt(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 2) {
				parametrosMC.setClabeBancos(Integer.parseInt(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 3) {
				parametrosMC.setClabeBanco(Integer.valueOf(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 4) {
				parametrosMC.setNombreBanco(Integer.valueOf(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 5) {
				parametrosMC.setCuentaBanco(Integer.valueOf(datos[1]));
			}
			if(arch.getNumeroLinea() == 6) {
				parametrosMC.setNombreTBanco(Integer.valueOf(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 7) {
				parametrosMC.setRfcBanco(Integer.valueOf(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 8) {
				parametrosMC.setStatusBanco(Integer.valueOf(datos[1]));
			}
		}
		
		System.out.println(parametrosMC.toString());
		return parametrosMC;*/
		return null;
	}

}
