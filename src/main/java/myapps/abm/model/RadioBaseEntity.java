package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "RADIO_BASE", schema = "SERVICIOS_BASICOS", catalog = "")
public class RadioBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idRadioBase;
    private String sitioid;
    private String nombreRbs;
    private String direccion;
    private String codInmueble;
    private String altura;
    private String codCve;
    private String costoInstall;
    private String area1;
    private String area2;
    private String dcattt;
    private String estadoInmueble;
    private String latitud;
    private String longitud;
    private String observaciones;
    private Long estado;
    private LocalidadEntity localidadByIdLocalidad;
    private PropiedadEntity propiedadByIdPropiedad;
    private TipoTorreEntity tipoTorreByIdTipoTorre;
    private ConstructorEntity constructorByIdConstructor;
    private String areax1;
    private String areax2;
    private String areay1;
    private String areay2;
    private Collection<RdbServEntity> rdbServsByIdRadioBase;
    private String obsErrors;
    private Long cInicial;
    private Collection<ContratosEntity> contratosByIdRadioBase;

    @Id
    @Column(name = "ID_RADIO_BASE")
    @SequenceGenerator(name="SEQ_RADIO_BASE_GENERATOR", sequenceName="SEQ_RADIO_BASE",
            allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RADIO_BASE_GENERATOR")
    public long getIdRadioBase() {
        return idRadioBase;
    }

    public void setIdRadioBase(long idRadioBase) {
        this.idRadioBase = idRadioBase;
    }

    @Basic
    @Column(name = "SITIOID")
    public String getSitioid() {
        return sitioid;
    }

    public void setSitioid(String sitioid) {
        this.sitioid = sitioid;
    }

    @Basic
    @Column(name = "NOMBRE_RBS")
    public String getNombreRbs() {
        return nombreRbs;
    }

    public void setNombreRbs(String nombreRbs) {
        this.nombreRbs = nombreRbs;
    }

    @Basic
    @Column(name = "DIRECCION")
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Basic
    @Column(name = "COD_INMUEBLE")
    public String getCodInmueble() {
        return codInmueble;
    }

    public void setCodInmueble(String codInmueble) {
        this.codInmueble = codInmueble;
    }

    @Basic
    @Column(name = "ALTURA")
    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    @Basic
    @Column(name = "COD_CVE")
    public String getCodCve() {
        return codCve;
    }

    public void setCodCve(String codCve) {
        this.codCve = codCve;
    }

    @Basic
    @Column(name = "COSTO_INSTALL")
    public String getCostoInstall() {
        return costoInstall;
    }

    public void setCostoInstall(String costoInstall) {
        this.costoInstall = costoInstall;
    }

    @Basic
    @Column(name = "DCATTT")
    public String getDcattt() {
        return dcattt;
    }

    public void setDcattt(String dcattt) {
        this.dcattt = dcattt;
    }

    @Basic
    @Column(name = "ESTADO_INMUEBLE")
    public String getEstadoInmueble() {
        return estadoInmueble;
    }

    public void setEstadoInmueble(String estadoInmueble) {
        this.estadoInmueble = estadoInmueble;
    }

    @Basic
    @Column(name = "LATITUD")
    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    @Basic
    @Column(name = "LONGITUD")
    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @Basic
    @Column(name = "OBSERVACIONES")
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Basic
    @Column(name = "ESTADO")
    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RadioBaseEntity that = (RadioBaseEntity) o;
        return idRadioBase == that.idRadioBase &&
                Objects.equals(sitioid, that.sitioid) &&
                Objects.equals(nombreRbs, that.nombreRbs) &&
                Objects.equals(direccion, that.direccion) &&
                Objects.equals(codInmueble, that.codInmueble) &&
                Objects.equals(altura, that.altura) &&
                Objects.equals(codCve, that.codCve) &&
                Objects.equals(costoInstall, that.costoInstall) &&
                Objects.equals(area1, that.area1) &&
                Objects.equals(area2, that.area2) &&
                Objects.equals(dcattt, that.dcattt) &&
                Objects.equals(estadoInmueble, that.estadoInmueble) &&
                Objects.equals(latitud, that.latitud) &&
                Objects.equals(longitud, that.longitud) &&
                Objects.equals(observaciones, that.observaciones) &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idRadioBase, sitioid, nombreRbs, direccion, codInmueble, altura, codCve, costoInstall, area1, area2, dcattt, estadoInmueble, latitud, longitud, observaciones, estado);
    }

    @ManyToOne
    @JoinColumn(name = "ID_LOCALIDAD", referencedColumnName = "ID_LOCALIDAD", nullable = false)
    public LocalidadEntity getLocalidadByIdLocalidad() {
        return localidadByIdLocalidad;
    }

    public void setLocalidadByIdLocalidad(LocalidadEntity localidadByIdLocalidad) {
        this.localidadByIdLocalidad = localidadByIdLocalidad;
    }

    @ManyToOne
    @JoinColumn(name = "ID_PROPIEDAD", referencedColumnName = "ID_PROPIEDAD", nullable = false)
    public PropiedadEntity getPropiedadByIdPropiedad() {
        return propiedadByIdPropiedad;
    }

    public void setPropiedadByIdPropiedad(PropiedadEntity propiedadByIdPropiedad) {
        this.propiedadByIdPropiedad = propiedadByIdPropiedad;
    }

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_TORRE", referencedColumnName = "ID_TIPO_TORRE", nullable = false)
    public TipoTorreEntity getTipoTorreByIdTipoTorre() {
        return tipoTorreByIdTipoTorre;
    }

    public void setTipoTorreByIdTipoTorre(TipoTorreEntity tipoTorreByIdTipoTorre) {
        this.tipoTorreByIdTipoTorre = tipoTorreByIdTipoTorre;
    }

    @ManyToOne
    @JoinColumn(name = "ID_CONSTRUCTOR", referencedColumnName = "ID_CONSTRUCTOR", nullable = false)
    public ConstructorEntity getConstructorByIdConstructor() {
        return constructorByIdConstructor;
    }

    public void setConstructorByIdConstructor(ConstructorEntity constructorByIdConstructor) {
        this.constructorByIdConstructor = constructorByIdConstructor;
    }

    @Basic
    @Column(name = "AREAX1")
    public String getAreax1() {
        return areax1;
    }

    public void setAreax1(String areax1) {
        this.areax1 = areax1;
    }

    @Basic
    @Column(name = "AREAX2")
    public String getAreax2() {
        return areax2;
    }

    public void setAreax2(String areax2) {
        this.areax2 = areax2;
    }

    @Basic
    @Column(name = "AREAY1")
    public String getAreay1() {
        return areay1;
    }

    public void setAreay1(String areay1) {
        this.areay1 = areay1;
    }

    @Basic
    @Column(name = "AREAY2")
    public String getAreay2() {
        return areay2;
    }

    public void setAreay2(String areay2) {
        this.areay2 = areay2;
    }

    @OneToMany(mappedBy = "radioBaseByIdRadioBase")
    public Collection<RdbServEntity> getRdbServsByIdRadioBase() {
        return rdbServsByIdRadioBase;
    }

    public void setRdbServsByIdRadioBase(Collection<RdbServEntity> rdbServsByIdRadioBase) {
        this.rdbServsByIdRadioBase = rdbServsByIdRadioBase;
    }

    @Transient
    public String getObsErrors() {
        return obsErrors;
    }

    @Transient
    public void setObsErrors(String obsErrors) {
        this.obsErrors = obsErrors;
    }

    @Basic
    @Column(name = "C_INICIAL")
    public Long getcInicial() {
        return cInicial;
    }

    public void setcInicial(Long cInicial) {
        this.cInicial = cInicial;
    }

    @OneToMany(mappedBy = "radioBaseByIdRadioBase")
    public Collection<ContratosEntity> getContratosByIdRadioBase() {
        return contratosByIdRadioBase;
    }

    public void setContratosByIdRadioBase(Collection<ContratosEntity> contratosByIdRadioBase) {
        this.contratosByIdRadioBase = contratosByIdRadioBase;
    }
}
