package com.bim.migracion.web.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.bim.migracion.web.Utilidades.Utilidades;

@CrossOrigin(origins = "*")
@RestController

public class ArchuvosMultipesController {
	
	@Autowired
	Utilidades utilidades;
	
	@PostMapping(value="api/bim/mw/archivosMultiples")
	public void archivosMultiples(@RequestParam("dataMasiva") String idConexion, @RequestParam("file") MultipartFile[] files) {
		try {
			System.out.println("id Conexion: " + idConexion);
			System.out.println("files: " + files.length);
				Map<Integer, String> mapaArchivo = utilidades.leerArchivos(idConexion, files);
				//System.out.println("mapa: " + mapaArchivo);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
