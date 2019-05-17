package myapps.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;

public class DashboardDto implements Serializable {
    private static final long serialVersionUID = 1L;

	private String nombre;
    private BigDecimal monto;
    private BigDecimal cantidad;
    private BigDecimal total;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
