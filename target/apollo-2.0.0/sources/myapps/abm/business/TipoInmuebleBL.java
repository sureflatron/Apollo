package myapps.abm.business;


import myapps.abm.dao.TipoInmuebleDAO;
import myapps.abm.model.TipoInmuebleEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TipoInmuebleBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(TipoInmuebleBL.class);

    @Inject
    private TipoInmuebleDAO tipoInmuebleDAO;

    public void save(TipoInmuebleEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            tipoInmuebleDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(TipoInmuebleEntity entity) {
        try {
            tipoInmuebleDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(TipoInmuebleEntity entity) {
        try {
            entity.setEstado(false);
            tipoInmuebleDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<TipoInmuebleEntity> listAll() {
        List<TipoInmuebleEntity> list = new ArrayList<>();
        try {
            list = tipoInmuebleDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }


    public List<TipoInmuebleEntity> findAll() {
        List<TipoInmuebleEntity> list = new ArrayList<>();
        try {
            list = tipoInmuebleDAO.findAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public TipoInmuebleEntity getById(long id) {

        TipoInmuebleEntity tipoInmuebleEntity = new TipoInmuebleEntity();
        try {
            tipoInmuebleEntity = tipoInmuebleDAO.getById(id);
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return tipoInmuebleEntity;
    }

    public TipoInmuebleEntity getByNombre(String nombre) {

        TipoInmuebleEntity tipoInmuebleEntity = new TipoInmuebleEntity();
        try {
            tipoInmuebleEntity = tipoInmuebleDAO.getByNombre(nombre.toUpperCase());
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_NOMBRE" + "|" + e.getMessage(), e);
        }
        return tipoInmuebleEntity;
    }


}
