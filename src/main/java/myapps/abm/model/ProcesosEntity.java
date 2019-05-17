package myapps.abm.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PROCESOS", schema = "SERVICIOS_BASICOS", catalog = "")
public class ProcesosEntity {
    private long idProceso;
    private String titulo;
    private String descripcion;

    @Id
    @Column(name = "ID_PROCESO")
    @SequenceGenerator(name="SEQ_PROCESOS_ID_GENERATOR", sequenceName="SEQ_PROCESOS", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PROCESOS_ID_GENERATOR")
    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    @Basic
    @Column(name = "TITULO")
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Basic
    @Column(name = "DESCRIPCION")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcesosEntity that = (ProcesosEntity) o;
        return idProceso == that.idProceso &&
                Objects.equals(titulo, that.titulo) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idProceso, titulo, descripcion);
    }
}
