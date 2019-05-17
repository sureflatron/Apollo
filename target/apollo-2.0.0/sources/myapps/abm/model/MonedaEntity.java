package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "MONEDA")
public class MonedaEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private long idMoneda;
    private String moneda;
    private String pais;
    private  String divisa;
    private Collection<ServicioEntity> serviciosByIdMoneda;

    @Id
    @Column(name = "ID_MONEDA")
    @SequenceGenerator(name="SEQ_MONEDA_ID_GENERATOR", sequenceName="SEQ_MONEDA", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_MONEDA_ID_GENERATOR")

    public long getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(long idMoneda) {
        this.idMoneda = idMoneda;
    }

    @Basic
    @Column(name = "MONEDA", nullable = true, length = 100)
    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Basic
    @Column(name = "PAIS", nullable = true, length = 100)
    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }




    @Basic
    @Column(name = "DIVISA", nullable = true, length = 100)
    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonedaEntity that = (MonedaEntity) o;
        return idMoneda == that.idMoneda &&
                Objects.equals(moneda, that.moneda) &&
                Objects.equals(pais, that.pais);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idMoneda, moneda, pais);
    }

    @OneToMany(mappedBy = "monedaByIdMoneda")
    public Collection<ServicioEntity> getServiciosByIdMoneda() {
        return serviciosByIdMoneda;
    }

    public void setServiciosByIdMoneda(Collection<ServicioEntity> serviciosByIdMoneda) {
        this.serviciosByIdMoneda = serviciosByIdMoneda;
    }
}
