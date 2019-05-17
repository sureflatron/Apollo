package myapps.abm.dao;

import myapps.abm.model.CamposEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class CamposDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(CamposEntity entity) {
        entityManager.persist(entity);
    }

    public void update(CamposEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<CamposEntity> listAll() {

        String consulta = "SELECT r FROM CamposEntity r where r.estado=true   Order By r.id";
        Query qu = entityManager.createQuery(consulta);
        return (List<CamposEntity>) qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<CamposEntity> listAllidConfiguraciones(long idConfiguraciones) {

        String consulta = "SELECT r FROM CamposEntity r where r.estado=true and r.configuracionesByIdConfiguraciones.idConfiguraciones=" + idConfiguraciones + " Order By r.numero";
        Query qu = entityManager.createQuery(consulta);
        return (List<CamposEntity>) qu.getResultList();

    }

    public CamposEntity getById(long id) {
        return entityManager.find(CamposEntity.class, id);
    }
}