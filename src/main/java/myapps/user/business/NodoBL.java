package myapps.user.business;

import myapps.user.dao.NodoDAO;
import myapps.user.model.Nodo;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@Stateless
public class NodoBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NodoDAO dao;

	public void save(Nodo nodo) throws Exception {
		dao.save(nodo);
	}

	public Nodo getUser(long idNodo) throws Exception {
		return dao.get(idNodo);
	}

	public void update(Nodo nodo) throws Exception {
		dao.update(nodo);
	}

	public void remove(Nodo nodo) throws Exception {
		dao.remove(nodo);
	}

	public List<Nodo> getList() throws Exception {
		return dao.getList();
	}

	public Nodo getNodoLogin(String login) throws Exception {
		return dao.getNodoLogin(login);
	}
}
