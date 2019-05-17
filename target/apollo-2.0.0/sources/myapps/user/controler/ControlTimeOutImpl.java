
package myapps.user.controler;

import myapps.user.business.BitacoraBL;
import myapps.user.business.NodoBL;
import myapps.user.model.MuBitacora;
import myapps.user.model.Nodo;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@Stateless
public class ControlTimeOutImpl implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(ControlTimeOutImpl.class);

	@Inject
	private BitacoraBL logBL;

	@Inject
	private NodoBL nodoBL;
	
	
	public ControlTimeOutImpl() {

	}

	public Nodo getNodoClient2(String user) {
		try {
			return nodoBL.getNodoLogin(user);
		} catch (Exception e) {
			log.error("Error al obtener nodo cliente: ", e);
		}
		return null;
	}

	public void setDatos2(Nodo nodo, long time) {
		nodo.setTime(time);
		try {
			nodoBL.update(nodo);
		} catch (Exception e) {
			log.error("Error al actualizar nodo cliente: ", e);
		}
	}

	public String getAddressIP2(String user) {
		try {
			Nodo nodo = nodoBL.getNodoLogin(user);
			if (nodo != null) {
				if (nodo.getIp() != null)
					return nodo.getIp();
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return "";
	}

	public void registerOutTime(long timeMax) {
		Date d = new Date();
		long tp = d.getTime();
		try {
			List<Nodo> list = nodoBL.getList();
			if (list != null) {
				for (Nodo nodo : list) {

					long value = nodo.getTime();
				    
					if ((tp - value) > timeMax) {
						log.debug("TIEMPO NODO: " + nodo.getUsuario() + " - " + nodo.getTime());
						MuBitacora logg = new MuBitacora();
						logg.setFormulario("");
						logg.setAccion("Salio del Sistema por expiracion de tiempo");
						logg.setUsuario(nodo.getUsuario());
						logg.setFecha(new Timestamp(value + timeMax));
						logg.setDireccionIp(nodo.getIp());
						logBL.save(logg);
						nodoBL.remove(nodo);
					}
				}
			}
		} catch (Exception e) {
			log.error("Error al eliminar clientes con timeout: ", e);
		}
	}

	public Nodo exisUserIngreso2(String user) {
		try {
			return nodoBL.getNodoLogin(user);
		} catch (Exception e) {
			log.error("Error al obtener nodo cliente: ", e);
		}
		return null;
	}


	public void insertUserIngreso2(String user, String ip) throws Exception {
		Nodo nodo = new Nodo();
		nodo.setFecha(Calendar.getInstance());
		nodo.setCount(1);
		nodo.setUsuario(user);
		nodo.setIp(ip);
		nodo.setTime(new Date().getTime());
		nodoBL.save(nodo);
	}

}