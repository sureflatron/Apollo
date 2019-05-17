package myapps.abm.dao;

import myapps.abm.model.MonedaEntity;
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
public class MonedaDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(MonedaDAO.class);
    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(MonedaEntity entity) {
        entityManager.persist(entity);
    }

    public void update(MonedaEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<MonedaEntity> listAll() {

        String consulta = "SELECT r FROM MonedaEntity r WHERE r.moneda='BOB' or r.moneda='USD'  " +
                "Order " +
                "By r" +
                ".idMoneda " +
                "desc ";

        Query qu = entityManager.createQuery(consulta);
        return (List<MonedaEntity>) qu.getResultList();

    }

    public MonedaEntity getById(long id) {
        return entityManager.find(MonedaEntity.class, id);
    }

    public MonedaEntity getMonedaByNombre(String nombre) {
        MonedaEntity monedaEntity=new MonedaEntity();
        try{
            String sql = "SELECT * FROM MONEDA WHERE UPPER(MONEDA)='"+nombre+"'";
            monedaEntity = (MonedaEntity) entityManager.createNativeQuery(sql,
                    MonedaEntity.class)
                    .getSingleResult();
        } catch (NoResultException e){
            logger.log(Level.ERROR, "getProveedorByNombre - DAO" + "|" + e.getMessage(), e);
        }

        return monedaEntity;
    }
}