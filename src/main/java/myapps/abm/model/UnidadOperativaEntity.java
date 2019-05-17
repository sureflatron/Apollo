package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity

@Table(name = "UNIDAD_OPERATIVA")
public class UnidadOperativaEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idUnidadOperativa;
    private String nombre;
    private String descripcion;
    private boolean estado;
    private Collection<ServicioEntity> serviciosByIdUnidadOperativa;

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Id
    @Column(name = "ID_UNIDAD_OPERATIVA")
    @SequenceGenerator(name="SEQ_UNIDAD_OPERATIVA_ID_GENERATOR", sequenceName="SEQ_UNIDAD_OPERATIVA", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_UNIDAD_OPERATIVA_ID_GENERATOR")


    public long getIdUnidadOperativa() {
        return idUnidadOperativa;
    }

    public void setIdUnidadOperativa(long idUnidadOperativa) {
        this.idUnidadOperativa = idUnidadOperativa;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = true, length = 50)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "DESCRIPCION", nullable = true, length = 50)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnidadOperativaEntity that = (UnidadOperativaEntity) o;
        return idUnidadOperativa == that.idUnidadOperativa &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idUnidadOperativa, nombre, descripcion);
    }

    @OneToMany(mappedBy = "unidadOperativaByIdUnidadOperativa")
    public Collection<ServicioEntity> getServiciosByIdUnidadOperativa() {
        return serviciosByIdUnidadOperativa;
    }

    public void setServiciosByIdUnidadOperativa(Collection<ServicioEntity> serviciosByIdUnidadOperativa) {
        this.serviciosByIdUnidadOperativa = serviciosByIdUnidadOperativa;
    }
}
