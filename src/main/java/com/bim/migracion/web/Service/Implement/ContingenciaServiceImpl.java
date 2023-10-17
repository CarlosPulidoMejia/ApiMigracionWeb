package com.bim.migracion.web.Service.Implement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;
import com.bim.migracion.web.Request.BeneficiarioRequest;
import com.bim.migracion.web.Request.ContingenciaRequest;
import com.bim.migracion.web.Request.DataContingenciaRequest;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.EnvioOrdenesRequest;
import com.bim.migracion.web.Request.FirmadorRequest;
import com.bim.migracion.web.Request.OrdenantePirRequest;
import com.bim.migracion.web.Request.OrdenanteRequest;
import com.bim.migracion.web.Request.ValidadorPoaRequest;
import com.bim.migracion.web.Response.ContingenciaResponse;
import com.bim.migracion.web.Response.GenerarEnvResponse;
import com.bim.migracion.web.Service.ContingenciaService;
import com.bim.migracion.web.Service.ContingenciasService;
import com.bim.migracion.web.Service.DatasourceService;
import com.bim.migracion.web.Service.EnvioOrdenesService;
import com.bim.migracion.web.Service.ValidacionesContingenciaService;
import com.bim.migracion.web.Utilidades.Utilidades;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ContingenciaServiceImpl implements ContingenciaService {

	@Autowired
	private DatasourceService dataService;

	@Autowired
	private PropiedadesSybase propiedadesSbe;

	@Autowired
	private EnvioOrdenesService envioOrdenesService;

	@Autowired
	private ValidacionesContingenciaService validacionesContingenciaService;

	@Autowired
	private Utilidades utilidades;
	
	@Autowired
	private ContingenciasService contingenciasService;

	int idBase, idFirma;

	String generarEnv;

	int idTipoEnvio = 0;

	String tipoContingencia = "";

	String fechaCertificacion = "";

	static String nombreArchivo = "";

	String rutaContingencia = "C:\\MigracionWeb\\Certificaciones\\";

	DataSourceRequest dataSourceRequest = new DataSourceRequest();
	
	ContingenciaRequest continRequest = new ContingenciaRequest();

	String[] actividad;
	String archivoCarga;

	// private Logger logger = LogManager.getLogger();

	@Override
	public ContingenciaResponse consultarContingencia(ContingenciaRequest contingenciaRequest) {
		// TODO Auto-generated method stub
		ResultSet rsContin;

		DataSourceRequest dataSourceRequest = new DataSourceRequest();

		ContingenciaResponse continResponse = new ContingenciaResponse();

		@SuppressWarnings("unused")
		int numeroContin = 0;

		String query = "call SPCONTINCON '','','000662','','001','001','SP'";
		try {

			dataSourceRequest = dataService.findByIdSbe(contingenciaRequest.getIdBase());

			System.out.println(contingenciaRequest.toString());

			DataSourceSybase.setConexion(dataSourceRequest.getIp(), dataSourceRequest.getPuerto(),
					dataSourceRequest.getBase(), dataSourceRequest.getUsuario(), dataSourceRequest.getPass());

			rsContin = propiedadesSbe.getResultSetStoreGenerico(query);

			while (rsContin.next()) {

				if (rsContin.getString("Con_Status").equals("C")) {
					numeroContin = 1;
				} else {
					numeroContin = 2;
				}

				continResponse.setNumeroContin(rsContin.getInt("Tic_Numero"));
				continResponse.setTipoContin(rsContin.getString("Tic_Descri"));
				continResponse.setStatusContin(rsContin.getString("Con_Status"));
				// listaContinResponse.add(contingenciaResponse);

			}

			DataSourceSybase.cerrarConexion();
			// DataSourceSybase.cerrarConexion();
		} catch (Exception e) {
			// TODO: handle exception
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error", e);
			e.printStackTrace();
		}
		return continResponse;
	}

	@Override
	public void activarContingencia(ContingenciaRequest contingenciaRequest) {
		// TODO Auto-generated method stub
		DataSourceRequest dataSourceRequest = new DataSourceRequest();

		String query = "";
		String fecha = "";

		try {
			dataSourceRequest = dataService.findByIdSbe(contingenciaRequest.getIdBase());

			DataSourceSybase.setConexion(dataSourceRequest.getIp(), dataSourceRequest.getPuerto(),
					dataSourceRequest.getBase(), dataSourceRequest.getUsuario(), dataSourceRequest.getPass());

			DataSourceSybase.getConnection();

			fecha = propiedadesSbe.getOrpFecha();

			System.out.println("Fecha SBE: " + fecha);
			System.out.println(contingenciaRequest.toString());
			// query="call SPCONTINALT 1,'" + fecha + "','','','000662','" + fecha +
			// "','001','001','SP'";

			query = "call SPCONTINALT " + contingenciaRequest.getTipoContingencia() + ",'" + fecha
					+ "','','','000662','" + fecha + "','001','001','SP'";

			propiedadesSbe.ejecutaStore(query);

			System.out.println(query);
			DataSourceSybase.cerrarConexion();
		} catch (Exception e) {
			// TODO: handle exception
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error", e);
			e.printStackTrace();
		}
	}

	@Override
	public void offContingencia(ContingenciaRequest contingenciaRequest) {
		// TODO Auto-generated method stub
		DataSourceRequest dataSourceRequest = new DataSourceRequest();
		String query = "";
		try {
			dataSourceRequest = dataService.findByIdSbe(contingenciaRequest.getIdBase());

			DataSourceSybase.setConexion(dataSourceRequest.getIp(), dataSourceRequest.getPuerto(),
					dataSourceRequest.getBase(), dataSourceRequest.getUsuario(), dataSourceRequest.getPass());

			DataSourceSybase.getConnection();

			query = "UPDATE SPCONTIN SET Con_Status='C', Con_FecCie=getDate() where Con_Status='A'";

			propiedadesSbe.ejecutaQuery(query);

			DataSourceSybase.cerrarConexion();

		} catch (Exception e) {
			// TODO: handle exception
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error", e);
			e.printStackTrace();
		}
	}

	@Override
	public GenerarEnvResponse uploadFileContingencia(String data, MultipartFile file) {
		// TODO Auto-generated method stub
		DataContingenciaRequest dataContingenciaRequest;

		ObjectMapper mapper = new ObjectMapper();
		
		GenerarEnvResponse generarArchivoEnv = new GenerarEnvResponse();

		String mensaje = "Enviando registros contingencia: ";

		String nombreArchivo = file.getOriginalFilename();

		actividad = nombreArchivo.split("-");

		archivoCarga = actividad[1];

		try {
			dataContingenciaRequest = mapper.readValue(data, DataContingenciaRequest.class);

			idBase = dataContingenciaRequest.getIdBase();
			generarEnv = dataContingenciaRequest.getIsArchivoEnv();
			idTipoEnvio = Integer.parseInt(dataContingenciaRequest.getIdTipoContin());
			fechaCertificacion = dataContingenciaRequest.getFechaCert();
			idFirma = dataContingenciaRequest.getIdFirma();
			
			continRequest.setFechaCert(fechaCertificacion);
			continRequest.setIdBase(idBase);
			continRequest.setTipoContingencia(idTipoEnvio);

			if (idTipoEnvio == 2) {
				tipoContingencia = "POA";
				GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, mensaje + "POA", null);
			}

			if (idTipoEnvio == 1) {
				tipoContingencia = "COAS";
				GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, mensaje + tipoContingencia, null);
			}

			System.out.println("Datos post: " + dataContingenciaRequest.getFechaCert());

			System.out.println("Base Id: " + dataContingenciaRequest.getIdBase());
			dataSourceRequest = dataService.findByIdSbe(dataContingenciaRequest.getIdBase());

			System.out.println("Contingencia - " + tipoContingencia);
			// System.out.println("Contin base: " + dataSourceRequest.get);

			/*
			 * DataSourceSybase.setConexion(dataSourceRequest.getIp(),dataSourceRequest.
			 * getPuerto(),dataSourceRequest.getBase(),
			 * dataSourceRequest.getUsuario(),dataSourceRequest.getPass());
			 * 
			 * DataSourceSybase.getConnection();
			 */

			InputStream inputStream = file.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			/*
			 * if(idTipoEnvio==2) { //readFileContingencia(br,null,null); }else {
			 * readFileContingencia(br); }
			 */

			/* 21/06/23 */
			/* Se comenta para implementar otra forma de leer archivo */
			// readFileContingencia(br);

			leerArchivo(file);

			// readFileContingencia(br);

			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Finalizo envio por contingencia",
					null);

			// DataSourceSybase.cerrarConexion();
			String archivo = nombreArchivoEnv();
			generarArchivoEnv.setNombreArchivo(archivo);
			generarArchivoEnv.setContingencia(tipoContingencia);

		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error contingencia ", e);
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error contingencia ", e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error contingencia ", e);
			e.printStackTrace();
		}
		System.out.println("Fin mensaje");
		
		return generarArchivoEnv;
	}

	@Override
	public void readFileContingencia(BufferedReader reader) {
		// TODO Auto-generated method stub
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Leyendo archivo", null);
		Map<Integer, String> mapaArchivo = new TreeMap<Integer, String>();
		String linea;
		int numero = 1;
		try {
			while ((linea = reader.readLine()) != null) {
				// System.out.println(linea);
				// System.out.println(numero);
				mapaArchivo.put(numero++, linea);
			}
			mapFileContingencia(mapaArchivo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error contingencia: ", e);
			e.printStackTrace();
		}
	}

	@Override
	public void leerArchivo(MultipartFile file) {
		// TODO Auto-generated method stub
		Map<Integer, String> mapaArchivo = utilidades.leerArchivo(file);

		System.out.println("Iterando Mapa");

		for (Map.Entry<Integer, String> dato : mapaArchivo.entrySet()) {
			System.out.println(dato.getValue());
		}

		EnvioOrdenesRequest envioOrdenesRequest = utilidades.envioOrdenesArchivo(mapaArchivo);

		System.out.println("Tipo de envio archivo: " + idTipoEnvio);

		if (idTipoEnvio == 3) {
			dataSourceRequest = dataService.findByIdSbe(dataSourceRequest.getId());
			DataSourceSybase.setConexion(dataSourceRequest.getIp(), dataSourceRequest.getPuerto(),
					dataSourceRequest.getBase(), dataSourceRequest.getUsuario(), dataSourceRequest.getPass());

			for (int i = 0; i < envioOrdenesRequest.getOrdenes().size(); i++) {
				envioOrdenesContingencia(envioOrdenesRequest.getOrdenes().get(i));
			}

			DataSourceSybase.cerrarConexion();
		} else {

			dataSourceRequest = dataService.findByIdSbe(dataSourceRequest.getId());
			DataSourceSybase.setConexion(dataSourceRequest.getIp(), dataSourceRequest.getPuerto(),
					dataSourceRequest.getBase(), dataSourceRequest.getUsuario(), dataSourceRequest.getPass());
			cantidadFileContingencia(envioOrdenesRequest.getNumArchivos(), envioOrdenesRequest);
			DataSourceSybase.cerrarConexion();
		}

	}

	@Override
	public void mapFileContingencia(Map<Integer, String> mapaArchivo) {
		// TODO Auto-generated method stub
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Configurando archivo", null);
		EnvioOrdenesRequest envioOrdenesRequest = new EnvioOrdenesRequest();

		OrdenanteRequest ordenanteRequest = new OrdenanteRequest();
		OrdenantePirRequest ordenantePirRequest = new OrdenantePirRequest();

		List<DetalleOrdenRequest> listDetalle = new ArrayList<DetalleOrdenRequest>();

		Iterator<Integer> it = mapaArchivo.keySet().iterator();

		int numeroArchivos = 0;
		Long segundos = 0L;

		// System.out.println(it.toString());

		while (it.hasNext()) {
			DetalleOrdenRequest detalle = new DetalleOrdenRequest();

			BeneficiarioRequest beneficiarioRequest = new BeneficiarioRequest();

			Integer key = it.next();

			String[] mapa = mapaArchivo.get(key).split("-");

			if (key == 1) {
				numeroArchivos = Integer.parseInt(mapa[0]);
				System.out.println("Numero Repeticiones " + numeroArchivos);
				// tiempo = Long.parseLong(mapa[1]);
				segundos = Long.parseLong(mapa[1]);
			} else if (key == 2) {
				// System.out.println("1.1----" + detalle.getSegundos());
				ordenanteRequest.setBanco(mapa[0]);
				ordenanteRequest.setCuenta(mapa[1]);
				ordenanteRequest.setTipoCuenta(mapa[2]);
				ordenanteRequest.setNombre(mapa[3]);
				ordenanteRequest.setRfc(mapa[4]);

			} else {
				String concepto = "";
				detalle.setSegundos(segundos);
				detalle.setCantidad(Integer.parseInt(mapa[0]));
				detalle.setTipoPago(mapa[1]);
				detalle.setMonto(Double.parseDouble(mapa[2]));
				beneficiarioRequest.setBanco(mapa[3]);
				beneficiarioRequest.setCuenta(mapa[4]);
				beneficiarioRequest.setTipoCuenta(mapa[5]);
				beneficiarioRequest.setNombre(mapa[6]);
				beneficiarioRequest.setRfc(mapa[7]);
				detalle.setModulo(mapa[9]);

				detalle.setRegistro(false);

				if (mapa[1].equals("30") || mapa[1].equals("31") || mapa[1].equals("32") || mapa[1].equals("33")
						|| mapa[1].equals("34") || mapa[1].equals("35") || mapa[1].equals("36")) {
					ordenantePirRequest.setTipoCuentaPir("40");
					ordenantePirRequest.setNombrePir("PIR VICTOR");
					ordenantePirRequest.setRfcPir("PIVV711223PIR");
					ordenantePirRequest.setCuentaPir("150990119400000014");
					// ordenanteRequest.setTipoCuenta("");
				}

				detalle.setOrdenante(ordenanteRequest);

				detalle.setOrdenantePir(ordenantePirRequest);

				detalle.setBeneficiario(beneficiarioRequest);
//1000P TP01 CV TC40 1.00 450
				if (mapa[8].equals("CV")) {
					concepto = archivoCarga + " " + detalle.getCantidad() + "P TP" + detalle.getTipoPago() + " "
							+ mapa[8] + " TC" + detalle.getBeneficiario().getTipoCuenta() + " " + detalle.getMonto()
							+ " " + detalle.getBeneficiario().getBanco();
				} else {
					if (detalle.getTipoPago().equals("02") || detalle.getTipoPago().equals("04")
							|| detalle.getTipoPago().equals("07") || detalle.getTipoPago().equals("31")) {
						concepto = "";
					} else {
						concepto = archivoCarga + " " + detalle.getCantidad() + "P TP" + detalle.getTipoPago() + " "
								+ mapa[8] + " TC" + detalle.getBeneficiario().getTipoCuenta() + " " + detalle.getMonto()
								+ " " + detalle.getBeneficiario().getBanco();
					}

				}
				/*
				 * concepto = archivoCarga + " " + detalle.getCantidad() + "P TP" +
				 * detalle.getTipoPago() + " " + mapa[8] + " TC" +
				 * detalle.getBeneficiario().getTipoCuenta() + " " + detalle.getMonto() + " " +
				 * detalle.getBeneficiario().getBanco();
				 */

				if (mapa[8].equals("CV")) {
					detalle.setConcepto(concepto);
				} else {
					if (detalle.getTipoPago().equals("02") || detalle.getTipoPago().equals("04")
							|| detalle.getTipoPago().equals("07")) {
						detalle.setConcepto("");
					} else {
						detalle.setConcepto(concepto);
					}
				}

				listDetalle.add(detalle);

				envioOrdenesRequest.setOrdenes(listDetalle);

			}
		}

		System.out.println("/**** Cantidad archivos .env ****/" + numeroArchivos);

		// idTipoEnvio=3;
		if (idTipoEnvio == 3) {
			dataSourceRequest = dataService.findByIdSbe(dataSourceRequest.getId());
			DataSourceSybase.setConexion(dataSourceRequest.getIp(), dataSourceRequest.getPuerto(),
					dataSourceRequest.getBase(), dataSourceRequest.getUsuario(), dataSourceRequest.getPass());

			for (int i = 0; i < envioOrdenesRequest.getOrdenes().size(); i++) {
				envioOrdenesContingencia(envioOrdenesRequest.getOrdenes().get(i));
			}

			DataSourceSybase.cerrarConexion();
		} else {

			System.out.println("COAS POA");
			dataSourceRequest = dataService.findByIdSbe(dataSourceRequest.getId());
			DataSourceSybase.setConexion(dataSourceRequest.getIp(), dataSourceRequest.getPuerto(),
					dataSourceRequest.getBase(), dataSourceRequest.getUsuario(), dataSourceRequest.getPass());
			System.out.println("Num files: " + numeroArchivos);
			cantidadFileContingencia(numeroArchivos, envioOrdenesRequest);
			DataSourceSybase.cerrarConexion();
		}
	}

	@Override
	public void cantidadFileContingencia(int numeroArchivos, EnvioOrdenesRequest envioOrdenesRequest) {
		// TODO Auto-generated method stub
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Preparando envio", null);
		
		GenerarEnvResponse generarEnvResponse;
		int orxAr = ((envioOrdenesRequest.getOrdenes().size() % numeroArchivos) > 0)
				? (envioOrdenesRequest.getOrdenes().size() / numeroArchivos) + 1
				: envioOrdenesRequest.getOrdenes().size() / numeroArchivos;

		System.out.println("num archivos: " +  numeroArchivos);
		 System.out.println("ORXAR: " + orxAr); 
		 System.out.println("Porcentaje" +
		 (envioOrdenesRequest.getOrdenes().size() % numeroArchivos));
		 
		 System.out.println("Ordenes: " + envioOrdenesRequest.getOrdenes().size());


		// System.out.println("/**** Tamanio de lista ******/: " +
		// envioOrdenesRequest.getOrdenes().size());

		Integer numeroOrden = 0;
		for (int j = 0; j < numeroArchivos; j++) {
			System.out.println("valor j" + j);
			for (int i = 0; i < orxAr; i++) {
				System.out.println("Ingreso for 2");
				if (numeroOrden >= envioOrdenesRequest.getOrdenes().size()) {
					System.out.println("Salir de if ");
					break;
				}
				// enviarOrden(envioOrdenesRequest.getOrdenes().get(numeroOrden));
				// System.out.println("Numero orden: " + numeroOrden);

				// System.out.println("TP:---" +
				// envio.getOrdenes().get(numeroOrden).getTipoPago());

				envioOrdenesContingencia(envioOrdenesRequest.getOrdenes().get(numeroOrden));
				numeroOrden++;
			}

			/*21/06/23 */
			/*Se comenta llamado, se ingresa en metodo principal*/
			/*Se regresa if por temas de iteracion variable J*/
			if (generarEnv.equals("true")) {
				System.out.println("/***** Creando archivo .env ****/ " + j);
				// creaArchivoContingencia();
				/*29/06/23*/
				//generarEnvContingencia(dataSourceRequest);
				generarEnvResponse = contingenciasService.generarEnvContingencia(continRequest);
				
				nombreArchivo = generarEnvResponse.getNombreArchivo();
			}

		}

		// DataSourceSybase.cerrarConexion();
	}

	@Override
	public void envioOrdenesContingencia(DetalleOrdenRequest detalleOrdenRequest) {
		// TODO Auto-generated method stub

		System.out.println("Enviando archivos");
		/*
		 * DataSourceSybase.setConexion(dataSourceRequest.getIp(),dataSourceRequest.
		 * getPuerto(),dataSourceRequest.getBase(),
		 * dataSourceRequest.getUsuario(),dataSourceRequest.getPass());
		 * 
		 * DataSourceSybase.getConnection();
		 * 
		 * System.out.println("Concepto: " + detalleOrdenRequest.getConcepto());
		 * 
		 * DataSourceSybase.cerrarConexion();
		 */
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Enviando registros de archivo", null);

		FirmadorRequest firmaRequest = new FirmadorRequest();

		if (idFirma == 0) {
			firmaRequest.setFirmar(false);
		} else {
			firmaRequest.setFirmar(true);
			firmaRequest.setId(idFirma);
		}

		System.out.println("Ingresando ordenes");
		envioOrdenesService.insertaOrdenes(detalleOrdenRequest, firmaRequest, dataSourceRequest);
	}

	@Override
	public String generarEnvContingencia(DataSourceRequest dataSourceRequest) {
		// TODO Auto-generated method stub
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Generando archivo .env", null);
		FileWriter fw = null;
		PrintWriter pw = null;

		ValidadorPoaRequest poaRequest = new ValidadorPoaRequest();

		String newNameFile ="";
		
		String rutaArchivo="";
		
		if(idTipoEnvio==2) {
			rutaArchivo = "POA";
		}
		else if(idTipoEnvio==3) {
			rutaArchivo="COAS";
		}
		try {
			String store = "CALL SPPARCOPOPRO 1,1,1,'','SCO','000662','','001','001','SP'";
			propiedadesSbe.ejecutaStore(store);

			Map<Integer, String> archivo = generarFileContingencia();

			newNameFile = archivo.get(0);
			poaRequest.setFilesPoa(archivo);
			poaRequest.setNameFile(newNameFile);
			//poaRequest.setTipeContingency(tipoContingencia);
			poaRequest.setTipeContingency("COAS");
			System.out.println("Antes de validar campos" + poaRequest.getTipeContingency());
			validacionesContingenciaService.validaFormatoPoa(poaRequest, fechaCertificacion);
			
			tipoContingencia="COAS";

			fw = new FileWriter(rutaContingencia + "\\"+tipoContingencia+"\\env\\" + archivo.get(0) + "");

			pw = new PrintWriter(fw);

			pw.println(archivo.get(1));

			for (int i = 2; i < archivo.size(); i++) {
				pw.println(archivo.get(i));
			}

			pw.close();
			fw.close();

			String store2 = "CALL SPCONRECACT '" + archivo.get(0) + "',3,'','SCO','000662','','001','001','SP'";

			propiedadesSbe.ejecutaStore(store2);

			System.out.println("Tipo envio antes de archivo: " + idTipoEnvio);
			if(idTipoEnvio==2) {
				renameFile(newNameFile);
			}
			

			// DataSourceSybase.cerrarConexion();

			// actualizarArchivo(archivo.get(0));

		} catch (Exception e) {
			// TODO: handle exception
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error contingencia", e);
			e.printStackTrace();
		}

		
		String nombreFinal = "";
		idTipoEnvio=1;
		if(idTipoEnvio==1) {
			nombreFinal = newNameFile;
			nombreArchivo = nombreFinal;
		}else {
			nombreFinal = nombreArchivoEnv();
		}
		
		return nombreFinal;

	}

	@Override
	public Map<Integer, String> generarFileContingencia() {
		// TODO Auto-generated method stub
		Map<Integer, String> newMapa = new HashMap<>();
		ResultSet rs2;
		String archivos = "SELECT Sea_Estruc FROM SPESTACP ORDER BY Sea_Consec ASC";

		rs2 = propiedadesSbe.ejecutaQueryGenericaRst(archivos);

		int contador = 0;
		try {
			while (rs2.next()) {
				newMapa.put(contador++, rs2.getString("Sea_Estruc"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error contingencia", e);
			e.printStackTrace();
		}

		return newMapa;
	}

	@Override
	public String validarFilesContingencia(MultipartFile[] files) {
		// TODO Auto-generated method stub
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Validacion archivos", null);
		String nameFile = "";
		// TODO Auto-generated method stub

		try {
			/*
			 * Files.copy(files.getInputStream(),
			 * this.root.resolve(files.getOriginalFilename()));
			 */
			/*
			 * InputStream st = files.getInputStream().;
			 * 
			 * BufferedReader br = new BufferedReader(new InputStreamReader(st));
			 * 
			 * String linea;
			 * 
			 * 
			 * while((linea = br.readLine()) != null) { System.out.println(linea); }
			 */
			// BufferedReader br;
			for (int i = 0; i < files.length; i++) {
				// System.out.println("Valor i: " + files[i].getInputStream().toString());

				// br = new BufferedReader(new InputStreamReader(st));

				// System.out.println("Files1 " + files[i].getOriginalFilename());
				// System.out.println(validaFile);
				// validaFile = readFile(br,nameFile);
				/*
				 * if(i==0) { validaFile=true; }
				 */
				// if(validaFile) {
				// st = files[i].getInputStream();
				nameFile = files[i].getOriginalFilename();
				nombreArchivo = nameFile;
				// validaFile = readFile(files[i].getInputStream(),nameFile);
				readFileValidacionContingencia(files[i].getInputStream(), nameFile);
				// System.out.println(validaFile);

				// }
				// TimeUnit.SECONDS.sleep(15);

				// System.out.println(files[i].get);
			}
		} catch (Exception e) {
			// TODO: handle exception
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error contingencia", e);
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}

		return nameFile;
	}

	@Override
	public void readFileValidacionContingencia(InputStream inputStream, String nameFile) {
		// TODO Auto-generated method stub
		// System.out.println("BufferReader");
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Leyendo archivo a validar", null);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		Map<Integer, String> archivoPoa = new HashMap<>();
		ValidadorPoaRequest poaRequest = new ValidadorPoaRequest();

		try {
			String line;
			int counter = 1;
			archivoPoa.put(0, nameFile);
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				archivoPoa.put(counter, line);
				counter++;
			}

			// archivoPoa.forEach((k,v)->System.out.println("Key" + k + "valor:" + v));

			poaRequest.setFilesPoa(archivoPoa);
			poaRequest.setNameFile(nameFile);
			poaRequest.setTipeContingency(tipoContingencia);
			// System.out.println("Archivo leer: " + poaRequest.getNameFile());
			// validas= validatePoaService.validaFormatoPoa(poaRequest);

			validacionesContingenciaService.validaFormatoPoa(poaRequest, fechaCertificacion);
			// validas=true;
			archivoPoa.clear();

		} catch (Exception e) {
			// TODO: handle exception
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error contingencia", e);
			e.printStackTrace();
		}
	}

	@Override
	public void renameFile(String fileName) {
		// TODO Auto-generated method stub
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Renombrando archivo" + fileName, null);
		String newFilename = "";
		newFilename = fileName.substring(0, fileName.length() - 18);
		// String ruta="C:\\MigracionWeb\\Certificaciones\\POA";

		System.out.println("fecha cert " + fechaCertificacion);

		// String descargaArchivo = newFilename + fechaCertificacion +"090000" + ".env";
		if (fechaCertificacion.equals("")) {
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

			fechaCertificacion = simpleDateFormat.format(new Date());
		}

		fechaCertificacion = fechaCertificacion.replace("-", "");
		tipoContingencia = "POA";
		nombreArchivo = newFilename + fechaCertificacion + "090000" + ".env";
		File oldfile = new File(rutaContingencia + tipoContingencia + "\\env\\" + fileName);
		File newfile = new File(
				rutaContingencia + tipoContingencia + "\\env\\" + newFilename + fechaCertificacion + "090000" + ".env");

		if (oldfile.renameTo(newfile)) {
			System.out.println("Archivo renombrado .env");
		} else {
			System.out.println("No se renombro el archivo ENV " + fileName);
		}

		File oldfile1 = new File(rutaContingencia + tipoContingencia + "\\validaciones\\" + fileName);
		File newfile1 = new File(rutaContingencia + tipoContingencia + "\\validaciones\\" + newFilename
				+ fechaCertificacion + "090000" + ".env");

		if (oldfile1.renameTo(newfile1)) {
			System.out.println("Archivo renombrado validaciones");
		} else {
			System.out.println("No se renombro el archivo Validaciones" + fileName);
		}
	}

	@Override
	public String nombreArchivoEnv() {

		return nombreArchivo;
	}

}
