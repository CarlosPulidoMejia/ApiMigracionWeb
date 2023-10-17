package com.bim.migracion.web.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name="datasource")
public class DatasourceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_Datasource;
	private String ip;
	private String puerto;
	private String nombre_Base;
	private String usuario;
	private String pass;
	private String descripcion;
	private String clave;
	private int estado;
}
