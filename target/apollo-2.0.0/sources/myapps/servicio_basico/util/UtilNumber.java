package myapps.servicio_basico.util;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilNumber implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Logger log = Logger.getLogger(UtilNumber.class);
    @EJB
    private static IApplicationContext beanApplicationContext;

    public static boolean esNroTigo(String isdn) {
        // log.info("Parametros.expresionRegularNroTigo: " +
        // Parametros.expresionRegularNroTigo);
        Pattern pattern = Pattern.compile(beanApplicationContext
                .getFormParameters().get(EnumParametros
                        .EXPRESION_REGULAR_NRO_TIGO.toString()));
        // Pattern pat = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(isdn);
        if (matcher.find()) {
            log.debug("nro=" + isdn + " es valido");
            return true;

        }
        log.debug("nro=" + isdn + " NO es valido");
        return false;
    }

    public static double redondear(double numero, int digitos) {

        // BigDecimal bd = new BigDecimal(numero);
        // bd.setScale(digitos, RoundingMode.HALF_UP);
        // return bd.doubleValue();

        int cifras = (int) Math.pow(10, digitos);
        return Math.rint(numero * cifras) / cifras;
    }

    public static String doubleToString(double number, int nroDecimales) {
        String result = "";
        // %n
        String format = "%." + nroDecimales + "f";
        try {
            // String country = System.getProperty("user.country");
            // String lan = System.getProperty("user.language");
            // log.debug("country: " + country);
            // log.debug("lan: " + lan);

            // result = String.format(Locale.US, format, number);
            Locale locale = new Locale("es", "BO");
            result = String.format(locale, format, number);
            // result = String.format(format, number);

        } catch (Exception e) {
            log.error("Error al convertir double a string: ", e);
        }
        return result;
    }


    public static String doubleToString(double number, int nroDecimales, String language, String country) {
        String result = "";
        // %n
        String format = "%." + nroDecimales + "f";
        try {
            // result = String.format(Locale.US, format, number);
            Locale locale = new Locale(language, country);
            result = String.format(locale, format, number);

        } catch (Exception e) {
            log.error("Error al convertir double a string: ", e);
        }
        return result;
    }

    public static boolean isNumeric(String cadena) {
        boolean resultado;
        try {
            int i = Integer.parseInt(cadena);
            resultado = true;
            log.info(i);
        } catch (NumberFormatException excepcion) {
            resultado = false;
            log.error("Error aco convertir String a entero" + excepcion.getMessage());
        }
        return resultado;
    }

    public static boolean validateMonto(String monto) {

        try {
            new BigDecimal(monto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
