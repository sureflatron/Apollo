package myapps.abm.business;


import myapps.abm.dao.ConstructorDAO;
import myapps.abm.model.ConstructorEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ConstructorBL implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ConstructorBL.class);

    @Inject
    private ConstructorDAO constructorDAO;
    public void save(ConstructorEntity entity){
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            constructorDAO.save(entity);
        } catch (Exception e){
            logger.log(Level.ERROR, "SAVE"+"|"+ e.getMessage(),e);
        }
    }

    public void update(ConstructorEntity entity){
        try{
            constructorDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR, "UPDATE"+"|"+ e.getMessage(),e);
        }
    }

    public void delete(ConstructorEntity entity){
        try{
            entity.setEstado(false);
            constructorDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR,"DELETE"+"|" +e.getMessage(),e);
        }
    }

    public List<ConstructorEntity> listAll(){
        List<ConstructorEntity> list= new ArrayList<>();
        try{
            list =constructorDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }
    public ConstructorEntity getById(long id){
        ConstructorEntity constructorEntity = new ConstructorEntity();
        try{
            constructorEntity = constructorDAO.getById(id);
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return constructorEntity;
    }

    public ConstructorEntity getByNombre(String nomre) {
        ConstructorEntity constructorEntity = new ConstructorEntity();
        try {
            constructorEntity = constructorDAO.getByNombre(nomre.toUpperCase());
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return constructorEntity;
    }
}
