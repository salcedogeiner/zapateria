package org.zapateria.logica;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the insumo database table.
 * 
 */
@Entity
@NamedQuery(name="Insumo.findAll", query="SELECT i FROM Insumo i")
public class Insumo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String abreviacion;

	private BigDecimal cantidad;

	private String nombre;

	//bi-directional many-to-one association to IsumoReparacion
	@OneToMany(mappedBy="insumoBean")
	private List<IsumoReparacion> isumoReparacions;

	public Insumo() {
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

	public BigDecimal getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<IsumoReparacion> getIsumoReparacions() {
		return this.isumoReparacions;
	}

	public void setIsumoReparacions(List<IsumoReparacion> isumoReparacions) {
		this.isumoReparacions = isumoReparacions;
	}

	public IsumoReparacion addIsumoReparacion(IsumoReparacion isumoReparacion) {
		getIsumoReparacions().add(isumoReparacion);
		isumoReparacion.setInsumoBean(this);

		return isumoReparacion;
	}

	public IsumoReparacion removeIsumoReparacion(IsumoReparacion isumoReparacion) {
		getIsumoReparacions().remove(isumoReparacion);
		isumoReparacion.setInsumoBean(null);

		return isumoReparacion;
	}

}