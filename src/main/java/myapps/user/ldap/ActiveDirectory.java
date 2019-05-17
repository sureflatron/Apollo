package myapps.user.ldap;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import java.util.*;

@Stateless
public class ActiveDirectory {

    public static final int EXIT_USER = 1;
    public static final int NOT_EXIT_USER = 2;
    public static final int ERROR = 3;

    @EJB
    private IApplicationContext beanApplicationContext;

    private static Logger log = Logger.getLogger(ActiveDirectory.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    public int validarUsuario(String usuario, String password) {
        int resultado;
        if (usuario == null || usuario.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            resultado = NOT_EXIT_USER;
        }

        Hashtable env = new Hashtable();
        try {
            env.put(Context.INITIAL_CONTEXT_FACTORY, beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_FACTORY_INITIAL.toString()));
            env.put(Context.PROVIDER_URL, beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_PROVIDER_URL.toString()));
            env.put(Context.SECURITY_AUTHENTICATION, beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_AUTHENTICATION.toString()));
            env.put(Context.SECURITY_PRINCIPAL, beanApplicationContext.getMapLdapParameters().get(EnumParametros.SECURITY_PRINCIPAL.toString()) + usuario);
            env.put(Context.SECURITY_CREDENTIALS, password);

        } catch (Exception e) {
            log.error("usuario: " + usuario + ", Fallo al establecer conexion con LDAP", e);
            resultado = ERROR;
        }

        try {
            InitialDirContext ctx = new InitialDirContext(env);
            ctx.close();
            resultado = EXIT_USER;
        } catch (Exception e) {
            log.error("usuario: " + usuario + ", Fallo al establecer conexion con LDAP", e);
            resultado = NOT_EXIT_USER;
        }
        return resultado;
    }

    @SuppressWarnings({"rawtypes", "unchecked", "unused"})
    public List<String> getListaGrupos(String usuario) {
        List<String> lGrupos = new ArrayList<>();
        InitialLdapContext ctx = null;
        try {
            Hashtable env = new Hashtable();
            env.put("java.naming.factory.initial", beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_FACTORY_INITIAL.toString()));
            env.put("java.naming.provider.url", beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_PROVIDER_URL.toString()));
            env.put("java.naming.security.authentication", beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_AUTHENTICATION.toString()));
            env.put("java.naming.security.principal", beanApplicationContext.getMapLdapParameters().get(EnumParametros.SECURITY_PRINCIPAL.toString()) + beanApplicationContext
                    .getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_USER.toString()));
            env.put("java.naming.security.credentials", beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_CREDENTIALS.toString()));

            ctx = new InitialLdapContext(env, null);
            String searchBase = beanApplicationContext.getMapLdapParameters().get(EnumParametros.SEARCH_BASE.toString()); /*"DC=tigo,DC=net,DC=bo";*/
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String returnAtts[] = {"memberOf"};
            String searchFilter = "(&(objectCategory=person)(objectClass=user)(mailNickname=" + usuario + "))";

            searchCtls.setReturningAttributes(returnAtts);

            NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
            int totalResults = 0;

            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                if (attrs != null) {
                    for (NamingEnumeration ne = attrs.getAll(); ne.hasMore(); ) {
                        Attribute attr = (Attribute) ne.next();
                        String grupo;
                        for (NamingEnumeration e = attr.getAll(); e.hasMore(); totalResults++) {
                            grupo = e.next().toString().trim();
                            grupo = grupo.substring(3, grupo.indexOf(",")).trim();
                            lGrupos.add(grupo);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("usuario: " + usuario + ", Error al obtener el listado de grupos", e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e2) {
                    log.warn("usuario: " + usuario + ", Fallo al cerrar el InitialLdapContext", e2);
                }
            }
        }
        return lGrupos;
    }

    public Map<String, String> getNombreCompleto(String usuario) {
        Map<String, String> mapa = new HashMap<>();

        try {
            ConexionLDAP conexion = new ConexionLDAP();
            conexion.setDominio(beanApplicationContext.getMapLdapParameters().get(EnumParametros.LDAP_DOMAIN.toString()));
            conexion.setUrl(beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_PROVIDER_URL.toString()));
            conexion.setUsuario(beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_USER.toString()));
            conexion.setClave(beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_CREDENTIALS.toString()));


            Ldap ld = new Ldap(conexion);

            String[] returnAtts = {Ldap.USER_NOMBRE_COMPLETO,
                    Ldap.USER_LOGIN,
                    Ldap.USER_EMAIL,
                    Ldap.USER_APELLIDO,
                    Ldap.USER_NOMBRE,
                    Ldap.USER_COD_EHUMANO};

            mapa = ld.obtenerDatos(usuario, returnAtts);

        } catch (Exception e) {
            log.error("usuario: " + usuario + ", Fallo al obtener el nombre completo del usuario", e);
        }
        return mapa;
    }

    public String getCorreo(String usuario) {
        String resultado = "";
        try {
            ConexionLDAP conexion = new ConexionLDAP();
            conexion.setDominio(beanApplicationContext.getMapLdapParameters().get(EnumParametros.LDAP_DOMAIN.toString()));
            conexion.setUrl(beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_PROVIDER_URL.toString()));
            conexion.setUsuario(beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_USER.toString()));
            conexion.setClave(beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_CREDENTIALS.toString()));

            Ldap ld = new Ldap(conexion);

            String[] returnAtts = {Ldap.USER_NOMBRE_COMPLETO,
                    Ldap.USER_LOGIN,
                    Ldap.USER_EMAIL,
                    Ldap.USER_APELLIDO,
                    Ldap.USER_NOMBRE,
                    Ldap.USER_COD_EHUMANO};

            Map<String, String> mapa = ld.obtenerDatos(usuario, returnAtts);
            if (mapa.isEmpty()) {
                resultado = "";
            } else {
                resultado = String.valueOf(mapa.get("mail")).trim();
            }
        } catch (Exception e) {
            log.error("usuario: " + usuario + ", Fallo al obtener el nombre completo del usuario", e);
        }
        return resultado;
    }


    public Map<String, String> getNEhumanoo(String usuario) {
        
        Map<String, String> mapa = new HashMap<>();
        try {
            ConexionLDAP conexion = new ConexionLDAP();
            conexion.setDominio(beanApplicationContext.getMapLdapParameters().get(EnumParametros.LDAP_DOMAIN.toString()));
            conexion.setUrl(beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_PROVIDER_URL.toString()));
            conexion.setUsuario(beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_USER.toString()));
            conexion.setClave(beanApplicationContext.getMapLdapParameters().get(EnumParametros.NAMING_SECURITY_CREDENTIALS.toString()));


            Ldap ld = new Ldap(conexion);

            String[] returnAtts = {Ldap.USER_NOMBRE_COMPLETO,
                    Ldap.USER_LOGIN,
                    Ldap.USER_EMAIL,
                    Ldap.USER_APELLIDO,
                    Ldap.USER_NOMBRE,
                    Ldap.USER_COD_EHUMANO};

            mapa = ld.obtenerDatosEh(usuario, returnAtts);

        } catch (Exception e) {
            log.error("usuario: " + usuario + ", Fallo al obtener el nombre completo del usuario", e);
        }
        return mapa;
    }

    @SuppressWarnings("rawtypes")
    public boolean validarGrupo(String grupo) {
        InitialDirContext dirC = null;
        NamingEnumeration answer = null;
        NamingEnumeration ae = null;

        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put("java.naming.factory.initial", beanApplicationContext
                    .getMapLdapParameters().get(EnumParametros
                            .NAMING_FACTORY_INITIAL.toString()));
            env.put("java.naming.provider.url", beanApplicationContext
                    .getMapLdapParameters().get(EnumParametros
                            .NAMING_PROVIDER_URL.toString()));
            env.put("java.naming.security.authentication",
                    beanApplicationContext.getMapLdapParameters().get
                            (EnumParametros.NAMING_SECURITY_AUTHENTICATION.toString()));
            env.put("java.naming.security.principal", beanApplicationContext
                    .getMapLdapParameters().get
                            (EnumParametros.SECURITY_PRINCIPAL.toString()) +
                    beanApplicationContext.getMapLdapParameters().get
                            (EnumParametros.NAMING_SECURITY_USER.toString()));
            env.put("java.naming.security.credentials",
                    beanApplicationContext.getMapLdapParameters().get
                            (EnumParametros.NAMING_SECURITY_CREDENTIALS.toString()));

            dirC = new InitialDirContext(env);
            if (dirC != null) {
                String searchBase = "DC=tigo,DC=net,DC=bo";
                SearchControls searchCtls = new SearchControls();
                searchCtls.setSearchScope(2);//
                String searchFilter = "(&(objectCategory=group)(!(groupType:1.2.840.113556.1.4.803:=2147483648)))";
                String[] returnAtts = {"cn"};
                searchCtls.setReturningAttributes(returnAtts);
                answer = dirC.search(searchBase, searchFilter, searchCtls);

                while (answer.hasMoreElements()) {
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes attrs = sr.getAttributes();
                    if (attrs != null) {
                        for (ae = attrs.getAll(); ae.hasMore(); ) {
                            Attribute attr = (Attribute) ae.next();
                            if (attr.get().toString().equals(grupo)) {
                                return true;
                            }
                        }

                    }

                }

            }

        } catch (Exception e) {
            log.error("grupo: " + grupo + ", Fallo al validar el grupo", e);
        } finally {
            try {
                if (dirC != null)
                    dirC.close();
            } catch (Exception e) {
                log.warn("grupo: " + grupo + ", Fallo al cerrar el contexto LDAP", e);
            }
        }
        return false;
    }

}