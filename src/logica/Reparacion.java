package logica;

import java.util.Date;
import java.util.Set;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:34
 */
public class Reparacion {

	private Calzado calzado;
	private Cliente cliente;
	private Double comisionZapatero;	
	private EstadoReparacion estadoReparacion;
	private int estimacionReparacion;
	private Date fechaEntrega;
	private Date fechaSolicitud;	
	private Integer id;
	private Set<Insumo> insumos;
	private Set<Pago> pagos;
	private Double valorReparacion;	
	private Zapatero zapateroEncargado;



	public Reparacion(){

	}

	/**
	 * 
	 * @param id
	 */
	public Reparacion(Integer id){

	}

	/**
	 * 
	 * @param id
	 * @param fechaSolicitud
	 * @param estimacionReparacion
	 */
	public Reparacion(Integer id, Date fechaSolicitud, int estimacionReparacion){

	}

	public int calcularUtilidadReparacion(){
		return 0;
	}

	public Calzado getCalzado(){
		return null;
	}

	public Persona getCliente(){
		return null;
	}

	public int getComisionZapatero(){
		return 0;
	}

	public EstadoReparacion getEstadoReparacion(){
		return null;
	}

	public int getEstimacionReparacion(){
		return 0;
	}

	public Date getFechaEntrega(){
		return null;
	}

	public Date getFechaSolicitud(){
		return null;
	}

	public Integer getId(){
		return 0;
	}

	public Set<Insumo> getInsumos(){
		return null;
	}

	public Set<Pago> getPagos(){
		return null;
	}

	public int getValorReparacion(){
		return 0;
	}

	public Zapatero getZapateroEncargado(){
		return null;
	}

	/**
	 * 
	 * @param calzado
	 */
	public void setCalzado(Calzado calzado){

	}

	/**
	 * 
	 * @param persona
	 */
	public void setCliente(Cliente persona){

	}

	/**
	 * 
	 * @param comisionZapatero
	 */
	public void setComisionZapatero(Double comisionZapatero){

	}

	/**
	 * 
	 * @param estadoReparacion
	 */
	public void setEstadoReparacion(EstadoReparacion estadoReparacion){

	}

	/**
	 * 
	 * @param estimacionReparacion
	 */
	public void setEstimacionReparacion(int estimacionReparacion){

	}

	/**
	 * 
	 * @param fechaEntrega
	 */
	public void setFechaEntrega(Date fechaEntrega){

	}

	/**
	 * 
	 * @param fechaSolicitud
	 */
	public void setFechaSolicitud(Date fechaSolicitud){

	}

	/**
	 * 
	 * @param id
	 */
	public void setId(Integer id){

	}

	/**
	 * 
	 * @param insumos
	 */
	public void setInsumos(Set<Insumo> insumos){

	}

	/**
	 * 
	 * @param pagos
	 */
	public void setPagos(Set<Pago> pagos){

	}

	/**
	 * 
	 * @param valorReparacion
	 */
	public void setValorReparacion(Double valorReparacion){

	}

	/**
	 * 
	 * @param zapateroEncargado
	 */
	public void setZapateroEncargado(Zapatero zapateroEncargado){

	}
}//end Reparacion