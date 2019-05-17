package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "TIPO_TORRE", schema = "SERVICIOS_BASICOS", catalog = "")
public class TipoTorreEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idTipoTorre;
    private String torre;
    private boolean estado;
    private String descripcion;    
    private Collection<RadioBaseEntity> radioBasesByIdTipoTorre;
    private byte[] imagen;

    @Id
    @Column(name = "ID_TIPO_TORRE")
    @SequenceGenerator(name="SEQ_TIPO_TORRE_ID_GENERATOR", sequenceName="SEQ_TIPO_TORRE", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TIPO_TORRE_ID_GENERATOR")

    public long getIdTipoTorre() {
        return idTipoTorre;
    }

    public void setIdTipoTorre(long idTipoTorre) {
        this.idTipoTorre = idTipoTorre;
    }

    @Basic
    @Column(name = "TORRE", nullable = true, length = 50)
    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    @Basic
    @Column(name = "ESTADO", nullable = true, precision = 0)
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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
        TipoTorreEntity that = (TipoTorreEntity) o;
        return idTipoTorre == that.idTipoTorre &&
                Objects.equals(torre, that.torre) &&
                Objects.equals(estado, that.estado) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idTipoTorre, torre, estado, descripcion);
    }

    @OneToMany(mappedBy = "tipoTorreByIdTipoTorre")
    public Collection<RadioBaseEntity> getRadioBasesByIdTipoTorre() {
        return radioBasesByIdTipoTorre;
    }

    public void setRadioBasesByIdTipoTorre(Collection<RadioBaseEntity> radioBasesByIdTipoTorre) {
        this.radioBasesByIdTipoTorre = radioBasesByIdTipoTorre;
    }

    @Basic
    @Column(name = "IMAGEN")
    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
