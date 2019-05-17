package myapps.user.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the MU_ROL_FORMULARIO database table.
 * 
 */
@Entity
@Table(name = "ROL_FORMULARIO")
public class MuRolFormulario implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MuRolFormularioPK id;

	private boolean estado;

	// uni-directional many-to-one association to MuFormulario
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FORMULARIO_ID", nullable = false, insertable = false, updatable = false)
	private MuFormulario muFormulario;

	// uni-directional many-to-one association to MuRol
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROL_ID", nullable = false, insertable = false, updatable = false)
	private MuRol muRol;

	public MuRolFormulario() {
	}

	public MuRolFormularioPK getId() {
		return this.id;
	}

	public void setId(MuRolFormularioPK id) {
		this.id = id;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public MuFormulario getMuFormulario() {
		return this.muFormulario;
	}

	public void setMuFormulario(MuFormulario muFormulario) {
		this.muFormulario = muFormulario;
	}

	public MuRol getMuRol() {
		return this.muRol;
	}

	public void setMuRol(MuRol muRol) {
		this.muRol = muRol;
	}

}