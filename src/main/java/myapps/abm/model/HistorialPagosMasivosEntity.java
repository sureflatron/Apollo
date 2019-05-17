package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "HISTORIAL_PAGOS_MASIVOS")
public class HistorialPagosMasivosEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String nombreArchivo;
    private String ext;
    private Long procesados;
    private Long noprocesados;
    private Timestamp fechaIni;
    private Timestamp fechaFin;
    private String usuario;
    private Collection<PagoServicioEntity> pagoServiciosById;
    private ConfiguracionesEntity configuracionesByConfiguracionId;

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_H_PAGO_MAS_GENERATOR", sequenceName = "SEQ_H_PAGO_MAS",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_H_PAGO_MAS_GENERATOR")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NOMBRE_ARCHIVO")
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    @Basic
    @Column(name = "EXT")
    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    @Basic
    @Column(name = "PROCESADOS")
    public Long getProcesados() {
        return procesados;
    }

    public void setProcesados(Long procesados) {
        this.procesados = procesados;
    }

    @Basic
    @Column(name = "NOPROCESADOS")
    public Long getNoprocesados() {
        return noprocesados;
    }

    public void setNoprocesados(Long noprocesados) {
        this.noprocesados = noprocesados;
    }

    @Basic
    @Column(name = "FECHA_INI")
    public Timestamp getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Timestamp fechaIni) {
        this.fechaIni = fechaIni;
    }

    @Basic
    @Column(name = "FECHA_FIN")
    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistorialPagosMasivosEntity that = (HistorialPagosMasivosEntity) o;
        return id == that.id &&
                Objects.equals(nombreArchivo, that.nombreArchivo) &&
                Objects.equals(ext, that.ext) &&
                Objects.equals(procesados, that.procesados) &&
                Objects.equals(noprocesados, that.noprocesados) &&
                Objects.equals(fechaIni, that.fechaIni) &&
                Objects.equals(fechaFin, that.fechaFin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nombreArchivo, ext, procesados, noprocesados, fechaIni, fechaFin);
    }

    @Transient
    public String getUsuario() {
        return usuario;
    }

    @Transient
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @OneToMany(mappedBy = "hPagosMasivosByIdHPagoM")
    public Collection<PagoServicioEntity> getPagoServiciosById() {
        return pagoServiciosById;
    }

    public void setPagoServiciosById(Collection<PagoServicioEntity> pagoServiciosById) {
        this.pagoServiciosById = pagoServiciosById;
    }

    @ManyToOne
    @JoinColumn(name = "CONFIGURACION_ID", referencedColumnName = "ID_CONFIGURACIONES", nullable = false)
    public ConfiguracionesEntity getConfiguracionesByConfiguracionId() {
        return configuracionesByConfiguracionId;
    }

    public void setConfiguracionesByConfiguracionId(ConfiguracionesEntity configuracionesByConfiguracionId) {
        this.configuracionesByConfiguracionId = configuracionesByConfiguracionId;
    }
}
