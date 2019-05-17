package myapps.abm.bean;

import myapps.abm.business.ConstructorBL;
import myapps.abm.model.ConstructorEntity;
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
public class ConstructorBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(ConstructorBean.class);

    @Inject
    private ConstructorBL constructorBL;

    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;
    private List<ConstructorEntity> constructorEntityList = new ArrayList<>();
    private ConstructorEntity constructorEntityObj;
    private boolean edit;
    private Map<String, String> validateParameter;

    @PostConstruct
    void init() {
        try {
            constructorEntityObj = new ConstructorEntity();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            loadConstructors();
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void newConstructor() {
        edit = false;
        constructorEntityObj = new ConstructorEntity();
    }

    public void actionConstructor() {
        try {
            if (isEdit() && constructorEntityObj.getIdConstructor() > 0) {
                constructorBL.update(constructorEntityObj);
                controlerBitacora.update(DescriptorBitacora.CONSTRUCTOR, String.valueOf
                        (getConstructorEntityObj().getIdConstructor()), getConstructorEntityObj()
                        .getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                constructorBL.save(constructorEntityObj);
                controlerBitacora.insert(DescriptorBitacora.CONSTRUCTOR, String.valueOf(getConstructorEntityObj()
                        .getIdConstructor()), getConstructorEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
            loadConstructors();
            newConstructor();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadConstructors() {
        constructorEntityList = constructorBL.listAll();
    }

    public void editConstructor(ConstructorEntity constructorEntity) {
        setConstructorEntityObj(constructorEntity);
        setEdit(true);
    }

    public void deleteConstructor(ConstructorEntity constructorEntity) {
        try {
            setConstructorEntityObj(constructorEntity);
            constructorBL.delete(getConstructorEntityObj());
            controlerBitacora.delete(DescriptorBitacora.CONSTRUCTOR, String.valueOf
                    (getConstructorEntityObj()
                            .getIdConstructor()), getConstructorEntityObj().getNombre());

            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newConstructor();
            loadConstructors();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public List<ConstructorEntity> getConstructorEntityList() {
        return constructorEntityList;
    }

    public void setConstructorEntityList(List<ConstructorEntity> constructorEntityList) {
        this.constructorEntityList = constructorEntityList;
    }

    public ConstructorEntity getConstructorEntityObj() {
        return constructorEntityObj;
    }

    public void setConstructorEntityObj(ConstructorEntity constructorEntityObj) {
        this.constructorEntityObj = constructorEntityObj;
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
