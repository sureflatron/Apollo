package myapps.workflow.business;

import myapps.workflow.dao.SolicitudesDAO;
import myapps.workflow.entity.SolicitudesEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SolicitudesBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(SolicitudesBL.class);

    @Inject
    private SolicitudesDAO solicitudesDAO;

    public void save(SolicitudesEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            solicitudesDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(SolicitudesEntity entity) {
        try {
            solicitudesDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(SolicitudesEntity entity) {
        try {
            entity.setEstado(false);
            solicitudesDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<SolicitudesEntity> listAll() {
        List<SolicitudesEntity> list = new ArrayList<>();
        try {
            list = solicitudesDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public SolicitudesEntity getById(long id) {
        SolicitudesEntity SolicitudesEntity = new SolicitudesEntity();
        try {
            SolicitudesEntity = solicitudesDAO.getById(id);
            return SolicitudesEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return SolicitudesEntity;
    }
}
