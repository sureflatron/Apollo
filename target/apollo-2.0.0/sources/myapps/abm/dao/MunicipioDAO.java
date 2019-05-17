package myapps.abm.dao;

import myapps.abm.model.MunicipioEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class MunicipioDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(MunicipioEntity entity) {
        entityManager.persist(entity);
    }

    public void update(MunicipioEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<MunicipioEntity> listAll() {
        String consulta = "SELECT b FROM MunicipioEntity b order by  b.idMunicipio desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<MunicipioEntity>) qu.getResultList();
    }


    @SuppressWarnings("unchecked")
    public List<MunicipioEntity> listAllIdProvincia(long idProvincia) {
        String consulta = "SELECT b FROM MunicipioEntity b where  b.provinciaByIdProvincia=" + idProvincia;
        Query qu = entityManager.createQuery(consulta);
        return (List<MunicipioEntity>) qu.getResultList();
    }

    public MunicipioEntity getById(long id) {
        return entityManager.find(MunicipioEntity.class, id);
    }
}
