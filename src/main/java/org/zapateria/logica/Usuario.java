package org.zapateria.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.zapateria.logica.RolUsuario;

/**
 *
 * The persistent class for the usuario database table.
 *
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:34
 */
@Entity
@NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id principal de usuario
     */
    @Id
    private Integer id;

    /**
     * Clave de usuario
     */
    
    private String clave;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Transient
    private Rol rol;

    // private Set<Rol> roles;
    //bi-directional many-to-one association to RolUsuario
    @OneToMany(mappedBy = "usuarioBean")
    private List<RolUsuario> rolUsuarios;

    //bi-directional many-to-one association to Persona
    @ManyToOne
    @JoinColumn(name = "persona")
    private Persona personaBean;

    /**
     * Constructor por defecto
     */
    public Usuario() {
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<RolUsuario> getRolUsuarios() {
        return this.rolUsuarios;
    }

    public void setRolUsuarios(List<RolUsuario> rolUsuarios) {
        this.rolUsuarios = rolUsuarios;
    }
/*
    public RolUsuario addRolUsuario(RolUsuario rolUsuario) {
        getRolUsuarios().add(rolUsuario);
        rolUsuario.setUsuarioBean(this);
        return rolUsuario;
    }

    public RolUsuario removeRolUsuario(RolUsuario rolUsuario) {
        getRolUsuarios().remove(rolUsuario);
        rolUsuario.setUsuarioBean(null);

        return rolUsuario;
    }
*/
    public Persona getPersonaBean() {
        return this.personaBean;
    }

    public void setPersonaBean(Persona personaBean) {
        this.personaBean = personaBean;
    }

}//end Usuario 

