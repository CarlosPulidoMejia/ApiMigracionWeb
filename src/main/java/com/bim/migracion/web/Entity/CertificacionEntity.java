package com.bim.migracion.web.Entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.WhereJoinTable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "certificacion")
public class CertificacionEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	 	@Column(unique = true)
	    private String nombre;
	 	
	    private boolean registro;
	    

	    /*@OneToMany(mappedBy = "cert", cascade = CascadeType.ALL)
	    @OrderBy("idConcepto")
	    private Set<RegistroEntity> registre = new HashSet<>();*/
	    
	    /*@ManyToMany(cascade = CascadeType.ALL)
	    @OrderBy("id_Escenario")
	    @JoinTable(name="certificadoact",joinColumns = @JoinColumn(name="id"),inverseJoinColumns = { @JoinColumn(name="Id_Escenario")})
	    @WhereJoinTable(clause = "status=0")
	    @JsonIgnore
	    private Set<EscenarioEntity> esc = new HashSet<>();*/
	    
	    @OneToMany(cascade = CascadeType.ALL)
	    @OrderBy("id_Escenario")
	    @JoinTable(name="certificadoact",joinColumns = @JoinColumn(name="id"),inverseJoinColumns = { @JoinColumn(name="Id_Escenario")})
	    @WhereJoinTable(clause = "status=0")
	    @JsonIgnore
	    private Set<EscenarioEntity> esc = new HashSet<>();
}
