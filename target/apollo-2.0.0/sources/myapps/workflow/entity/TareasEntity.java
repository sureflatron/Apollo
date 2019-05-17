package myapps.workflow.entity;

public class TareasEntity {
    private Long idProcedimiento;
    private Long idTarea;
    private String tarea;
    private String asignado;
    private String comentario;
    private String estado;

    public TareasEntity() {
    }

    public TareasEntity(Long idProcedimiento, Long idTarea, String tarea, String asignado) {
        this.idProcedimiento = idProcedimiento;
        this.idTarea = idTarea;
        this.tarea = tarea;
        this.asignado = asignado;
    }



    public Long getIdProcedimiento() {
        return idProcedimiento;
    }

    public void setIdProcedimiento(Long idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }

    public Long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getAsignado() {
        return asignado;
    }

    public void setAsignado(String asignado) {
        this.asignado = asignado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}