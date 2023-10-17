package com.bim.migracion.web.Logs;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


public class GuardaLogs {
	//private static Logger log = Logger.getLogger(GuardaLogs.class);
	
	//private static Logger logger = LoggerFactory.getLogger(GuardaLogs.class);
	
	private static Logger loggers = LogManager.getLogger(GuardaLogs.class);

	@SuppressWarnings("rawtypes")
	public static void registrarInfo(Class clase, TipoLog tipo, String mensaje,Exception e)
	{
		loggers.info(mensaje,e);
	}
}
