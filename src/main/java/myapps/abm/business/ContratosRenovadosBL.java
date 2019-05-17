package myapps.abm.business;

import myapps.abm.dao.ContratosRenovadosDAO;
import myapps.abm.model.ContratosEntity;
import myapps.abm.model.ContratosRenovadosEntity;
import myapps.abm.model.HistorialContratosEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ContratosRenovadosBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ContratosRenovadosBL.class);

    @Inject
    private ContratosRenovadosDAO contratosRenovadosDAO;
    @Inject
    private HistorialContratosBL historialContratosBL;

    public void save(ContratosEntity entity, HistorialContratosEntity historialContratosEntity) {
        logger.log(Level.INFO, "SAVE");
        try {
            ContratosRenovadosEntity renovadosEntity = new ContratosRenovadosEntity();

            renovadosEntity.setNroContrato(entity.getNroContrato());
            renovadosEntity.setResponsable(entity.getResponsable());
            renovadosEntity.setPropietario(entity.getPropietario());
            renovadosEntity.setCaducidad(entity.getCaducidad());
            renovadosEntity.setExpiracion(entity.getExpiracion());
            renovadosEntity.setTipo(entity.getTipo());
            renovadosEntity.setCatInm(entity.getCatInm());
            renovadosEntity.setFechaCreacion(entity.getFechaCreacion());
            renovadosEntity.setCatCont(entity.getCatCont());
            renovadosEntity.setAdjunto(entity.getAdjunto());
            renovadosEntity.setCodEbs(entity.getCodEbs());
            renovadosEntity.setCanonAlquiler(entity.getCanonAlquiler());
            renovadosEntity.setEstado(entity.getEstado());

            renovadosEntity.setServicioByIdServicio(entity.getServicioByIdServicio());
            renovadosEntity.setInmuebleByIdInmueble(entity.getInmuebleByIdInmueble());
            renovadosEntity.setRadioBaseByIdRadioBase(entity.getRadioBaseByIdRadioBase());
            renovadosEntity.setProveedorByIdProveedor(entity.getProveedorByIdProveedor());
            renovadosEntity.setDepartamentoByIdDepartamento(entity.getDepartamentoByIdDepartamento());

            renovadosEntity.setFechaRegistro(entity.getFechaRegistro());
            renovadosEntity.setCategoria(entity.getCategoria());
            renovadosEntity.setMonto(entity.getMonto());
            renovadosEntity.setEstadoContrato(entity.getEstadoContrato());
            renovadosEntity.setDias(entity.getDias());

           contratosRenovadosDAO.save(renovadosEntity);
            historialContratosEntity.setContratosRenovadosByContratoRevoId(renovadosEntity);
            historialContratosBL.save(historialContratosEntity);

        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(ContratosRenovadosEntity entity) {
        try {
            contratosRenovadosDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(ContratosRenovadosEntity entity) {
        try {

            contratosRenovadosDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<ContratosRenovadosEntity> listAll() {
        List<ContratosRenovadosEntity> entityList = new ArrayList<>();
        try {
            entityList = contratosRenovadosDAO.listAll();

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return entityList;
    }

    public ContratosRenovadosEntity getById(long id) {
        ContratosRenovadosEntity entity= new ContratosRenovadosEntity();
        try {
            entity = contratosRenovadosDAO.getById(id);
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return entity;
    }



}
