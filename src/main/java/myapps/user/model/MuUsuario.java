package myapps.user.model;

import myapps.abm.model.HistorialUsuarioEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "MU_USUARIO", schema = "SERVICIOS_BASICOS", catalog = "")
public class MuUsuario {
    private long usuarioId;
    private boolean estado;
    private String login;
    private String nombre;
    private Date fechaCreacion;
    private Long ehumano;
    private Collection<HistorialUsuarioEntity> historialUsuariosByUsuarioId;
    private MuRol rolByRolId;

    @Id
    @SequenceGenerator(name = "SEQ_MU_USUARIO_ID_GENERATOR", sequenceName = "SEQ_MU_USUARIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MU_USUARIO_ID_GENERATOR")

    @Column(name = "USUARIO_ID")
    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Basic
    @Column(name = "ESTADO")
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Basic
    @Column(name = "LOGIN")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
    @Column(name = "FECHA_CREACION")
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Basic
    @Column(name = "EHUMANO")
    public Long getEhumano() {
        return ehumano;
    }

    public void setEhumano(Long ehumano) {
        this.ehumano = ehumano;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MuUsuario that = (MuUsuario) o;
        return usuarioId == that.usuarioId &&
                Objects.equals(estado, that.estado) &&
                Objects.equals(login, that.login) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(fechaCreacion, that.fechaCreacion) &&
                Objects.equals(ehumano, that.ehumano);
    }

    @Override
    public int hashCode() {

        return Objects.hash(usuarioId, estado, login, nombre, fechaCreacion, ehumano);
    }

    @OneToMany(mappedBy = "muUsuarioByUsuarioId")
    public Collection<HistorialUsuarioEntity> getHistorialUsuariosByUsuarioId() {
        return historialUsuariosByUsuarioId;
    }

    public void setHistorialUsuariosByUsuarioId(Collection<HistorialUsuarioEntity> historialUsuariosByUsuarioId) {
        this.historialUsuariosByUsuarioId = historialUsuariosByUsuarioId;
    }

    @ManyToOne
    @JoinColumn(name = "ROL_ID", referencedColumnName = "ROL_ID")
    public MuRol getRolByRolId() {
        return rolByRolId;
    }

    public void setRolByRolId(MuRol rolByRolId) {
        this.rolByRolId = rolByRolId;
    }
}
