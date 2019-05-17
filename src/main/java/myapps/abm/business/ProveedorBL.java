package myapps.abm.business;

import myapps.abm.dao.ProveedorDAO;
import myapps.abm.model.FusionEntity;
import myapps.abm.model.ProveedorEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProveedorBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ProveedorBL.class);

    @Inject
    private ProveedorDAO proveedorDAO;

    public void save(ProveedorEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado((long) 1);
            proveedorDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(ProveedorEntity entity) {
        try {
            proveedorDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(ProveedorEntity entity) {
        try {
            entity.setEstado((long) 0);
            proveedorDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<ProveedorEntity> listAll() {
        List<ProveedorEntity> list = new ArrayList<>();
        try {
            list = proveedorDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<ProveedorEntity> listAllFusion(long idProveedor) {
        List<ProveedorEntity> list = new ArrayList<>();
        try {
            list = proveedorDAO.listAllFusion(idProveedor);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<FusionEntity> getFusionByProveedor(long idProveedor) {
        return proveedorDAO.getFusionByProveedor(idProveedor);
    }

    public void saveFusion(FusionEntity fusion) throws Exception {
        proveedorDAO.saveFusion(fusion);
    }

    public ProveedorEntity getProveedordById(long idProv) {
        ProveedorEntity proveedorEntity = new ProveedorEntity();
        try {
            return proveedorDAO.getById(idProv);
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return proveedorEntity;
    }


    public void deleteFusionProveedores(long idProveedor) {
        try {
            proveedorDAO.deleteFusionProveedor(idProveedor);
        } catch (Exception e) {
            logger.log(Level.ERROR, "deleteFusionProveedores" + "|" + e.getMessage(), e);
        }
    }

    public void deleteFusionByIdProveedor(long idProveedor) throws Exception {
        proveedorDAO.deleteFusionByIdProveedor(idProveedor);
    }


    public ProveedorEntity buscar(String nombre) {
        ProveedorEntity proveedorEntity = new ProveedorEntity();
        try {
            if (nombre.equals("")) {
                proveedorEntity = proveedorDAO.buscar(nombre);
            }

        } catch (Exception e) {
            logger.log(Level.ERROR, "buscar - BL" + "|" + e.getMessage(), e);
        }

        return proveedorEntity;
    }

    public ProveedorEntity getProveedorByNombre(String nombre) {
        ProveedorEntity proveedorEntity = new ProveedorEntity();
        try {
            if (!nombre.equals("")) {
                proveedorEntity = proveedorDAO.getProveedorByNombre(nombre.toUpperCase());
            }

        } catch (Exception e) {
            logger.log(Level.ERROR, "getProveedorByNombre - BL" + "|" + e.getMessage(), e);
        }

        return proveedorEntity;
    }

}
