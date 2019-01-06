package org.zapateria.logica;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the rol database table.
 * 
 */
@Entity
@NamedQuery(name="Rol.findAll", query="SELECT r FROM Rol r")
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String abreviacion;

	private String descripcion;

	private String nombre;

	//bi-directional many-to-one association to RolPrivilegio
	@OneToMany(mappedBy="rolBean")
	private List<RolPrivilegio> rolPrivilegios;

	//bi-directional many-to-one association to RolUsuario
	@OneToMany(mappedBy="rolBean")
	private List<RolUsuario> rolUsuarios;

	public Rol() {
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
		rolPrivilegio.setRolBean(this);

		return rolPrivilegio;
	}

	public RolPrivilegio removeRolPrivilegio(RolPrivilegio rolPrivilegio) {
		getRolPrivilegios().remove(rolPrivilegio);
		rolPrivilegio.setRolBean(null);

		return rolPrivilegio;
	}

	public List<RolUsuario> getRolUsuarios() {
		return this.rolUsuarios;
	}

	public void setRolUsuarios(List<RolUsuario> rolUsuarios) {
		this.rolUsuarios = rolUsuarios;
	}

	public RolUsuario addRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().add(rolUsuario);
		rolUsuario.setRolBean(this);

		return rolUsuario;
	}

	public RolUsuario removeRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().remove(rolUsuario);
		rolUsuario.setRolBean(null);

		return rolUsuario;
	}

}