package myapps.abm.business;


import myapps.abm.dao.ConfiguracionesDAO;
import myapps.abm.model.ConfiguracionesEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class ConfiguracionesBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ConfiguracionesBL.class);

    @Inject
    private ConfiguracionesDAO configuracionesDAO;

    public void save(ConfiguracionesEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            configuracionesDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(ConfiguracionesEntity entity) {
        try {
            entity.setEstado(true);
            configuracionesDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(ConfiguracionesEntity entity) {
        try {
            entity.setEstado(false);
            configuracionesDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<ConfiguracionesEntity> listAll() {
        List<ConfiguracionesEntity> list = new ArrayList<>();
        try {
            list = configuracionesDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public ConfiguracionesEntity getById(long id) {
        ConfiguracionesEntity configuracionesEntity = new ConfiguracionesEntity();
        try {
            configuracionesEntity = configuracionesDAO.getById(id);
            return configuracionesEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return configuracionesEntity;
    }

    public ConfiguracionesEntity obtenerConfiguracion(String nombre, String tipo, Date fecha) {
        ConfiguracionesEntity configuracionesEntity = new ConfiguracionesEntity();
        try {
            configuracionesEntity = configuracionesDAO.obtenerConfiguracion(nombre,tipo,fecha);
            return configuracionesEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "obtenerConfiguracion" + "|" + e.getMessage(), e);
        }
        return configuracionesEntity;
    }


}
