package myapps.user.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the MU_GRUPO_AD database table.
 * 
 */
@Entity
@Table(name = "GRUPO_AD")
public class MuGrupoAd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_GRUPO_AD_ID_GENERATOR", sequenceName = "SEQ_GRUPO_AD", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GRUPO_AD_ID_GENERATOR")
	@Column(name = "GRUPO_ID")
	private long grupoId;

	private String detalle;

	private boolean estado;

	private String nombre;

	// uni-directional many-to-one association to MuRol
	@ManyToOne
	@JoinColumn(name = "ROL_ID")
	private MuRol muRol;

	public MuGrupoAd() {
	}

	public long getGrupoId() {
		return this.grupoId;
	}

	public void setGrupoId(long grupoId) {
		this.grupoId = grupoId;
	}

	public String getDetalle() {
		return this.detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public MuRol getMuRol() {
		return this.muRol;
	}

	public void setMuRol(MuRol muRol) {
		this.muRol = muRol;
	}

}