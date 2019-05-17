package myapps.abm.business;


import myapps.abm.dao.LicAmbDAO;
import myapps.abm.model.DocumentosEntity;
import myapps.abm.model.LicAmbEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LicAmbBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(LicAmbBL.class);

    @Inject
    private LicAmbDAO licAmbDAO;
    @Inject
    private DocumentosBL documentosBL;

    public void save(LicAmbEntity entity, List<DocumentosEntity> documentosEntityList) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado((long) 1);
            entity.setAdjunto(documentosBL.contador(documentosEntityList));
            licAmbDAO.save(entity);
            documentosBL.guardarLista(documentosEntityList, entity.getIdLicAmb());

        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(LicAmbEntity entity, List<DocumentosEntity> documentosEntityList) {
        try {
            entity.setAdjunto(documentosBL.contador(documentosEntityList));
            licAmbDAO.update(entity);
            documentosBL.actualizarLista(documentosEntityList, entity.getIdLicAmb());
        } catch (Exception e) {

            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(LicAmbEntity entity, List<DocumentosEntity> documentosEntityList) {
        try {
            entity.setEstado((long) 0);
            licAmbDAO.update(entity);
            if (!documentosEntityList.isEmpty()) {
                for (DocumentosEntity documentosEntity : documentosEntityList) {
                    documentosEntity.setEstado(false);
                    documentosBL.update(documentosEntity);

                }
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<LicAmbEntity> listAll() {
        List<LicAmbEntity> list = new ArrayList<>();
        try {
            list = licAmbDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public LicAmbEntity getById(long id) {
        LicAmbEntity licAmbEntity = new LicAmbEntity();
        try {
            licAmbEntity = licAmbDAO.getById(id);
            return licAmbEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return licAmbEntity;
    }

}
