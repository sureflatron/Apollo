package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "DETALLE_MEDIDOR", schema = "SERVICIOS_BASICOS", catalog = "")
public class DetalleMedidorEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idDetalleMedidor;
    private long idMedidor;
    private Date mes;
    private Long lectura;
    private Long consumo;
    private Long importe;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private String observacion;
    private boolean estado;
    private MedidorEntity medidorByIdMedidor;

    @Id

    @Column(name = "ID_DETALLE_MEDIDOR")
    @SequenceGenerator(name="SEQ_DETALLE_MEDIDOR_ID_GENERATOR", sequenceName="SEQ_DETALLE_MEDIDOR", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_DETALLE_MEDIDOR_ID_GENERATOR")
    public long getIdDetalleMedidor() {
        return idDetalleMedidor;
    }

    public void setIdDetalleMedidor(long idDetalleMedidor) {
        this.idDetalleMedidor = idDetalleMedidor;
    }


    @Basic
    @Column(name = "MES")
    public Date getMes() {
        return mes;
    }

    public void setMes(Date mes) {
        this.mes = mes;
    }

    @Basic
    @Column(name = "LECTURA")
    public Long getLectura() {
        return lectura;
    }

    public void setLectura(Long lectura) {
        this.lectura = lectura;
    }

    @Basic
    @Column(name = "CONSUMO")
    public Long getConsumo() {
        return consumo;
    }

    public void setConsumo(Long consumo) {
        this.consumo = consumo;
    }

    @Basic
    @Column(name = "IMPORTE")
    public Long getImporte() {
        return importe;
    }

    public void setImporte(Long importe) {
        this.importe = importe;
    }

    @Basic
    @Column(name = "FECHA_EMISION")
    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    @Basic
    @Column(name = "FECHA_VENCIMIENTO")
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Basic
    @Column(name = "OBSERVACION")
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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
        DetalleMedidorEntity that = (DetalleMedidorEntity) o;
        return idDetalleMedidor == that.idDetalleMedidor &&
                idMedidor == that.idMedidor &&
                Objects.equals(mes, that.mes) &&
                Objects.equals(lectura, that.lectura) &&
                Objects.equals(consumo, that.consumo) &&
                Objects.equals(importe, that.importe) &&
                Objects.equals(fechaEmision, that.fechaEmision) &&
                Objects.equals(fechaVencimiento, that.fechaVencimiento) &&
                Objects.equals(observacion, that.observacion) &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDetalleMedidor, idMedidor, mes, lectura, consumo, importe, fechaEmision, fechaVencimiento, observacion, estado);
    }

    @ManyToOne
    @JoinColumn(name = "ID_MEDIDOR", referencedColumnName = "ID_MEDIDOR", nullable = false)
    public MedidorEntity getMedidorByIdMedidor() {
        return medidorByIdMedidor;
    }

    public void setMedidorByIdMedidor(MedidorEntity medidorByIdMedidor) {
        this.medidorByIdMedidor = medidorByIdMedidor;
    }
}
