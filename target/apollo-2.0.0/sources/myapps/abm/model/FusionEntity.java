package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "FUSION", schema = "SERVICIOS_BASICOS", catalog = "")
@IdClass(FusionEntityPK.class)
public class FusionEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long idProveedorFusion;
    private long idProveedor;
    private Long estado;

    @Id
    @Column(name = "ID_PROVEEDOR_FUSION")
    public long getIdProveedorFusion() {
        return idProveedorFusion;
    }

    public void setIdProveedorFusion(long idProveedorFusion) {
        this.idProveedorFusion = idProveedorFusion;
    }

    @Id
    @Column(name = "ID_PROVEEDOR")
    public long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(long idProveedor) {
        this.idProveedor = idProveedor;
    }

    @Basic
    @Column(name = "ESTADO")
    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FusionEntity that = (FusionEntity) o;
        return idProveedorFusion == that.idProveedorFusion &&
                idProveedor == that.idProveedor &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idProveedorFusion, idProveedor, estado);
    }
}
