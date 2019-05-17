package myapps.abm.dao;

import myapps.abm.model.TipoTorreEntity;
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
public class TipoTorreDAO implements Serializable {
    private static final Logger logger = LogManager.getLogger(TipoInmuebleDAO.class);
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(TipoTorreEntity entity) {
        entityManager.persist(entity);
    }

    public void update(TipoTorreEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<TipoTorreEntity> listAll() {
        String consulta = "SELECT r FROM TipoTorreEntity r WHERE  r.estado = true Order By r.idTipoTorre desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<TipoTorreEntity>) qu.getResultList();
    }

    public TipoTorreEntity getById(long id) {
        return entityManager.find(TipoTorreEntity.class, id);
    }
    public TipoTorreEntity getByNombre(String nombre) {
        TipoTorreEntity tipoTorreEntity=new TipoTorreEntity();
        try{
            String sql = "SELECT * FROM TIPO_TORRE WHERE UPPER(TORRE)='"+nombre+"'";
            tipoTorreEntity = (TipoTorreEntity) entityManager.createNativeQuery(sql,
                    TipoTorreEntity.class).getSingleResult();
        } catch (NoResultException e){
            logger.log(Level.ERROR, "getByNombre - TIPO TORRE DAO" + "|" + e.getMessage(), e);
        }

        return tipoTorreEntity;
    }
}
