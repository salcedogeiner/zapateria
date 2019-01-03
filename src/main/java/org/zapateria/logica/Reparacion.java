package org.zapateria.logica;

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

    public int calcularUtilidadReparacion() {
        return 0;
    }

    public Reparacion() {
    }

    public Calzado getCalzado() {
        return calzado;
    }

    public void setCalzado(Calzado calzado) {
        this.calzado = calzado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Double getComisionZapatero() {
        return comisionZapatero;
    }

    public void setComisionZapatero(Double comisionZapatero) {
        this.comisionZapatero = comisionZapatero;
    }

    public EstadoReparacion getEstadoReparacion() {
        return estadoReparacion;
    }

    public void setEstadoReparacion(EstadoReparacion estadoReparacion) {
        this.estadoReparacion = estadoReparacion;
    }

    public int getEstimacionReparacion() {
        return estimacionReparacion;
    }

    public void setEstimacionReparacion(int estimacionReparacion) {
        this.estimacionReparacion = estimacionReparacion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Insumo> getInsumos() {
        return insumos;
    }

    public void setInsumos(Set<Insumo> insumos) {
        this.insumos = insumos;
    }

    public Set<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(Set<Pago> pagos) {
        this.pagos = pagos;
    }

    public Double getValorReparacion() {
        return valorReparacion;
    }

    public void setValorReparacion(Double valorReparacion) {
        this.valorReparacion = valorReparacion;
    }

    public Zapatero getZapateroEncargado() {
        return zapateroEncargado;
    }

    public void setZapateroEncargado(Zapatero zapateroEncargado) {
        this.zapateroEncargado = zapateroEncargado;
    }

}//end Reparacion
