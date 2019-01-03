package org.zapateria.entidad;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the pago database table.
 * 
 */
@Entity
@NamedQuery(name="Pago.findAll", query="SELECT p FROM Pago p")
public class Pago implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private BigDecimal valor;

	//bi-directional many-to-one association to Concepto
	@ManyToOne
	@JoinColumn(name="concepto")
	private Concepto conceptoBean;

	//bi-directional many-to-one association to Reparacion
	@ManyToOne
	@JoinColumn(name="reparacion")
	private Reparacion reparacionBean;

	public Pago() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Concepto getConceptoBean() {
		return this.conceptoBean;
	}

	public void setConceptoBean(Concepto conceptoBean) {
		this.conceptoBean = conceptoBean;
	}

	public Reparacion getReparacionBean() {
		return this.reparacionBean;
	}

	public void setReparacionBean(Reparacion reparacionBean) {
		this.reparacionBean = reparacionBean;
	}

}