package myapps.abm.dao;

import myapps.abm.model.PagoServicioEntity;
import myapps.dashboard.DashboardDto;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class PagoServicioDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(PagoServicioDAO.class);

    @PersistenceContext(unitName = "servBasicoDS")
    private transient EntityManager entityManager;

    public void save(PagoServicioEntity entity) {
        entityManager.persist(entity);
    }

    public void update(PagoServicioEntity entity) {
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAll() {

        String consulta = "SELECT r FROM PagoServicioEntity r WHERE  r.estado = true Order By r.idPagoServicio desc ";

        Query qu = entityManager.createQuery(consulta);
        return (List<PagoServicioEntity>) qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAllByUser(String user) {

        String consulta = "SELECT r FROM PagoServicioEntity r WHERE r.usuario='" + user + "' and  r.estado = " +
                "true Order By r.idPagoServicio desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<PagoServicioEntity>) qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAllByHistorialId(String user, long idHistorial) {

        String consulta = "SELECT r FROM PagoServicioEntity r WHERE r.usuario='" + user + "' and r" +
                ".hPagosMasivosByIdHPagoM= " + idHistorial + " and  r.estado = true Order By r" +
                ".idPagoServicio desc ";
        Query qu = entityManager.createQuery(consulta);
        return (List<PagoServicioEntity>) qu.getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAllByHistorialId(long idHistorial) {

        String consulta = "FROM PagoServicioEntity r WHERE r.hPagosMasivosByIdHPagoM.id =?1 and  " +
                "r.estado = true Order By r.idPagoServicio desc ";
        Query qu = entityManager.createQuery(consulta);
        qu.setParameter(1,idHistorial);
        return (List<PagoServicioEntity>) qu.getResultList();

    }

    public PagoServicioEntity getById(long id) {
        return entityManager.find(PagoServicioEntity.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAllByServicioMes(int mes, int year) {
        List<PagoServicioEntity> pagoServicioEntity = new ArrayList<>();
        try {
            List<Object[]> list;

            String sql;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date anterior = cal.getTime();
            cal.setTime(anterior);
            if (mes <= 0) {
                mes = cal.get(Calendar.MONTH) + 1;
            }
            if (year <= 0) {
                year = cal.get(Calendar.YEAR);
            }
            sql = "select ts.SERVICIO, EXTRACT(month from ps.PERIODO) as Mes, sum(MONTO) as Monto " +
                    "from PAGO_SERVICIO ps " +
                    "INNER JOIN SERVICIO s on ps.ID_SERVICIO = s.ID_SERVICIO " +
                    "INNER JOIN TIPO_SERVICIO ts on ts.ID_TIPO_SERVICIO = s.ID_TIPO_SERVICIO " +
                    "WHERE ESTADO_PAGO = 'Confirmado' " +
                    "and EXTRACT(month from ps.PERIODO)=" + mes + " and EXTRACT(year from ps.PERIODO)=" + year + " " +
                    "group by ts.SERVICIO,EXTRACT(month from ps.PERIODO) " +
                    "ORDER BY Monto DESC";
            Query qu = entityManager.createNativeQuery(sql);
            list = qu.getResultList();

            if (!list.isEmpty()) {
                for (Object[] pago : list) {
                    PagoServicioEntity pagosServicio = new PagoServicioEntity();
                    pagosServicio.setServicio((String) pago[0]);
                    pagosServicio.setMes(((BigDecimal) pago[1]));
                    pagosServicio.setMontoXMes((BigDecimal) pago[2]);
                    pagoServicioEntity.add(pagosServicio);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return pagoServicioEntity;
    }

    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAllProveedores() {
        List<PagoServicioEntity> pagoServicioEntity = new ArrayList<>();
        try {
            List<Object[]> list;

            String sql;

            sql = "SELECT DISTINCT s.ID_PROVEEDOR, pr.NOMBRE " +
                    "FROM PROVEEDOR pr " +
                    "INNER JOIN SERVICIO s ON s.ID_PROVEEDOR = pr.ID_PROVEEDOR " +
                    "INNER JOIN TIPO_SERVICIO ts ON ts.ID_TIPO_SERVICIO = s.ID_TIPO_SERVICIO " +
                    "WHERE ts.ID_TIPO_SERVICIO = 6 " +
                    "AND pr.ESTADO = 1 " +
                    "ORDER BY s.ID_PROVEEDOR";

            Query qu = entityManager.createNativeQuery(sql);
            list = qu.getResultList();

            if (!list.isEmpty()) {
                for (Object[] pago : list) {
                    PagoServicioEntity pagosServicio = new PagoServicioEntity();
                    pagosServicio.setIdProveedor((BigDecimal) pago[0]);
                    pagosServicio.setNombreProveedor((String) pago[1]);
                    pagoServicioEntity.add(pagosServicio);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return pagoServicioEntity;
    }

    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAllCantidadMedidorByProveedor(int mes, int year, BigDecimal proveedor) {
        List<PagoServicioEntity> pagoServicioEntity = new ArrayList<>();
        try {
            List<Object[]> list;

            String sql;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date anterior = cal.getTime();
            cal.setTime(anterior);
            if (mes <= 0) {
                mes = cal.get(Calendar.MONTH) + 1;
            }
            if (year <= 0) {
                year = cal.get(Calendar.YEAR);
            }

            sql = "SELECT medidor_pagado,medidor_no_pagados" +
                    " FROM (SELECT COUNT(SERVICIO.ID_SERVICIO) AS medidor_pagado" +
                    " FROM SERVICIO" +
                    " WHERE ID_PROVEEDOR = " + proveedor + " AND SERVICIO.ID_SERVICIO IN (SELECT DISTINCT ID_SERVICIO " +
                    " FROM PAGO_SERVICIO ps " +
                    " WHERE ESTADO_PAGO = 'Confirmado' and EXTRACT(month from ps.PERIODO)= " + mes + " and EXTRACT(year from ps.PERIODO)= " + year + ")), " +
                    " (SELECT COUNT(SERVICIO.ID_SERVICIO) AS medidor_no_pagados " +
                    " FROM SERVICIO " +
                    " WHERE ID_PROVEEDOR = " + proveedor + " AND SERVICIO.ID_SERVICIO NOT IN (SELECT DISTINCT ID_SERVICIO " +
                    " FROM PAGO_SERVICIO ps " +
                    " WHERE ESTADO_PAGO = 'Confirmado' and EXTRACT(month from ps.PERIODO)= " + mes + " and EXTRACT(year from ps.PERIODO)= " + year + "))";

            Query qu = entityManager.createNativeQuery(sql);
            list = qu.getResultList();

            if (!list.isEmpty()) {
                for (Object[] pago : list) {
                    PagoServicioEntity pagosServicio = new PagoServicioEntity();
                    pagosServicio.setMedidorPagado((BigDecimal) pago[0]);
                    pagosServicio.setMedidorNoPagado(((BigDecimal) pago[1]));
                    pagoServicioEntity.add(pagosServicio);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return pagoServicioEntity;
    }

    
    public PagoServicioEntity listAllMedidoresNoPagadosByProveedor(int mes, int year, BigDecimal proveedor) {
        PagoServicioEntity pagoServicioEntity = new PagoServicioEntity();
        try {
            BigDecimal list;

            String sql;

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date anterior = cal.getTime();
            cal.setTime(anterior);

            if (mes <= 0) {
                mes = cal.get(Calendar.MONTH) + 1;
            }
            if (year <= 0) {
                year = cal.get(Calendar.YEAR);
            }

            sql = "SELECT COUNT(SERVICIO.ID_SERVICIO) AS medidor_no_pagados " +
                    "FROM SERVICIO " +
                    "WHERE ID_PROVEEDOR = " + proveedor + " AND SERVICIO.ID_SERVICIO NOT IN (SELECT DISTINCT ID_SERVICIO " +
                    "FROM PAGO_SERVICIO ps " +
                    "WHERE ESTADO_PAGO = 'Confirmado' and EXTRACT(month from ps.PERIODO)= " + mes + " and EXTRACT(year from ps.PERIODO)= " + year + ") ";

            Query qu = entityManager.createNativeQuery(sql);
            list = (BigDecimal) qu.getSingleResult();

            if (list != null) {
                pagoServicioEntity.setMedidorNoPagado(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return pagoServicioEntity;
    }

   
    public PagoServicioEntity listAllCantidadPagosByProveedor(int mes, int year, BigDecimal proveedor) {
        PagoServicioEntity pagoServicioEntity = new PagoServicioEntity();
        try {
            BigDecimal cantidadPago;

            String sql;

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date anterior = cal.getTime();
            cal.setTime(anterior);

            if (mes <= 0) {
                mes = cal.get(Calendar.MONTH) + 1;
            }
            if (year <= 0) {
                year = cal.get(Calendar.YEAR);
            }

            sql = "SELECT COUNT(ps.ID_SERVICIO) " +
                    "FROM PAGO_SERVICIO ps " +
                    "INNER JOIN SERVICIO s ON ps.ID_SERVICIO = s.ID_SERVICIO " +
                    "WHERE ID_PROVEEDOR = " + proveedor + " " +
                    "AND ESTADO_PAGO = 'Confirmado' " +
                    "AND EXTRACT(month from ps.PERIODO)= " + mes + " " +
                    "AND EXTRACT(year from ps.PERIODO)= " + year + "";


            Query qu = entityManager.createNativeQuery(sql);
            cantidadPago = (BigDecimal) qu.getSingleResult();

            if (cantidadPago != null) {
                pagoServicioEntity.setCantidadPagos(cantidadPago);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return pagoServicioEntity;
    }

    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAllByMedidorPagado(int mes, int year) {
        List<PagoServicioEntity> pagoServicioEntity = new ArrayList<>();
        try {
            List<Object[]> list;

            String sql;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date anterior = cal.getTime();
            cal.setTime(anterior);
            if (mes <= 0) {
                mes = cal.get(Calendar.MONTH) + 1;
            }
            if (year <= 0) {
                year = cal.get(Calendar.YEAR);
            }

            sql = "SELECT ts.SERVICIO, COUNT(ID_MEDIDOR) as cantidad_medidor " +
                    "FROM MEDIDOR me INNER JOIN SERVICIO s ON me.ID_SERVICIO = s.ID_SERVICIO " +
                    "INNER JOIN TIPO_SERVICIO ts ON ts.ID_TIPO_SERVICIO = s.ID_TIPO_SERVICIO " +
                    "WHERE me.ID_SERVICIO IN (SELECT ID_SERVICIO FROM PAGO_SERVICIO ps WHERE ESTADO_PAGO = 'Confirmado' " +
                    "and EXTRACT(month from ps.PERIODO)= " + mes + " and EXTRACT(year from ps.PERIODO)= " + year + ") " +
                    "GROUP BY ts.SERVICIO";

            Query qu = entityManager.createNativeQuery(sql);
            list = qu.getResultList();

            if (!list.isEmpty()) {
                for (Object[] pago : list) {
                    PagoServicioEntity pagosServicio = new PagoServicioEntity();
                    pagosServicio.setServicio((String) pago[0]);
                    pagosServicio.setCantidadMedidor(((BigDecimal) pago[1]));
                    pagoServicioEntity.add(pagosServicio);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return pagoServicioEntity;
    }

    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAllByMedidorNoPagado(int mes, int year) {
        List<PagoServicioEntity> pagoServicioEntity = new ArrayList<>();
        try {
            List<Object[]> list;

            String sql;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date anterior = cal.getTime();
            cal.setTime(anterior);
            if (mes <= 0) {
                mes = cal.get(Calendar.MONTH) + 1;
            }
            if (year <= 0) {
                year = cal.get(Calendar.YEAR);
            }

            sql = "SELECT ts.SERVICIO, COUNT(ID_MEDIDOR) as cantidad_medidor " +
                    "FROM MEDIDOR me INNER JOIN SERVICIO s ON me.ID_SERVICIO = s.ID_SERVICIO " +
                    "INNER JOIN TIPO_SERVICIO ts ON ts.ID_TIPO_SERVICIO = s.ID_TIPO_SERVICIO " +
                    "WHERE me.ID_SERVICIO NOT IN (SELECT ID_SERVICIO FROM PAGO_SERVICIO ps WHERE ESTADO_PAGO = 'Confirmado' " +
                    "and EXTRACT(month from ps.PERIODO)= " + mes + " and EXTRACT(year from ps.PERIODO)= " + year + ") " +
                    "GROUP BY ts.SERVICIO";

            Query qu = entityManager.createNativeQuery(sql);
            list = qu.getResultList();

            if (!list.isEmpty()) {
                for (Object[] pago : list) {
                    PagoServicioEntity pagosServicio = new PagoServicioEntity();
                    pagosServicio.setServicio((String) pago[0]);
                    pagosServicio.setCantidadMedidor(((BigDecimal) pago[1]));
                    pagoServicioEntity.add(pagosServicio);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return pagoServicioEntity;
    }


    @SuppressWarnings("unchecked")
    public List<PagoServicioEntity> listAllByServicioYear(int year) {
        List<PagoServicioEntity> pagoServicioEntity = new ArrayList<>();
        try {
            List<Object[]> list;

            String sql;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date anterior = cal.getTime();
            cal.setTime(anterior);

            if (year <= 0) {
                year = cal.get(Calendar.YEAR);
            }

            sql = "select ts.SERVICIO,  EXTRACT(year from ps.PERIODO) as Anio, sum(MONTO) as Monto " +
                    "from PAGO_SERVICIO ps " +
                    "INNER JOIN SERVICIO s on ps.ID_SERVICIO = s.ID_SERVICIO " +
                    "INNER JOIN TIPO_SERVICIO ts on ts.ID_TIPO_SERVICIO = s.ID_TIPO_SERVICIO " +
                    "WHERE ESTADO_PAGO = 'Confirmado' " +
                    "and EXTRACT(year from ps.PERIODO)=" + year + " " +
                    "group by ts.SERVICIO,EXTRACT(year from ps.PERIODO) " +
                    "ORDER BY  EXTRACT(year from ps.PERIODO) DESC";

            Query qu = entityManager.createNativeQuery(sql);
            list = qu.getResultList();

            if (!list.isEmpty()) {
                for (Object[] pago : list) {
                    PagoServicioEntity pagosServicio = new PagoServicioEntity();
                    pagosServicio.setServicio((String) pago[0]);
                    pagosServicio.setMes(((BigDecimal) pago[1]));
                    pagosServicio.setMontoXMes((BigDecimal) pago[2]);
                    pagoServicioEntity.add(pagosServicio);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return pagoServicioEntity;
    }

    @SuppressWarnings("unchecked")
    public List<String> obtenerYear() {
        String consulta = "SELECT  DISTINCT TO_CHAR(PERIODO,'YYYY') as fecha FROM PAGO_SERVICIO order by  fecha asc";
        Query qu = entityManager.createNativeQuery(consulta);
        return (List<String>) qu.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<DashboardDto> getPagosProveedor(int mes, int year) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date anterior = cal.getTime();
        cal.setTime(anterior);
        if (mes <= 0) {
            mes = cal.get(Calendar.MONTH) + 1;
        }
        if (year <= 0) {
            year = cal.get(Calendar.YEAR);
        }
        String sql = "select p.NOMBRE,sum(ps.MONTO) from PROVEEDOR p,SERVICIO s,PAGO_SERVICIO ps " +
                "where p.ID_PROVEEDOR=s.ID_PROVEEDOR and ps.ID_SERVICIO=s.ID_SERVICIO and ps.ESTADO_PAGO='Confirmado' " +
                "and EXTRACT(month from ps.PERIODO)= " + mes + " and EXTRACT(year from ps.PERIODO)= " + year + " group by p.NOMBRE";
        List<DashboardDto> list = new ArrayList<>();
        List<Object[]> listObj = new ArrayList<>();
        try {
            Query query = entityManager.createNativeQuery(sql);
            listObj = query.getResultList();

            if (!listObj.isEmpty()) {
                for (Object[] obj : listObj) {
                    DashboardDto dashboardDto = new DashboardDto();
                    dashboardDto.setNombre((String) obj[0]);
                    dashboardDto.setMonto((BigDecimal) obj[1]);
                    list.add(dashboardDto);
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return list;
    }


    @SuppressWarnings("unchecked")
    public List<DashboardDto> cantMedidores(int mes, int year) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date anterior = cal.getTime();
        cal.setTime(anterior);
        if (mes <= 0) {
            mes = cal.get(Calendar.MONTH) + 1;
        }
        if (year <= 0) {
            year = cal.get(Calendar.YEAR);
        }

        String sql = "select 'Pagado' as des, count(s.ID_SERVICIO) as cantidad " +
                    "from SERVICIO s,MEDIDOR m,PAGO_SERVICIO ps " +
                    " where m.ID_SERVICIO = s.ID_SERVICIO " +
                    "and ps.ID_SERVICIO = s.ID_SERVICIO " +
                    "and extract(month from ps.PERIODO) = ?1 "+
                    "and extract(year from ps.PERIODO) =  ?2 " +
                    "and ps.ESTADO_PAGO = 'Confirmado' " +
                "union " +
                    "select 'No Pagado' as des, (s.cantidad + t.cantidad) as total " +
                    "from (select count(s.ID_SERVICIO) as cantidad " +
                            "from SERVICIO s, MEDIDOR m, PAGO_SERVICIO ps " +
                            "where m.ID_SERVICIO = s.ID_SERVICIO " +
                            "and ps.ID_SERVICIO = s.ID_SERVICIO " +
                            "and extract(month from ps.PERIODO) =?1 "+
                            "and extract(year from ps.PERIODO) = ?2 " +
                            "and ps.ESTADO_PAGO != 'Confirmado') s, " +
                         "(select count(s.ID_SERVICIO) as cantidad " +
                            "from SERVICIO s inner join MEDIDOR m on m.ID_SERVICIO = s.ID_SERVICIO " +
                            "left outer join PAGO_SERVICIO ps on s.ID_SERVICIO = ps.ID_SERVICIO " +
                            "where ps.ID_SERVICIO is null) t " +
                "union " +
                    "select 'Alta medidor' as des, count(s.ID_SERVICIO) as cantidad " +
                    "from SERVICIO s,MEDIDOR m " +
                    "where m.ID_SERVICIO = s.ID_SERVICIO " +
                    "and extract(month from m.FECHA_CREACION) =?1 "+
                    "and extract(year from m.FECHA_CREACION) = ?2 "+
                "union " +
                    "select 'Baja medidor' as des, count(s.ID_SERVICIO) as cantidad " +
                    "from SERVICIO s left join MEDIDOR m on s.ID_SERVICIO = m.ID_SERVICIO " +
                    "where m.ID_SERVICIO is null order by des desc ";

        List<DashboardDto> list = new ArrayList<>();
        List<Object[]> listObj;
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1,mes);
            query.setParameter(2,year);
            listObj = query.getResultList();

            if (!listObj.isEmpty()) {
                BigDecimal total = new BigDecimal(0);
                for (Object[] obj : listObj) {
                    DashboardDto dashboardDto = new DashboardDto();
                    dashboardDto.setNombre((String) obj[0]);
                    dashboardDto.setCantidad((BigDecimal) obj[1]);

                    total=total.add((BigDecimal) obj[1]);

                    list.add(dashboardDto);
                }
                DashboardDto dashboardDto = new DashboardDto();
                dashboardDto.setNombre("Total general");
                dashboardDto.setCantidad(total);
                list.add(dashboardDto);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return list;

    }
}