package myapps.abm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "CARGA", schema = "SERVICIOS_BASICOS", catalog = "")
public class CargaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private long idCarga;
    private String ciudad;
    private String contratoInicio;
    private String contratoFin;
    private String frecuenciaDePago;
    private String mesDePago;
    private String cuentaBancaria;
    private String nContrato;
    private String nroProveedor;
    private String proveedor;
    private String siteProveedor;
    private String nombreCelda;
    private String montoDeFactura;
    private String montoPorPagar;
    private String divisa;

    @Id
    @Column(name = "ID_CARGA")
    @SequenceGenerator(name="SEQ_CARGA_ID_GENERATOR", sequenceName="SEQ_CARGA", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CARGA_ID_GENERATOR")

    public long getIdCarga() {
        return idCarga;
    }

    public void setIdCarga(long idCarga) {
        this.idCarga = idCarga;
    }

    @Basic
    @Column(name = "CIUDAD")
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Basic
    @Column(name = "CONTRATO_INICIO")
    public String getContratoInicio() {
        return contratoInicio;
    }

    public void setContratoInicio(String contratoInicio) {
        this.contratoInicio = contratoInicio;
    }

    @Basic
    @Column(name = "CONTRATO_FIN")
    public String getContratoFin() {
        return contratoFin;
    }

    public void setContratoFin(String contratoFin) {
        this.contratoFin = contratoFin;
    }

    @Basic
    @Column(name = "FRECUENCIA_DE_PAGO")
    public String getFrecuenciaDePago() {
        return frecuenciaDePago;
    }

    public void setFrecuenciaDePago(String frecuenciaDePago) {
        this.frecuenciaDePago = frecuenciaDePago;
    }

    @Basic
    @Column(name = "MES_DE_PAGO")
    public String getMesDePago() {
        return mesDePago;
    }

    public void setMesDePago(String mesDePago) {
        this.mesDePago = mesDePago;
    }

    @Basic
    @Column(name = "CUENTA_BANCARIA")
    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    @Basic
    @Column(name = "N_CONTRATO")
    public String getnContrato() {
        return nContrato;
    }

    public void setnContrato(String nContrato) {
        this.nContrato = nContrato;
    }

    @Basic
    @Column(name = "NRO_PROVEEDOR")
    public String getNroProveedor() {
        return nroProveedor;
    }

    public void setNroProveedor(String nroProveedor) {
        this.nroProveedor = nroProveedor;
    }

    @Basic
    @Column(name = "PROVEEDOR")
    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    @Basic
    @Column(name = "SITE_PROVEEDOR")
    public String getSiteProveedor() {
        return siteProveedor;
    }

    public void setSiteProveedor(String siteProveedor) {
        this.siteProveedor = siteProveedor;
    }

    @Basic
    @Column(name = "NOMBRE_CELDA")
    public String getNombreCelda() {
        return nombreCelda;
    }

    public void setNombreCelda(String nombreCelda) {
        this.nombreCelda = nombreCelda;
    }

    @Basic
    @Column(name = "MONTO_DE_FACTURA")
    public String getMontoDeFactura() {
        return montoDeFactura;
    }

    public void setMontoDeFactura(String montoDeFactura) {
        this.montoDeFactura = montoDeFactura;
    }

    @Basic
    @Column(name = "MONTO_POR_PAGAR")
    public String getMontoPorPagar() {
        return montoPorPagar;
    }

    public void setMontoPorPagar(String montoPorPagar) {
        this.montoPorPagar = montoPorPagar;
    }

    @Basic
    @Column(name = "DIVISA")
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
        CargaEntity that = (CargaEntity) o;
        return idCarga == that.idCarga &&
                Objects.equals(ciudad, that.ciudad) &&
                Objects.equals(contratoInicio, that.contratoInicio) &&
                Objects.equals(contratoFin, that.contratoFin) &&
                Objects.equals(frecuenciaDePago, that.frecuenciaDePago) &&
                Objects.equals(mesDePago, that.mesDePago) &&
                Objects.equals(cuentaBancaria, that.cuentaBancaria) &&
                Objects.equals(nContrato, that.nContrato) &&
                Objects.equals(nroProveedor, that.nroProveedor) &&
                Objects.equals(proveedor, that.proveedor) &&
                Objects.equals(siteProveedor, that.siteProveedor) &&
                Objects.equals(nombreCelda, that.nombreCelda) &&
                Objects.equals(montoDeFactura, that.montoDeFactura) &&
                Objects.equals(montoPorPagar, that.montoPorPagar) &&
                Objects.equals(divisa, that.divisa);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idCarga, ciudad, contratoInicio, contratoFin, frecuenciaDePago, mesDePago, cuentaBancaria, nContrato, nroProveedor, proveedor, siteProveedor, nombreCelda, montoDeFactura, montoPorPagar, divisa);
    }
}
