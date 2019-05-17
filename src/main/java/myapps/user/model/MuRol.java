package myapps.user.model;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the MU_ROL database table.
 */
@Entity
@Table(name = "ROL")
public class MuRol implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ROL_ID_GENERATOR", sequenceName = "SEQ_ROL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROL_ID_GENERATOR")
    @Column(name = "ROL_ID")
    private long rolId;
    private String descripcion;
    private boolean estado;
    private String nombre;
    //private Collection<MuUsuario> muUsuariosByRolId;



    @Column(name = "VALIDA_IP")
    private boolean validaIp;

    public MuRol() {
    }

    public long getRolId() {
        return this.rolId;
    }

    public void setRolId(long rolId) {
        this.rolId = rolId;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEstado() {
        return this.estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getValidaIp() {
        return this.validaIp;
    }

    public void setValidaIp(boolean validaIp) {
        this.validaIp = validaIp;
    }

/*
    @OneToMany(mappedBy = "rolByRolId")
    public Collection<MuUsuario> getMuUsuariosByRolId() {
        return muUsuariosByRolId;
    }

    public void setMuUsuariosByRolId(Collection<MuUsuario> muUsuariosByRolId) {
        this.muUsuariosByRolId = muUsuariosByRolId;
    }
*/
}