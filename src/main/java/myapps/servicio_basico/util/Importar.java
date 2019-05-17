package myapps.servicio_basico.util;

import myapps.cargainicial.ExcelUtil;
import jxl.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Importar {
    private static final Logger log = LogManager.getLogger(Importar.class);

    public static List<Boolean> operacion(int columnas) {
        List<Boolean> list = new ArrayList<>();
        for (int i = 1; i <= 27; i++) {
            list.add(campo(i, columnas));
        }
        return list;
    }

    public static boolean campo(int index, int columnas) {
        boolean valor = false;
        if (index <= columnas) {
            valor = true;
        }
        return valor;
    }

    public static ExcelUtil obtenerDatos(byte[] file) {
        ExcelUtil excelUtil = new ExcelUtil();
        try {
            InputStream fileInputStream = new ByteArrayInputStream(file);
            Workbook archivoExcel = Workbook.getWorkbook(fileInputStream);
            excelUtil.setHojas(archivoExcel.getNumberOfSheets());
            excelUtil.setColumnas(archivoExcel.getSheet(0).getColumns());
            excelUtil.setRows(archivoExcel.getSheet(0).getRows());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return excelUtil;
    }


    public static List<String[]> leerExcel(byte[] file) {


        ArrayList<String[]> data = new ArrayList<>();
        try {
            InputStream fileInputStream = new ByteArrayInputStream(file);
            Workbook archivoExcel = Workbook.getWorkbook(fileInputStream);
            Sheet hoja = archivoExcel.getSheet(0);
            int numColumnas = hoja.getColumns();
            int numFilas = hoja.getRows();
            String dato = "";
            for (int fila = 0; fila < numFilas; fila++) {
                String[] filas = new String[numColumnas];
                int contador = 0;
                for (int columna = 0; columna < numColumnas; columna++) {
                    Cell cell = hoja.getCell(columna, fila);
                    if (cell.getType() == CellType.DATE) {
                        Date dateCell = ((DateCell) cell).getDate();
                        dato = UtilDate.dateToString(dateCell, "dd/MM/yyyy");

                    } else {
                        dato = hoja.getCell(columna, fila).getContents();
                    }
                    hoja.getCell(columna, fila).getType();

                    filas[contador] = dato;
                    if ((contador + 1) % numColumnas == 0) {
                        data.add(filas);
                    }
                    contador++;
                }
            }
            archivoExcel.close();
            fileInputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return data;
    }


    public Date stringDate(String fecha) {
        SimpleDateFormat formatter;
        Date date = new Date();
        int count = 1;
        boolean formato = true;
        while (formato) {
            switch (count) {
                case 1:
                    formatter = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        date = formatter.parse(fecha);
                        formato = false;
                    } catch (ParseException e) {
                        count++;
                    }
                    break;
                case 2:
                    formatter = new SimpleDateFormat("dd-MM-yy");
                    try {
                        date = formatter.parse(fecha);
                        formato = false;
                    } catch (ParseException e) {
                        count++;
                    }
                    break;
                case 3:
                    formatter = new SimpleDateFormat("M/dd/yy");
                    try {
                        date = formatter.parse(fecha);
                        formato = false;
                    } catch (ParseException e) {
                        count++;
                    }
                    break;
                case 4:
                    formatter = new SimpleDateFormat("dd-MMMM-yy");
                    try {
                        date = formatter.parse(fecha);
                        formato = false;
                    } catch (ParseException e) {
                        count++;
                    }
                    break;
                case 5:
                    formatter = new SimpleDateFormat("dd/M/yy");
                    try {
                        date = formatter.parse(fecha);
                        formato = false;
                    } catch (ParseException e) {
                        count++;
                    }
                    break;
                default:
                    log.error("No se pudo formatear la fecha");
                    break;
            }
        }
        return date;
    }


    public static String letras(int i) {
        char s;
        s = (char) ('A' + i);
        return s + "";
    }


    public static void main(String[] args) {
        operacion(10);
    }
}
