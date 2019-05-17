package myapps.user.bean;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.business.RoleBL;
import myapps.user.dao.FormularioDAO;
import myapps.user.ldap.DescriptorBitacora;
import myapps.user.model.MuRol;
import myapps.user.model.MuRolFormulario;
import myapps.user.model.MuRolFormularioPK;
import myapps.user.model.RolModel;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@ManagedBean
@ViewScoped
public class RoleForm implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(RoleForm.class);

	@Inject
	private RoleBL roleBL;

	@Inject
	private ControlerBitacora controlerBitacora;

	@Inject
	private FormularioDAO daoFormulario;

	@EJB
	private IApplicationContext beanApplicationContext;

	private List<MuRol> listRole;
	private MuRol role = new MuRol();
	private String roleId;
	private boolean edit;
	//private List<MuRolIp> listRolIp;
	//private MuRolIp ipSeleccionada;
	//private MuRolIp rolIp = new MuRolIp();
	//private boolean editIp;
	private TreeNode roles;
	private TreeNode[] nodosSeleccionados;
	private boolean visibleDialog = false;
	private long rolId;

	private Map<String,String> validateParameter;
	private Map<String,String> emailParameter;
	private transient StreamedContent descarga;


	@PostConstruct
	public void init() {
		try {
			//editIp = false;
			cargarRoles(0);
			role = new MuRol();
			role.setValidaIp(false);
			//rolIp = new MuRolIp();
			// ipSeleccionada=new MuRolIp();
			//listRolIp = roleBL.getRolIp(0);
			listRole = roleBL.getRoles();
			validateParameter =  beanApplicationContext.getMapValidateParameters();
			emailParameter = beanApplicationContext.getMapEmailParameters();
		} catch (Exception e) {
			log.error("[init] Fallo en el init.", e);
		}
	}

	public void cargarRoles(long rolEditarId) {

		if (rolEditarId == 1) {
			SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()),"No se permite editar el Rol Administracion", "");
		} else {
			if (rolEditarId > 0) {
				visibleDialog = true;
				this.rolId = rolEditarId;
			}
			roles = new DefaultTreeNode(new RolModel(0, "", rolEditarId), null);
			roles.setExpanded(true);
			try {
				List<Object[]> lmenus = daoFormulario.findPadresPermisos(rolEditarId);
				for (Object[] formulario : lmenus) {
					cargarSubMenus(formulario, roles, rolEditarId);
				}
			} catch (Exception e) {
				log.error("[cargarRoles] Fallo al cargar los roles.", e);
			}
		}
	}
	public void donwloadAccesos(MuRol muRol){

		try{

			byte[] bytes=roleBL.rptAccesosRolDTOS(muRol);
			InputStream stream = new ByteArrayInputStream(bytes);
			descarga = new DefaultStreamedContent(stream, "application/xlsx",
					"AccesosRoles.xlsx");
		}catch (Exception e){
			log.error("[cargarRoles] Fallo al cargar los roles.", e);
		}

	}

	private void cargarSubMenus(Object[] formulario, TreeNode tree, long rolEditarId) {
		long idFormulario = ((BigDecimal) formulario[0]).longValue();
		String nombreFormulario = String.valueOf(formulario[1]);
		String permisoFormulario = String.valueOf(formulario[2]);
		List<Object[]> lHijos = daoFormulario.findHijosPermisos(idFormulario, rolEditarId);
		if (lHijos != null && !lHijos.isEmpty()) {
			TreeNode newtree = new DefaultTreeNode(new RolModel(idFormulario, nombreFormulario, rolEditarId), tree);
			for (Object[] ff : lHijos) {
				cargarSubMenus(ff, newtree, rolEditarId);
			}
			newtree.setSelected(permisoFormulario.trim().equals("1"));
			newtree.setExpanded(true);
		} else {
			TreeNode newtree = new DefaultTreeNode(new RolModel(idFormulario, nombreFormulario, rolEditarId), tree);
			newtree.setSelected(permisoFormulario.trim().equals("1"));
			newtree.setExpanded(true);
		}
	}

	public void guardarPermisos() {
		try {
			roleBL.deleteRolFormulario(rolId);
		} catch (Exception e) {
			log.error("[guardarPermisos] Fallo al intentar eliminar los permisos por formulario.", e);
		}

		for (TreeNode tn : nodosSeleccionados) {
			marcarRolFormulario(tn);
		}
		this.visibleDialog = false;
		 SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()),"Registro satisfactorio", "");
	}

	private void marcarRolFormulario(TreeNode tn) {
		if (tn != null) {
			TreeNode padre = tn.getParent();
			if (padre != null && padre.getData() != null && ((RolModel) padre.getData()).getId() != 0) {
				marcarRolFormulario(padre);
			}
			RolModel rolModel = (RolModel) tn.getData();
			MuRolFormularioPK pk = new MuRolFormularioPK();
			pk.setFormularioId((int) rolModel.getId());
			pk.setRolId((int) rolModel.getRolID());

			MuRolFormulario rf = new MuRolFormulario();
			rf.setId(pk);
			rf.setEstado(true);

			try {
				roleBL.updateRoleFormulario(rf);
			} catch (Exception e) {
				log.error("[guardarPermisos] Fallo al registrar los permisos del rol:" + rolModel.getRolID() + ", formulario:" + rolModel.getId() + "", e);
			}
		}
	}

	public void guardarRol() {
		log.debug("[saveRoleIp]: Ingresando..");
		String str = roleBL.validate(role, roleId);
		if (!str.isEmpty()) {
			SysMessage.warn(str, null);
			return;
		}
//		// validar Ips
//		Iterator<MuRolIp> it = listRolIp.iterator();
//		while (it.hasNext()) {
//			MuRolIp rolIp = it.next();
//			str = roleBL.validateIp(rolIp, "", false);
//			if (!str.isEmpty()) {
//				SysMessage.error(str, null);
//				return;
//			}
//		}

		try {
			if (!edit) {
				roleBL.saveRole(role);
				controlerBitacora.insert(DescriptorBitacora.ROL, role.getRolId() + "", role.getNombre());
				 SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()),"Registro satisfactorio", "");
			} else {
				int id = Integer.parseInt(roleId);
				role.setRolId(id);
				roleBL.updateRole(role);
				controlerBitacora.update(DescriptorBitacora.ROL, role.getRolId() + "", role.getNombre());
				SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()),"Actualización satisfactoria", "");
			}
			//GuardarIps();

			listRole = roleBL.getRoles();
			newRole();
		} catch (Exception e) {
			log.error("[saveRole] error al momento de modificar o guardar: " + role.getRolId() + " " + e);
			SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro" + e.getMessage(), "");
		}
	}

	public void editRole() {
		String idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roleId");
		int id = Integer.parseInt(idstr);
		role = roleBL.getRole(id);
		//listRolIp = roleBL.getRolIp(id);
		//newRolIp();
		roleId = idstr;
		edit = true;
		visibleDialog = false;
	}

	public String deleteRole() {
		String idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roleId");
		int id = Integer.parseInt(idstr);
		visibleDialog = false;
		if (id == 1) {
			SysMessage.warn("Este Rol no puede eliminarse es el rol de Administración.", null);
			return "";
		}
		try {
			role = roleBL.getRole(id);
			roleBL.deleteRole(id);
			controlerBitacora.delete(DescriptorBitacora.ROL, role.getRolId() + "", role.getNombre());
			listRole = roleBL.getRoles();
			newRole();
			SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()),"Se elimino el registro", "");
		} catch (Exception e) {
			log.error("[deleteRole]  error al eliminar el menu id:" + idstr + "  " + e);
			SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");

		}
		return "";
	}

	/*public void GuardarIps() {
		try {
			roleBL.deleteRolIp(role.getRolId());
			Iterator<MuRolIp> it = listRolIp.iterator();
			while (it.hasNext()) {
				MuRolIp rolIp = it.next();
				rolIp.setIpId(0);
				rolIp.setMuRol(role);
				String str = roleBL.validateIp(rolIp, "", true);
				if (str.isEmpty()) {
					roleBL.saveRolIp(rolIp);
				}
			}
		} catch (Exception e) {
			log.error("[saveRole] error al momento de modificar o guardar las Ips del Rol: " + role.getRolId() + " " + e);
			SysMessage.error("Fallo al guardar las IPs en la Base de Datos.", null);
		}
	}*/

	/*public void removeIp2(MuRolIp rolIp) {
		if (rolIp != null) {
			listRolIp.remove(rolIp);
		}
		newRolIp();
		editIp = false;
	}*/

/*	public void addIp() {
		String str = validateIp(rolIp);
		if (!str.isEmpty()) {
			SysMessage.warn(str, null);
			return;
		}

		
		if (!editIp)
			listRolIp.add(0, rolIp);
		else {
			for (MuRolIp ip : listRolIp) {
				if (ip.getIpId() == rolIp.getIpId()) {
					ip.setIp(rolIp.getIp());
					ip.setIpF(rolIp.getIpF());
					break;
				}
			}
		}

		newRolIp();
	}*/

/*	public void onRowSelect2(MuRolIp rolIp2) {
		rolIp = new MuRolIp();
		rolIp.setIpId(rolIp2.getIpId());
		rolIp.setIp(rolIp2.getIp());
		rolIp.setIpF(rolIp2.getIpF());
		editIp = true;
	}*/

/*	public String validateIp(MuRolIp roleIp) {
		log.debug("[validate]: Ingresando..");

		if (roleIp.getIp() == null || roleIp.getIp().isEmpty()) {
			return "El campo ip es requerido";
		}
		
		if (!roleBL.validate(roleIp.getIp())) {
			return "La ip " + roleIp.getIp() + " no es valida";
		}
		
		if (roleIp.getIpF() != null && !(roleIp.getIpF().isEmpty()))
		{
			if(roleIp.getIp().substring(0, 3).compareTo(roleIp.getIpF().substring(0, 3))==0){
				
			}
			else
				if(roleIp.getIp().substring(0, 3).compareTo(roleIp.getIpF().substring(0, 3))<0){
					return "La Ip "+ roleIp.getIp().substring(0, 3) +" inicial no se encuentra en la misma red que la Ip "+ roleIp.getIpF().substring(0, 3) +" final";
			
				}
				else
					if(roleIp.getIp().substring(0, 3).compareTo(roleIp.getIpF().substring(0, 3))>0){
						return "La Ip "+ roleIp.getIpF().substring(0, 3) +" final no se encuentra en la misma red que Ip "+ roleIp.getIp().substring(0, 3) +" inical";
					}
		}
		if (roleIp.getIpF() != null && !(roleIp.getIpF().isEmpty()))
		{
			if ( !roleBL.validate(roleIp.getIpF())) {
				return "La Ip " + roleIp.getIpF() + " final no es valida";
			}
		}

		if (roleIp.getIp().equals(roleIp.getIpF()) ) {
			return "El campo Ip "+ roleIp.getIp()+ " inicial y el campo Ip "+ roleIp.getIpF()+ " final son iguales, no se puede registrar";
		}
		
		
		
		if(roleIp.getIpF() != null && !(roleIp.getIpF().isEmpty())){
			if(quitarPunto(roleIp.getIpF()) < quitarPunto(roleIp.getIp())){
				return "La IP "+ roleIp.getIpF()+ " es menor a la IP Inicial "+ roleIp.getIp()+ ", no se puede registrar";
			}
		}
		
		
		Iterator<MuRolIp> it = listRolIp.iterator();
		while (it.hasNext()) {
			MuRolIp itIp = it.next();
			
			if(itIp.getIpF() != null && !(itIp.getIpF().isEmpty()))
			{
				if (itIp.getIp().equals(roleIp.getIp()) || itIp.getIpF().equals(roleIp.getIp() ) ) {
					if ((long) itIp.getIpId() != (long) roleIp.getIpId())					
						return "La Ip " + roleIp.getIp() + " inicial ya fue agregada";
				}
			}
			if(itIp.getIpF() != null && !(itIp.getIpF().isEmpty())){
				if(roleIp.getIpF() != null && !(roleIp.getIpF().isEmpty()))
				{
					if (itIp.getIpF().equals(roleIp.getIpF()) || itIp.getIp().equals(roleIp.getIpF() )) {
						if ((long) itIp.getIpId() != (long) roleIp.getIpId())
							return "La Ip " + roleIp.getIpF() + "final ya fue agregada";
					}
				}
			}
			
		}
		
		Iterator<MuRolIp> it2 = listRolIp.iterator();
		while (it2.hasNext()) {
			MuRolIp itIp = it2.next();
			if(roleIp.getIpF() != null && !(roleIp.getIpF().isEmpty())){
				if(quitarPunto(itIp.getIp()) >= quitarPunto(roleIp.getIp()) && quitarPunto(itIp.getIp()) <= quitarPunto(roleIp.getIpF())  ){
					if ((long) itIp.getIpId() != (long) roleIp.getIpId())
					return "La Ip "+roleIp.getIp()+" inicial y la Ip "+roleIp.getIpF()+"final intentan contener otro rango ip, no se puede registrar";
				}
			}
			if(itIp.getIpF() != null && !(itIp.getIpF().isEmpty())){
				if(quitarPunto(roleIp.getIp()) >= quitarPunto(itIp.getIp()) && quitarPunto(roleIp.getIp()) <= quitarPunto(itIp.getIpF())  ){
					if ((long) itIp.getIpId() != (long) roleIp.getIpId())
					return "La Ip "+roleIp.getIp()+"inicial se encuentra en un rango ya registrado, no se puede registrar";
				}
			}
			if(roleIp.getIpF() != null && !(roleIp.getIpF().isEmpty())){
				if(itIp.getIpF() != null && !(itIp.getIpF().isEmpty())){
					if(quitarPunto(roleIp.getIpF()) >= quitarPunto(itIp.getIp()) && quitarPunto(roleIp.getIpF()) <= quitarPunto(itIp.getIpF())  ){
						if ((long) itIp.getIpId() != (long) roleIp.getIpId())
						return "La Ip "+roleIp.getIpF()+" final se encuentra en un rango ya registrado, no se puede registrar";
					}
				}
			}
			
		}
				
		return "";
	}*/


	public long quitarPunto(String ip) {
		//Es una funcion para convertir una cadena a un arreglo, de acuerdo al caracter que se la pasa por parametro
		StringTokenizer st = new StringTokenizer(ip, ".");

		  StringBuilder response = new StringBuilder();
		  //recorre la cadena separado por puntos
		  while (st.hasMoreElements()) {
		   String token = st.nextToken();
		   //Aumenta los 0 faltantes dependiendo de la cantidad de digitos que tenga
		   if (token.length() < 3) {
		    int i = 3 - token.length();
		    for (int j = 1; j <= i; j++) {
		     token = "0" + token;
		    }
		    response.append(token);
		   } else {
		    response.append(token);
		   }
		  }
		  return Long.parseLong(response.toString());
		 }
	
	/*public MuRolIp getIpSeleccionada() {
		return ipSeleccionada;
	}

	public void setIpSeleccionada(MuRolIp ipSeleccionada) {
		this.ipSeleccionada = ipSeleccionada;
	}

	public void newRolIp() {
		ipSeleccionada = null;
		editIp = false;
		rolIp = new MuRolIp();
		rolIp.setIpId((new Date()).getTime());
		// if (listRolIp.size() > 0)
		// rolIp.setIpId(listRolIp.get(listRolIp.size() - 1).getIpId() + 1);
	}

	public MuRolIp getRolIp() {
		return rolIp;
	}

	public void setRolIp(MuRolIp rolIp) {
		this.rolIp = rolIp;
	}*/

	public void newRole() {
		edit = false;
		role = new MuRol();
		role.setValidaIp(false);
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public MuRol getRole() {
		return role;
	}

	public void setRole(MuRol role) {
		this.role = role;
	}

	public List<MuRol> getListRole() {
		return listRole;
	}

/*	public List<MuRolIp> getListRolIp() {
		return listRolIp;
	}*/

	public void setListRole(List<MuRol> listRole) {
		this.listRole = listRole;
	}

	public TreeNode getRoles() {
		return roles;
	}

	public void setRoles(TreeNode roles) {
		this.roles = roles;
	}

	public TreeNode[] getNodosSeleccionados() {
		return nodosSeleccionados;
	}

	public void setNodosSeleccionados(TreeNode[] nodosSeleccionados) {
		this.nodosSeleccionados = nodosSeleccionados;
	}

	public boolean isVisibleDialog() {
		return visibleDialog;
	}

	public void setVisibleDialog(boolean visibleDialog) {
		this.visibleDialog = visibleDialog;
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

	public StreamedContent getDescarga() {
		return descarga;
	}

	public void setDescarga(StreamedContent descarga) {
		this.descarga = descarga;
	}
}
