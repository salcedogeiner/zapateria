package org.zapateria.entidad;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the persona database table.
 * 
 */
@Entity
@NamedQuery(name="Persona.findAll", query="SELECT p FROM Persona p")
public class Persona implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String apellidos;

	private String clave;

	private String direccion;

	private String identificacion;

	private String nombres;

	private String telefono;

	private String tipo;

	//bi-directional many-to-one association to TipoIdentificacion
	@ManyToOne
	@JoinColumn(name="tipo_identificacion")
	private TipoIdentificacion tipoIdentificacionBean;

	//bi-directional many-to-one association to Reparacion
	@OneToMany(mappedBy="persona1")
	private List<Reparacion> reparacions1;

	//bi-directional many-to-one association to Reparacion
	@OneToMany(mappedBy="persona2")
	private List<Reparacion> reparacions2;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="personaBean")
	private List<Usuario> usuarios;

	public Persona() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getIdentificacion() {
		return this.identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public TipoIdentificacion getTipoIdentificacionBean() {
		return this.tipoIdentificacionBean;
	}

	public void setTipoIdentificacionBean(TipoIdentificacion tipoIdentificacionBean) {
		this.tipoIdentificacionBean = tipoIdentificacionBean;
	}

	public List<Reparacion> getReparacions1() {
		return this.reparacions1;
	}

	public void setReparacions1(List<Reparacion> reparacions1) {
		this.reparacions1 = reparacions1;
	}

	public Reparacion addReparacions1(Reparacion reparacions1) {
		getReparacions1().add(reparacions1);
		reparacions1.setPersona1(this);

		return reparacions1;
	}

	public Reparacion removeReparacions1(Reparacion reparacions1) {
		getReparacions1().remove(reparacions1);
		reparacions1.setPersona1(null);

		return reparacions1;
	}

	public List<Reparacion> getReparacions2() {
		return this.reparacions2;
	}

	public void setReparacions2(List<Reparacion> reparacions2) {
		this.reparacions2 = reparacions2;
	}

	public Reparacion addReparacions2(Reparacion reparacions2) {
		getReparacions2().add(reparacions2);
		reparacions2.setPersona2(this);

		return reparacions2;
	}

	public Reparacion removeReparacions2(Reparacion reparacions2) {
		getReparacions2().remove(reparacions2);
		reparacions2.setPersona2(null);

		return reparacions2;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setPersonaBean(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setPersonaBean(null);

		return usuario;
	}

}