package com.bim.migracion.web.Banxico;

import java.util.List;

public class SerieBmx {

	private String idSerie;
	
	private String titulo;
	
	private List<DataSerieBmx>datos;

	public String getIdSerie() {
		return idSerie;
	}

	public void setIdSerie(String idSerie) {
		this.idSerie = idSerie;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<DataSerieBmx> getDatos() {
		return datos;
	}

	public void setDatos(List<DataSerieBmx> datos) {
		this.datos = datos;
	}
}
