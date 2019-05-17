package myapps.workflow.dao;

import myapps.workflow.entity.TareasEntity;

import java.util.ArrayList;
import java.util.List;


public class TareasDAO {
    private static String[] tarea;
    private static String[] asignado;

    static {
        tarea = new String[10];
        tarea[0] = "Contratar Servicio";
        tarea[1] = "Pagar Medidor";
        tarea[2] = "Nuevo Medidor";
        tarea[3] = "Dar de Baja Servicio";
        tarea[4] = "Obtener Licencia";
        tarea[5] = "Renovar Contrato";
        tarea[6] = "Consequir Alquiler";
        tarea[7] = "Instalar Medidor";
        tarea[8] = "Cancelar Alquiler";
        tarea[9] = "Asignar Inmueble";

        asignado = new String[10];
        asignado[0] = "Cristian Perez";
        asignado[1] = "Jaiver Lijeron Hinojosa";
        asignado[2] = "Marcelo Fabiani";
        asignado[3] = "Juan Carlos Chávez Antelo";
        asignado[4] = "David Arroyo Pinto";
        asignado[5] = "Juan Carlos Rojas";
        asignado[6] = "Carlos Aguilera";
        asignado[7] = "Jose Pinheiro";
        asignado[8] = "Moises Lozada";
        asignado[9] = "Miguel Angel Parada";
    }

    public List<TareasEntity> listAll() {
        List<TareasEntity> lista = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            lista.add(new TareasEntity(new Long(getRandomId()), new Long(getRandomId()), getRandomTarea(), getRandomAsignado()));
        }
        return lista;
    }

    private int getRandomId() {
        return (int) (Math.random() * 1000)+1;
    }

    private String getRandomTarea() {
        return tarea[(int) (Math.random() * 10)];
    }

    private String getRandomAsignado() {
        return asignado[(int) (Math.random() * 10)];
    }

}
