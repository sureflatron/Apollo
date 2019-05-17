package myapps.abm.dao;

import myapps.abm.model.HistorialContratosEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class HistorialContratosDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(HistorialContratosEntity entity) {
        entityManager.persist(entity);
    }

    public void update(HistorialContratosEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<HistorialContratosEntity> listAll() {
        String consulta = "SELECT b FROM HistorialContratosEntity b order by b" +
                ".contratosRenovadosByContratoRevoId.nroContrato, b.contratosRenovadosByContratoRevoId.expiracion desc ";
        Query qu = entityManager.createQuery(consulta);
        return qu.getResultList();
    }

    public HistorialContratosEntity getById(long id) {
        return entityManager.find(HistorialContratosEntity.class, id);
    }


}
