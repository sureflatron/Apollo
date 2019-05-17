package myapps.abm.business;

import myapps.abm.dao.HistPagoMasivoDAO;
import myapps.abm.model.HistorialPagosMasivosEntity;
import myapps.servicio_basico.util.UtilDate;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

@Stateless
public class HistPagoMasivoBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(HistPagoMasivoBL.class);

    @Inject
    private HistPagoMasivoDAO histPagoMasivoDAO;

    public void save(HistorialPagosMasivosEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            histPagoMasivoDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(HistorialPagosMasivosEntity entity) {
        try {
            histPagoMasivoDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(HistorialPagosMasivosEntity entity) {
        try {
            histPagoMasivoDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<HistorialPagosMasivosEntity> listAll(String userFilter, HistorialPagosMasivosEntity
            hPagosMasivosEntity, Date fecha) {
        List<HistorialPagosMasivosEntity> list = new ArrayList<>();
        Map<String, Object> paramExtFile = new HashMap<>();
        try {

            if (fecha != null) {
                paramExtFile.put("fecha", UtilDate.dateToString(fecha, "yyyy-MM-dd"));
            }
            if (hPagosMasivosEntity != null && hPagosMasivosEntity.getId() > 0) {
                paramExtFile.put("id", hPagosMasivosEntity.getId());
            }
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String rolId = request.getSession().getAttribute("TEMP$IDROL").toString();
            String userName = request.getSession().getAttribute("TEMP$USER_NAME").toString();

            if (userFilter != null) {
                paramExtFile.put("usuario", userFilter);
            } else {
                if (!rolId.equals("1")) {
                    paramExtFile.put("usuario", userName);
                }
            }
            list = histPagoMasivoDAO.listAll(paramExtFile);


            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public HistorialPagosMasivosEntity getById(long id) {
        HistorialPagosMasivosEntity hPagosMasivosEntity = new HistorialPagosMasivosEntity();
        try {
            hPagosMasivosEntity = histPagoMasivoDAO.getById(id);
            return hPagosMasivosEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return hPagosMasivosEntity;
    }


}
