package com.bim.migracion.web.Service;

import java.util.List;
import java.util.Optional;

import com.bim.migracion.web.Entity.DatasourceEntity;
import com.bim.migracion.web.Request.DataSourceRequest;

public interface DatasourceService {

	public List<DatasourceEntity> listBases();
	
	public Optional<DatasourceEntity> findBaseId(int idBase);
	
	public DataSourceRequest findByIdSbe(int idBase);
}
