package myapps.abm.dao;

import myapps.abm.model.RadioBaseEntity;
import myapps.abm.model.RdbServEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class RdbsServDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(RdbServEntity entity) {
        entityManager.persist(entity);
    }

    public void update(RdbServEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<RdbServEntity> listAll() {
        String consulta = "SELECT b FROM RdbServEntity b WHERE b.estado=1 order by b.idInmServ desc ";
        Query qu = entityManager.createQuery(consulta);
        return qu.getResultList();
    }

    public RdbServEntity getById(long id) {
        return entityManager.find(RdbServEntity.class, id);
    }

    @SuppressWarnings("unchecked")
    public RdbServEntity getByIdServicio(long idServicio) {
        RdbServEntity inmServEntity = new RdbServEntity();
        Query qu = entityManager.createNativeQuery("SELECT b.* FROM RDB_SERV b WHERE b" +
                ".ID_SERVICIO=:idServicio").setParameter("idServicio", idServicio);
        List<Object[]> inmServEntityList = qu.getResultList();
        for (Object[] inmServ : inmServEntityList) {
            RadioBaseEntity inmuebleEntity = new RadioBaseEntity();
            BigDecimal idInm = (BigDecimal) inmServ[1];
            inmuebleEntity.setIdRadioBase(idInm.longValue());
            inmServEntity.setRadioBaseByIdRadioBase(inmuebleEntity);
        }
        return inmServEntity;
    }
    /*public RdbServEntity getByIdInmueble(long idInmueble) {
        RdbServEntity inmServEntity = new RdbServEntity();
        Query qu = entityManager.createNativeQuery("SELECT b.* FROM INM_SERV b WHERE b" +
                ".ID_SERVICIO=:idInmueble").setParameter("idInmueble", idInmueble);
        List<Object[]> inmServEntityList = qu.getResultList();
        for (Object[] inmServ : inmServEntityList) {
            InmuebleEntity inmuebleEntity = new InmuebleEntity();
            BigDecimal idInm = (BigDecimal) inmServ[1];
            inmuebleEntity.setIdInmueble(idInm.longValue());
            inmServEntity.setInmuebleByIdInmueble(inmuebleEntity);
            inmServEntity.setTipoInm((String) inmServ[4]);
        }
        return inmServEntity;
    }*/
}
