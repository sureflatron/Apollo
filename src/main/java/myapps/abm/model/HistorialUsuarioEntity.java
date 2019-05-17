package myapps.abm.model;



import myapps.user.model.MuUsuario;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "HISTORIAL_USUARIO", schema = "SERVICIOS_BASICOS", catalog = "")
public class HistorialUsuarioEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idHistorial;
    private Date fecha;
    private String responsable;
    private String descripcion;
    private String tipo;
    private MuUsuario muUsuarioByUsuarioId;


    @Id
    @Column(name = "ID_HISTORIAL")
    @SequenceGenerator(name = "SEQ_HISTORIAL_USUARIO_GENERATOR", sequenceName = "SEQ_HISTORIAL_USUARIO",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HISTORIAL_USUARIO_GENERATOR")

    public long getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(long idHistorial) {
        this.idHistorial = idHistorial;
    }


    @Basic
    @Column(name = "FECHA")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "RESPONSABLE")
    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
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
    @Column(name = "TIPO")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistorialUsuarioEntity that = (HistorialUsuarioEntity) o;
        return idHistorial == that.idHistorial &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(responsable, that.responsable) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idHistorial, fecha, responsable, descripcion, tipo);
    }

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "USUARIO_ID")
    public MuUsuario getMuUsuarioByUsuarioId() {
        return muUsuarioByUsuarioId;
    }

    public void setMuUsuarioByUsuarioId(MuUsuario muUsuarioByUsuarioId) {
        this.muUsuarioByUsuarioId = muUsuarioByUsuarioId;
    }

}
