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
@Table(name = "detalle")
public class DetalleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idDetalle;
	
	private int cantidad;
	
	private double monto;
	
	private String concepto;
	
	private long segundos;
	
	private boolean registro;
	
	private boolean ampliado;
	
	private boolean firmador;
	
	private String modulo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_detalle")
	@JsonIgnore
	private EnvioRelEntity detalleMap;
}
