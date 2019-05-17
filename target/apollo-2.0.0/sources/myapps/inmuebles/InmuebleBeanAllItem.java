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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class InmuebleBeanAllItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(InmuebleBeanAllItem.class);

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
    private long selectInmueble;
    private boolean estadoProvincia;
    private boolean estadoMunicipio;
    private boolean estadoLocalidad;
    private transient MapModel advancedModel;
    private boolean visibleDialog = false;
    private transient MapaGoole mapaPropiedades;
    private Marker marker;
    private InmuebleEntity selectInmuebleEntityObj;
    //endregion

    @PostConstruct
    void init() {
        try {
            validateParameter = beanApplicationContext.getMapValidateParameters();
            miscParameter = beanApplicationContext.getMapMiscellaneousParameters();
            inmuebleEntityObj = new InmuebleEntity();
            tipoInmuebleEntityObj = new TipoInmuebleEntity();
            localidadEntityObj = new LocalidadEntity();
            propiedadEntityObj = new PropiedadEntity();
            municipioEntityObj = new MunicipioEntity();
            departamentoEntityObj = new DepartamentoEntity();
            provinciaEntityObj = new ProvinciaEntity();
            selectInmuebleEntityObj= new InmuebleEntity();
            tipoInmuebleEntityList = selectOptions.cbxTipoInmueble();
            setEstadoProvincia(true);
            setEstadoMunicipio(true);
            setEstadoLocalidad(true);
            loadInmuebles();
            mapaPropiedades =new MapaGoole(miscParameter.get(EnumParametros.LATITUD_CENTER_MAPA.toString()) + ","+ miscParameter.get(EnumParametros.LONGITUD_CENTER_MAPA.toString()),
					   miscParameter.get(EnumParametros.ZOOM_MAPA.toString()),
					   miscParameter.get(EnumParametros.TIPO_MAPA.toString()));

        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }

    /**
     * Carga lista de inmuebles
     */
    private void loadInmuebles() {
        inmuebleEntityList = inmuebleBL.listAll();
    }

    /**
     * Elimina um inmueble de la lista
     */
    public void deleteInmueble() {

        try {
            setSelectInmueble(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("idInmueble")));
            setInmuebleEntityObj(inmuebleBL.getById(getSelectInmueble()));
            inmuebleBL.delete(getInmuebleEntityObj());
            controlerBitacora.delete(DescriptorBitacora.INMUEBLES, String.valueOf
                    (getInmuebleEntityObj().getIdInmueble()), getInmuebleEntityObj().getNombreRbs());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            loadInmuebles();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro", "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    /**
     * Captura ID de inmueble para edidion
     *
     * @param inmueble
     * @return
     */
    public String obtenerIdImnuble(InmuebleEntity inmueble) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("idInmueble",inmueble);
        return "/view/inmueble.xhtml";
    }

    public void viewMap(InmuebleEntity inmSelected) {
        try {
            setVisibleDialog(true);
            if (inmSelected == null) {
                advancedModel = new DefaultMapModel();
                for (InmuebleEntity inmuebleEntity : getInmuebleEntityList()) {

                    LatLng coord1 = new LatLng(Double.valueOf(inmuebleEntity.getLatitud()),
                            Double.valueOf(inmuebleEntity.getLongitud()));
                    advancedModel.addOverlay(new Marker(coord1, inmuebleEntity.getNombreRbs(), inmuebleEntity.getTipoInmuebleByIdTipoInmueble().getIdTipoInmueble(), UtilFile.mostrarImagen(inmuebleEntity.getTipoInmuebleByIdTipoInmueble().getImagen())));

                }
            } else {
                advancedModel = new DefaultMapModel();
                LatLng coord1 = new LatLng(Double.valueOf(inmSelected.getLatitud())
                        , Double.valueOf(inmSelected.getLongitud()));
                advancedModel.addOverlay(new Marker(coord1, inmSelected.getNombreRbs(), inmSelected.getTipoInmuebleByIdTipoInmueble().getIdTipoInmueble(), UtilFile.mostrarImagen(inmSelected.getTipoInmuebleByIdTipoInmueble().getImagen())));
                mapaPropiedades.setCenter(inmSelected.getLatitud() + "," + inmSelected.getLongitud());
            }


        } catch (Exception e) {
            logger.error("[cargarMapa] Fallo al cargar el mapa.", e);
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
        long idMunicipio = municipioEntityObj.getIdMunicipio();

        if (idMunicipio == -1) {
            SysMessage.warn("Seleccione una Localidad..", null);
            setEstadoLocalidad(true);
        }
        if (idMunicipio > 0) {
            setEstadoLocalidad(false);
            localidadEntityList = selectOptions.cbxLocalidadIdMunicipio(idMunicipio);

        }
    }

    /**
     * Filtro por criterios de busqueda
     */
    public void buscar() {
        inmuebleEntityList = new ArrayList<>();
        Map<String, Object> parameter = new HashMap<>();
        if (!getInmuebleEntityObj().getNombreRbs().equals("")) {
            parameter.put("nombreRbs", getInmuebleEntityObj().getNombreRbs().trim());
        }
        if (getTipoInmuebleEntityObj().getIdTipoInmueble() > 0) {
            parameter.put("idInm", getTipoInmuebleEntityObj().getIdTipoInmueble());
        }
        if (getPropiedadEntityObj().getIdPropiedad() > 0) {
            parameter.put("idProp", getPropiedadEntityObj().getIdPropiedad());
        }
        if (!getInmuebleEntityObj().getSector().equals("-1")) {
            parameter.put("sector", getInmuebleEntityObj().getSector());
        }
        if (getDepartamentoEntityObj().getIdDepartamento() > 0) {
            parameter.put("idDepto", getDepartamentoEntityObj().getIdDepartamento());
            if (getProvinciaEntityObj().getIdeProvincia() > 0) {
                parameter.put("idProv", getProvinciaEntityObj().getIdeProvincia());
                if (getMunicipioEntityObj().getIdMunicipio() > 0) {
                    parameter.put("idMun", getMunicipioEntityObj().getIdMunicipio());
                    if (getLocalidadEntityObj().getIdLocalidad() > 0) {
                        parameter.put("idLoc", getLocalidadEntityObj().getIdLocalidad());
                    }
                }
            }
        }
        inmuebleEntityList = parameter.size() == 0 ? inmuebleBL.listAll() : inmuebleBL.listAllFilter
                (parameter);
    }

    public void clean() {
        setInmuebleEntityObj(new InmuebleEntity());
        setTipoInmuebleEntityObj(new TipoInmuebleEntity());
        setPropiedadEntityObj(new PropiedadEntity());
        setDepartamentoEntityObj(new DepartamentoEntity());
        setProvinciaEntityObj(new ProvinciaEntity());
        setMunicipioEntityObj(new MunicipioEntity());
        setLocalidadEntityObj(new LocalidadEntity());

        loadInmuebles();
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
        setPropiedadEntityList(selectOptions.cbxPropiedad(false));
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
        setDepartamentoEntityList(selectOptions.cbxDepartamento());
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

    public long getSelectInmueble() {
        return selectInmueble;
    }

    public void setSelectInmueble(long selectInmueble) {
        this.selectInmueble = selectInmueble;
    }

    public boolean isEditInmueble() {
        return editInmueble;
    }

    public void setEditInmueble(boolean editarTorres) {
        this.editInmueble = editarTorres;
    }

    public boolean isVisibleDialog() {
        return visibleDialog;
    }

    public void setVisibleDialog(boolean visibleDialog) {
        this.visibleDialog = visibleDialog;
    }

    public Marker getMarker() {
        return marker;
    }

    public MapaGoole getMapaPropiedades() {
		return mapaPropiedades;
	}

	public void setMapaPropiedades(MapaGoole mapaPropiedades) {
		this.mapaPropiedades = mapaPropiedades;
	}

	public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }

    public InmuebleEntity getSelectInmuebleEntityObj() {
        return selectInmuebleEntityObj;
    }

    public void setSelectInmuebleEntityObj(InmuebleEntity selectInmuebleEntityObj) {
        this.selectInmuebleEntityObj = selectInmuebleEntityObj;
    }
}
