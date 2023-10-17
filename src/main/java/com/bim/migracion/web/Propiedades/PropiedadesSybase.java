package com.bim.migracion.web.Propiedades;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import org.springframework.stereotype.Component;

import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Conexion.DataSourceSybase2;
import com.bim.migracion.web.Conexion.DatabaseMysqlConfig;
import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;


@Component
public class PropiedadesSybase {

	public String getOrpFecha() {

		String Orp_Fecha = "";
		Connection con = DataSourceSybase.getConnection();
		Statement stmt = null;
		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Fecha Orp", null);
			stmt = con.createStatement();
			stmt.executeQuery("SELECT Par_FeApMo FROM SPPARAMS");

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				Orp_Fecha = rs.getString("Par_FeApMo");
			}

			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return Orp_Fecha;
	}
	
	public String getUcs_NumTra() {
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		String Ucs_NumTra = String.format("%02d%02d%02d%02d%02d", day,month, hour, minute, second);
		
		return Ucs_NumTra;
	}
	
	
	public String execEnvio(String sql){
		Connection con = DataSourceSybase.getConnection();
		String Orp_Numero = null;

		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, null);
			CallableStatement cs = con.prepareCall("{" + sql + "}");
			cs.executeQuery();
			ResultSet rs = cs.getResultSet();
			
			while (rs.next()) {
				Orp_Numero = rs.getString("Orp_Numero");
			}
			cs.close();
			
			} catch (Exception e) {
				//System.out.println(e.getMessage());
				GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, e);
				GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, e.getMessage(), null);
			}
		return Orp_Numero;
	}
	
	public void ejecutaQuery(String sql) {
		Connection con = DataSourceSybase.getConnection();
		Statement stmt = null;
		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, null);
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, e);
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, e.getMessage(), null);
		}	
	}
	
	public ResultSet ejecutaQueryGenericaRst(String sql) {
		Connection con = DataSourceSybase.getConnection();
		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, null);
			Statement stmt = con.createStatement();
			stmt.execute(sql);
			return stmt.getResultSet();
		} catch (SQLException e) {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, e);
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, e.getMessage(), null);
		}
		return null;
	}
	
	public ResultSet ejecutaQueryGenericaRst2(String sql) {
		Connection con = DataSourceSybase.getConnection();
		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, null);
			Statement stmt = con.createStatement();
			stmt.execute(sql);
			return stmt.getResultSet();
		} catch (SQLException e) {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, e);
		}
		return null;
	}
	
	public ResultSet ejecutaQueryGenericaRstRecepccion(String sql) {
		Connection con = DataSourceSybase2.getConnection();
		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, null);
			Statement stmt = con.createStatement();
			stmt.execute(sql);
			return stmt.getResultSet();
		} catch (SQLException e) {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, e);
		}
		return null;
	}
	
	public ResultSet getResultsetGenerico(String sql) {
		Connection con = DataSourceSybase.getConnection();
		ResultSet result = null;
		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, null);
			Statement stmt = con.createStatement();
			stmt.executeQuery(sql);
			result = stmt.getResultSet();
		} catch (SQLException e) {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, e.getMessage(), null);
			e.printStackTrace();
		}
		return result;
	}
	
	public ResultSet getResultsetGenericoMysql(String sql) {
		Connection con = DatabaseMysqlConfig.getConnection();
		ResultSet result = null;
		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, null);
			Statement stmt = con.createStatement();
			stmt.executeQuery(sql);
			result = stmt.getResultSet();
		} catch (SQLException e) {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, e.getMessage(), null);
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public void ejecutaStore(String sql){
		Connection con = DataSourceSybase.getConnection();
		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, null);
			CallableStatement cs = con.prepareCall("{" + sql + "}");
			cs.execute();			
			cs.close();
		} catch (Exception e) {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, e.getMessage(), null);
			e.printStackTrace();
		}

	}
	
	public ResultSet getResultSetStoreGenerico(String sql) {
		Connection con = DataSourceSybase.getConnection();
		ResultSet result = null;
		try {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, sql, null);
			CallableStatement cs = con.prepareCall("{" + sql + "}");
			result  = cs.executeQuery();			
		} catch (Exception e) {
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, e.getMessage(), null);
			e.printStackTrace();
		}
		return result;
	} 
}


