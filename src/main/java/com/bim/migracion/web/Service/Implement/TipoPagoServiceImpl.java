package com.bim.migracion.web.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bim.migracion.web.Entity.TipoPagoEntity;
import com.bim.migracion.web.Repository.TipoPagoRepository;
import com.bim.migracion.web.Service.TipoPagoService;

@Service
public class TipoPagoServiceImpl implements TipoPagoService{

	@Autowired
	private TipoPagoRepository pagoRepository;
	@Override
	public List<TipoPagoEntity> listPagos() {
		// TODO Auto-generated method stub
		return pagoRepository.findAll();
	}

}
