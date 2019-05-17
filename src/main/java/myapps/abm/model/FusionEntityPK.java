package myapps.abm.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class FusionEntityPK implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idProveedorFusion;
    private long idProveedor;

    @Column(name = "ID_PROVEEDOR_FUSION")
    @Id
    public long getIdProveedorFusion() {
        return idProveedorFusion;
    }

    public void setIdProveedorFusion(long idProveedorFusion) {
        this.idProveedorFusion = idProveedorFusion;
    }

    @Column(name = "ID_PROVEEDOR")
    @Id
    public long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(long idProveedor) {
        this.idProveedor = idProveedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FusionEntityPK that = (FusionEntityPK) o;
        return idProveedorFusion == that.idProveedorFusion &&
                idProveedor == that.idProveedor;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idProveedorFusion, idProveedor);
    }
}
