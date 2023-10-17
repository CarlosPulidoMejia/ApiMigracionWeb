package com.bim.migracion.web.Service.Implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bim.migracion.web.Entity.EscenariosEntity;
import com.bim.migracion.web.Repository.EscenariosRepository;
import com.bim.migracion.web.Response.BeneficiarioResponse;
import com.bim.migracion.web.Response.DataSourceResponse;
import com.bim.migracion.web.Response.DetalleEscenarioResponse;
import com.bim.migracion.web.Response.DetalleOrdenResponse;
import com.bim.migracion.web.Response.EscenarioResponse;
import com.bim.migracion.web.Response.FirmadorResponse;
import com.bim.migracion.web.Response.OrdenanteResponse;
import com.bim.migracion.web.Service.EscenariosService;

@Service
public class EscenariosServiceImpl implements EscenariosService{
	
	@Autowired
	private EscenariosRepository escenarioRepo;

	@Override
	public List<DetalleEscenarioResponse> listarEscenarios() {
		// TODO Auto-generated method stub
		
		List<EscenariosEntity> listaEscenarios = escenarioRepo.findAll();
		
		
		
		List<DetalleEscenarioResponse> lista = new ArrayList<DetalleEscenarioResponse>();
		
		
		
		for(EscenariosEntity env : listaEscenarios) {
			
			EscenarioResponse esc = new EscenarioResponse();
			
			DetalleEscenarioResponse detalleEscenario = new DetalleEscenarioResponse();
			
			DataSourceResponse dataResponse = new DataSourceResponse();
			FirmadorResponse firmadorResponse = new FirmadorResponse();
			DetalleOrdenResponse detalle = new DetalleOrdenResponse();
			OrdenanteResponse ordenanteResponse = new OrdenanteResponse();
			BeneficiarioResponse beneficiarioResponse = new BeneficiarioResponse();
			
			
			
			esc.setNombre(env.getNombre());
			
			detalleEscenario.setIdEscenario(env.getId());
			detalleEscenario.setIdRelacion(env.getRelacion().getId());
			detalleEscenario.setNombreEscenario(env.getNombre());
			
			
			dataResponse.setId(env.getRelacion().getData().getId_Datasource());
			dataResponse.setBase(env.getRelacion().getData().getNombre_Base());
			dataResponse.setClave(env.getRelacion().getData().getClave());
			dataResponse.setDescripcion(env.getRelacion().getData().getDescripcion());
			dataResponse.setEstado(env.getRelacion().getData().getEstado());
			dataResponse.setIp(env.getRelacion().getData().getIp());
			dataResponse.setPuerto(env.getRelacion().getData().getPuerto());
			
			firmadorResponse.setId(env.getRelacion().getDataFirm().getId_Firmador());
			firmadorResponse.setDescripcion(env.getRelacion().getDataFirm().getDescripcion());
			firmadorResponse.setIp(env.getRelacion().getDataFirm().getIp());
			firmadorResponse.setPuerto(env.getRelacion().getDataFirm().getPuerto());
			
			detalle.setIdDetalle(env.getRelacion().getDetalle().getIdDetalle());
			detalle.setAmpliado(env.getRelacion().getDetalle().isAmpliado());
			detalle.setCantidad(env.getRelacion().getDetalle().getCantidad());
			detalle.setConcepto(env.getRelacion().getDetalle().getConcepto());
			detalle.setFirmador(env.getRelacion().getDetalle().isFirmador());
			detalle.setMonto(env.getRelacion().getDetalle().getMonto());
			detalle.setRegistro(env.getRelacion().getDetalle().isRegistro());
			detalle.setSegundos(env.getRelacion().getDetalle().getSegundos());
			
			
			ordenanteResponse.setIdOrdenante(env.getRelacion().getOrdenante().getIdOrdenante());
			ordenanteResponse.setBanco(env.getRelacion().getOrdenante().getBanco());
			ordenanteResponse.setCuenta(env.getRelacion().getOrdenante().getCuenta());
			ordenanteResponse.setNombre(env.getRelacion().getOrdenante().getNombre());
			ordenanteResponse.setRfc(env.getRelacion().getOrdenante().getRfc());
			ordenanteResponse.setTipoCuenta(env.getRelacion().getOrdenante().getTipoCuenta());
			
			beneficiarioResponse.setIdBeneficiario(env.getRelacion().getBeneficiario().getIdBeneficiario());
			beneficiarioResponse.setBanco(env.getRelacion().getBeneficiario().getBanco());
			beneficiarioResponse.setCuenta(env.getRelacion().getBeneficiario().getCuenta());
			beneficiarioResponse.setCuenta(env.getRelacion().getBeneficiario().getCuenta());
			beneficiarioResponse.setNombre(env.getRelacion().getBeneficiario().getNombre());
			beneficiarioResponse.setRfc(env.getRelacion().getBeneficiario().getRfc());
			beneficiarioResponse.setTipoCuenta(env.getRelacion().getBeneficiario().getTipoCuenta());
			
			
			detalleEscenario.setDataSourceResponse(dataResponse);
			detalleEscenario.setFirmadorResponse(firmadorResponse);
			detalleEscenario.setDetalleOrdenResponse(detalle);
			detalleEscenario.setOrdenanteResponse(ordenanteResponse);
			detalleEscenario.setBeneficiarioResponse(beneficiarioResponse);
			
			
			lista.add(detalleEscenario);
			}
		
		return lista;
	}

}
