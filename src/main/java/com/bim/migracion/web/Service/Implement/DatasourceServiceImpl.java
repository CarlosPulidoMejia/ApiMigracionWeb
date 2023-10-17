package com.bim.migracion.web.Service.Implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bim.migracion.web.Entity.DatasourceEntity;
import com.bim.migracion.web.Repository.DatasourceRepository;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Service.DatasourceService;

@Service
public class DatasourceServiceImpl implements DatasourceService{

	@Autowired
	private DatasourceRepository dataRepository;
	@Override
	public List<DatasourceEntity> listBases() {
		// TODO Auto-generated method stub
		return dataRepository.findAll();
	}
	@Override
	public Optional<DatasourceEntity> findBaseId(int idBase) {
		// TODO Auto-generated method stub
		Optional<DatasourceEntity> dataEntity = dataRepository.findById(idBase);
		
		
		return dataEntity;
	}
	@Override
	public DataSourceRequest findByIdSbe(int idBase) {
		// TODO Auto-generated method stub
		Optional<DatasourceEntity> dataEntity = dataRepository.findById(idBase);
		
		DataSourceRequest dataSybaseRequest = new DataSourceRequest();
		
		dataSybaseRequest.setId(idBase);
		dataSybaseRequest.setIp(dataEntity.get().getIp());
		dataSybaseRequest.setPuerto(dataEntity.get().getPuerto());
		dataSybaseRequest.setBase(dataEntity.get().getNombre_Base());
		dataSybaseRequest.setUsuario(dataEntity.get().getUsuario());
		dataSybaseRequest.setPass(dataEntity.get().getPass());
		dataSybaseRequest.setDescripcion(dataEntity.get().getDescripcion());
		
		return dataSybaseRequest;
	}

}
