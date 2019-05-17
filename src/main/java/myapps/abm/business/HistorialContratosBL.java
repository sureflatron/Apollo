package myapps.abm.business;


import myapps.abm.dao.HistorialContratosDAO;
import myapps.abm.model.HistorialContratosEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class HistorialContratosBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(HistorialContratosBL.class);

    @Inject
    private HistorialContratosDAO historialContratosDAO;


    public void save(HistorialContratosEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            historialContratosDAO.save(entity);

        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(HistorialContratosEntity entity) {
        try {
            historialContratosDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(HistorialContratosEntity entity) {
        try {

            historialContratosDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<HistorialContratosEntity> listAll() {
        List<HistorialContratosEntity> entityList = new ArrayList<>();
        try {
            entityList = historialContratosDAO.listAll();

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return entityList;
    }

    public HistorialContratosEntity getById(long id) {
        HistorialContratosEntity entity= new HistorialContratosEntity();
        try {
            entity = historialContratosDAO.getById(id);
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return entity;
    }



}
