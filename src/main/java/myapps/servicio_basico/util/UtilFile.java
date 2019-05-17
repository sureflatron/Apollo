package myapps.servicio_basico.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class UtilFile {
    private static final Logger log = LogManager.getLogger(UtilFile.class);

    public static byte[] loadFile(String sourcePath) throws IOException {
        Path pdfPath = Paths.get(sourcePath);
        return Files.readAllBytes(pdfPath);
    }

    public static void writeBytesToFile(byte[] bFile, String fileDest, String nombre, String
            extension) {

        FileOutputStream zos = null;
        ByteArrayInputStream bais = null;
        try {
            StringBuilder nameExtFile = new StringBuilder("");
            nameExtFile.append(nombre).append(extension);

            File miArchivo = new File(fileDest, nameExtFile.toString());
            zos = new FileOutputStream(miArchivo);

            bais = new ByteArrayInputStream(bFile);
            byte[] buffer = new byte[2048];
            int leido = 0;
            while (0 < (leido = bais.read(buffer))) {
                zos.write(buffer, 0, leido);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

    }


    public static boolean deleteFile(String path) {
        boolean result = false;
        File file = new File(path);
        if (file.delete()) {
            result = true;
        }
        return result;
    }

    public static String getExtensionFile(String nameFile) {
        return FilenameUtils.getExtension(nameFile);
    }

    /*TODO  POR SI SER REQUIERE REUTILIZAR

    public static void changeNameFile(String pathFile, String oldName, String newName) {
        File archivo = null;
        File archivo2;
        try {
            archivo = new File(pathFile + oldName);
            archivo2 = new File(pathFile + newName);
            archivo.renameTo(archivo2);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }*/

    public static String generatePrefijo() {
        SimpleDateFormat formatEntrada = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss S");
        Date fechaEntrada = new Date();
        String fecha = formatEntrada.format(fechaEntrada);
        return fecha.replace("/", "").replace(" ", "").replace(":", "");
    }


    public static String mostrarImagen(byte[] imagen) {
        String imageString = "";
        if (imagen != null) {
            imageString = "data:image/png;base64," + Base64.getEncoder().encodeToString(imagen);

        }
        return imageString;
    }

    public static byte[] writeExcel(List<Object[]> objectList) throws IOException {

        //String excelFileName = "D:/Test.xlsx";//name of excel file

        String sheetName = "Sheet1";//name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);
        Map<String, CellStyle> styles = createStyles(wb);
        int contRow = 0;
        for (Object[] obj : objectList) {
            XSSFRow row = sheet.createRow(contRow);
            int contCell = 0;

            for (Object s : obj) {

                XSSFCell cell = row.createCell(contCell);

                /*if (contCell == 0 && contRow > 0) {
                    nomAct = s.toString();
                }*/
                
                /*if (contCell == 1 && contRow > 0) {
                    descripAct = s.toString();
                }*/

                if (contRow == 0) {
                    cell.setCellValue(s.toString());
                    cell.setCellStyle(styles.get("header"));
                } else {
                    /*if (nomAct.equals(nomAnt) && contCell == 0) {
                        cell.setCellValue("");
                        cell.setCellStyle(styles.get("cell_normal_rol"));
                    }
                    if (descripAct.equals(descripAnt) && contCell == 1) {
                        cell.setCellValue("");
                        cell.setCellStyle(styles.get("cell_normal_rol"));
                    } else {*/
                    cell.setCellValue(s.toString());
                      /*  if (contCell == 0 || contCell==1) {
                            cell.setCellStyle(styles.get("cell_normal_rol"));
                            nomAnt = nomAct;
                            descripAnt = descripAct;
                        } else {
                            cell.setCellStyle(styles.get("cell_normal"));
                        }*/
                    //}
                }

                contCell++;
            }
            contRow++;
        }

/*
        sheet.addMergedRegion(new CellRangeAddress(1, contRow - 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(1, contRow - 1, 1, 1));*/


        /*//iterating r number of rows
        for (int r = 0; r < 5; r++) {
            XSSFRow row = sheet.createRow(r);

            //iterating c number of columns
            for (int c = 0; c < 5; c++) {
            for (int c = 0; c < 5; c++) {
                XSSFCell cell = row.createCell(c);

                cell.setCellValue("Cell " + r + " " + c);
            }
        }*/

        // FileOutputStream fileOut = new FileOutputStream(excelFileName);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wb.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

        //write this workbook to an Outputstream.
/*        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();*/
    }

    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        DataFormat df = wb.createDataFormat();

        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put("header", style);

        Font font1 = wb.createFont();
        font1.setBold(true);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font1);
        styles.put("cell_b", style);


        Font font2 = wb.createFont();
        font2.setColor(IndexedColors.BLUE.getIndex());
        font2.setBold(true);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font2);
        styles.put("cell_bb", style);

        Font font3 = wb.createFont();
        font3.setFontHeightInPoints((short) 14);
        font3.setColor(IndexedColors.DARK_BLUE.getIndex());
        font3.setBold(true);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font3);
        style.setWrapText(true);
        styles.put("cell_h", style);

        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(true);
        styles.put("cell_normal", style);

        style = createBorderedStyle(wb);
        //style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        styles.put("cell_normal_rol", style);


        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        styles.put("cell_normal_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setWrapText(true);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_normal_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setIndention((short) 1);
        style.setWrapText(true);
        styles.put("cell_indented", style);

        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.put("cell_blue", style);

        return styles;
    }

    private static CellStyle createBorderedStyle(Workbook wb) {
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();

        CellStyle style = wb.createCellStyle();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
        return style;
    }

    public static StreamedContent media(String url) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("D:\\Datos\\Dev\\ArvhivosAdjuntos\\null-1249-1428-1-PB.pdf");
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        return new DefaultStreamedContent(bis);
    }


    public static void main(String[] args) {
        media("D:\\Datos\\Dev\\ArvhivosAdjuntos\\Datos.xls");
    }

}
