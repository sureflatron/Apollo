package myapps.servicio_basico.util;

public class ValidarUtil {

    private ValidarUtil() {
    }

    public static String isEmptyValue(String value){
        String stringResult="";
        if (value==null){
            stringResult="";
        }else {
            stringResult = value;
        }
        return stringResult;
    }
}
