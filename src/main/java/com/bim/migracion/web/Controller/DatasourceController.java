package com.bim.migracion.web.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bim.migracion.web.Entity.DatasourceEntity;
import com.bim.migracion.web.Service.DatasourceService;

@RestController
@CrossOrigin("*")
public class DatasourceController {

	@Autowired
	private DatasourceService datasourceService;
	
	@GetMapping("/bim/mw/datasource")
	public List<DatasourceEntity> listDataBasesAPI(){
		return datasourceService.listBases();
	}
}
