package org.zapateria.logica;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estado_reparacion database table.
 * 
 */
@Entity
@Table(name="estado_reparacion")
@NamedQuery(name="EstadoReparacion.findAll", query="SELECT e FROM EstadoReparacion e")
public class EstadoReparacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String abreviacion;

	private String descripcion;

	private String nombre;

	//bi-directional many-to-one association to Reparacion
	@OneToMany(mappedBy="estadoReparacionBean")
	private List<Reparacion> reparacions;

	public EstadoReparacion() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAbreviacion() {
		return this.abreviacion;
	}

	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Reparacion> getReparacions() {
		return this.reparacions;
	}

	public void setReparacions(List<Reparacion> reparacions) {
		this.reparacions = reparacions;
	}

	public Reparacion addReparacion(Reparacion reparacion) {
		getReparacions().add(reparacion);
		reparacion.setEstadoReparacionBean(this);

		return reparacion;
	}

	public Reparacion removeReparacion(Reparacion reparacion) {
		getReparacions().remove(reparacion);
		reparacion.setEstadoReparacionBean(null);

		return reparacion;
	}

}