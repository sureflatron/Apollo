package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "CONFIGURACIONES", schema = "SERVICIOS_BASICOS", catalog = "")
public class ConfiguracionesEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idConfiguraciones;
    private String nombre;
    private String tipo;
    private boolean estado;
    private Collection<CamposEntity> camposByIdConfiguraciones;
    private Date fechaCreacion;
    private Collection<HistorialPagosMasivosEntity> historialPagosMasivosByIdConfiguraciones;

    @Id
    @Column(name = "ID_CONFIGURACIONES")
    @SequenceGenerator(name="SEQ_CONFIGURACIONES_ID_GENERATOR", sequenceName="SEQ_CONFIGURACIONES", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CONFIGURACIONES_ID_GENERATOR")
    public long getIdConfiguraciones() {
        return idConfiguraciones;
    }

    public void setIdConfiguraciones(long idConfiguraciones) {
        this.idConfiguraciones = idConfiguraciones;
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
    @Column(name = "TIPO")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        ConfiguracionesEntity that = (ConfiguracionesEntity) o;
        return idConfiguraciones == that.idConfiguraciones &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(tipo, that.tipo) &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idConfiguraciones, nombre, tipo, estado);
    }

    @OneToMany(mappedBy = "configuracionesByIdConfiguraciones")
    public Collection<CamposEntity> getCamposByIdConfiguraciones() {
        return camposByIdConfiguraciones;
    }

    public void setCamposByIdConfiguraciones(Collection<CamposEntity> camposByIdConfiguraciones) {
        this.camposByIdConfiguraciones = camposByIdConfiguraciones;
    }

    @Basic
    @Column(name = "FECHA_CREACION")
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    @OneToMany(mappedBy = "configuracionesByConfiguracionId")
    public Collection<HistorialPagosMasivosEntity> getHistorialPagosMasivosByIdConfiguraciones() {
        return historialPagosMasivosByIdConfiguraciones;
    }

    public void setHistorialPagosMasivosByIdConfiguraciones(Collection<HistorialPagosMasivosEntity> historialPagosMasivosByIdConfiguraciones) {
        this.historialPagosMasivosByIdConfiguraciones = historialPagosMasivosByIdConfiguraciones;
    }
}
