package myapps.abm.business;

import myapps.abm.dao.RdbsServDAO;
import myapps.abm.model.RdbServEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class RdbsServBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(RdbsServBL.class);

    @Inject
    private RdbsServDAO rdbsServDAO;

    public void save(RdbServEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado((long) 1);
            rdbsServDAO.save(entity);
            /*if(getByIdInmueble(entity.getInmuebleByIdInmueble().getIdInmueble())!=null){
                System.out.println("ENVIAR NOTIFICACION POR EMAIL");
            }*/
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(RdbServEntity entity) {
        try {
            rdbsServDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(RdbServEntity entity) {
        try {
            entity.setEstado((long) 0);
            rdbsServDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<RdbServEntity> listAll() {
        List<RdbServEntity> list = new ArrayList<>();
        try {
            list = rdbsServDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public RdbServEntity getById(long id) {
        RdbServEntity rdbServEntity = new RdbServEntity();
        try {
            rdbServEntity = rdbsServDAO.getById(id);
            return rdbServEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return rdbServEntity;
    }

    public RdbServEntity getByIdServicio(long idServicio) {
        RdbServEntity inmServEntity = new RdbServEntity();
        try {
            inmServEntity = rdbsServDAO.getByIdServicio(idServicio);
            return inmServEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return inmServEntity;
    }
       /* public InmServEntity getByIdInmueble(long idInmueuble){
        InmServEntity inmServEntity = new InmServEntity();
        try{
            inmServEntity = rdbsServDAO.getByIdInmueble(idInmueuble);
            return inmServEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return inmServEntity;
    }*/
}
