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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class ServiciooDAO implements Serializable {
    private static final Logger logger = LogManager.getLogger(ServiciooDAO.class);
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    @Inject
    private TipoServicioBL tipoServicioBL;
    @Inject
    private TipoInmuebleBL tipoInmuebleBL;
    @Inject
    private TipoTorreBL tipoTorreBL;
    @Inject
    private ProveedorBL proveedorBL;
    @Inject
    private BancoBL bancoBL;
    @Inject
    private MonedaBL monedaBL;
    @Inject
    private QueryBL queryBL;

    public void save(ServicioEntity entity) {
        entityManager.persist(entity);
    }

    public void update(ServicioEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<ServicioEntity> listAll() {

        String consulta = "SELECT r FROM ServicioEntity r WHERE  r.estado = true Order By r.idServicio desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<ServicioEntity>) qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<ServicioEntity> listAllnotMedidor() {

        String consulta = "SELECT r FROM ServicioEntity r WHERE  not r.idServicio in(select  t.servicioByIdServicio.idServicio from MedidorEntity  t where t.estado=true) order by r.idServicio desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<ServicioEntity>) qu.getResultList();

    }

   
    public List<ServicioEntity> listAllFilter(Map<String, Object> parameter, int selectQuery) {
        List<ServicioEntity> servicioEntityList = new ArrayList<>();
        String sql;
        try {
            if (selectQuery == 2) {
                sql = queryBL.getQueryJson("FILTER_SERVICES");
                List<Object[]> list = QueryBuilder.query(sql, parameter, entityManager);

                if (!list.isEmpty()) {
                    for (Object[] objects : list) {
                        ServicioEntity servicioEntity = new ServicioEntity();

                        servicioEntity.setIdServicio(UtilConvert.convertToLong(objects[0]));

                        TipoServicioEntity tipoServicioEntity = tipoServicioBL.getById(UtilConvert.convertToLong(objects[1]));
                        servicioEntity.setTipoServicioByIdTipoServicio(tipoServicioEntity);

                        TipoInmuebleEntity tipoInmuebleEntity = tipoInmuebleBL.getById(UtilConvert
                                .convertToLong(objects[3]));
                        servicioEntity.setNombreTipoInmueble(tipoInmuebleEntity.getNombre());
                        servicioEntity.setIdSite((String) objects[4]);
                        servicioEntity.setCodigo((String) objects[5]);
                        servicioEntity.setNombreSite((String) objects[6]);

                        ProveedorEntity proveedorEntity = proveedorBL.getProveedordById(UtilConvert
                                .convertToLong(objects[7]));
                        servicioEntity.setProveedorByIdProveedor(proveedorEntity);

                        servicioEntity.setCodServMed((String) objects[8]);
                        servicioEntity.setCodFijo((String) objects[9]);
                        servicioEntity.setFormaPago((String) objects[10]);

                        BancoEntity bancoEntity = bancoBL.getById(UtilConvert.convertToLong
                                (objects[11]));
                        servicioEntity.setBancoByIdBanco(bancoEntity);

                        MonedaEntity monedaEntity = monedaBL.getById(UtilConvert.convertToLong
                                (objects[12]));
                        servicioEntity.setMonedaByIdMoneda(monedaEntity);

                        servicioEntity.setNroCuenta((String) objects[13]);
                        servicioEntity.setObservaciones((String) objects[14]);

                        UnidadOperativaEntity unidadOperativaEntity = new UnidadOperativaEntity();
                        unidadOperativaEntity.setIdUnidadOperativa(UtilConvert.convertToLong
                                (objects[15]));

                        servicioEntity.setUnidadOperativaByIdUnidadOperativa(unidadOperativaEntity);
                        servicioEntity.setEstado(UtilConvert.convertToLong(objects[16]) == 1);
                        servicioEntity.setLatitud(objects[17].toString());
                        servicioEntity.setLonguitud(objects[18].toString());

                        servicioEntityList.add(servicioEntity);
                    }
                }
            } else if (selectQuery == 1) {
                sql = queryBL.getQueryJson("FILTER_SERVICES_RDBS");
                List<Object[]> list = QueryBuilder.query(sql, parameter, entityManager);
                if (!list.isEmpty()) {
                    for (Object[] objects : list) {
                        ServicioEntity servicioEntity = new ServicioEntity();

                        servicioEntity.setIdServicio(UtilConvert.convertToLong(objects[0]));

                        TipoServicioEntity tipoServicioEntity = tipoServicioBL.getById(UtilConvert.convertToLong(objects[1]));
                        servicioEntity.setTipoServicioByIdTipoServicio(tipoServicioEntity);
                        TipoTorreEntity tipoTorreEntity =tipoTorreBL.getById(UtilConvert
                                .convertToLong(objects[3]));
                        servicioEntity.setNombreTipoInmueble(tipoTorreEntity.getTorre());
                        servicioEntity.setIdSite((String) objects[4]);
                        servicioEntity.setCodigo((String) objects[5]);
                        servicioEntity.setNombreSite((String) objects[6]);

                        ProveedorEntity proveedorEntity = proveedorBL.getProveedordById(UtilConvert
                                .convertToLong(objects[7]));
                        servicioEntity.setProveedorByIdProveedor(proveedorEntity);

                        servicioEntity.setCodServMed((String) objects[8]);
                        servicioEntity.setCodFijo((String) objects[9]);
                        servicioEntity.setFormaPago((String) objects[10]);

                        BancoEntity bancoEntity = bancoBL.getById(UtilConvert.convertToLong
                                (objects[11]));
                        servicioEntity.setBancoByIdBanco(bancoEntity);

                        MonedaEntity monedaEntity = monedaBL.getById(UtilConvert.convertToLong
                                (objects[12]));
                        servicioEntity.setMonedaByIdMoneda(monedaEntity);

                        servicioEntity.setNroCuenta((String) objects[13]);
                        servicioEntity.setObservaciones((String) objects[14]);

                        UnidadOperativaEntity unidadOperativaEntity = new UnidadOperativaEntity();
                        unidadOperativaEntity.setIdUnidadOperativa(UtilConvert.convertToLong
                                (objects[15]));

                        servicioEntity.setUnidadOperativaByIdUnidadOperativa(unidadOperativaEntity);
                        servicioEntity.setEstado(UtilConvert.convertToLong(objects[16]) == 1);

                        servicioEntityList.add(servicioEntity);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "LIST BY FILTER " + "|" + e.getMessage(), e);
        }
        return servicioEntityList;
    }

    public ServicioEntity getById(long id) {
        return entityManager.find(ServicioEntity.class, id);
    }


    public ServicioEntity buscar( String asociado){
        ServicioEntity servicioEntity = new ServicioEntity();
        try {
            servicioEntity =  (ServicioEntity) entityManager.createQuery(
                    "SELECT c FROM ServicioEntity c WHERE c.codServMed LIKE :asociado")
                    .setParameter("asociado", asociado)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Error al Obtener el campo "+ e.getMessage());
        }
        return servicioEntity;
    }

}