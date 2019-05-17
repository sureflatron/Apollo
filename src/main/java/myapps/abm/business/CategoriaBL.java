package myapps.abm.business;

import myapps.abm.dao.CategoriaDAO;
import myapps.abm.model.CategoriaEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CategoriaBL implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(CategoriaBL.class);
	
	@Inject
	private CategoriaDAO categoriaDAO;
	
    public List<CategoriaEntity> listAll(){
        List<CategoriaEntity> list= new ArrayList<>();
        try{
            list =categoriaDAO.listAll();
            return list;
        }catch (Exception e){
            logger.log(Level.ERROR,"SELECT"+"|" +e.getMessage(),e);
        }
        return list;
    }

}
