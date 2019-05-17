package myapps.cargas_masivas;

import myapps.abm.business.*;
import myapps.abm.model.*;
import myapps.cargainicial.ExcelUtil;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.*;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.DescriptorBitacora;
import myapps.user.model.MuUsuario;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class CargaPagosBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(CargaPagosBean.class);
    private static final String PATTERN_FORMAT = "dd/MM/yyyy";
    private static final String TEXT_DOC_PERTENECE = "HISTORIAL_CARGA_PAGO_MASIVO";

    @Inject
    private ServicioBL servicioBL;
    @Inject
    private PagoServicioBL pagoServicioBL;
    @Inject
    private ConfiguracionesBL configuracionesBL;
    @Inject
    private CamposBL camposBL;
    @Inject
    private DocumentosBL documentosBL;
    @Inject
    private HistPagoMasivoBL histPagoMasivoBL;
    @EJB
    private IApplicationContext iApplicationContext;
    @Inject
    private ControlerBitacora controlerBitacora;
    @Inject
    private SelectOptions selectOptionsBL;

    private List<InmuebleEntity> inmuebleEntityList = new ArrayList<>();
    private List<ServicioEntity> servicioEntityList = new ArrayList<>();
    private List<PagoServicioEntity> pagoServicioEntityList = new ArrayList<>();
    private List<PagoServicioEntity> pagoServNoExistDB = new ArrayList<>();
    private List<PagoServicioEntity> pagoServicioEntityListUnrealized = new ArrayList<>();
    private List<PagoServicioEntity> pagoServicioEntityListForProcess = new ArrayList<>();
    private List<PagoServicioEntity> pagoServicioEntityListAll = new ArrayList<>();
    private List<PagoServicioEntity> pagoServicioEntityListAllByHIstorial = new ArrayList<>();
    private List<CamposEntity> camposEntityList = new ArrayList<>();
    private List<ConfiguracionesEntity> configuracionesEntityList = new ArrayList<>();
    private List<HistorialPagosMasivosEntity> hPagosMasivosEntities = new ArrayList<>();
    private List<HistorialPagosMasivosEntity> hPagosMasivosEntitiesDocs = new ArrayList<>();

    private List<String[]> datos = new ArrayList<>();
    private List<String> campos = new ArrayList<>();
    private List<CamposEntity> parametros = new ArrayList<>();
    private transient List<MuUsuario> muUsuarios = new ArrayList<>();

    private FileUploadEvent fileUploadEvent;
    private InmuebleEntity inmuebleEntityObj;
    private ServicioEntity servicioEntityObj;
    private ProveedorEntity proveedorEntityObj;
    private CamposEntity camposEntityObj;
    private ConfiguracionesEntity configuracionesEntityObj;
    private ExcelUtil excelUtilObj;
    private PagoServicioEntity pagoServicioEntityObj;
    private HistorialPagosMasivosEntity hPagosMasivosEntityObj;
    private DocumentosEntity documentosEntityObj;
    private transient UploadedFile file;
    private boolean edit;
    private long selectIdUO;
    private Map<String, String> validateParameter;
    private String directoryFile;
    private String tamAdjuntos;
    private String extenAdjuntos;
    private String invalidateMessage;
    private boolean guardar = false;


    private boolean vertabla = true;
    private List<Boolean> list;
    private String userName;
    private String userRol;
    private String nombre;
    private boolean visibleDialog = false;
    private String userSeletFilter;
    private boolean disableCargarConfiguracion = false;
    private String[] indice;
    private boolean disableEliminar;
    private boolean disableValidar;
    private Date fechaNow;
    private transient StreamedContent descarga;
    private int nroProcesados;
    private int nroNoProcesados;
    private int nroNoExistentes;
    private String nombreConfig;
    private byte[] contentForCSV;
    private int nroCorrecto = 0;
    private int nroIncorrecto = 0;
    private static final String SERVICIO_MEDIDOR = "Servicio/Medidor";
    private static final String MONTO = "Monto";
    private static final String FECHA = "Fecha";
    private static final String PERIODO = "Periodo";
    private static final String FACTURA = "Factura";
    private static final String SELECCIONAR = "--Seleccionar--";

    @PostConstruct
    void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            userName = (String) request.getSession().getAttribute("TEMP$USER_NAME");
            setUserRol(request.getSession().getAttribute("TEMP$IDROL").toString());
            inmuebleEntityObj = new InmuebleEntity();
            servicioEntityObj = new ServicioEntity();
            proveedorEntityObj = new ProveedorEntity();
            pagoServicioEntityObj = new PagoServicioEntity();
            hPagosMasivosEntityObj = new HistorialPagosMasivosEntity();
            camposEntityObj = new CamposEntity();
            configuracionesEntityObj = new ConfiguracionesEntity();
            documentosEntityObj = new DocumentosEntity();

            excelUtilObj = new ExcelUtil();
            validateParameter = iApplicationContext.getMapValidateParameters();
            loadCargas();
            loadParameterMiscellaneuos();
            cargarConfiguraciones();
            pagoServicioEntityListAll = pagoServicioBL.listAll();
            configuracionesEntityList = configuracionesBL.listAll();
            setMuUsuarios(selectOptionsBL.cbxUsuarios());
            hPagosMasivosEntitiesDocs = selectOptionsBL.cbxListDocCargadosByUser();

            buscar();
            configuracionesEntityList = configuracionesBL.listAll();
            indice = new String[25];
            disableEliminar = false;
            disableValidar = true;
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }


    public void buscar() {
        hPagosMasivosEntities = histPagoMasivoBL.listAll(userSeletFilter, hPagosMasivosEntityObj,
                getFechaNow());
    }

    public void clean() {
        hPagosMasivosEntityObj = new HistorialPagosMasivosEntity();
        userSeletFilter = null;
        fechaNow = null;
        buscar();

    }

    private void loadParameterMiscellaneuos() {
        setDirectoryFile(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .DIRECTORIO_CARGA_MASIVA.toString()));
        setTamAdjuntos(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .TAMAÑO_ADJUNTOS.toString()));
        setExtenAdjuntos(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .EXTENSION_CARGA_MASIVA.toString()));
        setInvalidateMessage(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .EXTENSION_ADJUNTOS_MSG.toString()));

    }

    public void cargarConfiguraciones() {
        configuracionesEntityList = configuracionesBL.listAll();
    }


    private void loadCargas() {
        campos.add(SERVICIO_MEDIDOR);
        campos.add(MONTO);
        campos.add(FECHA);
        campos.add(PERIODO);
        campos.add(FACTURA);
    }

    public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            setFileUploadEvent(event);
            String name = fileUploadEvent.getFile().getFileName();
            int lastIndexOf = name.lastIndexOf('.');
            String extension = name.substring(lastIndexOf);
            SysMessage.handleFileUpload(event);

            ImportCsv csv = new ImportCsv();
            if (extension.equals(".xls")) {
                datos = Importar.leerExcel(fileUploadEvent.getFile().getContents());
                excelUtilObj = Importar.obtenerDatos(fileUploadEvent.getFile().getContents());

            } else if (extension.equals(".csv")) {
                setContentForCSV(fileUploadEvent.getFile().getContents());
                datos = csv.leerCsv(fileUploadEvent.getFile().getInputstream());
                excelUtilObj = csv.obtenerDatos(datos);
            } else {
                datos = ImportXlsx.leerrXlsx(fileUploadEvent.getFile().getContents());
                excelUtilObj = ImportXlsx.obtenerDatos(fileUploadEvent.getFile().getContents());
            }
            observation(datos);
            list = Importar.operacion(excelUtilObj.getColumnas());
            hPagosMasivosEntityObj.setNombreArchivo(name);
            hPagosMasivosEntityObj.setExt(extension);


        } catch (Exception e) {
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public void llenarListaConfig(int i) {
        try {
            int cont = 0;
            CamposEntity nuevocampo = new CamposEntity();
            nuevocampo.setNumero(Long.valueOf(i));
            nuevocampo.setLetra(Importar.letras(i));
            nuevocampo.setNombre(indice[i]);
            nuevocampo.setFecha(new Date());
            if (parametros.isEmpty()) {
                nuevocampo.setNumero(Long.valueOf(i));
                if (!nuevocampo.getNombre().equals(SELECCIONAR)) {
                    parametros.add(nuevocampo);
                }
            } else {
                for (CamposEntity camposEntity : parametros) {

                    if (nuevocampo.getNombre().equals(camposEntity.getNombre())) {
                        camposEntity.setLetra(nuevocampo.getLetra());
                        parametros.set(cont, camposEntity);
                        eliminarCampo(nuevocampo, parametros);
                        return;
                    }
                    if (nuevocampo.getLetra().equals(camposEntity.getLetra())) {
                        camposEntity.setNombre(nuevocampo.getNombre());
                        camposEntity.setLetra(nuevocampo.getLetra());
                        parametros.set(cont, camposEntity);
                        eliminarCampo(nuevocampo, parametros);
                        return;
                    }
                    cont++;
                }
                nuevocampo.setNumero(Long.valueOf(i));
                if (!nuevocampo.getNombre().equals(SELECCIONAR) && parametros.size() < 6) {
                    parametros.add(nuevocampo);
                } else {
                    SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Limite de campos", null);
                }
            }

        } catch (Exception e) {
            log.info("Error " + e.getMessage());
        }
    }


    public void eliminarCampo(CamposEntity camposEntity, List<CamposEntity> parametros2) {
        for (CamposEntity campo : parametros2) {
            if (camposEntity.getNombre().equals(campo.getNombre()) && camposEntity.getLetra() != campo.getLetra()) {
                parametros2.remove(campo);
                parametros = parametros2;
                return;
            }
        }

    }

    public boolean botonGuardarConfiguracion() {
        boolean guardar2 = validarLista();

        if (configuracionesEntityObj.getIdConfiguraciones() > 0) {
            guardar2 = true;
        }
        return guardar2;
    }

    private boolean validarLista() {
        guardar = true;
        int contador = 0;
        if (parametros.size() > 3) {
            for (CamposEntity camposEntity : parametros) {
                switch (camposEntity.getNombre()) {
                    case SERVICIO_MEDIDOR:
                        contador++;
                        break;
                    case MONTO:
                        contador++;
                        break;
                    case PERIODO:
                        contador++;
                        break;
                    case FECHA:
                        contador++;
                        break;
                    default:
                        log.error("Campo no definido");
                        break;
                }
            }
        }
        if (contador == 4) {
            guardar = false;
            disableValidar = false;
        }

        return guardar;
    }

    public void validarConfiguracion() {
        guardar = false;
        boolean listarepetida;
        String name = null;

        ExcelUtil excelUtil = new ExcelUtil();
        for (ConfiguracionesEntity configuracionesEntity : configuracionesEntityList) {

            List<CamposEntity> lista = camposBL.listAllidConfiguraciones(configuracionesEntity.getIdConfiguraciones());
            listarepetida = excelUtil.equalLists(parametros, lista);
            if (listarepetida) {
                configuracionesEntityObj.setIdConfiguraciones(configuracionesEntity.getIdConfiguraciones());
                name = configuracionesEntity.getNombre();
                guardar = true;
            }
        }
        if (guardar) {
            SysMessage.info("La Configuracion ya existe: " + name, null);
        }
        validarLista();
    }


    public void actualizarDatos() {
        datos.remove(0);
        if (!validarLista()) {
            pagoServicioEntityList = new ArrayList<>();
            Importar a = new Importar();
            for (String[] lista : datos) {
                boolean insertar = false;
                PagoServicioEntity pagoServicioEntity = new PagoServicioEntity();
                String codNoExistente = "";
                for (CamposEntity camposEntity : parametros) {
                    switch (camposEntity.getNombre()) {
                        case MONTO:
                            String monto = lista[(camposEntity.getNumero().intValue())].replace(".", "")
                                    .replace(",", ".");
                            pagoServicioEntity.setMonto(new BigDecimal(monto));
                            break;
                        case FECHA:
                            pagoServicioEntity.setFecha(UtilDate.stringToDate(lista[camposEntity
                                    .getNumero().intValue()], PATTERN_FORMAT));
                            break;
                        case PERIODO:
                            pagoServicioEntity.setPeriodo(UtilDate.stringToDate(lista[camposEntity.getNumero
                                    ().intValue()], PATTERN_FORMAT));
                            if (pagoServicioEntity.getPeriodo() == null) {
                                pagoServicioEntity.setPeriodo(a.stringDate(lista[camposEntity.getNumero
                                        ().intValue()]));
                            }
                            break;
                        case SERVICIO_MEDIDOR:
                            ServicioEntity servicioEntity = servicioBL.buscar(lista[camposEntity.getNumero().intValue()]);
                            if (servicioEntity.getCodServMed() != null) {
                                pagoServicioEntity.setServicioByIdServicio(servicioEntity);
                                insertar = true;
                            } else {
                                codNoExistente = lista[camposEntity.getNumero().intValue()];
                            }
                            break;
                        case FACTURA:
                            pagoServicioEntity.setFactura(lista[camposEntity.getNumero().intValue()]);
                            break;
                        default:
                            log.info("Error al leer lista");
                            break;
                    }
                }

                if (insertar) {
                    pagoServicioEntityList.add(pagoServicioEntity);
                } else {
                    ServicioEntity servicioEntity = new ServicioEntity();
                    servicioEntity.setCodServMed(codNoExistente);
                    pagoServicioEntity.setServicioByIdServicio(servicioEntity);
                    pagoServicioEntity.setObs("Cod. " + SERVICIO_MEDIDOR + " no existe");
                    pagoServNoExistDB.add(pagoServicioEntity);
                }

            }

            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Importación Completa", null);
        } else {

            SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Existen campos sin Seleccionar", null);
        }

    }

    public void insertarDatos() {
        cargarHistorial();
        if (hPagosMasivosEntityObj != null) {
            histPagoMasivoBL.save(hPagosMasivosEntityObj);
            controlerBitacora.insert(DescriptorBitacora.HISTORIAL_PAGO_SERVICIO_MAS,
                    String.valueOf(hPagosMasivosEntityObj.getId()), hPagosMasivosEntityObj.getNombreArchivo());
        }
        if (hPagosMasivosEntityObj != null) {

            for (int i = 0; i < pagoServicioEntityListForProcess.size(); i++) {
                PagoServicioEntity entity = pagoServicioEntityListForProcess.get(i);
                entity.sethPagosMasivosByIdHPagoM(hPagosMasivosEntityObj);
                pagoServicioEntityListForProcess.set(i, entity);
            }

            for (PagoServicioEntity pagar : pagoServicioEntityListForProcess) {
                pagoServicioBL.guardar(pagar);
            }
            documentosEntityObj.setNombre(getFileUploadEvent().getFile().getFileName());
            documentosEntityObj.setDocAdjunto(getContentForCSV());
            documentosEntityObj.setUbicacion(getDirectoryFile());
            documentosEntityObj.setPertenece(TEXT_DOC_PERTENECE);
            documentosEntityObj.setIdPertenece(hPagosMasivosEntityObj.getId());
            documentosEntityObj.setPrefijo(UtilFile.generatePrefijo());
            documentosEntityObj.setEstado(true);
            documentosBL.save(documentosEntityObj);
        }
        contadorPagos();
        SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Fin de Carga Masiva", null);
    }

    private void cargarHistorial() {
        long contProcess = 0;
        long contNoProcess = 0;
        hPagosMasivosEntityObj.setFechaIni(UtilDate.dateToTimeStamp(new Date()));
        hPagosMasivosEntityObj.setConfiguracionesByConfiguracionId(configuracionesEntityObj);
        setNombreConfig(configuracionesBL.getById(hPagosMasivosEntityObj.getConfiguracionesByConfiguracionId()
                .getIdConfiguraciones()).getNombre());
        for (PagoServicioEntity pagoServicioEntity : pagoServicioEntityList) {
            try {

                if (this.validatePago(pagoServicioEntity)) {
                    pagoServicioEntity.setObs("Ya existe");
                    pagoServicioEntityListUnrealized.add(pagoServicioEntity);
                    contNoProcess++;
                } else {
                    pagoServicioEntity.setUsuario(userName);
                    pagoServicioEntityListForProcess.add(pagoServicioEntity);
                    contProcess++;
                }
            } catch (Exception e) {
                log.info("Error al Insertar Carga Masiva");
            }
        }
        hPagosMasivosEntityObj.setNoprocesados(contNoProcess);
        hPagosMasivosEntityObj.setProcesados(contProcess);
        Date fecha2 = new Date();
        hPagosMasivosEntityObj.setFechaFin(UtilDate.dateToTimeStamp(fecha2));

        if (pagoServicioEntityListForProcess.isEmpty()) {
            sethPagosMasivosEntityObj(null);
        }
    }

    private boolean validatePago(PagoServicioEntity entity) {
        log.log(Level.INFO, "VALIDATE_PAGO");
        boolean exist = false;
        String dateString = UtilDate.dateToString(entity.getPeriodo(), PATTERN_FORMAT);

        try {
            for (PagoServicioEntity pagoServicioEntity : pagoServicioEntityListAll) {
                String datePago = UtilDate.dateToString(pagoServicioEntity.getPeriodo(),
                        PATTERN_FORMAT);
                if (datePago.equals(dateString)) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            log.log(Level.ERROR, "VALIDATE_PAGO" + "|" + e.getMessage(), e);
        }
        return exist;
    }


    public void guardarConfiguracion() {
        if (getNombre() == null) {
            SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Campo invalido", null);
            return;
        }
        configuracionesEntityObj.setNombre(getNombre());
        configuracionesEntityObj.setTipo("PAGO MASIVO");
        configuracionesEntityObj.setFechaCreacion(new Date());

        boolean nombrerepetido = false;


        for (ConfiguracionesEntity configuracionesEntity : configuracionesEntityList) {
            if (configuracionesEntityObj.getNombre().equals(configuracionesEntity.getNombre())) {
                nombrerepetido = true;
            }
        }
        if (!nombrerepetido) {

            configuracionesBL.save(configuracionesEntityObj);
            for (CamposEntity camposEntity : parametros) {
                camposEntity.setConfiguracionesByIdConfiguraciones(configuracionesEntityObj);
                camposBL.save(camposEntity);
            }
            configuracionesEntityObj = new ConfiguracionesEntity();
            cargarConfiguraciones();
            configuracionesEntityObj.setIdConfiguraciones(configuracionesEntityObj.getIdConfiguraciones());
            SysMessage.info("Configuracion Guardada Correctamente", null);
            PrimeFaces.current().executeScript("PF('carDialog').hide();");
        } else {
            SysMessage.warn("Este nombre ya esta siendo utilizado", null);
            PrimeFaces.current().executeScript("PF('carDialog').jq.effect(\"shake\", {times: 5}, 100);");
        }
    }

    public void nuevaConfiguracion() {
        parametros.clear();
        disableCargarConfiguracion = false;
        for (int i = 0; i <= 24; i++) {
            indice[i] = SELECCIONAR;
            disableValidar = true;
        }
        disableEliminar = true;
        configuracionesEntityObj.setIdConfiguraciones(-1);
    }

    public void cargarParametros() {
        if (configuracionesEntityObj.getIdConfiguraciones() < 0) {
            parametros.clear();
            disableCargarConfiguracion = false;
            for (int i = 0; i <= 24; i++) {
                indice[i] = SELECCIONAR;
                disableValidar = true;
            }
            disableEliminar = true;
        } else {
            parametros = camposBL.listAllidConfiguraciones(configuracionesEntityObj.getIdConfiguraciones());

            for (CamposEntity camposEntity : parametros) {
                int i = camposEntity.getNumero().intValue();
                indice[i] = camposEntity.getNombre();
            }
            disableValidar = false;
            disableEliminar = true;
            disableCargarConfiguracion = true;
        }
    }


    public String viewCarga(HistorialPagosMasivosEntity entity) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("hpagosObj", entity);
        return "/view/detalleHistPagoMasivo.xhtml";
    }

    public void eliminarColumna(CamposEntity campos) {
        parametros.remove(campos);
        for (CamposEntity camposEntity : parametros) {
            int i = camposEntity.getNumero().intValue();
            indice[i] = camposEntity.getNombre();
        }
    }

    /**
     * Descarga de archivos adjuntos pagos masivos
     *
     * @param hPagosMasivosEntity
     */
    public void donwloadAdjunt(HistorialPagosMasivosEntity hPagosMasivosEntity) {
        try {

            setDocumentosEntityObj(documentosBL.getByIdPertenece(hPagosMasivosEntity.getId(),
                    TEXT_DOC_PERTENECE));

            DocumentosEntity descargaDoc = documentosBL.donwloadDoc(getDocumentosEntityObj());

            InputStream stream = new ByteArrayInputStream(descargaDoc.getDocAdjunto());
            descarga = new DefaultStreamedContent(stream, "application/" + descargaDoc.getExtension(),
                    descargaDoc.getNombre());
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()),
                    "Directorio no encontrado: " + e.getMessage());
        }
    }

    public void contadorPagos() {
        setNroProcesados(pagoServicioEntityListForProcess.size());
        setNroNoProcesados(pagoServicioEntityListUnrealized.size());
        setNroNoExistentes(pagoServNoExistDB.size());

    }

    public boolean setColor(String[] datos) {
        for (String sdatos : datos) {
            if (sdatos.equals("")) {
                return true;
            }
        }
        return false;
    }

    private void observation(List<String[]> datos) {

        for (String[] strings : datos) {
            boolean isValid = true;
            for (String dato : strings) {
                isValid = !dato.equals("") && isValid;
            }
            if (isValid) {
                nroCorrecto++;
            } else {
                nroIncorrecto++;
            }
        }

    }

    public long getSelectIdUO() {
        return selectIdUO;
    }

    public void setSelectIdUO(long selectIdUO) {
        this.selectIdUO = selectIdUO;
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
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

    public boolean isVertabla() {
        return vertabla;
    }

    public void setVertabla(boolean vertabla) {
        this.vertabla = vertabla;
    }

    public List<Boolean> getList() {
        return list;
    }

    public void setList(List<Boolean> list) {
        this.list = list;
    }

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


    public List<ServicioEntity> getServicioEntityList() {
        return servicioEntityList;
    }

    public List<PagoServicioEntity> getPagoServicioEntityListUnrealized() {
        return pagoServicioEntityListUnrealized;
    }

    public void setPagoServicioEntityListUnrealized(List<PagoServicioEntity> pagoServicioEntityListUnrealized) {
        this.pagoServicioEntityListUnrealized = pagoServicioEntityListUnrealized;
    }

    public List<PagoServicioEntity> getPagoServicioEntityListForProcess() {
        return pagoServicioEntityListForProcess;
    }

    public void setPagoServicioEntityListForProcess(List<PagoServicioEntity> pagoServicioEntityListForProcess) {
        this.pagoServicioEntityListForProcess = pagoServicioEntityListForProcess;
    }

    public List<PagoServicioEntity> getPagoServicioEntityListAll() {
        return pagoServicioEntityListAll;
    }

    public void setPagoServicioEntityListAll(List<PagoServicioEntity> pagoServicioEntityListAll) {
        this.pagoServicioEntityListAll = pagoServicioEntityListAll;
    }

    public List<PagoServicioEntity> getPagoServicioEntityListAllByHIstorial() {
        return pagoServicioEntityListAllByHIstorial;
    }

    public void setPagoServicioEntityListAllByHIstorial(List<PagoServicioEntity> pagoServicioEntityListAllByHIstorial) {
        this.pagoServicioEntityListAllByHIstorial = pagoServicioEntityListAllByHIstorial;
    }

    public List<HistorialPagosMasivosEntity> gethPagosMasivosEntities() {
        return hPagosMasivosEntities;
    }

    public void sethPagosMasivosEntities(List<HistorialPagosMasivosEntity> hPagosMasivosEntities) {
        this.hPagosMasivosEntities = hPagosMasivosEntities;
    }

    public List<HistorialPagosMasivosEntity> gethPagosMasivosEntitiesDocs() {
        return hPagosMasivosEntitiesDocs;
    }

    public void sethPagosMasivosEntitiesDocs(List<HistorialPagosMasivosEntity> hPagosMasivosEntitiesDocs) {
        this.hPagosMasivosEntitiesDocs = hPagosMasivosEntitiesDocs;
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

    public ProveedorEntity getProveedorEntityObj() {
        return proveedorEntityObj;
    }

    public void setProveedorEntityObj(ProveedorEntity proveedorEntityObj) {
        this.proveedorEntityObj = proveedorEntityObj;
    }


    public ExcelUtil getExcelUtilObj() {
        return excelUtilObj;
    }

    public void setExcelUtilObj(ExcelUtil excelUtilObj) {
        this.excelUtilObj = excelUtilObj;
    }

    public List<PagoServicioEntity> getPagoServicioEntityList() {
        return pagoServicioEntityList;
    }

    public void setPagoServicioEntityList(List<PagoServicioEntity> pagoServicioEntityList) {
        this.pagoServicioEntityList = pagoServicioEntityList;
    }

    public List<PagoServicioEntity> getPagoServNoExistDB() {
        return pagoServNoExistDB;
    }

    public void setPagoServNoExistDB(List<PagoServicioEntity> pagoServNoExistDB) {
        this.pagoServNoExistDB = pagoServNoExistDB;
    }

    public PagoServicioEntity getPagoServicioEntityObj() {
        return pagoServicioEntityObj;
    }

    public void setPagoServicioEntityObj(PagoServicioEntity pagoServicioEntityObj) {
        this.pagoServicioEntityObj = pagoServicioEntityObj;
    }

    public List<String[]> getDatos() {
        return datos;
    }

    public void setDatos(List<String[]> datos) {
        this.datos = datos;
    }

    public List<String> getCampos() {
        return campos;
    }

    public void setCampos(List<String> campos) {
        this.campos = campos;
    }


    public List<ConfiguracionesEntity> getConfiguracionesEntityList() {
        return configuracionesEntityList;
    }

    public void setConfiguracionesEntityList(List<ConfiguracionesEntity> configuracionesEntityList) {
        this.configuracionesEntityList = configuracionesEntityList;
    }

    public List<MuUsuario> getMuUsuarios() {
        return muUsuarios;
    }

    public void setMuUsuarios(List<MuUsuario> muUsuarios) {
        this.muUsuarios = muUsuarios;
    }

    public ConfiguracionesEntity getConfiguracionesEntityObj() {
        return configuracionesEntityObj;
    }

    public void setConfiguracionesEntityObj(ConfiguracionesEntity configuracionesEntityObj) {
        this.configuracionesEntityObj = configuracionesEntityObj;
    }

    public HistorialPagosMasivosEntity gethPagosMasivosEntityObj() {
        return hPagosMasivosEntityObj;
    }

    public void sethPagosMasivosEntityObj(HistorialPagosMasivosEntity hPagosMasivosEntityObj) {
        this.hPagosMasivosEntityObj = hPagosMasivosEntityObj;
    }

    public List<CamposEntity> getParametros() {
        return parametros;
    }

    public void setParametros(List<CamposEntity> parametros) {
        this.parametros = parametros;
    }

    public List<CamposEntity> getCamposEntityList() {
        return camposEntityList;
    }

    public void setCamposEntityList(List<CamposEntity> camposEntityList) {
        this.camposEntityList = camposEntityList;
    }

    public CamposEntity getCamposEntityObj() {
        return camposEntityObj;
    }

    public void setCamposEntityObj(CamposEntity camposEntityObj) {
        this.camposEntityObj = camposEntityObj;
    }

    public DocumentosEntity getDocumentosEntityObj() {
        return documentosEntityObj;
    }

    public void setDocumentosEntityObj(DocumentosEntity documentosEntityObj) {
        this.documentosEntityObj = documentosEntityObj;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isVisibleDialog() {
        return visibleDialog;
    }

    public void setVisibleDialog(boolean visibleDialog) {
        this.visibleDialog = visibleDialog;
    }

    public String getUserSeletFilter() {
        return userSeletFilter;
    }

    public void setUserSeletFilter(String userSeletFilter) {
        this.userSeletFilter = userSeletFilter;
    }

    public String getUserRol() {
        return userRol;
    }

    public void setUserRol(String userRol) {
        this.userRol = userRol;
    }

    public boolean isDisableCargarConfiguracion() {
        return disableCargarConfiguracion;
    }

    public void setDisableCargarConfiguracion(boolean disableCargarConfiguracion) {
        this.disableCargarConfiguracion = disableCargarConfiguracion;
    }

    public String[] getIndice() {
        return indice;
    }

    public void setIndice(String[] indice) {
        this.indice = indice;
    }

    public boolean isDisableEliminar() {
        return disableEliminar;
    }

    public void setDisableEliminar(boolean disableEliminar) {
        this.disableEliminar = disableEliminar;
    }

    public Date getFechaNow() {
        return fechaNow;
    }

    public void setFechaNow(Date fechaNow) {
        this.fechaNow = fechaNow;
    }

    public boolean isDisableValidar() {
        return disableValidar;
    }

    public void setDisableValidar(boolean disableValidar) {
        this.disableValidar = disableValidar;
    }

    public StreamedContent getDescarga() {
        return descarga;
    }

    public void setDescarga(StreamedContent descarga) {
        this.descarga = descarga;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getNroProcesados() {
        return nroProcesados;
    }

    public void setNroProcesados(int nroProcesados) {
        this.nroProcesados = nroProcesados;
    }

    public int getNroNoProcesados() {
        return nroNoProcesados;
    }

    public void setNroNoProcesados(int nroNoProcesados) {
        this.nroNoProcesados = nroNoProcesados;
    }

    public int getNroNoExistentes() {
        return nroNoExistentes;
    }

    public void setNroNoExistentes(int nroNoExistentes) {
        this.nroNoExistentes = nroNoExistentes;
    }

    public String getNombreConfig() {
        return nombreConfig;
    }

    public void setNombreConfig(String nombreConfig) {
        this.nombreConfig = nombreConfig;
    }

    public byte[] getContentForCSV() {
        return contentForCSV;
    }

    public void setContentForCSV(byte[] contentForCSV) {
        this.contentForCSV = contentForCSV;
    }

    public int getNroCorrecto() {
        return nroCorrecto;
    }

    public void setNroCorrecto(int nroCorrecto) {
        this.nroCorrecto = nroCorrecto;
    }

    public int getNroIncorrecto() {
        return nroIncorrecto;
    }

    public void setNroIncorrecto(int nroIncorrecto) {
        this.nroIncorrecto = nroIncorrecto;
    }
}

