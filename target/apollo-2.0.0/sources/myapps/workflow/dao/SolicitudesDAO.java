package myapps.workflow.dao;

import myapps.workflow.entity.SolicitudesEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class SolicitudesDAO implements Serializable {
    private static final long serialVersionUID = 1L;
   
    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(SolicitudesEntity entity) {
        entityManager.persist(entity);
    }

    public void update(SolicitudesEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<SolicitudesEntity> listAll() {
        String consulta = "SELECT r FROM SolicitudesEntity r WHERE  r.estado = true Order By r.idSolicitud Desc";
        Query qu = entityManager.createQuery(consulta);
        return (List<SolicitudesEntity>) qu.getResultList();

    }

    public SolicitudesEntity getById(long id) {
        return entityManager.find(SolicitudesEntity.class, id);
    }

}