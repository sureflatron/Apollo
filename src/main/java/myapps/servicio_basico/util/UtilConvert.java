package myapps.servicio_basico.util;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

public class UtilConvert {
    private static final Logger logger = LogManager.getLogger(UtilConvert.class);
    /**
     * Convierte un Object Bigdecima a Long
     *
     * @param object
     * @return un objeto long
     */
    public static long convertToLong(Object object) {
        try {
            BigDecimal valor = (BigDecimal) object;
            return valor.longValue();
        } catch (NullPointerException e) {
            logger.log(Level.ERROR,"Existen valores nulos al realizar la conversion"+"|" +e.getMessage(),e);
        }
        return 0;
    }
}
