package myapps.abm.business;


import myapps.abm.dao.AlarmaDAO;
import myapps.abm.model.AlarmaEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AlarmaBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(AlarmaBL.class);

    @Inject
    private AlarmaDAO alarmaDAO;

    public void save(AlarmaEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            alarmaDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(AlarmaEntity entity) {
        try {
            alarmaDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(AlarmaEntity entity) {
        try {
            entity.setEstado(false);
            alarmaDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<AlarmaEntity> listAll(String tipo) {
        List<AlarmaEntity> list = new ArrayList<>();
        try {
            list = alarmaDAO.listAll(tipo);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public AlarmaEntity getById(long id) {
        AlarmaEntity alarmaEntity = new AlarmaEntity();
        try {
            alarmaEntity = alarmaDAO.getById(id);
            return alarmaEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return alarmaEntity;
    }


}
