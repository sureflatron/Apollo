package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "BANCO", schema = "SERVICIOS_BASICOS", catalog = "")
public class BancoEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private long idBanco;
    private String nombre;
    private String telefono;
    private String fax;
    private String direccion;
    private String email;
    private String contacto;
    private String nroCuenta;
    private boolean estado;
    private Collection<ServicioEntity> serviciosByIdBanco;
    private DepartamentoEntity departamentoByIdDepartamento;


    @Id
    @Column(name = "ID_BANCO")
    @SequenceGenerator(name = "SEQ_BANCO_GENERATOR", sequenceName = "SEQ_BANCO",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BANCO_GENERATOR")
    public long getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(long idBanco) {
        this.idBanco = idBanco;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BancoEntity that = (BancoEntity) o;
        return idBanco == that.idBanco &&

                Objects.equals(nombre, that.nombre) &&
                Objects.equals(telefono, that.telefono) &&
                Objects.equals(fax, that.fax) &&
                Objects.equals(direccion, that.direccion) &&
                Objects.equals(email, that.email) &&
                Objects.equals(contacto, that.contacto) &&
                Objects.equals(nroCuenta, that.nroCuenta)&&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idBanco, nombre, telefono, fax, direccion, email,
                contacto, nroCuenta,estado);
    }

    @Basic
    @Column(name = "ESTADO")
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @OneToMany(mappedBy = "bancoByIdBanco")
    public Collection<ServicioEntity> getServiciosByIdBanco() {
        return serviciosByIdBanco;
    }

    public void setServiciosByIdBanco(Collection<ServicioEntity> serviciosByIdBanco) {
        this.serviciosByIdBanco = serviciosByIdBanco;
    }


    @ManyToOne
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO", nullable = false)
    public DepartamentoEntity getDepartamentoByIdDepartamento() {
        return departamentoByIdDepartamento;
    }

    public void setDepartamentoByIdDepartamento(DepartamentoEntity departamentoByIdDepartamento) {
        this.departamentoByIdDepartamento = departamentoByIdDepartamento;
    }
}
