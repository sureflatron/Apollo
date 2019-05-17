package myapps.servicio_basico.util;

import javax.faces.context.FacesContext;
import java.io.Serializable;

public class SessionUtil implements Serializable{
	  private static final long serialVersionUID = 1L;
	  
    public static void add(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key,value);
    }

    public static Object get(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }
}
