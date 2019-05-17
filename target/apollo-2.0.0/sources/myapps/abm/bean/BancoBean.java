package myapps.abm.bean;

import myapps.abm.business.BancoBL;
import myapps.abm.business.SelectOptions;
import myapps.abm.model.BancoEntity;
import myapps.abm.model.DepartamentoEntity;
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
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class BancoBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(BancoBean.class);

    @Inject
    private BancoBL bancoBL;
    @Inject
    private SelectOptions selectOptions;
    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;

    private List<BancoEntity> bancoEntityList = new ArrayList<>();
    private List<DepartamentoEntity> departamentoEntityList = new ArrayList<>();

    private BancoEntity bancoEntityObj;
    private DepartamentoEntity departamentoEntityObj;
    private boolean editBanco;
    private long selectBanco;
    private Map<String, String> validateParameter;
    private Map<String, String> emailParameter;

    @PostConstruct
    void init() {
        try {
            bancoEntityObj = new BancoEntity();
            departamentoEntityObj = new DepartamentoEntity();
            departamentoEntityList = selectOptions.cbxDepartamento();
            loadBancos();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            emailParameter = beanApplicationContext.getMapEmailParameters();
        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }

    public void newBancos() {
        bancoEntityObj = new BancoEntity();
        departamentoEntityObj = new DepartamentoEntity();
        setSelectBanco(false);
        loadBancos();
    }

    public void actionBanco() {
        try {
            if (getSelectBanco() && bancoEntityObj.getIdBanco() > 0) {
                bancoEntityObj.setDepartamentoByIdDepartamento(departamentoEntityObj);
                bancoBL.update(bancoEntityObj);
                controlerBitacora.update(DescriptorBitacora.BANCOS, String.valueOf
                        (getBancoEntityObj().getIdBanco()), getBancoEntityObj()
                        .getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                bancoEntityObj.setDepartamentoByIdDepartamento(departamentoEntityObj);
                bancoBL.save(bancoEntityObj);
                controlerBitacora.insert(DescriptorBitacora.BANCOS, String.valueOf(getBancoEntityObj()
                        .getIdBanco()), getBancoEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }

            loadBancos();
            newBancos();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadBancos() {
        bancoEntityList = bancoBL.listAll();
    }

    public void editarBanco(BancoEntity bancoEntity) {
        setBancoEntityObj(bancoEntity);
        setDepartamentoEntityObj(bancoEntityObj.getDepartamentoByIdDepartamento());
        setSelectBanco(true);
    }

    public void deleteBanco() {
        try {
            setEditBanco(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("idBanco")));
            setBancoEntityObj(bancoBL.getById(getEditBanco()));
            bancoBL.delete(getBancoEntityObj());
            controlerBitacora.delete(DescriptorBitacora.PROVEEDORES, String.valueOf
                    (getBancoEntityObj()
                            .getIdBanco()), getBancoEntityObj().getNombre());
            newBancos();
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public List<BancoEntity> getBancoEntityList() {
        return bancoEntityList;
    }

    public void setBancoEntityList(List<BancoEntity> bancoEntityList) {
        this.bancoEntityList = bancoEntityList;
    }

    public List<DepartamentoEntity> getDepartamentoEntityList() {
        return departamentoEntityList;
    }

    public void setDepartamentoEntityList(List<DepartamentoEntity> departamentoEntityList) {
        this.departamentoEntityList = departamentoEntityList;
    }

    public BancoEntity getBancoEntityObj() {
        return bancoEntityObj;
    }

    public void setBancoEntityObj(BancoEntity bancoEntityObj) {
        this.bancoEntityObj = bancoEntityObj;
    }

    public boolean getSelectBanco() {
        return editBanco;
    }

    public void setSelectBanco(boolean selectBanco) {
        this.editBanco = selectBanco;
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

    public DepartamentoEntity getDepartamentoEntityObj() {
        return departamentoEntityObj;
    }

    public void setDepartamentoEntityObj(DepartamentoEntity departamentoEntityObj) {
        this.departamentoEntityObj = departamentoEntityObj;
    }

    public long getEditBanco() {
        return selectBanco;
    }

    public void setEditBanco(long editBanco) {
        this.selectBanco = editBanco;
    }
}
