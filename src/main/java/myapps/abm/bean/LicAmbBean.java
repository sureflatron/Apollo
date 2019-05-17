package myapps.abm.bean;

import myapps.abm.business.DocumentosBL;
import myapps.abm.business.LicAmbBL;
import myapps.abm.business.SelectOptions;
import myapps.abm.model.*;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
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
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static myapps.servicio_basico.util.UtilDate.*;
import static myapps.servicio_basico.util.UtilDate.dateToSql;

@ViewScoped
@ManagedBean
public class LicAmbBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(LicAmbBean.class);

    @Inject
    private LicAmbBL licAmbBL;
    @Inject
    private DocumentosBL documentosBL;
    @Inject
    private SelectOptions selectOptions;
    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext iApplicationContext;

    private List<LicAmbEntity> licAmbEntityList = new ArrayList<>();
    private List<ProveedorEntity> proveedorEntityList = new ArrayList<>();
    private List<DepartamentoEntity> departamentoEntityList = new ArrayList<>();
    private List<DocumentosEntity> documentosEntityList = new ArrayList<>();
    private List<InmuebleEntity> inmuebleEntityList = new ArrayList<>();

    private LicAmbEntity licAmbEntityObj;
    private DocumentosEntity documentosEntityObj;
    private ProveedorEntity proveedorEntityObj;
    private DepartamentoEntity departamentoEntityObj;
    private InmuebleEntity inmuebleEntityObj;
    private LicAmbEntity selectLicAmbBeanObj;

    private FileUploadEvent fileUploadEvent;

    private boolean editLicAmb;
    private boolean getPrefijo = true;
    private String setPrefijoDoc;
    private Date fechaExpira;
    private String directoryFile;
    private String tamAdjuntos;
    private String extenAdjuntos;
    private String invalidateMessage;

    private Map<String, String> validateParameter;
    private Map<String, String> emailParameter;

    private transient StreamedContent descarga;

    @PostConstruct
    void init() {
        initObj();
        selectLicAmbBeanObj = new LicAmbEntity();
        proveedorEntityList = selectOptions.cbxProveedores();
        departamentoEntityList = selectOptions.cbxDepartamento();
        inmuebleEntityList = selectOptions.cbxImnueble();
        loadParameterMiscellaneuos();
        loadLicAmb();
        validateParameter = iApplicationContext.getMapValidateParameters();
        emailParameter = iApplicationContext.getMapEmailParameters();
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

    private void initObj() {
        licAmbEntityObj = new LicAmbEntity();
        proveedorEntityObj = new ProveedorEntity();
        departamentoEntityObj = new DepartamentoEntity();
        inmuebleEntityObj = new InmuebleEntity();
        documentosEntityList = new ArrayList<>();
        fechaExpira = new Date();
    }

    public void newLicAmb() {
        initObj();
        setEditLicAmb(false);
        loadLicAmb();
    }

    public void actionLicAmb() {
        try {
            if (isEditLicAmb() && getLicAmbEntityObj().getIdLicAmb() > 0) {
                licAmbEntityObj.setProveedorByIdProveedor(getProveedorEntityObj());
                licAmbEntityObj.setDepartamentoByIdDepartamento(getDepartamentoEntityObj());
                licAmbEntityObj.setInmuebleByIdInmueble(getInmuebleEntityObj());
                licAmbEntityObj.setExpira(dateToSql(fechaExpira));
                licAmbBL.update(licAmbEntityObj, documentosEntityList);
                controlerBitacora.update(DescriptorBitacora.LICENCIA_AMBIENTAL, String.valueOf
                        (getLicAmbEntityObj().getIdLicAmb()), String.valueOf(getLicAmbEntityObj()
                        .getNro()));
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                licAmbEntityObj.setProveedorByIdProveedor(getProveedorEntityObj());
                licAmbEntityObj.setDepartamentoByIdDepartamento(getDepartamentoEntityObj());
                licAmbEntityObj.setInmuebleByIdInmueble(getInmuebleEntityObj());
                licAmbEntityObj.setExpira(dateToSql(fechaExpira));
                licAmbBL.save(licAmbEntityObj, documentosEntityList);
                controlerBitacora.insert(DescriptorBitacora.LICENCIA_AMBIENTAL, String.valueOf(getLicAmbEntityObj()
                        .getIdLicAmb()), String.valueOf(getLicAmbEntityObj().getNro()));
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
            loadLicAmb();
            newLicAmb();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadLicAmb() {
        licAmbEntityList = licAmbBL.listAll();
    }

    public void editarLicAmb(LicAmbEntity licObj) {
        setLicAmbEntityObj(licObj);
        setFechaExpira(getLicAmbEntityObj().getExpira());
        setProveedorEntityObj(getLicAmbEntityObj().getProveedorByIdProveedor());
        setDepartamentoEntityObj(getLicAmbEntityObj().getDepartamentoByIdDepartamento());
        setInmuebleEntityObj(getLicAmbEntityObj().getInmuebleByIdInmueble());

        documentosEntityList = documentosBL.listAll(getLicAmbEntityObj().getIdLicAmb());
        setEditLicAmb(true);

    }

    /**
     * Eliminado logico de licencia abmiental
     */
    public void deleteLicAmb(LicAmbEntity licObj) {

        try {
            setLicAmbEntityObj(licObj);
            setDocumentosEntityList(documentosBL.listAll(licObj.getIdLicAmb()));
            licAmbBL.delete(getLicAmbEntityObj(), getDocumentosEntityList());
            controlerBitacora.delete(DescriptorBitacora.PROVEEDORES, String.valueOf
                    (getLicAmbEntityObj()
                            .getIdLicAmb()), String.valueOf(getLicAmbEntityObj().getNro()));
            newLicAmb();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    /**
     * Elimina documentos adjuntos
     *
     * @param index
     */
    public void deleteDocAdjunto(int index) {
        long idDoc = documentosEntityList.get(index).getIdDocumento();
        for (DocumentosEntity documentosEntity : documentosEntityList) {
            if (documentosEntity.getIdDocumento() == idDoc) {
                documentosEntity.setEstado(false);
                documentosEntityList.set(index, documentosEntity);
            }
        }
    }

    public void loadDocAdjuntos(int index) {
        setDocumentosEntityList(new ArrayList<>());
        setDocumentosEntityList(documentosBL.listAll(licAmbEntityList.get(index).getIdLicAmb()));

    }

    /**
     * Descarga de archivos adjuntos licencias ambientales
     *
     * @param index
     */
    public void donwloadAdjunt(int index) {
        try {
            long idLicAmbiental = getDocumentosEntityList().get(index).getIdDocumento();
            setDocumentosEntityObj(documentosBL.getById(idLicAmbiental));

            DocumentosEntity descargaDoc = documentosBL.donwloadDoc(getDocumentosEntityObj());

            InputStream stream = new ByteArrayInputStream(descargaDoc.getDocAdjunto());
            descarga = new DefaultStreamedContent(stream, "application/" + descargaDoc.getExtension(),
                    descargaDoc.getNombre());
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()),
                    "Directorio no encontrado: " + e.getMessage());
        }
    }

    /**
     * Carga de documentos adjuntos
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {


        try {
            if (getPrefijo) {
                setPrefijoDoc = UtilFile.generatePrefijo();
                getPrefijo = false;
            }
            setFileUploadEvent(event);
            documentosEntityObj = new DocumentosEntity();
            documentosEntityObj.setUbicacion(getDirectoryFile());
            documentosEntityObj.setNombre(getFileUploadEvent().getFile().getFileName());
            documentosEntityObj.setPertenece("LICENCIA");
            documentosEntityObj.setEstado(true);
            documentosEntityObj.setDocAdjunto(fileUploadEvent.getFile().getContents());
            documentosEntityObj.setPrefijo(setPrefijoDoc);
            documentosEntityList.add(documentosEntityObj);

            SysMessage.handleFileUpload(event);
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public List<LicAmbEntity> getLicAmbEntityList() {
        return licAmbEntityList;
    }

    public void setLicAmbEntityList(List<LicAmbEntity> licAmbEntityList) {
        this.licAmbEntityList = licAmbEntityList;
    }

    public List<ProveedorEntity> getProveedorEntityList() {
        return proveedorEntityList;
    }

    public void setProveedorEntityList(List<ProveedorEntity> proveedorEntityList) {
        this.proveedorEntityList = proveedorEntityList;
    }

    public List<DepartamentoEntity> getDepartamentoEntityList() {
        return departamentoEntityList;
    }

    public void setDepartamentoEntityList(List<DepartamentoEntity> departamentoEntityList) {
        this.departamentoEntityList = departamentoEntityList;
    }

    public List<DocumentosEntity> getDocumentosEntityList() {
        return documentosEntityList;
    }

    public void setDocumentosEntityList(List<DocumentosEntity> documentosEntityList) {
        this.documentosEntityList = documentosEntityList;
    }

    public List<InmuebleEntity> getInmuebleEntityList() {
        return inmuebleEntityList;
    }

    public void setInmuebleEntityList(List<InmuebleEntity> inmuebleEntityList) {
        this.inmuebleEntityList = inmuebleEntityList;
    }

    public LicAmbEntity getLicAmbEntityObj() {
        return licAmbEntityObj;
    }

    public void setLicAmbEntityObj(LicAmbEntity licAmbEntityObj) {
        this.licAmbEntityObj = licAmbEntityObj;
    }

    public DocumentosEntity getDocumentosEntityObj() {
        return documentosEntityObj;
    }

    public void setDocumentosEntityObj(DocumentosEntity documentosEntityObj) {
        this.documentosEntityObj = documentosEntityObj;
    }

    public ProveedorEntity getProveedorEntityObj() {
        return proveedorEntityObj;
    }

    public void setProveedorEntityObj(ProveedorEntity proveedorEntityObj) {
        this.proveedorEntityObj = proveedorEntityObj;
    }

    public DepartamentoEntity getDepartamentoEntityObj() {
        return departamentoEntityObj;
    }

    public void setDepartamentoEntityObj(DepartamentoEntity departamentoEntityObj) {
        this.departamentoEntityObj = departamentoEntityObj;
    }

    public InmuebleEntity getInmuebleEntityObj() {
        return inmuebleEntityObj;
    }

    public void setInmuebleEntityObj(InmuebleEntity inmuebleEntityObj) {
        this.inmuebleEntityObj = inmuebleEntityObj;
    }

    public boolean isEditLicAmb() {
        return editLicAmb;
    }

    public void setEditLicAmb(boolean editLicAmb) {
        this.editLicAmb = editLicAmb;
    }

    public Date getFechaExpira() {
        return fechaExpira;
    }

    public void setFechaExpira(Date fechaExpira) {
        this.fechaExpira = fechaExpira;
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

    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public Map<String, String> getEmailParameter() {
        return emailParameter;
    }

    public void setEmailParameter(Map<String, String> emailParameter) {
        this.emailParameter = emailParameter;
    }

    public StreamedContent getDescarga() {
        return descarga;
    }

    public void setDescarga(StreamedContent descarga) {
        this.descarga = descarga;
    }

    public String getInvalidateMessage() {
        return invalidateMessage;
    }

    public void setInvalidateMessage(String invalidateMessage) {
        this.invalidateMessage = invalidateMessage;
    }

    public LicAmbEntity getSelectLicAmbBeanObj() {
        return selectLicAmbBeanObj;
    }

    public void setSelectLicAmbBeanObj(LicAmbEntity selectLicAmbBeanObj) {
        this.selectLicAmbBeanObj = selectLicAmbBeanObj;
    }
}
