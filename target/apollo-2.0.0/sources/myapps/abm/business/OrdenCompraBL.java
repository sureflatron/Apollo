package myapps.abm.business;


import myapps.abm.dao.OrdenCompraDAO;
import myapps.abm.model.OrdenCompraEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OrdenCompraBL implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(OrdenCompraBL.class);

    @Inject
    private OrdenCompraDAO ordenCompraDAO;
    public void save(OrdenCompraEntity entity){
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            ordenCompraDAO.save(entity);
        } catch (Exception e){
            logger.log(Level.ERROR, "SAVE"+"|"+ e.getMessage(),e);
        }
    }

    public void update(OrdenCompraEntity entity){
        try{
            ordenCompraDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR, "UPDATE"+"|"+ e.getMessage(),e);
        }
    }

    public void delete(OrdenCompraEntity entity){
        try{
            entity.setEstado(false);
            ordenCompraDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR,"DELETE"+"|" +e.getMessage(),e);
        }
    }

    public List<OrdenCompraEntity> listAll(){
        List<OrdenCompraEntity> list= new ArrayList<>();
        try{
            list = ordenCompraDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }
    public OrdenCompraEntity getById(long id){
        OrdenCompraEntity ordenCompraEntity = new OrdenCompraEntity();
        try{
            ordenCompraEntity = ordenCompraDAO.getById(id);
            return ordenCompraEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return ordenCompraEntity;
    }


}
