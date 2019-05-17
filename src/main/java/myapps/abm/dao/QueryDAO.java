package myapps.abm.dao;

import myapps.abm.model.QueryJsonEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;

@Stateless
public class QueryDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public QueryJsonEntity getQueryJson(String nombre) {
        String sql ="SELECT q FROM QueryJsonEntity q WHERE q.nombre='" + nombre + "'";
        Query qu= entityManager.createQuery(sql);
        return (QueryJsonEntity) qu.getResultList().get(0);
    }
}
