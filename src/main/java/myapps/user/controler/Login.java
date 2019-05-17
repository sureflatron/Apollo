package myapps.user.controler;

import myapps.servicio_basico.bean.model.Nota;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.servicio_basico.util.UtilDate;
import myapps.servicio_basico.util.UtilUrl;
import myapps.user.dao.FormularioDAO;
import myapps.user.dao.NodoDAO;
import myapps.user.ldap.ActiveDirectory;
import myapps.user.model.MuFormulario;
import myapps.user.model.Nodo;
import org.apache.log4j.Logger;
import org.primefaces.model.menu.*;
import org.primefaces.util.Base64;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.*;

@ManagedBean(name = "login")

@SessionScoped
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Logger log = Logger.getLogger(Login.class);

    @Inject
    ControlLoginImpl controlLogin;

    @Inject
    private FormularioDAO daoFormulario;

    @Inject
    private NodoDAO nodoDao;

    @EJB
    private IApplicationContext beanApplicationContext;

    @EJB
    private ActiveDirectory activeDirectory;

    private List<Nota> listNota;
    private Nota notaSeleccionada;

    private String userId;

    private String password;
    private String idSession;

    private MenuModel model;

    private String ipClient;
    private String ipServer;
    private String user;
    private String date;
    private boolean collapsed;

    private String urlServlet;
    private String urlServletVolver;

    @PostConstruct
    public void init() {
        collapsed = false;
    }

    public Login() {
        this.userId = "";
        this.password = "";
        listNota = new ArrayList<>();
        listNota.add(new Nota(1, "Teléfono", ""));
        listNota.add(new Nota(2, "Código Tigo Star", ""));
        listNota.add(new Nota(3, "Nro Documento", ""));
        listNota.add(new Nota(4, "Nombre(s)", ""));
        listNota.add(new Nota(5, "Apellido", ""));
        getIps();
    }

    public String logout() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession sesion = request.getSession();
        sesion.setAttribute("TEMP$ACTION_MESSAGE_ID", "");
        sesion.setAttribute("TEMP$USER_NAME", "");
        sesion.setAttribute("TEMP$GROUP", "");
        sesion.invalidate();
        return "/";
    }

    public void getIps() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipServer = ip.getHostAddress();

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            ipClient = UtilUrl.getClientIp(request);

            user = request.getSession().getAttribute("TEMP$USER_NAME") != null ? request.getSession().getAttribute("TEMP$USER_NAME") + "" : "nulo";

            setDate(UtilDate.dateToString(new Date(), "dd-MM-yyyy HH:mm:ss"));

            log.debug("IPS: ipClient: " + ipClient + ", ipServer: " + ipServer);

            // ------------------------------------------------------------------
            String scheme = "http";
            if (request.getScheme() != null && request.getScheme().toLowerCase().trim().equals("https")) {
                scheme = "https";
            }

            log.info("request.getLocalAddr: " + request.getLocalAddr());
            log.info("request.getServerName: " + request.getServerName());
            log.info("InetAddress.getLocalHost().getHostAddress: " + InetAddress.getLocalHost().getHostAddress());

            String ipNodo = request.getLocalAddr();
            if (ipNodo == null || ipNodo.equals("127.0.0.1") || ipNodo.trim().isEmpty()) {
                ipNodo = request.getServerName();
                if (ipNodo == null || ipNodo.equals("127.0.0.1") || ipNodo.trim().isEmpty()) {
                    ipNodo = InetAddress.getLocalHost().getHostAddress();
                }
            }

            urlServlet = scheme + "://" + ipNodo + ":" + request.getLocalPort() + request.getContextPath() + "/MyServlet";
            urlServletVolver = scheme + "://" + ipNodo + ":" + request.getLocalPort() + request.getContextPath() + "/Certificado";

            log.debug("urlServlet: " + urlServlet);
            log.debug("urlServletVolver: " + urlServletVolver);

            urlServlet = encriptUrl(urlServlet);
            urlServletVolver = encriptUrl(urlServletVolver);

            // ------------------------------------------------------------------

            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                // System.out.println(name + " : " + request.getHeader(name));
                log.debug("VARIABLE CABECERA " + name + ": " + request.getHeader(name));
            }

        } catch (Exception e) {
            log.error("Error al obtener ips: ", e);
        }
    }

    public void liberar(String usuario, boolean logout) {
        try {
            if (logout) {
                log.debug("Liberando conexiones para el usuario: " + usuario + " por logout");
            } else {
                log.debug("Liberando conexiones para el usuario: " + usuario + " por inicio de sesion");
            }
        } catch (Exception e) {
            log.error("Error al liberar conexiones de los hashmap: ", e);
        }
    }

    public String encriptUrl(String url) {
        if (url.contains("https")) {
            url = url.replace("https", "A4853A");
        } else {
            url = url.replace("http", "A48A");
        }
        url = url.replace("/", "A2FA");
        url = url.replace(".", "A2EA");
        url = url.replace(":", "A3AA");
        return url;
    }

    public void logoutSimple() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
            // String address = request.getRemoteAddr();
            liberar(strIdUs, true);
            String address = ipClient;
            controlLogin.salirBitacora(strIdUs, address);

            Nodo nodo = nodoDao.getNodoLogin(userId);
            if (nodo != null) {
                nodoDao.remove(nodo);
            } else {
                log.warn("Usuario no encontrado para logout: " + userId);
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/Logout");
        } catch (Exception e) {
            log.error("Error al cerrar sesion: ", e);
        }
    }

    public void addNota() {
        listNota.add(new Nota((new Date()).getTime(), "", ""));
    }

    public void removeNota() {
        if (notaSeleccionada != null) {
            log.debug("nota eliminada: " + notaSeleccionada.toString());
            listNota.remove(notaSeleccionada);
            notaSeleccionada = null;
        } else {
            log.debug("nota null");
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String verifyLogin() {
        log.debug("iniciando sesion por login: " + ipClient);
        try {
            String cuenta = userId.toLowerCase();
            log.info("userId: " + userId + ", password: " + password + ", ipClient: " + ipClient);
            try {
                ipClient = decryptBase64(ipClient);
            } catch (Exception e) {
                log.warn("ErrorIpDecoder: ", e);
            }

            Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                log.debug("Clave: " + entry.getKey() + ": " + entry.getValue());
            }

            // ---------------------------------------------------------------------
            if (ipClient == null || ipClient.trim().isEmpty() || ipClient.equals("ip")) {
                ipClient = InetAddress.getLocalHost().getHostAddress();
                if (ipClient == null || ipClient.trim().isEmpty() || ipClient.equals("ip")) {
                    SysMessage.error(beanApplicationContext.getFormParameters().get(EnumParametros.MENSAJE_VALIDACION_IP.toString()), null);
                    log.debug("userId: " + userId + ", ipClient: " + ipClient);
                    return "";
                }
            }

            ipClient = ipClient.trim();
            log.debug("userId: " + userId + ", ipClient: " + ipClient);
            // ---------------------------------------------------------------------

            String validacion = controlLogin.validateIngreso(cuenta, password);
            if (!validacion.isEmpty()) {
                SysMessage.error(validacion, null);
                return "";
            }

            boolean irActive = Boolean.parseBoolean(beanApplicationContext.getFormParameters().get(EnumParametros.COSULTAR_DIRECTORIO_ACTIVE.toString()));
            int valid = 0;

            if (irActive) {
                valid = activeDirectory.validarUsuario(cuenta, password);
            } else {
                valid = controlLogin.existUser(cuenta, password);
            }

            if (valid == ActiveDirectory.EXIT_USER) {

                List groups = null;
                if (irActive) {
                    groups = activeDirectory.getListaGrupos(cuenta);
                    // groups = controlLogin.getListaGrupo();
                } else {
                    groups = controlLogin.getListaGrupo();
                }

                if (groups != null && groups.size() > 0) {
                    long idRol = controlLogin.getIdRole(cuenta, groups);
                    log.debug("[usuario: " + userId + ", rolId:" + idRol + "] [Se obtuvo un rol]");

                    if (idRol > 0) {
                        //if (controlLogin.verificarIp(idRol, ipClient)) {

                        log.debug("[usuario: " + userId + ", rolId:" + idRol + "] login exitoso");
                        userId = userId.toLowerCase();
                        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                        request.getSession(false);
                        if (request.getSession().getAttribute("TEMP$USER_NAME") != null) {
                            String user = (String) request.getSession().getAttribute("TEMP$USER_NAME");
                            if (user.equals(userId)) {
                                log.debug("Ininico Session: " + user);
                                return "/dashboard.xhtml?faces-redirect=true";
                            }
                        }
                        request.getSession().setAttribute("TEMP$USER_NAME", userId);
                        request.getSession().setAttribute("TEMP$IP_CLIENT", ipClient.trim());
                        request.getSession().setAttribute("TEMP$IDROL", idRol);
                        user = userId;
                        HttpServletRequest httpRequest = (HttpServletRequest) request;
                        idSession = httpRequest.getRequestedSessionId();
                        // remoteAddr = remoteAddr.replaceAll("\\.", "_");
                        controlLogin.addBitacoraLogin(userId, ipClient, idRol);
                        // controlLogin.reiniciarReintentos(userId);

                        cargarv2(idRol);

                        liberar(userId, false);
                        return "/dashboard.xhtml?faces-redirect=true";
						/*} else {
							log.debug("[usuario: " + userId + ", rolId:" + idRol + "] login insatisfactorio por validacion de ip-rol");
							// SysMessage.error("Su equipo no esta registrado para el Rol Asignado", null);
							// return "";
							validacion = controlLogin.controlErrorRol(cuenta, ipClient);
							SysMessage.error(validacion, null);
							return "";
						}*/
                    } else {
                        log.debug("[usuario: " + userId + " usuario no registrado para ningun rol");
                        SysMessage.error("Usted no esta registrado para ningun Rol", null);
                        return "";
                    }
                } else {
                    log.debug("[usuario: " + userId + " usuario no tiene grupo de trabajo");
                    SysMessage.error("Usted no tiene grupo de trabajo", null);
                    return "";
                }
            } else {
                log.debug("[usuario: " + userId + " usuario no existe en el directorio activo");
                validacion = controlLogin.controlError(cuenta, ipClient);
                SysMessage.error(validacion, null);
                return "";
            }
        } catch (Exception e) {
            log.error("Error al iniciar sesion: ", e);
            SysMessage.error("Error al iniciar sesión: " + e.getMessage(), null);
            return "";
        }
    }

    private void cargarv2(long roleId) {
        model = new DefaultMenuModel();
        List<MuFormulario> lform = daoFormulario.findPadres(roleId);
        for (MuFormulario form : lform) {
            model.addElement(cargarv2(form, roleId));
        }

        DefaultSubMenu submenu = new DefaultSubMenu();
        submenu.setLabel("Opciones");

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String contextPath = request.getContextPath();

        DefaultMenuItem manualUsuario = new DefaultMenuItem();
        manualUsuario.setValue("Manual de Usuario");
        String pathManualUsuario = contextPath + "/resources/MU_CBS.pdf";
        String urlManualUsuario = "window.open('" + pathManualUsuario + "'); return false;";
        manualUsuario.setOnclick(urlManualUsuario);
        manualUsuario.setIcon("ui-icon ui-icon-document");

        submenu.addElement(manualUsuario);

        model.addElement(submenu);
    }

    private MenuElement cargarv2(MuFormulario form, long roleId) {
        List<MuFormulario> lHijos = daoFormulario.findHijos(form.getId(), roleId);
        if (lHijos != null && !lHijos.isEmpty()) {
            DefaultSubMenu submenu = new DefaultSubMenu();
            submenu.setLabel(form.getNombre());
            for (MuFormulario hijo : lHijos) {
                // submenu.getChildren().add(cargarv2(hijo, roleId));
                submenu.addElement(cargarv2(hijo, roleId));
            }
            return submenu;
        } else {
            DefaultMenuItem item = new DefaultMenuItem();
            item.setValue(form.getNombre());

            item.setUrl(form.getUrl());
            //item.setOnclick("menuClick(this)");
            item.setStyleClass("pintar");
            item.setId(String.valueOf(form.getId()));
            item.setIcon("fa fa-external-link");

            return item;
        }
    }


    public String getTituloAplicacion() {
        return beanApplicationContext.getFormParameters().get(EnumParametros.TITULO_APLICACION.toString());
    }

    public String getPiePagina() {
        return beanApplicationContext.getFormParameters().get(EnumParametros.TITULO_PIE_PAGINA.toString());
    }

    private String decryptBase64(String valor) throws Exception {
        if (valor != null) {
            byte[] str = Base64.decode(valor);
            if (str != null)
                valor = new String(str, "UTF-8");
        }
        return valor;
    }

    public MenuModel getModel() {
        return model;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Nota> getListNota() {
        return listNota;
    }

    public void setListNota(List<Nota> listNota) {
        this.listNota = listNota;
    }

    public Nota getNotaSeleccionada() {
        return notaSeleccionada;
    }

    public void setNotaSeleccionada(Nota notaSeleccionada) {
        this.notaSeleccionada = notaSeleccionada;
    }

    public String getIpClient() {
        return ipClient;
    }

    public void setIpClient(String ipClient) {
        this.ipClient = ipClient;
    }

    public String getIpServer() {
        return ipServer;
    }

    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public String getUrlServlet() {
        return urlServlet;
    }

    public void setUrlServlet(String urlServlet) {
        this.urlServlet = urlServlet;
    }

    public String getUrlServletVolver() {
        return urlServletVolver;
    }

    public void setUrlServletVolver(String urlServletVolver) {
        this.urlServletVolver = urlServletVolver;
    }

    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public String senderMenu() {
        return "/view/menu.xhtml?faces-redirect=true";
    }

}
