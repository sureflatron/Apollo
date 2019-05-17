package myapps.abm.business;


import myapps.abm.dao.LocalidadDAO;
import myapps.abm.model.LocalidadEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LocalidadBL implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(LocalidadBL.class);

    @Inject
    private LocalidadDAO localidadDAO;
    public void save(LocalidadEntity entity){
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            localidadDAO.save(entity);
        } catch (Exception e){
            logger.log(Level.ERROR, "SAVE"+"|"+ e.getMessage(),e);
        }
    }

    public void update(LocalidadEntity entity){
        try{
            localidadDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR, "UPDATE"+"|"+ e.getMessage(),e);
        }
    }

    public void delete(LocalidadEntity entity){
        try{
            entity.setEstado(false);
            localidadDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR,"DELETE"+"|" +e.getMessage(),e);
        }
    }

    public List<LocalidadEntity> listAll(){
        List<LocalidadEntity> list= new ArrayList<>();
        try{
            list =localidadDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }
    public LocalidadEntity getById(long id){
        LocalidadEntity localidadEntity = new LocalidadEntity();
        try{
            localidadEntity = localidadDAO.getById(id);
            return localidadEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return localidadEntity;
    }
    public LocalidadEntity getByNombre(String nombre){
        LocalidadEntity localidadEntity = new LocalidadEntity();
        try{
            localidadEntity = localidadDAO.getByNombre(nombre.toUpperCase());
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return localidadEntity;
    }

    public List<LocalidadEntity> listAllIdMunicipio( long idIdMunicipio){
        List<LocalidadEntity> list= new ArrayList<>();
        try{
            list =localidadDAO.listAllIdLocalidad(idIdMunicipio);
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }

}
