package myapps.user.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * The persistent class for the PARAMETROS database table.
 * 
 */
@Entity
@Table(name="PARAMETROS")
public class Parametro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PARAMETROS_IDPARAMETROS_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARAMETROS_IDPARAMETROS_GENERATOR")
	@Column(name="ID_PARAMETROS")
	private long idParametros;

	private String clave;

	private String descripcion;

	private BigDecimal orden;

	private String tipo;

	private String valor;

	public Parametro() {
	}

	public long getIdParametros() {
		return this.idParametros;
	}

	public void setIdParametros(long idParametros) {
		this.idParametros = idParametros;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getOrden() {
		return this.orden;
	}

	public void setOrden(BigDecimal orden) {
		this.orden = orden;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}