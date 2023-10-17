package com.bim.migracion.web.Service.Implement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DevolucionesRequest;
import com.bim.migracion.web.Request.GetDevolucionesRequest;
import com.bim.migracion.web.Request.SendDevolucionRequest;
import com.bim.migracion.web.Response.getDevolucionesResponse;
import com.bim.migracion.web.Service.DatasourceService;
import com.bim.migracion.web.Service.DevolucionesService;

@Service
public class DevolucionesServiceImpl implements DevolucionesService {

	@Autowired
	private DatasourceService dataService;

	@Autowired
	private PropiedadesSybase propiedades;

	@Override
	public List<getDevolucionesResponse> getDevoluciones(GetDevolucionesRequest request) {
		// TODO Auto-generated method stub

		DataSourceRequest dataSourceRequest = dataService.findByIdSbe(request.getDatasource().getId());

		request.setDatasource(dataSourceRequest);

		DataSourceSybase.setConexion(request.getDatasource().getIp(), request.getDatasource().getPuerto(),
				request.getDatasource().getBase(), request.getDatasource().getUsuario(),
				request.getDatasource().getPass());

		DataSourceSybase.getConnection();

		String busquedaSql = "";
		
		String fechaIni="";
		String fechaFin = "";
		
		if (request.getFechaInicio() != null && request.getFechaFin() != null) {
			fechaIni= request.getFechaInicio().replace("-", "");
			fechaFin = request.getFechaFin().replace("-", "");
		}
		

		// List<TipoPagoResponse> tiposPago =
		// tipoPago.getPagosxTipoDevolucion(request.getTipo());
		List<getDevolucionesResponse> listaDevoluciones = new ArrayList<>();
		int numOrdenes = request.getNumeroRegistros() == 0 ? 100 : request.getNumeroRegistros();

		if (request.getTipo().equals("Normal")) {

			busquedaSql = "SELECT TOP " + numOrdenes
					+ " Orp_Numero, Orp_TipPag,Orp_Cantid,Orp_ClaRas, Orp_CueBen, Orp_InsEmi, Orp_ConPag,Orp_Fecha,Orp_TiCuBe,Orp_InsRec,Orp_CueOrd,Orp_TiCuOr,Orp_InsSpe,Orp_Paquet \r\n"
					+ "FROM SPORDPAG SP  \r\n" + "INNER JOIN SPRETRIN RE ON\r\n" + "SP.Orp_Numero = RE. Orp_NuReTr\r\n"
					+ "WHERE Orp_Tipo = 'R'\r\n" + "AND Orp_Status = 'P'\r\n" + "AND Orp_StaEnv = 'A'\r\n"
					+ "AND Orp_TipPag = '" + request.getTipoPago() + "'\r\n"
					+ "AND CONVERT(Varchar,Orp_Fecha,112) =  (SELECT CONVERT(Varchar,Par_FeApMo,112) FROM SPPARAMS)\r\n"
					+ "AND Orp_Numero NOT IN (SELECT Orp_RefNum FROM SPORDPAG WHERE Orp_TipPag in ('00','16','17','23','18','24') AND Orp_Status = 'O' AND Orp_StaEnv = 'O') \r\n";

			if (request.getConcepto().length() > 0) {
				busquedaSql += "AND Orp_ConPag  LIKE '%" + request.getConcepto() + "%' \r\n";
			}

			if (request.getMontoDev() > 0) {
				busquedaSql += "AND Orp_Cantid=" + request.getMontoDev() + " \r\n";
			}

			busquedaSql += "AND Orp_InsSpe='" + request.getInstancia() + "' \r\n";

			busquedaSql += "ORDER by Orp_Numero ASC";

			try {

				ResultSet result = propiedades.getResultsetGenerico(busquedaSql);
				int contador = 0;

				while (result.next()) {
					getDevolucionesResponse devolucion = new getDevolucionesResponse();

					devolucion.setRow(++contador);
					devolucion.setNumero(result.getString("Orp_Numero").trim());
					devolucion.setTipoPago(result.getString("Orp_TipPag").trim());
					devolucion.setCantidad(result.getDouble("Orp_Cantid"));
					devolucion.setClaveRastreo(result.getString("Orp_ClaRas").trim());
					devolucion.setCuentaBeneficiaria(result.getString("Orp_CueBen").trim());
					devolucion.setInstitucionEmisora(result.getString("Orp_InsEmi").trim());
					devolucion.setConceptoPago(result.getString("Orp_ConPag").trim());
					devolucion.setFecha(result.getString("Orp_Fecha"));
					devolucion.setTipoCuentaBeneficiaria(result.getString("Orp_TiCuBe").trim());
					devolucion.setInstitucionReceptora(result.getString("Orp_InsRec").trim());
					devolucion.setTipoCuentaOrdenante(result.getString("Orp_TiCuOr").trim());
					devolucion.setCuentaOrdenante(result.getString("Orp_CueOrd").trim());
					devolucion.setInstancia(result.getInt("Orp_InsSpe"));
					devolucion.setPaquete(result.getInt("Orp_Paquet"));
					// devolucion.setPagos(tiposPago);

					listaDevoluciones.add(devolucion);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {

			busquedaSql = "SELECT TOP " + numOrdenes + " \r\n"
					+ " Orp_Numero,Orp_TipPag,Orp_Cantid,Orp_ClaRas,Orp_CueBen,Orp_InsEmi,Orp_ConPag,Orp_Fecha,Orp_TiCuBe,Orp_InsRec,Orp_CueOrd,Orp_TiCuOr,Orp_InsSpe,Orp_Paquet \r\n"
					+ " FROM SPORDPAG SP \r\n" + " INNER JOIN SPRETRIN RE ON \r\n"
					+ " SP.Orp_Numero = RE. Orp_NuReTr \r\n" + " WHERE  Orp_Tipo = 'R' \r\n"
					+ " AND Orp_Status = 'P' \r\n" + " AND Orp_StaEnv = 'A' \r\n" + "AND Orp_TipPag = '"
					+ request.getTipoPago() + "' \r\n";

			if (request.getFechaInicio() != null && request.getFechaFin() != null) {
				busquedaSql += "AND  Convert(varchar,Orp_Fecha,112) BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' \r\n";

				busquedaSql += "AND Convert(varchar,Orp_FecOpe,112) BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' \r\n";
			} else {
				busquedaSql += "AND  Convert(varchar,Orp_Fecha,112) BETWEEN dateadd(dd,-4,(SELECT CONVERT(Varchar,Par_FeApMo,112) FROM SPPARAMS)) AND dateadd(dd,-1,(SELECT CONVERT(Varchar,Par_FeApMo,112) FROM SPPARAMS)) \r\n";

				busquedaSql += "AND Convert(varchar,Orp_FecOpe,112) BETWEEN dateadd(dd,-4,(SELECT CONVERT(Varchar,Par_FeApMo,112) FROM SPPARAMS)) AND dateadd(dd,-1,(SELECT CONVERT(Varchar,Par_FeApMo,112) FROM SPPARAMS)) \r\n";
			}

			busquedaSql += "AND Orp_Numero NOT IN (SELECT Orp_RefNum FROM SPORDPAG WHERE Orp_TipPag IN ('00','16','17','23','18','24') AND Orp_Status = 'O' AND Orp_StaEnv = 'O') \r\n";

			if (request.getConcepto().length() > 0) {
				busquedaSql += "AND Orp_ConPag  LIKE '%" + request.getConcepto() + "%' \r\n";
			}

			if (request.getMontoDev() > 0) {
				busquedaSql += "AND Orp_Cantid=" + request.getMontoDev() + " \r\n";
			}
			// busquedaSql += "AND Orp_Cantid=1000 \r\n";

			busquedaSql += "AND Orp_InsSpe='" + request.getInstancia() + "' \r\n";

			/*
			 * if (request.getOrpNumeroDev().length() > 0) { busquedaSql +=
			 * "AND Orp_Numero  <= '" + request.getOrpNumeroDev() + "' \r\n"; }
			 */

			busquedaSql += "UNION \r\n" + "SELECT TOP " + numOrdenes + " \r\n"
					+ " Orp_Numero,Orp_TipPag,Orp_Cantid,Orp_ClaRas,Orp_CueBen,Orp_InsEmi,Orp_ConPag,Orp_Fecha,Orp_TiCuBe,Orp_InsRec,Orp_CueOrd,Orp_TiCuOr,Orp_InsSpe,Orp_Paquet \r\n"
					+ " FROM SPHISORD SP \r\n" + " INNER JOIN SPHISRET RE ON \r\n"
					+ " SP.Orp_Numero = RE. Orp_NuReTr \r\n" + " WHERE  Orp_Tipo = 'R' \r\n"
					+ " AND Orp_Status = 'P' \r\n" + " AND Orp_StaEnv = 'A' \r\n" + "AND Orp_TipPag = '"
					+ request.getTipoPago() + "' \r\n";

			if (request.getFechaInicio() != null && request.getFechaFin() != null) {
				busquedaSql += "AND  Convert(varchar,Orp_Fecha,112) BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' \r\n";

				busquedaSql += "AND Convert(varchar,Orp_FecOpe,112) BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' \r\n";
			} else {
				busquedaSql += "AND  Convert(varchar,Orp_Fecha,112) BETWEEN dateadd(dd,-4,(SELECT CONVERT(Varchar,Par_FeApMo,112) FROM SPPARAMS)) AND dateadd(dd,-1,(SELECT CONVERT(Varchar,Par_FeApMo,112) FROM SPPARAMS)) \r\n";
			}

			busquedaSql += "AND Orp_Numero NOT IN (SELECT Orp_RefNum FROM SPHISORD WHERE Orp_TipPag IN ('00','16','17','23','18','24') AND Orp_Status = 'O' AND Orp_StaEnv = 'O') \r\n";
			busquedaSql += "AND Orp_Numero NOT IN (SELECT Orp_RefNum FROM SPORDPAG WHERE Orp_TipPag IN ('00','16','17','23','18','24') AND Orp_Status = 'O' AND Orp_StaEnv = 'O') \r\n";

			if (request.getConcepto().length() > 0) {
				busquedaSql += "AND Orp_ConPag  LIKE '%" + request.getConcepto() + "%' \r\n";
			}

			if (request.getMontoDev() > 0) {
				busquedaSql += "AND Orp_Cantid=" + request.getMontoDev() + " \r\n";
			}

			busquedaSql += "AND Orp_InsSpe='" + request.getInstancia() + "' \r\n";

			/*
			 * if (request.getOrpNumeroDev().length() > 0) { busquedaSql +=
			 * "AND Orp_Numero  >= '" + request.getOrpNumeroDev() + "' \r\n"; }
			 */

			busquedaSql += "ORDER by Orp_Numero DESC";

			try {
				ResultSet result = propiedades.getResultsetGenerico(busquedaSql);
				int contador = 0;

				while (result.next()) {
					getDevolucionesResponse devolucion = new getDevolucionesResponse();

					devolucion.setSeleccion(false);
					devolucion.setRow(++contador);
					devolucion.setNumero(result.getString("Orp_Numero").trim());
					devolucion.setTipoPago(result.getString("Orp_TipPag").trim());
					devolucion.setCantidad(result.getDouble("Orp_Cantid"));
					devolucion.setClaveRastreo(result.getString("Orp_ClaRas").trim());
					devolucion.setCuentaBeneficiaria(result.getString("Orp_CueBen").trim());
					devolucion.setInstitucionEmisora(result.getString("Orp_InsEmi").trim());
					devolucion.setConceptoPago(result.getString("Orp_ConPag").trim());
					devolucion.setFecha(result.getString("Orp_Fecha"));
					devolucion.setTipoCuentaBeneficiaria(result.getString("Orp_TiCuBe").trim());
					devolucion.setInstitucionReceptora(result.getString("Orp_InsRec").trim());
					devolucion.setTipoCuentaOrdenante(result.getString("Orp_TiCuOr").trim());
					devolucion.setCuentaOrdenante(result.getString("Orp_CueOrd").trim());
					devolucion.setInstancia(result.getInt("Orp_InsSpe"));
					devolucion.setPaquete(result.getInt("Orp_Paquet"));
					// devolucion.setPagos(tiposPago);

					listaDevoluciones.add(devolucion);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		DataSourceSybase.cerrarConexion();

		return listaDevoluciones;
	}

	public void aplicarDevoluciones(SendDevolucionRequest devolucionesRequest) {

		DataSourceRequest dataSourceRequest = dataService.findByIdSbe(devolucionesRequest.getDatasource().getId());

		devolucionesRequest.setDatasource(dataSourceRequest);

		DataSourceSybase.setConexion(devolucionesRequest.getDatasource().getIp(),
				devolucionesRequest.getDatasource().getPuerto(), devolucionesRequest.getDatasource().getBase(),
				devolucionesRequest.getDatasource().getUsuario(), devolucionesRequest.getDatasource().getPass());

		String query = "";
		String orpFecha = propiedades.getOrpFecha();

		Double interesDevExt = 0.0;

		for (DevolucionesRequest dev : devolucionesRequest.getDevoluciones()) {

			if (dev.getPagoDev().equals("16")) {
				interesDevExt = calculoInteres(dev.getNumero());
				query = "UPDATE CHCUENTA SET  Cue_Status  = 'C' WHERE  Cue_Clabe  = '" + dev.getCuentaBeneficiaria()
						+ "'";
				propiedades.ejecutaQuery(query);
				System.out.println(query);

				query = "UPDATE SPHISORD SET Orp_Status='D',Orp_StaEnv='A' WHERE Orp_Numero = '" + dev.getNumero()
						+ "'";
				propiedades.ejecutaQuery(query);
				System.out.println(query);

				query = "UPDATE SPORDPAG SET Orp_Status='D',Orp_StaEnv='A' WHERE Orp_Numero = '" + dev.getNumero()
						+ "'";
				propiedades.ejecutaQuery(query);
				System.out.println(query);

				query = "CALL SPDEVEXTPRO '" + dev.getNumero() + "'," + interesDevExt + ",96.22";
				// query = "CALL SPDEVEXTPRO '" + dev.getNumero() + "',1,123";
				propiedades.ejecutaStore(query);
				System.out.println(query);

				query = "UPDATE CHCUENTA SET  Cue_Status  = 'A' WHERE  Cue_Clabe  = '" + dev.getCuentaBeneficiaria()
						+ "'";
				propiedades.ejecutaQuery(query);
				System.out.println(query);

			} else {
				// System.out.println(dev.getClaveRastreo());
				query = "CALL SPORDPAGALT '','E'," + "'','','','2','','','','','','','','','','','','','',"
						+ dev.getCantidad() + ",0,0,'" + dev.getConceptoPago() + "','','','','" + dev.getPagoDev()
						+ "'," + "'" + orpFecha + "','N','','','',0,'000662',0,0,'','','" + dev.getClaveRastreo() + "'"
						+ ",'','','V','0','','','B',0,'','','','" + orpFecha + "','001','001','BE'";

				System.out.println(query);
				propiedades.ejecutaStore(query);
			}

		}

		DataSourceSybase.cerrarConexion();
	}

	private Double calculoInteres(String numero) {
		// TODO Auto-generated method stub
		Double interes = 0.0;
		Double interesFinal = interesUma();
		System.out.println("Valor Uma" + interesFinal);
		String consulta = "SELECT Orp_Numero, Ori_TotInt  FROM SPORDPAG \r\n"
				+ " INNER JOIN SPORPAIN ON Ori_OrPaRe =  Orp_Numero \r\n" + "  WHERE Orp_Numero='1298237'\r\n"
				+ " UNION \r\n" + " SELECT Orp_Numero, Ori_TotInt  FROM SPHISORD \r\n"
				+ " INNER JOIN SPHIORIN ON Ori_OrPaRe =  Orp_Numero \r\n" + " WHERE Orp_Numero='" + numero + "'";
		ResultSet result = propiedades.getResultsetGenerico(consulta);

		try {
			while (result.next()) {
				interes = result.getDouble("Ori_TotInt");
			}
			interes = interes * 2;
			if (interes > interesFinal) {
				interesFinal = interes;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return interesFinal;
	}
	
	private Double interesUma() {
		// TODO Auto-generated method stub
		PropiedadesSybase sybase = new PropiedadesSybase();
		String fecha=sybase.getOrpFecha();
		Double uma=0.0;
		String consulta = "SELECT Uma  FROM SOUMAS \r\n"
				+ " WHERE Fecha_Aplica<='" + fecha + "' AND Fecha_Fin>'" + fecha +"'";
		
		System.out.println(consulta);
		ResultSet result = propiedades.getResultsetGenerico(consulta);

		try {
			while (result.next()) {
				uma = result.getDouble("Uma");
			}
			
			uma = uma*3.5;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uma;
	}

	@Override
	public List<getDevolucionesResponse> listaDevoluciones(GetDevolucionesRequest request) {
		// TODO Auto-generated method stub
		
		
		
		List<getDevolucionesResponse> listaDevoluciones = new ArrayList<>();
		ResultSet rsRetornos;
		
		//int numOrdenes = request.getNumeroRegistros() == 0 ? 100 : request.getNumeroRegistros();
		
		
		
		String fechaIni = null;
		String fechaFin = null;
		
		int numOrdenes = request.getNumeroRegistros() == 0 ? 100 : request.getNumeroRegistros();
		
		if (request.getFechaInicio() != null && request.getFechaFin() != null) {
			fechaIni= request.getFechaInicio().replace("-", "");
			System.out.println("Fecha inicio de pago: " + fechaIni);
			fechaFin = request.getFechaFin().replace("-", "");
			System.out.println("Fecha fin de pago: " + fechaFin);
		}else {
			fechaIni = "";
			fechaFin = "";
		}
		
		//String query = "CALL SP_LISTAR_TP '"+fechaIni+"', '"+fechaFin+"', '"+request.getConcepto()+"', "+request.getMontoDev()+", "+numOrdenes+",'"+ request.getTipo() +"'";
		String query = "CALL SP_LISTAR_TP '"+fechaIni+"', '"+fechaFin+"','"+ request.getTipo() +"'";
		
		try {
			DataSourceRequest dataSourceRequest = dataService.findByIdSbe(request.getDatasource().getId());

			//request.setDatasource(dataSourceRequest);

			/*DataSourceSybase.setConexion(request.getDatasource().getIp(), request.getDatasource().getPuerto(),
					request.getDatasource().getBase(), request.getDatasource().getUsuario(),
					request.getDatasource().getPass());*/
			
			DataSourceSybase.setConexion(dataSourceRequest.getIp(),dataSourceRequest.getPuerto(),dataSourceRequest.getBase(),dataSourceRequest.getUsuario(),dataSourceRequest.getPass());

			DataSourceSybase.getConnection();
			
			rsRetornos = propiedades.getResultSetStoreGenerico(query);

			int contador = 0;
			
			while (rsRetornos.next()) {

				getDevolucionesResponse devolucion = new getDevolucionesResponse();
				
				devolucion.setRow(++contador);
				devolucion.setNumero(rsRetornos.getString("Orp_Numero").trim());
				devolucion.setTipoPago(rsRetornos.getString("Orp_TipPag").trim());
				devolucion.setCantidad(rsRetornos.getDouble("Orp_Cantid"));
				devolucion.setClaveRastreo(rsRetornos.getString("Orp_ClaRas").trim());
				devolucion.setCuentaBeneficiaria(rsRetornos.getString("Orp_CueBen").trim());
				devolucion.setInstitucionEmisora(rsRetornos.getString("Orp_InsEmi").trim());
				devolucion.setConceptoPago(rsRetornos.getString("Orp_ConPag").trim());
				devolucion.setFecha(rsRetornos.getString("Orp_Fecha"));
				devolucion.setTipoCuentaBeneficiaria(rsRetornos.getString("Orp_TiCuBe").trim());
				devolucion.setInstitucionReceptora(rsRetornos.getString("Orp_InsRec").trim());
				devolucion.setTipoCuentaOrdenante(rsRetornos.getString("Orp_TiCuOr").trim());
				devolucion.setCuentaOrdenante(rsRetornos.getString("Orp_CueOrd").trim());
				devolucion.setInstancia(rsRetornos.getInt("Orp_InsSpe"));
				devolucion.setPaquete(rsRetornos.getInt("Orp_Paquet"));
				// devolucion.setPagos(tiposPago);

				listaDevoluciones.add(devolucion);

			}
			
			
			
			System.out.println("Concepto de pago: " + request.getConcepto());
			System.out.println("Monto de pago: " + request.getMontoDev());
			//System.out.println("Fecha inicio de pago: " + request.getMontoDev());
			
			
			
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		DataSourceSybase.cerrarConexion();
		
		return listaDevoluciones;
		
		
	}

}
