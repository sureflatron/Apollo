package myapps.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the MU_FORMULARIO database table.
 * 
 */
@Entity
@Table(name = "FORMULARIO")
public class MuFormulario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private boolean estado;

	@Column(name = "FORMULARIO_ID")
	private Long formularioId;

	private String nombre;

	private int orden;

	private String url;

	public MuFormulario() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Long getFormularioId() {
		return this.formularioId;
	}

	public void setFormularioId(Long formularioId) {
		this.formularioId = formularioId;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}