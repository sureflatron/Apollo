package myapps.abm.dao;

import myapps.abm.model.OrdenCompraEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class OrdenCompraDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(OrdenCompraEntity entity) {
        entityManager.persist(entity);
    }

    public void update(OrdenCompraEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<OrdenCompraEntity> listAll() {

        String consulta = "SELECT r FROM OrdenCompraEntity r WHERE  r.estado =true Order By r.idOc desc ";

        Query qu = entityManager.createQuery(consulta);
        return (List<OrdenCompraEntity>) qu.getResultList();

    }

    public OrdenCompraEntity getById(long id) {
        return entityManager.find(OrdenCompraEntity.class, id);
    }
}