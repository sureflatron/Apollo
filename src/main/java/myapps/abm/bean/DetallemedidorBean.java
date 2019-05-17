package myapps.abm.bean;

import myapps.abm.business.DetalleMedidorBL;
import myapps.abm.business.MedidorBL;
import myapps.abm.business.SelectOptions;
import myapps.abm.model.DetalleMedidorEntity;
import myapps.abm.model.MedidorEntity;
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


@ManagedBean
@ViewScoped
public class DetallemedidorBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(DetallemedidorBean.class);

    @Inject
    private DetalleMedidorBL detalleMedidorBL;
    @Inject
    private MedidorBL medidorBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @Inject
    private SelectOptions selectOptions;
    @EJB
    private IApplicationContext beanApplicationContext;

    private List<DetalleMedidorEntity> detalleMedidorEntityList = new ArrayList<>();
    private List<MedidorEntity> medidorEntityList = new ArrayList<>();

    private DetalleMedidorEntity detalleMedidorEntityObj;
    private MedidorEntity medidorEntityObj;

    private boolean edit;
    private Map<String, String> validateParameter;
    private DetalleMedidorEntity selectedDetalleMedidor;

    @PostConstruct
    void init() {
        try {
            detalleMedidorEntityObj = new DetalleMedidorEntity();
            medidorEntityObj = new MedidorEntity();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            medidorEntityList = selectOptions.cbxMedidor();
            loadDetalleMedidors();
            detalleMedidorEntityObj.setFechaEmision(new Date());
            detalleMedidorEntityObj.setFechaVencimiento(new Date());
            detalleMedidorEntityObj.setMes(new Date());
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void newDetalleMedidor() {
        edit = false;
        medidorEntityObj = new MedidorEntity();
        detalleMedidorEntityObj = new DetalleMedidorEntity();
        detalleMedidorEntityObj.setFechaEmision(new Date());
        detalleMedidorEntityObj.setMes(new Date());
        detalleMedidorEntityObj.setFechaVencimiento(new Date());
    }

    public void actionDetalleMedidor() {
        try {
            if (isEdit() && detalleMedidorEntityObj.getIdDetalleMedidor() > 0) {
                detalleMedidorEntityObj.setMedidorByIdMedidor(medidorEntityObj);
                detalleMedidorBL.update(detalleMedidorEntityObj);
                controlerBitacora.update(DescriptorBitacora.UNIDAD_OPERATIVA, String.valueOf
                        (getDetalleMedidorEntityObj().getIdDetalleMedidor()), getDetalleMedidorEntityObj()
                        .getMes().toString());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                detalleMedidorEntityObj.setMedidorByIdMedidor(medidorEntityObj);
                detalleMedidorBL.save(detalleMedidorEntityObj);
                controlerBitacora.insert(DescriptorBitacora.UNIDAD_OPERATIVA, String.valueOf(getDetalleMedidorEntityObj()
                        .getIdDetalleMedidor()), getDetalleMedidorEntityObj().getMes().toString());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }

            loadDetalleMedidors();
            newDetalleMedidor();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadDetalleMedidors() {
        detalleMedidorEntityList = detalleMedidorBL.listAll();
    }

    public void editDetalleMedidor(DetalleMedidorEntity detalleMedidorEntity) {
        setDetalleMedidorEntityObj(detalleMedidorEntity);
        setMedidorEntityObj(getDetalleMedidorEntityObj().getMedidorByIdMedidor());
        setEdit(true);
    }

    public void deleteDetalleMedidor(DetalleMedidorEntity detalleMedidorEntity) {

        try {
            setDetalleMedidorEntityObj(detalleMedidorEntity);
            detalleMedidorBL.delete(getDetalleMedidorEntityObj());
            controlerBitacora.delete(DescriptorBitacora.UNIDAD_OPERATIVA, String.valueOf(getDetalleMedidorEntityObj()
                    .getIdDetalleMedidor()), getDetalleMedidorEntityObj().getMes().toString());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newDetalleMedidor();
            loadDetalleMedidors();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }


    public void cargarMedidor() {
        if (getMedidorEntityObj().getIdMedidor() > 0) {
            MedidorEntity datos = medidorBL.getById(getMedidorEntityObj().getIdMedidor());
            setMedidorEntityObj(datos);
        } else {
            medidorEntityObj = new MedidorEntity();
        }
    }

    public List<DetalleMedidorEntity> getDetalleMedidorEntityList() {
        return detalleMedidorEntityList;
    }

    public void setDetalleMedidorEntityList(List<DetalleMedidorEntity> detalleMedidorEntityList) {
        this.detalleMedidorEntityList = detalleMedidorEntityList;
    }

    public DetalleMedidorEntity getDetalleMedidorEntityObj() {
        return detalleMedidorEntityObj;
    }

    public void setDetalleMedidorEntityObj(DetalleMedidorEntity detalleMedidorEntityObj) {
        this.detalleMedidorEntityObj = detalleMedidorEntityObj;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public List<MedidorEntity> getMedidorEntityList() {
        return medidorEntityList;
    }

    public void setMedidorEntityList(List<MedidorEntity> medidorEntityList) {
        this.medidorEntityList = medidorEntityList;
    }

    public MedidorEntity getMedidorEntityObj() {
        return medidorEntityObj;
    }

    public void setMedidorEntityObj(MedidorEntity medidorEntityObj) {
        this.medidorEntityObj = medidorEntityObj;
    }

    public DetalleMedidorEntity getSelectedDetalleMedidor() {
        return selectedDetalleMedidor;
    }

    public void setSelectedDetalleMedidor(DetalleMedidorEntity selectedDetalleMedidor) {
        this.selectedDetalleMedidor = selectedDetalleMedidor;
    }
}
