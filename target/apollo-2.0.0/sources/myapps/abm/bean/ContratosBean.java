package myapps.abm.bean;

import myapps.abm.business.*;
import myapps.abm.model.*;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.servicio_basico.util.UtilDate;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.DescriptorBitacora;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static myapps.servicio_basico.util.UtilDate.dateToSql;

@ManagedBean
@ViewScoped
public class ContratosBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(ContratosBean.class);

    @Inject
    private ContratosBL contratosBL;
    @Inject
    private ContratosRenovadosBL contratosRenovadosBL;

    @Inject
    private ProveedorBL proveedorBL;
    @Inject
    private ServicioBL servicioBL;
    @Inject
    private InmuebleBL inmuebleBL;
    @Inject
    private RadioBaseBL radioBaseBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @Inject
    private SelectOptions selectOptions;
    @Inject
    private DocumentosBL documentosBL;
    @EJB
    private IApplicationContext iApplicationContext;

    private List<ContratosEntity> contratosEntityList = new ArrayList<>();
    private List<ServicioEntity> servicioEntityList = new ArrayList<>();
    private List<InmuebleEntity> inmuebleEntityList = new ArrayList<>();
    private List<RadioBaseEntity> radioBaseEntityList = new ArrayList<>();
    private List<ProveedorEntity> proveedorEntityList = new ArrayList<>();
    private List<DocumentosEntity> documentosEntityList = new ArrayList<>();
    private List<DepartamentoEntity> departamentoEntityList = new ArrayList<>();
    private List<CategoriaEntity> categoriaEntityList;
    private List<String> categoria = new ArrayList<>();
    private List<String> combos = new ArrayList<>();

    private ContratosEntity contratosEntityObj;
    private ContratosEntity contratosEntityObjRenovado;
    private transient HistorialContratosEntity historialContratosEntityObj;
    private ContratosEntity selectcontratosEntityObj;
    private InmuebleEntity inmuebleEntityObj;
    private ServicioEntity servicioEntityObj;
    private RadioBaseEntity radioBaseEntityObj;
    private ProveedorEntity proveedorEntityObj;
    private DocumentosEntity documentosEntityObj;
    private DepartamentoEntity departamentoEntityObj;

    private InmuebleEntity selectInmuebleEntityObj;
    private ServicioEntity selectServicioEntityObj;
    private RadioBaseEntity selectRadioBaseEntityObj;
    private ProveedorEntity selectProveedorEntityObj;

    private Map<String, String> validateParameter;
    private Map<String, String> emailParameter;

    private boolean editContratos;
    private long selectIdContratos;
    private boolean mostrarInmueble;
    private boolean mostrarServicio;
    private boolean mostrarRadioBase;
    private boolean mostrarProveedor;
    private Date fechaCreacion;
    private Date fechaExpiracion;
    private String pertenece;
    private static final String INMUEBLE = "Inmueble";
    private static final String SERVICIO = "Servicio";
    private static final String PROVEEDOR = "Proveedor";
    private static final String RADIO_BASE = "Radio Base";
    private static final String SIN_ASIGNACION = "Sin Asignación";


    private FileUploadEvent fileUploadEvent;
    private String directoryFile;
    private String tamAdjuntos;
    private String extenAdjuntos;
    private String invalidateMessage;

    private transient StreamedContent descarga;

    private boolean renovacion;
    private int optionContrato;
    private transient HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();


    @PostConstruct
    void init() {
        try {
            departamentoEntityObj = new DepartamentoEntity();
            contratosEntityObj = new ContratosEntity();
            inmuebleEntityObj = new InmuebleEntity();
            selectInmuebleEntityObj = new InmuebleEntity();
            servicioEntityObj = new ServicioEntity();
            radioBaseEntityObj = new RadioBaseEntity();
            proveedorEntityObj = new ProveedorEntity();
            documentosEntityObj = new DocumentosEntity();
            inmuebleEntityList = selectOptions.cbxImnueble();
            servicioEntityList = selectOptions.cbxOnlyServicio();
            radioBaseEntityList = selectOptions.cbxRadioBases();
            proveedorEntityList = selectOptions.cbxProveedores();
            categoriaEntityList = selectOptions.cbxCategoria();
            departamentoEntityList = selectOptions.cbxDepartamento();

            loadCategoria();
            validateParameter = iApplicationContext.getMapValidateParameters();
            emailParameter = iApplicationContext.getMapEmailParameters();

            loadParameterMiscellaneuos();
            mostrarServicio = false;
            mostrarInmueble = false;
            fechaCreacion = new Date();
            fechaExpiracion = new Date();
        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }

    public void iconoInmueble() {
        if (inmuebleEntityObj.getIdInmueble() > 0) {
            selectInmuebleEntityObj = inmuebleBL.getById(inmuebleEntityObj.getIdInmueble());
            if (selectInmuebleEntityObj.getLocalidadByIdLocalidad().getMunicipioByIdMunicipio().getProvinciaByIdProvincia().getDepartamentoByIdDepartamento() != null) {
                departamentoEntityObj.setIdDepartamento(selectInmuebleEntityObj.getLocalidadByIdLocalidad().getMunicipioByIdMunicipio().getProvinciaByIdProvincia().getDepartamentoByIdDepartamento().getIdDepartamento());
            }
        }
    }

    public void iconoServicio() {
        if (servicioEntityObj.getIdServicio() > 0) {
            selectServicioEntityObj = servicioBL.getById(servicioEntityObj.getIdServicio());
            if (selectServicioEntityObj.getProveedorByIdProveedor().getDepartamentoByIdDepartamento() != null) {
                departamentoEntityObj.setIdDepartamento(selectServicioEntityObj.getProveedorByIdProveedor().getDepartamentoByIdDepartamento().getIdDepartamento());
            }
        }
    }

    public void iconoRadioBase() {
        if (radioBaseEntityObj.getIdRadioBase() > 0) {
            selectRadioBaseEntityObj = radioBaseBL.getById(radioBaseEntityObj.getIdRadioBase());
            if (selectRadioBaseEntityObj.getLocalidadByIdLocalidad().getMunicipioByIdMunicipio().getProvinciaByIdProvincia().getDepartamentoByIdDepartamento() != null) {
                departamentoEntityObj.setIdDepartamento(selectRadioBaseEntityObj.getLocalidadByIdLocalidad().getMunicipioByIdMunicipio().getProvinciaByIdProvincia().getDepartamentoByIdDepartamento().getIdDepartamento());
            }
        }
    }

    public void iconoProveedor() {
        if (proveedorEntityObj.getIdProveedor() > 0) {
            selectProveedorEntityObj = proveedorBL.getProveedordById(proveedorEntityObj.getIdProveedor());
            if (selectProveedorEntityObj.getDepartamentoByIdDepartamento() != null) {
                departamentoEntityObj.setIdDepartamento(selectProveedorEntityObj.getDepartamentoByIdDepartamento().getIdDepartamento());
            }
        }
    }

    public void mostrar() {
        switch (getPertenece()) {
            case INMUEBLE:
                mostrarInmueble = true;
                mostrarServicio = false;
                mostrarProveedor = false;
                mostrarRadioBase = false;
                break;
            case RADIO_BASE:
                mostrarServicio = false;
                mostrarInmueble = false;
                mostrarProveedor = false;
                mostrarRadioBase = true;
                break;
            case SERVICIO:
                mostrarServicio = true;
                mostrarInmueble = false;
                mostrarProveedor = false;
                mostrarRadioBase = false;
                break;
            case PROVEEDOR:
                mostrarServicio = false;
                mostrarInmueble = false;
                mostrarProveedor = true;
                mostrarRadioBase = false;
                break;
            case SIN_ASIGNACION:
                mostrarServicio = false;
                mostrarInmueble = false;
                mostrarProveedor = false;
                mostrarRadioBase = false;
                break;
            default:
                mostrarServicio = false;
                mostrarInmueble = false;
                mostrarProveedor = false;
                mostrarRadioBase = false;
                SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Seleccione un valor", null);
                break;

        }
        limpiarEntidad();
    }

    public void newContratos() {
        contratosEntityObj = new ContratosEntity();
        limpiarEntidad();
        setEditContratos(false);
        pertenece = "";
        mostrarServicio = false;
        mostrarInmueble = false;
        mostrarRadioBase = false;
        mostrarProveedor = false;
        documentosEntityList.clear();
        fechaExpiracion = new Date();
        fechaCreacion = new Date();
    }

    public void limpiarEntidad() {
        servicioEntityObj = new ServicioEntity();
        inmuebleEntityObj = new InmuebleEntity();
        radioBaseEntityObj = new RadioBaseEntity();
        proveedorEntityObj = new ProveedorEntity();
        selectInmuebleEntityObj = new InmuebleEntity();
        selectServicioEntityObj = new ServicioEntity();
        selectRadioBaseEntityObj = new RadioBaseEntity();
        selectProveedorEntityObj = new ProveedorEntity();
        departamentoEntityObj = new DepartamentoEntity();
    }

    public boolean validarCombos() {
        combos.clear();
        boolean validar = false;
        switch (pertenece) {
            case INMUEBLE:
                contratosEntityObj.setInmuebleByIdInmueble(inmuebleEntityObj);
                break;
            case RADIO_BASE:
                contratosEntityObj.setRadioBaseByIdRadioBase(radioBaseEntityObj);
                break;
            case SERVICIO:
                contratosEntityObj.setServicioByIdServicio(servicioEntityObj);
                break;
            case PROVEEDOR:
                contratosEntityObj.setProveedorByIdProveedor(proveedorEntityObj);
                break;
            default:
                logger.error("Variable no Definida");
                break;
        }

        if (!contratosEntityObj.getNroContrato().isEmpty()) {
            for (ContratosEntity contrato : contratosEntityList) {
                if (contratosEntityObj.getNroContrato().equals(contrato.getNroContrato()) && contratosEntityObj.getIdContrato() != contrato.getIdContrato()) {
                    combos.add("Este numero de contrato ya esta siendo utilizado");
                }
            }
        }
        if ((optionContrato == 1) && (UtilDate.comparaFechaDate(fechaExpiracion) != 2)) {
            combos.add("La Fecha de Expiración debe ser mayor.");
        } else if (UtilDate.numeroDiasEntreDosFechas(dateToSql(fechaCreacion), dateToSql(fechaExpiracion)) < 1) {
            combos.add("La Fecha de Creación no puede ser igual o menor a la fecha de Expiración");
        }

        if (!combos.isEmpty()) {
            validar = true;
            for (String message : combos) {
                SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), message, null);
            }
        }
        return validar;
    }

    public void actionContratos() {
        if (validarCombos()) {
            return;
        }
        try {
            if (isEditContratos() && contratosEntityObj.getIdContrato() > 0) {
                contratosEntityObj.setDepartamentoByIdDepartamento(departamentoEntityObj);
                contratosEntityObj.setDias(UtilDate.numeroDiasEntreDosFechas(dateToSql(fechaCreacion), dateToSql(fechaExpiracion)));
                contratosEntityObj.setExpiracion(dateToSql(fechaExpiracion));
                contratosEntityObj.setFechaCreacion(dateToSql(fechaCreacion));
                contratosBL.update(contratosEntityObj, documentosEntityList);
                controlerBitacora.update(DescriptorBitacora.CONTRATOS, String.valueOf
                        (getContratosEntityObj().getIdContrato()), getContratosEntityObj()
                        .getResponsable());
            } else {
                contratosEntityObj.setDepartamentoByIdDepartamento(departamentoEntityObj);
                contratosEntityObj.setDias(UtilDate.numeroDiasEntreDosFechas(dateToSql(fechaCreacion), dateToSql(fechaExpiracion)));
                contratosEntityObj.setExpiracion(dateToSql(fechaExpiracion));
                contratosEntityObj.setFechaCreacion(dateToSql(fechaCreacion));
                contratosEntityObj.setFechaRegistro(new Date());
                contratosBL.save(contratosEntityObj, documentosEntityList);
                controlerBitacora.insert(DescriptorBitacora.CONTRATOS, String.valueOf
                        (getContratosEntityObj()
                                .getIdContrato()), getContratosEntityObj().getResponsable());
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect("contratosAllItem.xhtml");

        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }


    private void loadCategoria() {
        categoria.add(SIN_ASIGNACION);
        categoria.add(INMUEBLE);
        categoria.add(RADIO_BASE);
        categoria.add(SERVICIO);
        categoria.add(PROVEEDOR);
    }

    public void editarContratos() {

        if (FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("ContratoObj") != null) {
            contratosEntityObj = (ContratosEntity) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("ContratoObj");

            if (UtilDate.comparaFechaDate(contratosEntityObj.getExpiracion()) == 1) {
                historialContratosEntityObj = new HistorialContratosEntity();
                renovacion = true;
                contratosEntityObjRenovado = new ContratosEntity();
                contratosEntityObjRenovado.setNroContrato(contratosEntityObj.getNroContrato());
                contratosEntityObjRenovado.setResponsable(contratosEntityObj.getResponsable());
                contratosEntityObjRenovado.setPropietario(contratosEntityObj.getPropietario());
                contratosEntityObjRenovado.setCaducidad(contratosEntityObj.getCaducidad());
                contratosEntityObjRenovado.setExpiracion(contratosEntityObj.getExpiracion());
                contratosEntityObjRenovado.setTipo(contratosEntityObj.getTipo());
                contratosEntityObjRenovado.setCatInm(contratosEntityObj.getCatInm());
                contratosEntityObjRenovado.setFechaCreacion(contratosEntityObj.getFechaCreacion());
                contratosEntityObjRenovado.setCatCont(contratosEntityObj.getCatCont());
                contratosEntityObjRenovado.setAdjunto(contratosEntityObj.getAdjunto());
                contratosEntityObjRenovado.setCodEbs(contratosEntityObj.getCodEbs());
                contratosEntityObjRenovado.setCanonAlquiler(contratosEntityObj.getCanonAlquiler());
                contratosEntityObjRenovado.setEstado(contratosEntityObj.getEstado());

                contratosEntityObjRenovado.setServicioByIdServicio(contratosEntityObj.getServicioByIdServicio() == null ? null : contratosEntityObj.getServicioByIdServicio());
                contratosEntityObjRenovado.setInmuebleByIdInmueble(contratosEntityObj.getInmuebleByIdInmueble() == null ? null : contratosEntityObj.getInmuebleByIdInmueble());
                contratosEntityObjRenovado.setRadioBaseByIdRadioBase(contratosEntityObj.getRadioBaseByIdRadioBase() == null ? null : contratosEntityObj.getRadioBaseByIdRadioBase());
                contratosEntityObjRenovado.setProveedorByIdProveedor(contratosEntityObj.getProveedorByIdProveedor() == null ? null : contratosEntityObj.getProveedorByIdProveedor());
                contratosEntityObjRenovado.setDepartamentoByIdDepartamento(contratosEntityObj.getDepartamentoByIdDepartamento());

                contratosEntityObjRenovado.setFechaRegistro(contratosEntityObj.getFechaRegistro());
                contratosEntityObjRenovado.setCategoria(contratosEntityObj.getCategoria());
                contratosEntityObjRenovado.setMonto(contratosEntityObj.getMonto());
                contratosEntityObjRenovado.setEstadoContrato(contratosEntityObj.getEstadoContrato());
                contratosEntityObjRenovado.setDias(contratosEntityObj.getDias());
            }

            if (contratosEntityObj.getServicioByIdServicio() != null) {
                mostrarServicio = true;
                mostrarInmueble = false;
                mostrarProveedor = false;
                mostrarRadioBase = false;
                pertenece = SERVICIO;
                servicioEntityObj.setIdServicio(contratosEntityObj.getServicioByIdServicio().getIdServicio());

                iconoServicio();
            } else if (contratosEntityObj.getInmuebleByIdInmueble() != null) {
                mostrarInmueble = true;
                mostrarServicio = false;
                mostrarProveedor = false;
                mostrarRadioBase = false;

                pertenece = INMUEBLE;
                inmuebleEntityObj.setIdInmueble(contratosEntityObj.getInmuebleByIdInmueble().getIdInmueble());

                iconoInmueble();
            } else if (contratosEntityObj.getRadioBaseByIdRadioBase() != null) {
                mostrarInmueble = false;
                mostrarServicio = false;
                mostrarProveedor = false;
                mostrarRadioBase = true;

                pertenece = RADIO_BASE;
                radioBaseEntityObj.setIdRadioBase(contratosEntityObj.getRadioBaseByIdRadioBase().getIdRadioBase());

                iconoRadioBase();
            } else if (contratosEntityObj.getProveedorByIdProveedor() != null) {
                mostrarInmueble = false;
                mostrarServicio = false;
                mostrarProveedor = true;
                mostrarRadioBase = false;

                pertenece = PROVEEDOR;
                proveedorEntityObj.setIdProveedor(contratosEntityObj.getProveedorByIdProveedor().getIdProveedor());

                iconoProveedor();
            } else {
                pertenece = SIN_ASIGNACION;
                mostrarInmueble = false;
                mostrarServicio = false;
                mostrarProveedor = false;
                mostrarRadioBase = false;
            }

            fechaCreacion = contratosEntityObj.getFechaCreacion();
            fechaExpiracion = contratosEntityObj.getExpiracion();

            if (contratosEntityObj.getDepartamentoByIdDepartamento() != null) {
                departamentoEntityObj.setIdDepartamento(contratosEntityObj.getDepartamentoByIdDepartamento().getIdDepartamento());
            }
            fechaCreacion = contratosEntityObj.getFechaCreacion();
            fechaExpiracion = contratosEntityObj.getExpiracion();

            documentosEntityList = documentosBL.listAll(contratosEntityObj.getIdContrato());
            setEditContratos(true);
        }


    }

    public void renovarContrato() {
        if (optionContrato > 0) {
            contratosEntityObj.setEstadoContrato
                    (optionContrato == 2 ? "Vencido" : contratosEntityObj.getEstadoContrato());
            actionContratos();
            historialContratosEntityObj.setFecha(new Date());
            historialContratosEntityObj.setResponsable((String) request.getSession().getAttribute("TEMP$USER_NAME"));
            historialContratosEntityObj.setTipo(optionContrato == 1 ? "RENOVADO" : "FINALIZADO");
            historialContratosEntityObj.setContratosByContratoId(contratosEntityObj);
            historialContratosEntityObj.setFechaCreacion(contratosEntityObjRenovado.getFechaCreacion());
            historialContratosEntityObj.setFechaExpiracion(contratosEntityObjRenovado.getExpiracion());

            contratosEntityObjRenovado.setEstadoContrato
                    (optionContrato == 2 ? "Finalizado" : "Renovado");
            contratosRenovadosBL.save(contratosEntityObjRenovado, historialContratosEntityObj);

        } else {
            SysMessage.warn("Seleecione una opcion a realizar.", null);
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            setFileUploadEvent(event);
            int duplicado = 0;
            for (DocumentosEntity doc : documentosEntityList) {
                if (fileUploadEvent.getFile().getFileName().equals(doc.getNombre())) {
                    duplicado++;
                }
            }
            if (duplicado == 0) {
                documentosEntityObj = new DocumentosEntity();
                documentosEntityObj.setUbicacion(getDirectoryFile());
                documentosEntityObj.setNombre(getFileUploadEvent().getFile().getFileName());
                documentosEntityObj.setPertenece("CONTRATOS");
                documentosEntityObj.setEstado(true);
                documentosEntityObj.setDocAdjunto(fileUploadEvent.getFile().getContents());
                documentosEntityList.add(documentosEntityObj);
                SysMessage.handleFileUpload(event);
            } else {
                SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Archivo Duplicado", "");
            }

        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadParameterMiscellaneuos() {
        setDirectoryFile(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .DIRECTORIO_ADJUNTOS.toString()));
        setTamAdjuntos(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .TAMAÑO_ADJUNTOS.toString()));
        setExtenAdjuntos(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .EXTENSION_ADJUNTOS.toString()));
        setInvalidateMessage(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .EXTENSION_ADJUNTOS_MSG.toString()));

    }

    public void loadDocAdjuntos(ContratosEntity contrato) {
        documentosEntityList.clear();
        documentosEntityList = documentosBL.listAll(contrato.getIdContrato());

    }

    public void donwloadAdjunt(DocumentosEntity documento) {
        try {

            DocumentosEntity descargaDoc = documentosBL.donwloadDoc(documento);

            InputStream stream = new ByteArrayInputStream(descargaDoc.getDocAdjunto());
            descarga = new DefaultStreamedContent(stream, "application/" + descargaDoc.getExtension(),
                    descargaDoc.getNombre());
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()),
                    "Directorio no encontrado: " + e.getMessage());
        }
    }

    public boolean validarCombosP() {
        boolean validarP = false;
        List<String> mensajesValidacion = new ArrayList<>();
        if (proveedorEntityObj.getDepartamentoByIdDepartamento() == null) {
            mensajesValidacion.add("Seleccione un Departamento");
        }
        if (proveedorEntityObj.getCategoriaByIdCategoria() == null) {
            mensajesValidacion.add("Seleccione una Categoria");
        }
        if (proveedorEntityObj.getTipo().equals("Nada")) {
            mensajesValidacion.add("Seleccione un Tipo");
        }
        if (!mensajesValidacion.isEmpty()) {
            validarP = true;
            for (String message : mensajesValidacion) {
                SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), message, null);
            }
        }
        return validarP;

    }

    public void guardarProveedor() {
        if (validarCombosP()) {
            return;
        }
        try {
            proveedorEntityObj.setIdProveedor(0);
            proveedorEntityObj.setIsFusion(Long.valueOf(0));
            proveedorBL.save(proveedorEntityObj);
            controlerBitacora.insert(DescriptorBitacora.PROVEEDORES, String.valueOf(getProveedorEntityObj()
                    .getIdProveedor()), getProveedorEntityObj().getNombre());
            PrimeFaces.current().executeScript("PF('newDialogo').hide();");
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al Crear el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
        limpiarEntidad();
        proveedorEntityList = selectOptions.cbxProveedores();
    }

    public void eliminarProveedor() {
        try {
            proveedorBL.delete(getSelectProveedorEntityObj());
            proveedorBL.deleteFusionProveedores(getSelectProveedorEntityObj().getIdProveedor());
            controlerBitacora.delete(DescriptorBitacora.PROVEEDORES, String.valueOf
                    (getProveedorEntityObj().getIdProveedor()), getProveedorEntityObj().getNombre());
            PrimeFaces.current().executeScript("PF('delettProveedor').hide();");
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
        limpiarEntidad();
        proveedorEntityList = selectOptions.cbxProveedores();
    }

    public List<ContratosEntity> getContratosEntityList() {
        return contratosEntityList;
    }

    public void setContratosEntityList(List<ContratosEntity> contratosEntityList) {
        this.contratosEntityList = contratosEntityList;
    }

    public ContratosEntity getContratosEntityObj() {
        return contratosEntityObj;
    }

    public void setContratosEntityObj(ContratosEntity contratosEntityObj) {
        this.contratosEntityObj = contratosEntityObj;
    }

    public ContratosEntity getContratosEntityObjRenovado() {
        return contratosEntityObjRenovado;
    }

    public void setContratosEntityObjRenovado(ContratosEntity contratosEntityObjRenovado) {
        this.contratosEntityObjRenovado = contratosEntityObjRenovado;
    }

    public HistorialContratosEntity getHistorialContratosEntityObj() {
        return historialContratosEntityObj;
    }

    public void setHistorialContratosEntityObj(HistorialContratosEntity historialContratosEntityObj) {
        this.historialContratosEntityObj = historialContratosEntityObj;
    }

    public long getSelectIdContratos() {
        return selectIdContratos;
    }

    public void setSelectIdContratos(long selectIdContratos) {
        this.selectIdContratos = selectIdContratos;
    }

    public boolean isEditContratos() {
        return editContratos;
    }

    public void setEditContratos(boolean editarTorres) {
        this.editContratos = editarTorres;
    }


    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public InmuebleEntity getInmuebleEntityObj() {
        return inmuebleEntityObj;
    }

    public void setInmuebleEntityObj(InmuebleEntity inmuebleEntityObj) {
        this.inmuebleEntityObj = inmuebleEntityObj;
    }

    public ServicioEntity getServicioEntityObj() {
        return servicioEntityObj;
    }

    public void setServicioEntityObj(ServicioEntity servicioEntityObj) {
        this.servicioEntityObj = servicioEntityObj;
    }

    public List<ServicioEntity> getServicioEntityList() {
        return servicioEntityList;
    }

    public void setServicioEntityList(List<ServicioEntity> servicioEntityList) {
        this.servicioEntityList = servicioEntityList;
    }

    public List<InmuebleEntity> getInmuebleEntityList() {
        return inmuebleEntityList;
    }

    public void setInmuebleEntityList(List<InmuebleEntity> inmuebleEntityList) {
        this.inmuebleEntityList = inmuebleEntityList;
    }

    public boolean isMostrarInmueble() {
        return mostrarInmueble;
    }

    public void setMostrarInmueble(boolean mostrarInmueble) {
        this.mostrarInmueble = mostrarInmueble;
    }

    public boolean isMostrarServicio() {
        return mostrarServicio;
    }

    public void setMostrarServicio(boolean mostrarServicio) {
        this.mostrarServicio = mostrarServicio;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getPertenece() {
        return pertenece;
    }

    public void setPertenece(String pertenece) {
        this.pertenece = pertenece;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public List<String> getCategoria() {
        return categoria;
    }

    public void setCategoria(List<String> categoria) {
        this.categoria = categoria;
    }

    public List<RadioBaseEntity> getRadioBaseEntityList() {
        return radioBaseEntityList;
    }

    public void setRadioBaseEntityList(List<RadioBaseEntity> radioBaseEntityList) {
        this.radioBaseEntityList = radioBaseEntityList;
    }

    public List<ProveedorEntity> getProveedorEntityList() {
        return proveedorEntityList;
    }

    public void setProveedorEntityList(List<ProveedorEntity> proveedorEntityList) {
        this.proveedorEntityList = proveedorEntityList;
    }

    public RadioBaseEntity getRadioBaseEntityObj() {
        return radioBaseEntityObj;
    }

    public void setRadioBaseEntityObj(RadioBaseEntity radioBaseEntityObj) {
        this.radioBaseEntityObj = radioBaseEntityObj;
    }

    public ProveedorEntity getProveedorEntityObj() {
        return proveedorEntityObj;
    }

    public void setProveedorEntityObj(ProveedorEntity proveedorEntityObj) {
        this.proveedorEntityObj = proveedorEntityObj;
    }

    public boolean isMostrarRadioBase() {
        return mostrarRadioBase;
    }

    public void setMostrarRadioBase(boolean mostrarRadioBase) {
        this.mostrarRadioBase = mostrarRadioBase;
    }

    public boolean isMostrarProveedor() {
        return mostrarProveedor;
    }

    public void setMostrarProveedor(boolean mostrarProveedor) {
        this.mostrarProveedor = mostrarProveedor;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public FileUploadEvent getFileUploadEvent() {
        return fileUploadEvent;
    }

    public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
        this.fileUploadEvent = fileUploadEvent;
    }

    public String getDirectoryFile() {
        return directoryFile;
    }

    public void setDirectoryFile(String directoryFile) {
        this.directoryFile = directoryFile;
    }

    public String getTamAdjuntos() {
        return tamAdjuntos;
    }

    public void setTamAdjuntos(String tamAdjuntos) {
        this.tamAdjuntos = tamAdjuntos;
    }

    public String getExtenAdjuntos() {
        return extenAdjuntos;
    }

    public void setExtenAdjuntos(String extenAdjuntos) {
        this.extenAdjuntos = extenAdjuntos;
    }

    public String getInvalidateMessage() {
        return invalidateMessage;
    }

    public void setInvalidateMessage(String invalidateMessage) {
        this.invalidateMessage = invalidateMessage;
    }

    public StreamedContent getDescarga() {
        return descarga;
    }

    public void setDescarga(StreamedContent descarga) {
        this.descarga = descarga;
    }

    public boolean isRenovacion() {
        return renovacion;
    }

    public void setRenovacion(boolean renovacion) {
        this.renovacion = renovacion;
    }

    public int getOptionContrato() {
        return optionContrato;
    }

    public void setOptionContrato(int optionContrato) {
        this.optionContrato = optionContrato;
    }

    public List<DocumentosEntity> getDocumentosEntityList() {
        return documentosEntityList;
    }

    public void setDocumentosEntityList(List<DocumentosEntity> documentosEntityList) {
        this.documentosEntityList = documentosEntityList;
    }

    public DocumentosEntity getDocumentosEntityObj() {
        return documentosEntityObj;
    }

    public void setDocumentosEntityObj(DocumentosEntity documentosEntityObj) {
        this.documentosEntityObj = documentosEntityObj;
    }


    public ContratosEntity getSelectcontratosEntityObj() {
        return selectcontratosEntityObj;
    }

    public void setSelectcontratosEntityObj(ContratosEntity selectcontratosEntityObj) {
        this.selectcontratosEntityObj = selectcontratosEntityObj;
    }

    public String getInmueble() {
        return INMUEBLE;
    }

    public String getServicio() {
        return SERVICIO;
    }

    public String getProveedor() {
        return PROVEEDOR;
    }

    public String getRadioBase() {
        return RADIO_BASE;
    }

    public String getSinAsignacion() {
        return SIN_ASIGNACION;
    }

    public List<DepartamentoEntity> getDepartamentoEntityList() {
        return departamentoEntityList;
    }

    public void setDepartamentoEntityList(List<DepartamentoEntity> departamentoEntityList) {
        this.departamentoEntityList = departamentoEntityList;
    }

    public DepartamentoEntity getDepartamentoEntityObj() {
        return departamentoEntityObj;
    }

    public void setDepartamentoEntityObj(DepartamentoEntity departamentoEntityObj) {
        this.departamentoEntityObj = departamentoEntityObj;
    }

    public InmuebleEntity getSelectInmuebleEntityObj() {
        return selectInmuebleEntityObj;
    }

    public void setSelectInmuebleEntityObj(InmuebleEntity selectInmuebleEntityObj) {
        this.selectInmuebleEntityObj = selectInmuebleEntityObj;
    }

    public ServicioEntity getSelectServicioEntityObj() {
        return selectServicioEntityObj;
    }

    public void setSelectServicioEntityObj(ServicioEntity selectServicioEntityObj) {
        this.selectServicioEntityObj = selectServicioEntityObj;
    }

    public RadioBaseEntity getSelectRadioBaseEntityObj() {
        return selectRadioBaseEntityObj;
    }

    public void setSelectRadioBaseEntityObj(RadioBaseEntity selectRadioBaseEntityObj) {
        this.selectRadioBaseEntityObj = selectRadioBaseEntityObj;
    }

    public ProveedorEntity getSelectProveedorEntityObj() {
        return selectProveedorEntityObj;
    }

    public void setSelectProveedorEntityObj(ProveedorEntity selectProveedorEntityObj) {
        this.selectProveedorEntityObj = selectProveedorEntityObj;
    }

    public List<CategoriaEntity> getCategoriaEntityList() {
        return categoriaEntityList;
    }

    public void setCategoriaEntityList(List<CategoriaEntity> categoriaEntityList) {
        this.categoriaEntityList = categoriaEntityList;
    }

    public Map<String, String> getEmailParameter() {
        return emailParameter;
    }

    public void setEmailParameter(Map<String, String> emailParameter) {
        this.emailParameter = emailParameter;
    }


}
