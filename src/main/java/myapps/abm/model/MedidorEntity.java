package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "MEDIDOR", schema = "SERVICIOS_BASICOS", catalog = "")
public class MedidorEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idMedidor;
    private long idServicio;
    private String nombre;
    private String observacion;
    private ServicioEntity servicioByIdServicio;
    private boolean estado;
    private Collection<DetalleMedidorEntity> detalleMedidorsByIdMedidor;
    private Date fechaCreacion;

    @Id
    @Column(name = "ID_MEDIDOR")
    @SequenceGenerator(name="SEQ_MEDIDOR_ID_GENERATOR", sequenceName="SEQ_MEDIDOR", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_MEDIDOR_ID_GENERATOR")
    public long getIdMedidor() {
        return idMedidor;
    }

    public void setIdMedidor(long idMedidor) {
        this.idMedidor = idMedidor;
    }

    @Basic
    @Column(name = "NOMBRE")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "OBSERVACION")
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedidorEntity that = (MedidorEntity) o;
        return idMedidor == that.idMedidor &&
                idServicio == that.idServicio &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(observacion, that.observacion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idMedidor, idServicio, nombre, observacion);
    }

    @OneToOne
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO", nullable = false)
    public ServicioEntity getServicioByIdServicio() {
        return servicioByIdServicio;
    }

    public void setServicioByIdServicio(ServicioEntity servicioByIdServicio) {
        this.servicioByIdServicio = servicioByIdServicio;
    }

    @Basic
    @Column(name = "ESTADO")
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @OneToMany(mappedBy = "medidorByIdMedidor")
    public Collection<DetalleMedidorEntity> getDetalleMedidorsByIdMedidor() {
        return detalleMedidorsByIdMedidor;
    }

    public void setDetalleMedidorsByIdMedidor(Collection<DetalleMedidorEntity> detalleMedidorsByIdMedidor) {
        this.detalleMedidorsByIdMedidor = detalleMedidorsByIdMedidor;
    }

    @Basic
    @Column(name = "FECHA_CREACION")
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
