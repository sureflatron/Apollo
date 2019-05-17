package myapps.cargainicial;


import myapps.abm.business.MonedaBL;
import myapps.abm.model.CamposEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;

public class ExcelUtil implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(MonedaBL.class);

    int hojas;
    int columnas;
    int rows;

    public int getHojas() {
        return hojas;
    }

    public void setHojas(int hojas) {
        this.hojas = hojas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void tiempo(int tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (Exception e) {
            logger.warn("Error al pedir tiempo " + e.getMessage());
        }
    }

    public boolean equalLists(List<CamposEntity> a, List<CamposEntity> b) {
        int contador = 0;
        boolean repetidos = false;
        if (a.size() == b.size()) {
            for (CamposEntity camposA : a) {
                for (CamposEntity camposB : b) {
                    if (camposA.getNumero() == camposB.getNumero() && camposA.getNombre().equals(camposB.getNombre()) && camposA.getLetra().equals(camposB.getLetra())) {
                        contador++;
                    }
                }

            }
            if (contador == b.size()) {
                repetidos = true;
            }
        }
        return repetidos;
    }
}
