package myapps.servicio_basico.bean.model;

import java.io.Serializable;

public class Nota implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String value;

	public Nota() {
		super();
	}

	public Nota(long id, String name, String value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Nota [id=" + id + ", name=" + name + ", value=" + value + "]";
	}

}
