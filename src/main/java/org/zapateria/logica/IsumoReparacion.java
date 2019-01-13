/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.logica;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author g.salcedo
 */
@Entity
@Table(name = "isumo_reparacion", catalog = "zapateriadb", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IsumoReparacion.findAll", query = "SELECT i FROM IsumoReparacion i"),
    @NamedQuery(name = "IsumoReparacion.findById", query = "SELECT i FROM IsumoReparacion i WHERE i.id = :id"),
    @NamedQuery(name = "IsumoReparacion.findByCantidadUtilizada", query = "SELECT i FROM IsumoReparacion i WHERE i.cantidadUtilizada = :cantidadUtilizada"),
    @NamedQuery(name = "IsumoReparacion.findByCantidadEstimada", query = "SELECT i FROM IsumoReparacion i WHERE i.cantidadEstimada = :cantidadEstimada")})
public class IsumoReparacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "cantidad_utilizada")
    private BigInteger cantidadUtilizada;
    @Basic(optional = false)
    @Column(name = "cantidad_estimada")
    private BigInteger cantidadEstimada;
    @JoinColumn(name = "insumo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Insumo insumo;
    @JoinColumn(name = "reparacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Reparacion reparacion;

    public IsumoReparacion() {
    }

    public IsumoReparacion(Integer id) {
        this.id = id;
    }

    public IsumoReparacion(Integer id, BigInteger cantidadEstimada) {
        this.id = id;
        this.cantidadEstimada = cantidadEstimada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(BigInteger cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
    }

    public BigInteger getCantidadEstimada() {
        return cantidadEstimada;
    }

    public void setCantidadEstimada(BigInteger cantidadEstimada) {
        this.cantidadEstimada = cantidadEstimada;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public Reparacion getReparacion() {
        return reparacion;
    }

    public void setReparacion(Reparacion reparacion) {
        this.reparacion = reparacion;
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
        if (!(object instanceof IsumoReparacion)) {
            return false;
        }
        IsumoReparacion other = (IsumoReparacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.zapateria.logica.IsumoReparacion[ id=" + id + " ]";
    }
    
}
