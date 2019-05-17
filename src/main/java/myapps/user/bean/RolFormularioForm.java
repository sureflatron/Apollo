package myapps.user.bean;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.business.RoleBL;
import myapps.user.model.MuRol;
import org.apache.log4j.Logger;
import org.primefaces.model.TreeNode;

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

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class RolFormularioForm implements Serializable {

	@Inject
	private RoleBL roleBL;

	@EJB
    private IApplicationContext beanApplicationContext;
	private Map<String,String> validateParameter;
	
	private String idRol;
	private String nameRole;

	private TreeNode root;
	private TreeNode[] selectedNodes;

	public static final Logger log = Logger.getLogger(RolFormularioForm.class);

	@PostConstruct
	public void init() {

		String idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roleId");

		try {
			idRol = idstr;
			int id = Integer.parseInt(idstr);

			MuRol role = roleBL.getRole(id);
			nameRole = role.getNombre();
			validateParameter =  beanApplicationContext.getMapValidateParameters();
			cargar(id);
		} catch (Exception e) {
			log.error("[init] Fallo.", e);
		}
	}

	private void cargar(int id) {

		// Map<String, TreeNode> mapTreeNode = new HashMap<String, TreeNode>();
		// root = new DefaultTreeNode("Root", null);
		// List<MuRolFormulario> lista = roleBL.getRolFormulario(id);
		// for (MuRolFormulario rolFor : lista) {
		// String str = rolFor.getMuFormulario().getNivel();
		// if (str != null)
		// if (!str.isEmpty()) {
		// int k = str.lastIndexOf(".");
		// if (k == -1) {
		// TreeNode node0 = new
		// DefaultTreeNode(rolFor.getFormulario().getNombre(), root);
		// node0.setSelected(rolFor.getEstado());
		// mapTreeNode.put(str, node0);
		// } else {
		// String path = str.substring(0, k);
		// TreeNode nodeF = mapTreeNode.get(path);
		// TreeNode node0 = new
		// DefaultTreeNode(rolFor.getFormulario().getNombre(), nodeF);
		// mapTreeNode.put(str, node0);
		// node0.setSelected(rolFor.getEstado());
		// nodeF.setSelected(false);
		// nodeF.setExpanded(true);
		// }
		// }
		// }
	}

	public String saveRolFormulario() {

		int id = Integer.parseInt(idRol);

		if (id == 1) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// "No puede modificarse es rol Administrativo"));		
			SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "No puede modificarse es Rol Administrativo.", "");
			return "";
		}
		List<String> lis = new ArrayList<>();
		for (TreeNode treeNode : selectedNodes) {
			lis.add(treeNode.getData().toString());
		}
		log.info("[saveRolFormulario] lista de selecciones:" + lis + " " + "id Role:" + id);
		roleBL.updateRoleFormularioList(lis, id);
		return "/view/Role.xhtml?faces-redirect=true";
	}

	public String getIdRol() {

		return idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getNameRole() {
		return nameRole;
	}

	public void setNameRole(String nameRole) {
		this.nameRole = nameRole;
	}

	public TreeNode getRoot() {
		return root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public Map<String, String> getValidateParameter() {
		return validateParameter;
	}

	public void setValidateParameter(Map<String, String> validateParameter) {
		this.validateParameter = validateParameter;
	}
	
	
}
