package myapps.user.dao;

import myapps.servicio_basico.util.UtilDate;
import myapps.user.model.MuBitacora;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named
@Stateless
public class BitacoraDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(MuBitacora dato) {
        entityManager.persist(dato);
    }

    @SuppressWarnings("unchecked")
    public List<MuBitacora> listBitacora(MuBitacora muBitacoraObj, Date fechainc, Date fechaFin) {

        List<MuBitacora> listBitacora;
        String sql = "";
        StringBuilder sqlWhere = new StringBuilder("");
        if (muBitacoraObj == null) {
            sql = "FROM MuBitacora b";
        } else {
            int contParametros = 0;
            sqlWhere = sqlWhere.append(" FROM MuBitacora b ");
            if (!muBitacoraObj.getUsuario().isEmpty() || !muBitacoraObj.getDireccionIp().isEmpty()
                    || fechainc != null || fechaFin != null) {
                sqlWhere = sqlWhere.append(" WHERE ");
            }
            if (!muBitacoraObj.getUsuario().isEmpty()) {
                sqlWhere = sqlWhere.append(" b.usuario='").append(muBitacoraObj
                        .getUsuario()).append("'");
                contParametros++;
            }
            if (!muBitacoraObj.getDireccionIp().isEmpty()) {
                sqlWhere = sqlWhere.append(contParametros > 0 ? " AND " : "").append(" b" +
                        ".direccionIp='").append(muBitacoraObj.getDireccionIp()).append("'");
                contParametros++;
            }
            /**
             * Filtro fechas
             */

            /*Date date1 = UtilDate.stringToDate(UtilDate.dateToTimeStampSql(fechainc),
                    "yyyy-MM-dd HH:mm:ss");
            Date date2 = UtilDate.stringToDate(UtilDate.dateToTimeStampSql(fechaFin),
                    "yyyy-MM-dd HH:mm:ss");

            Calendar fecha1 = UtilDate.dateToCalendar(date1);
            int second1 = fecha1.get(Calendar.SECOND);
            int hora1 = fecha1.get(Calendar.HOUR);
            int minuto1 = fecha1.get(Calendar.MINUTE);

            Calendar fecha2 = UtilDate.dateToCalendar(date1);
            int second2 = fecha2.get(Calendar.SECOND);
            int hora2 = fecha2.get(Calendar.HOUR);
            int minuto2 = fecha2.get(Calendar.MINUTE);
            String StringDateIni="";
            String StringDateFin ="";
            if (hora1 == 0 && minuto1 == 0 && second1 == 0) {
                StringDateIni = fecha1.get(Calendar.YEAR) + "-" + fecha1.get(Calendar.MONTH) +
                        "-" + fecha1.get(Calendar.DATE)+" "+"00:00:01";
            }
            if (hora1 == 0 && minuto1 == 0 && second1 == 0) {
                StringDateFin = fecha2.get(Calendar.YEAR) + "-" + fecha2.get(Calendar.MONTH) +
                        "-" + fecha2.get(Calendar.DATE)+" "+"23:59:59";
            }*/

/*            if (hora == 0 && minuto == 0 && second == 0) {
                sqlWhere = sqlWhere.append(contParametros > 0 ? " AND " : "").append("TO_CHAR(b" +
                        ".fecha,'yyyy-mm-dd')").append(" LIKE '%").append(StringDate).append
                        ("'");

                System.out.println("ES CERO");
            } else {*/
                if (fechainc != null && fechaFin != null) {

                    sqlWhere = sqlWhere.append(contParametros > 0 ? " AND " : "")
                            .append(" b.fecha>=TO_DATE('").append(UtilDate.dateToTimeStampSql
                                    (fechainc))
                            .append("','YYYY-MM-DD HH24:MI:SS')").append(" AND ")
                            .append(" b.fecha<=TO_DATE('").append(UtilDate.dateToTimeStampSql(fechaFin))
                            .append("','YYYY-MM-DD HH24:MI:SS')");

                } else {



                   if (fechainc != null) {
                       String dat= UtilDate.dateToString(fechainc,"yyyy-MM-dd");
                       sqlWhere = sqlWhere.append(contParametros > 0 ? " AND " : "").append("TO_CHAR(b" +
                           ".fecha,'yyyy-mm-dd')").append(" LIKE '%").append(dat).append
                           ("'");
/*
                        sqlWhere = sqlWhere.append(contParametros > 0 ? " AND " : "").append(" b" +
                                ".fecha>=TO_DATE('").append(UtilDate.dateToTimeStampSql(fechainc)).append("','YYYY-MM-DD HH24:MI:SS')");
*/
                    }
                    if (fechaFin != null) {
                      /*  sqlWhere = sqlWhere.append(contParametros > 0 ? " AND " : "").append("
                      b" +
                                ".fecha>=TO_DATE('").append(UtilDate.dateToTimeStampSql(fechaFin)).append("','YYYY-MM-DD HH24:MI:SS')");
                   */ }
                }
           // }
        }
        sqlWhere = sqlWhere.append(" ORDER BY b.fecha DESC");
        sql = sql + sqlWhere;
        Query query = entityManager.createQuery(sql);
        query.setMaxResults(1000);
        listBitacora = query.getResultList();

        return listBitacora;
    }

}
