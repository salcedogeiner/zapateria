package logica;

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



	public void finalize() throws Throwable {

	}
	public Usuario(){

	}

	/**
	 * 
	 * @param id
	 */
	public Usuario(Integer id){

	}

	/**
	 * 
	 * @param id
	 * @param nombreUsuario
	 * @param clave
	 */
	public Usuario(Integer id, String nombreUsuario, String clave){

	}

	/**
	 * 
	 * @param object
	 */
	@Override
	public boolean equals(Object object){
		return false;
	}

	public String getClave(){
		return "";
	}

	public Integer getId(){
		return 0;
	}

	public String getNombreUsuario(){
		return "";
	}

	public Rol getRol(){
		return null;
	}

	public Set<Rol> getRoles(){
		return null;
	}

	@Override
	public int hashCode(){
		return 0;
	}

	/**
	 * 
	 * @param clave
	 */
	public void setClave(String clave){

	}

	/**
	 * 
	 * @param id
	 */
	public void setId(Integer id){

	}

	/**
	 * 
	 * @param nombreUsuario
	 */
	public void setNombreUsuario(String nombreUsuario){

	}

	/**
	 * 
	 * @param rol
	 */
	public void setRol(Rol rol){

	}

	/**
	 * 
	 * @param roles
	 */
	public void setRoles(Set<Rol> roles){

	}

	/**
	 * 
	 * @param usuario
	 * @param clave
	 */
	private boolean validarUsuario(String usuario, String clave){
		return false;
	}
}//end Usuario