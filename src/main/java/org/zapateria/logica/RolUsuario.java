package org.zapateria.logica;

import java.io.Serializable;
import javax.persistence.*;
import org.zapateria.logica.Usuario;


/**
 * The persistent class for the rol_usuario database table.
 * 
 */
@Entity
@Table(name="rol_usuario")
@NamedQuery(name="RolUsuario.findAll", query="SELECT r FROM RolUsuario r")
public class RolUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	//bi-directional many-to-one association to Rol
	@ManyToOne
	@JoinColumn(name="rol")
	private Rol rolBean;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usuario")
	private Usuario usuarioBean;

	public RolUsuario() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Rol getRolBean() {
		return this.rolBean;
	}

	public void setRolBean(Rol rolBean) {
		this.rolBean = rolBean;
	}

	public Usuario getUsuarioBean() {
		return this.usuarioBean;
	}

	public void setUsuarioBean(Usuario usuarioBean) {
		this.usuarioBean = usuarioBean;
	}

}