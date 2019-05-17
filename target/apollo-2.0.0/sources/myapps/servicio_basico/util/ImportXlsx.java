package myapps.servicio_basico.util;

import myapps.cargainicial.ExcelUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class ImportXlsx {

    private static final Logger log = LogManager.getLogger(ImportXlsx.class);
    private static int nroColumn = 0;

    public static List<String[]> leerrXlsx(byte[] file) {
        ArrayList<String[]> data = new ArrayList<>();
        Workbook workbook = null;
        try {
            nroColumn = obtenerDatos(file).getColumnas();
            InputStream fileInputStream = new ByteArrayInputStream(file);
            workbook = new XSSFWorkbook(fileInputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();
            DataFormatter formatter = new DataFormatter();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                String[] filas = new String[nroColumn];
                while (cellIterator.hasNext()) {
                    String contenidoCelda = "";
                    Cell cell = cellIterator.next();
                    int nroCell = cell.getColumnIndex();

                    if (cell != null && cell.getCellTypeEnum() != CellType.STRING && cell
                            .getCellTypeEnum() == CellType.NUMERIC && DateUtil.isCellDateFormatted
                            (cell)) {
                        Date dateCell = cell.getDateCellValue();

                        contenidoCelda = UtilDate.dateToString(dateCell, "dd/MM/yyyy");
                    } else {
                        contenidoCelda = formatter.formatCellValue(cell);
                    }
                    filas[nroCell] = contenidoCelda;
                }
                data.add(filas);
            }
            workbook.close();
            fileInputStream.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e2) {
                    log.error(e2.getMessage());
                }
            }
        }
        return data;
    }

    public static ExcelUtil obtenerDatos(byte[] file) {
        ExcelUtil excelUtil = new ExcelUtil();
        Workbook archivoExcel = null;
        try {
            InputStream fileInputStream = new ByteArrayInputStream(file);
            archivoExcel = new XSSFWorkbook(fileInputStream);
            Sheet firstSheet = archivoExcel.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();
            Row nextRow = iterator.next();
            int n = nextRow.getPhysicalNumberOfCells();
            int m = firstSheet.getLastRowNum();
            excelUtil.setHojas(archivoExcel.getNumberOfSheets());
            excelUtil.setColumnas(n);
            excelUtil.setRows(m);
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            if (archivoExcel != null) {
                try {
                    archivoExcel.close();
                } catch (Exception e2) {
                    log.error(e2.getMessage());
                }
            }
        }
        return excelUtil;
    }


    private byte[] fileByte;
    private List<String[]> excelReaded;
    private ResumeLoadExcel resumeLoadExcel;

    private int validFields = 0;
    private int noValidFields = 0;

    public ImportXlsx(byte[] fileByte) {
        excelReaded = new ArrayList<>();
        resumeLoadExcel = new ResumeLoadExcel();
        this.fileByte = fileByte;

        excelReaded = readFileExcel();
        //contValidFields();
        resumeLoadExcel.setValidFields(validFields);
        resumeLoadExcel.setNoValidFields(noValidFields);
    }

    private List<String[]> readFileExcel() {
        List<String[]> data = new ArrayList<>();

        try {

            //  FileInputStream inputStream = new FileInputStream(new File(rutaArchivoExcel));
            //workbook = new XSSFWorkbook(inputStream);
            //FileInputStream file = new FileInputStream(new File(pathFile));
            InputStream inputStream = new ByteArrayInputStream(this.fileByte);
            Workbook libroExcel = WorkbookFactory.create(inputStream);
            //obtener la hoja que se va leer
            Sheet sheet = libroExcel.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            Row nextRow1 = iterator.next();

            resumeLoadExcel.setNumColumns(nextRow1.getPhysicalNumberOfCells());
            resumeLoadExcel.setNumRows(sheet.getLastRowNum());

            DataFormatter formatter = new DataFormatter();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                //int n = nextRow.getPhysicalNumberOfCells();
                String[] filas = new String[resumeLoadExcel.getNumColumns()];
                int cont = 0;
                while (cellIterator.hasNext()) {
                    String contenidoCelda = "";
                    Cell cell = cellIterator.next();

                    //int nroCell = cell.getColumnIndex();
                    if (cell != null && cell.getCellTypeEnum() != CellType.STRING && cell
                            .getCellTypeEnum() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                        Date dateCell = cell.getDateCellValue();
                        contenidoCelda = UtilDate.dateToString(dateCell, "dd/MM/yyyy");
                    } else {
                        contenidoCelda = formatter.formatCellValue(cell);
                    }
                    if(cont<resumeLoadExcel.getNumColumns()){
                        filas[cont] = contenidoCelda;
                    }

                    cont++;
                }
               // if (filas==null){
                    data.add(filas);
                //}
            }

            libroExcel.close();
        } catch (Exception e) {
            //
        } finally {
           /* if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e2) {
                    System.out.println(e2.getMessage());
                }
            }*/
        }
        return data;
    }
    public List<String[]> getExcelReaded() {
        return excelReaded;
    }

    public ResumeLoadExcel getResumeLoadExcel() {
        return resumeLoadExcel;
    }

}
