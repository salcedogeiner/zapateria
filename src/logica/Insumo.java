package logica;

import java.math.BigInteger;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:33
 */
public class Insumo {

	private String abreviacion;
	private BigInteger cantidad;
	private BigInteger cantidadEstimada;
	private BigInteger cantidadUtilizada;	
	private Integer id;
	private String nombre;



	public void finalize() throws Throwable {

	}
	public Insumo(){

	}

	/**
	 * 
	 * @param id
	 */
	public Insumo(Integer id){

	}

	/**
	 * 
	 * @param id
	 * @param nombre
	 * @param cantidad
	 */
	public Insumo(Integer id, String nombre, BigInteger cantidad){

	}

	public String getAbreviacion(){
		return "";
	}

	public BigInteger getCantidad(){
		return null;
	}

	public BigInteger getCantidadEstimada(){
		return null;
	}

	public BigInteger getCantidadUtilizada(){
		return null;
	}

	public Integer getId(){
		return 0;
	}

	public String getNombre(){
		return "";
	}

	/**
	 * 
	 * @param abreviacion
	 */
	public void setAbreviacion(String abreviacion){

	}

	/**
	 * 
	 * @param cantidad
	 */
	public void setCantidad(BigInteger cantidad){

	}

	/**
	 * 
	 * @param cantidadEstimada
	 */
	public void setCantidadEstimada(BigInteger cantidadEstimada){

	}

	/**
	 * 
	 * @param cantidadUtilizada
	 */
	public void setCantidadUtilizada(BigInteger cantidadUtilizada){

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
}//end Insumo