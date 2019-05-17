package myapps.inmuebles;


import myapps.abm.business.RadioBaseBL;
import myapps.abm.business.SelectOptions;
import myapps.abm.model.*;
import myapps.servicio_basico.bean.model.MapaGoole;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.servicio_basico.util.UtilFile;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.DescriptorBitacora;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

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
public class RadioBaseBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(RadioBaseBean.class);

    @Inject
    private RadioBaseBL radioBaseBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;
    @Inject
    private SelectOptions selectOptions;


    private List<RadioBaseEntity> radioBaseEntityList = new ArrayList<>();
    private List<PropiedadEntity> propiedadEntityList = new ArrayList<>();
    private List<TipoTorreEntity> tipoTorreEntityList = new ArrayList<>();
    private List<ConstructorEntity> constructorEntityList = new ArrayList<>();
    private List<LocalidadEntity> localidadEntityList = new ArrayList<>();
    private List<MunicipioEntity> municipioEntityList = new ArrayList<>();
    private List<DepartamentoEntity> departamentoEntityList = new ArrayList<>();
    private List<ProvinciaEntity> provinciaEntityList = new ArrayList<>();

    private MunicipioEntity municipioEntityObj;

    private ProvinciaEntity provinciaEntityObj;
    private DepartamentoEntity departamentoEntityObj;
    private RadioBaseEntity radioBaseEntityObj;
    private LocalidadEntity localidadEntityObj;
    private PropiedadEntity propiedadEntityObj;
    private TipoTorreEntity tipoTorreEntityObj;
    private ConstructorEntity constructorEntityObj;

    private Map<String, String> validateParameter;
    private Map<String, String> miscParameter;
    private boolean editRadioBase;
    private boolean estadoProvincia;
    private boolean estadoMunicipio;
    private boolean estadoLocalidad;

    private transient MapModel advancedModel;
    private transient MapaGoole mapaPropiedades;
    private Marker marker;
    private int suma1;
    private int suma2;
    private long parameter;

    @PostConstruct
    void init() {
        try {
            parameter = 0;
            radioBaseEntityObj = new RadioBaseEntity();
            localidadEntityObj = new LocalidadEntity();
            propiedadEntityObj = new PropiedadEntity();
            tipoTorreEntityObj = new TipoTorreEntity();
            constructorEntityObj = new ConstructorEntity();
            propiedadEntityList = selectOptions.cbxPropiedad(true);
            tipoTorreEntityList = selectOptions.cbxTipoTorre();
            constructorEntityList = selectOptions.cbxConstructor();

            localidadEntityObj = new LocalidadEntity();
            municipioEntityObj = new MunicipioEntity();
            departamentoEntityObj = new DepartamentoEntity();
            provinciaEntityObj = new ProvinciaEntity();
            setEstadoProvincia(true);
            setEstadoMunicipio(true);
            setEstadoLocalidad(true);
            departamentoEntityList = selectOptions.cbxDepartamento();
            loadRadioBase();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            miscParameter = beanApplicationContext.getMapMiscellaneousParameters();
            mapaPropiedades = new MapaGoole(miscParameter.get(EnumParametros.LATITUD_CENTER_MAPA.toString()) + "," + miscParameter.get(EnumParametros.LONGITUD_CENTER_MAPA.toString()),
                    miscParameter.get(EnumParametros.ZOOM_MAPA.toString()),
                    miscParameter.get(EnumParametros.TIPO_MAPA.toString()));

        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }

    public void newRadioBases() {
        radioBaseEntityObj = new RadioBaseEntity();
        setEditRadioBase(false);
        localidadEntityObj = new LocalidadEntity();
        propiedadEntityObj = new PropiedadEntity();
        tipoTorreEntityObj = new TipoTorreEntity();
        constructorEntityObj = new ConstructorEntity();
        departamentoEntityObj = new DepartamentoEntity();
        setEstadoProvincia(true);
        setEstadoMunicipio(true);
        setEstadoLocalidad(true);

    }

    public void actionRadioBases() {

        if (validate()) {
            try {
                if (isEditRadioBase() && getRadioBaseEntityObj() != null) {
                    radioBaseEntityObj.setLocalidadByIdLocalidad(localidadEntityObj);

                    radioBaseEntityObj.setPropiedadByIdPropiedad(propiedadEntityObj);
                    radioBaseEntityObj.setTipoTorreByIdTipoTorre(tipoTorreEntityObj);
                    radioBaseEntityObj.setConstructorByIdConstructor(constructorEntityObj);
                    radioBaseBL.update(radioBaseEntityObj);
                    controlerBitacora.update(DescriptorBitacora.RADIRO_BASES, String.valueOf
                            (getRadioBaseEntityObj().getIdRadioBase()), getRadioBaseEntityObj()
                            .getNombreRbs());
                    SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
                } else {
                    radioBaseEntityObj.setLocalidadByIdLocalidad(localidadEntityObj);
                    radioBaseEntityObj.setPropiedadByIdPropiedad(propiedadEntityObj);
                    radioBaseEntityObj.setTipoTorreByIdTipoTorre(tipoTorreEntityObj);
                    radioBaseEntityObj.setConstructorByIdConstructor(constructorEntityObj);
                    radioBaseBL.save(radioBaseEntityObj);
                    controlerBitacora.insert(DescriptorBitacora.RADIRO_BASES, String.valueOf
                            (getRadioBaseEntityObj().getIdRadioBase()), getRadioBaseEntityObj().getNombreRbs());
                    SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
                }
                loadRadioBase();
                newRadioBases();
                FacesContext.getCurrentInstance().getExternalContext().redirect("radioBaseAllItem.xhtml");
            } catch (Exception e) {
                SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro", "");
                logger.log(Level.ERROR, e.getMessage());
            }
        }
    }

    private boolean validate() {
        boolean noSelect = true;
        if (getRadioBaseEntityObj().getLatitud().isEmpty() && getRadioBaseEntityObj().getLongitud
                ().isEmpty()) {
            SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Seleccion una ubicacion de radio base.", null);
            noSelect = false;
        }
        return noSelect;
    }

    private void loadRadioBase() {
        radioBaseEntityList = radioBaseBL.listAll();
    }

    public void editarRadioBases() {
        if (getRadioBaseEntityObj() != null) {
            setPropiedadEntityObj(getRadioBaseEntityObj().getPropiedadByIdPropiedad());
            setTipoTorreEntityObj(getRadioBaseEntityObj().getTipoTorreByIdTipoTorre());
            setConstructorEntityObj(getRadioBaseEntityObj().getConstructorByIdConstructor());
            setLocalidadEntityObj(getRadioBaseEntityObj().getLocalidadByIdLocalidad());
            setDepartamentoEntityObj(getRadioBaseEntityObj().getLocalidadByIdLocalidad()
                    .getMunicipioByIdMunicipio().getProvinciaByIdProvincia()
                    .getDepartamentoByIdDepartamento());
            setEstadoProvincia(false);
            cargarProvincias();
            setProvinciaEntityObj(getRadioBaseEntityObj().getLocalidadByIdLocalidad()
                    .getMunicipioByIdMunicipio().getProvinciaByIdProvincia());
            setEstadoMunicipio(false);
            cargarMunicipios();
            setMunicipioEntityObj(getRadioBaseEntityObj().getLocalidadByIdLocalidad()
                    .getMunicipioByIdMunicipio());
            setEstadoLocalidad(false);
            cargarLocalidad();
            setLocalidadEntityObj(getRadioBaseEntityObj().getLocalidadByIdLocalidad());
            advancedModel = new DefaultMapModel();
            LatLng coord1 = new LatLng(Double.valueOf(getRadioBaseEntityObj().getLatitud()),
                    Double.valueOf(getRadioBaseEntityObj().getLongitud()));
            advancedModel.addOverlay(new Marker(coord1, getRadioBaseEntityObj().getNombreRbs(), getRadioBaseEntityObj().getIdRadioBase(), UtilFile.mostrarImagen(getRadioBaseEntityObj().getTipoTorreByIdTipoTorre().getImagen())));

            mapaPropiedades.setCenter(getRadioBaseEntityObj().getLatitud() + "," + getRadioBaseEntityObj().getLongitud());

            setEditRadioBase(true);
        }
    }

    public void idRadioBase() {
        if (FacesContext.getCurrentInstance().getExternalContext()
                .getRequestMap().get("radioBase") != null) {
            radioBaseEntityObj = (RadioBaseEntity) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().get("radioBase");
        }

        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type") != null) {
            parameter = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type"));
            tipoTorreEntityObj.setIdTipoTorre(parameter);
        }

        if (radioBaseEntityObj.getIdRadioBase() > 0) {
            editarRadioBases();
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

    public void cargarLocalidad() {
        if (municipioEntityObj.getIdMunicipio() > 0) {
            setEstadoLocalidad(false);
            localidadEntityList = selectOptions.cbxLocalidadIdMunicipio(municipioEntityObj.getIdMunicipio());
        }
    }

    public void area() {
        if (radioBaseEntityObj.getAreax1() != null && radioBaseEntityObj.getAreay1() != null) {
            suma1 = Integer.valueOf(radioBaseEntityObj.getAreax1()) * Integer.valueOf(radioBaseEntityObj.getAreay1());
        }

        if (radioBaseEntityObj.getAreax2() != null && radioBaseEntityObj.getAreay2() != null) {
            suma2 = Integer.valueOf(radioBaseEntityObj.getAreax2()) * Integer.valueOf(radioBaseEntityObj.getAreay2());
        }
    }

    public List<RadioBaseEntity> getRadioBaseEntityList() {
        return radioBaseEntityList;
    }

    public void setRadioBaseEntityList(List<RadioBaseEntity> radioBaseEntityList) {
        this.radioBaseEntityList = radioBaseEntityList;
    }

    public RadioBaseEntity getRadioBaseEntityObj() {
        return radioBaseEntityObj;
    }


    public void setRadioBaseEntityObj(RadioBaseEntity radioBaseEntityObj) {
        this.radioBaseEntityObj = radioBaseEntityObj;
    }

    public boolean isEditRadioBase() {
        return editRadioBase;
    }

    public void setEditRadioBase(boolean editRadioBase) {
        this.editRadioBase = editRadioBase;
    }

    public LocalidadEntity getLocalidadEntityObj() {
        return localidadEntityObj;
    }

    public void setLocalidadEntityObj(LocalidadEntity localidadEntityObj) {
        this.localidadEntityObj = localidadEntityObj;
    }

    public PropiedadEntity getPropiedadEntityObj() {
        return propiedadEntityObj;
    }

    public void setPropiedadEntityObj(PropiedadEntity propiedadEntityObj) {
        this.propiedadEntityObj = propiedadEntityObj;
    }

    public TipoTorreEntity getTipoTorreEntityObj() {
        return tipoTorreEntityObj;
    }

    public void setTipoTorreEntityObj(TipoTorreEntity tipoTorreEntityObj) {
        this.tipoTorreEntityObj = tipoTorreEntityObj;
    }

    public ConstructorEntity getConstructorEntityObj() {
        return constructorEntityObj;
    }

    public void setConstructorEntityObj(ConstructorEntity constructorEntityObj) {
        this.constructorEntityObj = constructorEntityObj;
    }


    public List<PropiedadEntity> getPropiedadEntityList() {
        return propiedadEntityList;
    }

    public void setPropiedadEntityList(List<PropiedadEntity> propiedadEntityList) {
        this.propiedadEntityList = propiedadEntityList;
    }

    public List<TipoTorreEntity> getTipoTorreEntityList() {
        return tipoTorreEntityList;
    }

    public void setTipoTorreEntityList(List<TipoTorreEntity> tipoTorreEntityList) {
        this.tipoTorreEntityList = tipoTorreEntityList;
    }

    public List<ConstructorEntity> getConstructorEntityList() {
        return constructorEntityList;
    }

    public void setConstructorEntityList(List<ConstructorEntity> constructorEntityList) {
        this.constructorEntityList = constructorEntityList;
    }

    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public List<LocalidadEntity> getLocalidadEntityList() {
        return localidadEntityList;
    }

    public Map<String, String> getMiscParameter() {
        return miscParameter;
    }

    public void setMiscParameter(Map<String, String> miscParameter) {
        this.miscParameter = miscParameter;
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

    public List<ProvinciaEntity> getProvinciaEntityList() {
        return provinciaEntityList;
    }

    public void setProvinciaEntityList(List<ProvinciaEntity> provinciaEntityList) {
        this.provinciaEntityList = provinciaEntityList;
    }


    public MunicipioEntity getMunicipioEntityObj() {
        return municipioEntityObj;
    }

    public void setMunicipioEntityObj(MunicipioEntity municipioEntityObj) {
        this.municipioEntityObj = municipioEntityObj;
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

    public boolean isEstadoLocalidad() {
        return estadoLocalidad;
    }

    public void setEstadoLocalidad(boolean estadoLocalidad) {
        this.estadoLocalidad = estadoLocalidad;
    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }

    public void setAdvancedModel(MapModel advancedModel) {
        this.advancedModel = advancedModel;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }

    public MapaGoole getMapaPropiedades() {
        return mapaPropiedades;
    }

    public void setMapaPropiedades(MapaGoole mapaPropiedades) {
        this.mapaPropiedades = mapaPropiedades;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public int getSuma1() {
        return suma1;
    }

    public void setSuma1(int suma1) {
        this.suma1 = suma1;
    }

    public int getSuma2() {
        return suma2;
    }

    public void setSuma2(int suma2) {
        this.suma2 = suma2;
    }

    public long getParameter() {
        return parameter;
    }

    public void setParameter(long parameter) {
        this.parameter = parameter;
    }


}
