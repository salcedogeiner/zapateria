package logica;

import java.util.Set;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:34
 */
public class Zapatero extends Persona {

	private Set<Reparacion> reparaciones;

	public Zapatero(){

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
	 * @param r
	 */
	public void eliminarReparacion(Reparacion r){

	}

	public Set<Reparacion> listarReparaciones(){
		return null;
	}
}//end Zapatero