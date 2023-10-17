package com.bim.migracion.web.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;


public class DataSourceSybase {
	
	public static void main(String args[]) {
		getConnection();
		cerrarConexion();
	}
	
	private static Connection conn = null;
	private static String driver = "net.sourceforge.jtds.jdbc.Driver";
	private static String url ="";
	private static String usuario = "";
	private static String password = "";
	
	private DataSourceSybase() {
	
		try {
			
			//Cargar el driver
			Class.forName(driver);
			
			// Creacion de la conexion a base
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
	    	new DataSourceSybase();
	    }
	 
	    return conn;
	}
	
	
	public static void cerrarConexion(){
		try {
			conn.close();
			conn = null;
			System.out.println("Conexion Cerrada");
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO,"Cerrando Conexion BD:" + url  ,null);
		} catch (SQLException e) {
			e.printStackTrace();
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.ERROR,"Error al Cerrar Conexion BD:" + url  ,e);
		}
	}
	
	public static void setConexion(String ip,String puerto,String base,String user,String pass) {
		url = "jdbc:jtds:sybase://"+ ip +":"+puerto+"/"+base+"";
		usuario = user;
		password = pass;
			
	}
	
	public static void setConexionProd(String ip,String user,String pass) {
		url = ip;
		usuario = user;
		password = pass;
			
	}

}
