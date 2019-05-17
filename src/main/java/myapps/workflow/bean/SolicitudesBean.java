package myapps.workflow.bean;

import myapps.abm.business.SelectOptions;
import myapps.abm.model.ServicioEntity;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.DescriptorBitacora;
import myapps.workflow.business.SolicitudesBL;
import myapps.workflow.entity.SolicitudesEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class SolicitudesBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(SolicitudesBean.class);

    @Inject
    private SolicitudesBL solicitudesBL;
    @Inject
    private SelectOptions selectOptions;
    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;

    private transient List<SolicitudesEntity> solicitudesEntityList = new ArrayList<>();
    private List<ServicioEntity> servicioEntityList = new ArrayList<>();
    private transient SolicitudesEntity solicitudesEntityObj;
    private ServicioEntity servicioEntityObj;
    private transient SolicitudesEntity selectSolicitudObj;
    private boolean edit;
    private Map<String, String> validateParameter;

    @PostConstruct
    void init() {
        try {
            solicitudesEntityObj = new SolicitudesEntity();
            solicitudesEntityObj.setUnidad("Telecel S.A");
            solicitudesEntityObj.setTipoSolicitud("Alta");
            solicitudesEntityObj.setFechaSolicitud(new Date());
            servicioEntityObj = new ServicioEntity();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            servicioEntityList = selectOptions.cbxOnlyServicio();
            loadSolicitudes();
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void newSolicitud() {
        edit = false;
        solicitudesEntityObj = new SolicitudesEntity();
        solicitudesEntityObj.setUnidad("Telecel S.A");
        servicioEntityObj = new ServicioEntity();
    }

    public void actionSolicitud() {
        try {
            if (isEdit() && solicitudesEntityObj.getIdSolicitud() > 0) {
                solicitudesEntityObj.setServicioByIdServicio(servicioEntityObj);

                solicitudesBL.update(solicitudesEntityObj);
                controlerBitacora.update(DescriptorBitacora.SOLICITUD,
                        String.valueOf(getSolicitudesEntityObj().getIdSolicitud()),
                        getSolicitudesEntityObj().getServicioByIdServicio().getNombreSite());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()),
                        "Actualización satisfactoria", "");
            } else {

                try {
                    Long idProceso = new Long(1234567); /* JbpmApi.startProcess(); */

                    solicitudesEntityObj.setServicioByIdServicio(servicioEntityObj);
                    solicitudesEntityObj.setFechaRegistro(new Date());

                    solicitudesEntityObj.setIdProceso(idProceso);

                    solicitudesBL.save(solicitudesEntityObj);
                    controlerBitacora.insert(DescriptorBitacora.SOLICITUD,
                            String.valueOf(getSolicitudesEntityObj().getIdSolicitud()),
                            getSolicitudesEntityObj().getServicioByIdServicio().getNombreSite());
                    SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()),
                            "Registro satisfactorio", "");

                } catch (Exception e) {
                    log.info("VEHIMAR: " + e.getMessage());
                }
            }

            loadSolicitudes();
            newSolicitud();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()),
                    "Error al guardar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadSolicitudes() {
        solicitudesEntityList = solicitudesBL.listAll();
    }

    public void editSolicitud() {
        setSolicitudesEntityObj(solicitudesBL.getById((Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("idSolicitud")))));
        setServicioEntityObj(getSolicitudesEntityObj().getServicioByIdServicio());
        setEdit(true);
    }

    public void deleteSolicitud() {
        try {
            setSolicitudesEntityObj(solicitudesBL.getById((Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("idSolicitud")))));
            solicitudesBL.delete(getSolicitudesEntityObj());
            controlerBitacora.delete(DescriptorBitacora.SOLICITUD,
                    String.valueOf(getSolicitudesEntityObj().getIdSolicitud()),
                    getSolicitudesEntityObj().getServicioByIdServicio().getNombreSite());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro",
                    "");
            newSolicitud();
            loadSolicitudes();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()),
                    "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public List<SolicitudesEntity> getSolicitudesEntityList() {
        return solicitudesEntityList;
    }

    public void setSolicitudesEntityList(List<SolicitudesEntity> solicitudesEntityList) {
        this.solicitudesEntityList = solicitudesEntityList;
    }

    public SolicitudesEntity getSolicitudesEntityObj() {
        return solicitudesEntityObj;
    }

    public void setSolicitudesEntityObj(SolicitudesEntity solicitudesEntityObj) {
        this.solicitudesEntityObj = solicitudesEntityObj;
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

    public List<ServicioEntity> getServicioEntityList() {
        return servicioEntityList;
    }

    public void setServicioEntityList(List<ServicioEntity> servicioEntityList) {
        this.servicioEntityList = servicioEntityList;
    }

    public SolicitudesEntity getSelectSolicitudObj() {
        return selectSolicitudObj;
    }

    public void setSelectSolicitudObj(SolicitudesEntity selectSolicitudObj) {
        this.selectSolicitudObj = selectSolicitudObj;
    }

    public ServicioEntity getServicioEntityObj() {
        return servicioEntityObj;
    }

    public void setServicioEntityObj(ServicioEntity servicioEntityObj) {
        this.servicioEntityObj = servicioEntityObj;
    }
}
