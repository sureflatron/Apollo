package myapps.abm.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "CONTRATOS_RENOVADOS", schema = "SERVICIOS_BASICOS", catalog = "")
public class ContratosRenovadosEntity {
    private long idContrato;
    private String nroContrato;
    private String responsable;
    private String propietario;
    private String caducidad;
    private Date expiracion;
    private String tipo;
    private String catInm;
    private Date fechaCreacion;
    private String catCont;
    private String adjunto;
    private String codEbs;
    private String canonAlquiler;
    private boolean estado;
    private InmuebleEntity inmuebleByIdInmueble;
    private ServicioEntity servicioByIdServicio;
    private RadioBaseEntity radioBaseByIdRadioBase;
    private ProveedorEntity proveedorByIdProveedor;
    private DepartamentoEntity departamentoByIdDepartamento;
    private Date fechaRegistro;
    private String categoria;
    private BigDecimal monto;
    private String estadoContrato;
    private Long dias;
    private Collection<HistorialContratosEntity> historialContratosByIdContrato;

    @Id
    @Column(name = "ID_CONTRATO")
    @SequenceGenerator(name = "SEQ_CONTRATOS_RENOVADOS_GENERATOR", sequenceName =
            "SEQ_CONTRATOS_RENOVADOS",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONTRATOS_RENOVADOS_GENERATOR")
    public long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(long idContrato) {
        this.idContrato = idContrato;
    }

    @Basic
    @Column(name = "NRO_CONTRATO")
    public String getNroContrato() {
        return nroContrato;
    }

    public void setNroContrato(String nroContrato) {
        this.nroContrato = nroContrato;
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
    @Column(name = "PROPIETARIO")
    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    @Basic
    @Column(name = "CADUCIDAD")
    public String getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    @Basic
    @Column(name = "EXPIRACION")
    public Date getExpiracion() {
        return expiracion;
    }

    public void setExpiracion(Date expiracion) {
        this.expiracion = expiracion;
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
    @Column(name = "CAT_INM")
    public String getCatInm() {
        return catInm;
    }

    public void setCatInm(String catInm) {
        this.catInm = catInm;
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
    @Column(name = "CAT_CONT")
    public String getCatCont() {
        return catCont;
    }

    public void setCatCont(String catCont) {
        this.catCont = catCont;
    }

    @Basic
    @Column(name = "ADJUNTO")
    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    @Basic
    @Column(name = "COD_EBS")
    public String getCodEbs() {
        return codEbs;
    }

    public void setCodEbs(String codEbs) {
        this.codEbs = codEbs;
    }

    @Basic
    @Column(name = "CANON_ALQUILER")
    public String getCanonAlquiler() {
        return canonAlquiler;
    }

    public void setCanonAlquiler(String canonAlquiler) {
        this.canonAlquiler = canonAlquiler;
    }

    @Basic
    @Column(name = "ESTADO")
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @ManyToOne
    @JoinColumn(name = "ID_INMUEBLE", referencedColumnName = "ID_INMUEBLE")
    public InmuebleEntity getInmuebleByIdInmueble() {
        return inmuebleByIdInmueble;
    }

    public void setInmuebleByIdInmueble(InmuebleEntity inmuebleByIdInmueble) {
        this.inmuebleByIdInmueble = inmuebleByIdInmueble;
    }

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO")
    public ServicioEntity getServicioByIdServicio() {
        return servicioByIdServicio;
    }

    public void setServicioByIdServicio(ServicioEntity servicioByIdServicio) {
        this.servicioByIdServicio = servicioByIdServicio;
    }

    @ManyToOne
    @JoinColumn(name = "ID_RADIO_BASE", referencedColumnName = "ID_RADIO_BASE")
    public RadioBaseEntity getRadioBaseByIdRadioBase() {
        return radioBaseByIdRadioBase;
    }

    public void setRadioBaseByIdRadioBase(RadioBaseEntity radioBaseByIdRadioBase) {
        this.radioBaseByIdRadioBase = radioBaseByIdRadioBase;
    }

    @ManyToOne
    @JoinColumn(name = "ID_PROVEEDOR", referencedColumnName = "ID_PROVEEDOR")
    public ProveedorEntity getProveedorByIdProveedor() {
        return proveedorByIdProveedor;
    }

    public void setProveedorByIdProveedor(ProveedorEntity proveedorByIdProveedor) {
        this.proveedorByIdProveedor = proveedorByIdProveedor;
    }
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO")
    public DepartamentoEntity getDepartamentoByIdDepartamento() {
        return departamentoByIdDepartamento;
    }

    public void setDepartamentoByIdDepartamento(DepartamentoEntity departamentoByIdDepartamento) {
        this.departamentoByIdDepartamento = departamentoByIdDepartamento;
    }

    @Basic
    @Column(name = "FECHA_REGISTRO")
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Basic
    @Column(name = "CATEGORIA")
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Basic
    @Column(name = "MONTO")
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    @Basic
    @Column(name = "ESTADO_CONTRATO")
    public String getEstadoContrato() {
        return estadoContrato;
    }

    public void setEstadoContrato(String estadoContrato) {
        this.estadoContrato = estadoContrato;
    }

    @Basic
    @Column(name = "DIAS")
    public Long getDias() {
        return dias;
    }

    public void setDias(Long dias) {
        this.dias = dias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContratosRenovadosEntity that = (ContratosRenovadosEntity) o;
        return idContrato == that.idContrato &&
                Objects.equals(nroContrato, that.nroContrato) &&
                Objects.equals(responsable, that.responsable) &&
                Objects.equals(propietario, that.propietario) &&
                Objects.equals(caducidad, that.caducidad) &&
                Objects.equals(expiracion, that.expiracion) &&
                Objects.equals(tipo, that.tipo) &&
                Objects.equals(catInm, that.catInm) &&
                Objects.equals(fechaCreacion, that.fechaCreacion) &&
                Objects.equals(catCont, that.catCont) &&
                Objects.equals(adjunto, that.adjunto) &&
                Objects.equals(codEbs, that.codEbs) &&
                Objects.equals(canonAlquiler, that.canonAlquiler) &&
                Objects.equals(estado, that.estado) &&
                Objects.equals(fechaRegistro, that.fechaRegistro) &&
                Objects.equals(categoria, that.categoria) &&
                Objects.equals(monto, that.monto) &&
                Objects.equals(estadoContrato, that.estadoContrato) &&
                Objects.equals(dias, that.dias);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idContrato, nroContrato, responsable, propietario, caducidad, expiracion, tipo, catInm, fechaCreacion, catCont, adjunto, codEbs, canonAlquiler, estado,  fechaRegistro, categoria, monto, estadoContrato, dias);
    }

    @OneToMany(mappedBy = "contratosRenovadosByContratoRevoId")
    public Collection<HistorialContratosEntity> getHistorialContratosByIdContrato() {
        return historialContratosByIdContrato;
    }

    public void setHistorialContratosByIdContrato(Collection<HistorialContratosEntity> historialContratosByIdContrato) {
        this.historialContratosByIdContrato = historialContratosByIdContrato;
    }
}
