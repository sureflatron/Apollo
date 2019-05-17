package myapps.abm.business;


import myapps.abm.dao.MedidorDAO;
import myapps.abm.model.MedidorEntity;
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
public class MedidorBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(MedidorBL.class);

    @Inject
    private MedidorDAO medidorDAO;

    public void save(MedidorEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            entity.setFechaCreacion(new Date());
            medidorDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(MedidorEntity entity) {
        try {
            medidorDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(MedidorEntity entity) {
        try {

            medidorDAO.eliminar(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<MedidorEntity> listAll() {
        List<MedidorEntity> list = new ArrayList<>();
        try {
            list = medidorDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }


    public MedidorEntity repetidos(long idServicio) {
        MedidorEntity list = new MedidorEntity();
        try {
            list = medidorDAO.repetidos(idServicio);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }


    public MedidorEntity getById(long id) {
        MedidorEntity medidorEntity = new MedidorEntity();
        try {
            medidorEntity = medidorDAO.getById(id);
            return medidorEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return medidorEntity;
    }


}
