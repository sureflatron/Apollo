package myapps.user.controler;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.user.business.BitacoraBL;
import myapps.user.business.NodoBL;
import myapps.user.business.RoleBL;
import myapps.user.business.UsuarioBL;
import myapps.user.model.MuBitacora;
import myapps.user.model.MuRolFormulario;
import myapps.user.model.Nodo;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Named
@Stateless
public class ControlLoginImpl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private BitacoraBL logBL;

    @Inject
    private UsuarioBL userBL;

    @Inject
    private RoleBL roleBL;

    @Inject
    private NodoBL nodoBL;

    @Inject
    private ControlTimeOutImpl controlTimeOut;
    @EJB
    private IApplicationContext beanApplicationContext;


    public static final Logger log = Logger.getLogger(ControlLoginImpl.class);

    public String validateIngreso(String login, String passWord) {

        if (login == null)
            return "Usuario invalido!";

        if (login.trim().isEmpty())
            return "Usuario invalido!";

        if (passWord == null)
            return "Contrasena invalido!";

        if (passWord.trim().isEmpty())
            return "Contrasena invalido!";

        if (!Pattern.matches(beanApplicationContext.getMapValidateParameters().get(EnumParametros.EXPRESION_REGULAR_USUARIO.toString()), login)) {
            return "Usuario: " + beanApplicationContext.getMapValidateParameters().get(EnumParametros.MENSAJE_VALIDACION_USUARIO.toString());
        }

        if (!Pattern.matches(beanApplicationContext.getMapValidateParameters().get(EnumParametros.EXPRESION_REGULAR_PASSWORD.toString()), passWord)) {
            return "Password: " + beanApplicationContext.getMapValidateParameters().get(EnumParametros.MENSAJE_VALIDACION_PASSWORD.toString());
        }

        try {
            if (controlTimeOut != null) {
                Nodo nodo = controlTimeOut.exisUserIngreso2(login);
                // if (controlTimeOut.exisUserIngreso2(login)) {
                if (nodo != null) {
                    // log.debug("................ existe: " + login);
                    // int cont = controlTimeOut.countIntentoUserIngreso(login);
                    int cont = nodo.getCount();

                    log.debug("login: " + login + ", NRO_INTENTOS: " + beanApplicationContext.getFormParameters().get(EnumParametros.NRO_INENTO.toString()) + ", contador:" + cont);
                    // log.debug("nroOption == cont ::: " + nroOption + " == " + cont);
                    if (Integer.parseInt(beanApplicationContext.getFormParameters().get(EnumParametros.NRO_INENTO.toString())) == cont) {
                        // log.debug("Se va realizar el bloqueo por reintentos fallidos");
                        Date dateAct = new Date();
                        Calendar cal = Calendar.getInstance();

                        // NodoIngresoUser user = controlTimeOut.getNodoIngresoUser(login);
                        cal.setTime(nodo.getFecha().getTime());
                        cal.add(Calendar.MINUTE, Integer.parseInt(beanApplicationContext.getFormParameters().get(EnumParametros.TIEMPO_FUERA.toString())));
                        Date dateUser = cal.getTime();
                        if (dateAct.before(dateUser)) {
                            return "EL USUARIO " + '"' + login + '"' + " HA SIDO BLOQUEADO. VUELVA A INTENTAR EN " + beanApplicationContext.getFormParameters().get(EnumParametros.TIEMPO_FUERA.toString()) + " MINUTOS.";
                        } else {
                            nodo.setCount(0);
                            nodo.setFecha(Calendar.getInstance());
                        }
                    } else {
                        log.debug("No se va realizar el bloqueo");
                    }

                    nodoBL.update(nodo);

                } else {
                    log.debug("no existe el usuario: " + login + " en la tabla vc_nodo");
                }
            } else {
                throw new Exception("controlTimeOut is null");
            }
            return "";
        } catch (Exception e) {
            return "Error de obtener Login: " + e.getMessage();
        }
    }

    // public void reiniciarReintentos(String login) {
    // NodoIngresoUser user = controlTimeOut.getNodoIngresoUser(login);
    // if (user != null) {
    // user.setCount(0);
    // }
    // }

/*    public boolean verificarIp(long idRole, String ipAdress) {
        return roleBL.verificarIp(idRole, ipAdress);
    }*/

    public String controlError(String login, String ip) throws Exception {

        String str = "";
        Nodo nodo = controlTimeOut.exisUserIngreso2(login);
        // if (controlTimeOut.exisUserIngreso(login)) {
        if (nodo != null) {
            // controlTimeOut.ingrementIntentoUser(login);
            nodo.setCount(nodo.getCount() + 1);
            // NodoIngresoUser user = controlTimeOut.getNodoIngresoUser(login);
            int countOp = Integer.parseInt(beanApplicationContext.getFormParameters().get(EnumParametros.NRO_INENTO.toString()));
            // int cont = user.getCount();
            int cont = nodo.getCount();
            countOp = countOp - cont;
            nodo.setIp(ip);
            nodo.setFecha(Calendar.getInstance());

            if (countOp > 0)
                str = "EL USUARIO O CONTRASEÑA INGRESADOS SON INCORRECTOS";
            else {
                int nroTime = Integer.parseInt(beanApplicationContext.getFormParameters().get(EnumParametros.TIEMPO_FUERA.toString()));
                str = "EL USUARIO " + '"' + login + '"' + " HA SIDO BLOQUEADO.VUELVA A INTENTAR EN " + nroTime + " MINUTOS.";
            }
            nodoBL.update(nodo);

        } else {
            controlTimeOut.insertUserIngreso2(login, ip);
            str = "EL USUARIO O CONTRASEÑA INGRESADOS SON INCORRECTOS";
        }
        return str;
    }

/*    public String controlErrorRol(String login, String ip) throws Exception {

        String str = "";
        VcNodo nodo = controlTimeOut.exisUserIngreso2(login);
        // if (controlTimeOut.exisUserIngreso(login)) {
        if (nodo != null) {
            // controlTimeOut.ingrementIntentoUser(login);
            nodo.setCount(nodo.getCount() + 1);
            // NodoIngresoUser user = controlTimeOut.getNodoIngresoUser(login);
            int countOp = Integer.parseInt(beanApplicationContext.getFormParameters().get(EnumParametros.NRO_INENTO.toString()));
            // int cont = user.getCount();
            int cont = nodo.getCount();
            countOp = countOp - cont;
            nodo.setIp(ip);
            nodo.setFechaNow(Calendar.getInstance());

            if (countOp > 0)
                str = "Su Ip " + ip + " no esta registrada para el Rol Asignado";
            else {
                int nroTime = Integer.parseInt(beanApplicationContext.getFormParameters().get(EnumParametros.TIEMPO_FUERA.toString()));
                str = "EL USUARIO " + '"' + login + '"' + " HA SIDO BLOQUEADO.VUELVA A INTENTAR EN " + nroTime + " MINUTOS.";
            }
            nodoBL.update(nodo);

        } else {
            controlTimeOut.insertUserIngreso2(login, ip);
            str = "Su Ip " + ip + " no esta registrada para el Rol Asignado";
        }
        return str;
    }*/

    public long getIdRole(String userID, List<Object> userGroups) {
        return userBL.getIdRole(userID, userGroups);
    }

    public int existUser(String userId, String password) {

        String strLogin = beanApplicationContext.getFormParameters().get(EnumParametros.USUARIO.toString());

        /*
         * TODO DESCOMENTAR CUANDO SE TERMINE EL DESARROLLO
         * boolean swLogin = userId.indexOf(strLogin) != -1 ? true : false;
         *
         */

        boolean swLogin = strLogin.indexOf(userId) != -1 ? true : false;

        if (swLogin && password.equals(beanApplicationContext.getFormParameters().get(EnumParametros.PASSWORD.toString())))
            return 1;
        else
            return 2;
    }

    public void addBitacoraLogin(String strIdUs, String address, long idRol) throws Exception {

        MuBitacora bitacora = new MuBitacora();
        bitacora.setFormulario("INICIO");
        bitacora.setAccion("Ingreso al Sistema");
        bitacora.setUsuario(strIdUs);
        bitacora.setDireccionIp(address);
        bitacora.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        logBL.save(bitacora);

        List<MuRolFormulario> lista = roleBL.getRolFormulario(idRol);

        Nodo nodo = nodoBL.getNodoLogin(strIdUs);
        if (nodo != null) {
            nodoBL.remove(nodo);
        }

        Nodo vcNodo = new Nodo();
        vcNodo.setUsuario(strIdUs);
        vcNodo.setIp(address);
        vcNodo.setTime(new Date().getTime());
        vcNodo.setCount(0);

        // NodoClient nd = new NodoClient();
        // nd.setUser(strIdUs);
        // nd.setAddressIp(address);
        // nd.setTime(new Date().getTime());

        StringBuilder formularios = new StringBuilder("menu.xhtml");
        for (MuRolFormulario rolFormulario : lista) {
            // log.debug("[addBitacoraLogin] URL:" + rolFormulario.getMuFormulario().getUrl() + " estado:" + rolFormulario.getEstado());
            // nd.addUrl(rolFormulario.getMuFormulario().getUrl(), rolFormulario.getEstado());
            if (rolFormulario.getEstado() && rolFormulario.getMuFormulario().getUrl() != null) {
                formularios.append("," + rolFormulario.getMuFormulario().getUrl());
            }
        }
        // log.debug("formularios: " + formularios.toString());
        vcNodo.setUrls(formularios.toString());

        // nd.addUrl("Menu.xhtml", true);
        // nd.addUrl("menu.xhtml", true);
        // controlTimeOut.addNodoClient(nd);
        nodoBL.save(vcNodo);

    }

    public void salirBitacora(String strIdUs, String address) {

        MuBitacora logg = new MuBitacora();
        logg.setFormulario("");
        logg.setAccion("Salio del Sistema");
        logg.setUsuario(strIdUs);

        // String addr = controlTimeOut.getAddressIP(strIdUs);

        logg.setDireccionIp(address);
        logg.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        logBL.save(logg);
        // if (addr.equals(address))
        // controlTimeOut.remove2(strIdUs);

    }

    public List<String> getListaGrupo() {
        List<String> groups = new ArrayList<>();
        groups.add("Administradores");
        return groups;

    }

}
