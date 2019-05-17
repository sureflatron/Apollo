package myapps.user.dao;

import myapps.user.model.Parametro;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ParametrosDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName = "servBasicoDS")
	private transient EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Parametro> listParam(String parametro)throws Exception{
		String consulta = "SELECT p FROM Parametro p WHERE p.tipo=:parametro ORDER BY p.orden ASC";
		Query qu = entityManager.createQuery(consulta).setParameter("parametro", parametro);
		List<Parametro> lista = qu.getResultList();
		return lista;
	}
	public void update(List<Parametro> parametros ) {
		for(Parametro param: parametros) {
			entityManager.merge(param);
		}
	}
	

}
