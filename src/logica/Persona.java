package logica;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:34
 */
public class Persona {

	private String apellidos;
	private String direccion;	
	private Integer id;
	private String identificacion;
	private String nombres;
	private String telefono;	
	private TipoIdentificacion tipoIdentificacion;
	private Usuario usuario;



	public void finalize() throws Throwable {

	}
	public Persona(){

	}

	/**
	 * 
	 * @param id
	 */
	public Persona(Integer id){

	}

	/**
	 * 
	 * @param id
	 * @param nombres
	 * @param apellidos
	 * @param identificacion
	 * @param tipoIdentificacion
	 */
	public Persona(Integer id, String nombres, String apellidos, String identificacion, TipoIdentificacion tipoIdentificacion){

	}

	public String getApellidos(){
		return "";
	}

	public String getDireccion(){
		return "";
	}

	public Integer getId(){
		return 0;
	}

	public String getIdentificacion(){
		return "";
	}

	public String getNombres(){
		return "";
	}

	public String getTelefono(){
		return "";
	}

	public TipoIdentificacion getTipoIdentificacion(){
		return null;
	}

	public Usuario getUsuario(){
		return null;
	}

	/**
	 * 
	 * @param apellidos
	 */
	public void setApellidos(String apellidos){

	}

	/**
	 * 
	 * @param direccion
	 */
	public void setDireccion(String direccion){

	}

	/**
	 * 
	 * @param id
	 */
	public void setId(Integer id){

	}

	/**
	 * 
	 * @param identificacion
	 */
	public void setIdentificacion(String identificacion){

	}

	/**
	 * 
	 * @param nombres
	 */
	public void setNombres(String nombres){

	}

	/**
	 * 
	 * @param telefono
	 */
	public void setTelefono(String telefono){

	}

	/**
	 * 
	 * @param tipoIdentificacion
	 */
	public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion){

	}

	/**
	 * 
	 * @param usuario
	 */
	public void setUsuario(Usuario usuario){

	}
}//end Persona