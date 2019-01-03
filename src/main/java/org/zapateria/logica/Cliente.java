package org.zapateria.logica;

import java.util.Set;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:33
 */
public class Cliente extends Persona {

	private Set<Reparacion> reparaciones;

	public Cliente(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param reparacion
	 */
	public void agregarReparacion(Reparacion reparacion){

	}

	/**
	 * 
	 * @param estado
	 */
	public Set<Reparacion> buscarReparacionesPorEstado(EstadoReparacion estado){
		return null;
	}

	/**
	 * 
	 * @param reparacion
	 */
	public void eliminarReparacion(Reparacion reparacion){

	}

	public Set<Reparacion> listarReparaciones(){
		return null;
	}
}//end Cliente