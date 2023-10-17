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
@Table (name="firmador")
public class FirmadorEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_Firmador;
	private String ip;
	private String puerto;
	private String usuario;
	private String pass;
	private String descripcion;
}
