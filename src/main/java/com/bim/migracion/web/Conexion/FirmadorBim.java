package com.bim.migracion.web.Conexion;


public class FirmadorBim {
	private static String url ="";
	@SuppressWarnings("unused")
	private static String usuario = "";
	@SuppressWarnings("unused")
	private static String password = "";
	
	private FirmadorBim() {
		getConnection();
		System.out.println("Conexion firmador " + url);
	}
	
	public static void setConexionFirmador(String ip,String puerto,String user,String pass) {
		url = "http://" + ip + ":" + puerto + "/api/authenticate";
		usuario = user;
		password = pass;
	}
	
	public static String getConnection(){
		 System.out.println("Firmador" + url);
	    if (url == null){
	    	new FirmadorBim();
	    }
	    
	    return url;

	}
}
