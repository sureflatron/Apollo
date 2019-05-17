package myapps.cargainicial;

import java.io.Serializable;

public class CargaInicialServiciosDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombreRdbInm;
	private String codRdbInm;
	private String tipoServicio;
	private String idSite;
	private String codigoServicio;
	private String nombreServicio;
	private String proveedor;
	private String codServMed;
	private String codFijo;
	private String formaPago;
	private String banco;
	private String moneda;
	private String nroCuenta;
	private String obs;
	private String unidadOperativa;
	private String error;

	public String getNombreRdbInm() {
		return nombreRdbInm;
	}

	public void setNombreRdbInm(String nombreRdbInm) {
		this.nombreRdbInm = nombreRdbInm;
	}

	public String getCodRdbInm() {
		return codRdbInm;
	}

	public void setCodRdbInm(String codRdbInm) {
		this.codRdbInm = codRdbInm;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public String getIdSite() {
		return idSite;
	}

	public void setIdSite(String idSite) {
		this.idSite = idSite;
	}

	public String getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getCodServMed() {
		return codServMed;
	}

	public void setCodServMed(String codServMed) {
		this.codServMed = codServMed;
	}

	public String getCodFijo() {
		return codFijo;
	}

	public void setCodFijo(String codFijo) {
		this.codFijo = codFijo;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getUnidadOperativa() {
		return unidadOperativa;
	}

	public void setUnidadOperativa(String unidadOperativa) {
		this.unidadOperativa = unidadOperativa;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
