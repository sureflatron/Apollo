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
public class RadioBaseBeanAllItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(RadioBaseBeanAllItem.class);

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
    private RadioBaseEntity radioBasesEntityObj;
    private LocalidadEntity localidadEntityObj;
    private PropiedadEntity propiedadEntityObj;
    private TipoTorreEntity tipoTorreEntityObj;
    private ConstructorEntity constructorEntityObj;
    private RadioBaseEntity selectRadioBaseEntity;

    private Map<String, String> validateParameter;
    private Map<String, String> miscParameter;
    private boolean editRadioBase;
    private long selectRadioBases;
    private boolean estadoProvincia;
    private boolean estadoMunicipio;
    private boolean estadoLocalidad;

    private transient MapModel advancedModel;
    private boolean visibleDialog = false;
    private transient MapaGoole mapaPropiedades;
    private Marker marker;

    @PostConstruct
    void init() {
        try {

            radioBasesEntityObj = new RadioBaseEntity();
            localidadEntityObj = new LocalidadEntity();
            propiedadEntityObj = new PropiedadEntity();
            tipoTorreEntityObj = new TipoTorreEntity();
            constructorEntityObj = new ConstructorEntity();
            selectRadioBaseEntity = new RadioBaseEntity();

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

    private void newRadioBases() {
        radioBasesEntityObj = new RadioBaseEntity();
        setEditRadioBase(false);
        setSelectRadioBases((long) 0);
        localidadEntityObj.setIdLocalidad(-1);
        propiedadEntityObj.setIdPropiedad(-1);
        tipoTorreEntityObj.setIdTipoTorre(-1);
        constructorEntityObj.setIdConstructor(-1);
        departamentoEntityObj.setIdDepartamento(-1);
        setEstadoProvincia(true);
        setEstadoMunicipio(true);
        setEstadoLocalidad(true);

    }

    public void actionRadioBases() {

        if (validate()) {
            try {
                if (isEditRadioBase() && getSelectRadioBases() != 0) {
                    radioBasesEntityObj.setLocalidadByIdLocalidad(localidadEntityObj);

                    radioBasesEntityObj.setPropiedadByIdPropiedad(propiedadEntityObj);
                    radioBasesEntityObj.setTipoTorreByIdTipoTorre(tipoTorreEntityObj);
                    radioBasesEntityObj.setConstructorByIdConstructor(constructorEntityObj);
                    radioBaseBL.update(radioBasesEntityObj);
                    controlerBitacora.update(DescriptorBitacora.RADIRO_BASES, String.valueOf
                            (getRadioBaseEntityObj().getIdRadioBase()), getRadioBaseEntityObj()
                            .getNombreRbs());
                    SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
                } else {
                    radioBasesEntityObj.setLocalidadByIdLocalidad(localidadEntityObj);
                    radioBasesEntityObj.setPropiedadByIdPropiedad(propiedadEntityObj);
                    radioBasesEntityObj.setTipoTorreByIdTipoTorre(tipoTorreEntityObj);
                    radioBasesEntityObj.setConstructorByIdConstructor(constructorEntityObj);
                    radioBaseBL.save(radioBasesEntityObj);
                    controlerBitacora.insert(DescriptorBitacora.RADIRO_BASES, String.valueOf
                            (getRadioBaseEntityObj().getIdRadioBase()), getRadioBaseEntityObj().getNombreRbs());
                    SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
                }
                loadRadioBase();
                newRadioBases();
            } catch (Exception e) {
                SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro", "");
                logger.log(Level.ERROR, e.getMessage());
            }
        }
    }

    private boolean validate() {
        boolean noSelect = true;
        if (localidadEntityObj.getIdLocalidad() == -1) {
            SysMessage.warn("Seleccione una Localidad..", null);
            noSelect = false;
        } else if (propiedadEntityObj.getIdPropiedad() == -1) {
            SysMessage.warn("Seleccione una Propiedad..", null);
            noSelect = false;
        } else if (tipoTorreEntityObj.getIdTipoTorre() == -1) {
            SysMessage.warn("Seleccione un Tipo Torre..", null);
            noSelect = false;
        } else if (constructorEntityObj.getIdConstructor() == -1) {
            SysMessage.warn("Seleccione un Constructor..", null);
            noSelect = false;
        }
        if (radioBasesEntityObj.getEstadoInmueble().equals("Nada")) {
            SysMessage.warn("Seleccione un EStado..", null);
            noSelect = false;
        }
        return noSelect;
    }

    private void loadRadioBase() {
        radioBaseEntityList = radioBaseBL.listAll();
    }

    public String obtenerRadioBase(RadioBaseEntity obj) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("radioBase",
                obj);
        return "/view/radioBase.xhtml";
    }

    public void deleteRadioBase() {

        try {
            setSelectRadioBases(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("idRadioBase")));
            setRadioBaseEntityObj(radioBaseBL.getById(getSelectRadioBases()));
            radioBaseBL.delete(getRadioBaseEntityObj());
            controlerBitacora.delete(DescriptorBitacora.RADIRO_BASES, String.valueOf
                    (getRadioBaseEntityObj().getIdRadioBase()), getRadioBaseEntityObj().getNombreRbs());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newRadioBases();
            loadRadioBase();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro", "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public void clean() {
        radioBasesEntityObj = new RadioBaseEntity();
        provinciaEntityObj = new ProvinciaEntity();
        tipoTorreEntityObj = new TipoTorreEntity();
        departamentoEntityObj = new DepartamentoEntity();
        provinciaEntityObj = new ProvinciaEntity();
        municipioEntityObj = new MunicipioEntity();
        localidadEntityObj = new LocalidadEntity();
        setEstadoProvincia(true);
        setEstadoMunicipio(true);
        setEstadoLocalidad(true);
        loadRadioBase();
    }

    public void buscar() {
        radioBaseEntityList = new ArrayList<>();
        provinciaEntityObj.setDepartamentoByIdDepartamento(getDepartamentoEntityObj());
        municipioEntityObj.setProvinciaByIdProvincia(provinciaEntityObj);
        localidadEntityObj.setMunicipioByIdMunicipio(getMunicipioEntityObj());

        radioBasesEntityObj.setLocalidadByIdLocalidad(getLocalidadEntityObj());
        radioBasesEntityObj.setPropiedadByIdPropiedad(getPropiedadEntityObj());
        radioBasesEntityObj.setTipoTorreByIdTipoTorre(getTipoTorreEntityObj());

        setRadioBaseEntityList(radioBaseBL.listAllFilter(radioBasesEntityObj));
    }

    public void viewMap(RadioBaseEntity radSelected) {
        try {
            setVisibleDialog(true);
            if (radSelected == null) {
                advancedModel = new DefaultMapModel();
                for (RadioBaseEntity radioBaseEntity : getRadioBaseEntityList()) {

                    LatLng coord1 = new LatLng(Double.valueOf(radioBaseEntity.getLatitud()),
                            Double.valueOf(radioBaseEntity.getLongitud()));
                    advancedModel.addOverlay(new Marker(coord1, radioBaseEntity.getNombreRbs(), radioBaseEntity.getIdRadioBase(), UtilFile.mostrarImagen(radioBaseEntity.getTipoTorreByIdTipoTorre().getImagen())));

                }
            } else {
                advancedModel = new DefaultMapModel();
                LatLng coord1 = new LatLng(Double.valueOf(radSelected.getLatitud()), Double.valueOf(radSelected
                        .getLongitud()));
                advancedModel.addOverlay(new Marker(coord1, radSelected.getNombreRbs(), radSelected.getIdRadioBase(), UtilFile.mostrarImagen(radSelected.getTipoTorreByIdTipoTorre().getImagen())));

                mapaPropiedades.setCenter(radSelected.getLatitud() + "," + radSelected.getLongitud());
            }

        } catch (Exception e) {
            logger.error("[cargarMapa] Fallo al cargar el mapa.", e);
        }
    }

    public List<RadioBaseEntity> getRadioBaseEntityList() {
        return radioBaseEntityList;
    }

    public void setRadioBaseEntityList(List<RadioBaseEntity> radioBaseEntityList) {
        this.radioBaseEntityList = radioBaseEntityList;
    }

    public RadioBaseEntity getRadioBaseEntityObj() {
        return radioBasesEntityObj;
    }

    public void setRadioBaseEntityObj(RadioBaseEntity radioBasesEntityObj) {
        this.radioBasesEntityObj = radioBasesEntityObj;
    }

    public long getSelectRadioBases() {
        return selectRadioBases;
    }

    public void setSelectRadioBases(long selectRadioBases) {
        this.selectRadioBases = selectRadioBases;
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

    public Map<String, String> getMiscParameter() {
        return miscParameter;
    }

    public void setMiscParameter(Map<String, String> miscParameter) {
        this.miscParameter = miscParameter;
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

    public void cargarProvincias() {
        if (departamentoEntityObj.getIdDepartamento() == -1) {
            SysMessage.warn("Seleccione un  Departamento..", null);
            setEstadoProvincia(true);
        } else if (departamentoEntityObj.getIdDepartamento() > 0) {
            setEstadoProvincia(false);
            provinciaEntityList = selectOptions.cbxProvinciaDpto(departamentoEntityObj.getIdDepartamento());
        }
    }

    public void cargarMunicipios() {
        if (provinciaEntityObj.getIdeProvincia() == -1) {
            SysMessage.warn("Seleccione una Provincia..", null);
            setEstadoMunicipio(true);
        } else if (provinciaEntityObj.getIdeProvincia() > 0) {
            setEstadoMunicipio(false);
            municipioEntityList = selectOptions.cbxMunicipioIdProvincia(provinciaEntityObj.getIdeProvincia());
        }
    }

    public void cargarLocalidad() {
        if (municipioEntityObj.getIdMunicipio() == -1) {
            SysMessage.warn("Seleccione una Localidad..", null);
            setEstadoLocalidad(true);
        }
        if (municipioEntityObj.getIdMunicipio() > 0) {
            setEstadoLocalidad(false);
            localidadEntityList = selectOptions.cbxLocalidadIdMunicipio(municipioEntityObj.getIdMunicipio());

        }
    }

    public boolean isVisibleDialog() {
        return visibleDialog;
    }

    public void setVisibleDialog(boolean visibleDialog) {
        this.visibleDialog = visibleDialog;
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

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }

    public RadioBaseEntity getSelectRadioBaseEntity() {
        return selectRadioBaseEntity;
    }

    public void setSelectRadioBaseEntity(RadioBaseEntity selectRadioBaseEntity) {
        this.selectRadioBaseEntity = selectRadioBaseEntity;
    }
}
