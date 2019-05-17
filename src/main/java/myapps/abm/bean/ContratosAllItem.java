package myapps.abm.bean;

import myapps.abm.business.ContratosBL;
import myapps.abm.business.DocumentosBL;
import myapps.abm.business.SelectOptions;
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
public class ContratosAllItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ContratosAllItem.class);

    @Inject
    private ContratosBL contratosBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @Inject
    private DocumentosBL documentosBL;
    @Inject
    private SelectOptions selectCbx;
    @EJB
    private IApplicationContext beanApplicationContext;

    private List<ContratosEntity> contratosEntityList;
    private List<ContratosEntity> contratosEntityListPropietarios;
    private List<DepartamentoEntity> departamentoEntityList;
    private List<DocumentosEntity> documentosEntityList;

    private List<ServicioEntity> servicioEntityList;
    private List<InmuebleEntity> inmuebleEntityList;
    private List<RadioBaseEntity> radioBaseEntityList;
    private List<ProveedorEntity> proveedorEntityList;

    private Map<String, String> validateParameter;

    private ContratosEntity contratosEntityObj;
    private ContratosEntity contratoSelected;
    private DepartamentoEntity departamentoEntityObj;
    private InmuebleEntity inmuebleEntityObj;
    private ServicioEntity servicioEntityObj;
    private RadioBaseEntity radioBaseEntityObj;
    private ProveedorEntity proveedorEntityObj;
    private DocumentosEntity documentosEntityObj;

    private InmuebleEntity selectInmuebleEntityObj;
    private ServicioEntity selectServicioEntityObj;
    private RadioBaseEntity selectRadioBaseEntityObj;
    private ProveedorEntity selectProveedorEntityObj;

    private boolean pnlVisualizeServ = false;
    private boolean pnlVisualizeInm = false;
    private boolean pnlVisualizeRdb = false;
    private boolean pnlVisualizeAll = false;

    private String selectOptions;
    private Date fechaIni;
    private Date fechaFin;
    private String sitioId;

    private static final String INMUEBLE = "Inmueble";
    private static final String SERVICIO = "Servicio";
    private static final String PROVEEDOR = "Proveedor";
    private static final String RADIO_BASE = "Radio Base";
    private static final String SIN_ASIGNACION = "Sin Asignación";

    private transient StreamedContent donwContent;

    @PostConstruct
    public void init() {
        setSelectOptions("0");
        validateParameter = beanApplicationContext.getMapValidateParameters();
        initializeList();
        initializeObj();
        loadCbx();

        contratosEntityList = contratosBL.listAll();
    }

    private void initializeObj() {
        setContratosEntityObj(new ContratosEntity());
        setDepartamentoEntityObj(new DepartamentoEntity());
        setContratoSelected(new ContratosEntity());
    }

    private void initializeList() {
        contratosEntityList = new ArrayList<>();
        departamentoEntityList = new ArrayList<>();
        setContratosEntityListPropietarios(new ArrayList<>());
        setDocumentosEntityList(new ArrayList<>());
    }

    private void loadCbx() {
        setDepartamentoEntityList(selectCbx.cbxDepartamento());
        setContratosEntityListPropietarios(selectCbx.cbxPropieatarioContratos());
    }

    /**
     * Filter from selectOneRadio
     */
    public void searchOptions() {
        setContratosEntityObj(new ContratosEntity());
        fechaIni = null;
        fechaFin = null;
        setContratosEntityList(contratosBL.listAllOptions(selectOptions));

    }
    public void clean(){
        setSelectOptions("0");
        setContratosEntityObj(new ContratosEntity());
        fechaIni = null;
        fechaFin = null;
        setContratosEntityList(contratosBL.listAllOptions(selectOptions));
    }

    public void search() {
        if (selectOptions == null) {
            SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Debe seleccionar una categoria de contrato.", null);
        } else {
            contratosEntityObj.setExpiracion(fechaIni);
            contratosEntityObj.setExpiracion2(fechaFin);
            contratosEntityObj.setDepartamentoByIdDepartamento(departamentoEntityObj);
            contratosEntityList = contratosBL.listAllFilter(contratosEntityObj, selectOptions);
        }

    }

    public void mensajes() {
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("operation") != null) {
            int operacion = (int) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("operation");
            if (operacion == 0) {
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else if (operacion == 1) {
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
        }

    }

    /**
     * Get attachement of contratos
     *
     * @param contract
     */
    public void loadAttachement(ContratosEntity contract) {
        documentosEntityList.clear();
        documentosEntityList = documentosBL.listAll(contract.getIdContrato());

    }

    /**
     * @param document
     */
    public void donwloadAttach(DocumentosEntity document) {
        try {

            DocumentosEntity descargaDoc = documentosBL.donwloadDoc(document);

            InputStream stream = new ByteArrayInputStream(descargaDoc.getDocAdjunto());
            donwContent = new DefaultStreamedContent(stream, "application/" + descargaDoc.getExtension(),
                    descargaDoc.getNombre());
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()),
                    "Directorio no encontrado: " + e.getMessage());
        }
    }

    public String getContract(ContratosEntity contract) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ContratoObj",
                contract);
        return "/view/contratos.xhtml";
    }

    public void deleteContract(ContratosEntity entity) {

        try {
            setContratosEntityObj(contratosBL.getById(entity.getIdContrato()));
            contratosBL.delete(getContratosEntityObj(), documentosEntityList);
            controlerBitacora.delete(DescriptorBitacora.CONTRATOS, String.valueOf
                    (getContratosEntityObj()
                            .getIdContrato()), getContratosEntityObj().getResponsable());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            contratosEntityList = contratosBL.listAll();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public boolean comparaFecha(ContratosEntity contratosEntity) {
        int valor = UtilDate.comparaFechaDate(contratosEntity.getExpiracion());
        return valor == 1;
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

    public DocumentosEntity getDocumentosEntityObj() {
        return documentosEntityObj;
    }

    public void setDocumentosEntityObj(DocumentosEntity documentosEntityObj) {
        this.documentosEntityObj = documentosEntityObj;
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

    public ContratosEntity getContratoSelected() {
        return contratoSelected;
    }

    public void setContratoSelected(ContratosEntity contratoSelected) {
        this.contratoSelected = contratoSelected;
    }

    public StreamedContent getDonwContent() {
        return donwContent;
    }

    public void setDonwContent(StreamedContent donwContent) {
        this.donwContent = donwContent;
    }

    public ContratosEntity getContratosEntityObj() {
        return contratosEntityObj;
    }

    public void setContratosEntityObj(ContratosEntity contratosEntityObj) {
        this.contratosEntityObj = contratosEntityObj;
    }

    public DepartamentoEntity getDepartamentoEntityObj() {
        return departamentoEntityObj;
    }

    public void setDepartamentoEntityObj(DepartamentoEntity departamentoEntityObj) {
        this.departamentoEntityObj = departamentoEntityObj;
    }

    public boolean isPnlVisualizeServ() {
        return pnlVisualizeServ;
    }

    public void setPnlVisualizeServ(boolean pnlVisualizeServ) {
        this.pnlVisualizeServ = pnlVisualizeServ;
    }

    public boolean isPnlVisualizeInm() {
        return pnlVisualizeInm;
    }

    public void setPnlVisualizeInm(boolean pnlVisualizeInm) {
        this.pnlVisualizeInm = pnlVisualizeInm;
    }

    public boolean isPnlVisualizeRdb() {
        return pnlVisualizeRdb;
    }

    public void setPnlVisualizeRdb(boolean pnlVisualizeRdb) {
        this.pnlVisualizeRdb = pnlVisualizeRdb;
    }

    public boolean isPnlVisualizeAll() {
        return pnlVisualizeAll;
    }

    public void setPnlVisualizeAll(boolean pnlVisualizeAll) {
        this.pnlVisualizeAll = pnlVisualizeAll;
    }

    public String getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(String selectOptions) {
        this.selectOptions = selectOptions;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getSitioId() {
        return sitioId;
    }

    public void setSitioId(String sitioId) {
        this.sitioId = sitioId;
    }

    //endregion

    //region GETTER AND SETTER LIST

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

    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public List<ContratosEntity> getContratosEntityList() {
        return contratosEntityList;
    }

    public void setContratosEntityList(List<ContratosEntity> contratosEntityList) {
        this.contratosEntityList = contratosEntityList;
    }

    public List<ContratosEntity> getContratosEntityListPropietarios() {
        return contratosEntityListPropietarios;
    }

    public void setContratosEntityListPropietarios(List<ContratosEntity> contratosEntityListPropietarios) {
        this.contratosEntityListPropietarios = contratosEntityListPropietarios;
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
}
