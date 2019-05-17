package myapps.user.filter;

import myapps.servicio_basico.util.UtilUrl;
import myapps.user.controler.ControlTimeOutImpl;
import myapps.user.model.Nodo;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class LoginFilter implements Filter {

    public static final Logger log = Logger.getLogger(LoginFilter.class);

    private FilterConfig filterConfig = null;

    private static final String AJAX_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"%s\"></redirect></partial-response>";

    private static String pathRaiz = "";
    private static String pathLogin = "";
    private static String rederingMenu = "";
    private static String rederingError = "";

    @Inject
    private ControlTimeOutImpl controlTimeOut;
    
    public LoginFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String context = filterConfig.getServletContext().getContextPath();
        log.debug("Contexto WEB:" + context);
        LoginFilter.pathRaiz = context + "/";
        LoginFilter.pathLogin = context + "login.xhtml";
        LoginFilter.rederingMenu = context + "menu.xhtml";
        LoginFilter.rederingError = context + "error.xhtml";
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpSession session = ((HttpServletRequest) request).getSession();
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String path = httpRequest.getRequestURI();
             log.debug("SESSION TIME: " + session.getMaxInactiveInterval());         
             log.debug("path: " + path);
             log.debug("req.getRequestedSessionId: " + httpRequest.getRequestedSessionId() + ", req.isRequestedSessionIdValid: " + httpRequest.isRequestedSessionIdValid());

            if (httpRequest.getRequestedSessionId() != null && !httpRequest.isRequestedSessionIdValid()) {
            	log.debug("sesion no valida se va a redireccionar path: " + path);            	                       
                long timeMax = (long) session.getMaxInactiveInterval() * (long) 1000;
                controlTimeOut.registerOutTime(timeMax);

                session.invalidate();
                httpRequest.getSession(true);

                boolean ajaxRequest = "partial/ajax".equals(httpRequest.getHeader("Faces-Request"));
                log.debug("______________ ajaxRequest: " + ajaxRequest);

                if (ajaxRequest) {
                    response.setContentType("text/xml");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().printf(AJAX_REDIRECT_XML, pathLogin);
                    // So, return special XML response instructing JSF ajax to send a redirect.
                } else {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.sendRedirect(pathLogin);
                    // So, just perform standard synchronous redirect.
                }
                return;
            }

            String usuario = (String) httpRequest.getSession().getAttribute("TEMP$USER_NAME");
            log.debug("usuario: " + usuario);

            Cookie cookie = new Cookie("JSESSIONID", httpRequest.getSession().getId());
            cookie.setDomain(pathRaiz);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            HttpServletResponse respon = (HttpServletResponse) response;
            respon.addCookie(cookie);
                                  
            if (path.equals(pathRaiz)) {
                if (usuario == null) {
                     log.debug("-----------------------1");
                    chain.doFilter(request, response);
                } else {
                     log.debug("-----------------------2");
                    HttpServletResponse hres = (HttpServletResponse) response;
                    hres.sendRedirect(rederingMenu);
                }
                return;
            }
                       
            if (path.equals(pathLogin)) {          
                if (request.getContentLength() != -1 && estanParametros(httpRequest.getParameterMap()) ) {
                    log.debug("-----------------------3");
                    Map<String, Object> mapValueNames = new HashMap<String, Object>();
                    Enumeration<String> nombres = session.getAttributeNames();

                    for (String valueName : Collections.list(nombres)) {
                        mapValueNames.put(valueName, session.getAttribute(valueName));
                    }
                    session.setAttribute("TEMP$ACTION_MESSAGE_ID", "");
                    session.setAttribute("TEMP$USER_NAME", "");
                    session.setAttribute("TEMP$GROUP", "");
                    session.setAttribute("TEMP$IP_CLIENT", "");
                    session.invalidate();
                    session = httpRequest.getSession(true);
                    List<String> listKeys = new ArrayList<String>(mapValueNames.keySet());
                    for (String key : listKeys) {
                        session.setAttribute(key, mapValueNames.get(key));
                    }
                    chain.doFilter(request, response);

                } else {
                    log.debug("-----------------------4");
                    HttpServletResponse hres = (HttpServletResponse) response;
                    hres.sendRedirect(pathRaiz);
                }
                return;
            }

            if (usuario != null) {
                 log.debug("-----------------------5");
                // String ipCliente = request.getRemoteAddr();
                String ipClient = UtilUrl.getClientIp(httpRequest);
                //log.debug("ip cliente: " + ipClient);

                // String addressUser = controlTimeOut.getAddressIP(usuario);
                String addressUser = controlTimeOut.getAddressIP2(usuario);

                // log.debug("ipCliente: " + ipClient + ", addressUser: " + addressUser);

                if (!ipClient.equals(addressUser) || addressUser.isEmpty()) {
                     log.debug("-----------------------5 A");
                    session.setAttribute("TEMP$USER_NAME", "");
                    session.setAttribute("TEMP$GROUP", "");
                    session.setAttribute("TEMP$IP_CLIENT", "");
                    session.invalidate();
                    HttpServletResponse hres = (HttpServletResponse) response;
                    hres.sendRedirect(pathRaiz);
                } else {
                     log.debug("-----------------------5 B");
                    if (path.equals(rederingMenu)) {
                        chain.doFilter(request, response);
                        return;
                    }

                    if (path.equals(rederingError)) {
                        chain.doFilter(request, response);
                        return;
                    }

                    // NodoClient nd = controlTimeOut.getNodoClient(usuario);
                    String pageRequest = path;
                    int k = pageRequest.lastIndexOf("/");
                    String strPg = pageRequest.substring(k + 1);

                    Nodo nd = controlTimeOut.getNodoClient2(usuario);

                    // if (nd != null && nd.existeUrl(strPg)) {
                    if (nd != null && nd.getUrls() != null && nd.getUrls().contains((strPg))) {
                        log.debug("-----------------------5 B1");
                        long tp = session.getLastAccessedTime();
                        controlTimeOut.setDatos2(nd, tp);
                        chain.doFilter(request, response);
                    } else {
                         log.debug("-----------------------5 B2");
                        HttpServletResponse hres = (HttpServletResponse) response;
                        hres.sendRedirect(rederingError);
                    }
                }

            } else {
            	 log.debug("-----------------------6");
                long timeMax = (long) session.getMaxInactiveInterval() * (long) 1000;
                controlTimeOut.registerOutTime(timeMax);
                session.setAttribute("TEMP$ACTION_MESSAGE_ID", "");
                session.setAttribute("TEMP$USER_NAME", "");
                session.setAttribute("TEMP$GROUP", "");
                session.setAttribute("TEMP$IP_CLIENT", "");
                session.invalidate();
                HttpServletResponse hres = (HttpServletResponse) response;
                hres.sendRedirect(pathRaiz);
            }
        } catch (ServletException se) {

            se.getMessage();
        }catch (IOException io){
            io.getMessage();
        }
    }

    private boolean estanParametros(Map<String, String[]> map) {
        return map != null && map.size() == 9 && valP(map.get("formLogin:usernameId")) && valP(map.get("formLogin:ihIp")) && valP(map.get("formLogin:passwordId")) && valP(map.get("formLogin")) && valP(map.get("formLogin:comanLogin")) && valP(map.get("javax.faces.ViewState"));        
    }

    private boolean valP(String[] param) {    	
        if (param != null) {
            if (param.length == 1) {
                String pp = param[0];
                return pp != null;// && !pp.trim().isEmpty();
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("LoginFilter()");
        }
        StringBuffer sb = new StringBuffer("LoginFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
