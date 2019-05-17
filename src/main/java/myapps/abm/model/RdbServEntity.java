package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "RDB_SERV", schema = "SERVICIOS_BASICOS", catalog = "")
public class RdbServEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idRdbsServ;
    private Long estado;
    private RadioBaseEntity radioBaseByIdRadioBase;
    private ServicioEntity servicioByIdServicio;

    @Id
    @Column(name = "ID_RDBS_SERV")
    @SequenceGenerator(name="SEQ_RDB_SERV_GENERATOR", sequenceName="SEQ_RDB_SERV", allocationSize
            = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RDB_SERV_GENERATOR")
    public long getIdRdbsServ() {
        return idRdbsServ;
    }

    public void setIdRdbsServ(long idRdbsServ) {
        this.idRdbsServ = idRdbsServ;
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
        RdbServEntity that = (RdbServEntity) o;
        return idRdbsServ == that.idRdbsServ &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idRdbsServ, estado);
    }

    @ManyToOne
    @JoinColumn(name = "ID_RADIO_BASE", referencedColumnName = "ID_RADIO_BASE", nullable = false)
    public RadioBaseEntity getRadioBaseByIdRadioBase() {
        return radioBaseByIdRadioBase;
    }

    public void setRadioBaseByIdRadioBase(RadioBaseEntity radioBaseByIdRadioBase) {
        this.radioBaseByIdRadioBase = radioBaseByIdRadioBase;
    }

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO", nullable = false)
    public ServicioEntity getServicioByIdServicio() {
        return servicioByIdServicio;
    }

    public void setServicioByIdServicio(ServicioEntity servicioByIdServicio) {
        this.servicioByIdServicio = servicioByIdServicio;
    }
}
