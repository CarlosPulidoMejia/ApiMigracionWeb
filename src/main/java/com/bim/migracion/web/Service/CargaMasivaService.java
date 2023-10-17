package com.bim.migracion.web.Service;

import org.springframework.web.multipart.MultipartFile;

public interface CargaMasivaService {

	public void cargaMasiva(String user,MultipartFile file);
}
