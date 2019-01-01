package logica;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:33
 */
public class Pago {

	private Concepto concepto;
	private Date fecha;	
	private Integer id;
	private BigInteger valor;



	public void finalize() throws Throwable {

	}
	public Pago(){

	}

	/**
	 * 
	 * @param id
	 */
	public Pago(Integer id){

	}

	public Concepto getConcepto(){
		return null;
	}

	public Date getFecha(){
		return null;
	}

	public Integer getId(){
		return 0;
	}

	public BigInteger getValor(){
		return null;
	}

	/**
	 * 
	 * @param concepto
	 */
	public void setConcepto(Concepto concepto){

	}

	/**
	 * 
	 * @param fecha
	 */
	public void setFecha(Date fecha){

	}

	/**
	 * 
	 * @param id
	 */
	public void setId(Integer id){

	}

	/**
	 * 
	 * @param valor
	 */
	public void setValor(BigInteger valor){

	}
}//end Pago