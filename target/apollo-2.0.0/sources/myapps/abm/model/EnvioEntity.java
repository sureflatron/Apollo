package myapps.abm.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ENVIO", schema = "SERVICIOS_BASICOS", catalog = "")
public class EnvioEntity {
    private Long idEnvio;
    private Long porcentaje;
    private boolean estado;
    private AlarmaEntity alarmaByIdAlarma;
    private boolean eliminar;

    @Id
    @Column(name = "ID_ENVIO")
    @SequenceGenerator(name = "SEQ_ENVIO_ID_GENERATOR", sequenceName = "SEQ_ENVIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENVIO_ID_GENERATOR")
    public Long getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Long idEnvio) {
        this.idEnvio = idEnvio;
    }

    @Basic
    @Column(name = "PORCENTAJE")
    public Long getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Long porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Basic
    @Column(name = "ESTADO")
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvioEntity that = (EnvioEntity) o;
        return Objects.equals(idEnvio, that.idEnvio) &&
                Objects.equals(porcentaje, that.porcentaje);

    }

    @Override
    public int hashCode() {

        return Objects.hash(idEnvio, porcentaje);
    }

    @ManyToOne
    @JoinColumn(name = "ID_ALARMA", referencedColumnName = "ID_ALARMA")
    public AlarmaEntity getAlarmaByIdAlarma() {
        return alarmaByIdAlarma;
    }

    public void setAlarmaByIdAlarma(AlarmaEntity alarmaByIdAlarma) {
        this.alarmaByIdAlarma = alarmaByIdAlarma;
    }

    @Transient
    public boolean isEliminar() {
        return eliminar;
    }

    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }

}
