package myapps.abm.dao;

import myapps.abm.model.DepartamentoEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class DepartamentoDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(DepartamentoEntity entity) {
        entityManager.persist(entity);
    }

    public void update(DepartamentoEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<DepartamentoEntity> listAll() {
        String consulta = "SELECT b FROM DepartamentoEntity b";
        Query qu = entityManager.createQuery(consulta);
        return (List<DepartamentoEntity>) qu.getResultList();
    }

    public DepartamentoEntity getById(long id) {
        return entityManager.find(DepartamentoEntity.class, id);
    }
}
