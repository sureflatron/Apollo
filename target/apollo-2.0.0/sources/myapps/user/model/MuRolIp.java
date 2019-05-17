package myapps.user.model;/*package com.myapps.user.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name="ROL_IP")
public class MuRolIp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SEQ_ROL_IP_ID_GENERATOR", sequenceName="SEQ_ROL_IP",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ROL_IP_ID_GENERATOR")
	@Column(name="IP_ID")
	private long ipId;

	private boolean estado;

	@Column(name="FECHA_CREACION")
	private Calendar fechaCreacion;

	private String ip;
	private String ipF;

	public String getIpF() {
		return this.ipF;
	}

	public void setIpF(String ipF) {
		this.ipF = ipF;
	}

	//bi-directional many-to-one association to MuRol
	@ManyToOne
	@JoinColumn(name="ROL_ID")
	private MuRol muRol;

	public MuRolIp() {
	}

	public long getIpId() {
		return this.ipId;
	}

	public void setIpId(long ipId) {
		this.ipId = ipId;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Calendar getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public MuRol getMuRol() {
		return this.muRol;
	}

	public void setMuRol(MuRol muRol) {
		this.muRol = muRol;
	}

}*/