package myapps.abm.bean;

import myapps.abm.business.LocalidadBL;
import myapps.abm.business.SelectOptions;
import myapps.abm.model.DepartamentoEntity;
import myapps.abm.model.LocalidadEntity;
import myapps.abm.model.MunicipioEntity;
import myapps.abm.model.ProvinciaEntity;
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
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@ManagedBean
@ViewScoped
public class LocalidadBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(LocalidadBean.class);

    @Inject
    private LocalidadBL localidadBL;

    @Inject
    private SelectOptions selectOptions;

    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;

    private List<LocalidadEntity> localidadEntityList = new ArrayList<>();
    private List<MunicipioEntity> municipioEntityList = new ArrayList<>();
    private List<DepartamentoEntity> departamentoEntityList = new ArrayList<>();
    private List<ProvinciaEntity> provinciaEntityList = new ArrayList<>();

    private MunicipioEntity municipioEntityObj;
    private LocalidadEntity localidadEntityObj;
    private ProvinciaEntity provinciaEntityObj;
    private DepartamentoEntity departamentoEntityObj;

    private boolean edit;
    private boolean estadoProvincia;
    private boolean estadoMunicipio;
    private Map<String, String> validateParameter;

    @PostConstruct
    void init() {
        try {
            localidadEntityObj = new LocalidadEntity();
            municipioEntityObj = new MunicipioEntity();
            departamentoEntityObj = new DepartamentoEntity();
            provinciaEntityObj = new ProvinciaEntity();
            setEstadoProvincia(true);
            setEstadoMunicipio(true);
            departamentoEntityList = selectOptions.cbxDepartamento();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            loadLocalidads();
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void newLocalidad() {
        edit = false;
        localidadEntityObj = new LocalidadEntity();
        municipioEntityObj = new MunicipioEntity();
        provinciaEntityObj = new ProvinciaEntity();
        departamentoEntityObj = new DepartamentoEntity();
        setEdit(false);
        setEstadoMunicipio(true);
        setEstadoProvincia(true);
    }

    public void actionLocalidad() {
        try {
            if (isEdit() && localidadEntityObj.getIdLocalidad() > 0) {
                localidadEntityObj.setMunicipioByIdMunicipio(getMunicipioEntityObj());
                localidadBL.update(localidadEntityObj);
                controlerBitacora.update(DescriptorBitacora.LOCALIDAD, String.valueOf
                        (getLocalidadEntityObj().getIdLocalidad()), getLocalidadEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");

            } else {
                localidadEntityObj.setMunicipioByIdMunicipio(municipioEntityObj);
                localidadBL.save(localidadEntityObj);
                controlerBitacora.insert(DescriptorBitacora.LOCALIDAD, String.valueOf(getLocalidadEntityObj()
                        .getIdLocalidad()), getLocalidadEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }

            loadLocalidads();
            newLocalidad();
        } catch (Exception e) {
            log.log(Level.ERROR, e.getMessage());
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
        }
    }

    private void loadLocalidads() {
        localidadEntityList = localidadBL.listAll();
    }

    public void editidLocalidad(LocalidadEntity localidadEntity) {
        setLocalidadEntityObj(localidadEntity);
        setMunicipioEntityObj(localidadEntity.getMunicipioByIdMunicipio());
        setEdit(true);
        setDepartamentoEntityObj(localidadEntity.getMunicipioByIdMunicipio().getProvinciaByIdProvincia().getDepartamentoByIdDepartamento());
        setEstadoProvincia(false);
        cargarProvincias();
        setProvinciaEntityObj(localidadEntity.getMunicipioByIdMunicipio().getProvinciaByIdProvincia());
        setEstadoMunicipio(false);
        cargarMunicipios();
        setMunicipioEntityObj(localidadEntity.getMunicipioByIdMunicipio());
    }

    public void deleteLocalidad() {
        try {
            localidadBL.delete(localidadBL.getById(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("idLocalidad"))));
            controlerBitacora.delete(DescriptorBitacora.LOCALIDAD, String.valueOf
                    (getLocalidadEntityObj().getIdLocalidad()), getLocalidadEntityObj().getNombre());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newLocalidad();
            loadLocalidads();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public void cargarProvincias() {
        if (departamentoEntityObj.getIdDepartamento() > 0) {
            setEstadoProvincia(false);
            provinciaEntityList = selectOptions.cbxProvinciaDpto(departamentoEntityObj.getIdDepartamento());
        }
    }

    public void cargarMunicipios() {
        if (provinciaEntityObj.getIdeProvincia() > 0) {
            setEstadoMunicipio(false);
            municipioEntityList = selectOptions.cbxMunicipioIdProvincia(provinciaEntityObj.getIdeProvincia());
        }
    }

    public MunicipioEntity getMunicipioEntityObj() {
        return municipioEntityObj;
    }

    public void setMunicipioEntityObj(MunicipioEntity municipioEntityObj) {
        this.municipioEntityObj = municipioEntityObj;
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

    public LocalidadEntity getLocalidadEntityObj() {
        return localidadEntityObj;
    }

    public void setLocalidadEntityObj(LocalidadEntity localidadEntityObj) {
        this.localidadEntityObj = localidadEntityObj;
    }

    public List<LocalidadEntity> getLocalidadEntityList() {
        return localidadEntityList;
    }

    public void setLocalidadEntityList(List<LocalidadEntity> localidadEntityList) {
        this.localidadEntityList = localidadEntityList;
    }

    public List<MunicipioEntity> getMunicipioEntityList() {
        return municipioEntityList;
    }

    public void setMunicipioEntityList(List<MunicipioEntity> municipioEntityList) {
        this.municipioEntityList = municipioEntityList;
    }

    public List<DepartamentoEntity> getDepartamentoEntityList() {
        return departamentoEntityList;
    }

    public void setDepartamentoEntityList(List<DepartamentoEntity> departamentoEntityList) {
        this.departamentoEntityList = departamentoEntityList;
    }

    public ProvinciaEntity getProvinciaEntityObj() {
        return provinciaEntityObj;
    }

    public void setProvinciaEntityObj(ProvinciaEntity provinciaEntityObj) {
        this.provinciaEntityObj = provinciaEntityObj;
    }

    public DepartamentoEntity getDepartamentoEntityObj() {
        return departamentoEntityObj;
    }

    public void setDepartamentoEntityObj(DepartamentoEntity departamentoEntityObj) {
        this.departamentoEntityObj = departamentoEntityObj;
    }

    public List<ProvinciaEntity> getProvinciaEntityList() {
        return provinciaEntityList;
    }

    public void setProvinciaEntityList(List<ProvinciaEntity> provinciaEntityList) {
        this.provinciaEntityList = provinciaEntityList;
    }

    public boolean isEstadoProvincia() {
        return estadoProvincia;
    }

    public void setEstadoProvincia(boolean estadoProvincia) {
        this.estadoProvincia = estadoProvincia;
    }

    public boolean isEstadoMunicipio() {
        return estadoMunicipio;
    }

    public void setEstadoMunicipio(boolean estadoMunicipio) {
        this.estadoMunicipio = estadoMunicipio;
    }

}
