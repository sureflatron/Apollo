package myapps.servicio_basico.util;

import java.io.Serializable;

public class ResumenCarga implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombreArchivo;
	private String nroColumn;
	private String nroFilas;
	private String nroRegistrosValidos;
	private String nroRegistrosNoValidos;
	private String nroRegistroExitoso;
	private String nroRegistroNoExistos;

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getNroColumn() {
		return nroColumn;
	}

	public void setNroColumn(String nroColumn) {
		this.nroColumn = nroColumn;
	}

	public String getNroFilas() {
		return nroFilas;
	}

	public void setNroFilas(String nroFilas) {
		this.nroFilas = nroFilas;
	}

	public String getNroRegistrosValidos() {
		return nroRegistrosValidos;
	}

	public void setNroRegistrosValidos(String nroRegistrosValidos) {
		this.nroRegistrosValidos = nroRegistrosValidos;
	}

	public String getNroRegistrosNoValidos() {
		return nroRegistrosNoValidos;
	}

	public void setNroRegistrosNoValidos(String nroRegistrosNoValidos) {
		this.nroRegistrosNoValidos = nroRegistrosNoValidos;
	}

	public String getNroRegistroExitoso() {
		return nroRegistroExitoso;
	}

	public void setNroRegistroExitoso(String nroRegistroExitoso) {
		this.nroRegistroExitoso = nroRegistroExitoso;
	}

	public String getNroRegistroNoExistos() {
		return nroRegistroNoExistos;
	}

	public void setNroRegistroNoExistos(String nroRegistroNoExistos) {
		this.nroRegistroNoExistos = nroRegistroNoExistos;
	}
}
