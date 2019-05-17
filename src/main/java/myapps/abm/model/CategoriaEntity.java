package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "CATEGORIA", schema = "SERVICIOS_BASICOS", catalog = "")
public class CategoriaEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idCategoria;
    private String nombre;
    private String descripcion;
    private long estado;
    private Collection<ProveedorEntity> proveedorsByIdCategoria;

    @Id
    @Column(name = "ID_CATEGORIA")
    public long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(long idCategoria) {
        this.idCategoria = idCategoria;
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
    @Column(name = "DESCRIPCION")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "ESTADO")
    public long getEstado() {
        return estado;
    }

    public void setEstado(long estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaEntity that = (CategoriaEntity) o;
        return idCategoria == that.idCategoria &&
                estado == that.estado &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idCategoria, nombre, descripcion, estado);
    }

    @OneToMany(mappedBy = "categoriaByIdCategoria")
    public Collection<ProveedorEntity> getProveedorsByIdCategoria() {
        return proveedorsByIdCategoria;
    }

    public void setProveedorsByIdCategoria(Collection<ProveedorEntity> proveedorsByIdCategoria) {
        this.proveedorsByIdCategoria = proveedorsByIdCategoria;
    }
}
