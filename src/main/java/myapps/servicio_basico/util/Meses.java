package myapps.servicio_basico.util;

public class Meses {
    private int numMes;
    private String mes;

    public Meses(int numMes, String mes) {
        this.numMes = numMes;
        this.mes = mes;
    }

    public Meses() {
    }

    public int getNumMes() {
        return numMes;
    }

    public void setNumMes(int numMes) {
        this.numMes = numMes;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

}
