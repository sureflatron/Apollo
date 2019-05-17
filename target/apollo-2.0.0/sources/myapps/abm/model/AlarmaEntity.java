package myapps.abm.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ALARMA", schema = "SERVICIOS_BASICOS", catalog = "")
public class AlarmaEntity {
    private long idAlarma;
    private String nombre;
    private String para;
    private String cc;
    private String co;
    private String asunto;
    private String cuerpo;
    private String frecuencia;
    private boolean adjunto;
    private boolean estado;
    private List<String[]> listadestinos;
    private List<String[]> listaCC;
    private List<String[]> listaCO;
    private String tipoAlarma;
    private Collection<DetalleAlarmaEntity> detalleAlarmasByIdAlarma;
    private String tipo;
    private Collection<EnvioEntity> enviosByIdAlarma;
    private Date fechaCreacion;
    private String frecuenciaNotificacion;

    @Id
    @Column(name = "ID_ALARMA")
    @SequenceGenerator(name = "SEQ_ALARMA_ID_GENERATOR", sequenceName = "SEQ_ALARMA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALARMA_ID_GENERATOR")
    public long getIdAlarma() {
        return idAlarma;
    }

    public void setIdAlarma(long idAlarma) {
        this.idAlarma = idAlarma;
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
    @Column(name = "PARA")
    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    @Basic
    @Column(name = "CC")
    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    @Basic
    @Column(name = "CO")
    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    @Basic
    @Column(name = "ASUNTO")
    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    @Basic
    @Column(name = "CUERPO")
    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    @Basic
    @Column(name = "FRECUENCIA")
    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    @Basic
    @Column(name = "ADJUNTO")
    public boolean getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(boolean adjunto) {
        this.adjunto = adjunto;
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
        AlarmaEntity that = (AlarmaEntity) o;
        return idAlarma == that.idAlarma &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(para, that.para) &&
                Objects.equals(cc, that.cc) &&
                Objects.equals(co, that.co) &&
                Objects.equals(asunto, that.asunto) &&
                Objects.equals(cuerpo, that.cuerpo) &&
                Objects.equals(frecuencia, that.frecuencia) &&
                Objects.equals(adjunto, that.adjunto) &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idAlarma, nombre, para, cc, co, asunto, cuerpo, frecuencia, adjunto, estado);
    }

    @Transient
    public List<String[]> getListadestinos() {
        return listadestinos;
    }

    public void setListadestinos(List<String[]> listadestinos) {
        this.listadestinos = listadestinos;
    }

    @Transient
    public List<String[]> getListaCC() {
        return listaCC;
    }

    public void setListaCC(List<String[]> listaCC) {
        this.listaCC = listaCC;
    }

    @Transient
    public List<String[]> getListaCO() {
        return listaCO;
    }

    public void setListaCO(List<String[]> listaCO) {
        this.listaCO = listaCO;
    }

    @Basic
    @Column(name = "TIPO_ALARMA")
    public String getTipoAlarma() {
        return tipoAlarma;
    }

    public void setTipoAlarma(String tipoAlarma) {
        this.tipoAlarma = tipoAlarma;
    }

    @OneToMany(mappedBy = "alarmaByIdAlarma")
    public Collection<DetalleAlarmaEntity> getDetalleAlarmasByIdAlarma() {
        return detalleAlarmasByIdAlarma;
    }

    public void setDetalleAlarmasByIdAlarma(Collection<DetalleAlarmaEntity> detalleAlarmasByIdAlarma) {
        this.detalleAlarmasByIdAlarma = detalleAlarmasByIdAlarma;
    }

    @Basic
    @Column(name = "TIPO")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @OneToMany(mappedBy = "alarmaByIdAlarma", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Collection<EnvioEntity> getEnviosByIdAlarma() {
        return enviosByIdAlarma;
    }

    public void setEnviosByIdAlarma(Collection<EnvioEntity> enviosByIdAlarma) {
        this.enviosByIdAlarma = enviosByIdAlarma;
    }

    @Basic
    @Column(name = "FECHA_CREACION")
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Transient
    public String getFrecuenciaNotificacion() {
        return frecuenciaNotificacion;
    }

    public void setFrecuenciaNotificacion(String frecuenciaNotificacion) {
        this.frecuenciaNotificacion = frecuenciaNotificacion;
    }
}
