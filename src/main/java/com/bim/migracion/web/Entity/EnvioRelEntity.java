package com.bim.migracion.web.Entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="enviosbd")
public class EnvioRelEntity {

	@Id
	@Column(name = "id_env")
	private int id;
	
	@Column(name="id_base")
	private int idBase;
	
	private int estados;
	
	@Column(name="id_firma")
	private int idFirma;
	
	@OneToOne(mappedBy = "ordenanteMap",fetch = FetchType.LAZY)
	private OrdenanteEntity ordenante;
	
	@OneToOne(mappedBy = "beneficiarioMap",fetch = FetchType.LAZY)
	private BeneficiarioEntity beneficiario;
	
	@OneToOne(mappedBy = "detalleMap",fetch = FetchType.LAZY)
	private DetalleEntity detalle;
	
	/*@ManyToOne
    @JoinColumn(name="id", nullable=false)
	@JsonIgnore
    private EnviosEntity cart;*/

}
