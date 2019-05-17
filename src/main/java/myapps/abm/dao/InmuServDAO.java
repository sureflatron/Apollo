package myapps.abm.dao;

import myapps.abm.model.InmServEntity;
import myapps.abm.model.InmuebleEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class InmuServDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(InmServEntity entity) {
        entityManager.persist(entity);
    }

    public void update(InmServEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<InmServEntity> listAll() {
        String consulta = "SELECT b FROM InmServEntity b WHERE b.estado=1 order by b.idInmServ desc ";
        Query qu = entityManager.createQuery(consulta);
        return qu.getResultList();
    }

    public InmServEntity getById(long id) {
        return entityManager.find(InmServEntity.class, id);
    }

    @SuppressWarnings("unchecked")
    public InmServEntity getByIdServicio(long idServicio) {
        InmServEntity inmServEntity = new InmServEntity();
        Query qu = entityManager.createNativeQuery("SELECT b.* FROM INM_SERV b WHERE b" +
                ".ID_SERVICIO=:idServicio").setParameter("idServicio", idServicio);
        List<Object[]> inmServEntityList = qu.getResultList();
        for (Object[] inmServ : inmServEntityList) {
            InmuebleEntity inmuebleEntity = new InmuebleEntity();
            BigDecimal idInm = (BigDecimal) inmServ[1];
            inmuebleEntity.setIdInmueble(idInm.longValue());
            inmServEntity.setIdInmServ(((BigDecimal)inmServ[0]).longValue());
            inmServEntity.setInmuebleByIdInmueble(inmuebleEntity);
        }
        return inmServEntity;
    }
    
    @SuppressWarnings("unchecked")
    public InmServEntity getByIdInmueble(long idInmueble) {
        InmServEntity inmServEntity = new InmServEntity();
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
    }
}
