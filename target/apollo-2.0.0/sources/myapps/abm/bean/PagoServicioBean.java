package myapps.abm.bean;

import myapps.abm.business.DocumentosBL;
import myapps.abm.business.PagoServicioBL;
import myapps.abm.business.SelectOptions;
import myapps.abm.business.ServicioBL;
import myapps.abm.model.DocumentosEntity;
import myapps.abm.model.PagoServicioEntity;
import myapps.abm.model.ServicioEntity;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@ManagedBean
@ViewScoped
public class PagoServicioBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(PagoServicioBean.class);

    @Inject
    private PagoServicioBL pagoServicioBL;
    @Inject
    private ServicioBL servicioBL;
    @Inject
    private DocumentosBL documentosBL;
    @Inject
    private SelectOptions selectOptions;
    @Inject
    private ControlerBitacora controlerBitacora;

    @EJB
    private IApplicationContext iApplicationContext;

    private List<PagoServicioEntity> pagoServicioEntityList = new ArrayList<>();
    private List<ServicioEntity> servicioEntityList = new ArrayList<>();
    private List<DocumentosEntity> documentosEntityList = new ArrayList<>();

    private PagoServicioEntity pagoServicioEntityObj;
    private ServicioEntity servicioEntityObj;


    private boolean edit;
    private Map<String, String> validateParameter;
    private FileUploadEvent fileUploadEvent;
    private String directoryFile;
    private String tamAdjuntos;
    private String extenAdjuntos;
    private String invalidateMessage;
    private transient StreamedContent descarga;
    private long editPagoServicio;

    @PostConstruct
    void init() {
        try {
            servicioEntityObj = new ServicioEntity();
            pagoServicioEntityObj = new PagoServicioEntity();
            loadParameterMiscellaneuos();
            loadPSs();
            validateParameter = iApplicationContext.getMapValidateParameters();
            cargarObjetos();
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void cargarObjetos() {
        pagoServicioEntityObj.setFecha(new Date());
        pagoServicioEntityObj.setPeriodo(new Date());
    }

    public void newPS() {
        edit = false;
        pagoServicioEntityObj = new PagoServicioEntity();
        servicioEntityObj = new ServicioEntity();
        setEdit(false);
        servicioEntityObj = new ServicioEntity();
        documentosEntityList = new ArrayList<>();
        cargarObjetos();
    }

    public void actionPS() {
        try {
            if (validatePago(pagoServicioEntityObj)) {
                SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Este Periodo ya fue cancelado", null);
                return;
            }
            if (isEdit() && pagoServicioEntityObj.getIdPagoServicio() > 0) {
                pagoServicioEntityObj.setServicioByIdServicio(servicioEntityObj);
                pagoServicioBL.update(pagoServicioEntityObj, documentosEntityList);
                controlerBitacora.update(DescriptorBitacora.PAGO_SERVICIO, String.valueOf
                        (getPagoServicioEntityObj().getIdPagoServicio()), getPagoServicioEntityObj()
                        .getMonto().toString());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
                loadPSs();
                newPS();

            } else {
                pagoServicioEntityObj.setServicioByIdServicio(servicioEntityObj);
                if (validatePago(pagoServicioEntityObj)) {
                    SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Este Periodo ya fue cancelado", null);
                } else {
                    pagoServicioBL.save(pagoServicioEntityObj, documentosEntityList);
                    controlerBitacora.insert(DescriptorBitacora.PAGO_SERVICIO, String.valueOf(getPagoServicioEntityObj()
                            .getIdPagoServicio()), getPagoServicioEntityObj().getMonto().toString());
                    SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
                    loadPSs();
                    newPS();
                }
            }
        } catch (Exception e) {
            log.log(Level.ERROR, e.getMessage());
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
        }
    }

    private void loadPSs() {
        servicioEntityList = selectOptions.cbxOnlyServicio();
        pagoServicioEntityList = pagoServicioBL.listAll();
    }

    public void editPS() {
        setEditPagoServicio(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("idpago")));
        setPagoServicioEntityObj(pagoServicioBL.getById(getEditPagoServicio()));
        setServicioEntityObj(getPagoServicioEntityObj().getServicioByIdServicio());
        documentosEntityList = documentosBL.listAll(getPagoServicioEntityObj().getIdPagoServicio());
        setEdit(true);
    }

    public void deletePS() {
        try {
            setEditPagoServicio(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("idpago")));
            setPagoServicioEntityObj(pagoServicioBL.getById(getEditPagoServicio()));
            pagoServicioBL.delete(getPagoServicioEntityObj(), documentosEntityList);
            controlerBitacora.delete(DescriptorBitacora.PAGO_SERVICIO, String.valueOf
                    (getPagoServicioEntityObj()
                            .getIdPagoServicio()), getPagoServicioEntityObj().getMonto().toString());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newPS();
            loadPSs();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());

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
                DocumentosEntity documentosEntityObj = new DocumentosEntity();
                documentosEntityObj.setUbicacion(getDirectoryFile());
                documentosEntityObj.setNombre(getFileUploadEvent().getFile().getFileName());
                documentosEntityObj.setPrefijo(UtilFile.generatePrefijo());
                documentosEntityObj.setPertenece("PAGOS");
                documentosEntityObj.setEstado(true);
                documentosEntityObj.setDocAdjunto(fileUploadEvent.getFile().getContents());
                documentosEntityList.add(documentosEntityObj);
                SysMessage.handleFileUpload(event);
            } else {
                SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Archivo Duplicado", "");
            }
        } catch (Exception e) {
            log.log(Level.ERROR, e.getMessage());
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


    public void loadDocAdjuntos(PagoServicioEntity pago) {

        setDocumentosEntityList(new ArrayList<>());
        setDocumentosEntityList(documentosBL.listAll(pago.getIdPagoServicio()));

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

    private boolean validatePago(PagoServicioEntity entity) {
        log.log(Level.INFO, "VALIDATE_PAGO");
        boolean exist = false;
        String dateString = UtilDate.dateToString(entity.getPeriodo(), "MM/yyyy");
        try {
            for (PagoServicioEntity pagoServicioEntity : pagoServicioEntityList) {
                if (entity.getIdPagoServicio() != pagoServicioEntity.getIdPagoServicio() && entity.getServicioByIdServicio().getIdServicio() == pagoServicioEntity.getServicioByIdServicio().getIdServicio()) {
                    String datePago = UtilDate.dateToString(pagoServicioEntity.getPeriodo(),
                            "MM/yyyy");
                    if (datePago.equals(dateString)) {
                        exist = true;
                    }
                }
            }
        } catch (Exception e) {
            log.log(Level.ERROR, "VALIDATE_PAGO" + "|" + e.getMessage(), e);
        }
        return exist;
    }

    public List<PagoServicioEntity> getPagoServicioEntityList() {
        return pagoServicioEntityList;
    }

    public void setPagoServicioEntityList(List<PagoServicioEntity> pagoServicioEntityList) {
        this.pagoServicioEntityList = pagoServicioEntityList;
    }


    public PagoServicioEntity getPagoServicioEntityObj() {
        return pagoServicioEntityObj;
    }

    public void setPagoServicioEntityObj(PagoServicioEntity pagoServicioEntityObj) {
        this.pagoServicioEntityObj = pagoServicioEntityObj;
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

    private FileUploadEvent getFileUploadEvent() {
        return fileUploadEvent;
    }

    private void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
        this.fileUploadEvent = fileUploadEvent;
    }

    public List<DocumentosEntity> getDocumentosEntityList() {
        return documentosEntityList;
    }

    public void setDocumentosEntityList(List<DocumentosEntity> documentosEntityList) {
        this.documentosEntityList = documentosEntityList;
    }

    private String getDirectoryFile() {
        return directoryFile;
    }

    private void setDirectoryFile(String directoryFile) {
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

    public long getEditPagoServicio() {
        return editPagoServicio;
    }

    public void setEditPagoServicio(long editPagoServicio) {
        this.editPagoServicio = editPagoServicio;
    }
}
