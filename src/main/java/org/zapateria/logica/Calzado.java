/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.logica;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author g.salcedo
 */
@Entity
@Table(name = "calzado", catalog = "zapateriadb", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calzado.findAll", query = "SELECT c FROM Calzado c"),
    @NamedQuery(name = "Calzado.findById", query = "SELECT c FROM Calzado c WHERE c.id = :id"),
    @NamedQuery(name = "Calzado.findByMarca", query = "SELECT c FROM Calzado c WHERE c.marca = :marca"),
    @NamedQuery(name = "Calzado.findByColor", query = "SELECT c FROM Calzado c WHERE c.color = :color"),
    @NamedQuery(name = "Calzado.findByTalla", query = "SELECT c FROM Calzado c WHERE c.talla = :talla"),
    @NamedQuery(name = "Calzado.findByMaterial", query = "SELECT c FROM Calzado c WHERE c.material = :material")})
public class Calzado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "marca")
    private String marca;
    @Basic(optional = false)
    @Column(name = "color")
    private String color;
    @Basic(optional = false)
    @Column(name = "talla")
    private String talla;
    @Basic(optional = false)
    @Column(name = "material")
    private String material;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "calzado")
    private Reparacion reparacion;

    public Calzado() {
    }

    public Calzado(Integer id) {
        this.id = id;
    }

    public Calzado(Integer id, String marca, String color, String talla, String material) {
        this.id = id;
        this.marca = marca;
        this.color = color;
        this.talla = talla;
        this.material = material;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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
        if (!(object instanceof Calzado)) {
            return false;
        }
        Calzado other = (Calzado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.marca + "-" + this.talla;
    }
    
}
