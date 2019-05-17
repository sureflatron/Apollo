package myapps.abm.business;


import myapps.abm.dao.PropiedadDAO;
import myapps.abm.model.PropiedadEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PropiedadBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PropiedadBL.class);

    @Inject
    private PropiedadDAO propiedadDAODAO;

    public void save(PropiedadEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            propiedadDAODAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(PropiedadEntity entity) {
        try {
            propiedadDAODAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(PropiedadEntity entity) {
        try {
            entity.setEstado(false);
            propiedadDAODAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<PropiedadEntity> listAll(boolean allPropiedad) {
        List<PropiedadEntity> list = new ArrayList<>();
        try {
            list = propiedadDAODAO.listAll(allPropiedad);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public PropiedadEntity getById(long id) {
        PropiedadEntity propiedadEntity = new PropiedadEntity();
        try {
            propiedadEntity = propiedadDAODAO.getById(id);
            return propiedadEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return propiedadEntity;
    }

    public PropiedadEntity getByNombre(String nomre) {
        PropiedadEntity propiedadEntity = new PropiedadEntity();
        try {
            propiedadEntity = propiedadDAODAO.getByNombre(nomre.toUpperCase());
            return propiedadEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return propiedadEntity;
    }


}
