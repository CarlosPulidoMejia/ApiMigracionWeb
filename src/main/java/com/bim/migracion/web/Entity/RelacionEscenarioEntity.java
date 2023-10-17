package com.bim.migracion.web.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "relacion_escenario")
public class RelacionEscenarioEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="id_env")
	@JsonIgnore
	private EscenariosEntity relMap;
	
	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "id_data")
    private DatasourceEntity data = new DatasourceEntity();
	
	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "id_firma")
    private FirmadorEntity dataFirm = new FirmadorEntity();
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_ordenante")
	private OrdenanteEntity ordenante;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_beneficiario")
	private BeneficiarioEntity beneficiario;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_detalle")
	private DetalleEntity detalle;
}
