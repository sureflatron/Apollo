package myapps.abm.bean;

import myapps.abm.business.CategoriaBL;
import myapps.abm.business.DepartamentoBL;
import myapps.abm.business.ProveedorBL;
import myapps.abm.model.CategoriaEntity;
import myapps.abm.model.DepartamentoEntity;
import myapps.abm.model.FusionEntity;
import myapps.abm.model.ProveedorEntity;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.DescriptorBitacora;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class ProveedorBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ProveedorBean.class);

    @Inject
    private ProveedorBL proveedorBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @Inject
    private DepartamentoBL departamentoBL;
    @Inject
    private CategoriaBL categoriaBL;

    @EJB
    private IApplicationContext beanApplicationContext;

    private List<ProveedorEntity> proveedorEntityList = new ArrayList<>();
    private ProveedorEntity proveedorEntityObj;
    private ProveedorEntity selectproveedorEntityObj;

    private boolean editProveedor;
    private long selectIdProv;
    private boolean fusion;
    private boolean visibleDialog = false;

    private List<String> proveedorSelected;

    private List<SelectItem> selectItemsProveedor;
    private List<DepartamentoEntity> listDepartamento;
    private List<CategoriaEntity> listCategoria;

    private Map<String, String> validateParameter;
    private Map<String, String> emailParameter;

    private boolean eliminarUnion = false;

    @PostConstruct
    void init() {
        try {
            fusion = false;
            proveedorSelected = new ArrayList<>();
            proveedorEntityObj = new ProveedorEntity();
            loadProveedores();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            emailParameter = beanApplicationContext.getMapEmailParameters();
            visibleDialog = false;
            listDepartamento = departamentoBL.listAll();
            listCategoria = categoriaBL.listAll();
            eliminarUnion = false;

        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }

    public void newProveedor() {
        proveedorEntityObj = new ProveedorEntity();
        setEditProveedor(false);
        setSelectIdProv((long) 0);
        setFusion(false);
        visibleDialog = false;
        loadProveedores();
        eliminarUnion = false;
    }

    public void actionProveedor() {
        try {
            if (fusion) {
                proveedorEntityObj.setIsFusion(Long.valueOf(1));
            } else {
                proveedorEntityObj.setIsFusion(Long.valueOf(0));
            }

            if (isEditProveedor() && getSelectIdProv() != 0) {
                if (eliminarUnion) {
                    proveedorBL.deleteFusionByIdProveedor(getSelectIdProv());
                    String aux = String.valueOf(getProveedorEntityObj().getIdProveedor()) + " - "
                            + getProveedorEntityObj().getNombre() +
                            ": Se eliminó la fusión de proveedores";
                    controlerBitacora.accion(DescriptorBitacora.PROVEEDORES, aux);
                }

                proveedorBL.update(proveedorEntityObj);
                controlerBitacora.update(DescriptorBitacora.PROVEEDORES, String.valueOf
                        (getProveedorEntityObj().getIdProveedor()), getProveedorEntityObj()
                        .getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
                eliminarUnion = false;
            } else {
                proveedorBL.save(proveedorEntityObj);
                controlerBitacora.insert(DescriptorBitacora.PROVEEDORES, String.valueOf(getProveedorEntityObj()
                        .getIdProveedor()), getProveedorEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }

            loadProveedores();
            newProveedor();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadProveedores() {
        proveedorEntityList = proveedorBL.listAll();
    }

    public void checkedFusion() {
        if (!fusion && isEditProveedor()) {
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se eliminar la unión de proveedores", "");
            eliminarUnion = true;
        }
    }

    public void editarProveedor() {
        setSelectIdProv(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("idProv")));
        setProveedorEntityObj(proveedorBL.getProveedordById(getSelectIdProv()));
        setEditProveedor(true);
        visibleDialog = false;
        fusion = proveedorEntityObj.getIsFusion() == 1 ? true : false;
    }

    public void deleteProveedor() {
        try {
            setSelectIdProv(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("idProv")));

            setProveedorEntityObj(proveedorBL.getProveedordById(getSelectIdProv()));

            proveedorBL.delete(getProveedorEntityObj());
            proveedorBL.deleteFusionProveedores(getProveedorEntityObj().getIdProveedor());

            controlerBitacora.delete(DescriptorBitacora.PROVEEDORES, String.valueOf
                    (getProveedorEntityObj().getIdProveedor()), getProveedorEntityObj().getNombre());

            newProveedor();
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public void cargarProveedores(long idProveedor) {
        proveedorSelected.clear();
        setProveedorEntityObj(proveedorBL.getProveedordById(idProveedor));
        try {
            if (idProveedor > 0) {
                List<FusionEntity> listaFusion = proveedorBL.getFusionByProveedor(idProveedor);
                if (listaFusion != null && !listaFusion.isEmpty()) {
                    for (FusionEntity fusionEntity : listaFusion) {
                        logger.info("vehimar: " + fusionEntity.getIdProveedor());
                        proveedorSelected.add(String.valueOf(fusionEntity.getIdProveedor()));
                    }
                }
                visibleDialog = true;
                setSelectIdProv(idProveedor);
                selectItemsProveedor = new ArrayList<>();
                List<ProveedorEntity> proveedorList = proveedorBL.listAllFusion(idProveedor);
                for (ProveedorEntity proveedorEntity : proveedorList) {
                    SelectItem item = new SelectItem(proveedorEntity.getIdProveedor(), proveedorEntity.getNombre());
                    selectItemsProveedor.add(item);
                }
            }

        } catch (Exception e) {
            logger.error("[cargarProveedores] Fallo al cargar los proveedores.", e);
        }
    }

    public void guardarFusion() {
        try {
            if (selectIdProv > 0) {
                proveedorBL.deleteFusionByIdProveedor(selectIdProv);
                controlerBitacora.delete(DescriptorBitacora.UNION_PROVEEDORES, String.valueOf(getProveedorEntityObj()
                        .getIdProveedor()), "Se elimino la unión de proveedores para crear nueva fusión");

                for (String select : proveedorSelected) {
                    FusionEntity fusionEntity = new FusionEntity();
                    fusionEntity.setEstado(Long.valueOf(1));
                    fusionEntity.setIdProveedorFusion(selectIdProv);
                    fusionEntity.setIdProveedor(Integer.parseInt(select));
                    proveedorBL.saveFusion(fusionEntity);
                    ProveedorEntity auxProveedor = proveedorBL.getProveedordById(new Long(select));
                    controlerBitacora.insert(DescriptorBitacora.UNION_PROVEEDORES, String.valueOf(getProveedorEntityObj()
                            .getIdProveedor()), getProveedorEntityObj().getNombre() + " - Unión: " + auxProveedor.getNombre());
                }
                this.visibleDialog = false;
            }
        } catch (Exception e) {
            logger.error("[guardarFusion] Fallo al guarda la union de proveedores.", e);
        }
    }


    public List<ProveedorEntity> getProveedorEntityList() {
        return proveedorEntityList;
    }

    public void setProveedorEntityList(List<ProveedorEntity> proveedorEntityList) {
        this.proveedorEntityList = proveedorEntityList;
    }

    public ProveedorEntity getProveedorEntityObj() {
        return proveedorEntityObj;
    }

    public void setProveedorEntityObj(ProveedorEntity proveedorEntityObj) {
        this.proveedorEntityObj = proveedorEntityObj;
    }

    public long getSelectIdProv() {
        return selectIdProv;
    }

    public void setSelectIdProv(long selectIdProv) {
        this.selectIdProv = selectIdProv;
    }

    public boolean isEditProveedor() {
        return editProveedor;
    }

    public void setEditProveedor(boolean editProveedor) {
        this.editProveedor = editProveedor;
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

    public boolean isFusion() {
        return fusion;
    }

    public void setFusion(boolean fusion) {
        this.fusion = fusion;
    }

    public boolean isVisibleDialog() {
        return visibleDialog;
    }

    public void setVisibleDialog(boolean visibleDialog) {
        this.visibleDialog = visibleDialog;
    }

    public List<String> getProveedorSelected() {
        return proveedorSelected;
    }

    public void setProveedorSelected(List<String> proveedorSelected) {
        this.proveedorSelected = proveedorSelected;
    }

    public List<SelectItem> getSelectItemsProveedor() {
        return selectItemsProveedor;
    }

    public void setSelectItemsProveedor(List<SelectItem> selectItemsProveedor) {
        this.selectItemsProveedor = selectItemsProveedor;
    }

    public List<DepartamentoEntity> getListDepartamento() {
        return listDepartamento;
    }

    public void setListDepartamento(List<DepartamentoEntity> listDepartamento) {
        this.listDepartamento = listDepartamento;
    }

    public List<CategoriaEntity> getListCategoria() {
        return listCategoria;
    }

    public void setListCategoria(List<CategoriaEntity> listCategoria) {
        this.listCategoria = listCategoria;
    }


    public ProveedorEntity getSelectproveedorEntityObj() {
        return selectproveedorEntityObj;
    }

    public void setSelectproveedorEntityObj(ProveedorEntity selectproveedorEntityObj) {
        this.selectproveedorEntityObj = selectproveedorEntityObj;
    }
}
