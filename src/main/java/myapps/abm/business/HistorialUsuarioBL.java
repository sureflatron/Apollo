package myapps.abm.business;


import myapps.abm.dao.HistorialUsuarioDAO;
import myapps.abm.model.HistorialUsuarioEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class HistorialUsuarioBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(HistorialUsuarioBL.class);

    @Inject
    private HistorialUsuarioDAO historialUsuarioDAO;

    public void save(HistorialUsuarioEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            historialUsuarioDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(HistorialUsuarioEntity entity) {
        try {
            historialUsuarioDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(HistorialUsuarioEntity entity) {
        try {

            historialUsuarioDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<HistorialUsuarioEntity> listAll() {
        List<HistorialUsuarioEntity> list = new ArrayList<>();
        try {
            list = historialUsuarioDAO.listAll();
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<HistorialUsuarioEntity> listAllId(long id) {
        List<HistorialUsuarioEntity> list = new ArrayList<>();
        try {
            list = historialUsuarioDAO.listAllID(id);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }


    public HistorialUsuarioEntity getById(long id) {
        HistorialUsuarioEntity historialUsuarioEntity = new HistorialUsuarioEntity();
        try {
            historialUsuarioEntity = historialUsuarioDAO.getById(id);
            return historialUsuarioEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return historialUsuarioEntity;
    }


}
