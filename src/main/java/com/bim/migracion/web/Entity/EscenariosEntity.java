package com.bim.migracion.web.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="envios")
public class EscenariosEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	public String nombre;
	
	@OneToOne(mappedBy = "relMap",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private RelacionEscenarioEntity relacion;
	
}
