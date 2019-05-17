package myapps.abm.dao;


import myapps.abm.model.AlarmaEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class AlarmaDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(AlarmaEntity entity) {
        entityManager.persist(entity);
    }

    public void update(AlarmaEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<AlarmaEntity> listAll(String tipo) {

        String consulta = "SELECT r FROM AlarmaEntity r WHERE  r.estado = true  and r.tipo=:TIPO Order By r.idAlarma desc";
        Query qu = entityManager.createQuery(consulta);
        qu.setParameter("TIPO", tipo);
        return (List<AlarmaEntity>) qu.getResultList();

    }

    public AlarmaEntity getById(long id) {
        return entityManager.find(AlarmaEntity.class, id);
    }
}