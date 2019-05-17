package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "CAMPOS", schema = "SERVICIOS_BASICOS")
public class CamposEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    private long idCampo;
    private Long numero;
    private String nombre;
    private String letra;
    private boolean estado;
    private ConfiguracionesEntity configuracionesByIdConfiguraciones;
    private Date fecha;

    @Id
    @Column(name = "ID_CAMPO")
    @SequenceGenerator(name = "SEQ_CAMPOS_ID_GENERATOR", sequenceName = "SEQ_CAMPOS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CAMPOS_ID_GENERATOR")
    public long getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(long idCampo) {
        this.idCampo = idCampo;
    }

    @Basic
    @Column(name = "NUMERO")
    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
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
    @Column(name = "LETRA")
    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    @Basic
    @Column(name = "ESTADO")
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CamposEntity that = (CamposEntity) o;
        return idCampo == that.idCampo &&
                Objects.equals(numero, that.numero) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(letra, that.letra) &&
                Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idCampo, numero, nombre, letra, estado);
    }

    @ManyToOne
    @JoinColumn(name = "ID_CONFIGURACIONES", referencedColumnName = "ID_CONFIGURACIONES")
    public ConfiguracionesEntity getConfiguracionesByIdConfiguraciones() {
        return configuracionesByIdConfiguraciones;
    }

    public void setConfiguracionesByIdConfiguraciones(ConfiguracionesEntity configuracionesByIdConfiguraciones) {
        this.configuracionesByIdConfiguraciones = configuracionesByIdConfiguraciones;
    }

    @Transient
    public Date getFecha() {
        return fecha;
    }

    @Transient
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
