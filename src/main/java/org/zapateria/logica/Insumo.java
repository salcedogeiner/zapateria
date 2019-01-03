package org.zapateria.logica;

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

    public Insumo() {
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public BigInteger getCantidadEstimada() {
        return cantidadEstimada;
    }

    public void setCantidadEstimada(BigInteger cantidadEstimada) {
        this.cantidadEstimada = cantidadEstimada;
    }

    public BigInteger getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(BigInteger cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
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

}//end Insumo
