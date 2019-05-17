package myapps.user.bean;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.business.GrupoAdBL;
import myapps.user.business.RoleBL;
import myapps.user.ldap.ActiveDirectory;
import myapps.user.ldap.DescriptorBitacora;
import myapps.user.model.MuGrupoAd;
import myapps.user.model.MuRol;
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
public class MuGrupoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private GrupoAdBL groupBL;
    @Inject
    private RoleBL rolBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private ActiveDirectory activeDirectory;
    @EJB
    private IApplicationContext beanApplicationContext;
    private List<MuGrupoAd> listGroup;
    private MuGrupoAd group = new MuGrupoAd();
    private String groupId;
    private boolean edit;

    private List<SelectItem> selectItems;
    private String select;

    public static final Logger log = Logger.getLogger(MuGrupoBean.class);
    private Map<String, String> validateParameter;

    @PostConstruct
    public void init() {
        try {
            group = new MuGrupoAd();
            listGroup = groupBL.getGroups();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            fillSelectItems();
        } catch (Exception e) {
            log.error("init|Fallo al iniciar gestion de grupo", e);
        }
    }

    private void fillSelectItems() {
        selectItems = new ArrayList<>();
        selectItems.add(new SelectItem("-1", "Grupos_Rol"));
        List<MuRol> listaRol = rolBL.getRoles();
        for (MuRol role : listaRol) {
            SelectItem sel = new SelectItem(role.getRolId(), role.getNombre());
            selectItems.add(sel);
        }
    }

    public String saveGroup() {
        int idRole = Integer.parseInt(select);
        if (idRole == -1) {
            SysMessage.warn("Seleccione un Rol.", null);
            return "";
        }

        String str = groupBL.validate(group, groupId);
        if (!str.isEmpty()) {
            SysMessage.warn(str, null);
            return "";
        }

        str = validarExisteEnAD(group.getNombre());
        if (!str.isEmpty()) {
            SysMessage.warn(str, null);
            return "";
        }

        try {
            if (!edit) {
                groupBL.saveGroupRole(group, idRole);
                controlerBitacora.insert(DescriptorBitacora.GRUPO, group.getGrupoId() + "", group.getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            } else {
                int id = Integer.parseInt(groupId);
                group.setGrupoId(id);
                groupBL.updateGroup(group, idRole);
                controlerBitacora.update(DescriptorBitacora.GRUPO, group.getGrupoId() + "", group.getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            }
            listGroup = groupBL.getGroups();
            newGroup();
        } catch (Exception e) {
            log.error("[saveGroup] error al momento de modificar o guardar: " + group.getNombre() + " " + e);
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
        }
        return "";
    }

    public void editRoleGroup() {
        String idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("groupId");
        int id = Integer.parseInt(idstr);
        group = groupBL.getGroup(id);
        select = group.getMuRol().getRolId() + "";
        groupId = idstr;
        edit = true;
    }

    public void deleteRoleGroup() {
        String idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("groupId");
        int id = Integer.parseInt(idstr);
        try {
            group = groupBL.getGroup(id);
            groupBL.deleteGroup(id);
            controlerBitacora.delete(DescriptorBitacora.GRUPO, group.getGrupoId() + "", group.getNombre());
            listGroup = groupBL.getGroups();
            newGroup();
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
        } catch (Exception e) {
            log.error("[deleteRoleGroup]  error al eliminar el menu id:" + idstr + "  " + e);
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
        }
    }

    public void newGroup() {
        edit = false;
        group = new MuGrupoAd();
        select = "-1";
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public MuGrupoAd getGroup() {
        return group;
    }

    public void setGroup(MuGrupoAd group) {
        this.group = group;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public List<MuGrupoAd> getListGroup() {
        return listGroup;
    }

    public void setListGroup(List<MuGrupoAd> listGroup) {
        this.listGroup = listGroup;
    }

    private String validarExisteEnAD(String nameNewGrupo) {
        try {
            if (!activeDirectory.validarGrupo(nameNewGrupo)) {
                return "No existe un grupo  '" + nameNewGrupo + "' en Active Directory";
            }
        } catch (Exception e) {
            log.error("[validarExisteEnAD] Error al intentar verificar si grupo existe en AD  nombre=" + nameNewGrupo);
            return "No se puede comprobar si este grupo existe en Active Directory";
        }
        return "";
    }

    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }
}
