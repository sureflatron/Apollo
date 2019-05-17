package myapps.abm.dao;


import myapps.abm.model.TipoServicioEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class TipoServicioDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(TipoServicioDAO.class);
    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(TipoServicioEntity entity) {
        entityManager.persist(entity);
    }

    public void update(TipoServicioEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<TipoServicioEntity> listAll() {

        String consulta = "SELECT r FROM TipoServicioEntity r WHERE  r.estado = true Order By r.idTipoServicio desc";

        Query qu = entityManager.createQuery(consulta);
        return (List<TipoServicioEntity>) qu.getResultList();

    }

    public TipoServicioEntity getById(long id) {
        return entityManager.find(TipoServicioEntity.class, id);
    }

    public TipoServicioEntity getByNombre(String nombre) {
        TipoServicioEntity tipoServicioEntity = new TipoServicioEntity();
        try {
            String sql = "FROM TipoServicioEntity WHERE servicio=?1";
            Query query = entityManager.createQuery(sql);
            query.setParameter(1, nombre);
            tipoServicioEntity = (TipoServicioEntity) query.getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.ERROR, "getByNombre - DAO" + "|" + e.getMessage(), e);
        }
        return tipoServicioEntity;
    }
}