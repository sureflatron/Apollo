package myapps.abm.business;

import myapps.abm.dao.MonedaDAO;
import myapps.abm.model.MonedaEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class MonedaBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(MonedaBL.class);

    @Inject
    private MonedaDAO monedaDAO;

    public List<MonedaEntity> listAll(){
        List<MonedaEntity> list= new ArrayList<>();
        try{
            list =monedaDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }
    public MonedaEntity getById(long id){
        MonedaEntity servicioEntity = new MonedaEntity();
        try{
            servicioEntity = monedaDAO.getById(id);
            return servicioEntity;
        }catch (Exception e){
            logger.log(Level.ERROR,"GET_BY_ID"+"|" +e.getMessage(),e);
        }
        return servicioEntity;
    }
    public MonedaEntity getMonedaByNombre(String nombre){
        MonedaEntity monedaEntity= new MonedaEntity();
        try{
            if (!nombre.equals("")){
                monedaEntity= monedaDAO.getMonedaByNombre(nombre.toUpperCase());
            }

        }catch (Exception e){
            logger.log(Level.ERROR,"getMonedaByNombre - BL"+"|" +e.getMessage(),e);
        }
        return monedaEntity;
    }
}
