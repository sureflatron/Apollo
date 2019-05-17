package myapps.abm.dao;


import myapps.abm.model.HistorialUsuarioEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class HistorialUsuarioDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(HistorialUsuarioEntity entity) {
        entityManager.persist(entity);
    }

    public void update(HistorialUsuarioEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<HistorialUsuarioEntity> listAll() {
        String consulta = "SELECT r FROM HistorialUsuarioEntity r";
        Query qu = entityManager.createQuery(consulta);
        return (List<HistorialUsuarioEntity>) qu.getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<HistorialUsuarioEntity> listAllID(long id) {
        String consulta = "select r from HistorialUsuarioEntity r where r.muUsuarioByUsuarioId.usuarioId=" + id+ "Order by r.fecha desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<HistorialUsuarioEntity>) qu.getResultList();
    }

    public HistorialUsuarioEntity getById(long id) {
        return entityManager.find(HistorialUsuarioEntity.class, id);
    }
}