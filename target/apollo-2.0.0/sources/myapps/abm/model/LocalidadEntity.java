package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "LOCALIDAD")
public class LocalidadEntity  implements Serializable {
    private static final long serialVersionUID = 1L;

    private long idLocalidad;
    private String nombre;

    private boolean estado;
    private MunicipioEntity municipioByIdMunicipio;
    private Collection<InmuebleEntity> inmueblesByIdLocalidad;
    private Collection<RadioBaseEntity> radioBasesByIdLocalidad;

    @Id
    @Column(name = "ID_LOCALIDAD")
    @SequenceGenerator(name="SEQ_LOCALIDAD_ID_GENERATOR", sequenceName="SEQ_LOCALIDAD", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_LOCALIDAD_ID_GENERATOR")

    public long getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(long idLocalidad) {
        this.idLocalidad = idLocalidad;
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
        LocalidadEntity that = (LocalidadEntity) o;
        return idLocalidad == that.idLocalidad &&

                estado == that.estado &&
                Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idLocalidad, nombre, estado);
    }

    @ManyToOne
    @JoinColumn(name = "ID_MUNICIPIO", referencedColumnName = "ID_MUNICIPIO", nullable = false)
    public MunicipioEntity getMunicipioByIdMunicipio() {
        return municipioByIdMunicipio;
    }

    public void setMunicipioByIdMunicipio(MunicipioEntity municipioByIdMunicipio) {
        this.municipioByIdMunicipio = municipioByIdMunicipio;
    }

    @OneToMany(mappedBy = "localidadByIdLocalidad")
    public Collection<InmuebleEntity> getInmueblesByIdLocalidad() {
        return inmueblesByIdLocalidad;
    }

    public void setInmueblesByIdLocalidad(Collection<InmuebleEntity> inmueblesByIdLocalidad) {
        this.inmueblesByIdLocalidad = inmueblesByIdLocalidad;
    }

    @OneToMany(mappedBy = "localidadByIdLocalidad")
    public Collection<RadioBaseEntity> getRadioBasesByIdLocalidad() {
        return radioBasesByIdLocalidad;
    }

    public void setRadioBasesByIdLocalidad(Collection<RadioBaseEntity> radioBasesByIdLocalidad) {
        this.radioBasesByIdLocalidad = radioBasesByIdLocalidad;
    }
}
