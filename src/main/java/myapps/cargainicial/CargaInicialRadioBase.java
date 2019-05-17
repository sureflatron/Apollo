package myapps.cargainicial;


import myapps.abm.business.DocumentosBL;
import myapps.abm.business.RadioBaseBL;
import myapps.abm.model.DocumentosEntity;
import myapps.abm.model.RadioBaseEntity;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.ResumenCarga;
import myapps.servicio_basico.util.SysMessage;
import myapps.servicio_basico.util.UtilFile;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ManagedBean
@ViewScoped
public class CargaInicialRadioBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(CargaInicialRadioBase.class);

    @EJB
    private IApplicationContext iApplicationContext;
    @Inject
    private RadioBaseBL radioBaseBL;
    @Inject
    private DocumentosBL documentosBL;

    private List<RadioBaseEntity> radioBaseEntityList;
    private List<RadioBaseEntity> radioBaseEntityListErrors;
    private List<ResumenCarga> resumenList;

    private RadioBaseEntity radioBaseEntityObj;
    private DocumentosEntity documentosEntityObj;

    private FileUploadEvent fileUploadEvent;
    private String directoryFile;
    private String nombreArchivo;
    private String registrosLeidos;
    private String nroColumn;
    private String registrosCargados;
    private String registroExistoso;
    private ResumenCarga resumenCarga;

    @PostConstruct
    void init() {
        try {
            loadParameterMiscellaneuos();
            radioBaseEntityList = new ArrayList<>();
            radioBaseEntityObj = new RadioBaseEntity();
            resumenList = new ArrayList<>();
            documentosEntityObj = new DocumentosEntity();
            resumenList = new ArrayList<>();
            resumenCarga = new ResumenCarga();

        } catch (Exception e) {
            LOG.error("[init] Fallo en el init.", e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void handleFileUpload(FileUploadEvent event) {
        try {
            setFileUploadEvent(event);
            String name = getFileUploadEvent().getFile().getFileName();
            int lastIndexOf = name.lastIndexOf('.');
            String extension = name.substring(lastIndexOf);
            List<ArrayList> arrayLists = radioBaseBL.readFile(extension, getFileUploadEvent()
                    .getFile().getContents());

            setRadioBaseEntityList(arrayLists.get(0));
            resumenList = arrayLists.get(1);

            resumenCarga.setNombreArchivo(getFileUploadEvent().getFile().getFileName());
            resumenCarga.setNroFilas(resumenList.get(0).getNroFilas());
            resumenCarga.setNroColumn(resumenList.get(0).getNroColumn());

            resumenCarga.setNroRegistrosNoValidos(resumenList.get(0).getNroRegistrosNoValidos());
            resumenCarga.setNroRegistrosValidos(resumenList.get(0).getNroRegistrosValidos());

            SysMessage.handleFileUpload(event);
        } catch (Exception e) {
            LOG.log(Level.ERROR, e.getMessage());
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void guardarRadioBase() {
        documentosEntityObj.setPertenece("CARGA INICIAL RADIO BASE");
        documentosEntityObj.setUbicacion(getDirectoryFile());
        documentosEntityObj.setEstado(true);
        documentosEntityObj.setPrefijo(UtilFile.generatePrefijo());
        documentosEntityObj.setDocAdjunto(getFileUploadEvent().getFile().getContents());
        documentosEntityObj.setNombre(getFileUploadEvent().getFile().getFileName());

        if (!radioBaseEntityList.isEmpty()) {
            List<ArrayList> arrayLists = radioBaseBL.saveRdbIni(radioBaseEntityList, documentosEntityObj);
            radioBaseEntityListErrors = arrayLists.get(0);
            List<String> obs = arrayLists.get(1);
            resumenCarga.setNroRegistroExitoso(obs.get(0));
        }
    }

    private void loadParameterMiscellaneuos() {
        setDirectoryFile(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .DIRECTORIO_CARGA_MASIVA.toString()));

/*		setExtenAdjuntos(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
				.EXTENSION_CARGA_MASIVA.toString()));
		setInvalidateMessage(iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
				.EXTENSION_ADJUNTOS_MSG.toString()));*/

    }

    public String handleFlow(FlowEvent event) {
        String stepToGo = event.getNewStep();
        if (stepToGo.equals("validacion") && (!resumenCarga.getNroColumn().equals("20"))) {
            SysMessage.error("ERROR", "Archivo no válido", null);
            documentosBL.remove(documentosEntityObj);
            documentosEntityObj = new DocumentosEntity();
            return event.getOldStep();
        }
        return event.getNewStep();
    }

    private FileUploadEvent getFileUploadEvent() {
        return fileUploadEvent;
    }

    private void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
        this.fileUploadEvent = fileUploadEvent;
    }

    private String getDirectoryFile() {
        return directoryFile;
    }

    private void setDirectoryFile(String directoryFile) {
        this.directoryFile = directoryFile;
    }

    public boolean observation(RadioBaseEntity radioBaseEntity) {
        return radioBaseEntity.getSitioid().equals("") || radioBaseEntity.getNombreRbs().equals("")
                || radioBaseEntity.getCodInmueble().equals("") || radioBaseEntity.getCodCve().equals("") || radioBaseEntity.getCostoInstall().equals("");

    }

    //region "GETTER and SETTER, Obj"


    public RadioBaseEntity getRadioBaseEntityObj() {
        return radioBaseEntityObj;
    }

    public void setRadioBaseEntityObj(RadioBaseEntity radioBaseEntityObj) {
        this.radioBaseEntityObj = radioBaseEntityObj;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRegistrosLeidos() {
        return registrosLeidos;
    }

    public void setRegistrosLeidos(String registrosLeidos) {
        this.registrosLeidos = registrosLeidos;
    }

    public String getNroColumn() {
        return nroColumn;
    }

    public void setNroColumn(String nroColumn) {
        this.nroColumn = nroColumn;
    }

    public String getRegistrosCargados() {
        return registrosCargados;
    }

    public void setRegistrosCargados(String registrosCargados) {
        this.registrosCargados = registrosCargados;
    }

    public String getRegistroExistoso() {
        return registroExistoso;
    }

    public void setRegistroExistoso(String registroExistoso) {
        this.registroExistoso = registroExistoso;
    }

    public ResumenCarga getResumenCarga() {
        return resumenCarga;
    }

    public void setResumenCarga(ResumenCarga resumenCarga) {
        this.resumenCarga = resumenCarga;
    }
    //endregion

    //region "GETTER and SETTER, List"


    public List<RadioBaseEntity> getRadioBaseEntityList() {
        return radioBaseEntityList;
    }

    public void setRadioBaseEntityList(List<RadioBaseEntity> radioBaseEntityList) {
        this.radioBaseEntityList = radioBaseEntityList;
    }

    public List<RadioBaseEntity> getRadioBaseEntityListErrors() {
        return radioBaseEntityListErrors;
    }

    public void setRadioBaseEntityListErrors(List<RadioBaseEntity> radioBaseEntityListErrors) {
        this.radioBaseEntityListErrors = radioBaseEntityListErrors;
    }

    //endregion
}
