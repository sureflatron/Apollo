package myapps.abm.dao;

import myapps.abm.model.ContratosRenovadosEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ContratosRenovadosDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(ContratosRenovadosEntity entity) {
        entityManager.persist(entity);
    }

    public void update(ContratosRenovadosEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<ContratosRenovadosEntity> listAll() {
        String consulta = "SELECT b FROM ContratosRenovadosEntity b WHERE b.estado=1 order by b.idBanco desc ";
        Query qu = entityManager.createQuery(consulta);
        return qu.getResultList();
    }

    public ContratosRenovadosEntity getById(long id) {
        return entityManager.find(ContratosRenovadosEntity.class, id);
    }


}
