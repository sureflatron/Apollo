package myapps.abm.bean;

import myapps.abm.business.UOBL;
import myapps.abm.model.UnidadOperativaEntity;
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
import java.util.List;
import java.util.Map;


@ManagedBean
@ViewScoped
public class UOBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(UOBean.class);

    @Inject
    private UOBL uoBL;

    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;

    private List<UnidadOperativaEntity> unidadOperativaEntityList = new ArrayList<>();
    private UnidadOperativaEntity unidadOperativaEntityObj;
    private boolean edit;
    private Map<String, String> validateParameter;

    @PostConstruct
    void init() {
        try {
            unidadOperativaEntityObj = new UnidadOperativaEntity();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            loadUOs();
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void newUO() {
        edit = false;
        unidadOperativaEntityObj = new UnidadOperativaEntity();
    }

    public void actionUO() {
        try {
            if (isEdit() && unidadOperativaEntityObj.getIdUnidadOperativa() > 0) {
                uoBL.update(unidadOperativaEntityObj);
                controlerBitacora.update(DescriptorBitacora.UNIDAD_OPERATIVA, String.valueOf
                        (getUnidadOperativaEntityObj().getIdUnidadOperativa()), getUnidadOperativaEntityObj()
                        .getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {

                uoBL.save(unidadOperativaEntityObj);
                controlerBitacora.insert(DescriptorBitacora.UNIDAD_OPERATIVA, String.valueOf(getUnidadOperativaEntityObj()
                        .getIdUnidadOperativa()), getUnidadOperativaEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }

            loadUOs();
            newUO();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadUOs() {
        unidadOperativaEntityList = uoBL.listAll();
    }

    public void editUO(UnidadOperativaEntity unidadOperativaEntity) {
        setUnidadOperativaEntityObj(unidadOperativaEntity);
        setEdit(true);
    }

    public void deleteUO(UnidadOperativaEntity unidadOperativaEntity) {
        try {
            setUnidadOperativaEntityObj(unidadOperativaEntity);
            uoBL.delete(getUnidadOperativaEntityObj());
            controlerBitacora.delete(DescriptorBitacora.UNIDAD_OPERATIVA, String.valueOf
                    (getUnidadOperativaEntityObj()
                            .getIdUnidadOperativa()), getUnidadOperativaEntityObj().getNombre());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newUO();
            loadUOs();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public List<UnidadOperativaEntity> getUnidadOperativaEntityList() {
        return unidadOperativaEntityList;
    }

    public void setUnidadOperativaEntityList(List<UnidadOperativaEntity> unidadOperativaEntityList) {
        this.unidadOperativaEntityList = unidadOperativaEntityList;
    }

    public UnidadOperativaEntity getUnidadOperativaEntityObj() {
        return unidadOperativaEntityObj;
    }

    public void setUnidadOperativaEntityObj(UnidadOperativaEntity unidadOperativaEntityObj) {
        this.unidadOperativaEntityObj = unidadOperativaEntityObj;
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
}
