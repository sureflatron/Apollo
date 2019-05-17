package myapps.abm.dao;


import myapps.abm.business.LocalidadBL;
import myapps.abm.business.PropiedadBL;
import myapps.abm.business.QueryBL;
import myapps.abm.business.TipoInmuebleBL;
import myapps.abm.model.*;
import myapps.servicio_basico.util.QueryBuilder;
import myapps.servicio_basico.util.UtilConvert;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class InmuebleDAO implements Serializable {
    private static final Logger logger = LogManager.getLogger(InmuebleDAO.class);
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    @Inject
    private QueryBL queryBL;
    @Inject
    private LocalidadBL localidadBL;
    @Inject
    private TipoInmuebleBL tipoInmuebleBL;
    @Inject
    private PropiedadBL propiedadBL;

    public void save(InmuebleEntity entity) {
        entityManager.persist(entity);
    }

    public void update(InmuebleEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<InmuebleEntity> listAll() {
        String consulta = "SELECT r FROM InmuebleEntity r WHERE  r.estado = true Order By r" +
                ".idInmueble desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<InmuebleEntity>) qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<InmuebleEntity> listAllByIdTipoInmueble(long tipo) {
        String consulta = "SELECT r FROM InmuebleEntity r WHERE r" +
                ".tipoInmuebleByIdTipoInmueble=" + tipo + " and r.estado = true Order By r" +
                ".idInmueble " +
                "desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<InmuebleEntity>) qu.getResultList();

    }

    public InmuebleEntity getById(long id) {
        return entityManager.find(InmuebleEntity.class, id);
    }

    public InmuebleEntity getByNombre(String nombre) {
        InmuebleEntity inmuebleEntity = new InmuebleEntity();
        try {
            String sql = "SELECT * FROM INMUEBLE WHERE UPPER(NOMBRE_RBS)='" + nombre + "'";
            inmuebleEntity = (InmuebleEntity) entityManager.createNativeQuery(sql,
                    InmuebleEntity.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.ERROR, "getByNombre - TIPO INMUEBLE DAO" + "|" + e.getMessage(), e);
        }

        return inmuebleEntity;
    }

    public InmuebleEntity getByCodInmueble(String codigo) {
        InmuebleEntity inmuebleEntity = new InmuebleEntity();
        try {
            String sql = "FROM InmuebleEntity WHERE codInmueble=?1";
            Query query = entityManager.createQuery(sql);
            query.setParameter(1, codigo);
            inmuebleEntity = (InmuebleEntity) query.getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.ERROR, "getByCodInmueble - TIPO INMUEBLE DAO" + "|" + e.getMessage(), e);
        }
        return inmuebleEntity;
    }

    public List<InmuebleEntity> listAllFilter(Map<String, Object> parameter) {
        List<InmuebleEntity> inmuebleEntityList = new ArrayList<>();
        String sql = queryBL.getQueryJson("FILTER_INMUEBLES");
        try {
            List<Object[]> list = QueryBuilder.query(sql, parameter, entityManager);

            if (!list.isEmpty()) {
                for (Object[] objects : list) {
                    InmuebleEntity inmuebleEntity = new InmuebleEntity();
                    inmuebleEntity.setIdInmueble(UtilConvert.convertToLong(objects[0]));

                    LocalidadEntity localidadEntity = localidadBL.getById(UtilConvert.convertToLong
                            (objects[1]));
                    inmuebleEntity.setLocalidadByIdLocalidad(localidadEntity);
                    inmuebleEntity.setSitioid((String) objects[2]);
                    inmuebleEntity.setNombreRbs((String) objects[3]);

                    TipoInmuebleEntity tipoInmuebleEntity = tipoInmuebleBL.getById(UtilConvert
                            .convertToLong(objects[4]));
                    inmuebleEntity.setTipoInmuebleByIdTipoInmueble(tipoInmuebleEntity);

                    PropiedadEntity propiedadEntity = propiedadBL.getById(UtilConvert
                            .convertToLong(objects[5]));
                    inmuebleEntity.setPropiedadByIdPropiedad(propiedadEntity);
                    inmuebleEntity.setDireccion((String) objects[6]);
                    inmuebleEntity.setCodInmueble((String) objects[7]);
                    inmuebleEntity.setCodCve((String) objects[8]);
                    inmuebleEntity.setCostoInstall((String) objects[9]);
                    inmuebleEntity.setLatitud((String) objects[10]);
                    inmuebleEntity.setLongitud((String) objects[11]);
                    inmuebleEntity.setObservaciones((String) objects[12]);
                    inmuebleEntity.setSector((String) objects[13]);
                    inmuebleEntity.setEstado(UtilConvert.convertToLong(objects[14]) == 1);
                    inmuebleEntityList.add(inmuebleEntity);
                }
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "LIST BY FILTER " + "|" + e.getMessage(), e);
        }
        return inmuebleEntityList;
    }
}