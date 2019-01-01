package logica;

import java.util.Set;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:34
 */
public class Rol {

	private String abreviacion;
	private String descripcion;	
	private Integer id;
	private String nombre;
	private Set<Privilegio> privillegios;



	public void finalize() throws Throwable {

	}
	public Rol(){

	}

	/**
	 * 
	 * @param id
	 */
	public Rol(Integer id){

	}

	/**
	 * 
	 * @param id
	 * @param nombre
	 */
	public Rol(Integer id, String nombre){

	}

	/**
	 * 
	 * @param object
	 */
	@Override
	public boolean equals(Object object){
		return false;
	}

	public String getAbreviacion(){
		return "";
	}

	public String getDescripcion(){
		return "";
	}

	public Integer getId(){
		return 0;
	}

	public String getNombre(){
		return "";
	}

	@Override
	public int hashCode(){
		return 0;
	}

	/**
	 * 
	 * @param abreviacion
	 */
	public void setAbreviacion(String abreviacion){

	}

	/**
	 * 
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion){

	}

	/**
	 * 
	 * @param id
	 */
	public void setId(Integer id){

	}

	/**
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre){

	}

	@Override
	public String toString(){
		return "";
	}
}//end Rol