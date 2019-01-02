package logica;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:33
 */
public class EstadoReparacion {

    private String abreviacion;
    private String descripcion;
    private Integer id;
    private String nombre;

    public EstadoReparacion() {
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}//end EstadoReparacion
