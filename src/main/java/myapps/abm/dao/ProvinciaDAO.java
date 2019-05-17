package myapps.abm.dao;

import myapps.abm.model.ProvinciaEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ProvinciaDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(ProvinciaEntity entity) {
        entityManager.persist(entity);
    }

    public void update(ProvinciaEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<ProvinciaEntity> listAll() {
        String consulta = "SELECT b FROM ProvinciaEntity b ";
        Query qu = entityManager.createQuery(consulta);
        return (List<ProvinciaEntity>) qu.getResultList();
    }


    @SuppressWarnings("unchecked")
    public List<ProvinciaEntity> listAllIdDpto(long idDpto) {
        String consulta = "SELECT b FROM ProvinciaEntity b where  b.departamentoByIdDepartamento=" + idDpto;
        Query qu = entityManager.createQuery(consulta);
        return (List<ProvinciaEntity>) qu.getResultList();
    }


    public ProvinciaEntity getById(long id) {
        return entityManager.find(ProvinciaEntity.class, id);
    }
}
