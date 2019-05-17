package myapps.abm.dao;

import myapps.abm.model.FusionEntity;
import myapps.abm.model.ProveedorEntity;
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
public class ProveedorDAO implements Serializable {
    private static final Logger logger = LogManager.getLogger(ProveedorDAO.class);
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(ProveedorEntity entity) {
        entityManager.persist(entity);
    }

    public void update(ProveedorEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<ProveedorEntity> listAll() {
        String consulta = "SELECT p FROM ProveedorEntity p WHERE p.estado=1 order by p.idProveedor desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<ProveedorEntity>) qu.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ProveedorEntity> listAllFusion(long idProveedor) {
        String consulta = "SELECT p FROM ProveedorEntity p WHERE p.estado=1 and p.idProveedor != :idProveedor";
        Query qu = entityManager.createQuery(consulta);
        qu.setParameter("idProveedor", idProveedor);
        return (List<ProveedorEntity>) qu.getResultList();
    }

    public ProveedorEntity getById(long id) {
        return entityManager.find(ProveedorEntity.class, id);
    }

    public void deleteFusionProveedor(long idProveedor) {
        String sql = "UPDATE FUSION SET ESTADO = 0 WHERE ID_PROVEEDOR_FUSION = :idProveedor";
        Query q = entityManager.createNativeQuery(sql);
        q.setParameter("idProveedor", idProveedor);
        q.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<FusionEntity> getFusionByProveedor(long idProveedor) {
        String consulta = "FROM FusionEntity r WHERE r.idProveedorFusion = :idProveedor";
        Query qu = entityManager.createQuery(consulta).setParameter("idProveedor", idProveedor);
        return qu.getResultList();

    }

    public void saveFusion(FusionEntity dato) {
        entityManager.persist(dato);
    }

    public void deleteFusionByIdProveedor(long idProveedor) {
        String sql = "DELETE FusionEntity r WHERE r.idProveedorFusion = :idProveedor";
        Query q = entityManager.createQuery(sql);
        q.setParameter("idProveedor", idProveedor);
        q.executeUpdate();
    }


    public ProveedorEntity buscar(String nombre) {
        ProveedorEntity proveedorEntity = new ProveedorEntity();
        try {
            proveedorEntity = (ProveedorEntity) entityManager.createQuery(
                    "SELECT c FROM ProveedorEntity c WHERE c.nombre LIKE :nombre")
                    .setParameter("nombre", nombre)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {

            logger.log(Level.ERROR, "buscar - DAO" + "|" + e.getMessage(), e);
        }
        return proveedorEntity;
    }
    public ProveedorEntity getProveedorByNombre(String nombre) {
        ProveedorEntity proveedorEntity=new ProveedorEntity();
        try{
            String sql = "SELECT * FROM PROVEEDOR WHERE UPPER(NOMBRE)='"+nombre+"'";
            proveedorEntity = (ProveedorEntity) entityManager.createNativeQuery(sql,
                    ProveedorEntity.class)
                    .getSingleResult();
        } catch (NoResultException e){
            logger.log(Level.ERROR, "getProveedorByNombre - DAO" + "|" + e.getMessage(), e);
        }

        return proveedorEntity;
    }

}
