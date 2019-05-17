package myapps.abm.dao;

import myapps.abm.model.LicAmbEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class LicAmbDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(LicAmbEntity entity) {
        entityManager.persist(entity);
    }

    public void update(LicAmbEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<LicAmbEntity> listAll() {
        String consulta = "SELECT b FROM LicAmbEntity b WHERE b.estado=1 order by b.idLicAmb desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<LicAmbEntity>) qu.getResultList();
    }

    public LicAmbEntity getById(long id) {
        return entityManager.find(LicAmbEntity.class, id);
    }
}
