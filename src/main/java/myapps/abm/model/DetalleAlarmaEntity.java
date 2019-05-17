package myapps.abm.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "DETALLE_ALARMA", schema = "SERVICIOS_BASICOS", catalog = "")
public class DetalleAlarmaEntity {
    private long idDetalleAlarma;
    private Time fechaEnvio;
    private String destinos;
    private Long nroEnvio;
    private AlarmaEntity alarmaByIdAlarma;

    @Id
    @Column(name = "ID_DETALLE_ALARMA")
    public long getIdDetalleAlarma() {
        return idDetalleAlarma;
    }

    public void setIdDetalleAlarma(long idDetalleAlarma) {
        this.idDetalleAlarma = idDetalleAlarma;
    }

    @Basic
    @Column(name = "FECHA_ENVIO")
    public Time getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Time fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    @Basic
    @Column(name = "DESTINOS")
    public String getDestinos() {
        return destinos;
    }

    public void setDestinos(String destinos) {
        this.destinos = destinos;
    }

    @Basic
    @Column(name = "NRO_ENVIO")
    public Long getNroEnvio() {
        return nroEnvio;
    }

    public void setNroEnvio(Long nroEnvio) {
        this.nroEnvio = nroEnvio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleAlarmaEntity that = (DetalleAlarmaEntity) o;
        return idDetalleAlarma == that.idDetalleAlarma &&
                Objects.equals(fechaEnvio, that.fechaEnvio) &&
                Objects.equals(destinos, that.destinos) &&
                Objects.equals(nroEnvio, that.nroEnvio);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDetalleAlarma, fechaEnvio, destinos, nroEnvio);
    }

    @ManyToOne
    @JoinColumn(name = "ID_ALARMA", referencedColumnName = "ID_ALARMA")
    public AlarmaEntity getAlarmaByIdAlarma() {
        return alarmaByIdAlarma;
    }

    public void setAlarmaByIdAlarma(AlarmaEntity alarmaByIdAlarma) {
        this.alarmaByIdAlarma = alarmaByIdAlarma;
    }
}
