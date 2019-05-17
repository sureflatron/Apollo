package myapps.abm.dao;


import myapps.abm.model.ConfiguracionesEntity;
import myapps.servicio_basico.util.UtilDate;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
public class ConfiguracionesDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ConfiguracionesDAO.class);
    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(ConfiguracionesEntity entity) {
        entityManager.persist(entity);
    }

    public void update(ConfiguracionesEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<ConfiguracionesEntity> listAll() {

        String consulta = "SELECT r FROM ConfiguracionesEntity r where r.estado=true Order By r.id";

        Query qu = entityManager.createQuery(consulta);
        return (List<ConfiguracionesEntity>) qu.getResultList();

    }

    public ConfiguracionesEntity obtenerConfiguracion(String nombre, String tipo, Date fecha) {
        ConfiguracionesEntity configuracion = new ConfiguracionesEntity();
        String fecha2 = UtilDate.dateToTimeStampSql(fecha);
        try {
            configuracion = (ConfiguracionesEntity) entityManager.createQuery(
                    "SELECT c FROM ConfiguracionesEntity c WHERE c.nombre=:nombre and c.tipo=:tipo and c.fechaCreacion=TO_DATE(:fecha2,'YYYY-MM-DD HH24:MI:SS')")
                    .setParameter("nombre", nombre)
                    .setParameter("tipo", tipo)
                    .setParameter("fecha2", fecha2)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            logger.error("Error al Obtener el campo " + e.getMessage());
        }
        return configuracion;
    }

    public ConfiguracionesEntity getById(long id) {
        return entityManager.find(ConfiguracionesEntity.class, id);
    }
}