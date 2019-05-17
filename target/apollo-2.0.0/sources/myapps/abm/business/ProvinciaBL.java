package myapps.abm.business;

import myapps.abm.dao.ProvinciaDAO;
import myapps.abm.model.ProvinciaEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProvinciaBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ProvinciaBL.class);

    @Inject
    private ProvinciaDAO provinciaDAO;

    public List<ProvinciaEntity> listAll(){
        List<ProvinciaEntity> list= new ArrayList<>();
        try{
            list =provinciaDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }

    public List<ProvinciaEntity> listAllIdDpto( long idDpto){
        List<ProvinciaEntity> list= new ArrayList<>();
        try{
            list =provinciaDAO.listAllIdDpto(idDpto);
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }



}
