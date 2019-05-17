package myapps.abm.business;

import myapps.abm.dao.InmuServDAO;
import myapps.abm.model.InmServEntity;
import myapps.abm.model.InmuebleEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class InmServBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(InmServBL.class);

    @Inject
    private InmuServDAO inmuServDAO;
    @Inject
    private InmuebleBL inmuebleBL;

    public void save(InmServEntity entity){
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado((long) 1);
            inmuServDAO.save(entity);
            if(getByIdInmueble(entity.getInmuebleByIdInmueble().getIdInmueble())!=null){
                System.out.println("ENVIAR NOTIFICACION POR EMAIL");
            }
        } catch (Exception e){
            logger.log(Level.ERROR, "SAVE"+"|"+ e.getMessage(),e);
        }
    }

    public void update(InmServEntity entity){
        try{
            inmuServDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR, "UPDATE"+"|"+ e.getMessage(),e);
        }
    }

    public void delete(InmServEntity entity){
        try{
            entity.setEstado((long) 0);
            inmuServDAO.update(entity);
        }catch (Exception e){
            logger.log(Level.ERROR,"DELETE"+"|" +e.getMessage(),e);
        }
    }

    public List<InmServEntity> listAll(){
        List<InmServEntity> list= new ArrayList<>();
        try{
            list =inmuServDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }
    public InmServEntity getById(long id){
        InmServEntity inmServEntity = new InmServEntity();
        try{
            inmServEntity = inmuServDAO.getById(id);
            return inmServEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return inmServEntity;
    }
    public InmServEntity getByIdServicio(long idServicio){
        InmServEntity inmServEntity = new InmServEntity();
        try{
            inmServEntity = inmuServDAO.getByIdServicio(idServicio);
            InmuebleEntity inmuebleEntity = inmuebleBL.getById(inmServEntity.getInmuebleByIdInmueble()
                    .getIdInmueble());
            inmServEntity.setInmuebleByIdInmueble(inmuebleEntity);
            return inmServEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return inmServEntity;
    }
    public InmServEntity getByIdInmueble(long idInmueuble){
        InmServEntity inmServEntity = new InmServEntity();
        try{
            inmServEntity = inmuServDAO.getByIdInmueble(idInmueuble);
            return inmServEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return inmServEntity;
    }
}
