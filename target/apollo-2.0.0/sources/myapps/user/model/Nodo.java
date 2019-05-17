package myapps.user.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;


/**
 * The persistent class for the VC_NODO database table.
 * 
 */
@Entity
@Table(name="NODO")
public class Nodo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SEQ_NODO_GENERATOR", sequenceName="SEQ_NODO", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_NODO_GENERATOR")
	@Column(name="ID_NODO", unique=true, nullable=false, precision=19)
	private long idNodo;

	@Column(name="\"COUNT\"", precision=10)
	private Integer count;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fecha;

	@Column(length=20)
	private String ip;

	@Column(name="\"TIME\"", precision=19)
	private Long time;

	@Column(length=4000)
	private String urls;

	@Column(nullable=false, length=20)
	private String usuario;

	public Nodo() {
	}

	public long getIdNodo() {
		return this.idNodo;
	}

	public void setIdNodo(long idNodo) {
		this.idNodo = idNodo;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Calendar getFecha() {
		return this.fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getTime() {
		return this.time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getUrls() {
		return this.urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}