/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.logica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author g.salcedo
 */
@Entity
@Table(name = "reparacion", catalog = "zapateriadb", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reparacion.findAll", query = "SELECT r FROM Reparacion r"),
    @NamedQuery(name = "Reparacion.findById", query = "SELECT r FROM Reparacion r WHERE r.id = :id"),
    @NamedQuery(name = "Reparacion.findByFechaSolicitud", query = "SELECT r FROM Reparacion r WHERE r.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "Reparacion.findByEstimacionReparacion", query = "SELECT r FROM Reparacion r WHERE r.estimacionReparacion = :estimacionReparacion"),
    @NamedQuery(name = "Reparacion.findByFechaEntrega", query = "SELECT r FROM Reparacion r WHERE r.fechaEntrega = :fechaEntrega"),
    @NamedQuery(name = "Reparacion.findByValorReparacion", query = "SELECT r FROM Reparacion r WHERE r.valorReparacion = :valorReparacion"),
    @NamedQuery(name = "Reparacion.findByCaracteristicaCalzado", query = "SELECT r FROM Reparacion r WHERE r.caracteristicaCalzado = :caracteristicaCalzado"),
    @NamedQuery(name = "Reparacion.findByComisionZapatero", query = "SELECT r FROM Reparacion r WHERE r.comisionZapatero = :comisionZapatero")})
public class Reparacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitud;
    @Basic(optional = false)
    @Column(name = "estimacion_reparacion")
    private int estimacionReparacion;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    @Column(name = "valor_reparacion")
    private BigInteger valorReparacion;
    @Column(name = "caracteristica_calzado")
    private String caracteristicaCalzado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "comision_zapatero")
    private BigDecimal comisionZapatero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reparacion")
    private Set<IsumoReparacion> isumoReparacionSet;
    @JoinColumn(name = "calzado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Calzado calzado;
    @JoinColumn(name = "estado_reparacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstadoReparacion estadoReparacion;
    @JoinColumn(name = "cliente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Persona cliente;
    @JoinColumn(name = "zapatero_encargado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Persona zapateroEncargado;
    @OneToMany(mappedBy = "reparacion")
    private Set<Pago> pagoSet;

    public Reparacion() {
    }

    public Reparacion(Integer id) {
        this.id = id;
    }

    public Reparacion(Integer id, Date fechaSolicitud, int estimacionReparacion) {
        this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.estimacionReparacion = estimacionReparacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public int getEstimacionReparacion() {
        return estimacionReparacion;
    }

    public void setEstimacionReparacion(int estimacionReparacion) {
        this.estimacionReparacion = estimacionReparacion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public BigInteger getValorReparacion() {
        return valorReparacion;
    }

    public void setValorReparacion(BigInteger valorReparacion) {
        this.valorReparacion = valorReparacion;
    }

    public String getCaracteristicaCalzado() {
        return caracteristicaCalzado;
    }

    public void setCaracteristicaCalzado(String caracteristicaCalzado) {
        this.caracteristicaCalzado = caracteristicaCalzado;
    }

    public BigDecimal getComisionZapatero() {
        return comisionZapatero;
    }

    public void setComisionZapatero(BigDecimal comisionZapatero) {
        this.comisionZapatero = comisionZapatero;
    }

    @XmlTransient
    public Set<IsumoReparacion> getIsumoReparacionSet() {
        return isumoReparacionSet;
    }

    public void setIsumoReparacionSet(Set<IsumoReparacion> isumoReparacionSet) {
        this.isumoReparacionSet = isumoReparacionSet;
    }

    public Calzado getCalzado() {
        return calzado;
    }

    public void setCalzado(Calzado calzado) {
        this.calzado = calzado;
    }

    public EstadoReparacion getEstadoReparacion() {
        return estadoReparacion;
    }

    public void setEstadoReparacion(EstadoReparacion estadoReparacion) {
        this.estadoReparacion = estadoReparacion;
    }

    public Persona getCliente() {
        return cliente;
    }

    public void setCliente(Persona cliente) {
        this.cliente = cliente;
    }

    public Persona getZapateroEncargado() {
        return zapateroEncargado;
    }

    public void setZapateroEncargado(Persona zapateroEncargado) {
        this.zapateroEncargado = zapateroEncargado;
    }

    @XmlTransient
    public Set<Pago> getPagoSet() {
        return pagoSet;
    }

    public void setPagoSet(Set<Pago> pagoSet) {
        this.pagoSet = pagoSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reparacion)) {
            return false;
        }
        Reparacion other = (Reparacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.zapateria.logica.Reparacion[ id=" + id + " ]";
    }
    
}
