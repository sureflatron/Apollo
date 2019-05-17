package myapps.user.dao;

import myapps.user.model.MuUsuario;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;


@Named
@Stateless
public class UsuarioDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "servBasicoDS")
	private transient EntityManager entityManager;

	public void save(MuUsuario dato) {
		entityManager.persist(dato);
	}

	public MuUsuario get(long id)  {
		return entityManager.find(MuUsuario.class, id);
	}

	public void update(MuUsuario dato) {
		entityManager.merge(dato);
	}

	@SuppressWarnings("unchecked")
	public List<MuUsuario> getList() {
		return entityManager.createQuery("SELECT us FROM MuUsuario us WHERE us.estado=true Order by us.usuarioId desc").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuUsuario> getListNA() throws Exception {
		return entityManager.createQuery("SELECT us FROM MuUsuario us WHERE us.estado=false Order by us.usuarioId desc").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuUsuario> getList2() {
		return entityManager.createQuery("SELECT us FROM MuUsuario us WHERE us.estado=true Order by us.usuarioId desc ").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuUsuario> getList(long idRol) throws Exception {

		String consulta = "SELECT us FROM MuUsuario us WHERE us.rolByRolId.rolId = :idRol and us.rolByRolId.estado = true and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("idRol", idRol);
		List<MuUsuario> lista = qu.getResultList();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public MuUsuario getUsuarioLogin(String login) {
		login = login.toLowerCase();
		String consulta = "SELECT us FROM MuUsuario us WHERE LOWER(us.login) = :login";
		Query qu = entityManager.createQuery(consulta).setParameter("login", login);
		List<MuUsuario> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);
	}

}
