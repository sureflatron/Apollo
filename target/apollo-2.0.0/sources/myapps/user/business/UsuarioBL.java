package myapps.user.business;

import myapps.servicio_basico.commons.IApplicationContext;
import myapps.user.dao.GrupoAdDAO;
import myapps.user.dao.RoleDAO;
import myapps.user.dao.UsuarioDAO;
import myapps.user.model.MuGrupoAd;
import myapps.user.model.MuRol;
import myapps.user.model.MuRolFormulario;
import myapps.user.model.MuUsuario;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named
@Stateless
public class UsuarioBL implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioDAO dao;

    @Inject
    private RoleDAO rolDao;

    @Inject
    private GrupoAdDAO groupDao;
    @EJB
    private IApplicationContext beanApplicationContext;
    private MuRol muRol = new MuRol();

    public String validate(MuUsuario user, String idStr) throws Exception {
        String resultado = "";
        if (user != null) {

            MuUsuario usAux = dao.getUsuarioLogin(user.getLogin());
            if (usAux == null) {
                resultado = "";

            } else if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                if (id == usAux.getUsuarioId())
                    if (user.getLogin().equals(usAux.getLogin())){

                    }
            } else {
                if (user.getUsuarioId() != usAux.getUsuarioId()) {
                    resultado = "El usuario ya existe";
                }
            }
        } else {
            resultado = "Usuario nulo para validar";
        }
        return resultado;
    }

    public void saveUserRole(MuUsuario user, int idRole) throws Exception {
        muRol.setRolId(idRole);
        user.setRolByRolId(muRol);
        user.setEstado(true);
        dao.save(user);
    }

    public void updateUser(MuUsuario user, int idRole) throws Exception {
        muRol.setRolId(idRole);
        user.setRolByRolId(muRol);
        dao.update(user);
    }

    public void activateUser(MuUsuario user) throws Exception {
        user.setEstado(true);
        dao.update(user);
    }

    public void deleteUser(long idUser) throws Exception {
        MuUsuario user = dao.get(idUser);
        user.setEstado(false);
        dao.update(user);
    }

    public List<MuUsuario> getUsers() throws Exception {
        return dao.getList();
    }

    public List<MuUsuario> getUsersNA() throws Exception {
        return dao.getListNA();
    }

    public MuUsuario getUser(int idUser) throws Exception {
        return dao.get(idUser);
    }

    public List<String> getListFormUser(String login) throws Exception {

        long k = 1;
        MuUsuario user = dao.getUsuarioLogin(login);
        if (user != null)
            k = user.getRolByRolId().getRolId();

        List<MuRolFormulario> lista = rolDao.getRolFormularioUser(k);
        List<String> listaUrl = new ArrayList<>();
        for (MuRolFormulario rolFormulario : lista) {
            listaUrl.add(rolFormulario.getMuFormulario().getUrl());
        }
        return listaUrl;
    }

    public List<MuRolFormulario> getRolFormularios(String login) throws Exception {
        long k = 1;
        MuUsuario user = dao.getUsuarioLogin(login);
        if (user != null)
            k = user.getRolByRolId().getRolId();

        return rolDao.getRolFormulario(k);

    }

    public long getIdRole(String login, List<Object> userGroups) {

        MuUsuario user = dao.getUsuarioLogin(login);

        if (user != null)
            return user.getRolByRolId().getRolId();

        for (Object object : userGroups) {
            MuGrupoAd gr = groupDao.getGroupName(object.toString());
            if (gr != null)
                return gr.getMuRol().getRolId();
        }

        return -1;
    }

    public List<MuUsuario> getUsuario() {
        return dao.getList2();
    }
    public long getIdUser(String login){
        return dao.getUsuarioLogin(login).getUsuarioId();
    }

}
