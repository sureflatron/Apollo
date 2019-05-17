package myapps.user.dao;

import myapps.user.model.MuRol;
import myapps.user.model.MuRolFormulario;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Named
@Stateless
public class RoleDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(MuRol dato) throws Exception {
        entityManager.persist(dato);
    }

    public void saveRolForulario(MuRolFormulario dato) throws Exception {
        entityManager.persist(dato);
    }

	/*public void saveRolIp(MuRolIp dato) throws Exception {		
		//transaction.begin();
		entityManager.persist(dato);
		//transaction.commit();
	}*/

    public MuRol get(long id) {
        return entityManager.find(MuRol.class, id);
    }
	
	/*public MuRolIp getRolIp(long id) {
		return entityManager.find(MuRolIp.class, id);
	}*/

    public void update(MuRol dato) throws Exception {
        entityManager.merge(dato);
    }

    public void updateRolFormulario(MuRolFormulario dato) throws Exception {
        entityManager.merge(dato);
    }

    public void updateRolForulario(MuRolFormulario dato) throws Exception {
        entityManager.merge(dato);
    }
	
	/*public void updateRolIp(MuRolIp dato) throws Exception {		
		//transaction.begin();
		entityManager.merge(dato);
		//transaction.commit();
	}*/

    @SuppressWarnings("unchecked")
    public List<MuRol> getList() {
        return entityManager.createQuery("SELECT r FROM MuRol r WHERE  r.estado = true Order By r.rolId desc ").getResultList();
    }

    @SuppressWarnings("unchecked")
    public MuRol getName(String name) {
        String consulta = "SELECT r FROM MuRol r WHERE r.nombre = :name and r.estado = true";
        Query qu = entityManager.createQuery(consulta).setParameter("name", name);
        List<MuRol> lista = qu.getResultList();
        return lista.isEmpty() ? null : lista.get(0);
    }

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
	
	/*@SuppressWarnings("unchecked")
	public MuRolIp getIp(String ip,long id) {
		Long ipL = quitarPunto(ip);
//		String consulta = "SELECT r FROM MuRolIp r WHERE (:ip BETWEEN r.ip and r.ipF or r.ip = :ip)  and r.muRol.rolId = :id and r.estado = true";
		String consulta = "SELECT r.* FROM ROL_IP r WHERE (r.ip=:ip or (:ipL BETWEEN  TO_NUMBER(lpad(SUBSTR(r.ip,0,INSTR(r.ip,'.')-1),3,'0') || lpad(SUBSTR(r.ip,INSTR(r.ip,'.')+1,(INSTR(r.ip,'.',1,2)-INSTR(r.ip,'.')-1)),3,'0') || lpad(SUBSTR(r.ip,INSTR(r.ip,'.',1,2)+1,INSTR(r.ip,'.',1,3)-INSTR(r.ip,'.',1,2)-1),3,'0') || lpad(SUBSTR(r.ip,INSTR(r.ip,'.',1,3)+1),3,'0')) AND TO_NUMBER(lpad(SUBSTR(r.ipF,0,INSTR(r.ipF,'.')-1),3,'0') || lpad(SUBSTR(r.ipF,INSTR(r.ipF,'.')+1,(INSTR(r.ipF,'.',1,2)-INSTR(r.ipF,'.')-1)),3,'0') || lpad(SUBSTR(r.ipF,INSTR(r.ipF,'.',1,2)+1,INSTR(r.ipF,'.',1,3)-INSTR(r.ipF,'.',1,2)-1),3,'0') || lpad(SUBSTR(r.ipF,INSTR(r.ipF,'.',1,3)+1),3,'0')))) and r.rol_id = :id and r.estado = 1";
		Query qu = entityManager.createNativeQuery(consulta,MuRolIp.class);
		qu.setParameter("ipL",ipL);
		qu.setParameter("ip",ip);
		qu.setParameter("id", id);
				
		List<MuRolIp> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);
	} */

    @SuppressWarnings("unchecked")
    public List<MuRolFormulario> getRolFormulario(long id) {
        String consulta = "FROM MuRolFormulario r WHERE r.muRol.rolId = :id ORDER BY r.muFormulario.orden";
        Query qu = entityManager.createQuery(consulta).setParameter("id", id);
        return qu.getResultList();

    }
	
	/*@SuppressWarnings("unchecked")
	public List<MuRolIp> getRolIps(long id) {
		String consulta = "FROM MuRolIp r WHERE r.muRol.rolId = :id and r.estado = true ORDER BY r.ipId";
		Query qu = entityManager.createQuery(consulta).setParameter("id", id);
		return qu.getResultList();
	}*/

    @SuppressWarnings("unchecked")
    public List<MuRolFormulario> getRolFormularioDelete(long idRol) {
        String consulta = "SELECT r FROM MuRolFormulario r WHERE r.muRol.rolId = :idRol and r.muRol.estado = true";
        Query qu = entityManager.createQuery(consulta).setParameter("idRol", idRol);
        return qu.getResultList();
    }

    public void deleteRolFormulario(long rolId) throws Exception {
        String sql = "UPDATE ROL_FORMULARIO SET ESTADO = 0 WHERE ROL_ID = :rolId";
        Query q = entityManager.createNativeQuery(sql);
        q.setParameter("rolId", rolId);
        q.executeUpdate();
    }

    public void deleteRolIp(long rolId) throws Exception {
        String sql = "DELETE MuRolIp r WHERE r.muRol.rolId = :rolId";
        Query q = entityManager.createQuery(sql);
        q.setParameter("rolId", rolId);
        q.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<MuRolFormulario> getRolFormularioUser(long id) {
        String consulta = "SELECT r FROM MuRolFormulario r WHERE  r.rol.rolId = :id  " + "ORDER BY r.formulario.posicionColumna , r.formulario.posicionFila   ";
        Query qu = entityManager.createQuery(consulta).setParameter("id", id);
        return qu.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getAccesosRol(MuRol muRol) {
        List<Object[]> objects = new ArrayList<>();
        Object[] titles = {"ROL", "DESCRIPCION", "MODULO", "FORMULARIO", "URL"};
        objects.add(titles);

        String sql = "SELECT r.NOMBRE as rol,r.DESCRIPCION,(select ff.NOMBRE from FORMULARIO " +
                "ff where ff.ID=f.FORMULARIO_ID) as modulo, f.NOMBRE as formulario,f.URL FROM ROL r, ROL_FORMULARIO rf, FORMULARIO f WHERE r.ROL_ID=rf" +
                ".ROL_ID and rf.FORMULARIO_ID= f.ID and r.ESTADO=1 and rf.ESTADO=1  and f.FORMULARIO_ID!=0 and " +
                "r.ROL_ID=" + muRol.getRolId() + " group by r.NOMBRE,r.DESCRIPCION,f.ID,f.NOMBRE,f.URL,f" +
                ".FORMULARIO_ID order by r.NOMBRE,modulo asc";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();

        for (Object[] obj : resultList) {
            objects.add(obj);
        }

        return objects;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getAccesosRolAll(){
        List<Object[]> objects = new ArrayList<>();
        Object[] titles= {"ROL","DESCRIPCION","MODULO","FORMULARIO","URL"};
        objects.add(titles);

        String sql= "SELECT r.NOMBRE as rol,r.DESCRIPCION,(select ff.NOMBRE from FORMULARIO " +
                "ff where ff.ID=f.FORMULARIO_ID) as modulo, f.NOMBRE as formulario,f.URL FROM ROL r, ROL_FORMULARIO rf, FORMULARIO f WHERE r.ROL_ID=rf" +
                ".ROL_ID and rf.FORMULARIO_ID= f.ID and r.ESTADO=1 and rf.ESTADO=1  and f.FORMULARIO_ID!=0 group by r.NOMBRE,r.DESCRIPCION,f.ID,f.NOMBRE,f.URL,f" +
                ".FORMULARIO_ID order by r.NOMBRE,modulo asc";
        Query query= entityManager.createNativeQuery(sql);
        List<Object[]> resultList =query.getResultList();

        for (Object[] obj:resultList){
            objects.add(obj);
        }

        return  objects;
    }

}
