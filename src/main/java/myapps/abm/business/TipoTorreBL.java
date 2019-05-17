package myapps.abm.business;

import myapps.abm.dao.TipoTorreDAO;
import myapps.abm.model.TipoTorreEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TipoTorreBL implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(TipoTorreBL.class);

	@Inject
	private TipoTorreDAO tipoTorreDAO;

	public void save(TipoTorreEntity entity) {
		logger.log(Level.INFO, "SAVE");
		try {

			entity.setEstado(true);

			tipoTorreDAO.save(entity);
		} catch (Exception e) {
			logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
		}
	}

	public void update(TipoTorreEntity entity) {
		try {
			tipoTorreDAO.update(entity);
		} catch (Exception e) {
			logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
		}
	}

	public void delete(TipoTorreEntity entity) {
		try {
			entity.setEstado(false);
			tipoTorreDAO.update(entity);
		} catch (Exception e) {
			logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
		}
	}

	public List<TipoTorreEntity> listAll() {
		List<TipoTorreEntity> list = new ArrayList<>();
		try {
			list = tipoTorreDAO.listAll();
			return list;
		} catch (Exception e) {
			logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
		}
		return list;
	}

	public TipoTorreEntity getById(long id) {
		TipoTorreEntity tipoTorreEntity = new TipoTorreEntity();
		try {
			return tipoTorreDAO.getById(id);
		} catch (Exception e) {
			logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
		}
		return tipoTorreEntity;
	}

	public TipoTorreEntity getByNombre(String nombre) {

		TipoTorreEntity tipoTorreEntity = new TipoTorreEntity();
		try {
			tipoTorreEntity = tipoTorreDAO.getByNombre(nombre.toUpperCase());
		} catch (Exception e) {
			logger.log(Level.ERROR, "GET_BY_NOMBRE" + "|" + e.getMessage(), e);
		}
		return tipoTorreEntity;
	}
}
