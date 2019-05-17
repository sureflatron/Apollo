package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "DEPARTAMENTO", schema = "SERVICIOS_BASICOS", catalog = "")
public class DepartamentoEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private long idDepartamento;
    private String nombre;
    private Collection<LicAmbEntity> licAmbsByIdDepartamento;
    private Collection<ProvinciaEntity> provinciasByIdDepartamento;
    private Collection<ProveedorEntity> proveedorsByIdDepartamento;
    private Collection<BancoEntity> bancosByIdDepartamento;
    private Collection<ContratosEntity> contratosByIdDepartamento;

    @Id
    @Column(name = "ID_DEPARTAMENTO")
    public long getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(long idDepartamento) {
        this.idDepartamento = idDepartamento;
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
        DepartamentoEntity that = (DepartamentoEntity) o;
        return idDepartamento == that.idDepartamento &&
                Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDepartamento, nombre);
    }

    @OneToMany(mappedBy = "departamentoByIdDepartamento")
    public Collection<LicAmbEntity> getLicAmbsByIdDepartamento() {
        return licAmbsByIdDepartamento;
    }

    public void setLicAmbsByIdDepartamento(Collection<LicAmbEntity> licAmbsByIdDepartamento) {
        this.licAmbsByIdDepartamento = licAmbsByIdDepartamento;
    }

    @OneToMany(mappedBy = "departamentoByIdDepartamento")
    public Collection<ProvinciaEntity> getProvinciasByIdDepartamento() {
        return provinciasByIdDepartamento;
    }

    public void setProvinciasByIdDepartamento(Collection<ProvinciaEntity> provinciasByIdDepartamento) {
        this.provinciasByIdDepartamento = provinciasByIdDepartamento;
    }

    @OneToMany(mappedBy = "departamentoByIdDepartamento")
    public Collection<ProveedorEntity> getProveedorsByIdDepartamento() {
        return proveedorsByIdDepartamento;
    }

    public void setProveedorsByIdDepartamento(Collection<ProveedorEntity> proveedorsByIdDepartamento) {
        this.proveedorsByIdDepartamento = proveedorsByIdDepartamento;
    }

    @OneToMany(mappedBy = "departamentoByIdDepartamento")
    public Collection<BancoEntity> getBancosByIdDepartamento() {
        return bancosByIdDepartamento;
    }

    public void setBancosByIdDepartamento(Collection<BancoEntity> bancosByIdDepartamento) {
        this.bancosByIdDepartamento = bancosByIdDepartamento;
    }

    @OneToMany(mappedBy = "departamentoByIdDepartamento")
    public Collection<ContratosEntity> getContratosByIdDepartamento() {
        return contratosByIdDepartamento;
    }

    public void setContratosByIdDepartamento(Collection<ContratosEntity> contratosByIdDepartamento) {
        this.contratosByIdDepartamento = contratosByIdDepartamento;
    }
}
