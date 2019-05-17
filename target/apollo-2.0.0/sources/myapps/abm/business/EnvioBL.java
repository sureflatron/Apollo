package myapps.abm.business;


import myapps.abm.dao.EnvioDAO;
import myapps.abm.model.EnvioEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class EnvioBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ConstructorBL.class);

    @Inject
    private EnvioDAO envioDAO;

    public void save(EnvioEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            envioDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(EnvioEntity entity) {
        try {
            envioDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(EnvioEntity entity) {
        try {
            entity.setEstado(false);
            envioDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<EnvioEntity> listAll() {
        List<EnvioEntity> list = new ArrayList<>();
        try {
            list = envioDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public EnvioEntity getById(long id) {
        EnvioEntity envioEntity = new EnvioEntity();
        try {
            envioEntity = envioDAO.getById(id);
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return envioEntity;
    }

    public List<EnvioEntity> getByIdAlarma(long id) {
        List<EnvioEntity> envioEntity = new ArrayList<>();
        try {
            envioEntity = envioDAO.listAllByidAlarma(id);
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return envioEntity;
    }


}
