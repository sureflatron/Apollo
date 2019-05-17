package myapps.inmuebles;

import myapps.abm.business.InmuebleBL;
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
public class InmuebleBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(InmuebleBean.class);

    @Inject
    private InmuebleBL inmuebleBL;

    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;
    @Inject
    private SelectOptions selectOptions;

    //region "INMUEBLE"
    private List<InmuebleEntity> inmuebleEntityList = new ArrayList<>();
    private List<TipoInmuebleEntity> tipoInmuebleEntityList = new ArrayList<>();
    private List<PropiedadEntity> propiedadEntityList = new ArrayList<>();
    private List<LocalidadEntity> localidadEntityList = new ArrayList<>();
    private List<MunicipioEntity> municipioEntityList = new ArrayList<>();
    private List<DepartamentoEntity> departamentoEntityList = new ArrayList<>();
    private List<ProvinciaEntity> provinciaEntityList = new ArrayList<>();

    private MunicipioEntity municipioEntityObj;
    private ProvinciaEntity provinciaEntityObj;
    private DepartamentoEntity departamentoEntityObj;
    private InmuebleEntity inmuebleEntityObj;
    private TipoInmuebleEntity tipoInmuebleEntityObj;
    private LocalidadEntity localidadEntityObj;
    private PropiedadEntity propiedadEntityObj;

    private Map<String, String> validateParameter;
    private Map<String, String> miscParameter;
    private boolean editInmueble;

    private boolean estadoProvincia;
    private boolean estadoMunicipio;
    private boolean estadoLocalidad;
    private transient MapModel advancedModel;
    private transient MapaGoole mapaPropiedades;

    private long parameter;

    //endregion

    //region "ORDEN COMPRA"

    //endregion

    @PostConstruct
    void init() {
        try {
            parameter = 0;
            inmuebleEntityObj = new InmuebleEntity();
            tipoInmuebleEntityObj = new TipoInmuebleEntity();
            localidadEntityObj = new LocalidadEntity();
            propiedadEntityObj = new PropiedadEntity();
            tipoInmuebleEntityList = selectOptions.cbxTipoInmueble();
            propiedadEntityList = selectOptions.cbxPropiedad(true);
            localidadEntityObj = new LocalidadEntity();
            municipioEntityObj = new MunicipioEntity();
            departamentoEntityObj = new DepartamentoEntity();
            provinciaEntityObj = new ProvinciaEntity();
            tipoInmuebleEntityList = selectOptions.cbxTipoInmueble();
            propiedadEntityList = selectOptions.cbxPropiedad(true);
            departamentoEntityList = selectOptions.cbxDepartamento();
            setEstadoProvincia(true);
            setEstadoMunicipio(true);
            setEstadoLocalidad(true);
            validateParameter = beanApplicationContext.getMapValidateParameters();
            miscParameter = beanApplicationContext.getMapMiscellaneousParameters();
            mapaPropiedades = new MapaGoole(miscParameter.get(EnumParametros.LATITUD_CENTER_MAPA.toString()) + "," + miscParameter.get(EnumParametros.LONGITUD_CENTER_MAPA.toString()),
                    miscParameter.get(EnumParametros.ZOOM_MAPA.toString()),
                    miscParameter.get(EnumParametros.TIPO_MAPA.toString()));
        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }

    public void newInmueble() {
        inmuebleEntityObj = new InmuebleEntity();
        setEditInmueble(false);
        tipoInmuebleEntityObj.setIdTipoInmueble(-1);
        localidadEntityObj.setIdLocalidad(-1);
        propiedadEntityObj.setIdPropiedad(-1);
        departamentoEntityObj.setIdDepartamento(-1);
        setEstadoProvincia(true);
        setEstadoMunicipio(true);
        setEstadoLocalidad(true);

    }

    public void actionInmueble() {
        long idTipoInmueble = tipoInmuebleEntityObj.getIdTipoInmueble();
        long idLocalidad = localidadEntityObj.getIdLocalidad();
        long idPropiedad = propiedadEntityObj.getIdPropiedad();

        if (idTipoInmueble == -1) {
            SysMessage.warn("Seleccione un Tipo Inmueble..", null);

        } else if (idLocalidad == -1) {
            SysMessage.warn("Seleccione una Localidad..", null);

        } else if (idPropiedad == -1) {
            SysMessage.warn("Seleccione una Propiedad..", null);
        } else {
            try {
                if (isEditInmueble() && inmuebleEntityObj.getIdInmueble() > 0) {
                    inmuebleEntityObj.setLocalidadByIdLocalidad(localidadEntityObj);
                    inmuebleEntityObj.setTipoInmuebleByIdTipoInmueble(tipoInmuebleEntityObj);
                    inmuebleEntityObj.setPropiedadByIdPropiedad(propiedadEntityObj);

                    inmuebleBL.update(inmuebleEntityObj);
                    controlerBitacora.update(DescriptorBitacora.INMUEBLES, String.valueOf(getInmuebleEntityObj().getIdInmueble()), getInmuebleEntityObj()
                            .getNombreRbs());
                    SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
                } else {
                    inmuebleEntityObj.setLocalidadByIdLocalidad(localidadEntityObj);
                    inmuebleEntityObj.setTipoInmuebleByIdTipoInmueble(tipoInmuebleEntityObj);
                    inmuebleEntityObj.setPropiedadByIdPropiedad(propiedadEntityObj);


                    inmuebleBL.save(inmuebleEntityObj);
                    controlerBitacora.insert(DescriptorBitacora.INMUEBLES, String.valueOf
                            (getInmuebleEntityObj()
                                    .getIdInmueble()), getInmuebleEntityObj().getNombreRbs());
                    SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
                }
                newInmueble();
            } catch (Exception e) {
                SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro", "");
                logger.log(Level.ERROR, e.getMessage());
            }
        }
    }

    public void editarInmueble() {
        setTipoInmuebleEntityObj(getInmuebleEntityObj().getTipoInmuebleByIdTipoInmueble());
        setPropiedadEntityObj(getInmuebleEntityObj().getPropiedadByIdPropiedad());
        setLocalidadEntityObj(getInmuebleEntityObj().getLocalidadByIdLocalidad());
        setDepartamentoEntityObj(getInmuebleEntityObj().getLocalidadByIdLocalidad().getMunicipioByIdMunicipio().getProvinciaByIdProvincia().getDepartamentoByIdDepartamento());
        setEstadoProvincia(false);
        cargarProvincias();
        setProvinciaEntityObj(getInmuebleEntityObj().getLocalidadByIdLocalidad().getMunicipioByIdMunicipio().getProvinciaByIdProvincia());
        setEstadoMunicipio(false);
        cargarMunicipios();
        setMunicipioEntityObj(getInmuebleEntityObj().getLocalidadByIdLocalidad().getMunicipioByIdMunicipio());

        setEstadoLocalidad(false);
        cargarLocalidad();
        setLocalidadEntityObj(getInmuebleEntityObj().getLocalidadByIdLocalidad());


        advancedModel = new DefaultMapModel();
        LatLng coord1 = new LatLng(Double.valueOf(getInmuebleEntityObj().getLatitud()),
                Double.valueOf(getInmuebleEntityObj().getLongitud()));
        advancedModel.addOverlay(new Marker(coord1, getInmuebleEntityObj().getNombreRbs(), getInmuebleEntityObj().getTipoInmuebleByIdTipoInmueble().getIdTipoInmueble(), UtilFile.mostrarImagen(getInmuebleEntityObj().getTipoInmuebleByIdTipoInmueble().getImagen())));
        mapaPropiedades.setCenter(getInmuebleEntityObj().getLatitud() + "," + getInmuebleEntityObj().getLongitud());
        setEditInmueble(true);
    }

    public void idInmueble() {
        if (FacesContext.getCurrentInstance().getExternalContext()
                .getRequestMap().get("idInmueble") != null) {

            inmuebleEntityObj = (InmuebleEntity) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().get("idInmueble");
        }

        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type") != null) {
            parameter = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type"));
            tipoInmuebleEntityObj.setIdTipoInmueble(parameter);
        }

        if (inmuebleEntityObj.getIdInmueble() > 0) {
            editarInmueble();
        }
    }

    public void cargarProvincias() {
        long idDpto = departamentoEntityObj.getIdDepartamento();

        if (idDpto == -1) {
            SysMessage.warn("Seleccione un  Departamento..", null);
            setEstadoProvincia(true);
        } else if (idDpto > 0) {
            setEstadoProvincia(false);

            provinciaEntityList = selectOptions.cbxProvinciaDpto(idDpto);
        }
    }

    public void cargarMunicipios() {
        long idProvincia = provinciaEntityObj.getIdeProvincia();
        if (idProvincia == -1) {
            SysMessage.warn("Seleccione una Provincia..", null);
            setEstadoMunicipio(true);
        } else if (idProvincia > 0) {
            setEstadoMunicipio(false);
            municipioEntityList = selectOptions.cbxMunicipioIdProvincia(idProvincia);
        }
    }

    public void cargarLocalidad() {
        long idMunicipio;
        idMunicipio = municipioEntityObj.getIdMunicipio();

        if (idMunicipio == -1) {
            SysMessage.warn("Seleccione una Localidad..", null);
            setEstadoLocalidad(true);
        }
        if (idMunicipio > 0) {
            setEstadoLocalidad(false);
            localidadEntityList = selectOptions.cbxLocalidadIdMunicipio(idMunicipio);

        }
    }
    //region "ORDEN COMPRA"

    //endregion

    public List<InmuebleEntity> getInmuebleEntityList() {
        return inmuebleEntityList;
    }

    public void setInmuebleEntityList(List<InmuebleEntity> inmuebleEntityList) {
        this.inmuebleEntityList = inmuebleEntityList;
    }

    public InmuebleEntity getInmuebleEntityObj() {
        return inmuebleEntityObj;
    }

    public void setInmuebleEntityObj(InmuebleEntity inmuebleEntityObj) {
        this.inmuebleEntityObj = inmuebleEntityObj;
    }

    public boolean isEditInmueble() {
        return editInmueble;
    }

    public void setEditInmueble(boolean editarTorres) {
        this.editInmueble = editarTorres;
    }

    public TipoInmuebleEntity getTipoInmuebleEntityObj() {
        return tipoInmuebleEntityObj;
    }

    public void setTipoInmuebleEntityObj(TipoInmuebleEntity tipoInmuebleEntityObj) {
        this.tipoInmuebleEntityObj = tipoInmuebleEntityObj;
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


    public List<TipoInmuebleEntity> getTipoInmuebleEntityList() {
        return tipoInmuebleEntityList;
    }

    public void setTipoInmuebleEntityList(List<TipoInmuebleEntity> tipoInmuebleEntityList) {
        this.tipoInmuebleEntityList = tipoInmuebleEntityList;
    }

    public List<PropiedadEntity> getPropiedadEntityList() {
        return propiedadEntityList;
    }

    public void setPropiedadEntityList(List<PropiedadEntity> propiedadEntityList) {
        this.propiedadEntityList = propiedadEntityList;
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

    public MapaGoole getMapaPropiedades() {
        return mapaPropiedades;
    }

    public long getParameter() {
        return parameter;
    }

    public void setParameter(long parameter) {
        this.parameter = parameter;
    }

    public void setMapaPropiedades(MapaGoole mapaPropiedades) {
        this.mapaPropiedades = mapaPropiedades;
    }


}
