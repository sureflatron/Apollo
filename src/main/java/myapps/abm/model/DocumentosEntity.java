package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "DOCUMENTOS", schema = "SERVICIOS_BASICOS", catalog = "")
public class DocumentosEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private long idDocumento;
    private long idPertenece;
    private String pertenece;
    private String ubicacion;
    private boolean estado;
    private byte[] docAdjunto;
    private String nombre;
    private String extension;
    private String prefijo;
    private boolean eliminar;

    @Id
    @Column(name = "ID_DOCUMENTO")
    @SequenceGenerator(name = "SEQ_DOCUMENTOS_GENERATOR", sequenceName = "SEQ_DOCUMENTOS",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DOCUMENTOS_GENERATOR")
    public long getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(long idDocumento) {
        this.idDocumento = idDocumento;
    }

    @Basic
    @Column(name = "ID_PERTENECE")
    public long getIdPertenece() {
        return idPertenece;
    }

    public void setIdPertenece(long idPertenece) {
        this.idPertenece = idPertenece;
    }

    @Basic
    @Column(name = "PERTENECE")
    public String getPertenece() {
        return pertenece;
    }

    public void setPertenece(String pertenece) {
        this.pertenece = pertenece;
    }

    @Basic
    @Column(name = "UBICACION")
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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
        DocumentosEntity that = (DocumentosEntity) o;
        return idDocumento == that.idDocumento &&
                idPertenece == that.idPertenece &&
                estado == that.estado &&
                Objects.equals(pertenece, that.pertenece) &&
                Objects.equals(ubicacion, that.ubicacion) &&
                Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDocumento, idPertenece, pertenece, ubicacion, estado, nombre);
    }

    @Transient
    public byte[] getDocAdjunto() {
        return docAdjunto;
    }

    @Transient
    public void setDocAdjunto(byte[] docAdjunto) {
        this.docAdjunto = docAdjunto;
    }

    @Transient
    public String getExtension() {
        return extension;
    }

    @Transient
    public void setExtension(String extension) {
        this.extension = extension;
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
    @Column(name = "PREFIJO")
    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }
    @Transient
    public boolean getEliminar() {
        return eliminar;
    }
    @Transient
    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }
}
