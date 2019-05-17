package myapps.abm.dao;

import myapps.abm.business.*;
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
public class RadioBaseDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ServiciooDAO.class);
    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;


    @Inject
    private QueryBL queryBL;
    @Inject
    private LocalidadBL localidadBL;
    @Inject
    private PropiedadBL propiedadBL;
    @Inject
    private TipoTorreBL tipoTorreBL;
    @Inject
    private ConstructorBL constructorBL;

    public void save(RadioBaseEntity entity) {
        entityManager.persist(entity);
    }

    public void update(RadioBaseEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<RadioBaseEntity> listAll() {

        String consulta = "SELECT r FROM RadioBaseEntity r WHERE  r.estado = '1' Order By r" +
                ".idRadioBase DESC ";
        Query qu = entityManager.createQuery(consulta);
        return (List<RadioBaseEntity>) qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<RadioBaseEntity> listAllByTipoTorre(long idTipoTorre) {

        String consulta = "FROM RadioBaseEntity r WHERE r.tipoTorreByIdTipoTorre=" + idTipoTorre + " and  r.estado= '1' Order By r.idRadioBase DESC ";
        Query qu = entityManager.createQuery(consulta);
        return (List<RadioBaseEntity>) qu.getResultList();

    }

    public RadioBaseEntity getByNombre(String nombre) {
        RadioBaseEntity radioBaseEntity = new RadioBaseEntity();
        try {
            String sql = "SELECT * FROM RADIO_BASE WHERE UPPER(NOMBRE_RBS)='" + nombre + "'";
            radioBaseEntity = (RadioBaseEntity) entityManager.createNativeQuery(sql,
                    RadioBaseEntity.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.ERROR, "getByNombre " + "|" + e.getMessage(), e);
        }

        return radioBaseEntity;
    }

    public RadioBaseEntity getByCodRadioBase(String codigo) {
        RadioBaseEntity radioBaseEntity = new RadioBaseEntity();
        try {
            String sql = "FROM RadioBaseEntity WHERE codInmueble=?1";
            Query query = entityManager.createQuery(sql);
            query.setParameter(1, codigo);
            radioBaseEntity = (RadioBaseEntity) query.getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.ERROR, "getByCodRadioBase - DAO" + "|" + e.getMessage(), e);
        }
        return radioBaseEntity;
    }

    public List<RadioBaseEntity> listAllFilter(Map<String, Object> parameter) {
        List<RadioBaseEntity> radioBaseEntityList = new ArrayList<>();
        String sql = queryBL.getQueryJson("FILTER_RABIO_BASE");
        try {
            List<Object[]> list = QueryBuilder.query(sql, parameter, entityManager);

            if (!list.isEmpty()) {
                for (Object[] objects : list) {
                    RadioBaseEntity radioBaseEntity = new RadioBaseEntity();
                    radioBaseEntity.setIdRadioBase(UtilConvert.convertToLong(objects[0]));

                    LocalidadEntity localidadEntity = localidadBL.getById(UtilConvert
                            .convertToLong(objects[1]));

                    radioBaseEntity.setLocalidadByIdLocalidad(localidadEntity);
                    radioBaseEntity.setSitioid((String) objects[2]);
                    radioBaseEntity.setNombreRbs((String) objects[3]);

                    PropiedadEntity propiedadEntity = propiedadBL.getById(UtilConvert.convertToLong
                            (objects[4]));

                    radioBaseEntity.setPropiedadByIdPropiedad(propiedadEntity);
                    radioBaseEntity.setDireccion((String) objects[5]);
                    radioBaseEntity.setCodInmueble((String) objects[6]);

                    TipoTorreEntity tipoTorreEntity = tipoTorreBL.getById(UtilConvert
                            .convertToLong(objects[7]));
                    radioBaseEntity.setTipoTorreByIdTipoTorre(tipoTorreEntity);
                    radioBaseEntity.setAltura((String) objects[8]);
                    radioBaseEntity.setCodCve((String) objects[9]);
                    radioBaseEntity.setCostoInstall((String) objects[10]);
                    radioBaseEntity.setAreax1((String) objects[11]);
                    radioBaseEntity.setAreax2((String) objects[12]);
                    radioBaseEntity.setDcattt((String) objects[13]);
                    radioBaseEntity.setEstadoInmueble((String) objects[14]);

                    ConstructorEntity constructorEntity = constructorBL.getById(UtilConvert
                            .convertToLong(objects[15]));
                    radioBaseEntity.setConstructorByIdConstructor(constructorEntity);

                    radioBaseEntity.setLatitud((String) objects[16]);
                    radioBaseEntity.setLongitud((String) objects[17]);
                    radioBaseEntity.setObservaciones((String) objects[18]);
                    radioBaseEntity.setEstado(Long.parseLong((String) objects[19]));
                    radioBaseEntity.setAreay1((String) objects[20]);
                    radioBaseEntity.setAreay2((String) objects[21]);

                    radioBaseEntityList.add(radioBaseEntity);

                }
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "LIST BY FILTER " + "|" + e.getMessage(), e);
        }
        return radioBaseEntityList;
    }

    public RadioBaseEntity getById(long id) {
        return entityManager.find(RadioBaseEntity.class, id);
    }
}