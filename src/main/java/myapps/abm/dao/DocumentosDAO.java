package myapps.abm.dao;

import myapps.abm.model.DocumentosEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class DocumentosDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(DocumentosEntity entity) {
        entityManager.persist(entity);
    }

    public void update(DocumentosEntity entity) {
        entityManager.merge(entity);
    }
    public void remove(DocumentosEntity entity) {
        String sql = "DELETE  DocumentosEntity d WHERE d.idDocumento = :idDoc";
        Query q = entityManager.createQuery(sql);
        q.setParameter("idDoc", entity.getIdDocumento());
        q.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<DocumentosEntity> listAll(long idLic) {
        String sql = "SELECT b FROM DocumentosEntity b";
        StringBuilder sqlWhere = new StringBuilder("");
        if (idLic > 0) {
            sqlWhere = sqlWhere.append(" WHERE b.idPertenece=").append(idLic).append(" and b.estado=true order by b.idDocumento desc");
        } else {
            sqlWhere = sqlWhere.append(" WHERE b.estado=1");
        }
        sql = sql + sqlWhere;
        Query qu = entityManager.createQuery(sql);
        return qu.getResultList();
    }

    public DocumentosEntity getById(long id) {
        return entityManager.find(DocumentosEntity.class, id);
    }
    public DocumentosEntity getDocByIdPertenece(long idPert,String textPert) {
        String sql= "SELECT b FROM DocumentosEntity b WHERE b.idPertenece="+idPert+" and b" +
                ".pertenece='"+textPert+"'";

        return (DocumentosEntity) entityManager.createQuery(sql).getSingleResult();
    }
}
