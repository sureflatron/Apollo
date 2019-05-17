package myapps.abm.bean;

import myapps.abm.business.MedidorBL;
import myapps.abm.business.SelectOptions;
import myapps.abm.business.ServicioBL;
import myapps.abm.model.MedidorEntity;
import myapps.abm.model.ServicioEntity;
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
public class MedidorBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(MedidorBean.class);

    @Inject
    private MedidorBL medidorBL;
    @Inject
    private ServicioBL servicioBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @Inject
    private SelectOptions selectOptions;
    @EJB

    private IApplicationContext beanApplicationContext;

    private List<MedidorEntity> medidorEntityList = new ArrayList<>();
    private List<ServicioEntity> servicioEntityList = new ArrayList<>();

    private MedidorEntity medidorEntityObj;
    private ServicioEntity servicioEntityObj;
    private Map<String, String> validateParameter;
    private boolean editMedidor;
    private long selectIdMedidor;

    @PostConstruct
    void init() {
        try {
            medidorEntityObj = new MedidorEntity();
            servicioEntityObj = new ServicioEntity();
            loadTipoMedidor();
            validateParameter = beanApplicationContext.getMapValidateParameters();

        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }


    public void newMedidor() {
        medidorEntityObj = new MedidorEntity();
        servicioEntityObj = new ServicioEntity();
        loadTipoMedidor();
        setEditMedidor(false);
        setSelectIdMedidor((long) 0);
    }

    public void actionMedidor() {
        try {
            MedidorEntity objeto = medidorBL.repetidos(servicioEntityObj.getIdServicio());
            if (objeto.getNombre() != null) {
                setSelectIdMedidor(objeto.getIdMedidor());
                setEditMedidor(true);
                if (!editMedidor) {
                    medidorEntityObj.setIdMedidor(objeto.getIdMedidor());
                }
                medidorEntityObj.setEstado(true);
            }
            if (isEditMedidor() && medidorEntityObj.getIdMedidor() > 0) {
                medidorEntityObj.setServicioByIdServicio(servicioEntityObj);
                medidorBL.update(medidorEntityObj);
                controlerBitacora.update(DescriptorBitacora.MEDIDOR, String.valueOf
                        (getMedidorEntityObj().getIdMedidor()), getMedidorEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");

            } else {
                medidorEntityObj.setServicioByIdServicio(servicioEntityObj);
                medidorBL.save(medidorEntityObj);
                controlerBitacora.insert(DescriptorBitacora.MEDIDOR, String.valueOf
                        (getMedidorEntityObj().getIdMedidor()), getMedidorEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
            loadTipoMedidor();
            newMedidor();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadTipoMedidor() {
        medidorEntityList = medidorBL.listAll();
        servicioEntityList = selectOptions.cbxServicionotMedidor();
        if (servicioEntityList.isEmpty()) {
            servicioEntityObj.setIdServicio(-2);
            servicioEntityObj.setNombreSite("No Existe Servicios Disponibles");
            servicioEntityList.add(servicioEntityObj);
        }
    }

    public void editarMedidor(MedidorEntity medidor) {
        setSelectIdMedidor(medidor.getIdMedidor());
        servicioEntityList = selectOptions.cbxServicionotMedidor();
        setMedidorEntityObj(medidor);
        setServicioEntityObj(medidor.getServicioByIdServicio());
        getServicioEntityList().add(medidor.getServicioByIdServicio());
        setEditMedidor(true);
    }

    public void deleteMedidor(MedidorEntity medidor) {
        try {
            medidorBL.delete(medidor);
            controlerBitacora.delete(DescriptorBitacora.MEDIDOR, String.valueOf
                    (getMedidorEntityObj().getIdMedidor()), getMedidorEntityObj().getNombre());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newMedidor();
            loadTipoMedidor();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public void cargarServicio() {
        if (getServicioEntityObj().getIdServicio() > 0) {
            ServicioEntity datos = servicioBL.getById(getServicioEntityObj().getIdServicio());
            setServicioEntityObj(datos);
        } else {
            servicioEntityObj = new ServicioEntity();
        }
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

    public long getSelectIdMedidor() {
        return selectIdMedidor;
    }

    public void setSelectIdMedidor(long selectIdMedidor) {
        this.selectIdMedidor = selectIdMedidor;
    }

    public boolean isEditMedidor() {
        return editMedidor;
    }

    public void setEditMedidor(boolean editarMedidor) {
        this.editMedidor = editarMedidor;
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

    public ServicioEntity getServicioEntityObj() {
        return servicioEntityObj;
    }

    public void setServicioEntityObj(ServicioEntity servicioEntityObj) {
        this.servicioEntityObj = servicioEntityObj;
    }


}
