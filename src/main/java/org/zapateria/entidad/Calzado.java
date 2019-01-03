package org.zapateria.entidad;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the calzado database table.
 * 
 */
@Entity
@NamedQuery(name="Calzado.findAll", query="SELECT c FROM Calzado c")
public class Calzado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String color;

	private String marca;

	private String material;

	private String talla;

	//bi-directional many-to-one association to Reparacion
	@OneToMany(mappedBy="calzadoBean")
	private List<Reparacion> reparacions;

	public Calzado() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMarca() {
		return this.marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getMaterial() {
		return this.material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getTalla() {
		return this.talla;
	}

	public void setTalla(String talla) {
		this.talla = talla;
	}

	public List<Reparacion> getReparacions() {
		return this.reparacions;
	}

	public void setReparacions(List<Reparacion> reparacions) {
		this.reparacions = reparacions;
	}

	public Reparacion addReparacion(Reparacion reparacion) {
		getReparacions().add(reparacion);
		reparacion.setCalzadoBean(this);

		return reparacion;
	}

	public Reparacion removeReparacion(Reparacion reparacion) {
		getReparacions().remove(reparacion);
		reparacion.setCalzadoBean(null);

		return reparacion;
	}

}