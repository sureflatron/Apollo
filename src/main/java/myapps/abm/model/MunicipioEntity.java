package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "MUNICIPIO")
public class MunicipioEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private Collection<LocalidadEntity> localidadsByIdMunicipio;
    private long idMunicipio;
    private String nombre;

    private ProvinciaEntity provinciaByIdProvincia;

    @OneToMany(mappedBy = "municipioByIdMunicipio")
    public Collection<LocalidadEntity> getLocalidadsByIdMunicipio() {
        return localidadsByIdMunicipio;
    }

    public void setLocalidadsByIdMunicipio(Collection<LocalidadEntity> localidadsByIdMunicipio) {
        this.localidadsByIdMunicipio = localidadsByIdMunicipio;
    }

    @Id
    @Column(name = "ID_MUNICIPIO")
    public long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    @Basic
    @Column(name = "NOMBRE")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MunicipioEntity that = (MunicipioEntity) o;
        return idMunicipio == that.idMunicipio &&

                Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idMunicipio, nombre);
    }

    @ManyToOne
    @JoinColumn(name = "ID_PROVINCIA", referencedColumnName = "IDE_PROVINCIA", nullable = false)
    public ProvinciaEntity getProvinciaByIdProvincia() {
        return provinciaByIdProvincia;
    }

    public void setProvinciaByIdProvincia(ProvinciaEntity provinciaByIdProvincia) {
        this.provinciaByIdProvincia = provinciaByIdProvincia;
    }
}
