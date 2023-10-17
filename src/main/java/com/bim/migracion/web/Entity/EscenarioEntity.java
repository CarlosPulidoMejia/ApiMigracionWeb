package com.bim.migracion.web.Entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name="actividad")
public class EscenarioEntity {
    
	@Column(name="Id_Escenario")
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_Escenario;
	
	@Column(name="Descripcion")
	private String descripcion;
	
	@Column(name="Id_Datasource")
	private int id_Datasource;
	
	@Column(name="Id_Firmador")
    private int id_Firmador;
	
	@Column(name="Clave_Pago")
    private String clave_Pago;
	
	@Column(name="Cantidad")
    private int cantidad;
	
	@Column(name="Monto")
    private double monto;
	
	@Column(name="Concepto")
    private String concepto;
	
	@Column(name="Modulo")
    private String modulo;
	
	@Column(name="Segundos")
    private int segundos;
	
	@Column(name="Registro")
    private boolean registro;
	
	@Column(name="Ampliador")
    private boolean ampliador;
	
	@Column(name="OBanco")
    private String oBanco;
	
	@Column(name="otipocuenta")
    private String oTipoCuenta;
	
	@Column(name="OCuenta")
    private String oCuenta;
	
	@Column(name="ONombre")
    private String oNombre;
	
	@Column(name="ORFC")
    private String oRFC;
    
    @Column(name="BBanco")
    private String bBanco;
    
    @Column(name="btipocuenta")
    private String bTipoCuenta;
    
    @Column(name="BCuenta")
    private String bCuenta;

    @Column(name="BNombre")
    private String bNombre;
    
    @Column(name="BRFC")
    private String bRFC;
    
    @Column(name="estado")
    private int estado;
    
    @Column(name = "pago_Origen")
    private String pagoOrigen;
    
    @Column(name = "cantidad_Origen")
    private int cantidadOrigen;
    
    @ManyToMany(mappedBy = "esc",fetch = FetchType.LAZY)
    Set<CertificacionEntity> cert;
    
    
}
