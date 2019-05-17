package myapps.user.bean;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import myapps.abm.business.HistorialUsuarioBL;
import myapps.abm.model.HistorialUsuarioEntity;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.servicio_basico.util.UtilNumber;
import myapps.servicio_basico.util.UtilUrl;
import myapps.user.business.RoleBL;
import myapps.user.business.UsuarioBL;
import myapps.user.ldap.ActiveDirectory;
import myapps.user.ldap.DescriptorBitacora;
import myapps.user.model.MuRol;


import myapps.user.model.MuUsuario;
import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class RoleUserForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioBL userBL;
    @Inject
    private HistorialUsuarioBL historialUsuarioBL;
    @Inject
    private RoleBL rolBL;

    @Inject
    private ControlerBitacora controlerBitacora;

    @EJB
    private ActiveDirectory activeDirectory;

    @EJB
    private IApplicationContext beanApplicationContext;

    private List<MuUsuario> listUser;
    private List<MuUsuario> listUserNA;
    private List<HistorialUsuarioEntity> historialUsuarioEntityList;
    private HistorialUsuarioEntity historialUsuarioEntityObj;
    private MuUsuario user = new MuUsuario();
    private String userId;
    private boolean edit;
    private String tipo;
    private List<SelectItem> selectItems;
    private String select;
    private Map<String, String> validateParameter;
    private static Logger log = Logger.getLogger(RoleUserForm.class);
    private boolean btnActivar;
    private boolean btnElimnar;
    private boolean btnGuardar;
    private boolean disablecampos;
    private MuUsuario selectMuUsuarioObj;
    private transient HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private String old;

    @PostConstruct
    public void init() {
        try {
			/*Servicio.setPagina(this.getClass().getSimpleName());
			Servicio.eliminarConexionesInactivas(this.getClass().getSimpleName());*/
            user = new MuUsuario();
            historialUsuarioEntityObj = new HistorialUsuarioEntity();
            cargarTablas();
            fillSelectItems();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            newUser();
        } catch (Exception e) {
            log.error("init|Fallo al inicializar la clase. " + e.getMessage());
        }
    }

    public void cargarTablas() {
        try {

            listUser = userBL.getUsers();
            listUserNA = userBL.getUsersNA();
            for (MuUsuario usuario : listUser) {
                usuario.setHistorialUsuariosByUsuarioId(historialUsuarioBL.listAllId(usuario.getUsuarioId()));
            }
            for (MuUsuario usuario : listUserNA) {
                usuario.setHistorialUsuariosByUsuarioId(historialUsuarioBL.listAllId(usuario.getUsuarioId()));
            }
        } catch (Exception e) {
            log.error("Fallo en cargar Tablas " + e.getMessage());
        }

    }

    private void fillSelectItems() {
        selectItems = new ArrayList<>();
        selectItems.add(new SelectItem("-1", "--Seleccionar--"));
        List<MuRol> listaRol = rolBL.getRoles();
        for (MuRol role : listaRol) {
            SelectItem sel = new SelectItem(role.getRolId(), role.getNombre());
            selectItems.add(sel);
        }
    }

    public void saveUser() {
        String name = "";
        Map<String, String> mapa;
        int idRole = Integer.parseInt(select);
        if (idRole == -1) {
            SysMessage.warn("Campo Rol es requerido", null);
            return;
        }
        if (UtilNumber.isNumeric(user.getLogin())) {
            mapa = activeDirectory.getNEhumanoo(user.getLogin().trim());
        } else if (UtilUrl.esTextoValido(user.getLogin(), "^[a-zA-Z]+$")) {
            mapa = activeDirectory.getNombreCompleto(user.getLogin().trim());
        } else {
            SysMessage.warn("Usuario/Ehumano Invalido", null);
            return;
        }
        if (!mapa.isEmpty()) {
            name = String.valueOf(mapa.get("givenName") + " " + mapa.get("sn")).trim();
            user.setLogin(mapa.get("sAMAccountName").trim());
            if (mapa.get("initials") != null) {
                user.setEhumano(Long.valueOf(mapa.get("initials").trim()));
            }
        }
        if (name.trim().isEmpty()) {
            SysMessage.warn("No se pudo obtener el nombre del usuario", null);
            return;
        }
        String str = "";
        try {
            str = userBL.validate(user, userId);
        } catch (Exception e) {
            log.error("[saveUser] Fallo al intentar validar los parametros.", e);
            str = "error con la conexion a la BD o otro problema";
        }

        if (!str.isEmpty()) {
            SysMessage.warn(str, null);
            if (edit) {
                user.setLogin(old);
            }
            return;
        }
        if (!name.isEmpty()) {
            user.setNombre(name);
        }
        try {
            user.setLogin(user.getLogin().toLowerCase());
            if (!edit) {
                user.setFechaCreacion(new Date());
                userBL.saveUserRole(user, idRole);
                historialUsuarioEntityObj.setFecha(new Date());
                historialUsuarioEntityObj.setMuUsuarioByUsuarioId(user);
                historialUsuarioEntityObj.setTipo("CREACIÓN");
                historialUsuarioEntityObj.setResponsable((String) request.getSession().getAttribute("TEMP$USER_NAME"));
                historialUsuarioBL.save(historialUsuarioEntityObj);
                controlerBitacora.insert(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            } else {
                userBL.updateUser(user, idRole);
                historialUsuarioEntityObj.setFecha(new Date());
                historialUsuarioEntityObj.setMuUsuarioByUsuarioId(user);
                historialUsuarioEntityObj.setTipo("MODIFICACIÓN");
                historialUsuarioEntityObj.setResponsable((String) request.getSession().getAttribute("TEMP$USER_NAME"));
                historialUsuarioBL.save(historialUsuarioEntityObj);
                controlerBitacora.update(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            }
            cargarTablas();
            newUser();
        } catch (Exception e) {
            log.error("[saveUser] usuario: " + user.getNombre() + ", Fallo al guardar el usuario", e);
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
        }
    }

    public void editRoleUser(MuUsuario usuario) {
        setTipo("Descripcion para Modificación del Usuario");
        user = usuario;
        select = String.valueOf(usuario.getRolByRolId().getRolId());
        edit = true;
        old = usuario.getLogin();
        btnElimnar = false;
        btnActivar = false;
        btnGuardar = true;
    }

    public void deleteUser(MuUsuario usuario) {
        setTipo("Descripcion para Eliminación del Usuario");
        user = usuario;
        select = String.valueOf(usuario.getRolByRolId().getRolId());
        btnElimnar = true;
        btnActivar = false;
        btnGuardar = false;
        disablecampos = true;
    }

    public void activarRoleUser(MuUsuario usuario) {
        setTipo("Descripcion para Activación del Usuario");
        user = usuario;
        select = String.valueOf(usuario.getRolByRolId().getRolId());
        btnActivar = true;
        btnGuardar = false;
        btnElimnar = false;
        disablecampos = true;
    }

    public void deleteRoleUser() {
        if (user.getUsuarioId() == 1) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Este Usuario no se puede elimnar es usuario Interno.", "");

        }
        try {
            userBL.deleteUser(user.getUsuarioId());
            historialUsuarioEntityObj.setFecha(new Date());
            historialUsuarioEntityObj.setMuUsuarioByUsuarioId(user);
            historialUsuarioEntityObj.setTipo("DESACTIVACIÓN");
            historialUsuarioEntityObj.setResponsable((String) request.getSession().getAttribute("TEMP$USER_NAME"));
            historialUsuarioBL.save(historialUsuarioEntityObj);
            controlerBitacora.delete(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
            cargarTablas();
            newUser();
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
        } catch (Exception e) {
            log.error("[deleteRoleUser]  error al eliminar el menu id:" + user.getUsuarioId() + "  " + e);
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
        }

    }

    public void newUser() {
        btnGuardar = true;
        btnActivar = false;
        btnElimnar = false;
        disablecampos = false;
        edit = false;
        user = new MuUsuario();
        select = "-1";
        historialUsuarioEntityObj = new HistorialUsuarioEntity();
        setTipo("Descripción para Nuevo Usuario");
    }

    public void activarUsuario() throws Exception {
        userBL.activateUser(user);
        historialUsuarioEntityObj.setFecha(new Date());
        historialUsuarioEntityObj.setMuUsuarioByUsuarioId(user);
        historialUsuarioEntityObj.setTipo("ACTIVACIÓN");
        historialUsuarioEntityObj.setResponsable((String) request.getSession().getAttribute("TEMP$USER_NAME"));
        historialUsuarioBL.save(historialUsuarioEntityObj);
        controlerBitacora.update(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
        SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Activacion satisfactoria", "");
        btnActivar = false;
        cargarTablas();
        newUser();

    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public MuUsuario getUser() {
        return user;
    }

    public void setUser(MuUsuario user) {
        this.user = user;
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

    public List<MuUsuario> getListUser() {
        return listUser;
    }

    public void setListUser(List<MuUsuario> listUser) {
        this.listUser = listUser;
    }

    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public List<MuUsuario> getListUserNA() {
        return listUserNA;
    }

    public void setListUserNA(List<MuUsuario> listUserNA) {
        this.listUserNA = listUserNA;
    }

    public List<HistorialUsuarioEntity> getHistorialUsuarioEntityList() {
        return historialUsuarioEntityList;
    }

    public void setHistorialUsuarioEntityList(List<HistorialUsuarioEntity> historialUsuarioEntityList) {
        this.historialUsuarioEntityList = historialUsuarioEntityList;
    }

    public HistorialUsuarioEntity getHistorialUsuarioEntityObj() {
        return historialUsuarioEntityObj;
    }

    public void setHistorialUsuarioEntityObj(HistorialUsuarioEntity historialUsuarioEntityObj) {
        this.historialUsuarioEntityObj = historialUsuarioEntityObj;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isBtnActivar() {
        return btnActivar;
    }

    public void setBtnActivar(boolean btnActivar) {
        this.btnActivar = btnActivar;
    }

    public boolean isBtnElimnar() {
        return btnElimnar;
    }

    public void setBtnElimnar(boolean btnElimnar) {
        this.btnElimnar = btnElimnar;
    }

    public boolean isBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(boolean btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public MuUsuario getSelectMuUsuarioObj() {
        return selectMuUsuarioObj;
    }

    public void setSelectMuUsuarioObj(MuUsuario selectMuUsuarioObj) {
        this.selectMuUsuarioObj = selectMuUsuarioObj;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public boolean isDisablecampos() {
        return disablecampos;
    }

    public void setDisablecampos(boolean disablecampos) {
        this.disablecampos = disablecampos;
    }
}
