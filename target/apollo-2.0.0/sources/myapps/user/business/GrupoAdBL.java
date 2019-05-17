package myapps.user.business;

import myapps.servicio_basico.commons.IApplicationContext;
import myapps.user.dao.GrupoAdDAO;
import myapps.user.model.MuGrupoAd;
import myapps.user.model.MuRol;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Named
@Stateless
public class GrupoAdBL implements Serializable {

    @Inject
    private GrupoAdDAO dao;
    @EJB
    private IApplicationContext beanApplicationContext;

    public String validate(MuGrupoAd user, String idStr) {
		
		/*if (user == null || user.getNombre().trim().isEmpty()) {
			return "El campo Nombre es requerido";
		}

		if (!UtilUrl.esTextoValido(user.getNombre(),beanApplicationContext
				.getFormParameters().get(EnumParametros
								.EXPRESION_REGULAR_TEXTO_NORMAL.toString()))) {
			return "El campo Nombre no es valido: " + beanApplicationContext
					.getFormParameters().get(EnumParametros
							.EXPRESION_REGULAR_TEXTO_NORMAL.toString());
		}

		if (user.getDetalle() != null && !user.getDetalle().trim().isEmpty()) {
			if (!UtilUrl.esTextoValido(user.getDetalle(), beanApplicationContext
					.getFormParameters().get(EnumParametros
							.EXPRESION_REGULAR_TEXTO_NORMAL.toString()))) {
				return "El campo Descripcion no es valido: " + beanApplicationContext
						.getFormParameters().get(EnumParametros
								.EXPRESION_REGULAR_TEXTO_NORMAL.toString());
			}
		}*/
        String resultado = "";
        MuGrupoAd usAux = dao.getGroupName(user.getNombre());
        if (usAux == null) {
            resultado = "";
        } else if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            if (id == usAux.getGrupoId() && user.getNombre().equals(usAux.getNombre())) {
                resultado = "";
            }
        } else {
            resultado = "este nombre de grupo ya existe";
        }
        return resultado;
    }

    public void saveGroupRole(MuGrupoAd group, int idRole) throws Exception {
        MuRol rol = new MuRol();
        rol.setRolId(idRole);
        group.setMuRol(rol);
        group.setEstado(true);
        dao.save(group);
    }

    public void updateGroup(MuGrupoAd group, int idRole) throws Exception {
        MuRol rol = new MuRol();
        rol.setRolId(idRole);

        MuGrupoAd groupAux = dao.get(group.getGrupoId());
        groupAux.setNombre(group.getNombre());
        groupAux.setDetalle(group.getDetalle());
        groupAux.setMuRol(rol);
        dao.update(groupAux);
    }

    public void deleteGroup(int idGroup) throws Exception {
        MuGrupoAd user = dao.get(idGroup);
        user.setEstado(false);
        dao.update(user);
    }

    public List<MuGrupoAd> getGroups() {
        return dao.getList();
    }

    public MuGrupoAd getGroup(int idGroup) {
        return dao.get(idGroup);
    }
}