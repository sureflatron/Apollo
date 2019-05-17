package myapps.abm.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "HISTORIAL_CONTRATOS", schema = "SERVICIOS_BASICOS", catalog = "")
public class HistorialContratosEntity {
    private long idHistorial;
    private Date fecha;
    private String responsable;
    private String descripcion;
    private String tipo;
    private ContratosEntity contratosByContratoId;
    private ContratosRenovadosEntity contratosRenovadosByContratoRevoId;
    private Date fechaCreacion;
    private Date fechaExpiracion;

    @Id
    @Column(name = "ID_HISTORIAL")
    @SequenceGenerator(name = "SEQ_HISTORIAL_CONTRATOS_GENERATOR", sequenceName =
            "SEQ_HISTORIAL_CONTRATOS",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HISTORIAL_CONTRATOS_GENERATOR")
    public long getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(long idHistorial) {
        this.idHistorial = idHistorial;
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
    @Column(name = "RESPONSABLE")
    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    @Basic
    @Column(name = "DESCRIPCION")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "TIPO")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistorialContratosEntity that = (HistorialContratosEntity) o;
        return idHistorial == that.idHistorial &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(responsable, that.responsable) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idHistorial, fecha, responsable, descripcion, tipo);
    }

    @ManyToOne
    @JoinColumn(name = "CONTRATO_ID", referencedColumnName = "ID_CONTRATO", nullable = false)
    public ContratosEntity getContratosByContratoId() {
        return contratosByContratoId;
    }

    public void setContratosByContratoId(ContratosEntity contratosByContratoId) {
        this.contratosByContratoId = contratosByContratoId;
    }

    @ManyToOne
    @JoinColumn(name = "CONTRATO_REVO_ID", referencedColumnName = "ID_CONTRATO", nullable = false)
    public ContratosRenovadosEntity getContratosRenovadosByContratoRevoId() {
        return contratosRenovadosByContratoRevoId;
    }

    public void setContratosRenovadosByContratoRevoId(ContratosRenovadosEntity contratosRenovadosByContratoRevoId) {
        this.contratosRenovadosByContratoRevoId = contratosRenovadosByContratoRevoId;
    }

    @Basic
    @Column(name = "FECHA_CREACION")
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Basic
    @Column(name = "FECHA_EXPIRACION")
    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
