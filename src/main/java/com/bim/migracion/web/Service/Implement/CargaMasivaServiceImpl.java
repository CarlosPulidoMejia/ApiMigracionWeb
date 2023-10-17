package com.bim.migracion.web.Service.Implement;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.bim.migracion.web.Service.CargaMasivaService;
import com.bim.migracion.web.Service.ContingenciaService;


@Service
public class CargaMasivaServiceImpl implements CargaMasivaService{

	@Autowired
	private ContingenciaService contingenciaService;
	
	@Override
	public void cargaMasiva(String user, MultipartFile file) {
		// TODO Auto-generated method stub
		System.out.println("123456");
		
		contingenciaService.uploadFileContingencia(user,file);

		/*DataSourceRequest dataSourceRequest = new DataSourceRequest();
		int idBase;

		ObjectMapper mapper = new ObjectMapper();
		
		String mensaje = "Enviando registros contingencia: ";

		try {
			cargaMasivaRequest = mapper.readValue(user, DataContingenciaRequest.class);

			idBase = cargaMasivaRequest.getIdBase();
			
			
			System.out.println("Hola mundo carga masiva ---" +  idBase);
			
			InputStream inputStream = file.getInputStream();
			

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			dataSourceRequest = dataService.findByIdSbe(idBase);
			DataSourceSybase.setConexion(dataSourceRequest.getIp(), dataSourceRequest.getPuerto(), dataSourceRequest.getBase(),dataSourceRequest.getUsuario() , dataSourceRequest.getPass());
			contingenciaService.readFileContingencia(br);
			DataSourceSybase.cerrarConexion();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
	}

}
