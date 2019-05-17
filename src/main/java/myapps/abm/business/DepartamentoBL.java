package myapps.abm.business;

import myapps.abm.dao.DepartamentoDAO;
import myapps.abm.model.DepartamentoEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DepartamentoBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(DepartamentoBL.class);

    @Inject
    private DepartamentoDAO departamentoDAO;

    public List<DepartamentoEntity> listAll(){
        List<DepartamentoEntity> list= new ArrayList<>();
        try{
            list =departamentoDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }

}
