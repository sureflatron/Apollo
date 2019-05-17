package myapps.servicio_basico.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class UtilDate implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(UtilDate.class);
    private static final String DD_MM_YYYY = "dd/MM/yyyy";

    public static java.sql.Date dateToSql(Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static String dateToTimeStampSql(Date date) {
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.format(ts);
    }

    public static Timestamp dateToTimeStamp(Date date) {
        return new Timestamp(date.getTime());
    }

    //Convert Date to Calendar
    public static Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    public static Date stringToDate(String fecha, String format) {
        Date date = null;
        try {

            DateFormat formatter;
            formatter = new SimpleDateFormat(format);
            date = formatter.parse(fecha);

        } catch (Exception e) {
            e.getMessage();
        }
        return date;
    }

    public static String dateToString(Date fecha, String format) {
        String date = "";
        try {
            DateFormat formatter = new SimpleDateFormat(format);
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = formatter.format(fecha);

        } catch (Exception e) {
            e.getMessage();
        }
        return date;
    }

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);

        return calendar.getTime();
    }

    public static String stringToCalendarXML(String fecha) {
        String resultado = "";
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
            Date date = formatter.parse(fecha);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            DateFormat formatterr = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            return formatterr.format(calendar.getTime());

        } catch (ParseException e) {
            e.getMessage();
        }
        return resultado;
    }

    public static Date timeStampToDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }


    public static Long numeroDiasEntreDosFechas(Date fecha1, Date fecha2) {
        return ((fecha2.getTime() - fecha1.getTime()) / 86400000);
    }

    /**
     * @param fechaCompare
     * @return 0 si son iguales, 1 si fechaDate1 es mayor, 2 si fechaDate2 es mayor
     */
    public static int comparaFechaDate(Date fechaCompare) {

        int resultado = 0;
        try {

            Date fechaActual = new Date();

            String fechaSistema = dateToString(fechaActual, DD_MM_YYYY);
            String fecha = dateToString(fechaCompare, DD_MM_YYYY);

            Date fechaDate1 = stringToDate(fecha, DD_MM_YYYY);
            Date fechaDate2 = stringToDate(fechaSistema, DD_MM_YYYY);

            if (fechaDate1 != null && fechaDate1.before(fechaDate2)) {
                resultado = 1;
            } else {
                if (fechaDate2 != null && fechaDate2.before(fechaDate1)) {
                    resultado = 2;
                } else {
                    resultado = 0;
                }
            }
        } catch (Exception e) {
            log.error("Se Produjo un Error!!!  " + e.getMessage());
        }
        return resultado;
    }

}
