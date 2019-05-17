package myapps.abm.dao;

import myapps.abm.model.TipoInmuebleEntity;
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
public class TipoInmuebleDAO implements Serializable {
    private static final Logger logger = LogManager.getLogger(TipoInmuebleDAO.class);
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(TipoInmuebleEntity entity) {
        entityManager.persist(entity);
    }

    public void update(TipoInmuebleEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<TipoInmuebleEntity> listAll() {
        String consulta = "SELECT r FROM TipoInmuebleEntity r WHERE r.nombre!='Radio Bases' and r" +
                ".estado = true " +
                "Order By r.idTipoInmueble desc";
        Query qu = entityManager.createQuery(consulta);
        return (List<TipoInmuebleEntity>) qu.getResultList();

    }
    @SuppressWarnings("unchecked")
    public List<TipoInmuebleEntity> findAll() {
        String consulta = "SELECT r FROM TipoInmuebleEntity r WHERE r" +
                ".estado = true " +
                "Order By r.idTipoInmueble desc";
        Query qu = entityManager.createQuery(consulta);
        return (List<TipoInmuebleEntity>) qu.getResultList();

    }

    public TipoInmuebleEntity getById(long id) {
        return entityManager.find(TipoInmuebleEntity.class, id);
    }

    public TipoInmuebleEntity getByNombre(String nombre) {
        TipoInmuebleEntity tipoInmuebleEntity=new TipoInmuebleEntity();
        try{
            String sql = "SELECT * FROM TIPO_INMUEBLE WHERE UPPER(NOMBRE)='"+nombre+"'";
            tipoInmuebleEntity = (TipoInmuebleEntity) entityManager.createNativeQuery(sql,
                    TipoInmuebleEntity.class)
                    .getSingleResult();
        } catch (NoResultException e){
            logger.log(Level.ERROR, "getByNombre - TIPO INMUEBLE DAO" + "|" + e.getMessage(), e);
        }

        return tipoInmuebleEntity;
    }
}
