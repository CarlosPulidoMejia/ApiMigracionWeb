package com.bim.migracion.web.SibamexStoreVAPA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;

@Component
public class ActualizarRegistrosVAPA {
	
	@Autowired
	PropiedadesSybase propiedades;
	
	public String execVaPa(String NumTransac, String Transaccio, String Usuario, String FechaSis, String SucOrigen, String SucDestino, String Modulo) {
		
		String query = "call SPVAAPSPPRO ";

		query += NumTransac + ", " + Transaccio + ", '" + Usuario + "', '" + FechaSis + "', '" + SucOrigen + "', '" + SucDestino + "', '" + Modulo + "'";
		propiedades.ejecutaStore(query);		
		return null; 
	}
}
