package com.bim.migracion.web.Service.Implement;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Conexion.DatabaseMysqlConfig;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Response.BeneficiarioResponse;
import com.bim.migracion.web.Response.ComparacionCdaResponse;
import com.bim.migracion.web.Response.DatosAdicionalesCdaResponse;
import com.bim.migracion.web.Response.DatosCdaMysql;
import com.bim.migracion.web.Response.DatosCdaSybase;
import com.bim.migracion.web.Response.DetalleErrorResponse;
import com.bim.migracion.web.Response.OrdenanteResponse;
import com.bim.migracion.web.Response.ValidacionCdaResponse;
import com.bim.migracion.web.Service.ArchivosService;
import com.bim.migracion.web.Service.DatasourceService;
import com.bim.migracion.web.Service.ValidacionCdaService;
import com.bim.migracion.web.Utilidades.Utilidades;

@Service
@Configuration
@PropertySource("file:C:/MigracionWeb/Validaciones/Parametros_CDA.properties")
public class ValidacionCdaServiceImpl implements ValidacionCdaService {

	@Autowired
	private DatasourceService dataService;

	@Autowired
	private PropiedadesSybase propiedades;

	@Autowired
	private Utilidades utilidades;

	@Autowired
	private ArchivosService archivoService;

	@Autowired
	private Environment env;

	@Autowired
	private PropiedadesSybase propiedadesSbe;

	List<DetalleErrorResponse> detalleErrorResonse = new ArrayList<>();

	List<ComparacionCdaResponse> comparacionCda = new ArrayList<>();

	StringBuffer claRas = new StringBuffer();

	@Override
	public void buscarRegistros() {
		// TODO Auto-generated method stub

		DataSourceRequest datasource = new DataSourceRequest();

		datasource = dataService.findByIdSbe(30);

		DataSourceSybase.setConexion(datasource.getIp(), datasource.getPuerto(), datasource.getBase(),
				datasource.getUsuario(), datasource.getPass());

		/*
		 * String url="jdbc:mysql://172.30.215.17:3306/CDADB"; String user = "root";
		 * String pass = "sibamex12";
		 * 
		 * Connection conexion = null; try { conexion =
		 * DriverManager.getConnection(url,user,pass); }catch (Exception e) { // TODO:
		 * handle exception e.printStackTrace(); }
		 * 
		 * if(conexion!=null) { try { conexion.close();
		 * System.out.println("Mysql CLOSE"); }catch (Exception e) { // TODO: handle
		 * exception e.printStackTrace(); } }
		 */

		String query = "SELECT TOP 10 * FROM SPORDPAG ORDER BY Orp_Numero DESC";
		ResultSet rs = propiedades.getResultsetGenerico(query);
		String numero = "";
		try {
			while (rs.next()) {
				numero = rs.getString("Orp_Numero");
				System.out.println("Numero: " + numero);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataSourceSybase.cerrarConexion();

		String campoValidar = "||1|10032023|100320230|125700|40150|GEM-INMOBIMEX|VICTOR PINEDA VELAZQUEZ|40|150180019480800160|PIVV711223LN5|INMOBILIARIO|NOMBRE AP PATERNO AP MATERNO|40|150180019480800160|PIVV711223LN5|450P TP01 CV TC40 1.0 40150|0.00|1.00|NA|NA|0|0|NA|0|0|00000100000100014054|PK6w4eIN84NaGOuR96p6S1TZh4tK2CYr1uAnxFOTKyRVbX8YrRxj2sxFFz6BnCXs8j+c3PBqrAEP6KSm3wvLhO2ew2hGxsCDTZ0cSR9qv5VuPYvwVcIhkeeMBUjBOAlkydba0HUKYBAlkQY/iov9ZnGm8MO6uagOy+EP8SXSLcPcPOFjtVO2jnkP5VAoq+bijju5W0VxWKvw9HMC2Efjn7L57W6ZA+DIVK475xb9dQaS/ghpKwi1gBA4vnthWRY7Q0JL84TR+RhP3QG5dWTvzRnRSKVTMrQbiDWAXU7Bsxkds1HCSzU9jluYayw9dmx6+pU6nYurTgHcV1lKStvMvQ==";

		List<String> listaCda = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			String campoValidar0 = "||" + i
					+ "|10032023|100320230|125700|40150|GEM-INMOBIMEX|VICTOR PINEDA VELAZQUEZ|40|150180019480800160|PIVV711223LN5|INMOBILIARIO|NOMBRE AP PATERNO AP MATERNO|40|150180019480800160|PIVV711223LN5|450P TP01 CV TC40 1.0 40150|0.00|1.00|NA|NA|0|0|NA|0|0|00000100000100014054|PK6w4eIN84NaGOuR96p6S1TZh4tK2CYr1uAnxFOTKyRVbX8YrRxj2sxFFz6BnCXs8j+c3PBqrAEP6KSm3wvLhO2ew2hGxsCDTZ0cSR9qv5VuPYvwVcIhkeeMBUjBOAlkydba0HUKYBAlkQY/iov9ZnGm8MO6uagOy+EP8SXSLcPcPOFjtVO2jnkP5VAoq+bijju5W0VxWKvw9HMC2Efjn7L57W6ZA+DIVK475xb9dQaS/ghpKwi1gBA4vnthWRY7Q0JL84TR+RhP3QG5dWTvzRnRSKVTMrQbiDWAXU7Bsxkds1HCSzU9jluYayw9dmx6+pU6nYurTgHcV1lKStvMvQ==";
			listaCda.add(campoValidar0);
		}

		campoValidar = campoValidar.replace("||", "|");

		campoValidar = campoValidar.replace("|", ",");

		System.out.println(campoValidar);

		String[] cadenacampoValidar = campoValidar.split(",");
		/*
		 * for(String campo: cadenacampoValidar) {
		 * 
		 * //System.out.println("******"); System.out.println(campo);
		 * 
		 * for(ArchivoRequest arch: listArch) { System.out.println("Linea: " +
		 * arch.numeroLinea); System.out.println("Datos: " + arch.datoLinea); }
		 * //System.out.println(cadenacampoValidar[i]); }
		 */
		System.out.println("TEST: " + env.getProperty("clave"));
		/*
		 * String archivo = "C:\\MigracionWeb\\Validaciones\\Parametros_CDA.txt"; File
		 * fileArch = new File(archivo);
		 * 
		 * List<ArchivoRequest> listArch = archivoService.leerArchvivo(fileArch);
		 */

		cadenacampoValidar(cadenacampoValidar);

	}

	@Override
	public void cadenacampoValidar(String[] cadenacampoValidar) {
		// TODO Auto-generated method stub
		ValidacionCdaResponse validaCdaResponse = new ValidacionCdaResponse();

		Map<Integer, String> mapHeader = new TreeMap<>();
		Map<Integer, String> mapDetalle = new TreeMap<>();
		for (int i = 0; i < cadenacampoValidar.length; i++) {
			String encabezado = "";
			int num = 0;
			String buscar = "";
			String[] campo;
			int minimo = 0;
			int maximo = 0;

			num = i;
			System.out.println("Campo: " + cadenacampoValidar[i] + " Posicion: " + num);
			buscar = env.getProperty("CDA." + num);
			campo = buscar.split(" ");
			// System.out.println("Campo: " + campo.length);
			encabezado = campo[0];
			minimo = Integer.parseInt(campo[2]);
			maximo = Integer.parseInt(campo[3]);
			Boolean valida;

			if (campo[1].equals("A")) {
				System.out.println("Buscar alfanumerico");
				valida = utilidades.validarCampo(cadenacampoValidar[i], minimo, maximo);
			} else {
				System.out.println("Buscar numerico");
				valida = utilidades.validarCampoNum(cadenacampoValidar[i], minimo, maximo);
			}

			// valida = utilidades.validarCampo("VICTOR ", 1, 18);

			if (valida) {
				System.out.println("Resultado: //***Campo correcto***\\");
			} else {
				System.out.println("Resultado: //***Campo erroneo***\\");
			}
			System.out.println("////************\\\\\\");

			if (num != 0) {
				mapHeader.put(num, encabezado);
				mapDetalle.put(num, cadenacampoValidar[i] + " - " + valida);
			}

		}

		validaCdaResponse.setMapHeader(mapHeader);
		validaCdaResponse.setInformacion(mapDetalle);

		for (Map.Entry<Integer, String> header : validaCdaResponse.getMapHeader().entrySet()) {
			System.out.println("Llave : " + header.getKey());
			System.out.println("Valor map: " + header.getValue());
		}

		for (Map.Entry<Integer, String> detalle : validaCdaResponse.getInformacion().entrySet()) {
			System.out.println("Llave : " + detalle.getKey());
			System.out.println("Valor map: " + detalle.getValue());
		}

		try {
			archivoService.generarExcel(validaCdaResponse, "C:\\MigracionWeb\\Validaciones\\Excel\\Test1.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void validarCdas() {
		// TODO Auto-generated method stub
		/*** Objetos ***/
		Map<String, DatosCdaSybase> cdaSybase = buscarCdasSybase();
		Map<String, DatosCdaMysql> cdaMysql = buscarCdasMysql(claRas);

		validadorMap(cdaSybase, cdaMysql);

		archivoService.generarExcelErrores(detalleErrorResonse);

		if (comparacionCda.size() > 0) {
			System.out.println("Se encontraron diferencias - generando archivo");
			archivoService.generarExcelComparacion(comparacionCda);
			comparacionCda.clear();
		}

		detalleErrorResonse.clear();
		// comparacionCda.clear();

	}

	@Override
	public Map<String, DatosCdaSybase> buscarCdasSybase() {
		// TODO Auto-generated method stub
		/*** Objetos ***/
		DataSourceRequest datasource = new DataSourceRequest();
		Map<String, DatosCdaSybase> mapCdaSybase = new TreeMap<>();

		Map<String, DatosCdaMysql> mapCdaMysql = new TreeMap<>();

		// StringBuffer texto = new StringBuffer();

		ResultSet rsSybase;

		// DatabaseMysqlConfig.getConnection();

		datasource = dataService.findByIdSbe(1);
		DataSourceSybase.setConexion(datasource.getIp(), datasource.getPuerto(), datasource.getBase(),
				datasource.getUsuario(), datasource.getPass());

		rsSybase = propiedadesSbe.getResultsetGenerico("SELECT Orp_Numero,Orp_TipPag,Orp_InsRec,\r\n"
				+ "CASE WHEN Orp_TipPag not in('30','31','32','33','34','35','36','05','06')\r\n"
				+ "THEN Orp_NomOrd\r\n" + "WHEN Orp_TipPag in('05','06') THEN 'NA'				ELSE  Pir_NoOrIn\r\n"
				+ "END AS NomOrdenante,\r\n" + "								\r\n"
				+ "CASE WHEN Orp_TipPag not in('30','31','32','33','34','35','36','05','06')\r\n"
				+ "THEN Orp_TiCuOr\r\n" + "WHEN Orp_TipPag in('05','06') THEN 'NA'				ELSE   Pir_TiCuOI \r\n"
				+ "END AS TipoCueOrd,\r\n" + "								\r\n"
				+ "CASE WHEN Orp_TipPag not in('30','31','32','33','34','35','36','05','06')\r\n"
				+ "THEN Orp_CueOrd\r\n"
				+ "WHEN Orp_TipPag in('05','06') THEN 'NA'				ELSE    Pir_CuOrIn  \r\n" + "END AS CueOrd,\r\n"
				+ "								\r\n"
				+ "CASE WHEN Orp_TipPag not in('30','31','32','33','34','35','36','05','06')\r\n"
				+ "THEN Orp_DatOrd\r\n" + "WHEN Orp_TipPag in('05','06') THEN 'NA'				ELSE Pir_RFCCOI   \r\n"
				+ "END AS RfcOrd,\r\n" + "								\r\n" + "CASE WHEN Orp_TipPag not in ('04')\r\n"
				+ "THEN Orp_NomBen\r\n" + "ELSE\r\n" + "'NA'\r\n" + "END OrpNomBen,\r\n"
				+ "CASE WHEN Orp_TipPag not in('31','04')\r\n" + "THEN Orp_TiCuBe\r\n" + "ELSE\r\n"
				+ "'NA' END AS OrpTiCuBe,\r\n" + "CASE WHEN Orp_TipPag not in('31','04')\r\n" + "THEN Orp_CueBen\r\n"
				+ "ELSE\r\n" + "'NA' END AS OrpCueBen,\r\n" + "CASE WHEN Orp_TipPag not in('31','04','06')\r\n"
				+ "THEN Orp_DatBen\r\n" + "ELSE\r\n" + "'NA' END AS OrpDatBen,\r\n"
				+ "CASE WHEN Orp_TipPag in('06')\r\n" + "THEN 'Concepto del Pago 1'\r\n" + "ELSE\r\n" + "Orp_ConPag\r\n"
				+ "END AS OrpConPag,Orp_IVAPro,Orp_Cantid,\r\n" + "							\r\n"
				+ "CASE WHEN  Orp_TipPag not in('22')\r\n" + "THEN 'NA'\r\n" + "ELSE\r\n"
				+ "Cod_NomBe2 END AS NomBenAd,\r\n" + "CASE WHEN  Orp_TipPag not in('22')\r\n" + "THEN 'NA'\r\n"
				+ "ELSE\r\n" + "Cod_RFCUR2 END AS RfcBenAd,\r\n" + "						\r\n"
				+ "CASE WHEN  Orp_TipPag not in('22')\r\n" + "THEN 0\r\n" + "ELSE\r\n"
				+ "Cod_TiCuB2 END AS TipoCuenBenAd,\r\n" + "								\r\n"
				+ "CASE WHEN  Orp_TipPag not in('22')\r\n" + "THEN '0'\r\n" + "ELSE\r\n"
				+ "Cod_CuBen2 END AS CuentaBenAd,\r\n" + "Orp_ClaRas\r\n" + "								\r\n"
				+ "FROM SPORDPAG \r\n" + "LEFT JOIN SPPAINRE ON  Pir_OrpNum = Orp_Numero\r\n"
				+ "INNER JOIN SPCODEAB ON  Cda_OrPaRe = Orp_Numero\r\n"
				+ "LEFT JOIN SPCOBDIG ON  Cda_OrPaRe = Cod_OrpNum\r\n"
				+ "WHERE Orp_Numero>'3195828' AND Orp_Status='P' AND Orp_StaEnv='A' AND Orp_Tipo='R'\r\n"
				+ "ORDER BY Orp_Numero ASC");

		// rsSybase = propiedadesSbe.getResultsetGenerico("SELECT Orp_ClaRas FROM
		// SPHISORD WHERE Orp_Numero>'3134548' AND Orp_Numero<='3144448'");

		// rsContin= propiedadesSbe.getResultsetGenericoMysql("SELECT * FROM cda_info
		// WHERE cda_folio>70359472 AND cda_folio<=70359694");

		try {
			String claveRas;
			// int i = 0;
			while (rsSybase.next()) {
				// int OI
				DatosCdaSybase datosCdaSybase = new DatosCdaSybase();
				OrdenanteResponse ordenResponse = new OrdenanteResponse();
				BeneficiarioResponse benefResponse = new BeneficiarioResponse();
				DatosAdicionalesCdaResponse datosAdResponse = new DatosAdicionalesCdaResponse();

				claveRas = rsSybase.getString("Orp_ClaRas").trim();

				ordenResponse.setBanco(rsSybase.getString("Orp_InsRec").trim());
				ordenResponse.setNombre(rsSybase.getString("NomOrdenante"));
				ordenResponse.setTipoCuenta(rsSybase.getString("TipoCueOrd"));
				ordenResponse.setCuenta(rsSybase.getString("CueOrd"));
				ordenResponse.setRfc(rsSybase.getString("RfcOrd"));

				benefResponse.setTipoCuenta(rsSybase.getString("OrpTiCuBe").trim());
				benefResponse.setCuenta(rsSybase.getString("OrpCueBen").trim());
				benefResponse.setRfc(rsSybase.getString("OrpDatBen").trim());

				datosAdResponse.setTipoPago(rsSybase.getString("Orp_TipPag"));
				datosAdResponse.setConcepto(rsSybase.getString("OrpConPag"));
				// System.out.println("Rfc adi: " + );
				datosAdResponse.setRfcBenAdi(rsSybase.getString("RfcBenAd"));
				// System.out.println("Rfc adi: " + datosAdResponse.getRfcBenAdi());
				datosAdResponse.setTipoCuentaBenAdi(rsSybase.getString("TipoCuenBenAd"));
				// System.out.println("tip cuenta adi: " +
				// datosAdResponse.getTipoCuentaBenAdi());
				datosAdResponse.setCuentaBenAdi(rsSybase.getString("CuentaBenAd"));

				datosCdaSybase.setOrdResponse(ordenResponse);
				datosCdaSybase.setBenResponse(benefResponse);
				datosCdaSybase.setDatosAdResponse(datosAdResponse);

				mapCdaSybase.put(claveRas, datosCdaSybase);

				claRas.append("'" + claveRas + "',");
			}

			claRas.deleteCharAt(claRas.length() - 1);

			validadorMap(mapCdaSybase, mapCdaMysql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataSourceSybase.cerrarConexion();

		return mapCdaSybase;
	}

	@Override
	public Map<String, DatosCdaMysql> buscarCdasMysql(StringBuffer claRas) {
		// TODO Auto-generated method stub
		Map<String, DatosCdaMysql> mapMysql = new TreeMap<>();
		List<String> listaCdasTest = new ArrayList<>();

		ResultSet rsMysql;
		String cadena;
		String claRasMysql;

		try {
			System.out.println("Realizando conexion a base Mysql");
			rsMysql = propiedadesSbe.getResultsetGenericoMysql(
					"SELECT * FROM cda_info WHERE clave_rastreo IN (" + claRas + ") ORDER BY orp_paquete ASC");

			String[] cdaMysql;

			while (rsMysql.next()) {
				DatosCdaMysql datosCdaMySql = new DatosCdaMysql();
				OrdenanteResponse ordenResponse = new OrdenanteResponse();
				BeneficiarioResponse benefResponse = new BeneficiarioResponse();
				DatosAdicionalesCdaResponse datosAdResponse = new DatosAdicionalesCdaResponse();

				cadena = rsMysql.getString("cadena_original");

				claRasMysql = rsMysql.getString("clave_rastreo");

				cadena = cadena.replace("||", "|");

				cadena = cadena.replace("|", ",");

				cadena = cadena.replace("|", ",");

				listaCdasTest.add(cadena);

				cdaMysql = cadena.split(",");

				ordenResponse.setBanco(cdaMysql[5].trim());
				ordenResponse.setNombre(cdaMysql[7]);
				ordenResponse.setTipoCuenta(cdaMysql[8]);
				ordenResponse.setCuenta(cdaMysql[9]);
				ordenResponse.setRfc(cdaMysql[10]);

				benefResponse.setTipoCuenta(cdaMysql[13]);
				benefResponse.setCuenta(cdaMysql[14]);
				benefResponse.setRfc(cdaMysql[15]);

				datosAdResponse.setConcepto(cdaMysql[16]);
				datosAdResponse.setRfcBenAdi(cdaMysql[20]);
				datosAdResponse.setTipoCuentaBenAdi(cdaMysql[21]);
				datosAdResponse.setCuentaBenAdi(cdaMysql[22]);

				/*
				 * datosCdaMySql.setEmisorCDA(Integer.parseInt(cdaMysql[5]));
				 * datosCdaMySql.setNombreOrd(cdaMysql[7]);
				 * datosCdaMySql.setTipoCueOrd(Integer.parseInt(cdaMysql[8]));
				 * datosCdaMySql.setCueOrd(cdaMysql[9]); datosCdaMySql.setRfcOrd(cdaMysql[10]);
				 */

				datosCdaMySql.setOrdResponse(ordenResponse);
				datosCdaMySql.setBenResponse(benefResponse);
				datosCdaMySql.setDatosAdResponse(datosAdResponse);

				mapMysql.put(claRasMysql, datosCdaMySql);
			}
			System.out.println("Realizar validaciones");
			Map<Integer, String> mapCdas = validarCda(listaCdasTest);
			System.out.println("Generando CSV");
			archivoService.generarCsv(mapCdas);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		DatabaseMysqlConfig.cerrarConexion();

		return mapMysql;
	}

	private void validadorMap(Map<String, DatosCdaSybase> mapCdaSybase, Map<String, DatosCdaMysql> mapCdaMysql) {
		// TODO Auto-generated method stub
		// System.out.println("Validador mapas");

		for (Entry<String, DatosCdaSybase> dat : mapCdaSybase.entrySet()) {

			// System.out.println(dat.getKey());
			// System.out.println("ClaRas: " + dat.getKey());
			for (Entry<String, DatosCdaMysql> dats : mapCdaMysql.entrySet()) {
				// System.out.println("ClaRas Mysql: " + dats.getKey());

				if (dats.getKey().equals(dat.getKey())) {

					// System.out.println("Clave rastreo: " + dat.getKey());

					// System.out.println("cUENTA ORDS---: " +
					// dat.getValue().getOrdResponse().getCuenta());

					// .out.println("cUENTA ORDS--- " +
					// dats.getValue().getOrdResponse().getCuenta());

					if (!dats.getValue().getOrdResponse().getBanco()
							.equals(dat.getValue().getOrdResponse().getBanco())) {
						// System.out.println("Emisor CDA: " +
						// dat.getValue().getOrdResponse().getBanco());
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase("Banco Ord - " + dat.getValue().getOrdResponse().getBanco());
						// comparar.setCampoSybase(dat.getValue().getOrdResponse().getBanco());
						comparar.setCampoMysql(dats.getValue().getOrdResponse().getBanco());

						comparacionCda.add(comparar);
					}

					if (!dats.getValue().getOrdResponse().getNombre()
							.equals(dat.getValue().getOrdResponse().getNombre())) {
						// System.out.println("Nombre ordenante : " +
						// dat.getValue().getOrdResponse().getBanco());
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase("Nombre Ord - " + dat.getValue().getOrdResponse().getNombre());
						comparar.setCampoMysql(dats.getValue().getOrdResponse().getNombre());
						comparacionCda.add(comparar);
					}

					if (!dats.getValue().getOrdResponse().getTipoCuenta()
							.equals(dat.getValue().getOrdResponse().getTipoCuenta())) {
						// System.out.println("TipoCuenta ordenante : " +
						// dat.getValue().getOrdResponse().getTipoCuenta());
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase("Tipo cuenta Ord - " + dat.getValue().getOrdResponse().getTipoCuenta());
						// comparar.setCampoSybase(dat.getValue().getOrdResponse().getTipoCuenta());
						comparar.setCampoMysql(dats.getValue().getOrdResponse().getTipoCuenta());
						comparacionCda.add(comparar);
					}

					if (!dats.getValue().getOrdResponse().getRfc().equals(dat.getValue().getOrdResponse().getRfc())) {
						// System.out.println("RFC ordenante - " +
						// dat.getValue().getOrdResponse().getRfc());
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase(dat.getValue().getOrdResponse().getRfc());
						comparar.setCampoMysql(dats.getValue().getOrdResponse().getRfc());
						comparacionCda.add(comparar);
					}

					// System.out.println("cUENTA BEN : " +
					// dat.getValue().getBenResponse().getCuenta());
					// System.out.println("cUENTA BEN : " +
					// dats.getValue().getBenResponse().getCuenta());
					if (!dats.getValue().getOrdResponse().getCuenta()
							.equals(dat.getValue().getOrdResponse().getCuenta())) {
						// System.out.println("Cuenta diferente - " +
						// dats.getValue().getBenResponse().getCuenta() + "--" +
						// dat.getValue().getBenResponse().getCuenta());
						// if(!dat.getValue().getDatosAdResponse().getTipoPago().equals("31")) {
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase("Cuenta Ord- " + dat.getValue().getOrdResponse().getCuenta());
						// comparar.setCampoSybase(dat.getValue().getBenResponse().getCuenta());
						comparar.setCampoMysql(dats.getValue().getOrdResponse().getCuenta());
						comparacionCda.add(comparar);

						// }

					}

					// System.out.println("tIPO cUENTA BEN : " +
					// dat.getValue().getBenResponse().getTipoCuenta());
					// System.out.println("tIPO cUENTA BEN : " +
					// dats.getValue().getBenResponse().getTipoCuenta());
					if (!dats.getValue().getBenResponse().getTipoCuenta()
							.equals(dat.getValue().getBenResponse().getTipoCuenta())) {
						// System.out.println("TipoCuenta ben : " +
						// dat.getValue().getOrdResponse().getTipoCuenta());
						// if(!dat.getValue().getDatosAdResponse().getTipoPago().equals("31")) {
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase("Tipo cuenta Ben - " + dat.getValue().getBenResponse().getTipoCuenta());
						comparar.setCampoMysql(dats.getValue().getBenResponse().getTipoCuenta());
						comparacionCda.add(comparar);
						// }

					}

					if (!dats.getValue().getBenResponse().getRfc().equals(dat.getValue().getBenResponse().getRfc())) {
						// System.out.println("RFC BEN : " + dat.getValue().getBenResponse().getRfc());
						// if(!dat.getValue().getDatosAdResponse().getTipoPago().equals("31")) {

						// System.out.println("RFC BEN : " + dat.getValue().getBenResponse().getRfc());
						// System.out.println("RFC BEN : " + dats.getValue().getBenResponse().getRfc());

						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase("Rfc Ben - " + dat.getValue().getBenResponse().getRfc());
						// comparar.setCampoSybase(dat.getValue().getBenResponse().getRfc());
						comparar.setCampoMysql(dats.getValue().getBenResponse().getRfc());
						comparacionCda.add(comparar);
						// }

					}

					if (!dats.getValue().getDatosAdResponse().getConcepto()
							.equals(dat.getValue().getDatosAdResponse().getConcepto())) {
						// System.out.println("Concepto : " + dat.getValue().getBenResponse().getRfc());
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase("Concepto - " + dat.getValue().getDatosAdResponse().getConcepto());
						// comparar.setCampoSybase(dat.getValue().getDatosAdResponse().getConcepto());
						comparar.setCampoMysql(dats.getValue().getDatosAdResponse().getConcepto());
						comparacionCda.add(comparar);
					}

					if (!dats.getValue().getDatosAdResponse().getRfcBenAdi()
							.equals(dat.getValue().getDatosAdResponse().getRfcBenAdi())) {
						// System.out.println("Rfc adi : " + dat.getValue().getBenResponse().getRfc());
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase("Rfc adi - " + dat.getValue().getDatosAdResponse().getRfcBenAdi());
						// comparar.setCampoSybase(dat.getValue().getDatosAdResponse().getRfcBenAdi());
						comparar.setCampoMysql(dats.getValue().getDatosAdResponse().getRfcBenAdi());
						comparacionCda.add(comparar);
					}

					if (!dats.getValue().getDatosAdResponse().getTipoCuentaBenAdi()
							.equals(dat.getValue().getDatosAdResponse().getTipoCuentaBenAdi())) {
						// System.out.println("tipo cuenta adi : " +
						// dat.getValue().getBenResponse().getRfc());
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase(
								"Tipo cuenta adi - " + dat.getValue().getDatosAdResponse().getTipoCuentaBenAdi());
						// comparar.setCampoSybase(dat.getValue().getDatosAdResponse().getTipoCuentaBenAdi());
						comparar.setCampoMysql(dats.getValue().getDatosAdResponse().getTipoCuentaBenAdi());
						comparacionCda.add(comparar);
					}

					// System.out.println("test-" +
					// dats.getValue().getDatosAdResponse().getCuentaBenAdi() + "---" +
					// dat.getValue().getDatosAdResponse().getCuentaBenAdi());
					if (!dats.getValue().getDatosAdResponse().getCuentaBenAdi()
							.equals(dat.getValue().getDatosAdResponse().getCuentaBenAdi())) {
						// System.out.println("cuenta adi : " +
						// dat.getValue().getBenResponse().getRfc());
						ComparacionCdaResponse comparar = new ComparacionCdaResponse();
						comparar.setClaRas(dat.getKey());
						comparar.setCampoSybase(
								"cuenta adi - " + dat.getValue().getDatosAdResponse().getCuentaBenAdi());
						// comparar.setCampoSybase(dat.getValue().getDatosAdResponse().getCuentaBenAdi());
						comparar.setCampoMysql(dats.getValue().getDatosAdResponse().getCuentaBenAdi());
						comparacionCda.add(comparar);
					}

					break;
				}
			}
		}

	}

	@Override
	public Map<Integer, String> validarCda(List<String> listaCdas) {
		// TODO Auto-generated method stub

		Map<Integer, String> mapCdas = new TreeMap<>();

		for (int i = 0; i < listaCdas.size(); i++) {

			// System.out.println("Valor: " + listaCdas);
			String mapCda = validaCda(listaCdas.get(i), i);
			mapCdas.put(i, mapCda);
		}

		// System.out.println("Fin");
		return mapCdas;
	}

	@Override
	public String validaCda(String validaCda, int lineaLeida) {
		// TODO Auto-generated method stub

		/*** Objetos ****/
		StringBuffer texto = new StringBuffer();

		String[] validaCdas = validaCda.split(",");
		String buscar = "";
		String[] campo;
		int minimo = 0;
		int maximo = 0;

		Boolean valida = false;
		String textos = "";
		String bandera = "";

		String tipoPago = validaCdas[1];
		int longitudCda = 0;
		// System.out.println("-------Tipo de pago: " + validaCdas[1]);

		DetalleErrorResponse detalleErrorLong = new DetalleErrorResponse();

		/*if (tipoPago.equals("30") || tipoPago.equals("31") || tipoPago.equals("32") || tipoPago.equals("33")
				|| tipoPago.equals("34") || tipoPago.equals("35") || tipoPago.equals("36")) {
			longitudCda = 44;
			if (validaCdas.length != 44) {
				detalleErrorLong.setNumeroLinea(lineaLeida);
				detalleErrorLong.setCampoError("Longitud de CDA incorrecta TP " + validaCdas[1]);
				detalleErrorLong.setDetalleError(String.valueOf(validaCdas.length));
				detalleErrorResonse.add(detalleErrorLong);
			}
		} else {
			longitudCda = 27;
			if (validaCdas.length != longitudCda) {
				detalleErrorLong.setNumeroLinea(lineaLeida);
				detalleErrorLong.setCampoError("Longitud de CDA incorrecta TP " + validaCdas[1]);
				detalleErrorLong.setDetalleError(String.valueOf(validaCdas.length));
				detalleErrorResonse.add(detalleErrorLong);
			}
		}*/

		for (int i = 0; i < validaCdas.length; i++) {
			/* Objeto */
			DetalleErrorResponse detalleError = new DetalleErrorResponse();
			buscar = env.getProperty("CDA." + i);
			campo = buscar.split(" ");

			minimo = Integer.parseInt(campo[2]);
			maximo = Integer.parseInt(campo[3]);

			// Validar tp5
			String campoNA = "";

			if (i > 0) {
				if (campo[1].equals("A")) {
					// System.out.println("Buscar alfanumerico");
					valida = utilidades.validarCampo(validaCdas[i], minimo, maximo);
				} else {
					// System.out.println("Buscar numerico");
					if (i > 0) {
						valida = utilidades.validarCampoNum(validaCdas[i], minimo, maximo);
					}

				}
			}

			if (validaCdas[1].equals("5")) {

				if (i >= 7 && i <= 10) {
					campoNA = env.getProperty("CDA.TP5." + i);
					// System.out.println("Map: " + validaCdas[i] + "---------" + campoNA);
					if (validaCdas[i].equals(campoNA)) {
						bandera = "OK";
						valida = true;
					} else {
						// System.out.println("Campo incorrecto ------------------------");
						bandera = "NOK";
						valida = false;
					}
				}
			}

			if (validaCdas[1].equals("4")) {

				if (i >= 12 && i <= 15) {
					campoNA = env.getProperty("CDA.TP4." + i);
					// System.out.println("Map: " + validaCdas[i] + "---------" + campoNA);
					if (validaCdas[i].equals(campoNA)) {
						// System.out.println("Campo correcto ------------------------");
						bandera = "OK";
						valida = true;
					} else {
						// System.out.println("Campo incorrecto ------------------------");
						bandera = "NOK";

						valida = false;
					}
				}
			}

			if (validaCdas[1].equals("6")) {

				if (i >= 7 && i <= 10) {
					campoNA = env.getProperty("CDA.TP6." + i);
					// System.out.println("Map: " + validaCdas[i] + "---------" + campoNA);
					if (validaCdas[i].equals(campoNA)) {
						bandera = "OK";
						valida = true;
					} else {
						// System.out.println("Campo incorrecto ------------------------");
						bandera = "NOK";
						valida = false;
					}
				}
			}

			if (validaCdas[1].equals("31")) {

				if (i >= 12 && i <= 15) {
					campoNA = env.getProperty("CDA.TP31." + i);
					// System.out.println("Map: " + validaCdas[i] + "---------" + campoNA);
					if (validaCdas[i].equals(campoNA)) {
						// System.out.println("Campo correcto ------------------------");
						bandera = "OK";
						valida = true;
					} else {
						// System.out.println("Campo incorrecto ------------------------");
						bandera = "NOK";

						valida = false;
					}
				}
			}

			if (valida) {
				// System.out.println("Resultado: //***Campo correcto***\\");
				bandera = "OK";
			} else {
				// System.out.println("Resultado: //***Campo erroneo***\\");
				bandera = "NOK";
				// System.out.println("Error en linea: " + lineaLeida + " - " + campo[0] + " - "
				// + validaCdas[i]);

				if (i != 0) {
					detalleError.setNumeroLinea(lineaLeida);
					detalleError.setCampoError(campo[0]);
					detalleError.setDetalleError(validaCdas[i]);
					detalleError.setSolucion(campo[1] + " - " + maximo);

					detalleErrorResonse.add(detalleError);
				}

			}

			if (i > 0) {
				texto.append(validaCdas[i] + " - " + bandera);
				texto.append(",");
			}

		}
		textos = texto.toString();

		return textos;
	}

}
