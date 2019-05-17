package myapps.servicio_basico.bean.model;

public class MapaGoole {
	String center;
	String zoom;
	String typeMapa;
	
	
	public MapaGoole(String center, String zoom, String typeMapa) {
		super();
		this.center = center;
		this.zoom = zoom;
		this.typeMapa = typeMapa;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	public String getZoom() {
		return zoom;
	}
	public void setZoom(String zoom) {
		this.zoom = zoom;
	}
	public String getTypeMapa() {
		return typeMapa;
	}
	public void setTypeMapa(String typeMapa) {
		this.typeMapa = typeMapa;
	}
	
	

}
