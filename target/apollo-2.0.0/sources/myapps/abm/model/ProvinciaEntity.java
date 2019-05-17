package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "PROVINCIA", schema = "SERVICIOS_BASICOS", catalog = "")
public class ProvinciaEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private Collection<MunicipioEntity> municipiosByIdeProvincia;
    private long ideProvincia;
    private String nombre;

    private DepartamentoEntity departamentoByIdDepartamento;

    @OneToMany(mappedBy = "provinciaByIdProvincia")
    public Collection<MunicipioEntity> getMunicipiosByIdeProvincia() {
        return municipiosByIdeProvincia;
    }

    public void setMunicipiosByIdeProvincia(Collection<MunicipioEntity> municipiosByIdeProvincia) {
        this.municipiosByIdeProvincia = municipiosByIdeProvincia;
    }

    @Id
    @Column(name = "IDE_PROVINCIA")
    public long getIdeProvincia() {
        return ideProvincia;
    }

    public void setIdeProvincia(long ideProvincia) {
        this.ideProvincia = ideProvincia;
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
        ProvinciaEntity that = (ProvinciaEntity) o;
        return ideProvincia == that.ideProvincia &&

                Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ideProvincia, nombre);
    }

    @ManyToOne
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO", nullable = false)
    public DepartamentoEntity getDepartamentoByIdDepartamento() {
        return departamentoByIdDepartamento;
    }

    public void setDepartamentoByIdDepartamento(DepartamentoEntity departamentoByIdDepartamento) {
        this.departamentoByIdDepartamento = departamentoByIdDepartamento;
    }
}
