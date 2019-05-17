package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "CONSTRUCTOR")
public class ConstructorEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idConstructor;
    private String nombre;
    private String descripcion;
    private boolean estado;
    private Collection<RadioBaseEntity> radioBasesByIdConstructor;

    @Id
    @Column(name = "ID_CONSTRUCTOR")
    @SequenceGenerator(name="SEQ_CONSTRUCTOR_ID_GENERATOR", sequenceName="SEQ_CONSTRUCTOR", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CONSTRUCTOR_ID_GENERATOR")
    public long getIdConstructor() {
        return idConstructor;
    }

    public void setIdConstructor(long idConstructor) {
        this.idConstructor = idConstructor;
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
    @Column(name = "ESTADO", nullable = true, precision = 2)
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
        ConstructorEntity that = (ConstructorEntity) o;
        return idConstructor == that.idConstructor &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idConstructor, nombre, descripcion, estado);
    }



    @OneToMany(mappedBy = "constructorByIdConstructor")
    public Collection<RadioBaseEntity> getRadioBasesByIdConstructor() {
        return radioBasesByIdConstructor;
    }

    public void setRadioBasesByIdConstructor(Collection<RadioBaseEntity> radioBasesByIdConstructor) {
        this.radioBasesByIdConstructor = radioBasesByIdConstructor;
    }
}
