package org.zapateria.entidad;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the privilegio database table.
 * 
 */
@Entity
@NamedQuery(name="Privilegio.findAll", query="SELECT p FROM Privilegio p")
public class Privilegio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String abreviacion;

	private String descripcion;

	private String nombre;

	//bi-directional many-to-one association to RolPrivilegio
	@OneToMany(mappedBy="privilegioBean")
	private List<RolPrivilegio> rolPrivilegios;

	public Privilegio() {
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

	public List<RolPrivilegio> getRolPrivilegios() {
		return this.rolPrivilegios;
	}

	public void setRolPrivilegios(List<RolPrivilegio> rolPrivilegios) {
		this.rolPrivilegios = rolPrivilegios;
	}

	public RolPrivilegio addRolPrivilegio(RolPrivilegio rolPrivilegio) {
		getRolPrivilegios().add(rolPrivilegio);
		rolPrivilegio.setPrivilegioBean(this);

		return rolPrivilegio;
	}

	public RolPrivilegio removeRolPrivilegio(RolPrivilegio rolPrivilegio) {
		getRolPrivilegios().remove(rolPrivilegio);
		rolPrivilegio.setPrivilegioBean(null);

		return rolPrivilegio;
	}

}