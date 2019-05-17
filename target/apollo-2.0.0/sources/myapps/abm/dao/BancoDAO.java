package myapps.abm.dao;

import myapps.abm.model.BancoEntity;
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
public class BancoDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(BancoDAO.class);

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(BancoEntity entity) {
        entityManager.persist(entity);
    }

    public void update(BancoEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<BancoEntity> listAll() {
        String consulta = "SELECT b FROM BancoEntity b WHERE b.estado=1 order by b.idBanco desc ";
        Query qu = entityManager.createQuery(consulta);
        return qu.getResultList();
    }

    public BancoEntity getById(long id) {
        return entityManager.find(BancoEntity.class, id);
    }

    public BancoEntity getBancoByNombre(String nombre) {
        BancoEntity bancoEntity=new BancoEntity();
        try{
            String sql = "SELECT * FROM BANCO WHERE UPPER(NOMBRE)='"+nombre+"'";
            bancoEntity = (BancoEntity) entityManager.createNativeQuery(sql,
                    BancoEntity.class)
                    .getSingleResult();
        } catch (NoResultException e){
            logger.log(Level.ERROR, "getBancoByNombre - DAO" + "|" + e.getMessage(), e);
        }

        return bancoEntity;
    }

}
