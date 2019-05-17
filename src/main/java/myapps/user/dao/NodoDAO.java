package myapps.user.dao;

import myapps.user.model.Nodo;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Named
@Stateless
public class NodoDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "servBasicoDS")
	private transient EntityManager entityManager;

	public void save(Nodo dato) throws Exception {
		entityManager.persist(dato);
	}

	public Nodo get(long id) throws Exception {
		return entityManager.find(Nodo.class, id);
	}

	public void update(Nodo dato) throws Exception {	
		entityManager.joinTransaction();
		try {
			entityManager.merge(dato);
		} catch (Exception e) {
			throw e;
		}
	}

	public void remove(Nodo dato) throws Exception {				
		entityManager.joinTransaction();
		try {
			entityManager.remove(entityManager.contains(dato) ? dato : entityManager.merge(dato));
		} catch (Exception e) {			
			throw e;		
		}
	}

	@SuppressWarnings("unchecked")
	public List<Nodo> getList() throws Exception {
		return entityManager.createQuery("SELECT us FROM Nodo us").getResultList();
	}

	@SuppressWarnings("unchecked")
	public Nodo getNodoLogin(String login) throws Exception {
		String consulta = "SELECT us FROM Nodo us WHERE us.usuario = :login";
		Query qu = entityManager.createQuery(consulta).setParameter("login", login);
		List<Nodo> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
