package myapps.abm.business;


import myapps.abm.dao.DetalleMedidorDAO;
import myapps.abm.model.DetalleMedidorEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DetalleMedidorBL implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(DetalleMedidorBL.class);

    @Inject
    private DetalleMedidorDAO detalleMedidorDAO;
    public void save(DetalleMedidorEntity entity){
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            detalleMedidorDAO.save(entity);
        } catch (Exception e){
            logger.log(Level.ERROR, "SAVE"+"|"+ e.getMessage(),e);
        }
    }

    public void update(DetalleMedidorEntity entity){
        try{
            detalleMedidorDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR, "UPDATE"+"|"+ e.getMessage(),e);
        }
    }

    public void delete(DetalleMedidorEntity entity){
        try{
            entity.setEstado(false);
            detalleMedidorDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR,"DELETE"+"|" +e.getMessage(),e);
        }
    }

    public List<DetalleMedidorEntity> listAll(){
        List<DetalleMedidorEntity> list= new ArrayList<>();
        try{
            list = detalleMedidorDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }
    public DetalleMedidorEntity getById(long id){
        DetalleMedidorEntity detalleMedidorEntity = new DetalleMedidorEntity();
        try{
            detalleMedidorEntity = detalleMedidorDAO.getById(id);
            return detalleMedidorEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return detalleMedidorEntity;
    }


}
