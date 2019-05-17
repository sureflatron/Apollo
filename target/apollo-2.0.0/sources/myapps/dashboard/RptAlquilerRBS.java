package myapps.dashboard;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;


@ManagedBean
@ViewScoped
public class RptAlquilerRBS implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(RptAlquilerRBS.class);
    private BarChartModel barModel;

    @PostConstruct
    void init() {
        try {
            createBarModels();
        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }

    private void createBarModels() {
        createBarModel();
    }

    private void createBarModel() {
        barModel = initBarModel();

        barModel.setTitle("Pago por mes");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Mes");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Pago");
        yAxis.setMin(0);
        yAxis.setMax(10000);
    }

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries boys = new ChartSeries();
        boys.setLabel("Mes");
        boys.set("Enero", 120);
        boys.set("Febrero", 100);
        boys.set("Marzo", 44);
        boys.set("Abril", 150);
        boys.set("Mayo", 25);
        boys.set("Junio", 25);
        boys.set("Julio", 25);
        boys.set("Agosto", 25);
        boys.set("Septiembre", 25);
        boys.set("Octubre", 25);
        boys.set("Noviembre", 25);
        boys.set("Diciembre", 25);

        model.addSeries(boys);

        return model;
    }


    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }


}



