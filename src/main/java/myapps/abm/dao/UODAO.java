package myapps.abm.dao;


import myapps.abm.model.UnidadOperativaEntity;
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
public class UODAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(UODAO.class);
    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(UnidadOperativaEntity entity) {
        entityManager.persist(entity);
    }

    public void update(UnidadOperativaEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<UnidadOperativaEntity> listAll() {

        String consulta = "SELECT r FROM UnidadOperativaEntity r WHERE  r.estado = true Order By r.idUnidadOperativa desc";

        Query qu = entityManager.createQuery(consulta);
        return (List<UnidadOperativaEntity>) qu.getResultList();

    }

    public UnidadOperativaEntity getById(long id) {
        return entityManager.find(UnidadOperativaEntity.class, id);
    }
    public UnidadOperativaEntity getUOperativaByNombre(String nombre) {
        UnidadOperativaEntity unidadOperativaEntity=new UnidadOperativaEntity();
        try{
            String sql = "SELECT * FROM UNIDAD_OPERATIVA WHERE UPPER(NOMBRE)='"+nombre+"'";
            unidadOperativaEntity = (UnidadOperativaEntity) entityManager.createNativeQuery(sql,
                    UnidadOperativaEntity.class)
                    .getSingleResult();
        } catch (NoResultException e){
            logger.log(Level.ERROR, "getUOperativaByNombre - DAO" + "|" + e.getMessage(), e);
        }

        return unidadOperativaEntity;
    }
}