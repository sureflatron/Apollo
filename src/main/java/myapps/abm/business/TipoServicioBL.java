package myapps.abm.business;


import myapps.abm.dao.TipoServicioDAO;
import myapps.abm.model.TipoServicioEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TipoServicioBL implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(TipoServicioBL.class);

    @Inject
    private TipoServicioDAO tipoServicioDAO;
    public void save(TipoServicioEntity entity){
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            tipoServicioDAO.save(entity);
        } catch (Exception e){
            logger.log(Level.ERROR, "SAVE"+"|"+ e.getMessage(),e);
        }
    }

    public void update(TipoServicioEntity entity){
        try{
            tipoServicioDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR, "UPDATE"+"|"+ e.getMessage(),e);
        }
    }

    public void delete(TipoServicioEntity entity){
        try{
            entity.setEstado(false);
            tipoServicioDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR,"DELETE"+"|" +e.getMessage(),e);
        }
    }

    public List<TipoServicioEntity> listAll(){
        List<TipoServicioEntity> list= new ArrayList<>();
        try{
            list =tipoServicioDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }
    public TipoServicioEntity getById(long id){
        TipoServicioEntity tipoServicioEntity = new TipoServicioEntity();
        try{
            tipoServicioEntity = tipoServicioDAO.getById(id);
            return tipoServicioEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return tipoServicioEntity;
    }
    public TipoServicioEntity getByNombre(String nombre){
        TipoServicioEntity tipoServicioEntity = new TipoServicioEntity();
        try{
            if (!nombre.equals("")){
                tipoServicioEntity = tipoServicioDAO.getByNombre(nombre);
            }

        }catch (Exception e){
            logger.log(Level.ERROR,"getByNombre - BL"+"|" +e.getMessage(),e);
        }
        return tipoServicioEntity;
    }


}
