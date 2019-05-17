package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "ORDEN_COMPRA", schema = "SERVICIOS_BASICOS", catalog = "")
public class OrdenCompraEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idOc;
    private String nroOrden;
    private Long monto;
    private Date fecha;
    private String estadoCompra;
    private boolean estado;
    private InmuebleEntity inmuebleByIdInmueble;

    @Id

    @Column(name = "ID_OC")
    @SequenceGenerator(name="SEQ_ORDEN_COMPRA_ID_GENERATOR", sequenceName="SEQ_ORDEN_COMPRA", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ORDEN_COMPRA_ID_GENERATOR")
    public long getIdOc() {
        return idOc;
    }

    public void setIdOc(long idOc) {
        this.idOc = idOc;
    }

    @Basic
    @Column(name = "NRO_ORDEN")
    public String getNroOrden() {
        return nroOrden;
    }

    public void setNroOrden(String nroOrden) {
        this.nroOrden = nroOrden;
    }

    @Basic
    @Column(name = "MONTO")
    public Long getMonto() {
        return monto;
    }

    public void setMonto(Long monto) {
        this.monto = monto;
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
    @Column(name = "ESTADO_COMPRA")
    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
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
        OrdenCompraEntity that = (OrdenCompraEntity) o;
        return idOc == that.idOc &&
                estado == that.estado &&
                Objects.equals(nroOrden, that.nroOrden) &&
                Objects.equals(monto, that.monto) &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(estadoCompra, that.estadoCompra);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idOc, nroOrden, monto, fecha, estadoCompra, estado);
    }

    @ManyToOne
    @JoinColumn(name = "ID_INMUEBLE", referencedColumnName = "ID_INMUEBLE")
    public InmuebleEntity getInmuebleByIdInmueble() {
        return inmuebleByIdInmueble;
    }

    public void setInmuebleByIdInmueble(InmuebleEntity inmuebleByIdInmueble) {
        this.inmuebleByIdInmueble = inmuebleByIdInmueble;
    }
}
