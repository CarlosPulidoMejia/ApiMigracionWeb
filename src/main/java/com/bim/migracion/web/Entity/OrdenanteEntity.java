package com.bim.migracion.web.Entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ordenante")
public class OrdenanteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int idOrdenante;
	
	public String banco;
	
	public String tipoCuenta;
	
	public String cuenta;
	
	public String nombre;
	
	public String rfc;
	
	public String modulo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_ordenante")
	@JsonIgnore
	private EnvioRelEntity ordenanteMap;
}
