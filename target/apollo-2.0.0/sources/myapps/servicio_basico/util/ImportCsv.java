package myapps.servicio_basico.util;

import myapps.cargainicial.ExcelUtil;
import com.opencsv.CSVReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImportCsv {
    private static final Logger log = LogManager.getLogger(ImportCsv.class);
    private static final char SEPARATOR = ';';
    private static final char QUOTE = '"';

    public List<String[]> leerCsv(InputStream pathFile) throws IOException {

        CSVReader reader = null;
        ArrayList<String[]> data = new ArrayList<>();
        try {
            reader = new CSVReader(new InputStreamReader(pathFile), SEPARATOR, QUOTE);
            String[] nextLine = null;

            while ((nextLine = reader.readNext()) != null) {

                data.add(nextLine);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (null != reader) {
                reader.close();
            }
        }
        return data;
    }

    public ExcelUtil obtenerDatos(List<String[]> file) {
        ExcelUtil excelUtil = new ExcelUtil();
        try {
            int contColumn = 0;
            int contRows = 0;
            int repeatColumn = 0;
            for (String[] m : file) {
                if (repeatColumn == 0) {
                    contColumn = m.length;
                }
                repeatColumn++;
                contRows++;
            }
            excelUtil.setHojas(1);
            excelUtil.setColumnas(contColumn);
            excelUtil.setRows(contRows - 1);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return excelUtil;
    }


}
