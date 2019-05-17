package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "PROVEEDOR", schema = "SERVICIOS_BASICOS", catalog = "")
public class ProveedorEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idProveedor;
    private String nombre;
    private String sigla;
    private String telefono;
    private String fax;
    private String direccion;
    private String email;
    private String contacto;
    private String nroCuenta;
    private String infoAd;
    private long estado;
    private Collection<LicAmbEntity> licAmbsByIdProveedor;
    private Long isFusion;
    private Collection<ServicioEntity> serviciosByIdProveedor;
    private CategoriaEntity categoriaByIdCategoria;
    private DepartamentoEntity departamentoByIdDepartamento;
    private String tipo;
    private Collection<ContratosEntity> contratosByIdProveedor;

    @Id
    @Column(name = "ID_PROVEEDOR")
    @SequenceGenerator(name = "SEQ_PROVEEDOR_GENERATOR", sequenceName = "SEQ_PROVEEDOR",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PROVEEDOR_GENERATOR")
    public long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(long idProveedor) {
        this.idProveedor = idProveedor;
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
    @Column(name = "SIGLA")
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Basic
    @Column(name = "TELEFONO")
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Basic
    @Column(name = "FAX")
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
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
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "CONTACTO")
    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    @Basic
    @Column(name = "NRO_CUENTA")
    public String getNroCuenta() {
        return nroCuenta;
    }

    public void setNroCuenta(String nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    @Basic
    @Column(name = "INFO_AD")
    public String getInfoAd() {
        return infoAd;
    }

    public void setInfoAd(String infoAd) {
        this.infoAd = infoAd;
    }

    @Basic
    @Column
    public long getEstado() {
        return estado;
    }

    public void setEstado(long estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProveedorEntity that = (ProveedorEntity) o;
        return idProveedor == that.idProveedor &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(sigla, that.sigla) &&
                Objects.equals(telefono, that.telefono) &&
                Objects.equals(fax, that.fax) &&
                Objects.equals(direccion, that.direccion) &&
                Objects.equals(email, that.email) &&
                Objects.equals(contacto, that.contacto) &&
                Objects.equals(nroCuenta, that.nroCuenta) &&
                Objects.equals(infoAd, that.infoAd) &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProveedor, nombre, sigla, telefono, fax,
                direccion, email, contacto, nroCuenta, infoAd, estado);
    }
    
    @OneToMany(mappedBy = "proveedorByIdProveedor")
    public Collection<LicAmbEntity> getLicAmbsByIdProveedor() {
        return licAmbsByIdProveedor;
    }

    public void setLicAmbsByIdProveedor(Collection<LicAmbEntity> licAmbsByIdProveedor) {
        this.licAmbsByIdProveedor = licAmbsByIdProveedor;
    }

    @Basic
    @Column(name = "IS_FUSION")
    public Long getIsFusion() {
        return isFusion;
    }

    public void setIsFusion(Long isFusion) {
        this.isFusion = isFusion;
    }

    @OneToMany(mappedBy = "proveedorByIdProveedor")
    public Collection<ServicioEntity> getServiciosByIdProveedor() {
        return serviciosByIdProveedor;
    }

    public void setServiciosByIdProveedor(Collection<ServicioEntity> serviciosByIdProveedor) {
        this.serviciosByIdProveedor = serviciosByIdProveedor;
    }

    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID_CATEGORIA")
    public CategoriaEntity getCategoriaByIdCategoria() {
        return categoriaByIdCategoria;
    }

    public void setCategoriaByIdCategoria(CategoriaEntity categoriaByIdCategoria) {
        this.categoriaByIdCategoria = categoriaByIdCategoria;
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
    @Column(name = "TIPO")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @OneToMany(mappedBy = "proveedorByIdProveedor")
    public Collection<ContratosEntity> getContratosByIdProveedor() {
        return contratosByIdProveedor;
    }

    public void setContratosByIdProveedor(Collection<ContratosEntity> contratosByIdProveedor) {
        this.contratosByIdProveedor = contratosByIdProveedor;
    }
}
