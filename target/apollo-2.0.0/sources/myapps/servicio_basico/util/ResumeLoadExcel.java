package myapps.servicio_basico.util;

import java.io.Serializable;

public class ResumeLoadExcel implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nameFile;
    private int numColumns;
    private int numRows;
    private int validFields;
    private int noValidFields;
    private String extentionFile;
    private int numSave;

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getValidFields() {
        return validFields;
    }

    public void setValidFields(int validFields) {
        this.validFields = validFields;
    }

    public int getNoValidFields() {
        return noValidFields;
    }

    public void setNoValidFields(int noValidFields) {
        this.noValidFields = noValidFields;
    }

    public String getExtentionFile() {
        return extentionFile;
    }

    public void setExtentionFile(String extentionFile) {
        this.extentionFile = extentionFile;
    }

    public int getNumSave() {
        return numSave;
    }

    public void setNumSave(int numSave) {
        this.numSave = numSave;
    }
}
