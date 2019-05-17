package myapps.user.bean;

import myapps.servicio_basico.util.UtilUrl;
import myapps.user.business.BitacoraBL;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@Stateless
public class ControlerBitacora implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	BitacoraBL logBl;

	@SuppressWarnings("rawtypes")
	public void insert(Enum dato, String id, String name) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");		
		String remoteAddr = UtilUrl.getClientIp(request);
		// remoteAddr = remoteAddr.replaceAll("\\.", "_");
		logBl.accionInsert(strIdUs, remoteAddr, dato, id, name);

	}

	@SuppressWarnings("rawtypes")
	public void update(Enum dato, String id, String name) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");		
		// remoteAddr = remoteAddr.replaceAll("\\.", "_");
		String remoteAddr = UtilUrl.getClientIp(request);
		logBl.accionUpdate(strIdUs, remoteAddr, dato, id, name);

	}

	@SuppressWarnings("rawtypes")
	public void delete(Enum dato, String id, String name) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");		
		String remoteAddr = UtilUrl.getClientIp(request);
		logBl.accionDelete(strIdUs, remoteAddr, dato, id, name);

	}

	@SuppressWarnings("rawtypes")
	public void find(Enum dato, String id, String name) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");		
		String remoteAddr = UtilUrl.getClientIp(request);
		// remoteAddr = remoteAddr.replaceAll("\\.", "_");
		logBl.accionFind(strIdUs, remoteAddr, dato, id, name);

	}

	@SuppressWarnings("rawtypes")
	public void cortar(Enum dato, String id) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");		
		String remoteAddr = UtilUrl.getClientIp(request);
		// remoteAddr = remoteAddr.replaceAll("\\.", "_");
		logBl.accionCortar(strIdUs, remoteAddr, dato, id);

	}

	@SuppressWarnings("rawtypes")
	public void reconectar(Enum dato, String id) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");		
		String remoteAddr = UtilUrl.getClientIp(request);
		// remoteAddr = remoteAddr.replaceAll("\\.", "_");
		logBl.accionReconectar(strIdUs, remoteAddr, dato, id);

	}

	@SuppressWarnings("rawtypes")
	public void accion(Enum dato, String accion) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");		
		String remoteAddr = UtilUrl.getClientIp(request);
		// remoteAddr = remoteAddr.replaceAll("\\.", "_");
		logBl.accion(strIdUs, remoteAddr, dato, accion);
	}

}
