package myapps.abm.dao;


import myapps.abm.model.ConstructorEntity;
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
public class ConstructorDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ConstructorDAO.class);

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(ConstructorEntity entity) {
        entityManager.persist(entity);
    }

    public void update(ConstructorEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<ConstructorEntity> listAll() {

        String consulta = "SELECT r FROM ConstructorEntity r WHERE  r.estado = true Order By r.idConstructor desc ";

        Query qu = entityManager.createQuery(consulta);
        return (List<ConstructorEntity>) qu.getResultList();

    }

    public ConstructorEntity getById(long id) {
        return entityManager.find(ConstructorEntity.class, id);
    }
    public ConstructorEntity getByNombre(String nombre) {
        ConstructorEntity constructorEntity = new ConstructorEntity();
        try {
            String sql = "SELECT * FROM CONSTRUCTOR WHERE UPPER(NOMBRE)='" + nombre + "'";
            constructorEntity = (ConstructorEntity) entityManager.createNativeQuery(sql, ConstructorEntity.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.ERROR, "getByNombre - CONSTRUCTOR DAO" + "|" + e.getMessage(), e);
        }
        return constructorEntity;
    }
}