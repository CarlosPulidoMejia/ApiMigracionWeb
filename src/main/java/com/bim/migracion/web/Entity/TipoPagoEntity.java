package com.bim.migracion.web.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name="tipopago")
public class TipoPagoEntity {
	
	@Id
	private String clave;
	private String descripcion;
	private String tipo;
	private String aplicadevolucion;
	private String tipodevolucion;

}
