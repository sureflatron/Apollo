package myapps.abm.dao;


import myapps.abm.model.PropiedadEntity;
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
public class PropiedadDAO implements Serializable {
    private static final Logger logger = LogManager.getLogger(LocalidadDAO.class);
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(PropiedadEntity entity) {
        entityManager.persist(entity);
    }

    public void update(PropiedadEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<PropiedadEntity> listAll(boolean allPropiedad) {
        String consulta = "";
        if (allPropiedad) {
            consulta = "SELECT r FROM PropiedadEntity r WHERE  r.estado = true Order By r.idPropiedad desc ";

        } else {
            consulta = "SELECT r FROM PropiedadEntity r Order By r.nombre";
        }
        Query qu = entityManager.createQuery(consulta);
        return (List<PropiedadEntity>) qu.getResultList();

    }

    public PropiedadEntity getById(long id) {
        return entityManager.find(PropiedadEntity.class, id);
    }

    public PropiedadEntity getByNombre(String nombre) {
        PropiedadEntity propiedadEntity = new PropiedadEntity();
        try {
            String sql = "SELECT * FROM PROPIEDAD WHERE UPPER(NOMBRE)='" + nombre + "'";
            propiedadEntity = (PropiedadEntity) entityManager.createNativeQuery(sql, PropiedadEntity.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.ERROR, "getByNombre - PROPIEDAD DAO" + "|" + e.getMessage(), e);
        }
        return propiedadEntity;
    }
}