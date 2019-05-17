package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "INM_SERV", schema = "SERVICIOS_BASICOS", catalog = "")
public class InmServEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private long idInmServ;
    private Long estado;
    private InmuebleEntity inmuebleByIdInmueble;
    private ServicioEntity servicioByIdServicio;
    private String tipoInm;

    @Id
    @Column(name = "ID_INM_SERV")
    @SequenceGenerator(name="SEQ_INM_SERV_GENERATOR", sequenceName="SEQ_INM_SERV", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_INM_SERV_GENERATOR")
    public long getIdInmServ() {
        return idInmServ;
    }

    public void setIdInmServ(long idInmServ) {
        this.idInmServ = idInmServ;
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
        InmServEntity that = (InmServEntity) o;
        return idInmServ == that.idInmServ &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idInmServ, estado);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INMUEBLE", referencedColumnName = "ID_INMUEBLE", nullable = false)
    public InmuebleEntity getInmuebleByIdInmueble() {
        return inmuebleByIdInmueble;
    }

    public void setInmuebleByIdInmueble(InmuebleEntity inmuebleByIdInmueble) {
        this.inmuebleByIdInmueble = inmuebleByIdInmueble;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO", nullable = false)
    public ServicioEntity getServicioByIdServicio() {
        return servicioByIdServicio;
    }

    public void setServicioByIdServicio(ServicioEntity servicioByIdServicio) {
        this.servicioByIdServicio = servicioByIdServicio;
    }

    @Basic
    @Column(name = "TIPO_INM")
    public String getTipoInm() {
        return tipoInm;
    }

    public void setTipoInm(String tipoInm) {
        this.tipoInm = tipoInm;
    }
}
