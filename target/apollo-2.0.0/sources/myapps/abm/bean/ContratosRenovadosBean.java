package myapps.abm.bean;

import myapps.abm.business.HistorialContratosBL;
import myapps.abm.model.HistorialContratosEntity;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class ContratosRenovadosBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
    private HistorialContratosBL historialContratosBL;

    private transient List<HistorialContratosEntity> historialContratosEntityList;

    private transient HistorialContratosEntity historialContratosEntitySelected;

    private static final String INMUEBLE = "Inmueble";
    private static final String SERVICIO = "Servicio";
    private static final String PROVEEDOR = "Proveedor";
    private static final String RADIO_BASE = "Radio Base";
    private static final String SIN_ASIGNACION = "Sin Asignación";

    @PostConstruct
    public void init(){
        setHistorialContratosEntityList(new ArrayList<>());
        setHistorialContratosEntityList(historialContratosBL.listAll());
    }

    public boolean colorContratoVencido(HistorialContratosEntity historialContratosEntity){
        return historialContratosEntity.getContratosRenovadosByContratoRevoId().getEstadoContrato().equals
                ("Finalizado");
    }


    //region GETTER AND SETTER OJB


    public HistorialContratosEntity getHistorialContratosEntitySelected() {
        return historialContratosEntitySelected;
    }

    public void setHistorialContratosEntitySelected(HistorialContratosEntity historialContratosEntitySelected) {
        this.historialContratosEntitySelected = historialContratosEntitySelected;
    }

    public String getInmueble() {
        return INMUEBLE;
    }

    public String getServicio() {
        return SERVICIO;
    }

    public String getProveedor() {
        return PROVEEDOR;
    }

    public String getRadioBase() {
        return RADIO_BASE;
    }

    public String getSinAsignacion() {
        return SIN_ASIGNACION;
    }
    //endregion

    //region GETTER AND SETTER LIST

    public List<HistorialContratosEntity> getHistorialContratosEntityList() {
        return historialContratosEntityList;
    }

    public void setHistorialContratosEntityList(List<HistorialContratosEntity> historialContratosEntityList) {
        this.historialContratosEntityList = historialContratosEntityList;
    }


    //endregion


}
