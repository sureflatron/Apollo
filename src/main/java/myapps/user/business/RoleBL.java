package myapps.user.business;

import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.UtilFile;
import myapps.user.dao.FormDAO;
import myapps.user.dao.GrupoAdDAO;
import myapps.user.dao.RoleDAO;
import myapps.user.dao.UsuarioDAO;
import myapps.user.model.*;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

/*import myapps.servicio_basico.dao.MasterDao;*/
/*import myapps.user.model.MuRolIp;*/

@Named
@Stateless
public class RoleBL implements Serializable {

    private static final long serialVersionUID = 1L;

    /* @Inject
     private MasterDao masterDao;
     */
    @Inject
    private RoleDAO dao;

    @Inject
    private FormDAO formDao;

    @Inject
    private GrupoAdDAO grupoDao;

    @Inject
    private UsuarioDAO usuarioDao;

    @EJB
    private IApplicationContext beanApplicationContext;

    private static Logger log = Logger.getLogger(RoleBL.class);

    public String validate(MuRol role, String idStr) {
        String resultado = "";
        if (role != null) {
            /*if (role.getNombre() != null && role.getNombre().trim().isEmpty()) {
                return "El campo Nombre es requerido";
            }

            if (role.getNombre() != null && !UtilUrl.esTextoValido(role
                    .getNombre(), beanApplicationContext.getMapValidateParameters()
                    .get(EnumParametros.EXPRESION_REGULAR_TEXTO_NORMAL.toString()))) {
            	
            	
                return "Campo Nombre no valido: " + beanApplicationContext.getMapValidateParameters().get(EnumParametros
                                       .EXPRESION_REGULAR_TEXTO_NORMAL.toString());
            }

            if (role.getDescripcion() != null && !role.getDescripcion().trim().isEmpty()) {
                if (!UtilUrl.esTextoValido(role.getDescripcion(),
                        beanApplicationContext.getMapValidateParameters().get
                                (EnumParametros.EXPRESION_REGULAR_TEXTO_NORMAL.toString()))) {
                    return "El campo Descripcion no es valido: "+
                            beanApplicationContext.getMapValidateParameters()
                            .get(EnumParametros.EXPRESION_REGULAR_TEXTO_NORMAL.toString());
                }
            }*/

            MuRol rolAux = dao.getName(role.getNombre());
            if (rolAux == null) {
                resultado = "";
            } else if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                if (id == rolAux.getRolId() && role.getNombre().equals(rolAux.getNombre())) {
                    log.info(role.getRolId() + " " + role.getNombre() + " " + role.getDescripcion());
                }
            } else {
                resultado = "Este nombre existe";
            }
        } else {
            resultado = "Rol nulo para validar";
        }
        return resultado;
    }

    public void saveRole(MuRol role) throws Exception {
        role.setEstado(true);
        dao.save(role);
        //masterDao.save(role);
        List<MuFormulario> lista = formDao.getList();

        for (MuFormulario formulario : lista) {
            MuRolFormularioPK rfk = new MuRolFormularioPK();
            rfk.setRolId(role.getRolId());
            rfk.setFormularioId(formulario.getId());
            MuRolFormulario rolFor = new MuRolFormulario();
            rolFor.setEstado(true);
            rolFor.setId(rfk);
            dao.saveRolForulario(rolFor);
            // masterDao.save(rolFor);
        }
        log.info("Rol guardado correctamente");
    }

    public void updateRole(MuRol role) throws Exception {
        MuRol roleAux = dao.get(role.getRolId());
        roleAux.setNombre(role.getNombre());
        roleAux.setDescripcion(role.getDescripcion());
        roleAux.setValidaIp(role.getValidaIp());
        dao.update(roleAux);
    }

    public void updateRoleFormulario(MuRolFormulario roleForm) throws Exception {
        dao.updateRolForulario(roleForm);
    }

    public void deleteRole(long idRole) throws Exception {

        List<MuGrupoAd> listGrupo = grupoDao.getList(idRole);
        for (MuGrupoAd g : listGrupo) {
            g.setEstado(false);
            grupoDao.update(g);
        }

        List<MuUsuario> listUser = usuarioDao.getList(idRole);
        for (MuUsuario u : listUser) {
            u.setEstado(false);
            usuarioDao.update(u);
        }

        /*List<MuRolIp> listIps = dao.getRolIps(idRole);
        for (MuRolIp i : listIps) {
            i.setEstado(false);
            dao.updateRolIp(i);
        }*/

        List<MuRolFormulario> listRolForm = dao.getRolFormularioDelete(idRole);
        for (MuRolFormulario rf : listRolForm) {
            rf.setEstado(false);
            dao.updateRolFormulario(rf);
        }

        MuRol rol = dao.get(idRole);
        rol.setEstado(false);
        dao.update(rol);
    }

    public void deleteRolFormulario(long rolId) throws Exception {
        dao.deleteRolFormulario(rolId);
    }

    // Ips
    /*public void saveRolIp(MuRolIp roleIp) throws Exception {
        roleIp.setEstado(true);
        roleIp.setFechaCreacion(Calendar.getInstance());
        //masterDao.save(roleIp);
        dao.saveRolIp(roleIp);
    }

    public void updateRolIp(MuRolIp roleIp) throws Exception {
        MuRolIp roleIpAux = dao.getRolIp(roleIp.getIpId());
        roleIpAux.setIp(roleIp.getIp());
        dao.updateRolIp(roleIpAux);
    }*/

    public void deleteRolIp(long idRol) throws Exception {
        dao.deleteRolIp(idRol);
    }

    /*public List<MuRolIp> getRolIp(int idRole) {
        return dao.getRolIps(idRole);
    }

    public boolean verificarIp(long idRole, String ipAdress) {
        if (dao.get(idRole).getValidaIp()) {
            MuRolIp rolIp = dao.getIp(ipAdress, idRole);
            return rolIp != null;
        }

        return true;
    }*/

    /*public String validateIp(MuRolIp roleIp, String idStr, boolean validaDatos) {
        log.debug("[validate]: Ingresando..");

        if (roleIp.getIp() == null || roleIp.getIp().isEmpty()) {
            return "El campo ip es requerido";
        }
        if (!validate(roleIp.getIp())) {
            return "La ip " + roleIp.getIp() + " no es valida";
        }
        if (validaDatos) {
            MuRolIp rolIpAux = dao.getIp(roleIp.getIp(), roleIp.getMuRol().getRolId());
            if (rolIpAux == null)
                return "";

            if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                if (id == rolIpAux.getIpId()) {
                    if (roleIp.getIp().equals(rolIpAux.getIp()))
                        return "";
                }

            }
            return "La ip " + roleIp.getIp() + " existe";
        }
        return "";
    }*/

    public boolean validate(final String ip) {
        Pattern pattern = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        return pattern.matcher(ip).matches();
    }

    /**
     * Verifica que la dirección IP es una dirección privada válida.
     *
     * @param ip Cadena de texto con la dirección que se quiere validar.
     * @return true si es una dirección válida, false en otro caso.
     */
//	public boolean verificaHost(final String ip)  
//	{  
//	  /* patrón para los números de .0 a .255 */  
//	  String n = "\\.(([1-9]?[0-9])|(1[0-9]{2})|(2([0-4][0-9]|5[0-5])))";  
//	  
//	  /* patrón para los números de .16 a .31 */  
//	  String m = "\\.((1[6-9])|(2[0-9])|(3[01]))";  
//	  
//	  /* 192.168.0.0 a 192.168.255.255 */  
//	  if(ip.matches("^192\\.168(" + n + "){2}$"))  
//	      return true;  
//	  
//	  /* 10.0.0.0 a 10.255.255.255 */  
//	  else if(ip.matches("^10(" + n + "){3}$"))  
//	      return true;
//	  
//	  /* 172.16.0.0 a 172.31.255.255 */  
//	  else if(ip.matches("^172" + m + "(" + n + "){2}$"))  
//	      return true;
//	  
//	  /* la dirección no coincidió con ninguna de las anteriores */
//	  return false;
//	}


    // *****************
    public List<MuRol> getRoles() {
        return dao.getList();
    }

    public MuRol getRole(int idRole) {
        return dao.get(idRole);
    }

    public List<MuRolFormulario> getRolFormulario(long id) {
        return dao.getRolFormulario(id);
    }

    public void updateRoleFormularioList(List<String> listaAvil, int idRol) {
        // try {
        //
        // List<RolFormulario> listRolFor = getRolFormulario(idRol);
        //
        // if (listRolFor != null) {
        // Map<Integer, RolFormulario> mapRolForIdForm = new HashMap<Integer,
        // RolFormulario>();
        // Map<String, RolFormulario> mapRolForName = new HashMap<String,
        // RolFormulario>();
        // Map<String, RolFormulario> mapRolForNivel = new HashMap<String,
        // RolFormulario>();
        //
        // List<RolFormulario> listRFAux = new ArrayList<RolFormulario>();
        // List<RolFormulario> listRFDepende = new ArrayList<RolFormulario>();
        //
        // for (RolFormulario rolFor : listRolFor) {
        // String str = rolFor.getFormulario().getNivel();
        //
        // if (str != null && !str.isEmpty()) {
        // mapRolForName.put(rolFor.getFormulario().getNombre(), rolFor);
        // mapRolForNivel.put(rolFor.getFormulario().getNivel(), rolFor);
        // listRFAux.add(0, rolFor);
        // } else
        // listRFDepende.add(rolFor);
        //
        // mapRolForIdForm.put(rolFor.getFormulario().getFormularioId(),
        // rolFor);
        // }
        //
        // for (RolFormulario rolFor : listRFAux) {
        //
        // String name = rolFor.getFormulario().getNombre();
        // if (listaAvil.indexOf(name) != -1) {
        //
        // RolFormulario rolForAux = mapRolForName.get(name);
        // String str = rolForAux.getFormulario().getNivel();
        // int k = str.lastIndexOf(".");
        // if (k != -1) {
        // String path = str.substring(0, k);
        // rolForAux = mapRolForNivel.get(path);
        // name = rolForAux.getFormulario().getNombre();
        // if (listaAvil.indexOf(name) == -1) {
        // listaAvil.add(name);
        // }
        // }
        // }
        // }
        //
        // try {
        //
        // for (RolFormulario rolFor : listRFAux) {
        // String name = rolFor.getFormulario().getNombre();
        // if (listaAvil.indexOf(name) != -1)
        // rolFor.setEstado(Boolean.TRUE);
        // else
        // rolFor.setEstado(Boolean.FALSE);
        //
        // updateRoleFormulario(rolFor);
        // }
        //
        // for (RolFormulario rolFor : listRFDepende) {
        //
        // RolFormulario rolForAux;
        // do {
        // rolForAux = mapRolForIdForm.get(rolFor.getFormulario().getDepende());
        // } while (rolForAux.getFormulario().getDepende() != 0);
        //
        // rolFor.setEstado(rolForAux.getEstado());
        // updateRoleFormulario(rolFor);
        // }
        //
        // } catch (Exception e) {
        // log.error("[updateRoleFormularioList] error al guardar los roles",
        // e);
        // }
        // } else {
        // log.error("[updateRoleFormularioList] Error al obtener la lista de Roles.");
        // }
        //
        // } catch (Exception e) {
        // log.error("[updateRoleFormularioList] error al guardar los roles",
        // e);
        // }

    }

    public byte[] rptAccesosRolDTOS(MuRol muRol) {
        byte[] byteExcel = new byte[0];
        try {
            List<Object[]> rptAccesosRolDtos;
            if (muRol != null) {
                rptAccesosRolDtos = dao.getAccesosRol(muRol);
            } else {
                rptAccesosRolDtos = dao.getAccesosRolAll();
            }

            byteExcel = UtilFile.writeExcel(rptAccesosRolDtos);

        } catch (Exception e) {
//
        }
        return byteExcel;
    }

}
