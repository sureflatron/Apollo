package myapps.abm.dao;


import myapps.abm.model.EnvioEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class EnvioDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(EnvioEntity entity) {
        entityManager.persist(entity);
    }

    public void update(EnvioEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<EnvioEntity> listAll() {

        String consulta = "SELECT r FROM EnvioEntity r WHERE  r.estado = true Order By r.idEnvio desc";

        Query qu = entityManager.createQuery(consulta);
        return (List<EnvioEntity>) qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<EnvioEntity> listAllByidAlarma(long id) {

        String consulta = "SELECT r FROM EnvioEntity r WHERE  r.estado = true and r.alarmaByIdAlarma.idAlarma=" + id + " Order By r.porcentaje asc";

        Query qu = entityManager.createQuery(consulta);
        return (List<EnvioEntity>) qu.getResultList();

    }

    public EnvioEntity getById(long id) {
        return entityManager.find(EnvioEntity.class, id);
    }

}