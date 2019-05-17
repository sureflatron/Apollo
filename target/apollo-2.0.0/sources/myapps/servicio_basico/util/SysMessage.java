package myapps.servicio_basico.util;

import org.primefaces.event.FileUploadEvent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class SysMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public static void error(String title, String msg, String id) {
		FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, msg));
	}

	public static void error(String msg, String id) {
		FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", msg));
	}

	public static void info(String title, String msg, String id) {
		FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_INFO, title, msg));
	}

	public static void info(String msg, String id) {
		FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", msg));
	}

	public static void warn(String title, String msg, String id) {
		FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_WARN, title, msg));

	}

	public static void warn(String msg, String id) {
		FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN", msg));

	}

	public static void fatal(String title, String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, title, msg));

	}

	public static void fatal(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "FATAL", msg));

	}

	public static void addMessage(FacesMessage facesMessage) {
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}
	public static void handleFileUpload(FileUploadEvent event) {

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("INFO.", event
				.getFile().getFileName() + " Archivos cargado exitosamente. "));
	}
}
