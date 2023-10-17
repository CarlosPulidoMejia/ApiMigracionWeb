package com.bim.migracion.web.Service;

import java.util.List;

import com.bim.migracion.web.Entity.FirmadorEntity;
import com.bim.migracion.web.Request.FirmadorRequest;

public interface FirmadorService {

	public List<FirmadorEntity> listFirmador();
	
	public FirmadorRequest firmadorId(int idFirma);
	
	public String tokenFirmador(FirmadorRequest firmadorRequest);
}
