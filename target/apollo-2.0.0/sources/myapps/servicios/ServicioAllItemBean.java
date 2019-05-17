package myapps.servicios;

import myapps.abm.business.*;
import myapps.abm.model.*;
import myapps.servicio_basico.bean.model.MapaGoole;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.servicio_basico.util.UtilDate;
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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class ServicioAllItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(ServicioAllItemBean.class);
    @Inject
    private ServicioBL servicioBL;
    @Inject
    private SelectOptions selectOptions;
    @Inject
    private ControlerBitacora controlerBitacora;

    @EJB
    private IApplicationContext beanApplicationContext;

    private List<ServicioEntity> servicioEntityList = new ArrayList<>();
    private List<MonedaEntity> monedaEntityList = new ArrayList<>();
    private List<TipoServicioEntity> tipoServicioEntityList = new ArrayList<>();
    private List<ProveedorEntity> proveedorEntityList = new ArrayList<>();
    private List<UnidadOperativaEntity> unidadOperativaEntityList = new ArrayList<>();
    private List<BancoEntity> bancoEntityList = new ArrayList<>();
    private List<RadioBaseEntity> radioBaseEntityList = new ArrayList<>();
    private List<InmuebleEntity> inmuebleEntityList = new ArrayList<>();
    private List<TipoInmuebleEntity> tipoInmuebleEntityList = new ArrayList<>();
    private List<TipoTorreEntity> tipoTorreEntityList = new ArrayList<>();
    transient List<MapModel> mapModels = null;

    private ServicioEntity servicioEntityObj;
    private TipoServicioEntity tipoServicioEntityObj;
    private ProveedorEntity proveedorEntityObj;
    private BancoEntity bancoEntityObj;
    private MonedaEntity monedaEntityObj;
    private UnidadOperativaEntity unidadOperativaEntityObj;
    private RadioBaseEntity radioBaseEntityObj;
    private InmuebleEntity inmuebleEntityObj;
    private InmServEntity inmServEntityObj;
    private TipoInmuebleEntity tipoInmuebleEntityObj;
    private TipoTorreEntity tipoTorreEntityObj;

    private boolean edit;
    private long selectIdServicio;
    private Map<String, String> validateParameter;
    private Map<String, String> miscParameter;
    private List<SelectItem> selectItemsTS;
    private List<SelectItem> selectItemsP;
    private List<SelectItem> selectItemsBanco;
    private List<SelectItem> selectItemsUO;
    private String select;
    private transient MapModel advancedModel;
    private int selectPara;
    private Date fechaDesde;
    private Date fechaHasta;
    private static final String FORMATO_FECHA = "yyyy-MM-dd";
    private boolean visibleDialog = false;
    private transient MapaGoole mapaPropiedades;
    private ServicioEntity selectServicioEntityObj;
    private boolean enableSelectTipoImn = false;
    private boolean enableSelectTipoTor = false;

    @PostConstruct
    public void init() {
        try {
            servicioEntityObj = new ServicioEntity();
            tipoServicioEntityObj = new TipoServicioEntity();
            proveedorEntityObj = new ProveedorEntity();
            bancoEntityObj = new BancoEntity();
            unidadOperativaEntityObj = new UnidadOperativaEntity();
            monedaEntityObj = new MonedaEntity();
            inmuebleEntityObj = new InmuebleEntity();
            radioBaseEntityObj = new RadioBaseEntity();
            inmServEntityObj = new InmServEntity();
            tipoInmuebleEntityObj = new TipoInmuebleEntity();
            tipoTorreEntityObj = new TipoTorreEntity();
            selectServicioEntityObj = new ServicioEntity();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            miscParameter = beanApplicationContext.getMapMiscellaneousParameters();
            loadSelectOneMenu();
            loadServicios();
            mapaPropiedades = new MapaGoole(miscParameter.get(EnumParametros.LATITUD_CENTER_MAPA.toString()) + "," + miscParameter.get(EnumParametros.LONGITUD_CENTER_MAPA.toString()),
                    miscParameter.get(EnumParametros.ZOOM_MAPA.toString()),
                    miscParameter.get(EnumParametros.TIPO_MAPA.toString()));
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    private void loadSelectOneMenu() {
        concatProveedor();
        setTipoInmuebleEntityList(selectOptions.cbxTipoInmFindAll());
        setTipoTorreEntityList(selectOptions.cbxTipoTorre());

    }


    private List<ProveedorEntity> concatProveedor() {

        proveedorEntityList = selectOptions.cbxProveedores();
        int cont = 0;
        for (ProveedorEntity proveedorEntity : getProveedorEntityList()) {
            proveedorEntity.setNombre(proveedorEntity.getSigla() + " - " + proveedorEntity.getNombre());
            proveedorEntityList.set(cont, proveedorEntity);
            cont++;
        }
        return proveedorEntityList;
    }

    private void loadServicios() {
        servicioEntityList = servicioBL.listAll();
    }

    /**
     * Selimina un servicio de la lista
     */
    public void deleteServicio() {
        try {
            setSelectIdServicio(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("idServicio")));
            setServicioEntityObj(servicioBL.getById(getSelectIdServicio()));
            servicioBL.delete(getServicioEntityObj());
            controlerBitacora.delete(DescriptorBitacora.SERVICIOS, String.valueOf
                    (getServicioEntityObj()
                            .getIdServicio()), getServicioEntityObj().getNombreSite());

            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            loadServicios();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    /**
     * Filtro por criterios
     */
    public void buscar() {
        servicioEntityList = new ArrayList<>();
        Map<String, Object> parameter = new HashMap<>();
        if ((getTipoInmuebleEntityObj().getIdTipoInmueble() != 0) || getTipoTorreEntityObj()
                .getIdTipoTorre() != 0) {
            parameter.put("para", String.valueOf(getSelectPara()));
            if (getTipoTorreEntityObj().getIdTipoTorre() > 0) {
                parameter.put("tipotorre", String.valueOf(getTipoTorreEntityObj().getIdTipoTorre()));
            }
            if (getTipoInmuebleEntityObj().getIdTipoInmueble() > 0) {
                parameter.put("tipoinmserv", String.valueOf(getTipoInmuebleEntityObj().getIdTipoInmueble()));
            }
            if (proveedorEntityObj.getIdProveedor() > 0) {
                parameter.put("idproveedor", String.valueOf(proveedorEntityObj.getIdProveedor()));
            }
            if (servicioEntityObj.getFormaPago() != null) {
                parameter.put("formapago", servicioEntityObj.getFormaPago());
            }
            if (getFechaDesde() != null && getFechaHasta() != null) {
                parameter.put("fechaDesde", UtilDate.dateToString(getFechaDesde(), FORMATO_FECHA));
                parameter.put("fechaHasta", UtilDate.dateToString(getFechaHasta(), FORMATO_FECHA));
            } else {
                if (getFechaDesde() != null) {
                    parameter.put("fecha", UtilDate.dateToString(getFechaDesde(), FORMATO_FECHA));
                }
                if (getFechaHasta() != null) {
                    parameter.put("fecha", UtilDate.dateToString(getFechaHasta(), FORMATO_FECHA));
                }
            }

            if (!servicioEntityObj.getNombreSite().equals("")) {
                parameter.put("nombresite", servicioEntityObj.getNombreSite());
            }
            if (!servicioEntityObj.getCodServMed().equals("")) {
                parameter.put("servmed", servicioEntityObj.getCodServMed());
            }
            if (!servicioEntityObj.getCodigo().equals("")) {
                parameter.put("codfijo", servicioEntityObj.getCodigo());
            }


            if (parameter.size() == 0) {
                servicioEntityList = servicioBL.listAll();
            } else {
                servicioEntityList = servicioBL.listAllFilter(parameter, getSelectPara());
            }
        } else {
            SysMessage.warn("Debe seleccionar una categoria.", null);

        }
    }


    /**
     * Obtiene objeto de la lista
     *
     * @param index
     * @return link servicios.xhtml
     */
    public String obtenerIdServ(ServicioEntity servicioEntity) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("idServicio",
                servicioEntity);
        return "/view/servicio.xhtml";
    }

    public void clean() {

        setTipoInmuebleEntityObj(new TipoInmuebleEntity());
        setTipoTorreEntityObj(new TipoTorreEntity());
        setProveedorEntityObj(new ProveedorEntity());
        setServicioEntityObj(new ServicioEntity());
        setFechaDesde(null);
        setFechaHasta(null);
        setEnableSelectTipoImn(false);
        setEnableSelectTipoTor(false);
        loadServicios();
    }


    public void viewMap(ServicioEntity serSelected) {
        try {

            if (serSelected == null) {
                advancedModel = new DefaultMapModel();
                for (ServicioEntity servicioEntity : getServicioEntityList()) {
                    LatLng coord1 = new LatLng(Double.valueOf(servicioEntity.getLatitud()), Double.valueOf(servicioEntity.getLonguitud()));
                    advancedModel.addOverlay(new Marker(coord1, servicioEntity.getNombreSite(), servicioEntity.getIdServicio(), UtilFile.mostrarImagen(servicioEntity.getImagen())));
                }
                mapaPropiedades.setCenter(miscParameter.get(EnumParametros.LATITUD_CENTER_MAPA.toString()) + "," + miscParameter.get(EnumParametros.LONGITUD_CENTER_MAPA.toString()));
            } else {
                advancedModel = new DefaultMapModel();
                LatLng coord1 = new LatLng(Double.valueOf(serSelected.getLatitud()), Double.valueOf(serSelected.getLonguitud()));
                advancedModel.addOverlay(new Marker(coord1, serSelected.getNombreSite(), serSelected.getIdServicio(), UtilFile.mostrarImagen(serSelected.getImagen())));
                mapaPropiedades.setCenter(serSelected.getLatitud() + "," + serSelected.getLonguitud());
            }
        } catch (Exception e) {
            log.error("[cargarMapa] Fallo al cargar el mapa.", e);
        }
    }

    public ControlerBitacora getControlerBitacora() {
        return controlerBitacora;
    }

    public void setControlerBitacora(ControlerBitacora controlerBitacora) {
        this.controlerBitacora = controlerBitacora;
    }

    public void loadInmuebleByTipe() {
        if (getTipoInmuebleEntityObj().getIdTipoInmueble() > 0) {
            setSelectPara(2);
            setEnableSelectTipoTor(true);
        }
        if (getTipoTorreEntityObj().getIdTipoTorre() > 0) {
            setSelectPara(1);
            setEnableSelectTipoImn(true);
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

    public void setInmServEntityObj(InmServEntity inmServEntityObj) {
        this.inmServEntityObj = inmServEntityObj;
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
    //endregion

    //region "SETTER y GETTER, LISTAS"
    public List<ServicioEntity> getServicioEntityList() {
        return servicioEntityList;
    }

    public void setServicioEntityList(List<ServicioEntity> servicioEntityList) {
        this.servicioEntityList = servicioEntityList;
    }

    public List<SelectItem> getSelectItemsTS() {
        return selectItemsTS;
    }

    public void setSelectItemsTS(List<SelectItem> selectItemsTS) {
        this.selectItemsTS = selectItemsTS;
    }

    public List<SelectItem> getSelectItemsP() {
        return selectItemsP;
    }

    public void setSelectItemsP(List<SelectItem> selectItemsP) {
        this.selectItemsP = selectItemsP;
    }

    public List<SelectItem> getSelectItemsBanco() {
        return selectItemsBanco;
    }

    public void setSelectItemsBanco(List<SelectItem> selectItemsBanco) {
        this.selectItemsBanco = selectItemsBanco;
    }

    public List<SelectItem> getSelectItemsUO() {
        return selectItemsUO;
    }

    public void setSelectItemsUO(List<SelectItem> selectItemsUO) {
        this.selectItemsUO = selectItemsUO;
    }

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

    public long getSelectIdServicio() {
        return selectIdServicio;
    }

    public void setSelectIdServicio(long selectIdServicio) {
        this.selectIdServicio = selectIdServicio;
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

    public Map<String, String> getMiscParameter() {
        return miscParameter;
    }

    public void setMiscParameter(Map<String, String> miscParameter) {
        this.miscParameter = miscParameter;
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

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {

        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public boolean isVisibleDialog() {
        return visibleDialog;
    }

    public void setVisibleDialog(boolean visibleDialog) {
        this.visibleDialog = visibleDialog;
    }

    public List<MapModel> getMapModels() {
        return mapModels;
    }

    public void setMapModels(List<MapModel> mapModels) {
        this.mapModels = mapModels;
    }

    public ServicioEntity getSelectServicioEntityObj() {
        return selectServicioEntityObj;
    }

    public void setSelectServicioEntityObj(ServicioEntity selectServicioEntityObj) {
        this.selectServicioEntityObj = selectServicioEntityObj;
    }

    public MapaGoole getMapaPropiedades() {
        return mapaPropiedades;
    }

    public void setMapaPropiedades(MapaGoole mapaPropiedades) {
        this.mapaPropiedades = mapaPropiedades;
    }

    public boolean isEnableSelectTipoImn() {
        return enableSelectTipoImn;
    }

    public void setEnableSelectTipoImn(boolean enableSelectTipoImn) {
        this.enableSelectTipoImn = enableSelectTipoImn;
    }

    public boolean isEnableSelectTipoTor() {
        return enableSelectTipoTor;
    }

    public void setEnableSelectTipoTor(boolean enableSelectTipoTor) {
        this.enableSelectTipoTor = enableSelectTipoTor;
    }
}
