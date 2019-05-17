package myapps.abm.dao;


import myapps.abm.model.LocalidadEntity;
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
public class LocalidadDAO implements Serializable {
    private static final Logger logger = LogManager.getLogger(LocalidadDAO.class);
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(LocalidadEntity entity) {
        entityManager.persist(entity);
    }

    public void update(LocalidadEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<LocalidadEntity> listAll() {

        String consulta = "SELECT r FROM LocalidadEntity r WHERE  r.estado = true Order By r.idLocalidad desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<LocalidadEntity>) qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<LocalidadEntity> listAllIdLocalidad(long idLocalidad) {
        String consulta = "SELECT b FROM LocalidadEntity b where  b.municipioByIdMunicipio=" + idLocalidad;
        Query qu = entityManager.createQuery(consulta);
        return (List<LocalidadEntity>) qu.getResultList();
    }

    public LocalidadEntity getById(long id) {
        return entityManager.find(LocalidadEntity.class, id);
    }

    public LocalidadEntity getByNombre(String nombre) {
        LocalidadEntity  localidadEntity = new LocalidadEntity();
        try{
            String sql = "SELECT * FROM LOCALIDAD WHERE UPPER(NOMBRE)='"+nombre+"'";
            localidadEntity = (LocalidadEntity) entityManager.createNativeQuery(sql,
                    LocalidadEntity.class).getSingleResult();

        }catch (NoResultException e){
            logger.log(Level.ERROR, "getByNombre - LOCALIDAD DAO" + "|" + e.getMessage(), e);
        }
        return localidadEntity;
    }
}