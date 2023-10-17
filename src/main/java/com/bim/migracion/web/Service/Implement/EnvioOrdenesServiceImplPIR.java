package com.bim.migracion.web.Service.Implement;


import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.EnvioOrdenesRequest;
import com.bim.migracion.web.Request.FirmaRequest;
import com.bim.migracion.web.Request.FirmadorRequest;
import com.bim.migracion.web.Service.DatasourceService;

import com.bim.migracion.web.Service.EnvioOrdenesServicePIR;
import com.bim.migracion.web.Service.FirmadorService;

@Service
public class EnvioOrdenesServiceImplPIR implements EnvioOrdenesServicePIR {

	//private DataSourceSybase dataSybase;
	@Autowired
	PropiedadesSybase propiedades;
	
	@Autowired
	private DatasourceService dataSourceService;
	
	@Autowired
	private FirmadorService firmadorService;
	
	@Autowired
	RestTemplate restTemplate;
	
	String token="";
	
	String numTransaccion="";
	
	private int ordenesEnviadas = 0;
	
	private static final Logger logger = LogManager.getLogger();
	
	DataSourceRequest datasource = new DataSourceRequest();
	
	@Override
	public void generaOrdenes(EnvioOrdenesRequest request) {
		// TODO Auto-generated method stub
		//
		datasource = dataSourceService.findByIdSbe(2);
		DataSourceSybase.setConexion(datasource.getIp(),datasource.getPuerto(),datasource.getBase(),datasource.getUsuario(),datasource.getPass());		
		
		for (DetalleOrdenRequest orden : request.getOrdenes()) {
			insertaOrdenes(orden, request.getFirmador(), request.getDatasource());
		}
		DataSourceSybase.cerrarConexion();
		
	}

	@Override
	public void insertaOrdenes(DetalleOrdenRequest orden, FirmadorRequest firmador, DataSourceRequest datasource) {
		// TODO Auto-generated method stub
		
		//datasource = dataSourceService.findByIdSbe(datasource.getId());
		
		//DataSourceSybase.setConexion(datasource.getIp(),datasource.getPuerto(),datasource.getBase(),datasource.getUsuario(),datasource.getPass());		

		
		//if(firmador.isFirmar()) {
			firmador = firmadorService.firmadorId(2);
			
			token = firmadorService.tokenFirmador(firmador); //contingencia no necesita token
			
			System.out.println("Token: " + token);
		//}
		

		//String query = generaQueryPago(orden);
		
		String query = generaQueryPago(orden);
		
		for (int i = 0; i < orden.getCantidad(); i++) {
			System.out.println(query);
			//insertaOrden(query, orden, firmador);
			//insertaOrden(query, orden, null);
			
			if ((ordenesEnviadas == 90) || (i + 1 == orden.getCantidad())) {
				try {
					TimeUnit.SECONDS.sleep(orden.getSegundos());
					System.out.println("Segundos de espera: " + orden.getSegundos());
					ordenesEnviadas = 0;
					propiedades.ejecutaQuery("dump transaction "+ datasource.getBase() +" with"
							+ " truncate_only");
					//propiedades.ejecutaQuery("dump transaction dbBIM_SPEI with truncate_only");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		
		//DataSourceSybase.cerrarConexion();
	}

	@Override
	public void insertaOrden(String query, DetalleOrdenRequest orden, FirmadorRequest firmador) {
		// TODO Auto-generated method stub
		String OrpNumero = propiedades.execEnvio(query);
		
		if (firmador.isFirmar()) { 
			firmarOrden(orden, firmador, OrpNumero);
		}
		
		autorizarOrden(OrpNumero, orden.getTipoPago());
		
		ordenesEnviadas++;
	}

	@Override
	public void firmarOrden(DetalleOrdenRequest orden, FirmadorRequest firmador, String numero) {
		// TODO Auto-generated method stub
		String uri = "http://" + firmador.getIp() +":" + firmador.getPuerto() + "/api/firmar";
		//GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO,"Cerrando Conexion BD:" + url  ,null);

		logger.info(uri);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		// Creacion de la firma
		FirmaRequest body = new FirmaRequest();
		body.setNumero(numero);
		body.setFirma(String.format("%.2f", orden.getMonto())
				+ orden.getBeneficiario().getBanco().substring(orden.getBeneficiario().getBanco().length() - 3)
				+ firmador.getUsuario()
				+ orden.getOrdenante().getBanco().substring(orden.getOrdenante().getBanco().length() - 3));

		HttpEntity<FirmaRequest> request = new HttpEntity<FirmaRequest>(body, headers);

		try {
			restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	@Override
	public void autorizarOrden(String numero, String tipPag) {
		// TODO Auto-generated method stub
		String update = "";

		//if (tipPag.equals("01") || tipPag.equals("02") || tipPag.equals("03") || tipPag.equals("04")
				//|| tipPag.equals("19") || tipPag.equals("20") || tipPag.equals("21") || tipPag.equals("22")) {
		//	if (!numero.isEmpty()) {
				update = "EXEC SPAUORCLPRO '" + numero + "','0025272561','SOA','000662','','001','001','SP'";
			//}
		//} else {
		//	update = "UPDATE SPORDPAG SET  Orp_Status = 'O', Orp_StaEnv = 'A', Orp_UsuEnv = '02',NumTransac = '00000'  WHERE NumTransac IN('"
		//			+ numTransaccion + "')";
		//}

		propiedades.ejecutaQuery(update);
	}

	@Override
	public String generaQueryPago(DetalleOrdenRequest orden) {
		// TODO Auto-generated method stub
		String query = "call SPORPAMEALTPIR '','E','001','00194808','001948080016','','','150180019480800160','VICTOR PINEDA VELAZQUEZ','PIVV711223LN5','40','150180019480800160','USUARIO GENERICO','XXXX0000009K1','','','','','110117',1.00,0.0,0.0,'TEST PIR TP30','TEST PIR TP30','TEST PIR TP30','','30','2023-02-02 00:00:00.0','N','CC','','',0.0,'000662',0,0,'','','','450','40150','V','0','','','P',0,0.0,'PIR VICTOR','40','150990119400000014','PIRVPVRFC','',0,'2501114717','SWM','','','001','001','01'";
		
		return query;
	}

}
