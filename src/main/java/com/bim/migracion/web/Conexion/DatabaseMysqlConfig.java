package com.bim.migracion.web.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;



public class DatabaseMysqlConfig {

	public static void main(String args[]) {
		getConnection();
		cerrarConexion();
	}
	
	private static Connection conn = null;
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String url ="jdbc:mysql://172.30.215.17:3306/CDADB";
	private static String usuario = "mpbim";
	private static String password = "Enared01";
	
	private DatabaseMysqlConfig() {
	
		try {
			Class.forName(driver);

			conn = DriverManager.getConnection(url,usuario,password);
			System.out.println("Base conectada " + url);
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO,"Conexion BD:" + url  ,null);
		} catch (SQLException e) {
			e.printStackTrace();
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.ERROR,"Error en conexion BD",e);
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.ERROR, "Error en conexion BD",e);
		}
	}
	
	public static Connection getConnection(){
		 
	    if (conn == null){
	    	new DatabaseMysqlConfig();
	    }
	 
	    return conn;
	}
	
	
	public static void cerrarConexion(){
		try {
			conn.close();
			conn = null;
			System.out.println("Conexion Cerrada Mysql");
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO,"Cerrando Conexion BD:" + url  ,null);
		} catch (SQLException e) {
			e.printStackTrace();
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.ERROR,"Error al Cerrar Conexion BD:" + url  ,e);
		}
	}
	
	public static void setConexion(String ip,String puerto,String base,String user,String pass) {
		url = "jdbc:mysql://172.30.215.17:3306/CDADB";
		usuario = user;
		password = pass;
			
	}
	
	public static void setConexionProd(String ip,String user,String pass) {
		url = ip;
		usuario = user;
		password = pass;
			
	}

}
