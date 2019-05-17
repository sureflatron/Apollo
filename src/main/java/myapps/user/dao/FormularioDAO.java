package myapps.user.dao;

import myapps.user.model.MuFormulario;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Named
@Stateless
public class FormularioDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "servBasicoDS")
	private transient EntityManager entityManager;

	/*@Resource
	private transient UserTransaction transaction;*/

	public MuFormulario find(long id) {
		return (MuFormulario) entityManager.find(MuFormulario.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<MuFormulario> findPadres(long rolId) {
		//String sql = "SELECT F.* FROM FORMULARIO F INNER JOIN ROL_FORMULARIO RF ON F.ID = RF.FORMULARIO_ID where F.FORMULARIO_ID is null AND RF.ROL_ID = :rolId AND RF.ESTADO = 1 AND F.ESTADO = 1 order by orden";
		String sql = "SELECT F.* FROM FORMULARIO F INNER JOIN ROL_FORMULARIO RF ON F.ID = RF.FORMULARIO_ID where F.FORMULARIO_ID is null AND RF.ROL_ID = :rolId AND RF.ESTADO = 1 AND F.ESTADO = 1 AND F.VISIBLE = 1 order by orden";
		Query query = entityManager.createNativeQuery(sql, MuFormulario.class);
		query.setParameter("rolId", rolId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuFormulario> findHijos(long raizId, long rolId) {
		//String sql = "SELECT F.* FROM FORMULARIO F INNER JOIN ROL_FORMULARIO RF ON F.ID = RF.FORMULARIO_ID where F.FORMULARIO_ID = :raizId AND RF.ROL_ID = :rolId AND RF.ESTADO = 1 AND F.ESTADO= 1 order by F.orden";
		String sql = "SELECT F.* FROM FORMULARIO F INNER JOIN ROL_FORMULARIO RF ON F.ID = RF.FORMULARIO_ID where F.FORMULARIO_ID = :raizId AND RF.ROL_ID = :rolId AND RF.ESTADO = 1 AND F.ESTADO= 1 AND F.VISIBLE = 1 order by F.orden";
		Query query = entityManager.createNativeQuery(sql, MuFormulario.class);
		query.setParameter("raizId", raizId);
		query.setParameter("rolId", rolId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findPadresPermisos(long rolId) {		
		String sql = "SELECT ID, NOMBRE, CASE WHEN (SELECT ID FROM FORMULARIO FF INNER JOIN ROL_FORMULARIO RR ON FF.ID = RR.FORMULARIO_ID WHERE RR.ROL_ID = :rolId AND FF.ESTADO = 1 AND RR.ESTADO = 1 AND F.ID = FF.ID) IS NULL THEN 0 ELSE 1 END PERMISO FROM FORMULARIO F INNER JOIN ROL_FORMULARIO R ON F.ID = R.FORMULARIO_ID WHERE F.FORMULARIO_ID is null and R.ROL_ID = 1 AND F.ESTADO = 1 AND R.ESTADO = 1  order by orden";		
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("rolId", rolId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findHijosPermisos(long raizId, long rolId) {
		String sql = "SELECT ID, NOMBRE, CASE WHEN (SELECT ID FROM FORMULARIO FF INNER JOIN ROL_FORMULARIO RR ON FF.ID = RR.FORMULARIO_ID WHERE RR.ROL_ID = :rolId AND FF.ESTADO = 1 AND RR.ESTADO = 1 AND F.ID = FF.ID) IS NULL THEN 0 ELSE 1 END PERMISO FROM FORMULARIO F INNER JOIN ROL_FORMULARIO R ON F.ID = R.FORMULARIO_ID WHERE F.FORMULARIO_ID = :raizId and R.ROL_ID = 1 AND F.ESTADO = 1 AND R.ESTADO = 1  order by orden";		
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("raizId", raizId);
		query.setParameter("rolId", rolId);
		return query.getResultList();
	}

}
