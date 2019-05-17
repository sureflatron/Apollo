package myapps.cargas_masivas;

import myapps.abm.business.*;
import myapps.abm.model.*;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class DetalleHistPagoMasBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(DetalleHistPagoMasBean.class);

    @Inject
    private ConfiguracionesBL configuracionesBL;

    private List<PagoServicioEntity> pagoServicioEntityListAllByHIstorial = new ArrayList<>();

    private String userName;

    private int nroProcesados;
    private int nroNoProcesados;
    private String nombreConfig;

    public void editar() {
        try {
            HistorialPagosMasivosEntity entity = (HistorialPagosMasivosEntity) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRequestMap().get("hpagosObj");
            pagoServicioEntityListAllByHIstorial = (List<PagoServicioEntity>) entity.getPagoServiciosById();
            setUserName(((List<PagoServicioEntity>) entity.getPagoServiciosById()).get(0).getUsuario());
            setNroProcesados(entity.getProcesados().intValue());
            setNroNoProcesados(entity.getNoprocesados().intValue());
            setNombreConfig(configuracionesBL.getById(entity.getConfiguracionesByConfiguracionId()
                    .getIdConfiguraciones()).getNombre());
        } catch (Exception e) {
            log.log(Level.ERROR, "ERRRO AL CARGAR LA LISTA DE PAGOS" + "|" + e.getMessage(), e);
        }
    }

    public List<PagoServicioEntity> getPagoServicioEntityListAllByHIstorial() {
        return pagoServicioEntityListAllByHIstorial;
    }

    public void setPagoServicioEntityListAllByHIstorial(List<PagoServicioEntity> pagoServicioEntityListAllByHIstorial) {
        this.pagoServicioEntityListAllByHIstorial = pagoServicioEntityListAllByHIstorial;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNroProcesados() {
        return nroProcesados;
    }

    public void setNroProcesados(int nroProcesados) {
        this.nroProcesados = nroProcesados;
    }

    public int getNroNoProcesados() {
        return nroNoProcesados;
    }

    public void setNroNoProcesados(int nroNoProcesados) {
        this.nroNoProcesados = nroNoProcesados;
    }

    public String getNombreConfig() {
        return nombreConfig;
    }

    public void setNombreConfig(String nombreConfig) {
        this.nombreConfig = nombreConfig;
    }
}

