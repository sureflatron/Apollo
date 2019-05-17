package myapps.user.dao;

import myapps.user.model.MuGrupoAd;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Named
@Stateless
public class GrupoAdDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "servBasicoDS")
	private transient EntityManager entityManager;

	public void save(MuGrupoAd dato) throws Exception {					
		entityManager.persist(dato);
	}

	public MuGrupoAd get(long id) {
		return entityManager.find(MuGrupoAd.class, id);
	}

	public void update(MuGrupoAd dato) throws Exception {					
		entityManager.merge(dato);
	}

	@SuppressWarnings("unchecked")
	public List<MuGrupoAd> getList() {
		return entityManager.createQuery("SELECT us FROM MuGrupoAd us WHERE us.estado = true Order By us.grupoId desc ").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuGrupoAd> getList(long idRol) {
		String sql = "SELECT us FROM MuGrupoAd us WHERE us.muRol.rolId = :idRol and us.muRol.estado = true and us.estado=true";
		Query qu = entityManager.createQuery(sql).setParameter("idRol", idRol);
		List<MuGrupoAd> lista = qu.getResultList();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public MuGrupoAd getGroupName(String name) {
		String consulta = "SELECT us FROM MuGrupoAd us WHERE us.nombre = :name and us.estado = true and us.muRol.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("name", name);
		List<MuGrupoAd> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);
	}
}