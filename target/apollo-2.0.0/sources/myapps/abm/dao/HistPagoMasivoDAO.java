package myapps.abm.dao;

import myapps.abm.business.QueryBL;
import myapps.abm.model.ConfiguracionesEntity;
import myapps.abm.model.HistorialPagosMasivosEntity;
import myapps.abm.model.PagoServicioEntity;
import myapps.servicio_basico.util.QueryBuilder;
import myapps.servicio_basico.util.UtilConvert;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class HistPagoMasivoDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(HistPagoMasivoDAO.class);
    @Inject
    private QueryBL queryBL;
    @Inject
    private PagoServicioDAO pagoServicioDAO;
    @Inject
    private ConfiguracionesDAO configuracionesDAO;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(HistorialPagosMasivosEntity entity) {
        entityManager.persist(entity);
    }

    public void update(HistorialPagosMasivosEntity entity) {
        entityManager.merge(entity);
    }

   
    public List<HistorialPagosMasivosEntity> listAll(Map<String, Object> paramExtFile) {
        List<HistorialPagosMasivosEntity> hPagosMasivosEntity = new ArrayList<>();
        try {
            List<Object[]> list;

            String sqlJson = queryBL.getQueryJson("FILTER_HISTORY_PAGOS_MASIVOS");
            list = QueryBuilder.query(sqlJson, paramExtFile, entityManager);

            if (!list.isEmpty()) {
                for (Object[] hPago : list) {
                    HistorialPagosMasivosEntity hPagosMasivo = new HistorialPagosMasivosEntity();
                    hPagosMasivo.setId(((BigDecimal) hPago[0]).longValue());
                    hPagosMasivo.setNombreArchivo((String) hPago[1]);
                    hPagosMasivo.setProcesados(((BigDecimal) hPago[2]).longValue());
                    hPagosMasivo.setNoprocesados(((BigDecimal) hPago[3]).longValue());
                    hPagosMasivo.setFechaIni((Timestamp) hPago[4]);
                    hPagosMasivo.setFechaFin((Timestamp) hPago[5]);
                    hPagosMasivo.setExt((String) hPago[6]);
                    hPagosMasivo.setUsuario((String) hPago[7]);

                    List<PagoServicioEntity> entity = pagoServicioDAO.listAllByHistorialId(hPagosMasivo.getId());
                    hPagosMasivo.setPagoServiciosById(entity);

                    ConfiguracionesEntity configuracionesEntity = configuracionesDAO.getById
                            (UtilConvert.convertToLong(hPago[8]));
                    hPagosMasivo.setConfiguracionesByConfiguracionId(configuracionesEntity);
                    hPagosMasivosEntity.add(hPagosMasivo);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return hPagosMasivosEntity;
    }

    public HistorialPagosMasivosEntity getById(long id) {
        return entityManager.find(HistorialPagosMasivosEntity.class, id);
    }
}
