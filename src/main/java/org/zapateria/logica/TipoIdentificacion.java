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
@Table(name = "tipo_identificacion", catalog = "zapateriadb", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoIdentificacion.findAll", query = "SELECT t FROM TipoIdentificacion t"),
    @NamedQuery(name = "TipoIdentificacion.findById", query = "SELECT t FROM TipoIdentificacion t WHERE t.id = :id"),
    @NamedQuery(name = "TipoIdentificacion.findByNombre", query = "SELECT t FROM TipoIdentificacion t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoIdentificacion.findByDescripcion", query = "SELECT t FROM TipoIdentificacion t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoIdentificacion.findByAbreviacion", query = "SELECT t FROM TipoIdentificacion t WHERE t.abreviacion = :abreviacion")})
public class TipoIdentificacion implements Serializable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoIdentificacion")
    private Set<Persona> personaSet;

    public TipoIdentificacion() {
    }

    public TipoIdentificacion(Integer id) {
        this.id = id;
    }

    public TipoIdentificacion(Integer id, String nombre) {
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
    public Set<Persona> getPersonaSet() {
        return personaSet;
    }

    public void setPersonaSet(Set<Persona> personaSet) {
        this.personaSet = personaSet;
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
        if (!(object instanceof TipoIdentificacion)) {
            return false;
        }
        TipoIdentificacion other = (TipoIdentificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
    
}
