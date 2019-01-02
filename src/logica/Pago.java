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

    public Pago() {
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

}//end Pago
