package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "TIPO_SERVICIO")
public class TipoServicioEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private long idTipoServicio;
    private String servicio;
    private String descripcion;
    private boolean estado;
    private Collection<ServicioEntity> serviciosByIdTipoServicio;

    @OneToMany(mappedBy = "tipoServicioByIdTipoServicio")
    public Collection<ServicioEntity> getServiciosByIdTipoServicio() {
        return serviciosByIdTipoServicio;
    }

    public void setServiciosByIdTipoServicio(Collection<ServicioEntity> serviciosByIdTipoServicio) {
        this.serviciosByIdTipoServicio = serviciosByIdTipoServicio;
    }

    @Id
    @Column(name = "ID_TIPO_SERVICIO")
    @SequenceGenerator(name="SEQ_TIPO_SERVICIO_ID_GENERATOR", sequenceName="SEQ_TIPO_SERVICIO", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TIPO_SERVICIO_ID_GENERATOR")


    public long getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(long idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    @Basic
    @Column(name = "SERVICIO", nullable = true, length = 50)
    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    @Basic
    @Column(name = "DESCRIPCION", nullable = true, length = 100)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "ESTADO", nullable = false, precision = 2)
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
        TipoServicioEntity that = (TipoServicioEntity) o;
        return idTipoServicio == that.idTipoServicio &&
                estado == that.estado &&
                Objects.equals(servicio, that.servicio) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idTipoServicio, servicio, descripcion, estado);
    }


}
