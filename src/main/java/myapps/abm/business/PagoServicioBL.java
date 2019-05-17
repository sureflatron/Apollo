package myapps.abm.business;

import myapps.abm.dao.PagoServicioDAO;
import myapps.abm.model.DocumentosEntity;
import myapps.abm.model.PagoServicioEntity;
import myapps.dashboard.DashboardDto;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PagoServicioBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PagoServicioBL.class);

    @Inject
    private PagoServicioDAO pagoServicioDAO;
    @Inject
    private DocumentosBL documentosBL;

    public void save(PagoServicioEntity entity, List<DocumentosEntity> documentosEntityList) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            entity.setTipoPago("Manual");
            entity.setAdjunto(documentosBL.contador(documentosEntityList));
            pagoServicioDAO.save(entity);
            documentosBL.guardarLista(documentosEntityList, entity.getIdPagoServicio());
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(PagoServicioEntity entity, List<DocumentosEntity> documentosEntityList) {
        try {
            entity.setAdjunto(documentosBL.contador(documentosEntityList));
            pagoServicioDAO.update(entity);
            documentosBL.actualizarLista(documentosEntityList, entity.getIdPagoServicio());
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(PagoServicioEntity entity, List<DocumentosEntity> documentosEntityList) {
        try {
            entity.setEstado(false);
            pagoServicioDAO.update(entity);
            if (!documentosEntityList.isEmpty()) {
                for (DocumentosEntity documentosEntity : documentosEntityList) {
                    documentosBL.delete(documentosEntity);
                }
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<PagoServicioEntity> listAll() {
        List<PagoServicioEntity> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<PagoServicioEntity> listAllByUser(String user) {
        List<PagoServicioEntity> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.listAllByUser(user);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<PagoServicioEntity> listAllByHistorialId(String user, long idHistorial) {
        List<PagoServicioEntity> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.listAllByHistorialId(user, idHistorial);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<PagoServicioEntity> listAllByServicioMes(int mes, int year) {

        List<PagoServicioEntity> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.listAllByServicioMes(mes, year);
            return list;

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<PagoServicioEntity> listAllProveedores() {

        List<PagoServicioEntity> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.listAllProveedores();
            return list;

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<PagoServicioEntity> listAllCantidadMedidorByProveedor(int mes, int year, BigDecimal proveedor) {

        List<PagoServicioEntity> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.listAllCantidadMedidorByProveedor(mes, year, proveedor);
            return list;

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public PagoServicioEntity listAllMedidoresNoPagadosByProveedor(int mes, int year, BigDecimal proveedor) {

        PagoServicioEntity list = new PagoServicioEntity();
        try {
            list = pagoServicioDAO.listAllMedidoresNoPagadosByProveedor(mes, year, proveedor);

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public PagoServicioEntity listAllCantidadPagosByProveedor(int mes, int year, BigDecimal proveedor) {

        PagoServicioEntity pago = new PagoServicioEntity();
        try {
            pago = pagoServicioDAO.listAllCantidadPagosByProveedor(mes, year, proveedor);

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return pago;
    }

    public List<PagoServicioEntity> listAllByMedidorPagado(int mes, int year) {

        List<PagoServicioEntity> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.listAllByMedidorPagado(mes, year);
            return list;

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<PagoServicioEntity> listAllByMedidorNoPagado(int mes, int year) {

        List<PagoServicioEntity> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.listAllByMedidorNoPagado(mes, year);
            return list;

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<PagoServicioEntity> listAllByServicioYear(int year) {

        List<PagoServicioEntity> list = new ArrayList<>();
        try {

            list = pagoServicioDAO.listAllByServicioYear(year);
            return list;

        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }


    public PagoServicioEntity getById(long id) {
        PagoServicioEntity pagoServicioEntity = new PagoServicioEntity();
        try {
            pagoServicioEntity = pagoServicioDAO.getById(id);
            return pagoServicioEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return pagoServicioEntity;
    }


    public void guardar(PagoServicioEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            entity.setEstadoPago("Confirmado");
            entity.setTipoPago("Masivo");
            entity.setAdjunto("0");
            pagoServicioDAO.save(entity);

        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public List<String> obtenerYear() {
        List<String> year = new ArrayList<>();
        try {
            year = pagoServicioDAO.obtenerYear();
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return year;
    }

    public List<DashboardDto> getPagosProveedor(int mes, int year) {
        List<DashboardDto> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.getPagosProveedor(mes, year);
        } catch (Exception e) {
            logger.log(Level.ERROR, "getPagosProveedor" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<DashboardDto> cantMedidores(int mes, int year) {
        List<DashboardDto> list = new ArrayList<>();
        try {
            list = pagoServicioDAO.cantMedidores(mes, year);
        } catch (Exception e) {
            logger.log(Level.ERROR, "cantMedidores" + "|" + e.getMessage(), e);
        }
        return list;
    }

}
