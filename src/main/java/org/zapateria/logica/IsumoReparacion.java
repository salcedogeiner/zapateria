package org.zapateria.logica;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the isumo_reparacion database table.
 * 
 */
@Entity
@Table(name="isumo_reparacion")
@NamedQuery(name="IsumoReparacion.findAll", query="SELECT i FROM IsumoReparacion i")
public class IsumoReparacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="cantidad_estimada")
	private BigDecimal cantidadEstimada;

	@Column(name="cantidad_utilizada")
	private BigDecimal cantidadUtilizada;

	//bi-directional many-to-one association to Insumo
	@ManyToOne
	@JoinColumn(name="insumo")
	private Insumo insumoBean;

	//bi-directional many-to-one association to Reparacion
	@ManyToOne
	@JoinColumn(name="reparacion")
	private Reparacion reparacionBean;

	public IsumoReparacion() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getCantidadEstimada() {
		return this.cantidadEstimada;
	}

	public void setCantidadEstimada(BigDecimal cantidadEstimada) {
		this.cantidadEstimada = cantidadEstimada;
	}

	public BigDecimal getCantidadUtilizada() {
		return this.cantidadUtilizada;
	}

	public void setCantidadUtilizada(BigDecimal cantidadUtilizada) {
		this.cantidadUtilizada = cantidadUtilizada;
	}

	public Insumo getInsumoBean() {
		return this.insumoBean;
	}

	public void setInsumoBean(Insumo insumoBean) {
		this.insumoBean = insumoBean;
	}

	public Reparacion getReparacionBean() {
		return this.reparacionBean;
	}

	public void setReparacionBean(Reparacion reparacionBean) {
		this.reparacionBean = reparacionBean;
	}

}