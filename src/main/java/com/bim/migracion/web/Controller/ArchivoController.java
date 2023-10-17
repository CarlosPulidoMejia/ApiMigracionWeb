package com.bim.migracion.web.Controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bim.migracion.web.Request.ArchivoRequest;
import com.bim.migracion.web.Request.ParametrosMCRequest;
import com.bim.migracion.web.Service.ArchivosService;

@RestController
public class ArchivoController {

	@Autowired
	private ArchivosService archivoService;
	
	@GetMapping("api/bim/archivo")
	public void leer() {
		String archivo = "C:\\MigracionWeb\\Certificaciones\\POA\\MC\\ParametrosMC.txt";
		File fileArch = new File(archivo);
		List<ArchivoRequest> listArch = archivoService.leerArchvivo(fileArch);
		
		ParametrosMCRequest parametrosMC = new ParametrosMCRequest();
		//String[] datos;
		//int numero;
		for(ArchivoRequest arch: listArch) {
			System.out.println("Linea numero: " +arch.getNumeroLinea());
			System.out.println("Dato archivo: " + arch.getDatoLinea());
			//datos = arch.getDatoLinea().split(",");
			//numero= datos[1];
			/*
			System.out.println("Valor: " + datos[1]);
			if(arch.getNumeroLinea() == 0) {
				parametrosMC.setClabeBancos(Integer.parseInt(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 1) {
				parametrosMC.setClabeBanco(Integer.valueOf(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 2) {
				parametrosMC.setNombreBanco(datos[1]);
			}
			
			if(arch.getNumeroLinea() == 3) {
				parametrosMC.setCuentaBanco(datos[1]);
			}
			if(arch.getNumeroLinea() == 4) {
				parametrosMC.setNombreTBanco(datos[1]);
			}
			
			if(arch.getNumeroLinea() == 5) {
				parametrosMC.setRfcBanco(datos[1]);
			}
			
			if(arch.getNumeroLinea() == 6) {
				parametrosMC.setStatusBanco(datos[1]);
			}*/
			
		}
		
		System.out.println(parametrosMC.toString());
		
	}
}
