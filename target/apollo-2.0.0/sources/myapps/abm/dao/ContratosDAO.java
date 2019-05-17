package myapps.abm.dao;

import myapps.abm.model.ContratosEntity;
import myapps.servicio_basico.util.UtilDate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ContratosDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    private static final String EXPIRA2="expira2";
    private static final String EXPIRA="expira";
    private static final String NRO_CONTRATO="nroContrato";
    private static final String PROPIETARIO="propietario";
    private static final String DEPTO="depto";


    public void save(ContratosEntity entity) {
        entityManager.persist(entity);
    }

    public void update(ContratosEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<ContratosEntity> listAll() {

        String consulta = "SELECT r FROM ContratosEntity r WHERE  r.estado = true and r" +
                ".estadoContrato='Vigente' Order By r.idContrato desc ";
        Query qu = entityManager.createQuery(consulta);
        return qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<ContratosEntity> listAllByParameterObj(String parameter) {
        String consulta = buildSql(parameter, new HashMap<>());

        Query query = entityManager.createQuery(consulta);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ContratosEntity> listAllFilter(String obj, Map<String, Object> parameter) {

        String consulta = buildSql(obj, parameter);

        Query qu = entityManager.createQuery(consulta);

        if (!parameter.get(EXPIRA2).equals("") && !parameter.get(EXPIRA).equals("")) {
            qu.setParameter("fecha1", UtilDate.stringToDate(parameter.get(EXPIRA).toString(), "yyyy-MM-dd"),
                    TemporalType.DATE);
            qu.setParameter("fecha2", UtilDate.stringToDate(parameter.get(EXPIRA2).toString(),
                    "yyyy-MM-dd"),
                    TemporalType.DATE);
        }

        return (List<ContratosEntity>) qu.getResultList();

    }

    public ContratosEntity getById(long id) {
        return entityManager.find(ContratosEntity.class, id);
    }

    private String buildSql(String obj, Map<String, Object> mapParam) {

        StringBuilder stringBuilder = new StringBuilder("");
        String objValid = obj.equals("") ? "" : obj + " is not null";
        String select = "SELECT r FROM ContratosEntity r WHERE " + objValid;
        String orderBy = " AND r.estado = true AND r.estadoContrato='Vigente' Order By r.idContrato desc";
        String sql = "";

        if (!mapParam.isEmpty() && !obj.equals("")) {

            stringBuilder = stringBuilder
                    .append(select)
                    .append(mapParam.get(NRO_CONTRATO).toString().equals("") ? "" : " AND r.nroContrato='" + mapParam.get(NRO_CONTRATO) + "'")
                    .append(mapParam.get(PROPIETARIO) == null ? "" : " AND r.propietario='" +
                            mapParam.get
                                    (PROPIETARIO) + "'")
                    .append(mapParam.get(DEPTO).toString().equals("") ? "" : " AND r.departamentoByIdDepartamento.idDepartamento=" + mapParam.get(DEPTO))
                    .append(mapParam.get(EXPIRA).equals("") ? "" : " AND r.expiracion>=:fecha1")
                    .append(mapParam.get(EXPIRA2).equals("") ? "" : " AND r.expiracion<=:fecha2");

        } else {
            int cont = 0;
            String nroC = "";
            String prop = "";
            String depto = "";
            String fIni = "";
            String fFin = "";
            if (!mapParam.isEmpty()) {
                if (!mapParam.get(NRO_CONTRATO).toString().isEmpty()) {
                    nroC = mapParam.get(NRO_CONTRATO).toString().equals("") ? "" : "r.nroContrato='" + mapParam.get(NRO_CONTRATO) + "'";
                    cont++;
                }
                if (mapParam.get(PROPIETARIO) != null) {
                    String logAnd = cont > 0 ? " AND " : "";
                    prop = mapParam.get(PROPIETARIO).toString().equals("") ? "" : logAnd + " r.propietario='" + mapParam.get(PROPIETARIO) + "'";
                    cont++;
                }
                if (!mapParam.get(DEPTO).toString().isEmpty()) {
                    String logAnd = cont > 0 ? " AND " : "";
                    depto = mapParam.get(DEPTO).toString().equals("") ? "" : logAnd + " r.departamentoByIdDepartamento.idDepartamento=" + mapParam.get(DEPTO);
                    cont++;
                }

                if (!mapParam.get(EXPIRA).equals("")) {
                    String logAnd = cont > 0 ? " AND " : "";
                    fIni = mapParam.get(EXPIRA) == null ? "" : logAnd + " r.expiracion>=:fecha1";
                    cont++;
                }
                if (!mapParam.get(EXPIRA2).equals("")) {
                    String logAnd = cont > 0 ? " AND " : "";
                    fFin = mapParam.get(EXPIRA2) == null ? "" : logAnd + " r" +
                            ".expiracion<=:fecha2";

                }


            }
            if (!objValid.isEmpty()) {
                orderBy = " AND r.estado = true AND r.estadoContrato='Vigente' Order By r.idContrato desc";
            } else {
                if (!mapParam.isEmpty() && cont > 0) {
                    orderBy = " AND r.estado = true Order By r.idContrato desc";
                } else {
                    orderBy = " r.estado = true AND r.estadoContrato='Vigente' Order By r.idContrato desc";
                }


            }

            stringBuilder = stringBuilder.append(select)
                    .append(nroC)
                    .append(prop)
                    .append(depto)
                    .append(fIni)
                    .append(fFin);
        }
        sql = stringBuilder.append(orderBy).toString();

        return sql;
    }

}