package com.bim.migracion.web.Service.Implement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.EnvioOrdenesRequest;
import com.bim.migracion.web.Request.FirmaRequest;
import com.bim.migracion.web.Request.FirmadorRequest;
import com.bim.migracion.web.Service.DatasourceService;
import com.bim.migracion.web.Service.EnvioOrdenesService;
import com.bim.migracion.web.Service.FirmadorService;

@Service
public class EnvioOrdenesServiceImpl implements EnvioOrdenesService {

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
		datasource = dataSourceService.findByIdSbe(request.getDatasource().getId());
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

		
		if(firmador.isFirmar()) {
			firmador = firmadorService.firmadorId(firmador.getId());
			
			token = firmadorService.tokenFirmador(firmador); //contingencia no necesita token
			
			System.out.println("Token: " + token);
		}

		//String query = generaQueryPago(orden);
		
		String query = generaQueryPago(orden);
		
		for (int i = 0; i < orden.getCantidad(); i++) {
			//System.out.println("Query a mostrar: " + orden.getTipoPago());
			//System.out.println(query);
			insertaOrden(query, orden, firmador);
			//insertaOrden(query, orden, null);
			
			if ((ordenesEnviadas == 90) || (i + 1 == orden.getCantidad())) {
				try {
					TimeUnit.SECONDS.sleep(orden.getSegundos());
				//	TimeUnit.SECONDS.sleep(2);
					System.out.println("Segundos de espera: " + orden.getSegundos());
					GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Segundos de espera: " + orden.getSegundos(), null);
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
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Orden a firmar: " + numero, null);
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
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Orden #" + numero + " firmada con exito", null);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.ERROR, "Error al firmar orden #" + numero, e);
		}
	}

	@Override
	public void autorizarOrden(String numero, String tipPag) {
		// TODO Auto-generated method stub
		String update = "";

		if (tipPag.equals("01") || tipPag.equals("02") || tipPag.equals("03") || tipPag.equals("04")
				|| tipPag.equals("19") || tipPag.equals("20") || tipPag.equals("21") 
				|| tipPag.equals("22") || tipPag.equals("30") || tipPag.equals("31")
				|| tipPag.equals("32") || tipPag.equals("33") || tipPag.equals("34")
				|| tipPag.equals("35") || tipPag.equals("36")) {
			if (!numero.isEmpty()) {
				update = "EXEC SPAUORCLPRO '" + numero + "','0025272561','SOA','000662','','001','001','SP'";
			}
		} else {
			update = "UPDATE SPORDPAG SET  Orp_Status = 'O', Orp_StaEnv = 'A', Orp_UsuEnv = '02',NumTransac = '00000'  WHERE NumTransac IN('"
					+ numTransaccion + "')";
		}

		propiedades.ejecutaQuery(update);
	}

	@Override
	public String generaQueryPago(DetalleOrdenRequest orden) {
		// TODO Auto-generated method stub
		String query = "call ";
		String cliente = "";
		String cuenta = "";
		numTransaccion = propiedades.getUcs_NumTra();

		// Creacion clave de pago
		SimpleDateFormat fts = new SimpleDateFormat("hhMMss");// PARA QUE ES
		String clavePago = fts.format(new Date());
		
		System.out.println("Enviando pagos: " + orden.getTipoPago() + "ordenante: " + orden.getOrdenante().getTipoCuenta());

		if (orden.getOrdenante().getTipoCuenta().equals("40") 
				|| orden.getTipoPago().equals("30")
				|| orden.getTipoPago().equals("31")
				|| orden.getTipoPago().equals("32")
				|| orden.getTipoPago().equals("33")
				|| orden.getTipoPago().equals("34")
				|| orden.getTipoPago().equals("35")
				|| orden.getTipoPago().equals("36")) {
			cuenta = orden.getOrdenante().getCuenta().substring(5, 17);
			cliente = orden.getOrdenante().getCuenta().substring(5, 13);
		} else if (orden.getOrdenante().getTipoCuenta().equals("03")) {
			// Orp_Cuenta = util.getCuentaTarjeta(ordenes.getCuentaOrdenante());
			// Orp_Client = util.getClienteTarjeta(ordenes.getCuentaOrdenante());
		}

		if (orden.getTipoPago().equals("01") || orden.getTipoPago().equals("02") || orden.getTipoPago().equals("03")
				|| orden.getTipoPago().equals("04") || orden.getTipoPago().equals("05")
				|| orden.getTipoPago().equals("06") || orden.getTipoPago().equals("07")) {
			query += "SPORPAMEALT '','E','001',";
		} else if (orden.getTipoPago().equals("19") || orden.getTipoPago().equals("20")
				|| orden.getTipoPago().equals("21") || orden.getTipoPago().equals("22")) {
			query += "SPOPCODIALT '','E','001',";
		}
		else if (orden.getTipoPago().equals("30") || orden.getTipoPago().equals("31") 
				|| orden.getTipoPago().equals("32") || orden.getTipoPago().equals("33")
				|| orden.getTipoPago().equals("34") || orden.getTipoPago().equals("35") || orden.getTipoPago().equals("36")) {
			query += "SPORPAMEALTPIR '','E','001',";
		}

		// Cliente y Cuenta
		if (orden.getTipoPago().equals("01") || orden.getTipoPago().equals("02") || orden.getTipoPago().equals("03")
				|| orden.getTipoPago().equals("04") || orden.getTipoPago().equals("19")
				|| orden.getTipoPago().equals("20") || orden.getTipoPago().equals("21")
				|| orden.getTipoPago().equals("22")
				|| orden.getTipoPago().equals("30")
				|| orden.getTipoPago().equals("31")
				|| orden.getTipoPago().equals("32")
				|| orden.getTipoPago().equals("33")
				|| orden.getTipoPago().equals("34")
				|| orden.getTipoPago().equals("35")
				|| orden.getTipoPago().equals("36")) {
			query += "'" + cliente + "',";
			query += "'" + cuenta + "',";
		} else if (orden.getTipoPago().equals("05") || orden.getTipoPago().equals("06")
				|| orden.getTipoPago().equals("07")) {
			query += "'',";
			query += "'',";
		}

		// Tipo de Operacion
		if (orden.getTipoPago().equals("07") || orden.getTipoPago().equals("04") || orden.getTipoPago().equals("31")) {
			query += "'04',";
		} else {
			query += "'',";
		}

		// Datos Ordenante--SE AGREGA TIPO 6 PARA COAS
		if (orden.getTipoPago().equals("01") || orden.getTipoPago().equals("02") || orden.getTipoPago().equals("03")
				|| orden.getTipoPago().equals("04") || orden.getTipoPago().equals("06")
				|| orden.getTipoPago().equals("19")
				|| orden.getTipoPago().equals("20") || orden.getTipoPago().equals("21")
				|| orden.getTipoPago().equals("22")
				|| orden.getTipoPago().equals("30")
				|| orden.getTipoPago().equals("31")
				|| orden.getTipoPago().equals("32")
				|| orden.getTipoPago().equals("33")
				|| orden.getTipoPago().equals("34")
				|| orden.getTipoPago().equals("35")
				|| orden.getTipoPago().equals("36")) {
			if(orden.getTipoPago().equals("30") || orden.getTipoPago().equals("31")
					|| orden.getTipoPago().equals("32") || orden.getTipoPago().equals("33")
					|| orden.getTipoPago().equals("34") || orden.getTipoPago().equals("35")
					|| orden.getTipoPago().equals("36")) {
				query += "'',";
			}
			else {
				query += "'" + orden.getOrdenante().getTipoCuenta() + "',";
			}
			//query += "'" + orden.getOrdenante().getTipoCuenta() + "',";
			query += "'" + orden.getOrdenante().getCuenta() + "',";
			query += "'" + orden.getOrdenante().getNombre() + "',";
			query += "'" + orden.getOrdenante().getRfc() + "',";
		} else if (orden.getTipoPago().equals("05")
				|| orden.getTipoPago().equals("07")) {
			query += "'',";
			query += "'',";
			query += "'',";
			query += "'',";
		}

		// Datos Beneficiario Adicional
		if (orden.getTipoPago().equals("07")) {
			query += "'',";
			query += "'',";
			query += "'',";
			query += "'',";
		} else {
			query += "'" + orden.getBeneficiario().getTipoCuenta() + "',";
			query += "'" + orden.getBeneficiario().getCuenta() + "',";
			query += "'" + orden.getBeneficiario().getNombre() + "',";
			query += "'" + orden.getBeneficiario().getRfc() + "',";
		}

		// Datos Beneficiario Adicional
		if(orden.getTipoPago().equals("06")) {
			query += "'40',";
			query += "'" + orden.getOrdenante().getCuenta() + "',";
		}else {
			query += "'',";
			query += "'',";
			
		}
		query += "'',";
		query += "'',";
		

		// Clave de pago
		query += "'" + clavePago + "',";

		// Montos
		query += String.format("%.2f", orden.getMonto()) + ","; // Monto
		query += 0.0 + ","; // iva
		query += 0.0 + ","; // Comision

		// Conceptos de pago
		query += "'" + orden.getConcepto() + "',"; // Concepto de pago
		query += "'" + orden.getConcepto() + "',"; // Concepto de pago 1
		query += "'" + orden.getConcepto() + "',"; // Concepto de pago 2

		query += "'',"; // Referencia de cobro

		query += "'" + orden.getTipoPago() + "',"; // Tipo de pago

		query += "'" + propiedades.getOrpFecha() + "',"; // Fecha
		// query += "'fecha12345',"; // Fecha

		// Datos del cheque
		query += "'" + "N" + "',"; // El cheque incluye iva
		query += "'" + "CC" + "',"; // Forma de pago envios (CC Cargo a Cuenta,CP Cheque Propio,CJ Cheque de Caja)
									// recepcion (AB Abono a Cuenta )
		query += "'',"; // Numero de cheque
		query += "'',"; // RFC Provedor
		query += 0.0 + ","; // iva provedor

		query += "'" + "000662" + "',"; // Autorizador

		query += 0 + ","; // paquete
		query += 0 + ","; // conpag

		query += "'',"; // CDE
		query += "'',"; // Causa Devolucion
		query += "'',"; // Clave de Rastreo

		query += "'" + orden.getOrdenante().getBanco() + "',"; // Institucion Emisora
		query += "'" + orden.getBeneficiario().getBanco() + "',"; // Institucion Receptora

		query += "'" + "V" + "',"; // Topologia
		query += "'" + "0" + "',"; // Prioridad

		query += "'',"; // Referencia Numerica
		query += "'',"; // Movimiento Compra Venta
		query += "'" + "P" + "',"; // P- Usuario de Plataforma, T- Usuario de Tesoreria

		query += 0 + ","; // LoCrLa
		
		

		if (orden.getTipoPago().equals("01") || orden.getTipoPago().equals("02") || orden.getTipoPago().equals("03")
				|| orden.getTipoPago().equals("04") || orden.getTipoPago().equals("05")
				|| orden.getTipoPago().equals("06") || orden.getTipoPago().equals("07") || orden.getTipoPago().equals("30")
				|| orden.getTipoPago().equals("31")
				|| orden.getTipoPago().equals("32")
				|| orden.getTipoPago().equals("33")
				|| orden.getTipoPago().equals("34")
				|| orden.getTipoPago().equals("35")
				|| orden.getTipoPago().equals("36")) {
			query += 0 + ","; // Tot int
			//query += 9510 + ","; // Tot int
		}

		if (orden.getTipoPago().equals("01") || orden.getTipoPago().equals("02") || orden.getTipoPago().equals("03")
				|| orden.getTipoPago().equals("04") || orden.getTipoPago().equals("05")
				|| orden.getTipoPago().equals("06") || orden.getTipoPago().equals("07")
				|| orden.getTipoPago().equals("30") || orden.getTipoPago().equals("31")
				|| orden.getTipoPago().equals("35") || orden.getTipoPago().equals("36")) {
			
			if(orden.getTipoPago().equals("01") || orden.getTipoPago().equals("02") || orden.getTipoPago().equals("03")
					|| orden.getTipoPago().equals("04") || orden.getTipoPago().equals("05")
					|| orden.getTipoPago().equals("06") || orden.getTipoPago().equals("07")) {
				query += "'',"; // Celular Ordenante
				query += 0 + ","; // Digito Verificador Ordenante
			}
				query += "'',"; // Celular del beneficiario
				if(orden.getTipoPago().equals("30") || orden.getTipoPago().equals("31")
						|| orden.getTipoPago().equals("35") || orden.getTipoPago().equals("36")) {
					query += "'',"; // Certificado del beneficiario
				}
				
				query += 0 + ","; // Numero verificador beneficiario
				query += "'',"; // Folio del esquema codi
				if(orden.getTipoPago().equals("30") || orden.getTipoPago().equals("31")
						|| orden.getTipoPago().equals("35") || orden.getTipoPago().equals("36")) {
					query += "'',"; // fecha y hora limite de pago--TP33
				}
				query += 0 + ","; // Tipo de pago de la comision por la transferencia 1/El cliente Emisor paga.
									// 2/El cliente Beneficiario Paga
				query += 0.0 + ","; // Monto de la comision por la transferencia
			
			
			
		} else if (orden.getTipoPago().equals("19") || orden.getTipoPago().equals("20")
				|| orden.getTipoPago().equals("21") || orden.getTipoPago().equals("22")
				|| orden.getTipoPago().equals("32")
				|| orden.getTipoPago().equals("33")
				|| orden.getTipoPago().equals("34")) {
			
			if(orden.getTipoPago().equals("19") || orden.getTipoPago().equals("20")
					|| orden.getTipoPago().equals("21") || orden.getTipoPago().equals("22")){
					query += "'" + "5580292897" + "',"; // Celular Ordenante
					query += 6 + ","; // Digito Verificador Ordenante
				}
			if(orden.getTipoPago().equals("32")) {
				query += "'" + "9616190001" + "',"; // Celular del beneficiario
				query += "'',"; // Certificado del beneficiario
			}else if(orden.getTipoPago().equals("33") || orden.getTipoPago().equals("34")) {
				query += "'',"; // Celular del beneficiario
				query += "'" + "00000100000100012159" + "',"; // Certificado del beneficiario
			}else {
				query += "'" + "9616190001" + "',"; // Celular del beneficiario
				//query += "'',"; // Certificado del beneficiario
			}
			query += 3 + ","; // Numero verificador beneficiario
			query += "'" + "26e9ba727e26e9ba7e69" + "',"; // Folio del esquema codi
			if(orden.getTipoPago().equals("32") || orden.getTipoPago().equals("33")) {
				query += "'',"; // fecha y hora limite de pago--TP33
			}else if(orden.getTipoPago().equals("34")) {
				Calendar now = Calendar.getInstance();
				int month = now.get(Calendar.MONTH) + 1;
				int day = now.get(Calendar.DAY_OF_MONTH);
				int hour = now.get(Calendar.HOUR_OF_DAY);
				int minute = now.get(Calendar.MINUTE);
				int year = now.get(Calendar.YEAR);

				String Ucs_NumTra = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + "T"
						+ String.format("%02d", hour) + ":" + String.format("%02d", minute);

				query += "'" + Ucs_NumTra + "',"; // Fecha y hora del limite de pago
			}
			
			query += 1 + ","; // Tipo de pago de la comision por la transferencia 1/El cliente Emisor paga.
								// 2/El cliente Beneficiario Paga
			query += 0 + ","; // Monto de la comision por la transferencia
		}

		if (orden.getTipoPago().equals("20")) {
			query += "'00000100000100012159',"; // Numero de serie
			query += "'',"; // Nombre Beneficiario 2
			query += "'',"; // Rfc o Curp del beneficiario 2
			query += 0 + ","; // Tipo cuenta beneficiario 2
			query += "'',"; // Cuenta beneficiario 2

			query += "'',"; // Fecha y hora del limite de pago
		} else if (orden.getTipoPago().equals("21")) {
			query += "'00000100000100012159',"; // Numero de serie
			query += "'',"; // Nombre Beneficiario 2
			query += "'',"; // Rfc o Curp del beneficiario 2
			query += 0 + ","; // Tipo cuenta beneficiario 2
			query += "'',"; // Cuenta beneficiario 2

			Calendar now = Calendar.getInstance();
			int month = now.get(Calendar.MONTH) + 1;
			int day = now.get(Calendar.DAY_OF_MONTH);
			int hour = now.get(Calendar.HOUR_OF_DAY);
			int minute = now.get(Calendar.MINUTE);
			int year = now.get(Calendar.YEAR);

			String Ucs_NumTra = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + "T"
					+ String.format("%02d", hour) + ":" + String.format("%02d", minute);

			query += "'" + Ucs_NumTra + "',"; // Fecha y hora del limite de pago
		} else if (orden.getTipoPago().equals("22")) {
			query += "'00000100000100012159',"; // Numero de serie
			query += "'NOM BEN 2',"; // Nombre Beneficiario 2
			query += "'PIVV711223LN5',"; // Rfc o Curp del beneficiario 2
			query += 40 + ","; // Tipo cuenta beneficiario 2
			query += "'150180019517100128',"; // Cuenta beneficiario 2

			Calendar now = Calendar.getInstance();
			int month = now.get(Calendar.MONTH) + 1;
			int day = now.get(Calendar.DAY_OF_MONTH);
			int hour = now.get(Calendar.HOUR_OF_DAY);
			int minute = now.get(Calendar.MINUTE);
			int year = now.get(Calendar.YEAR);

			String Ucs_NumTra = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + "T"
					+ String.format("%02d", hour) + ":" + String.format("%02d", minute);

			query += "'" + Ucs_NumTra + "',"; // Fecha y hora del limite de pago
		} else {

			if(!orden.getTipoPago().equals("30") && !orden.getTipoPago().equals("31") && !orden.getTipoPago().equals("32")
					&& !orden.getTipoPago().equals("33") && !orden.getTipoPago().equals("34") && !orden.getTipoPago().equals("35")
					&& !orden.getTipoPago().equals("36")) {
				query += "'',"; // Numero de serie
				query += "'',"; // Nombre Beneficiario 2
				query += "'',"; // Rfc o Curp del beneficiario 2
				query += 0 + ","; // Tipo cuenta beneficiario 2
				query += "'',"; // Cuenta beneficiario 2
				query += "'',"; // Fecha y hora del limite de pago
			}
			
		}
		
		if (orden.getTipoPago().equals("30") 
			|| orden.getTipoPago().equals("31")
			|| orden.getTipoPago().equals("32")
			|| orden.getTipoPago().equals("33")
			|| orden.getTipoPago().equals("34")
			|| orden.getTipoPago().equals("35")
			|| orden.getTipoPago().equals("36")) {
			query += "'" + orden.getOrdenantePir().getNombrePir() + "'" + ","; // Nombre Pir
			query += "'" + orden.getOrdenantePir().getTipoCuentaPir() + "'" + ","; // Tipo cuenta Pir
			query += "'" + orden.getOrdenantePir().getCuentaPir() + "'" + ","; // Cuenta Pir
			query += "'" + orden.getOrdenantePir().getRfcPir() + "'" + ","; // RFC Pir
			if(orden.getTipoPago().equals("32") || orden.getTipoPago().equals("33") || orden.getTipoPago().equals("34")) {
				query += "'" + "5580292897" + "',"; // Celular Ordenante
				query += 6 + ","; // Digito Verificador Ordenante
			}else {
				query += "''" + ","; // x Pir
				query += 0 + ","; // x Pir
			}
			
			/*Tipo de pago remesas*/
			if(orden.getTipoPago().equals("35") || orden.getTipoPago().equals("36")) {
				query += "'JUAN ANTONIO TAPIA',";//Nombre emisor remesa
				if(orden.getTipoPago().equals("35")) {
					query += "'40',";//Tipo cuenta emisor remesa
				}else {
					query += "'',";//Tipo cuenta emisor remesa
				}
				
				query += "'3240982093840920',";//Cuenta emisor remesa
				query += "'TACJ841011SS3',";//RFC emisor remesa
				
				query += "'444',";//identificador remesa
				query += "'US',";//Pais remesa
				query += "'840',";//Divisa remesa
				query += "'JUAN ANTONIO TAPIA',";//Nombre beneficiario remesa
				query += "'WESTERN UNION',";//Proveedor remesa extranjero
				query += "'WELLS FARGO',";//Proveedor remesa nacional
				if(orden.getTipoPago().equals("35")) {
					query += "'FARMACIAS GUADALAJARA',";//Nombre cliente indirecto receptor
				}else {
					query += "'',";//Nombre cliente indirecto receptor
				}
				
				query += "22,";//Tipo de cambio
				if(orden.getTipoPago().equals("35")) {
					query += "'',";//Nombre cliente indirecto receptor
				}else {
					query += "'FARMACIAS GUADALAJARA',";//Nombre cliente indirecto receptor
				}
				
			}else {
				query += "'',";//Nombre emisor remesa
				query += "'',";//Tipo cuenta emisor remesa
				query += "'',";//Cuenta emisor remesa
				query += "'',";//RFC emisor remesa
				
				query += "'',";//identificador remesa
				query += "'',";//Pais remesa
				query += "'',";//Divisa remesa
				query += "'',";//Nombre beneficiario remesa
				query += "'',";//Proveedor remesa extranjero
				query += "'',";//Proveedor remesa nacional
				query += "'',";//Nombre cliente indirecto receptor
				
				query += 0 + ",";//Tipo de cambio
				query += "'',";//Nombre cliente indirecto distribuidor
			}
			
			
			
		}
		

		query += "'" + numTransaccion + "',"; // Numero de transaccion
		query += "'" + "SWM" + "',"; // Transaccion
		query += "'',"; // Usuario
		query += "'',"; // Fechasis
		query += "'" + "001" + "',"; // Sucursal Origen
		query += "'" + "001" + "',"; // Sucursal Destino
		query += "'" + orden.getModulo() + "'"; // Modulo

		return query;
	}

}
