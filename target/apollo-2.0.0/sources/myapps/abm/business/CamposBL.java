package myapps.abm.business;


import myapps.abm.dao.CamposDAO;
import myapps.abm.model.CamposEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CamposBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(CamposBL.class);

    @Inject
    private CamposDAO camposDAO;

    public void save(CamposEntity entity) {
        try {
            entity.setEstado(true);
            camposDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(CamposEntity entity) {
        try {
            entity.setEstado(true);
            camposDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(CamposEntity entity) {
        try {
            entity.setEstado(false);
            camposDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

     public List<CamposEntity> listAll() {
        List<CamposEntity> list = new ArrayList<>();
        try {
            list = camposDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<CamposEntity> listAllidConfiguraciones(long idConfiguraciones) {
        List<CamposEntity> list = new ArrayList<>();
        try {
            list = camposDAO.listAllidConfiguraciones(idConfiguraciones);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }


    public CamposEntity getById(long id) {
        CamposEntity camposEntity = new CamposEntity();
        try {
            camposEntity = camposDAO.getById(id);
            return camposEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return camposEntity;
    }


}
