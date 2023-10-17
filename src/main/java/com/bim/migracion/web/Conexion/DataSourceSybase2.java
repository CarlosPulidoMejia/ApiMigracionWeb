package com.bim.migracion.web.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceSybase2 {
	private static Connection conn = null;
	private static String driver = "net.sourceforge.jtds.jdbc.Driver";
	private static String url ="";
	private static String usuario = "";
	private static String password = "";
	
	private DataSourceSybase2() {
	
		try {
			
			//Cargar el driver
			Class.forName(driver);
			
			// Creacion de la conexion a base
			conn = DriverManager.getConnection(url,usuario,password);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	
	public static Connection getConnection(){
		 
	    if (conn == null){
	    	new DataSourceSybase2();
	    }
	 
	    return conn;
	}
	
	
	public static void cerrarConexion(){
		try {
			conn.close();
			conn = null;
			System.out.println("Conexion Cerrada");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setConexion(String ip,String puerto,String base,String user,String pass) {
		url = "jdbc:jtds:sybase://" + ip + ":" + puerto + "/" + base;
		usuario = user;
		password = pass;
			
	}

}
