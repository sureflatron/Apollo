package myapps.cargainicial;

import myapps.abm.business.ServicioBL;
import myapps.abm.model.DocumentosEntity;
import myapps.abm.model.InmServEntity;
import myapps.abm.model.RdbServEntity;
import myapps.abm.model.ServicioEntity;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.ResumeLoadExcel;
import myapps.servicio_basico.util.SysMessage;
import myapps.servicio_basico.util.UtilFile;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean
public class CargaInicialServicios implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(CargaInicialServicios.class);

    @Inject
    private ServicioBL servicioBL;
    @EJB
    private IApplicationContext iApplicationContext;

    private List<ServicioEntity> serviciosList;
    private List<InmServEntity> inmServList;
    private List<RdbServEntity> rdbServList;
    private List<CargaInicialServiciosDto> dtoList;
    private List<CargaInicialServiciosDto> dtoListErrors;

    private FileUploadEvent fileUpload;
    private String directoryFile;
    private ResumeLoadExcel resumeLoadExcel;
    private DocumentosEntity documentosEntityObj;

    @PostConstruct
    public void init() {
        initializeList();
        initializeObj();
        loadParameterMiscellaneuos();
    }

    /**
     * Inicializa listas
     */
    private void initializeList() {

        serviciosList = new ArrayList<>();
        inmServList = new ArrayList<>();
        rdbServList = new ArrayList<>();
        dtoList = new ArrayList<>();
    }

    /**
     * Inicializa objetos
     */
    private void initializeObj() {

        resumeLoadExcel = new ResumeLoadExcel();
        documentosEntityObj = new DocumentosEntity();

    }
    private void loadParameterMiscellaneuos() {
         directoryFile = iApplicationContext.getMapMiscellaneousParameters().get(EnumParametros
                .DIRECTORIO_CARGA_MASIVA.toString());

    }
    /**
     * Lectura de archivo excel
     * @param event
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void loadExcel(FileUploadEvent event) {
        try {

            fileUpload = event;
            resumeLoadExcel.setNameFile(event.getFile().getFileName());
            resumeLoadExcel.setExtentionFile(resumeLoadExcel.getNameFile().substring(resumeLoadExcel.getNameFile().lastIndexOf('.')));

            List<ArrayList> list = servicioBL.readFile(fileUpload.getFile().getContents());
            dtoList = list.get(0);

            List<ResumeLoadExcel> resumeList = list.get(1);
            resumeList.forEach(r -> {
                resumeLoadExcel.setNumColumns(r.getNumColumns());
                resumeLoadExcel.setNumRows(r.getNumRows());
                resumeLoadExcel.setValidFields(r.getValidFields());
                resumeLoadExcel.setNoValidFields(r.getNoValidFields());
            });

            SysMessage.handleFileUpload(event);
        } catch (Exception e) {
            LOG.log(Level.ERROR, e.getMessage());
        }

    }

    /**
     * Valida valore vacios
     *
     * @param dto
     * @return true
     */
    public boolean observation(CargaInicialServiciosDto dto) {
        return servicioBL.validarObligatorios(dto);
    }

    /**
     * Guarda servicios
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveServBasicos(){
        try{
            documentosEntityObj.setPertenece("CARGA INICIAL SERVICIOS");
            documentosEntityObj.setUbicacion(directoryFile);
            documentosEntityObj.setEstado(true);
            documentosEntityObj.setPrefijo(UtilFile.generatePrefijo());
            documentosEntityObj.setDocAdjunto(getFileUpload().getFile().getContents());
            documentosEntityObj.setNombre(getFileUpload().getFile().getFileName());

            List<ArrayList> arrayLists = servicioBL.saveServBasicosIni(getDtoList(),documentosEntityObj);
            dtoListErrors = new ArrayList<>();
            dtoListErrors= arrayLists.get(0);
            resumeLoadExcel.setNumSave(arrayLists.get(1).size());

        }catch (Exception e){
            LOG.log(Level.ERROR, e.getMessage());
        }

    }
    //region "SETTER AND GETTER OBJ"

    public FileUploadEvent getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUploadEvent fileUpload) {
        this.fileUpload = fileUpload;
    }

    public ResumeLoadExcel getResumeLoadExcel() {
        return resumeLoadExcel;
    }

//endregion

    //region "SETTER AND GETTER LIST"

    public List<ServicioEntity> getServiciosList() {
        return serviciosList;
    }

    public void setServiciosList(List<ServicioEntity> serviciosList) {
        this.serviciosList = serviciosList;
    }

    public List<InmServEntity> getInmServList() {
        return inmServList;
    }

    public void setInmServList(List<InmServEntity> inmServList) {
        this.inmServList = inmServList;
    }

    public List<RdbServEntity> getRdbServList() {
        return rdbServList;
    }

    public void setRdbServList(List<RdbServEntity> rdbServList) {
        this.rdbServList = rdbServList;
    }

    public List<CargaInicialServiciosDto> getDtoList() {
        return dtoList;
    }

    public void setDtoList(List<CargaInicialServiciosDto> dtoList) {
        this.dtoList = dtoList;
    }

    public List<CargaInicialServiciosDto> getDtoListErrors() {
        return dtoListErrors;
    }

    public void setDtoListErrors(List<CargaInicialServiciosDto> dtoListErrors) {
        this.dtoListErrors = dtoListErrors;
    }
    //endregion

}
