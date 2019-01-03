package org.zapateria.entidad;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the reparacion database table.
 * 
 */
@Entity
@NamedQuery(name="Reparacion.findAll", query="SELECT r FROM Reparacion r")
public class Reparacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="caracteristica_calzado")
	private String caracteristicaCalzado;

	@Column(name="comision_zapatero")
	private BigDecimal comisionZapatero;

	@Column(name="estimacion_reparacion")
	private Integer estimacionReparacion;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_entrega")
	private Date fechaEntrega;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_solicitud")
	private Date fechaSolicitud;

	@Column(name="valor_reparacion")
	private BigDecimal valorReparacion;

	//bi-directional many-to-one association to IsumoReparacion
	@OneToMany(mappedBy="reparacionBean")
	private List<IsumoReparacion> isumoReparacions;

	//bi-directional many-to-one association to Pago
	@OneToMany(mappedBy="reparacionBean")
	private List<Pago> pagos;

	//bi-directional many-to-one association to Calzado
	@ManyToOne
	@JoinColumn(name="calzado")
	private Calzado calzadoBean;

	//bi-directional many-to-one association to EstadoReparacion
	@ManyToOne
	@JoinColumn(name="estado_reparacion")
	private EstadoReparacion estadoReparacionBean;

	//bi-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="cliente")
	private Persona persona1;

	//bi-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="zapatero_encargado")
	private Persona persona2;

	public Reparacion() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCaracteristicaCalzado() {
		return this.caracteristicaCalzado;
	}

	public void setCaracteristicaCalzado(String caracteristicaCalzado) {
		this.caracteristicaCalzado = caracteristicaCalzado;
	}

	public BigDecimal getComisionZapatero() {
		return this.comisionZapatero;
	}

	public void setComisionZapatero(BigDecimal comisionZapatero) {
		this.comisionZapatero = comisionZapatero;
	}

	public Integer getEstimacionReparacion() {
		return this.estimacionReparacion;
	}

	public void setEstimacionReparacion(Integer estimacionReparacion) {
		this.estimacionReparacion = estimacionReparacion;
	}

	public Date getFechaEntrega() {
		return this.fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public Date getFechaSolicitud() {
		return this.fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public BigDecimal getValorReparacion() {
		return this.valorReparacion;
	}

	public void setValorReparacion(BigDecimal valorReparacion) {
		this.valorReparacion = valorReparacion;
	}

	public List<IsumoReparacion> getIsumoReparacions() {
		return this.isumoReparacions;
	}

	public void setIsumoReparacions(List<IsumoReparacion> isumoReparacions) {
		this.isumoReparacions = isumoReparacions;
	}

	public IsumoReparacion addIsumoReparacion(IsumoReparacion isumoReparacion) {
		getIsumoReparacions().add(isumoReparacion);
		isumoReparacion.setReparacionBean(this);

		return isumoReparacion;
	}

	public IsumoReparacion removeIsumoReparacion(IsumoReparacion isumoReparacion) {
		getIsumoReparacions().remove(isumoReparacion);
		isumoReparacion.setReparacionBean(null);

		return isumoReparacion;
	}

	public List<Pago> getPagos() {
		return this.pagos;
	}

	public void setPagos(List<Pago> pagos) {
		this.pagos = pagos;
	}

	public Pago addPago(Pago pago) {
		getPagos().add(pago);
		pago.setReparacionBean(this);

		return pago;
	}

	public Pago removePago(Pago pago) {
		getPagos().remove(pago);
		pago.setReparacionBean(null);

		return pago;
	}

	public Calzado getCalzadoBean() {
		return this.calzadoBean;
	}

	public void setCalzadoBean(Calzado calzadoBean) {
		this.calzadoBean = calzadoBean;
	}

	public EstadoReparacion getEstadoReparacionBean() {
		return this.estadoReparacionBean;
	}

	public void setEstadoReparacionBean(EstadoReparacion estadoReparacionBean) {
		this.estadoReparacionBean = estadoReparacionBean;
	}

	public Persona getPersona1() {
		return this.persona1;
	}

	public void setPersona1(Persona persona1) {
		this.persona1 = persona1;
	}

	public Persona getPersona2() {
		return this.persona2;
	}

	public void setPersona2(Persona persona2) {
		this.persona2 = persona2;
	}

}