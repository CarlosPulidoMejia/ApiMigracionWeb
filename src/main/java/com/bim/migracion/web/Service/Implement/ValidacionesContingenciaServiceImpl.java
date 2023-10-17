package com.bim.migracion.web.Service.Implement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Entity.ReportePoaExcel;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;
import com.bim.migracion.web.Request.ValidadorPoaRequest;
import com.bim.migracion.web.Response.PoaPagosErroneosResponse;
import com.bim.migracion.web.Service.ArchivosService;
import com.bim.migracion.web.Service.ExcelService;
import com.bim.migracion.web.Service.ValidacionesContingenciaService;
import com.bim.migracion.web.Utilidades.Utilidades;

@Service
@Configuration
@PropertySource("file:C:/MigracionWeb/Validaciones/Parametros_POA.properties")
public class ValidacionesContingenciaServiceImpl implements ValidacionesContingenciaService {

	String validaAlfanumerico;
	String nombreArchivo;
	String nuevoCampoMod = "";

	@Autowired
	private ExcelService excelService;

	@Autowired
	private ArchivosService archivoService;

	@Autowired
	private Utilidades utilidades;

	@Autowired
	private Environment env;
	
	private String idTipoContin;

	@Override
	public void validaFormatoPoa(ValidadorPoaRequest validadorPoaRequest, String fecha) {
		// TODO Auto-generated method stub
		String tipoContingencia = validadorPoaRequest.getTipeContingency();
		idTipoContin = tipoContingencia;
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Validando registros", null);
		String nombreArchivo;
		ArrayList<ReportePoaExcel> listRepotePoaDTO = new ArrayList<ReportePoaExcel>();
		Map<Integer, String> archivoPoa = new HashMap<>();

		readFilePoa(validadorPoaRequest, listRepotePoaDTO, archivoPoa, fecha);
		// System.out.println("---"+validadorPoaRequest.getNameFile());
		// excelService.exportExcelPoa(validadorPoaRequest.getNameFile(),
		// listRepotePoaDTO);
		// createFileService.createFimePoaMap(archivoPoa);
		nombreArchivo = validadorPoaRequest.getNameFile();
		
		System.out.println("Nombre archivo: " +  nombreArchivo);

		nombreArchivo = nombreArchivo.substring(0, nombreArchivo.length() - 4);
		
		System.out.println("Nombre archivo 2: " +  nombreArchivo);

		excelService.exportExcelPoa(nombreArchivo, listRepotePoaDTO, tipoContingencia);
		createFilePoaMap(archivoPoa, tipoContingencia);

		listRepotePoaDTO.clear();
	}

	@Override
	public void readFilePoa(ValidadorPoaRequest validadorPoaRequest, ArrayList<ReportePoaExcel> listRepotePoaDTO,
			Map<Integer, String> archivoPoa, String fecha) {
		// TODO Auto-generated method stub
		String[] datosxlinea;

		Properties properties = new Properties();
		// InputStream propertiesStream =
		// ClassLoader.getSystemResourceAsStream("src\\main\\java\\com\\bim\\migracion\\web\\Service\\Implement\\LayoutTiposPagos.properties");

		InputStream propertiesStream = getClass().getResourceAsStream("LayoutTiposPagos.properties");

		Boolean validador;

		String campoA = "";

		String[] validaA;

		StringBuilder myString = new StringBuilder();

		String paqueteFile;

		/* Pagos erroneos POA */
		Map<Integer, PoaPagosErroneosResponse> pagosErroneos = new HashMap<Integer, PoaPagosErroneosResponse>();
		// Map<Integer, String> pagosErroneos = new HashMap<Integer,String>();

		StringBuffer textoPoa = new StringBuffer();

		try {

			properties.load(propertiesStream);

			propertiesStream.close();

			validadorPoaRequest.setTipeContingency("POA");
			System.out.println("Tipo contingencia: " + validadorPoaRequest.getTipeContingency());
			// if(validadorPoaRequest.getTipeContingency().equals("POA")) {
			campoA = env.getProperty("POA.A.VAL");
			// }
			// else if (validadorPoaRequest.getTipeContingency().equals("COAS")) {
			// campoA = properties.getProperty("COAS.A.VAL");
			// }

			// validaA = campoA.split(" ");

			// validaAlfanumerico = validaA[1];
			System.out.println("Validando Contingencia: " +  idTipoContin);
			if(idTipoContin.equals("POA")) {
				campoA = env.getProperty("POA.A.VAL");
				validadorPoaRequest.setTipeContingency("POA");
			}else {
				campoA = env.getProperty("COAS.A.VAL");
				validadorPoaRequest.setTipeContingency("COAS");
			}
			
			validaA = campoA.split(" ");
			validaAlfanumerico = validaA[1];

			System.out.println("Nuevo validador: " + validaAlfanumerico);

			Iterator<Integer> it = validadorPoaRequest.getFilesPoa().keySet().iterator();
			while (it.hasNext()) {
				ReportePoaExcel reportePoaExcel = new ReportePoaExcel();

				Integer key = it.next();

				datosxlinea = validadorPoaRequest.getFilesPoa().get(key).split("~");

				/** Errores TP poa */
				PoaPagosErroneosResponse poaPagos = new PoaPagosErroneosResponse();

				if (key == 0) {
					// System.out.println(validadorPoaRequest.getFilesPoa().get(key));

					nombreArchivo = validadorPoaRequest.getFilesPoa().get(key);
					archivoPoa.put(key, nombreArchivo);
				} else if (key == 1) {

					paqueteFile = nombreArchivo.substring(5, nombreArchivo.length() - 18);

					String n;
					for (int i = 0; i < datosxlinea.length; i++) {

						if (paqueteFile.equals(datosxlinea[2])) {
						} else {
							reportePoaExcel.setLinea(1);
							reportePoaExcel.setDetalleError("El paquete no corresponde con el consecutivo");
							reportePoaExcel.setDetalleSolucion("El paquete no corresponde con el consecutivo");
							reportePoaExcel.setInstitucion("");
							myString.append(datosxlinea[i]).append("~");
							listRepotePoaDTO.add(reportePoaExcel);
							break;

						}
						myString.append(datosxlinea[i]).append("~");

					}

					n = myString.toString();
					archivoPoa.put(key, n);
					myString.setLength(0);
				} else {
					// System.out.println(datosxlinea);
					for (int i = 0; i < datosxlinea.length; i++) {
						// System.out.println(datosxlinea[i] + "xyz");
						// System.out.println("Spring validaciones");
						// System.out.println(validadorPoaRequest.getTipeContingency() + ".TP" +
						// datosxlinea[1] + "." + (i + 1));

						if(idTipoContin.equals("POA")){
							if (!datosxlinea[1].equals("6") && !datosxlinea[1].equals("5")) {
								validador = validaCampo(
										env.getProperty(validadorPoaRequest.getTipeContingency() + ".TP"
												+ datosxlinea[1] + "." + (i + 1)),
										datosxlinea[i], key, validadorPoaRequest.getTipeContingency(), datosxlinea[1],
										listRepotePoaDTO);

								System.out.println("Datos archivo");
								System.out.println(env.getProperty(validadorPoaRequest.getTipeContingency() + ".TP" + datosxlinea[1] + "." + (i + 1) + "**") +  datosxlinea[i] + "**" + key + "**" + validadorPoaRequest.getTipeContingency() + datosxlinea[1] + "**");
								if (validador) {

									myString.append(datosxlinea[i]).append("~");

								} else {

									myString.append(nuevoCampoMod).append("~");

								}
							}
						}else {
							System.out.println("Validacion COAS");
							validador = validaCampo(
									env.getProperty(validadorPoaRequest.getTipeContingency() + ".TP"
											+ datosxlinea[1] + "." + (i + 1)),
									datosxlinea[i], key, validadorPoaRequest.getTipeContingency(), datosxlinea[1],
									listRepotePoaDTO);

							System.out.println("Datos archivo");
							System.out.println(env.getProperty(validadorPoaRequest.getTipeContingency() + ".TP" + datosxlinea[1] + "." + (i + 1) + "**") +  datosxlinea[i] + "**" + key + "**" + validadorPoaRequest.getTipeContingency() + datosxlinea[1] + "**");
							if (validador) {

								myString.append(datosxlinea[i]).append("~");

							} else {

								myString.append(nuevoCampoMod).append("~");

							}
						}
						
						// System.out.println("Vali " + validador);

					} // termina for

					if (!datosxlinea[1].equals("1") && !datosxlinea[1].equals("3") && !datosxlinea[1].equals("7")
							&& !datosxlinea[1].equals("0")) {
						reportePoaExcel.setLinea(key);
						reportePoaExcel.setDetalleError("Tipo de pago erroneo: " + datosxlinea[1]);
						reportePoaExcel.setDetalleSolucion("Institucion: " + datosxlinea[2]);
						listRepotePoaDTO.add(reportePoaExcel);

						poaPagos.setTipoPago(datosxlinea[1]);
						poaPagos.setBanco(datosxlinea[2]);

						// texto = datosxlinea[1]+","+datosxlinea[2];
						pagosErroneos.put(key, poaPagos);
					}

					if (datosxlinea[1].equals("1") && datosxlinea.length < 18) {
						myString.append("~");
					}

					String singleString = myString.toString();
					
					if(idTipoContin.equals("POA")){
						if (!datosxlinea[1].equals("6") && !datosxlinea[1].equals("5")) {
							archivoPoa.put(key, singleString);
						}
					}else {
						archivoPoa.put(key, singleString);
					}

					myString.setLength(0);

				}
			}
			Map<Integer, PoaPagosErroneosResponse> mapBens = new TreeMap<>(pagosErroneos);

			Map<String, String> frecuencia = new HashMap<String, String>();

			int num = 1;
			for (PoaPagosErroneosResponse poa : mapBens.values()) {
				if (frecuencia.containsKey(poa.getTipoPago() + poa.getBanco())) {

					frecuencia.put(poa.getTipoPago() + poa.getBanco(),
							poa.getTipoPago() + "," + poa.getBanco() + "," + num);
					num++;
				} else {
					num = 1;
					frecuencia.put(poa.getTipoPago() + poa.getBanco(),
							poa.getTipoPago() + "," + poa.getBanco() + "," + num);
					num++;
				}
			}

			for (Entry<String, String> conteo : frecuencia.entrySet()) {
				// if(!conteo.getValue().isEmpty()) {
				String[] test;
				test = conteo.getValue().split(",");
				textoPoa.append("La institucion " + test[1] + " envio " + test[2] + " registros TP" + test[0]);
				textoPoa.append("\n");
			}
			String fechaUtil = utilidades.obtenerFecha();
			archivoService.crearArchivo(textoPoa, "incidencias\\Poa Pagos erroneos_" + fechaUtil);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error validaciones", e);
			e.printStackTrace();
		}

		// readFail(listRepotePoaDTO);
	}

	@Override
	public Boolean validaCampo(String datos, String campo, Integer linea, String tipo, String tipoPago,
			ArrayList<ReportePoaExcel> listRepotePoaDTO) {
		
		System.out.println("Datos a buscar: " + datos);
		System.out.println("Campo a validar: " + campo);
		// TODO Auto-generated method stub
		ReportePoaExcel repotePoaDTO = new ReportePoaExcel();
		 System.out.println("Datos:" + datos + "---+" + linea + "X" + campo);
		String[] indicaciones = datos.split(" ");

		boolean valida = false;

		// String nuevoCampoMod;

		if (indicaciones[1].equals("N")) {
			if (validaNumero(indicaciones[2], indicaciones[3], campo)) {
				valida = true;
			} else {

				repotePoaDTO.setLinea(linea);
				repotePoaDTO.setDetalleError(indicaciones[0]);
				repotePoaDTO.setDetalleSolucion(campo);
				repotePoaDTO.setInstitucion("");

				listRepotePoaDTO.add(repotePoaDTO);

				valida = false;
			}
		} else if (indicaciones[1].equals("A")) {

			if (validaAlfanumerico(indicaciones[2], indicaciones[3], campo, validaAlfanumerico)) {
				valida = true;
			} else {

				nuevoCampoMod = modificacionCampo(tipo, indicaciones[1], campo, validaAlfanumerico);

				// System.out.println(nuevoCampoMod);
				repotePoaDTO.setLinea(linea);
				repotePoaDTO.setDetalleError(indicaciones[0]);
				repotePoaDTO.setDetalleSolucion(campo);
				repotePoaDTO.setInstitucion(nuevoCampoMod);

				listRepotePoaDTO.add(repotePoaDTO);

				valida = false;

			}

		}
		return valida;
	}

	@Override
	public boolean validaAlfanumerico(String numIni, String numFin, String campo, String validaAlfanumerico2) {
		// TODO Auto-generated method stub
		String regex = "[" + validaAlfanumerico2 + "]{" + numIni + "," + numFin + "}";
		return Pattern.matches(regex, campo);
	}

	@Override
	public boolean validaNumero(String numIni, String numFin, String campo) {
		// TODO Auto-generated method stub
		String regex = "^[\\d(.\\d)?]{" + numIni + "," + numFin + "}$";
		return Pattern.matches(regex, campo);
	}

	@Override
	public String modificacionCampo(String tipo, String tipoDato, String campo, String validaAlfanumerico2) {
		// TODO Auto-generated method stub
		String nuevoCampo = "";
		try {

			String nombre = "";
			@SuppressWarnings("unused")
			String[] indicaciones;

			Properties properties = new Properties();
			InputStream propertiesStream = ClassLoader.getSystemResourceAsStream("LayoutTiposPago.properties");

			// propertiesStream//
			properties.load(propertiesStream);
			propertiesStream.close();

			nombre = properties.getProperty(tipo + "." + tipoDato + ".VAL");

			indicaciones = nombre.split(" ");

			nuevoCampo = campo.replaceAll("[^" + validaAlfanumerico + "]", "");

		} catch (Exception e) {
			// TODO: handle exception
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error validaciones", e);
		}

		return nuevoCampo;
	}

	@Override
	public void createFilePoaMap(Map<Integer, String> filePoa, String tipoContingencia) {
		// TODO Auto-generated method stub
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Creando env validado", null);
		// String pathPoa="C:\\MigracionWeb\\Certificaciones\\"+ tipoContingencia +
		// "\\validaciones\\";
		String contingencia="";
		if(tipoContingencia.equals("COAS")) {
			contingencia="COAS";
		}else {
			contingencia="POA";
		}
		
		String pathPoa = "C:\\MigracionWeb\\Certificaciones\\"+contingencia+"\\validaciones\\";

		File file = new File(filePoa.get(0));

		BufferedWriter bf = null;

		/*
		 * String header = "";
		 * 
		 * String [] nuevoHeader;
		 * 
		 * header = filePoa.get(1);
		 * 
		 * nevheader.split("~");
		 * 
		 * for(int i = 0;i<header.)
		 */

		try {
			bf = new BufferedWriter(new FileWriter(pathPoa + file));

			for (Map.Entry<Integer, String> entry : filePoa.entrySet()) {
				if (entry.getKey() > 0) {
					/*
					 * System.out.println("Key = " + entry.getKey() + ", Value = " +
					 * entry.getValue());
					 */
					bf.write(entry.getValue());
					bf.newLine();
				}

			}
			bf.flush();
		} catch (IOException e) {
			// TODO: handle exception
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error validaciones", e);
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (Exception e) {
				// TODO: handle exception
				GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error validaciones",
						e);
				e.printStackTrace();
			}
		}
	}

}
