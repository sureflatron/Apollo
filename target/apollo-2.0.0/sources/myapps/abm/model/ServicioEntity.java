package myapps.abm.model;

import myapps.workflow.entity.SolicitudesEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "SERVICIO", schema = "SERVICIOS_BASICOS", catalog = "")
public class ServicioEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idServicio;
    private String idSite;
    private String codigo;
    private String nombreSite;
    private String codServMed;
    private String codFijo;
    private String formaPago;
    private String nroCuenta;
    private String observaciones;
    private boolean estado;
    private String latitud;
    private String longuitud;
    private byte[] imagen;
    private TipoServicioEntity tipoServicioByIdTipoServicio;
    private ProveedorEntity proveedorByIdProveedor;
    private BancoEntity bancoByIdBanco;
    private MonedaEntity monedaByIdMoneda;
    private UnidadOperativaEntity unidadOperativaByIdUnidadOperativa;
    private Collection<PagoServicioEntity> pagoServiciosByIdServicio;
    private Collection<InmServEntity> inmServsByIdServicio;
    private Collection<ContratosEntity> contratosByIdServicio;
    private Date fechaCreacion;
    private MedidorEntity medidorByIdServicio;
    private Collection<RdbServEntity> rdbServsByIdServicio;
    private String nombreTipoInmueble;
    private String pertenece;
    private InmuebleEntity inmuebleEntity;
    private RadioBaseEntity radioBaseEntity;
    private Long cInicial;
    private Collection<SolicitudesEntity> solicitudesByIdServicio;

    @Id
    @Column(name = "ID_SERVICIO")
    @SequenceGenerator(name = "SEQ_SERVICIO_ID_GENERATOR", sequenceName = "SEQ_SERVICIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SERVICIO_ID_GENERATOR")
    public long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(long idServicio) {
        this.idServicio = idServicio;
    }

    @Basic
    @Column(name = "ID_SITE", nullable = true, length = 50)
    public String getIdSite() {
        return idSite;
    }

    public void setIdSite(String idSite) {
        this.idSite = idSite;
    }

    @Basic
    @Column(name = "CODIGO", nullable = true, length = 50)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "NOMBRE_SITE", nullable = true, length = 50)
    public String getNombreSite() {
        return nombreSite;
    }

    public void setNombreSite(String nombreSite) {
        this.nombreSite = nombreSite;
    }

    @Basic
    @Column(name = "COD_SERV_MED", nullable = true, length = 50)
    public String getCodServMed() {
        return codServMed;
    }

    public void setCodServMed(String codServMed) {
        this.codServMed = codServMed;
    }

    @Basic
    @Column(name = "COD_FIJO", nullable = true, length = 50)
    public String getCodFijo() {
        return codFijo;
    }

    public void setCodFijo(String codFijo) {
        this.codFijo = codFijo;
    }

    @Basic
    @Column(name = "FORMA_PAGO", nullable = true, length = 50)
    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    @Basic
    @Column(name = "NRO_CUENTA", nullable = true, length = 50)
    public String getNroCuenta() {
        return nroCuenta;
    }

    public void setNroCuenta(String nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    @Basic
    @Column(name = "OBSERVACIONES", nullable = true, length = 50)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Basic
    @Column(name = "ESTADO", nullable = true, precision = 9)
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_SERVICIO", referencedColumnName = "ID_TIPO_SERVICIO", nullable = false)
    public TipoServicioEntity getTipoServicioByIdTipoServicio() {
        return tipoServicioByIdTipoServicio;
    }

    public void setTipoServicioByIdTipoServicio(TipoServicioEntity tipoServicioByIdTipoServicio) {
        this.tipoServicioByIdTipoServicio = tipoServicioByIdTipoServicio;
    }

    @ManyToOne
    @JoinColumn(name = "ID_PROVEEDOR", referencedColumnName = "ID_PROVEEDOR", nullable = false)
    public ProveedorEntity getProveedorByIdProveedor() {
        return proveedorByIdProveedor;
    }

    public void setProveedorByIdProveedor(ProveedorEntity proveedorByIdProveedor) {
        this.proveedorByIdProveedor = proveedorByIdProveedor;
    }

    @ManyToOne
    @JoinColumn(name = "ID_BANCO", referencedColumnName = "ID_BANCO", nullable = false)
    public BancoEntity getBancoByIdBanco() {
        return bancoByIdBanco;
    }

    public void setBancoByIdBanco(BancoEntity bancoByIdBanco) {
        this.bancoByIdBanco = bancoByIdBanco;
    }

    @ManyToOne
    @JoinColumn(name = "ID_MONEDA", referencedColumnName = "ID_MONEDA", nullable = false)
    public MonedaEntity getMonedaByIdMoneda() {
        return monedaByIdMoneda;
    }

    public void setMonedaByIdMoneda(MonedaEntity monedaByIdMoneda) {
        this.monedaByIdMoneda = monedaByIdMoneda;
    }

    @ManyToOne
    @JoinColumn(name = "ID_UNIDAD_OPERATIVA", referencedColumnName = "ID_UNIDAD_OPERATIVA", nullable = false)
    public UnidadOperativaEntity getUnidadOperativaByIdUnidadOperativa() {
        return unidadOperativaByIdUnidadOperativa;
    }

    public void setUnidadOperativaByIdUnidadOperativa(UnidadOperativaEntity unidadOperativaByIdUnidadOperativa) {
        this.unidadOperativaByIdUnidadOperativa = unidadOperativaByIdUnidadOperativa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServicioEntity that = (ServicioEntity) o;
        return idServicio == that.idServicio &&
                estado == that.estado &&
                Objects.equals(idSite, that.idSite) &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(nombreSite, that.nombreSite) &&
                Objects.equals(codServMed, that.codServMed) &&
                Objects.equals(codFijo, that.codFijo) &&
                Objects.equals(formaPago, that.formaPago) &&
                Objects.equals(nroCuenta, that.nroCuenta) &&
                Objects.equals(observaciones, that.observaciones);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idServicio, idSite, codigo, nombreSite, codServMed, codFijo, formaPago, nroCuenta, observaciones, estado);
    }

    @OneToMany(mappedBy = "servicioByIdServicio")
    public Collection<PagoServicioEntity> getPagoServiciosByIdServicio() {
        return pagoServiciosByIdServicio;
    }

    public void setPagoServiciosByIdServicio(Collection<PagoServicioEntity> pagoServiciosByIdServicio) {
        this.pagoServiciosByIdServicio = pagoServiciosByIdServicio;
    }

    @Transient
    public String getLatitud() {
        return latitud;
    }

    @Transient
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    @Transient
    public String getLonguitud() {
        return longuitud;
    }

    @Transient
    public void setLonguitud(String longuitud) {
        this.longuitud = longuitud;
    }

    @OneToMany(mappedBy = "servicioByIdServicio")
    public Collection<InmServEntity> getInmServsByIdServicio() {
        return inmServsByIdServicio;
    }

    public void setInmServsByIdServicio(Collection<InmServEntity> inmServsByIdServicio) {
        this.inmServsByIdServicio = inmServsByIdServicio;
    }

    @OneToMany(mappedBy = "servicioByIdServicio")
    public Collection<ContratosEntity> getContratosByIdServicio() {
        return contratosByIdServicio;
    }

    public void setContratosByIdServicio(Collection<ContratosEntity> contratosByIdServicio) {
        this.contratosByIdServicio = contratosByIdServicio;
    }

    @Basic
    @Column(name = "FECHA_CREACION")
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @OneToOne(mappedBy = "servicioByIdServicio")
    public MedidorEntity getMedidorByIdServicio() {
        return medidorByIdServicio;
    }

    public void setMedidorByIdServicio(MedidorEntity medidorByIdServicio) {
        this.medidorByIdServicio = medidorByIdServicio;
    }

    @OneToMany(mappedBy = "servicioByIdServicio")
    public Collection<RdbServEntity> getRdbServsByIdServicio() {
        return rdbServsByIdServicio;
    }

    public void setRdbServsByIdServicio(Collection<RdbServEntity> rdbServsByIdServicio) {
        this.rdbServsByIdServicio = rdbServsByIdServicio;
    }

    @Transient
    public String getNombreTipoInmueble() {
        return nombreTipoInmueble;
    }

    public void setNombreTipoInmueble(String nombreTipoInmueble) {
        this.nombreTipoInmueble = nombreTipoInmueble;
    }

    @Transient
    public byte[] getImagen() {
        return imagen;
    }

    @Transient
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    @Transient
    public InmuebleEntity getInmuebleEntity() {
        return inmuebleEntity;
    }

    @Transient
    public void setInmuebleEntity(InmuebleEntity inmuebleEntity) {
        this.inmuebleEntity = inmuebleEntity;
    }

    @Transient
    public RadioBaseEntity getRadioBaseEntity() {
        return radioBaseEntity;
    }

    @Transient
    public void setRadioBaseEntity(RadioBaseEntity radioBaseEntity) {
        this.radioBaseEntity = radioBaseEntity;
    }

    @Transient
    public String getPertenece() {
        return pertenece;
    }

    @Transient
    public void setPertenece(String pertenece) {
        this.pertenece = pertenece;
    }

    @Basic
    @Column(name = "C_INICIAL")
    public Long getcInicial() {
        return cInicial;
    }

    public void setcInicial(Long cInicial) {
        this.cInicial = cInicial;
    }

    @OneToMany(mappedBy = "servicioByIdServicio")
    public Collection<SolicitudesEntity> getSolicitudesByIdServicio() {
        return solicitudesByIdServicio;
    }

    public void setSolicitudesByIdServicio(Collection<SolicitudesEntity> solicitudesByIdServicio) {
        this.solicitudesByIdServicio = solicitudesByIdServicio;
    }
}
