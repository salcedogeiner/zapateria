package org.zapateria.logica;

import java.util.Set;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:34
 */
public class Usuario {

    private String clave;
    private Integer id;
    private String nombreUsuario;
    private Rol rol;
    private Set<Rol> roles;

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

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public boolean validarUsuario() {
        System.out.println(this.clave + this.nombreUsuario);
        if ("123".equals(this.clave) && "usuario".equals(this.nombreUsuario)) {
            
            return true;
        }
        return false;
    }
    
}//end Usuario 
