package com.bim.migracion.web.Service.Implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bim.migracion.web.Entity.FirmadorEntity;
import com.bim.migracion.web.Repository.FirmadorRepository;
import com.bim.migracion.web.Request.FirmadorRequest;
import com.bim.migracion.web.Request.RequestFirmadorLogin;
import com.bim.migracion.web.Response.LoginResponseFirmador;
import com.bim.migracion.web.Service.FirmadorService;

@Service
public class FirmadorServiceImpl implements FirmadorService {

	@Autowired
	private FirmadorRepository firmadorRepo;

	@Autowired
	RestTemplate restTemplate;

	@Override
	public List<FirmadorEntity> listFirmador() {
		// TODO Auto-generated method stub
		return firmadorRepo.findAll();
	}

	@Override
	public FirmadorRequest firmadorId(int idFirma) {
		// TODO Auto-generated method stub
		Optional<FirmadorEntity> firmadorEntity = firmadorRepo.findById(idFirma);

		FirmadorRequest firmaRequestId = new FirmadorRequest();

		firmaRequestId.setIp(firmadorEntity.get().getIp());
		firmaRequestId.setPuerto(firmadorEntity.get().getPuerto());
		firmaRequestId.setUsuario(firmadorEntity.get().getUsuario());
		firmaRequestId.setPass(firmadorEntity.get().getPass());
		firmaRequestId.setFirmar(true);

		return firmaRequestId;
	}

	@Override
	public String tokenFirmador(FirmadorRequest firmadorRequest) {
		// TODO Auto-generated method stub
		String token="";

		try {
			String uri = "http://" + firmadorRequest.getIp() + ":" + firmadorRequest.getPuerto() + "/api/authenticate";

			System.out.println(uri);

			// Datos Firmador
			RequestFirmadorLogin login = new RequestFirmadorLogin();
			login.setUsername(firmadorRequest.getUsuario());
			login.setPassword(firmadorRequest.getPass());

			LoginResponseFirmador responseLogin = restTemplate.postForObject(uri, login, LoginResponseFirmador.class);
			token = "Bearer " + responseLogin.getJwt();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return token;
	}

}
