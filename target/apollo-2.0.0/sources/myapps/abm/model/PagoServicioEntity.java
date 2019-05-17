package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "PAGO_SERVICIO", schema = "SERVICIOS_BASICOS", catalog = "")
public class PagoServicioEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idPagoServicio;
    private Date fecha;
    private BigDecimal monto;
    private boolean estado;
    private ServicioEntity servicioByIdServicio;
    private String adjunto;
    private String estadoPago;
    private String factura;
    private Date periodo;
    private String tipoPago;
    private String obs;
    private String usuario;
    private HistorialPagosMasivosEntity hPagosMasivosByIdHPagoM;
    private String servicio;
    private BigDecimal mes;
    private BigDecimal montoXMes;
    private BigDecimal cantidadMedidor;
    private BigDecimal idProveedor;
    private String nombreProveedor;
    private BigDecimal medidorPagado;
    private BigDecimal medidorNoPagado;
    private BigDecimal cantidadPagos;

    @Id
    @Column(name = "ID_PAGO_SERVICIO")
    @SequenceGenerator(name = "SEQ_PAGO_SERVICIO_ID_GENERATOR", sequenceName = "SEQ_PAGO_SERVICIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAGO_SERVICIO_ID_GENERATOR")

    public long getIdPagoServicio() {
        return idPagoServicio;
    }

    public void setIdPagoServicio(long idPagoServicio) {
        this.idPagoServicio = idPagoServicio;
    }

    @Basic
    @Column(name = "FECHA")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "MONTO")
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    @Basic
    @Column(name = "ESTADO")
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagoServicioEntity that = (PagoServicioEntity) o;
        return idPagoServicio == that.idPagoServicio &&
                //  idServicio == that.idServicio &&
                estado == that.estado &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(monto, that.monto);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idPagoServicio, fecha, monto, estado);
    }

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO", nullable = false)
    public ServicioEntity getServicioByIdServicio() {
        return servicioByIdServicio;
    }

    public void setServicioByIdServicio(ServicioEntity servicioByIdServicio) {
        this.servicioByIdServicio = servicioByIdServicio;
    }

    @Basic
    @Column(name = "ADJUNTO")
    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    @Basic
    @Column(name = "ESTADO_PAGO")
    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    @Basic
    @Column(name = "FACTURA")
    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    @Basic
    @Column(name = "PERIODO")
    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    @Basic
    @Column(name = "TIPO_PAGO")
    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    @Transient
    public BigDecimal getCantidadPagos() {
        return cantidadPagos;
    }

    @Transient
    public void setCantidadPagos(BigDecimal cantidadPagos) {
        this.cantidadPagos = cantidadPagos;
    }

    @Transient
    public BigDecimal getIdProveedor() {
        return idProveedor;
    }

    @Transient
    public void setIdProveedor(BigDecimal idProveedor) {
        this.idProveedor = idProveedor;
    }

    @Transient
    public BigDecimal getMedidorPagado() {
        return medidorPagado;
    }

    @Transient
    public void setMedidorPagado(BigDecimal medidorPagado) {
        this.medidorPagado = medidorPagado;
    }

    @Transient
    public BigDecimal getMedidorNoPagado() {
        return medidorNoPagado;
    }

    @Transient
    public void setMedidorNoPagado(BigDecimal medidorNoPagado) {
        this.medidorNoPagado = medidorNoPagado;
    }

    @Transient
    public String getNombreProveedor() {
        return nombreProveedor;
    }

    @Transient
    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    @Transient
    public String getObs() {
        return obs;
    }

    @Transient
    public void setObs(String obs) {
        this.obs = obs;
    }

    @Transient
    public String getServicio() {
        return servicio;
    }

    @Transient
    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    @Transient
    public BigDecimal getCantidadMedidor() {
        return cantidadMedidor;
    }

    @Transient
    public void setCantidadMedidor(BigDecimal cantidadMedidor) {
        this.cantidadMedidor = cantidadMedidor;
    }

    @Transient
    public BigDecimal getMes() {
        return mes;
    }

    @Transient
    public void setMes(BigDecimal mes) {
        this.mes = mes;
    }

    @Transient
    public BigDecimal getMontoXMes() {
        return montoXMes;
    }

    @Transient
    public void setMontoXMes(BigDecimal montoXMes) {
        this.montoXMes = montoXMes;
    }

    @Basic
    @Column(name = "USUARIO")
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @ManyToOne
    @JoinColumn(name = "ID_H_PAGO_M", referencedColumnName = "ID")
    public HistorialPagosMasivosEntity gethPagosMasivosByIdHPagoM() {
        return hPagosMasivosByIdHPagoM;
    }

    public void sethPagosMasivosByIdHPagoM(HistorialPagosMasivosEntity hPagosMasivosByIdHPagoM) {
        this.hPagosMasivosByIdHPagoM = hPagosMasivosByIdHPagoM;
    }

}
