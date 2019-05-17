package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "INMUEBLE", schema = "SERVICIOS_BASICOS", catalog = "")
public class InmuebleEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private long idInmueble;
    private String sitioid;
    private String nombreRbs;
    private String direccion;
    private String codInmueble;
    private String codCve;
    private String costoInstall;

    private String estadoInmueble;
    private String latitud;
    private String longitud;
    private String observaciones;
    private boolean estado;
    private LocalidadEntity localidadByIdLocalidad;
    private TipoInmuebleEntity tipoInmuebleByIdTipoInmueble;
    private PropiedadEntity propiedadByIdPropiedad;

    private Collection<LicAmbEntity> licAmbsByIdInmueble;
    private Collection<InmServEntity> inmServsByIdInmueble;
    private String sector;
    private Collection<ContratosEntity> contratosByIdInmueble;


    private String obsErrors;
    private Long cInicial;

    @Id
    @Column(name = "ID_INMUEBLE")
    @SequenceGenerator(name="SEQ_INMUEBLE_GENERATOR", sequenceName="SEQ_INMUEBLE", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_INMUEBLE_GENERATOR")
    public long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble = idInmueble;
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
        InmuebleEntity that = (InmuebleEntity) o;
        return idInmueble == that.idInmueble &&
                Objects.equals(sitioid, that.sitioid) &&
                Objects.equals(nombreRbs, that.nombreRbs) &&
                Objects.equals(direccion, that.direccion) &&
                Objects.equals(codInmueble, that.codInmueble) &&
                Objects.equals(codCve, that.codCve) &&
                Objects.equals(costoInstall, that.costoInstall) &&
                Objects.equals(estadoInmueble, that.estadoInmueble) &&
                Objects.equals(latitud, that.latitud) &&
                Objects.equals(longitud, that.longitud) &&
                Objects.equals(observaciones, that.observaciones) &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idInmueble, sitioid, nombreRbs, direccion, codInmueble, codCve, costoInstall, estadoInmueble, latitud, longitud, observaciones, estado);
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
    @JoinColumn(name = "ID_TIPO_INMUEBLE", referencedColumnName = "ID_TIPO_INMUEBLE", nullable = false)
    public TipoInmuebleEntity getTipoInmuebleByIdTipoInmueble() {
        return tipoInmuebleByIdTipoInmueble;
    }

    public void setTipoInmuebleByIdTipoInmueble(TipoInmuebleEntity tipoInmuebleByIdTipoInmueble) {
        this.tipoInmuebleByIdTipoInmueble = tipoInmuebleByIdTipoInmueble;
    }

    @ManyToOne
    @JoinColumn(name = "ID_PROPIEDAD", referencedColumnName = "ID_PROPIEDAD", nullable = false)
    public PropiedadEntity getPropiedadByIdPropiedad() {
        return propiedadByIdPropiedad;
    }

    public void setPropiedadByIdPropiedad(PropiedadEntity propiedadByIdPropiedad) {
        this.propiedadByIdPropiedad = propiedadByIdPropiedad;
    }

    @OneToMany(mappedBy = "inmuebleByIdInmueble")
    public Collection<LicAmbEntity> getLicAmbsByIdInmueble() {
        return licAmbsByIdInmueble;
    }

    public void setLicAmbsByIdInmueble(Collection<LicAmbEntity> licAmbsByIdInmueble) {
        this.licAmbsByIdInmueble = licAmbsByIdInmueble;
    }

    @OneToMany(mappedBy = "inmuebleByIdInmueble")
    public Collection<InmServEntity> getInmServsByIdInmueble() {
        return inmServsByIdInmueble;
    }

    public void setInmServsByIdInmueble(Collection<InmServEntity> inmServsByIdInmueble) {
        this.inmServsByIdInmueble = inmServsByIdInmueble;
    }

    @Basic
    @Column(name = "SECTOR")
    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @OneToMany(mappedBy = "inmuebleByIdInmueble")
    public Collection<ContratosEntity> getContratosByIdInmueble() {
        return contratosByIdInmueble;
    }

    public void setContratosByIdInmueble(Collection<ContratosEntity> contratosByIdInmueble) {
        this.contratosByIdInmueble = contratosByIdInmueble;
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
}
