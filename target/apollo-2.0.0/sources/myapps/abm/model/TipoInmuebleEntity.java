package myapps.abm.model;


import org.primefaces.model.StreamedContent;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;


@Entity
@Table(name = "TIPO_INMUEBLE", schema = "SERVICIOS_BASICOS", catalog = "")
public class TipoInmuebleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idTipoInmueble;
    private String nombre;
    private String descripcion;
    private boolean estado;
    private Collection<InmuebleEntity> inmueblesByIdTipoInmueble;
    private byte[] imagen;
    private transient StreamedContent img;

    @Id
    @Column(name = "ID_TIPO_INMUEBLE")
    @SequenceGenerator(name = "SEQ_TIPO_INMUEBLE_ID_GENERATOR", sequenceName = "SEQ_TIPO_INMUEBLE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_INMUEBLE_ID_GENERATOR")


    public long getIdTipoInmueble() {
        return idTipoInmueble;
    }

    public void setIdTipoInmueble(long idTipoInmueble) {
        this.idTipoInmueble = idTipoInmueble;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = true, length = 50)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "DESCRIPCION", nullable = true, length = 50)
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
        TipoInmuebleEntity that = (TipoInmuebleEntity) o;
        return idTipoInmueble == that.idTipoInmueble &&
                estado == that.estado &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idTipoInmueble, nombre, descripcion, estado);
    }

    @OneToMany(mappedBy = "tipoInmuebleByIdTipoInmueble")
    public Collection<InmuebleEntity> getInmueblesByIdTipoInmueble() {
        return inmueblesByIdTipoInmueble;
    }

    public void setInmueblesByIdTipoInmueble(Collection<InmuebleEntity> inmueblesByIdTipoInmueble) {
        this.inmueblesByIdTipoInmueble = inmueblesByIdTipoInmueble;
    }


    @Lob
    @Column(name = "IMAGEN")
    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    @Transient
    public StreamedContent getImg() {
        return img;
    }

    public void setImg(StreamedContent img) {
        this.img = img;
    }
}
