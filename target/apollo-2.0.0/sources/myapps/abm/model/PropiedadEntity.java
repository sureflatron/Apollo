package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "PROPIEDAD")
public class PropiedadEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idPropiedad;
    private String nombre;
    private String descripcion;
    private boolean estado;
    private Collection<InmuebleEntity> inmueblesByIdPropiedad;
    private Collection<RadioBaseEntity> radioBasesByIdPropiedad;

    @Id
    @Column(name = "ID_PROPIEDAD")
    @SequenceGenerator(name="SEQ_PROPIEDAD_ID_GENERATOR", sequenceName="SEQ_PROPIEDAD", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PROPIEDAD_ID_GENERATOR")
    public long getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(long idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = false, length = 50)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        PropiedadEntity that = (PropiedadEntity) o;
        return idPropiedad == that.idPropiedad &&
                estado == that.estado &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idPropiedad, nombre, descripcion, estado);
    }

    @OneToMany(mappedBy = "propiedadByIdPropiedad")
    public Collection<InmuebleEntity> getInmueblesByIdPropiedad() {
        return inmueblesByIdPropiedad;
    }

    public void setInmueblesByIdPropiedad(Collection<InmuebleEntity> inmueblesByIdPropiedad) {
        this.inmueblesByIdPropiedad = inmueblesByIdPropiedad;
    }

    @OneToMany(mappedBy = "propiedadByIdPropiedad")
    public Collection<RadioBaseEntity> getRadioBasesByIdPropiedad() {
        return radioBasesByIdPropiedad;
    }

    public void setRadioBasesByIdPropiedad(Collection<RadioBaseEntity> radioBasesByIdPropiedad) {
        this.radioBasesByIdPropiedad = radioBasesByIdPropiedad;
    }
}
