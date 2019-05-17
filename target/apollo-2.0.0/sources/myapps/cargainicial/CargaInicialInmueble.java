package myapps.cargainicial;

import myapps.abm.business.DocumentosBL;
import myapps.abm.business.InmuebleBL;
import myapps.abm.model.DocumentosEntity;
import myapps.abm.model.InmuebleEntity;
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
public class CargaInicialInmueble implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(CargaInicialInmueble.class);

    @EJB
    private IApplicationContext iApplicationContext;
    @Inject
    private InmuebleBL inmuebleBL;
    @Inject
    private DocumentosBL documentosBL;

    private ResumenCarga resumenCarga;

    private List<InmuebleEntity> inmuebleEntityList;
    private List<InmuebleEntity> inmuebleEntityListErrors;
    private List<ResumenCarga> resumenList;

    private InmuebleEntity inmuebleEntityObj;
    private DocumentosEntity documentosEntityObj;

    private FileUploadEvent fileUploadEvent;
    private String directoryFile;

    @PostConstruct
    void init() {
        try {
            loadParameterMiscellaneuos();
            inmuebleEntityList = new ArrayList<>();
            inmuebleEntityObj = new InmuebleEntity();
            resumenList = new ArrayList<>();
            documentosEntityObj = new DocumentosEntity();
            resumenCarga=new ResumenCarga();

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
            List<ArrayList> inmuebleEntityList1 = inmuebleBL.readFile(extension,
                    getFileUploadEvent().getFile().getContents());

            setInmuebleEntityList( inmuebleEntityList1.get(0));
            resumenList = inmuebleEntityList1.get(1);

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

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void guardarInmueble() {
        documentosEntityObj.setPertenece("CARGA INICIAL INMUEBLE");
        documentosEntityObj.setUbicacion(getDirectoryFile());
        documentosEntityObj.setEstado(true);
        documentosEntityObj.setPrefijo(UtilFile.generatePrefijo());
        documentosEntityObj.setDocAdjunto(getFileUploadEvent().getFile().getContents());
        documentosEntityObj.setNombre(getFileUploadEvent().getFile().getFileName());

        if (!inmuebleEntityList.isEmpty()) {
            List<ArrayList> arrayLists= inmuebleBL.saveInmIni(inmuebleEntityList, documentosEntityObj);
            inmuebleEntityListErrors = arrayLists.get(0);
            List<String> obs  = arrayLists.get(1);
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

        if (stepToGo.equals("validacion") && (!resumenCarga.getNroColumn().equals("14"))) {
            SysMessage.error("ERROR", "Archivo no válido", null);
            documentosBL.remove(documentosEntityObj);
            documentosEntityObj = new DocumentosEntity();
            return event.getOldStep();
        }
        return event.getNewStep();
    }

    /**
     * Valida valore vacios
     * @param inm
     * @return true
     */
    public boolean observation(InmuebleEntity inm){
        return inm.getNombreRbs().equals("");
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

    //region "GETTER and SETTER, Obj"

    public InmuebleEntity getInmuebleEntityObj() {
        return inmuebleEntityObj;
    }

    public void setInmuebleEntityObj(InmuebleEntity inmuebleEntityObj) {
        this.inmuebleEntityObj = inmuebleEntityObj;
    }

    public ResumenCarga getResumenCarga() {
        return resumenCarga;
    }

    public void setResumenCarga(ResumenCarga resumenCarga) {
        this.resumenCarga = resumenCarga;
    }
//endregion

    //region "GETTER and SETTER, List"

    public List<InmuebleEntity> getInmuebleEntityList() {
        return inmuebleEntityList;
    }

    public void setInmuebleEntityList(List<InmuebleEntity> inmuebleEntityList) {
        this.inmuebleEntityList = inmuebleEntityList;
    }

    public List<InmuebleEntity> getInmuebleEntityListErrors() {
        return inmuebleEntityListErrors;
    }

    public void setInmuebleEntityListErrors(List<InmuebleEntity> inmuebleEntityListErrors) {
        this.inmuebleEntityListErrors = inmuebleEntityListErrors;
    }

//endregion
}
