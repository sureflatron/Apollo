package myapps.abm.business;


import myapps.abm.dao.UODAO;
import myapps.abm.model.UnidadOperativaEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UOBL implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(UOBL.class);

    @Inject
    private UODAO uoDAO;
    public void save(UnidadOperativaEntity entity){
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            uoDAO.save(entity);
        } catch (Exception e){
            logger.log(Level.ERROR, "SAVE"+"|"+ e.getMessage(),e);
        }
    }

    public void update(UnidadOperativaEntity entity){
        try{
            uoDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR, "UPDATE"+"|"+ e.getMessage(),e);
        }
    }

    public void delete(UnidadOperativaEntity entity){
        try{
            entity.setEstado(false);
            uoDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR,"DELETE"+"|" +e.getMessage(),e);
        }
    }

    public List<UnidadOperativaEntity> listAll(){
        List<UnidadOperativaEntity> list= new ArrayList<>();
        try{
            list =uoDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }
    public UnidadOperativaEntity getById(long id){
        UnidadOperativaEntity unidadOperativaEntity = new UnidadOperativaEntity();
        try{
            unidadOperativaEntity = uoDAO.getById(id);
            return unidadOperativaEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return unidadOperativaEntity;
    }
    public UnidadOperativaEntity getUOperativaByNombre(String nombre){
        UnidadOperativaEntity unidadOperativaEntity= new UnidadOperativaEntity();
        try{
            if (!nombre.equals("")){
                unidadOperativaEntity= uoDAO.getUOperativaByNombre(nombre.toUpperCase());
            }
        }catch (Exception e){
            logger.log(Level.ERROR,"buscarBanco - BL"+"|" +e.getMessage(),e);
        }
        return unidadOperativaEntity;
    }

}
