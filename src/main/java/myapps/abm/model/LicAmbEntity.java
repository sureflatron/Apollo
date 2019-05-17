package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "LIC_AMB")
public class LicAmbEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private long idLicAmb;
    private String nro;
    private String nroPermiso;
    private long estado;
    private String sitio;
    private String adjunto;
    private String estadoLa;
    private ProveedorEntity proveedorByIdProveedor;
    private DepartamentoEntity departamentoByIdDepartamento;
    private Date expira;
    private String tipo;
    private InmuebleEntity inmuebleByIdInmueble;

    @Id
    @Column(name = "ID_LIC_AMB")
    @SequenceGenerator(name = "SEQ_LIC_AMB_GENERATOR", sequenceName = "SEQ_LIC_AMB",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LIC_AMB_GENERATOR")
    public long getIdLicAmb() {
        return idLicAmb;
    }

    public void setIdLicAmb(long idLicAmb) {
        this.idLicAmb = idLicAmb;
    }

    @Basic
    @Column(name = "NRO")
    public String getNro() {
        return nro;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    @Basic
    @Column(name = "NRO_PERMISO")
    public String getNroPermiso() {
        return nroPermiso;
    }

    public void setNroPermiso(String nroPermiso) {
        this.nroPermiso = nroPermiso;
    }

    @Basic
    @Column(name = "ESTADO")
    public long getEstado() {
        return estado;
    }

    public void setEstado(long estado) {
        this.estado = estado;
    }

    @Basic
    @Column(name = "SITIO")
    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
    }

    @Basic
    @Column(name = "ADJUNTO")
    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LicAmbEntity that = (LicAmbEntity) o;
        return idLicAmb == that.idLicAmb &&
                nro == that.nro &&

                Objects.equals(nroPermiso, that.nroPermiso) &&
                Objects.equals(estado, that.estado) &&
                Objects.equals(expira, that.expira) &&
                Objects.equals(sitio, that.sitio) &&
                Objects.equals(adjunto, that.adjunto)&&
                Objects.equals(estadoLa, that.estadoLa);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idLicAmb, nro, nroPermiso, estado, expira,
                 sitio, adjunto,estadoLa);
    }

    @Basic
    @Column(name = "ESTADO_LA")
    public String getEstadoLa() {
        return estadoLa;
    }

    public void setEstadoLa(String estadoLa) {
        this.estadoLa = estadoLa;
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
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO", nullable = false)
    public DepartamentoEntity getDepartamentoByIdDepartamento() {
        return departamentoByIdDepartamento;
    }

    public void setDepartamentoByIdDepartamento(DepartamentoEntity departamentoByIdDepartamento) {
        this.departamentoByIdDepartamento = departamentoByIdDepartamento;
    }

    @Basic
    @Column(name = "EXPIRA")
    public Date getExpira() {
        return expira;
    }

    public void setExpira(Date expira) {
        this.expira = expira;
    }

    @Basic
    @Column(name = "TIPO")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @ManyToOne
    @JoinColumn(name = "ID_INMUEBLE", referencedColumnName = "ID_INMUEBLE")
    public InmuebleEntity getInmuebleByIdInmueble() {
        return inmuebleByIdInmueble;
    }

    public void setInmuebleByIdInmueble(InmuebleEntity inmuebleByIdInmueble) {
        this.inmuebleByIdInmueble = inmuebleByIdInmueble;
    }
}
