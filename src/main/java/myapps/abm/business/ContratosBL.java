package myapps.abm.business;


import myapps.abm.dao.ContratosDAO;
import myapps.abm.model.ContratosEntity;
import myapps.abm.model.DocumentosEntity;
import myapps.servicio_basico.util.UtilDate;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Stateless
public class ContratosBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ContratosBL.class);

    @Inject
    private ContratosDAO contratosDAO;
    @Inject
    private DocumentosBL documentosBL;

    public void save(ContratosEntity entity, List<DocumentosEntity> documentosEntityList) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setAdjunto(documentosBL.contador(documentosEntityList));
            entity.setEstado(true);
            contratosDAO.save(entity);
            documentosBL.guardarLista(documentosEntityList, entity.getIdContrato());
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(ContratosEntity entity, List<DocumentosEntity> documentosEntityList) {
        try {
            entity.setAdjunto(documentosBL.contador(documentosEntityList));
            contratosDAO.update(entity);
            documentosBL.actualizarLista(documentosEntityList, entity.getIdContrato());
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(ContratosEntity entity, List<DocumentosEntity> documentosEntityList) {
        try {
            entity.setEstado(false);
            contratosDAO.update(entity);
            if (!documentosEntityList.isEmpty()) {
                for (DocumentosEntity documentosEntity : documentosEntityList) {
                    documentosBL.delete(documentosEntity);
                }
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<ContratosEntity> listAll() {
        List<ContratosEntity> list = new ArrayList<>();
        try {
            list = contratosDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<ContratosEntity> listAllOptions(String options) {
        List<ContratosEntity> list = new ArrayList<>();
        try {
            if (options.equals("0")) {
                list = contratosDAO.listAll();
            } else {
                list = contratosDAO.listAllByParameterObj(setOptionObj(options));
            }

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<ContratosEntity> listAllFilter(ContratosEntity contratosEntity, String options) {
        List<ContratosEntity> list = new ArrayList<>();
        Map<String, Object> objectMap = new HashMap<>();
        try {
            if (contratosEntity != null) {
                objectMap.put("nroContrato", contratosEntity.getNroContrato());
                objectMap.put("propietario", contratosEntity.getPropietario());
                objectMap.put("depto", contratosEntity.getDepartamentoByIdDepartamento()
                        .getIdDepartamento() > 0 ? contratosEntity.getDepartamentoByIdDepartamento()
                        .getIdDepartamento() : "");

                objectMap.put("expira", contratosEntity.getExpiracion()
                        != null ? UtilDate.dateToString(contratosEntity.getExpiracion(), "yyyy-MM-dd") : "");
                objectMap.put("expira2", contratosEntity.getExpiracion2()
                        != null ? UtilDate.dateToString(contratosEntity.getExpiracion2(),
                        "yyyy-MM-dd") : "");
                list = contratosDAO.listAllFilter(setOptionObj(options), objectMap);
            }

        } catch (Exception e) {
            logger.log(Level.ERROR, "listAllFilter" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public ContratosEntity getById(long id) {
        ContratosEntity contratosEntity = new ContratosEntity();
        try {
            contratosEntity = contratosDAO.getById(id);
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return contratosEntity;
    }

    private String setOptionObj(String options) {
        String parameter = "";
        switch (options) {
            case "1":
                parameter = "r.servicioByIdServicio";
                break;
            case "2":
                parameter = "r.inmuebleByIdInmueble";
                break;
            case "3":
                parameter = "r.radioBaseByIdRadioBase";
                break;
            default:
                parameter = "";
        }
        return parameter;
    }
}
