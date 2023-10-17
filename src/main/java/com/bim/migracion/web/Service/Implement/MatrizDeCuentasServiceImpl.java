package com.bim.migracion.web.Service.Implement;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Request.ArchivoRequest;
import com.bim.migracion.web.Request.BeneficiarioRequest;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.DetalleOrdenRequest;
import com.bim.migracion.web.Request.MatrizDeCuentasRequest;
import com.bim.migracion.web.Request.ParametrosMCRequest;
import com.bim.migracion.web.Service.ArchivosService;
import com.bim.migracion.web.Service.DatasourceService;
import com.bim.migracion.web.Service.MatrizDeCuentasService;
import com.bim.migracion.web.Utilidades.Utilidades;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MatrizDeCuentasServiceImpl implements MatrizDeCuentasService{

	@Autowired
	private ArchivosService archivoService;
	
	@Autowired
	private DatasourceService dataService;
	
	@Autowired
	private Utilidades utilidades;
	@Override
	public String generarMatriz(MultipartFile file,String datos) {
		// TODO Auto-generated method stub
		ParametrosMCRequest parametr = new ParametrosMCRequest();
		MatrizDeCuentasRequest matriz;
		ObjectMapper mapper = new ObjectMapper();
		StringBuffer texto = new StringBuffer();
		
		String nombreArchivo="";
		
		String date = utilidades.obtenerFecha();
		
		DataSourceRequest dataRequest = new DataSourceRequest();
		try {
			
			
			matriz = mapper.readValue(datos, MatrizDeCuentasRequest.class);
			System.out.println("Datos");
			System.out.println(matriz.toString());
			
			dataRequest = dataService.findByIdSbe(matriz.getIdBase());
			
			DataSourceSybase.setConexion(dataRequest.getIp(), dataRequest.getPuerto(), dataRequest.getBase(),dataRequest.getUsuario() , dataRequest.getPass());

			nombreArchivo = "insumos\\" + matriz.getTipoCertificacion() + "_" + date;
			//nombreArchivo
			
			
			
			
			//System.out.println(matriz.getEncabezadoMatriz().getNombre());
			//System.out.println("Nombre Ordenante: " + matriz.getOrdenanteMatriz().getNombre());
			//System.out.println("RFC Ordenante: " + matriz.getOrdenanteMatriz().getRfc());
			
			/*for(DetalleOrdenRequest detalle: matriz.getDetalleMatriz()) {
				System.out.println("id : " + detalle.getCantidad());
			}*/
			texto.append(matriz.getEncabezadoMatriz().getNumCiclos()+"-"+matriz.getEncabezadoMatriz().getSegundos());
			texto.append("\n");
			texto.append(matriz.getOrdenanteMatriz().getBanco()+"-"+matriz.getOrdenanteMatriz().getCuenta()+"-"+matriz.getOrdenanteMatriz().getTipoCuenta()+"-"+matriz.getOrdenanteMatriz().getNombre()+"-"+matriz.getOrdenanteMatriz().getRfc());
			texto.append("\n");
			
			System.out.println("Datos base: ");
			System.out.println(dataRequest.toString());
			parametr = parametros(file);
			
			List<BeneficiarioRequest> listaBeneficiarios = archivoService.leerArchivoExcel(file,parametr);
			
			System.out.println("Imprimir lista final");
			for(DetalleOrdenRequest detalle: matriz.getDetalleMatriz()) {
				//System.out.println("id : " + detalle.getCantidad());
				if(detalle.getStatus().equals("CV")) {
					for(BeneficiarioRequest ben : listaBeneficiarios) {
						if(ben.getStatusBen().equals("CV")) {
							texto.append(detalle.getCantidad()+"-"+detalle.getTipoPago()+"-"+ detalle.getMonto() + "-" + ben.getBanco()+ "-" + ben.getCuenta() +"-40" + "-" + ben.getNombre() + "-" + ben.getRfc() + "-" + ben.getStatusBen() + "-" + detalle.getModulo() + "-0");
							texto.append("\n");
						}
					}
				}else {
					for(BeneficiarioRequest ben : listaBeneficiarios) {
						//System.out.println("Beneficiario: " + ben.getBanco());
						if(ben.getStatusBen().equals("CI")) {
							//texto.append(detalle.getCantidad()+"-"+detalle.getTipoPago()+"-"+ben.getBanco()+"-40"+"-" + ben.getCuenta() + "--" + ben.getStatusBen());
							texto.append(detalle.getCantidad()+"-"+detalle.getTipoPago()+"-"+ detalle.getMonto() + "-" + ben.getBanco() + "-"  + ben.getCuenta() +"-40"+"-" + ben.getNombre() + "-" + ben.getRfc() + "-" + ben.getStatusBen() + "-" + detalle.getModulo() + "-0");
							texto.append("\n");
						}
						
						
					}
				}
				
				
			}
			//System.out.println(texto);
			
			
			archivoService.crearArchivo(texto, nombreArchivo);
			
			
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nombreArchivo;
		
		
	}
	@Override
	public ParametrosMCRequest parametros(MultipartFile file) {
		// TODO Auto-generated method stub
		
		String archivo = "C:\\MigracionWeb\\Certificaciones\\POA\\MC\\ParametrosMC.txt";
		File fileArch = new File(archivo);
		List<ArchivoRequest> listArch = archivoService.leerArchvivo(fileArch);
		
		ParametrosMCRequest parametrosMC = new ParametrosMCRequest();
		String[] datos;
		for(ArchivoRequest arch: listArch) {
			//System.out.println("Linea numero: " +arch.getNumeroLinea());
			//System.out.println("Dato archivo: " + arch.getDatoLinea());
			datos = arch.getDatoLinea().split("-");
			//numero= datos[1];
			
			
			if(arch.getNumeroLinea() == 0) {
				parametrosMC.setInicio1(Integer.parseInt(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 1) {
				parametrosMC.setInicio2(Integer.parseInt(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 2) {
				parametrosMC.setClabeBancos(Integer.parseInt(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 3) {
				parametrosMC.setClabeBanco(Integer.valueOf(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 4) {
				parametrosMC.setNombreBanco(Integer.valueOf(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 5) {
				parametrosMC.setCuentaBanco(Integer.valueOf(datos[1]));
			}
			if(arch.getNumeroLinea() == 6) {
				parametrosMC.setNombreTBanco(Integer.valueOf(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 7) {
				parametrosMC.setRfcBanco(Integer.valueOf(datos[1]));
			}
			
			if(arch.getNumeroLinea() == 8) {
				parametrosMC.setStatusBanco(Integer.valueOf(datos[1]));
			}
		}
		
		//System.out.println(parametrosMC.toString());
		return parametrosMC;
		//return null;
	}

}
