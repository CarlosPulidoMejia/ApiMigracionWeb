package com.bim.migracion.web.Entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "beneficiario")
public class BeneficiarioEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int idBeneficiario;
	
	public String banco;
	
	public String tipoCuenta;
	
	public String cuenta;
	
	public String nombre;
	
	public String rfc;
	
	public String modulo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_beneficiario")
	@JsonIgnore
	private EnvioRelEntity beneficiarioMap;
	
}
