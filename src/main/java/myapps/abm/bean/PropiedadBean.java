package myapps.abm.bean;

import myapps.abm.business.PropiedadBL;
import myapps.abm.model.PropiedadEntity;
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
public class PropiedadBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(PropiedadBean.class);

    @Inject
    private PropiedadBL propiedadBL;

    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;

    private List<PropiedadEntity> propiedadEntityList = new ArrayList<>();
    private PropiedadEntity propiedadEntityObj;
    private boolean edit;
    private Map<String, String> validateParameter;

    @PostConstruct
    void init() {
        try {
            propiedadEntityObj = new PropiedadEntity();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            loadPropiedadess();
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void newPropiedad() {
        edit = false;
        propiedadEntityObj = new PropiedadEntity();
    }

    public void actionPropiedad() {
        try {
            if (isEdit() && propiedadEntityObj.getIdPropiedad() > 0) {
                propiedadBL.update(propiedadEntityObj);
                controlerBitacora.update(DescriptorBitacora.PROPIEDAD, String.valueOf
                        (getPropiedadEntityObj().getIdPropiedad()), getPropiedadEntityObj()
                        .getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                propiedadBL.save(propiedadEntityObj);
                controlerBitacora.insert(DescriptorBitacora.PROPIEDAD, String.valueOf(getPropiedadEntityObj()
                        .getIdPropiedad()), getPropiedadEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
            loadPropiedadess();
            newPropiedad();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadPropiedadess() {
        propiedadEntityList = propiedadBL.listAll(true);
    }

    public void editPropiedad(PropiedadEntity propiedadEntity) {
        setPropiedadEntityObj(propiedadEntity);
        setEdit(true);
    }

    public void deletePropiedad(PropiedadEntity propiedadEntity) {
        try {
            setPropiedadEntityObj(propiedadEntity);
            propiedadBL.delete(getPropiedadEntityObj());
            controlerBitacora.delete(DescriptorBitacora.PROPIEDAD, String.valueOf
                    (getPropiedadEntityObj()
                            .getIdPropiedad()), getPropiedadEntityObj().getNombre());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newPropiedad();
            loadPropiedadess();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public List<PropiedadEntity> getPropiedadEntityList() {
        return propiedadEntityList;
    }

    public void setPropiedadEntityList(List<PropiedadEntity> propiedadEntityList) {
        this.propiedadEntityList = propiedadEntityList;
    }

    public PropiedadEntity getPropiedadEntityObj() {
        return propiedadEntityObj;
    }

    public void setPropiedadEntityObj(PropiedadEntity propiedadEntityObj) {
        this.propiedadEntityObj = propiedadEntityObj;
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
