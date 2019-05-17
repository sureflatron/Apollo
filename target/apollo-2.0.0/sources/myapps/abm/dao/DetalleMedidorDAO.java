package myapps.abm.dao;


import myapps.abm.model.DetalleMedidorEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class DetalleMedidorDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(DetalleMedidorEntity entity) {
        entityManager.persist(entity);
    }

    public void update(DetalleMedidorEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<DetalleMedidorEntity> listAll() {

        String consulta = "SELECT r FROM DetalleMedidorEntity r WHERE  r.estado = true Order By r.idDetalleMedidor desc";
        Query qu = entityManager.createQuery(consulta);
        return (List<DetalleMedidorEntity>) qu.getResultList();

    }

    public DetalleMedidorEntity getById(long id) {
        return entityManager.find(DetalleMedidorEntity.class, id);
    }
}