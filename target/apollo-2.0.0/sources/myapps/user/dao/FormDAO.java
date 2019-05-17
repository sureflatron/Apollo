package myapps.user.dao;

import myapps.user.model.MuFormulario;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Named
@Stateless
public class FormDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "servBasicoDS")
	private transient EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<MuFormulario> getList() {
		return entityManager.createQuery("SELECT us FROM MuFormulario us").getResultList();
	}
}
