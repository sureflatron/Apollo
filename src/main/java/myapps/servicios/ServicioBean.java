package myapps.servicios;


import myapps.abm.business.*;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class ServicioBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(ServicioBean.class);

    @Inject
    private ServicioBL servicioBL;
    @Inject
    private InmServBL inmServBL;
    @Inject
    private RdbsServBL rdbsServBL;
    @Inject
    private InmuebleBL inmuebleBL;
    @Inject
    private RadioBaseBL radioBaseBL;
    @Inject
    private SelectOptions selectOptions;
    @Inject
    private ControlerBitacora controlerBitacora;


    @EJB
    private IApplicationContext beanApplicationContext;
    private List<MonedaEntity> monedaEntityList = new ArrayList<>();
    private List<TipoServicioEntity> tipoServicioEntityList = new ArrayList<>();
    private List<ProveedorEntity> proveedorEntityList = new ArrayList<>();
    private List<UnidadOperativaEntity> unidadOperativaEntityList = new ArrayList<>();
    private List<BancoEntity> bancoEntityList = new ArrayList<>();
    private List<RadioBaseEntity> radioBaseEntityList = new ArrayList<>();
    private List<InmuebleEntity> inmuebleEntityList = new ArrayList<>();
    private List<TipoInmuebleEntity> tipoInmuebleEntityList = new ArrayList<>();
    private List<TipoTorreEntity> tipoTorreEntityList = new ArrayList<>();

    private ServicioEntity servicioEntityObj;
    private TipoServicioEntity tipoServicioEntityObj;
    private ProveedorEntity proveedorEntityObj;
    private BancoEntity bancoEntityObj;
    private MonedaEntity monedaEntityObj;
    private UnidadOperativaEntity unidadOperativaEntityObj;
    private RadioBaseEntity radioBaseEntityObj;
    private InmuebleEntity inmuebleEntityObj;
    private InmServEntity inmServEntityObj;
    private RdbServEntity rdbServEntityObj;
    private TipoInmuebleEntity tipoInmuebleEntityObj;
    private TipoTorreEntity tipoTorreEntityObj;
    private InmuebleEntity inmuebleEntityShow;
    private RadioBaseEntity radioBaseEntityShow;


    private Map<String, String> validateParameter;
    private Map<String, String> miscParameter;

    private String select;
    private transient MapModel advancedModel;
    private int selectPara;
    private boolean nuevoServicio;
    private boolean collapse = true;
    private boolean visibleDialog = false;
    private transient MapaGoole mapaPropiedades;
    private boolean disableControls = false;

    private String codigo;
    private String nombre;
    private String propiedad;
    private String direccion;
    private Marker marker;

    @PostConstruct
    void init() {
        try {
            validateParameter = beanApplicationContext.getMapValidateParameters();
            miscParameter = beanApplicationContext.getMapMiscellaneousParameters();
            servicioEntityObj = new ServicioEntity();
            tipoServicioEntityObj = new TipoServicioEntity();
            proveedorEntityObj = new ProveedorEntity();
            bancoEntityObj = new BancoEntity();
            unidadOperativaEntityObj = new UnidadOperativaEntity();
            monedaEntityObj = new MonedaEntity();
            inmuebleEntityObj = new InmuebleEntity();
            radioBaseEntityObj = new RadioBaseEntity();
            inmServEntityObj = new InmServEntity();
            rdbServEntityObj = new RdbServEntity();
            tipoInmuebleEntityObj = new TipoInmuebleEntity();
            tipoTorreEntityObj = new TipoTorreEntity();

            loadSelectOneMenu();

            mapaPropiedades = new MapaGoole(miscParameter.get(EnumParametros.LATITUD_CENTER_MAPA.toString()) + "," + miscParameter.get(EnumParametros.LONGITUD_CENTER_MAPA.toString()),
                    miscParameter.get(EnumParametros.ZOOM_MAPA.toString()),
                    miscParameter.get(EnumParametros.TIPO_MAPA.toString()));
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    private void loadSelectOneMenu() {
        monedaEntityList = selectOptions.cbxMoneda();
        tipoServicioEntityList = selectOptions.cbxTipoServicio();
        concatProveedor();
        unidadOperativaEntityList = selectOptions.cbxUO();
        bancoEntityList = selectOptions.cbxBancos();
        radioBaseEntityList = selectOptions.cbxRadioBases();
        setTipoTorreEntityList(selectOptions.cbxTipoTorre());
        setTipoInmuebleEntityList(selectOptions.cbxTipoInmueble());
    }

    private List<ProveedorEntity> concatProveedor() {
        proveedorEntityList = selectOptions.cbxProveedores();
        int cont = 0;
        for (ProveedorEntity proveedorEntity : getProveedorEntityList()) {
            proveedorEntity.setNombre(proveedorEntity.getSigla() + "-" + proveedorEntity.getNombre());
            proveedorEntityList.set(cont, proveedorEntity);
            cont++;
        }
        return proveedorEntityList;
    }

    private void newServicio() {
        servicioEntityObj = new ServicioEntity();
        tipoServicioEntityObj = new TipoServicioEntity();
        proveedorEntityObj = new ProveedorEntity();
        unidadOperativaEntityObj = new UnidadOperativaEntity();
        bancoEntityObj = new BancoEntity();
        monedaEntityObj = new MonedaEntity();
    }

    /**
     * Acciones de persist y merge en base de datos
     */
    public void actionServicio() {
        try {
            if (!isNuevoServicio() && servicioEntityObj.getIdServicio() > 0) {
                servicioEntityObj.setTipoServicioByIdTipoServicio(tipoServicioEntityObj);
                servicioEntityObj.setProveedorByIdProveedor(proveedorEntityObj);
                servicioEntityObj.setUnidadOperativaByIdUnidadOperativa(unidadOperativaEntityObj);
                servicioEntityObj.setBancoByIdBanco(bancoEntityObj);
                servicioEntityObj.setMonedaByIdMoneda(monedaEntityObj);
                servicioBL.update(servicioEntityObj);
                controlerBitacora.update(DescriptorBitacora.SERVICIOS, String.valueOf
                        (getServicioEntityObj().getIdServicio()), getServicioEntityObj()
                        .getNombreSite());

                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                servicioEntityObj.setTipoServicioByIdTipoServicio(tipoServicioEntityObj);
                servicioEntityObj.setProveedorByIdProveedor(proveedorEntityObj);
                servicioEntityObj.setUnidadOperativaByIdUnidadOperativa(unidadOperativaEntityObj);
                servicioEntityObj.setBancoByIdBanco(bancoEntityObj);
                servicioEntityObj.setMonedaByIdMoneda(monedaEntityObj);
                servicioEntityObj.setFechaCreacion(new Date());
                servicioBL.save(servicioEntityObj);
                controlerBitacora.insert(DescriptorBitacora.SERVICIOS, String.valueOf(getServicioEntityObj()
                        .getIdServicio()), getServicioEntityObj().getNombreSite());
                saveInmServ();
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
            newServicio();
            FacesContext.getCurrentInstance().getExternalContext().redirect("servicioAllItem.xhtml");
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    /**
     * Persiste en tabla inmu_serv, datos de inmueble y servicio
     */
    private void saveInmServ() {

        if (getSelectPara() == 1) {
            rdbServEntityObj.setServicioByIdServicio(getServicioEntityObj());
            rdbServEntityObj.setRadioBaseByIdRadioBase(getRadioBaseEntityObj());
            rdbsServBL.save(rdbServEntityObj);
        } else {
            inmServEntityObj.setServicioByIdServicio(getServicioEntityObj());
            inmServEntityObj.setInmuebleByIdInmueble(getInmuebleEntityObj());
            inmServEntityObj.setTipoInm(String.valueOf(tipoInmuebleEntityObj.getIdTipoInmueble()));
            inmServBL.save(inmServEntityObj);
        }
        controlerBitacora.insert(DescriptorBitacora.SERVICIOS, String.valueOf(getInmServEntityObj().getIdInmServ()),
                getServicioEntityObj().getNombreSite()
                        + "-" + getInmServEntityObj().getServicioByIdServicio().getCodServMed());
    }

    /**
     * Carga los datos del servicio para edicion
     */
    private void editServicio() {
        try {

            advancedModel = new DefaultMapModel();

            //Shared coordinates
            LatLng coord1 = new LatLng(Double.valueOf(servicioEntityObj.getLatitud()), Double.valueOf(servicioEntityObj.getLonguitud()));

            //Basic marker
            advancedModel.addOverlay(new Marker(coord1, servicioEntityObj.getNombreSite(), servicioEntityObj.getIdServicio(), UtilFile.mostrarImagen(servicioEntityObj.getImagen())));
            cargarObjeto(servicioEntityObj);
            loadInmuebleByTipe();
            loadMapByInmuelbe();
            isNuevoServicio();
        } catch (Exception e) {
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()),
                    "No se logro cargar todos los datos.", "");
            log.log(Level.ERROR, e.getMessage());
        }
    }


    private ServicioEntity cargarObjeto(ServicioEntity servicio) {
        ServicioEntity servicioEntity = servicio;

        setTipoServicioEntityObj(servicio.getTipoServicioByIdTipoServicio());
        setProveedorEntityObj(servicioEntity.getProveedorByIdProveedor());
        setUnidadOperativaEntityObj(servicioEntity.getUnidadOperativaByIdUnidadOperativa());
        setBancoEntityObj(servicioEntity.getBancoByIdBanco());
        setMonedaEntityObj(servicioEntity.getMonedaByIdMoneda());
        InmServEntity inmServEntity = inmServBL.getByIdServicio(servicioEntity
                .getIdServicio());
        if (inmServEntity.getInmuebleByIdInmueble() == null) {
            setSelectPara(1);
            RdbServEntity rdbServEntity = rdbsServBL.getByIdServicio(servicioEntity
                    .getIdServicio());
            setRadioBaseEntityObj(rdbServEntity.getRadioBaseByIdRadioBase());
        } else {
            setSelectPara(2);
            setInmuebleEntityObj(inmServEntity.getInmuebleByIdInmueble());
            setTipoInmuebleEntityObj(inmServEntity.getInmuebleByIdInmueble()
                    .getTipoInmuebleByIdTipoInmueble());
        }
        return servicioEntity;
    }

    /**
     * Carga marcadores en el mapa de acuerdo a seleccion de inmueble
     */
    public void loadMapByInmuelbe() {
        try {
            Double lat = null;
            Double lon = null;
            String nombreShow = "";
            byte[] imagen = null;
            Long id = null;
            if (getSelectPara() == 1) {
                setRadioBaseEntityShow(radioBaseBL.getById(getRadioBaseEntityObj().getIdRadioBase()));
                setCodigo(radioBaseEntityShow.getCodInmueble());
                setNombre(radioBaseEntityShow.getNombreRbs());
                setPropiedad(radioBaseEntityShow.getPropiedadByIdPropiedad().getNombre());
                setDireccion(radioBaseEntityShow.getDireccion());
                lat = Double.parseDouble(getRadioBaseEntityShow().getLatitud());
                lon = Double.parseDouble(getRadioBaseEntityShow().getLongitud());
                nombreShow = getRadioBaseEntityShow().getNombreRbs();
                imagen = getRadioBaseEntityShow().getTipoTorreByIdTipoTorre().getImagen();
                id = getRadioBaseEntityShow().getIdRadioBase();
            } else {
                setInmuebleEntityShow(inmuebleBL.getById(getInmuebleEntityObj().getIdInmueble()));
                setCodigo(inmuebleEntityShow.getCodInmueble());
                setNombre(inmuebleEntityShow.getNombreRbs());
                setPropiedad(inmuebleEntityShow.getPropiedadByIdPropiedad().getNombre());
                setDireccion(inmuebleEntityShow.getDireccion());
                lat = Double.parseDouble(getInmuebleEntityShow().getLatitud());
                lon = Double.parseDouble(getInmuebleEntityShow().getLongitud());
                nombreShow = getInmuebleEntityShow().getNombreRbs();
                imagen = getInmuebleEntityShow().getTipoInmuebleByIdTipoInmueble().getImagen();
                id = getInmuebleEntityShow().getIdInmueble();
            }

            advancedModel = new DefaultMapModel();
            LatLng coord1 = new LatLng(lat, lon);
            advancedModel.addOverlay(new Marker(coord1, nombreShow, id, UtilFile.mostrarImagen
                    (imagen)));
            servicioEntityObj.setLatitud(lat.toString());
            servicioEntityObj.setLonguitud(lon.toString());
            setCollapse(false);
        } catch (Exception e) {
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Sin " +
                    "ubicación para mostrar", "");
            log.log(Level.ERROR, e.getMessage());
        }

    }

    public void loadInmuebleByTipe() {
        inmuebleEntityList = inmuebleBL.listAllByTipoInmueble(tipoInmuebleEntityObj.getIdTipoInmueble());

    }

    public void loadRadioBaseByTipe() {
        radioBaseEntityList = radioBaseBL.listAllByTipoTorre(tipoTorreEntityObj.getIdTipoTorre());
    }

    /**
     * Obtiene el idServicio enviado desde la lista de servicios, para poder ser editado.
     */
    public void idServicio() {

        if (FacesContext.getCurrentInstance().getExternalContext()
                .getRequestMap().get("idServicio") != null) {

            servicioEntityObj = (ServicioEntity) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().get("idServicio");

        }
        if (servicioEntityObj.getIdServicio() > 0) {
            editServicio();
            setDisableControls(true);
        } else {
            setNuevoServicio(true);
        }
    }


    public ControlerBitacora getControlerBitacora() {
        return controlerBitacora;
    }

    public void setControlerBitacora(ControlerBitacora controlerBitacora) {
        this.controlerBitacora = controlerBitacora;
    }

    public void viewMap() {
        try {
            advancedModel = new DefaultMapModel();
            LatLng coord1;
            setVisibleDialog(true);
            if (getSelectPara() == 1) {
                coord1 = new LatLng(Double.valueOf(radioBaseEntityShow.getLatitud()),
                        Double.valueOf(radioBaseEntityShow.getLongitud()));
                advancedModel.addOverlay(new Marker(coord1, radioBaseEntityShow.getNombreRbs(), radioBaseEntityShow.getIdRadioBase(), UtilFile.mostrarImagen(radioBaseEntityShow.getTipoTorreByIdTipoTorre().getImagen())));
                mapaPropiedades.setCenter(radioBaseEntityShow.getLatitud() + "," + radioBaseEntityShow.getLongitud());
            } else {

                coord1 = new LatLng(Double.valueOf(inmuebleEntityShow.getLatitud()),
                        Double.valueOf(inmuebleEntityShow.getLongitud()));
                advancedModel.addOverlay(new Marker(coord1, inmuebleEntityShow.getNombreRbs(), inmuebleEntityShow.getIdInmueble(), UtilFile.mostrarImagen(inmuebleEntityShow.getTipoInmuebleByIdTipoInmueble().getImagen())));

                mapaPropiedades.setCenter(inmuebleEntityShow.getLatitud() + "," + inmuebleEntityShow.getLongitud());
            }

        } catch (Exception e) {
            log.error("[cargarMapa] Fallo al cargar el mapa.", e);
        }
    }

    //region "SETTER y GETTER, OBJETOS"
    public ServicioEntity getServicioEntityObj() {
        return servicioEntityObj;
    }

    public void setServicioEntityObj(ServicioEntity servicioEntityObj) {
        this.servicioEntityObj = servicioEntityObj;
    }

    public TipoServicioEntity getTipoServicioEntityObj() {
        return tipoServicioEntityObj;
    }

    public void setTipoServicioEntityObj(TipoServicioEntity tipoServicioEntityObj) {
        this.tipoServicioEntityObj = tipoServicioEntityObj;
    }

    public ProveedorEntity getProveedorEntityObj() {
        return proveedorEntityObj;
    }

    public void setProveedorEntityObj(ProveedorEntity proveedorEntityObj) {
        this.proveedorEntityObj = proveedorEntityObj;
    }

    public BancoEntity getBancoEntityObj() {
        return bancoEntityObj;
    }

    public void setBancoEntityObj(BancoEntity bancoEntityObj) {
        this.bancoEntityObj = bancoEntityObj;
    }

    public MonedaEntity getMonedaEntityObj() {
        return monedaEntityObj;
    }

    public void setMonedaEntityObj(MonedaEntity monedaEntityObj) {
        this.monedaEntityObj = monedaEntityObj;
    }

    public UnidadOperativaEntity getUnidadOperativaEntityObj() {
        return unidadOperativaEntityObj;
    }

    public void setUnidadOperativaEntityObj(UnidadOperativaEntity unidadOperativaEntityObj) {
        this.unidadOperativaEntityObj = unidadOperativaEntityObj;
    }

    public RadioBaseEntity getRadioBaseEntityObj() {
        return radioBaseEntityObj;
    }

    public void setRadioBaseEntityObj(RadioBaseEntity radioBaseEntityObj) {
        this.radioBaseEntityObj = radioBaseEntityObj;
    }

    public InmuebleEntity getInmuebleEntityObj() {
        return inmuebleEntityObj;
    }

    public void setInmuebleEntityObj(InmuebleEntity inmuebleEntityObj) {
        this.inmuebleEntityObj = inmuebleEntityObj;
    }

    public InmServEntity getInmServEntityObj() {
        return inmServEntityObj;
    }


    public TipoInmuebleEntity getTipoInmuebleEntityObj() {
        return tipoInmuebleEntityObj;
    }

    public void setTipoInmuebleEntityObj(TipoInmuebleEntity tipoInmuebleEntityObj) {
        this.tipoInmuebleEntityObj = tipoInmuebleEntityObj;
    }

    public TipoTorreEntity getTipoTorreEntityObj() {
        return tipoTorreEntityObj;
    }

    public void setTipoTorreEntityObj(TipoTorreEntity tipoTorreEntityObj) {
        this.tipoTorreEntityObj = tipoTorreEntityObj;
    }

    public InmuebleEntity getInmuebleEntityShow() {
        return inmuebleEntityShow;
    }

    public void setInmuebleEntityShow(InmuebleEntity inmuebleEntityShow) {
        this.inmuebleEntityShow = inmuebleEntityShow;
    }

    public RadioBaseEntity getRadioBaseEntityShow() {
        return radioBaseEntityShow;
    }

    public void setRadioBaseEntityShow(RadioBaseEntity radioBaseEntityShow) {
        this.radioBaseEntityShow = radioBaseEntityShow;
    }
//endregion

    //region "SETTER y GETTER, LISTAS"


    public List<MonedaEntity> getMonedaEntityList() {
        return monedaEntityList;
    }

    public void setMonedaEntityList(List<MonedaEntity> monedaEntityList) {
        this.monedaEntityList = monedaEntityList;
    }

    public List<TipoServicioEntity> getTipoServicioEntityList() {
        return tipoServicioEntityList;
    }

    public void setTipoServicioEntityList(List<TipoServicioEntity> tipoServicioEntityList) {
        this.tipoServicioEntityList = tipoServicioEntityList;
    }

    public List<ProveedorEntity> getProveedorEntityList() {
        return proveedorEntityList;
    }

    public void setProveedorEntityList(List<ProveedorEntity> proveedorEntityList) {
        this.proveedorEntityList = proveedorEntityList;
    }

    public List<UnidadOperativaEntity> getUnidadOperativaEntityList() {
        return unidadOperativaEntityList;
    }

    public void setUnidadOperativaEntityList(List<UnidadOperativaEntity> unidadOperativaEntityList) {
        this.unidadOperativaEntityList = unidadOperativaEntityList;
    }

    public List<BancoEntity> getBancoEntityList() {
        return bancoEntityList;
    }

    public void setBancoEntityList(List<BancoEntity> bancoEntityList) {
        this.bancoEntityList = bancoEntityList;
    }

    public List<RadioBaseEntity> getRadioBaseEntityList() {
        return radioBaseEntityList;
    }

    public void setRadioBaseEntityList(List<RadioBaseEntity> radioBaseEntityList) {
        this.radioBaseEntityList = radioBaseEntityList;
    }

    public List<InmuebleEntity> getInmuebleEntityList() {
        return inmuebleEntityList;
    }

    public void setInmuebleEntityList(List<InmuebleEntity> inmuebleEntityList) {
        this.inmuebleEntityList = inmuebleEntityList;
    }

    public List<TipoInmuebleEntity> getTipoInmuebleEntityList() {
        return tipoInmuebleEntityList;
    }

    public void setTipoInmuebleEntityList(List<TipoInmuebleEntity> tipoInmuebleEntityList) {
        this.tipoInmuebleEntityList = tipoInmuebleEntityList;
    }

    public List<TipoTorreEntity> getTipoTorreEntityList() {
        return tipoTorreEntityList;
    }

    public void setTipoTorreEntityList(List<TipoTorreEntity> tipoTorreEntityList) {
        this.tipoTorreEntityList = tipoTorreEntityList;
    }
    //endregion

    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }

    public void setAdvancedModel(MapModel advancedModel) {
        this.advancedModel = advancedModel;
    }

    public int getSelectPara() {
        return selectPara;
    }

    public void setSelectPara(int selectPara) {
        this.selectPara = selectPara;
    }


    public boolean isNuevoServicio() {
        return nuevoServicio;
    }

    public void setNuevoServicio(boolean nuevoServicio) {
        this.nuevoServicio = nuevoServicio;
    }

    public boolean isCollapse() {
        return collapse;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isDisableControls() {
        return disableControls;
    }

    public void setDisableControls(boolean disableControls) {
        this.disableControls = disableControls;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Map<String, String> getMiscParameter() {
        return miscParameter;
    }

    public void setMiscParameter(Map<String, String> miscParameter) {
        this.miscParameter = miscParameter;
    }
}
