/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.logica;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author g.salcedo
 */
@Entity
@Table(name = "estado_reparacion", catalog = "zapateriadb", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoReparacion.findAll", query = "SELECT e FROM EstadoReparacion e"),
    @NamedQuery(name = "EstadoReparacion.findById", query = "SELECT e FROM EstadoReparacion e WHERE e.id = :id"),
    @NamedQuery(name = "EstadoReparacion.findByNombre", query = "SELECT e FROM EstadoReparacion e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoReparacion.findByDescripcion", query = "SELECT e FROM EstadoReparacion e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "EstadoReparacion.findByAbreviacion", query = "SELECT e FROM EstadoReparacion e WHERE e.abreviacion = :abreviacion")})
public class EstadoReparacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "abreviacion")
    private String abreviacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoReparacion")
    private Set<Reparacion> reparacionSet;

    public EstadoReparacion() {
    }

    public EstadoReparacion(Integer id) {
        this.id = id;
    }

    public EstadoReparacion(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    @XmlTransient
    public Set<Reparacion> getReparacionSet() {
        return reparacionSet;
    }

    public void setReparacionSet(Set<Reparacion> reparacionSet) {
        this.reparacionSet = reparacionSet;
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
        if (!(object instanceof EstadoReparacion)) {
            return false;
        }
        EstadoReparacion other = (EstadoReparacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.zapateria.logica.EstadoReparacion[ id=" + id + " ]";
    }
    
}
