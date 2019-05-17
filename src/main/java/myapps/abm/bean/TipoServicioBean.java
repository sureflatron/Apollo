package myapps.abm.bean;

import myapps.abm.business.TipoServicioBL;
import myapps.abm.model.TipoServicioEntity;
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
public class TipoServicioBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(TipoServicioBean.class);

    @Inject
    private TipoServicioBL tipoServicioBL;

    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;

    private List<TipoServicioEntity> tipoServicioEntityList = new ArrayList<>();

    private TipoServicioEntity tipoServicioEntityObj;

    private boolean edit;
    private Map<String, String> validateParameter;

    @PostConstruct
    void init() {
        try {
            tipoServicioEntityObj = new TipoServicioEntity();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            loadTSs();
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void newTS() {
        edit = false;
        tipoServicioEntityObj = new TipoServicioEntity();
    }

    public void actionTS() {
        try {
            if (isEdit() && tipoServicioEntityObj.getIdTipoServicio() > 0) {

                tipoServicioBL.update(tipoServicioEntityObj);
                controlerBitacora.update(DescriptorBitacora.TIPO_SERVICIO, String.valueOf
                        (getTipoServicioEntityObj().getIdTipoServicio()), getTipoServicioEntityObj()
                        .getServicio());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {

                tipoServicioBL.save(tipoServicioEntityObj);
                controlerBitacora.insert(DescriptorBitacora.TIPO_SERVICIO, String.valueOf(getTipoServicioEntityObj()
                        .getIdTipoServicio()), getTipoServicioEntityObj().getServicio());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
            loadTSs();
            newTS();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadTSs() {
        tipoServicioEntityList = tipoServicioBL.listAll();
    }

    public void editTS(TipoServicioEntity tipoServicioEntity) {
        setTipoServicioEntityObj(tipoServicioEntity);
        setEdit(true);

    }

    public void deleteTS(TipoServicioEntity tipoServicioEntity) {
        try {
            setTipoServicioEntityObj(tipoServicioEntity);
            tipoServicioBL.delete(getTipoServicioEntityObj());
            controlerBitacora.delete(DescriptorBitacora.TIPO_SERVICIO, String.valueOf
                    (getTipoServicioEntityObj()
                            .getIdTipoServicio()), getTipoServicioEntityObj().getServicio());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newTS();
            loadTSs();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public List<TipoServicioEntity> getTipoServicioEntityList() {
        return tipoServicioEntityList;
    }

    public void setTipoServicioEntityList(List<TipoServicioEntity> tipoServicioEntityList) {
        this.tipoServicioEntityList = tipoServicioEntityList;
    }

    public TipoServicioEntity getTipoServicioEntityObj() {
        return tipoServicioEntityObj;
    }

    public void setTipoServicioEntityObj(TipoServicioEntity tipoServicioEntityObj) {
        this.tipoServicioEntityObj = tipoServicioEntityObj;
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
