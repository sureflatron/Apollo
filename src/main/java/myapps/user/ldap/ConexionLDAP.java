package myapps.user.ldap;

import java.io.Serializable;

public class ConexionLDAP implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long code;
	private String descripcion;
	private String dominio;
	private String usuario;
	private String clave;
	private String url;
	private boolean activo;

	public ConexionLDAP() {
	}

	public ConexionLDAP(String descripcion, String usuario, String clave, String url, boolean activo) {
		this.descripcion = descripcion;
		this.usuario = usuario;
		this.clave = clave;
		this.url = url;
		this.activo = activo;
	}

	public ConexionLDAP(String descripcion, String dominio, String usuario, String clave, String url, boolean activo) {
		this.descripcion = descripcion;
		this.dominio = dominio;
		this.usuario = usuario;
		this.clave = clave;
		this.url = url;
		this.activo = activo;
	}

	public Long getCode() {
		return this.code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDominio() {
		return this.dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}