package myapps.user.bean;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.business.BitacoraBL;
import myapps.user.business.UsuarioBL;
import myapps.user.model.MuBitacora;
import myapps.user.model.MuUsuario;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class BitacoraForm implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Logger log = Logger.getLogger(BitacoraForm.class);

    @EJB
    private IApplicationContext beanApplicationContext;

    @Inject
    private BitacoraBL logBl;

    @Inject
    private UsuarioBL usuarioBl;

    private List<MuBitacora> listBitacora;
    private MuBitacora muBitacoraObj;

    private String direccionIp;
    private String usuarioSelect;
    private List<SelectItem> selectItemsUsuarios;
    private Date fechainc;
    private Date fechafin;

    private Map<String, String> validateParameter;

    @PostConstruct
    public void init() {
        try {
            loadBitacora();
            cargarListUsuarios();
            direccionIp = "";
            usuarioSelect = "";
            fechainc = null;
            fechafin = null;
            validateParameter = beanApplicationContext.getMapValidateParameters();
            if (listBitacora != null && !listBitacora.isEmpty()) {
                String separador = beanApplicationContext.getFormParameters().get(EnumParametros.SEPARADOR_DIRRECCION_IP.toString());
                for (MuBitacora item : listBitacora) {
                    if (item != null && item.getDireccionIp() != null) {
                        item.setDireccionIp(item.getDireccionIp().replace(".", separador));
                    }
                }
            }
        } catch (Exception e) {
            log.error("[init] Fallo al iniciar el bean.", e.getCause());
        }
    }

    private void loadBitacora() {
        listBitacora = logBl.listBitacora(muBitacoraObj, fechainc, fechafin);
    }

    private void cargarListUsuarios() {
        try {
            selectItemsUsuarios = new ArrayList<>();
            selectItemsUsuarios.add(new SelectItem("", "Todo"));
            List<MuUsuario> listaUser = usuarioBl.getUsuario();
            for (MuUsuario user : listaUser) {
                SelectItem sel = new SelectItem(user.getLogin(), user.getLogin());
                selectItemsUsuarios.add(sel);
            }
        } catch (Exception e) {
            log.error("Error al cargar lista de Usuarios: ", e);
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al cargar lista de Usuarios: " + e.getMessage(), "");
        }
    }

    public List<MuBitacora> getListBitacora() {
        return listBitacora;
    }

    public void setListBitacora(List<MuBitacora> listBitacora) {
        this.listBitacora = listBitacora;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    public String getUsuarioSelect() {
        return usuarioSelect;
    }

    public void setUsuarioSelect(String usuarioSelect) {
        this.usuarioSelect = usuarioSelect;
    }

    public List<SelectItem> getSelectItemsUsuarios() {
        return selectItemsUsuarios;
    }

    public void setSelectItemsUsuarios(List<SelectItem> selectItemsUsuarios) {
        this.selectItemsUsuarios = selectItemsUsuarios;
    }

    public Date getFechainc() {
        return fechainc;
    }

    public void setFechainc(Date fechainc) {
        this.fechainc = fechainc;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public Map<String, String> getvalidateParameter() {
        return validateParameter;
    }

    public void setvalidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public void buscar() {
        muBitacoraObj = new MuBitacora();
        if (getUsuarioSelect() == null) {

            setUsuarioSelect("");
        }
        muBitacoraObj.setUsuario(getUsuarioSelect());
        muBitacoraObj.setDireccionIp(direccionIp);
        loadBitacora();
    }

    public void limpiar() {
        direccionIp = "";
        usuarioSelect = "";
        fechainc = null;
        fechafin = null;
        listBitacora = new ArrayList<>();
    }

    public MuBitacora getMuBitacoraObj() {
        return muBitacoraObj;
    }

    public void setMuBitacoraObj(MuBitacora muBitacoraObj) {
        this.muBitacoraObj = muBitacoraObj;
    }
}
