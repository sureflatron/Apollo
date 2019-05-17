package myapps.abm.dao;

import myapps.abm.model.CategoriaEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class CategoriaDAO implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@PersistenceContext(unitName = "servBasicoDS")
	private transient EntityManager entityManager;
	
    @SuppressWarnings("unchecked")
    public List<CategoriaEntity> listAll(){
        String consulta = "SELECT p FROM CategoriaEntity p WHERE p.estado=1 order by p.id desc";
        Query qu = entityManager.createQuery(consulta);
        return (List<CategoriaEntity>) qu.getResultList();
    }
    

}
