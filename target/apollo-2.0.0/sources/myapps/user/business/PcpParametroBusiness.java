package myapps.user.business;

import myapps.servicio_basico.appContext.IPcpParametroBusiness;
import myapps.user.dao.ParametrosDAO;
import myapps.user.model.Parametro;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;

@Stateless
public class PcpParametroBusiness implements IPcpParametroBusiness {
	public static final Logger log = Logger.getLogger(PcpParametroBusiness.class);
	@EJB
	private ParametrosDAO parametroDao;
	
	public List<Parametro> list(String parametro)throws Exception {		
		return  parametroDao.listParam(parametro);			
	}

	@Override
	public HashMap<String, String> listMap(String typeParam) {
		List<Parametro> parametros;
		try {
			parametros = parametroDao.listParam(typeParam);
			HashMap<String,String> parametrosMap=new HashMap<>();
			for (Parametro param: parametros){
				parametrosMap.put(param.getClave(),param.getValor());
			}
			return  parametrosMap;
		} catch (Exception e) {
		log.error(e.getMessage());
		}

		return null;
	}

	public void update(List<Parametro> parametros) {
			parametroDao.update(parametros);
	}
}
