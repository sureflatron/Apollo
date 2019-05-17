package myapps.abm.bean;

import myapps.abm.business.OrdenCompraBL;
import myapps.abm.business.SelectOptions;
import myapps.abm.model.InmuebleEntity;
import myapps.abm.model.OrdenCompraEntity;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.DescriptorBitacora;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static myapps.servicio_basico.util.UtilDate.dateToSql;

@ManagedBean
@ViewScoped
public class OrdenCompraBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(OrdenCompraBean.class);

    @Inject
    private OrdenCompraBL ordenCompraBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @Inject
    private SelectOptions selectOptions;

    @EJB
    private IApplicationContext beanApplicationContext;

    private List<OrdenCompraEntity> ordenCompraEntityList = new ArrayList<>();
    private List<InmuebleEntity> inmuebleEntityList = new ArrayList<>();

    private OrdenCompraEntity ordenCompraEntityObj;
    private InmuebleEntity inmuebleEntityObj;
    private Map<String, String> validateParameter;
    private boolean editOrdenCompra;
    private Date fecha;

    @PostConstruct
    void init() {
        try {
            ordenCompraEntityObj = new OrdenCompraEntity();
            inmuebleEntityObj = new InmuebleEntity();
            fecha = new Date();
            inmuebleEntityList = selectOptions.cbxImnueble();
            loadOrdenCompras();
            validateParameter = beanApplicationContext.getMapValidateParameters();
        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }

    public void newOrdenCompra() {
        ordenCompraEntityObj = new OrdenCompraEntity();
        setEditOrdenCompra(false);
        inmuebleEntityObj.setIdInmueble(-1);

    }

    public void actionOrdenCompra() {
        try {
            if (isEditOrdenCompra() && ordenCompraEntityObj.getIdOc() > 0) {
                ordenCompraEntityObj.setInmuebleByIdInmueble(inmuebleEntityObj);
                ordenCompraEntityObj.setFecha(dateToSql(fecha));
                ordenCompraBL.update(ordenCompraEntityObj);
                controlerBitacora.update(DescriptorBitacora.ORDEN_COMPRA, String.valueOf
                        (getOrdenCompraEntityObj().getIdOc()), getOrdenCompraEntityObj()
                        .getNroOrden());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                ordenCompraEntityObj.setInmuebleByIdInmueble(inmuebleEntityObj);
                ordenCompraEntityObj.setFecha(dateToSql(fecha));
                ordenCompraBL.save(ordenCompraEntityObj);
                controlerBitacora.insert(DescriptorBitacora.ORDEN_COMPRA, String.valueOf
                        (getOrdenCompraEntityObj()
                                .getIdOc()), getOrdenCompraEntityObj().getNroOrden());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
            loadOrdenCompras();
            newOrdenCompra();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }


    private void loadOrdenCompras() {
        ordenCompraEntityList = ordenCompraBL.listAll();
    }

    public void editarOrdenCompra(OrdenCompraEntity ordenCompraEntity) {
        setOrdenCompraEntityObj(ordenCompraEntity);
        setEditOrdenCompra(true);
        setInmuebleEntityObj(getOrdenCompraEntityObj().getInmuebleByIdInmueble());
        setFecha(getOrdenCompraEntityObj().getFecha());

    }

    public void deleteOrdenCompra(OrdenCompraEntity ordenCompraEntity) {
        try {
            setOrdenCompraEntityObj(ordenCompraEntity);
            ordenCompraBL.delete(getOrdenCompraEntityObj());
            controlerBitacora.delete(DescriptorBitacora.ORDEN_COMPRA, String.valueOf
                    (getOrdenCompraEntityObj()
                            .getIdOc()), getOrdenCompraEntityObj().getNroOrden());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newOrdenCompra();
            loadOrdenCompras();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public List<OrdenCompraEntity> getOrdenCompraEntityList() {
        return ordenCompraEntityList;
    }

    public void setOrdenCompraEntityList(List<OrdenCompraEntity> ordenCompraEntityList) {
        this.ordenCompraEntityList = ordenCompraEntityList;
    }

    public OrdenCompraEntity getOrdenCompraEntityObj() {
        return ordenCompraEntityObj;
    }

    public void setOrdenCompraEntityObj(OrdenCompraEntity ordenCompraEntityObj) {
        this.ordenCompraEntityObj = ordenCompraEntityObj;
    }

    public boolean isEditOrdenCompra() {
        return editOrdenCompra;
    }

    public void setEditOrdenCompra(boolean editarTorres) {
        this.editOrdenCompra = editarTorres;
    }


    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public List<InmuebleEntity> getInmuebleEntityList() {
        return inmuebleEntityList;
    }

    public void setInmuebleEntityList(List<InmuebleEntity> inmuebleEntityList) {
        this.inmuebleEntityList = inmuebleEntityList;
    }

    public InmuebleEntity getInmuebleEntityObj() {
        return inmuebleEntityObj;
    }

    public void setInmuebleEntityObj(InmuebleEntity inmuebleEntityObj) {
        this.inmuebleEntityObj = inmuebleEntityObj;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
