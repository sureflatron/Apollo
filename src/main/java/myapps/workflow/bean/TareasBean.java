package myapps.workflow.bean;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.DescriptorBitacora;
import myapps.workflow.business.TareasBL;
import myapps.workflow.entity.TareasEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

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
public class TareasBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(TareasBean.class);


    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;

    private transient List<TareasEntity> tareasEntityList = new ArrayList<>();
    private transient TareasEntity tareasEntity;
    private transient TareasEntity selectTareasEntity;
    private boolean edit;
    private Map<String, String> validateParameter;

    @PostConstruct
    void init() {
        try {
            tareasEntity = new TareasEntity();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            loadUOs();
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void newUO() {
        edit = false;
        tareasEntity = new TareasEntity();
    }

    public void actionUO() {
        try {
            if (isEdit() && tareasEntity.getIdProcedimiento() > 0) {
                // tareasBL.update(tareasEntity);
                controlerBitacora.update(DescriptorBitacora.UNIDAD_OPERATIVA, String.valueOf
                        (getTareasEntity().getIdProcedimiento()), getTareasEntity()
                        .getTarea());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {

                //tareasBL.save(tareasEntity);
                controlerBitacora.insert(DescriptorBitacora.UNIDAD_OPERATIVA, String.valueOf(getTareasEntity()
                        .getIdProcedimiento()), getTareasEntity().getTarea());
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
    	/*VEHIMAR*/
        /*tareasEntityList =  JbpmApi.getTasks(new Long(1236547));*/
    	TareasBL a = new TareasBL();
        tareasEntityList = a.listAll();
    }

    public void editUO(TareasEntity tareasEntity) {
        setTareasEntity(tareasEntity);
        setEdit(true);
    }

    public void deleteUO(TareasEntity tareasEntity) {
        try {
            setTareasEntity(tareasEntity);
            // tareasBL.delete(getTareasEntity());
            controlerBitacora.delete(DescriptorBitacora.UNIDAD_OPERATIVA, String.valueOf
                    (getTareasEntity()
                            .getIdProcedimiento()), getTareasEntity().getTarea());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newUO();
            loadUOs();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public List<TareasEntity> getTareasEntityList() {
        return tareasEntityList;
    }

    public void setTareasEntityList(List<TareasEntity> tareasEntityList) {
        this.tareasEntityList = tareasEntityList;
    }

    public TareasEntity getTareasEntity() {
        return tareasEntity;
    }

    public void setTareasEntity(TareasEntity tareasEntity) {
        this.tareasEntity = tareasEntity;
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

    public TareasEntity getSelectTareasEntity() {
        return selectTareasEntity;
    }

    public void setSelectTareasEntity(TareasEntity selectTareasEntity) {
        this.selectTareasEntity = selectTareasEntity;
    }


    public void mensaje(int index) {
        switch (index) {
            case 1:
                selectTareasEntity.setEstado("Aprobado");
                //JbpmApi.taskComplete(selectTareasEntity.getComentario(), selectTareasEntity.getIdProcedimiento(), "aprobado");
                
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Tarea Aprobada", null);
                break;
            case 2:
                selectTareasEntity.setEstado("Rechazado");
                
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Tarea Rechazada", null);
                break;
            case 3:
                selectTareasEntity.setEstado("Cancelado");
                
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Tarea Cancelada", null);
                break;
            default:
                log.error("Variable no definida");
                break;
        }
        selectTareasEntity = new TareasEntity();
        PrimeFaces.current().executeScript("PF('carTarea').hide()");
    }

    public String color(TareasEntity tareasEntity) {
        String color = "background: white";
        if (tareasEntity.getEstado() != null) {
            switch (tareasEntity.getEstado()) {
                case "Aprobado":
                    color = "verde";
                    break;
                case "Rechazado":
                    color = "naranja";
                    break;
                case "Cancelado":
                    color = "rojo";
                    break;
                default:
                    color = "background: white";
                    break;
            }
        }
        return color;
    }
}
