package org.zapateria.logica;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the rol_privilegio database table.
 * 
 */
@Entity
@Table(name="rol_privilegio")
@NamedQuery(name="RolPrivilegio.findAll", query="SELECT r FROM RolPrivilegio r")
public class RolPrivilegio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	//bi-directional many-to-one association to Privilegio
	@ManyToOne
	@JoinColumn(name="privilegio")
	private Privilegio privilegioBean;

	//bi-directional many-to-one association to Rol
	@ManyToOne
	@JoinColumn(name="rol")
	private Rol rolBean;

	public RolPrivilegio() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Privilegio getPrivilegioBean() {
		return this.privilegioBean;
	}

	public void setPrivilegioBean(Privilegio privilegioBean) {
		this.privilegioBean = privilegioBean;
	}

	public Rol getRolBean() {
		return this.rolBean;
	}

	public void setRolBean(Rol rolBean) {
		this.rolBean = rolBean;
	}

}