package com.bim.migracion.web.Utilidades.Implement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Conexion.DataSourceSybase;
//import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;
import com.bim.migracion.web.Request.ArchivosCoasRequest;
import com.bim.migracion.web.Request.BeneficiarioRequest;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.EnvioOrdenesRequest;
import com.bim.migracion.web.Request.FilesRequest;
import com.bim.migracion.web.Request.OrdenantePirRequest;
import com.bim.migracion.web.Request.OrdenanteRequest;
import com.bim.migracion.web.Service.DatasourceService;
import com.bim.migracion.web.Service.SibamexService;
import com.bim.migracion.web.Utilidades.Utilidades;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@Configuration
@PropertySource("file:C:/MigracionWeb/Validaciones/Parametros_CDA.properties")
public class UtilidadesImpl implements Utilidades {
	
	@Autowired
	private DatasourceService dataService;

	@Autowired
	Environment env;
	
	@Autowired
	SibamexService sibamex;
	
	int idBase;
	String tipArch;
	DataSourceRequest dataSourceRequest = new DataSourceRequest();
	
	ArchivosCoasRequest archivosCoasReqiest;
	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public String obtenerFecha() {
		// TODO Auto-generated method stub
		String pattern = "ddMMhhmmss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		return date;
	}

	@Override
	public boolean validarCampo(String campo, int longMinima, int longMaxima) {
		// TODO Auto-generated method stub

		String reglas = env.getProperty("CDA.A");

		Boolean validacion;

		Pattern pat = Pattern.compile("^[" + reglas + "]{" + longMinima + "," + longMaxima + "}$");
		Matcher match = pat.matcher(campo);

		if (match.matches()) {
			validacion = true;
		} else {
			validacion = false;
		}
		return validacion;
	}

	@Override
	public boolean validarCampoNum(String campo, int longMinima, int longMaxima) {
		// TODO Auto-generated method stub
		Boolean validacion;
		//System.out.println("Campo validar " + campo + " min: " + longMinima + " max: " + longMaxima);

		Pattern pat = Pattern.compile("^[(0-9)(.)]{" + longMinima + "," + longMaxima + "}$");
		Matcher match = pat.matcher(campo);

		if (match.matches()) {
			validacion = true;
		} else {
			validacion = false;
		}
		//System.out.println("validacion: " + validacion);
		return validacion;
	}

	@Override
	public Map<Integer, String> leerArchivo(MultipartFile archivo) {
		// TODO Auto-generated method stub
		

		
		
		Map<Integer, String> mapaArchivo = new TreeMap<Integer, String>();
		
		String nombreArchivo = archivo.getOriginalFilename();

		String archivos="";
		String []nombreArchivos;
		nombreArchivos = nombreArchivo.split("-");
		
		archivos = nombreArchivos[1];
		
		String linea="";
		int numero = 1;
		try {
			InputStream inputStream = archivo.getInputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			mapaArchivo.put(0, archivos);
			while ((linea = br.readLine()) != null) {
				// System.out.println(linea);
				// System.out.println(numero);
				mapaArchivo.put(numero++, linea);
			}
			System.out.println("Archivo leido correctamente");
			//mapFileContingencia(mapaArchivo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error en archivo: ", e);
			e.printStackTrace();
		}
		
		return mapaArchivo;
		
	}
	
	@Override//Multiples archivos
	public Map<Integer, String> leerArchivos(String idConexion, MultipartFile[] archivos) {
		String linea="";
		int numero = 0;
	
		try {
			archivosCoasReqiest = mapper.readValue(idConexion, ArchivosCoasRequest.class);
			
			tipArch = archivosCoasReqiest.getTipArch();
			System.out.println(tipArch);
			idBase = archivosCoasReqiest.getIdBase();
			dataSourceRequest = dataService.findByIdSbe(archivosCoasReqiest.getIdBase());
			System.out.println(dataSourceRequest);
			DataSourceSybase.setConexion(dataSourceRequest.getIp(),dataSourceRequest.getPuerto(),dataSourceRequest.getBase(),dataSourceRequest.getUsuario(),dataSourceRequest.getPass());
			DataSourceSybase.getConnection();
			for (int i = 0; i < archivos.length; i++ ) {
				System.out.println("archivo: " + archivos[i].getOriginalFilename());
				InputStream inputStream = archivos[i].getInputStream();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				Map<Integer, String> mapaArchivo = new TreeMap<Integer, String>();
				mapaArchivo.put(0,archivos[i].getOriginalFilename());
				while ((linea = br.readLine()) != null) {
					mapaArchivo.put(numero++,linea);	
				}
				FilesRequest parametros = new FilesRequest();
				parametros.setFileReq(mapaArchivo);
				sibamex.ejecutarStore(tipArch, parametros);
				mapaArchivo.clear();			
				numero = 0;
			}
			DataSourceSybase.cerrarConexion();
		}catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@Override
	public EnvioOrdenesRequest envioOrdenesArchivo(Map<Integer, String> mapaArchivo) {
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
		
		String nombreArchivo="";
		
		while (it.hasNext()) {
			DetalleOrdenRequest detalle = new DetalleOrdenRequest();

			BeneficiarioRequest beneficiarioRequest = new BeneficiarioRequest();

			Integer key = it.next();

			String[] mapa = mapaArchivo.get(key).split("-");

			if(key==0) {
				nombreArchivo = mapa[0];
			}
			else if (key == 1) {
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

			} else  {
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

				
				
				if(mapa[1].equals("30") || mapa[1].equals("31")
						|| mapa[1].equals("32") || mapa[1].equals("33")
						|| mapa[1].equals("34")
						|| mapa[1].equals("35")
						|| mapa[1].equals("36")) {
					ordenantePirRequest.setTipoCuentaPir("40");
					ordenantePirRequest.setNombrePir("PIR VICTOR");
					ordenantePirRequest.setRfcPir("PIVV711223PIR");
					ordenantePirRequest.setCuentaPir("150990119400000014");
					//ordenanteRequest.setTipoCuenta("");
				}

				detalle.setOrdenante(ordenanteRequest);
				
				detalle.setOrdenantePir(ordenantePirRequest);

				detalle.setBeneficiario(beneficiarioRequest);
//1000P TP01 CV TC40 1.00 450
				if(mapa[8].equals("CV")) {
					concepto =  nombreArchivo  + " " + detalle.getCantidad() + "P TP" + detalle.getTipoPago() + " " + mapa[8] + " TC"
							+ detalle.getBeneficiario().getTipoCuenta() + " " + detalle.getMonto() + " "
							+ detalle.getBeneficiario().getBanco();
				}else {
					if(detalle.getTipoPago().equals("02") || detalle.getTipoPago().equals("04")
							|| detalle.getTipoPago().equals("07") || detalle.getTipoPago().equals("31")) {
						concepto = "";
					}else {
						concepto = nombreArchivo  + " " +  detalle.getCantidad() + "P TP" + detalle.getTipoPago() + " " + mapa[8] + " TC"
								+ detalle.getBeneficiario().getTipoCuenta() + " " + detalle.getMonto() + " "
								+ detalle.getBeneficiario().getBanco();
					}
					
				}
				/*concepto = archivoCarga + " " +  detalle.getCantidad() + "P TP" + detalle.getTipoPago() + " " + mapa[8] + " TC"
						+ detalle.getBeneficiario().getTipoCuenta() + " " + detalle.getMonto() + " "
						+ detalle.getBeneficiario().getBanco();*/

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

		
		envioOrdenesRequest.setNumArchivos(numeroArchivos);
		
		return envioOrdenesRequest;
	}

}
