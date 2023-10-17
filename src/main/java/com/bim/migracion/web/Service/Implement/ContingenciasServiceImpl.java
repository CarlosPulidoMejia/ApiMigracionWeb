package com.bim.migracion.web.Service.Implement;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;
import com.bim.migracion.web.Request.ContingenciaRequest;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.ValidadorPoaRequest;
import com.bim.migracion.web.Response.ContingenciaResponse;
import com.bim.migracion.web.Response.GenerarEnvResponse;
import com.bim.migracion.web.Service.ContingenciasService;
import com.bim.migracion.web.Service.DatasourceService;
import com.bim.migracion.web.Service.ValidacionesContingenciaService;

@Service
public class ContingenciasServiceImpl implements ContingenciasService{
	
	@Autowired
	private PropiedadesSybase propiedadesSbe;
	
	@Autowired
	private ValidacionesContingenciaService validacionesContingenciaService;
	
	@Autowired
	private DatasourceService dataSourceService;
	
	
	
	public String nombreArchivoEnv="";
	
	DataSourceRequest datasource = new DataSourceRequest();

	@Override
	public ContingenciaResponse consultarContingencia(ContingenciaRequest contingenciaRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activarContingencia(ContingenciaRequest contingenciaRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void desactivarContingencia(ContingenciaRequest contingenciaRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GenerarEnvResponse generarEnvContingencia(ContingenciaRequest contingenciaRequest) {
		// TODO Auto-generated method stub
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Generando archivo .env", null);
		FileWriter fw = null;
		PrintWriter pw = null;

		ValidadorPoaRequest poaRequest = new ValidadorPoaRequest();
		
		GenerarEnvResponse generarEnv = new GenerarEnvResponse();
		
		String tipoContingencia="";
		
		String nombreArchivo="";
		
		String rutaContingencia = "C:\\MigracionWeb\\Certificaciones";
		
		String fechaCertificacion = contingenciaRequest.getFechaCert();
		
		if(contingenciaRequest.getTipoContingencia()==1) {
			tipoContingencia = "COAS";
		}else{
			tipoContingencia = "POA";
		}
		
		try {
			System.out.println("BASE: " + contingenciaRequest.getIdBase());
			datasource = dataSourceService.findByIdSbe(contingenciaRequest.getIdBase());
			DataSourceSybase.setConexion(datasource.getIp(),datasource.getPuerto(),datasource.getBase(),datasource.getUsuario(),datasource.getPass());		
			
			
			
			String store = "CALL SPPARCOPOPRO 1,1,1,'','SCO','000662','','001','001','SP'";
			propiedadesSbe.ejecutaStore(store);

			Map<Integer, String> archivo = generarFileContingencia();

			nombreArchivo = archivo.get(0);
			poaRequest.setFilesPoa(archivo);
			poaRequest.setNameFile(nombreArchivo);
			//poaRequest.setTipeContingency(tipoContingencia);
			poaRequest.setTipeContingency("COAS");
			System.out.println("Archivo a validar: " + poaRequest.getNameFile());
			System.out.println("Antes de validar campos" + poaRequest.getTipeContingency());
			
			validacionesContingenciaService.validaFormatoPoa(poaRequest, contingenciaRequest.getFechaCert());
			
			

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

			System.out.println("Tipo Contingencia antes de archivo: " + tipoContingencia);
			
			if(tipoContingencia.equals("POA")) {
				String newFilename = "";
				String nombreFinal = "";
				
				if (fechaCertificacion.equals("")) {
					String pattern = "yyyy-MM-dd";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

					fechaCertificacion = simpleDateFormat.format(new Date());
				}

				fechaCertificacion = fechaCertificacion.replace("-", "");
				
				System.out.println("nombreArchivo: " +  nombreArchivo);
				newFilename = nombreArchivo.substring(0, nombreArchivo.length() - 18);
				nombreFinal = newFilename + fechaCertificacion +  "090000" + ".env";
				File oldfile = new File(rutaContingencia + "\\" + tipoContingencia + "\\env\\" + nombreArchivo);
				File newfile = new File(
						rutaContingencia + "\\" + tipoContingencia + "\\env\\" + nombreFinal);

				if (oldfile.renameTo(newfile)) {
					System.out.println("Archivo renombrado .env");
				} else {
					System.out.println("No se renombro el archivo ENV " + nombreFinal);
				}
				nombreArchivo = nombreFinal;
			}
			

			generarEnv.setNombreArchivo(nombreArchivo);
			generarEnv.setContingencia(tipoContingencia);
			nombreArchivoEnv = generarEnv.getNombreArchivo();
			// DataSourceSybase.cerrarConexion();

			// actualizarArchivo(archivo.get(0));

		} catch (Exception e) {
			// TODO: handle exception
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Error al crear archivo .env ", e);
			e.printStackTrace();
		}finally {
			//DataSourceSybase.cerrarConexion();
		}

		/*
		String nombreFinal = "";
		idTipoEnvio=1;
		if(idTipoEnvio==1) {
			nombreFinal = newNameFile;
			nombreArchivo = nombreFinal;
		}else {
			nombreFinal = nombreArchivoEnv();
		}*/
		
		return generarEnv;
	}
	
	@Override
	public Map<Integer, String> generarFileContingencia() {
		// TODO Auto-generated method stub
		Map<Integer, String> mapArchivoEnv = new HashMap<>();
		ResultSet rs2;
		String archivos = "SELECT Sea_Estruc FROM SPESTACP ORDER BY Sea_Consec ASC";

		rs2 = propiedadesSbe.ejecutaQueryGenericaRst(archivos);

		int contador = 0;
		try {
			while (rs2.next()) {
				mapArchivoEnv.put(contador++, rs2.getString("Sea_Estruc"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error al consultar informacion de archivo", e);
			e.printStackTrace();
		}

		return mapArchivoEnv;
	}

	@Override
	public String nombreArchivoEnv() {
		// TODO Auto-generated method stub
		return nombreArchivoEnv;
	}
	
	

}
