package myapps.abm.dao;

import myapps.abm.model.MedidorEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class MedidorDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(MedidorDAO.class);
    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(MedidorEntity entity) {
        entityManager.persist(entity);
    }

    public void update(MedidorEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<MedidorEntity> listAll() {

        String consulta = "SELECT r FROM MedidorEntity r WHERE  r.estado = true Order By r.idMedidor desc";

        Query qu = entityManager.createQuery(consulta);
        return (List<MedidorEntity>) qu.getResultList();

    }

    public MedidorEntity repetidos(long idServicio) {
        MedidorEntity medidorEntity = new MedidorEntity();
        try {
            String consulta = "SELECT r FROM MedidorEntity r WHERE  r.servicioByIdServicio.idServicio= " + idServicio;
            Query qu = entityManager.createQuery(consulta);
            medidorEntity = (MedidorEntity) qu.getSingleResult();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return medidorEntity;
    }

    public MedidorEntity getById(long id) {
        return entityManager.find(MedidorEntity.class, id);
    }

    public void eliminar(MedidorEntity medidor) {
        try {
            String consulta = "delete  FROM MedidorEntity r WHERE  r.idMedidor=" + medidor.getIdMedidor();
            entityManager.createQuery(consulta).executeUpdate();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}