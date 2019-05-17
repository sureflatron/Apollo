package myapps.dashboard;

import myapps.abm.business.PagoServicioBL;
import myapps.abm.model.PagoServicioEntity;
import myapps.servicio_basico.util.Meses;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by michael on 11/1/2019.
 */

@ManagedBean
@ViewScoped
public class ChartView implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ChartView.class);

    private BarChartModel barModelPago;
    private BarChartModel barModel;
    private BarChartModel barCantMedidores;
    private HorizontalBarChartModel horizontalBarModel;
    private PieChartModel pieModel1;

    private PieChartModel pieModel2;

    private PieChartModel pieModelMontosPagados;
    private PieChartModel pieModelCantidadPagados;

    private LineChartModel dateModel;
    PagoServicioEntity lstMedidoresNoPagados = new PagoServicioEntity();
    PagoServicioEntity cantidadPagos = new PagoServicioEntity();

    private List<PagoServicioEntity> pagoServicioEntityList = new ArrayList<>();
    private List<PagoServicioEntity> proveedoresEntityList = new ArrayList<>();
    private List<PagoServicioEntity> cantidadMedidoresEntityList = new ArrayList<>();
    private List<PagoServicioEntity> pagoServicioEntityListYear = new ArrayList<>();
    private List<String> lstYear = new ArrayList<>();
    private transient List<Meses> lstMeses = new ArrayList<>();
    private transient Meses meses;


    @Inject
    private PagoServicioBL pagoServicioBL;

    private Date fechaFilter;
    private int mes;
    private int year;
    private int numAnio;
    private int numMes;

    @PostConstruct
    public void init() {

        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.MONTH, -1);
        Date anterior = calendario.getTime();
        calendario.setTime(anterior);
        numMes = calendario.get(Calendar.MONTH) + 1;
        numAnio = calendario.get(Calendar.YEAR);

        meses = new Meses();
        createBarModels();
        createPieModels();
        createDateModel();
        cargarMeses();
        lstYear = pagoServicioBL.obtenerYear();
        if (lstYear.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            lstYear.add(String.valueOf(cal.get(Calendar.YEAR)));
        }
    }

    public void filtrar() {
        createBarModels();
    }

    public void cargarModel() {
        numAnio = getYear();
        numMes = meses.getNumMes();
        int max = montoMax();
        createBarModel(max);
        createBarModelPago(max);
        createPieModels();
        cantDetalleMedidores();
    }

    private BarChartModel initBarModel() {

        barModel = new BarChartModel();

        if (numMes == 0) {
            pagoServicioEntityList = pagoServicioBL.listAllByServicioMes(0, 0);
        } else {
            pagoServicioEntityList = pagoServicioBL.listAllByServicioMes(numMes, numAnio);
        }

        if (pagoServicioEntityList.isEmpty()) {
            ChartSeries objServicio = new ChartSeries();
            objServicio.setLabel("");
            objServicio.set(obtenerMes(numMes) + "-" + numAnio, 0);
            barModel.addSeries(objServicio);
        } else {
            //recorremos la lista de servicios
            for (PagoServicioEntity servicio : pagoServicioEntityList) {
                ChartSeries objServicio = new ChartSeries();
                objServicio.setLabel(servicio.getServicio());
                objServicio.set(obtenerMes(numMes) + "-" + numAnio, servicio.getMontoXMes());
                barModel.addSeries(objServicio);
            }
        }
        return barModel;
    }

    private BarChartModel initBarModelPago() {

        barModelPago = new BarChartModel();

        proveedoresEntityList = pagoServicioBL.listAllProveedores();
        ChartSeries pagados = new ChartSeries();
        pagados.setLabel("Pagados");
        ChartSeries noPagados = new ChartSeries();
        noPagados.setLabel("No Pagados");

        if (proveedoresEntityList != null) {
            for (PagoServicioEntity proveedor : proveedoresEntityList) {
                if (numMes == 0) {
                    cantidadMedidoresEntityList = pagoServicioBL.listAllCantidadMedidorByProveedor(0, 0, proveedor.getIdProveedor());
                } else {
                    cantidadMedidoresEntityList = pagoServicioBL.listAllCantidadMedidorByProveedor(numMes, numAnio, proveedor.getIdProveedor());
                }

                for (PagoServicioEntity medidor : cantidadMedidoresEntityList) {
                    pagados.set(proveedor.getNombreProveedor(), medidor.getMedidorPagado());
                    noPagados.set(proveedor.getNombreProveedor(), medidor.getMedidorNoPagado());

                }
            }
        }
        barModelPago.addSeries(pagados);
        barModelPago.addSeries(noPagados);
        return barModelPago;
    }

    private String obtenerMes(int mes) {
        String monthString;
        switch (mes) {
            case 1:
                monthString = "Enero";
                break;
            case 2:
                monthString = "Febrero";
                break;
            case 3:
                monthString = "Marzo";
                break;
            case 4:
                monthString = "Abril";
                break;
            case 5:
                monthString = "Mayo";
                break;
            case 6:
                monthString = "Junio";
                break;
            case 7:
                monthString = "Julio";
                break;
            case 8:
                monthString = "Agosto";
                break;
            case 9:
                monthString = "Septiembre";
                break;
            case 10:
                monthString = "Octubre";
                break;
            case 11:
                monthString = "Noviembre";
                break;
            case 12:
                monthString = "Diciembre";
                break;
            default:
                monthString = "Invalid month";
                break;
        }
        return monthString;
    }

    private int montoMax() {
        int max = 0;
        for (int i = 0; i < pagoServicioEntityList.size(); i++) {
            if (pagoServicioEntityList.get(i).getMontoXMes().intValue() > max) {
                max = pagoServicioEntityList.get(i).getMontoXMes().intValue();
                max *= 2;
            }
        }
        return max;
    }

    private void createBarModels() {
        int max = montoMax();
        createBarModel(max);
        createBarModelPago(max);
        cantDetalleMedidores();
    }

    private void createBarModel(int max) {
        barModel = initBarModel();
        if (max == 0) {
            max = 1000;
        }
        barModel.setTitle("Servicio pago por mes");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Mes");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Pago");
        yAxis.setMin(0);
        //yAxis.setMax(5000);
    }

    private void createBarModelPago(int max) {
        barModelPago = initBarModelPago();
        if (max == 0) {
            max = 200;
        }
        barModelPago.setTitle("Pago SSBB " + obtenerMes(numMes) + "-" + numAnio);
        barModelPago.setLegendPosition("ne");
        barModelPago.setStacked(true);

        Axis xAxis = barModelPago.getAxis(AxisType.X);
        xAxis.setLabel("Servicio");

        Axis yAxis = barModelPago.getAxis(AxisType.Y);
        yAxis.setLabel("Pago");
        yAxis.setMin(0);
       // yAxis.setMax(10);
    }

    private void createPieModels() {
        createPieModel1();
        createPieModel2();
        createPieModelMontosPagados();
        createPieModel3();

    }

    private void createPieModel1() {
        pieModel1 = new PieChartModel();

        if (numAnio == 0) {
            pagoServicioEntityListYear = pagoServicioBL.listAllByServicioYear(0);
        } else {
            pagoServicioEntityListYear = pagoServicioBL.listAllByServicioYear(numAnio);

        }

        if (pagoServicioEntityListYear.isEmpty()) {
            pieModel1.set("", 0);
        } else {
            for (PagoServicioEntity pago : pagoServicioEntityListYear) {
                pieModel1.set(pago.getServicio(), pago.getMontoXMes());
            }
        }

        pieModel1.setTitle("Servicio pago x año");
        pieModel1.setLegendPosition("w");
        pieModel1.setShadow(false);
    }

    private void createPieModel2() {

        pieModel2 = new PieChartModel();

        proveedoresEntityList = pagoServicioBL.listAllProveedores();

        for (PagoServicioEntity proveedor : proveedoresEntityList) {
            if (numMes == 0) {
                lstMedidoresNoPagados = pagoServicioBL.listAllMedidoresNoPagadosByProveedor(0, 0, proveedor.getIdProveedor());
            } else {
                lstMedidoresNoPagados = pagoServicioBL.listAllMedidoresNoPagadosByProveedor(numMes, numAnio, proveedor.getIdProveedor());
            }
            pieModel2.set(proveedor.getNombreProveedor(), lstMedidoresNoPagados.getMedidorNoPagado());
        }

        pieModel2.setTitle("Medidores no pagados");
        pieModel2.setLegendPosition("w");
        pieModel2.setShadow(false);
    }

    private void createPieModel3() {

        pieModelCantidadPagados = new PieChartModel();

        proveedoresEntityList = pagoServicioBL.listAllProveedores();

        for (PagoServicioEntity proveedor : proveedoresEntityList) {
            if (numMes == 0) {
                cantidadPagos = pagoServicioBL.listAllCantidadPagosByProveedor(0, 0, proveedor.getIdProveedor());
            } else {
                cantidadPagos = pagoServicioBL.listAllCantidadPagosByProveedor(numMes, numAnio, proveedor.getIdProveedor());
            }
            pieModelCantidadPagados.set(proveedor.getNombreProveedor(), cantidadPagos.getCantidadPagos());
        }

        pieModelCantidadPagados.setTitle("Cantidad Pagos");
        pieModelCantidadPagados.setLegendPosition("w");
        pieModelCantidadPagados.setShadow(false);
    }

    private void createDateModel() {

        dateModel = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");

        series1.set("2014-01-01", 51);
        series1.set("2014-01-06", 22);
        series1.set("2014-01-12", 65);
        series1.set("2014-01-18", 74);
        series1.set("2014-01-24", 24);
        series1.set("2014-01-30", 51);

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Series 2");

        series2.set("2014-01-01", 32);
        series2.set("2014-01-06", 73);
        series2.set("2014-01-12", 24);
        series2.set("2014-01-18", 12);
        series2.set("2014-01-24", 74);
        series2.set("2014-01-30", 62);

        dateModel.addSeries(series1);
        dateModel.addSeries(series2);

        dateModel.setTitle("Zoom for Details");
        dateModel.setZoom(true);
        dateModel.getAxis(AxisType.Y).setLabel("Values");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMax("2014-02-01");
        axis.setTickFormat("%b %#d, %y");

        dateModel.getAxes().put(AxisType.X, axis);
    }

    private void cargarMeses() {
        lstMeses.add(new Meses(1, "Enero"));
        lstMeses.add(new Meses(2, "Febrero"));
        lstMeses.add(new Meses(3, "Marzo"));
        lstMeses.add(new Meses(4, "Abril"));
        lstMeses.add(new Meses(5, "Mayo"));
        lstMeses.add(new Meses(6, "Junio"));
        lstMeses.add(new Meses(7, "Julio"));
        lstMeses.add(new Meses(8, "Agosto"));
        lstMeses.add(new Meses(9, "Septiembre"));
        lstMeses.add(new Meses(10, "Octubre"));
        lstMeses.add(new Meses(11, "Noviembre"));
        lstMeses.add(new Meses(12, "Diciembre"));
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public void setPieModel2(PieChartModel pieModel2) {
        this.pieModel2 = pieModel2;
    }

    private void createPieModelMontosPagados() {
        try {
            List<DashboardDto> list;
            if (numMes == 0) {
                list = pagoServicioBL.getPagosProveedor(mes, year);
            } else {
                list = pagoServicioBL.getPagosProveedor(numMes, numAnio);
            }
            pieModelMontosPagados = new PieChartModel();

            if (list.isEmpty()) {
                pieModelMontosPagados.set("Vacio", 0);
            } else {
                for (DashboardDto dashboardDto : list) {
                    pieModelMontosPagados.set(dashboardDto.getNombre(), dashboardDto.getMonto());
                }
            }


            pieModelMontosPagados.setTitle("MONTO PAGADOS");
            pieModelMontosPagados.setLegendPosition("w");
            pieModelMontosPagados.setDataFormat("value");
            pieModelMontosPagados.setShowDataLabels(true);
            pieModelMontosPagados.setDataLabelFormatString("%.8s");
            pieModelMontosPagados.setDataLabelThreshold(0);
            //pieModelMontosPagados.setShowDatatip(false);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    private void cantDetalleMedidores() {
        barCantMedidores = new BarChartModel();
        barCantMedidores.setTitle("MEDIDORES");
        barCantMedidores.setSeriesColors("339894,a23637,F74A4A,F52F2F,24386b");
        barCantMedidores.setExtender("ext");
        barCantMedidores.setShowPointLabels(true);
        barCantMedidores.setShowDatatip(false);
        barCantMedidores.setMouseoverHighlight(false);

        Axis xAxis = barCantMedidores.getAxis(AxisType.X);
        xAxis.setLabel("Estado");

        Axis yAxis = barCantMedidores.getAxis(AxisType.Y);
        yAxis.setLabel("Cantidad medidores");
        yAxis.setMin(0);


        ChartSeries chartSeries = new ChartSeries();

        List<DashboardDto> list;

        if (numMes == 0) {
            list = pagoServicioBL.cantMedidores(mes, year);
        } else {
            list = pagoServicioBL.cantMedidores(numMes, numAnio);
        }

        if (list.isEmpty()) {
            DashboardDto valueEmpty = new DashboardDto();
            valueEmpty.setNombre("Vacio");
            valueEmpty.setCantidad(BigDecimal.valueOf(0));
            chartSeries.set(valueEmpty.getNombre(), valueEmpty.getCantidad());
        } else {
            for (DashboardDto dashboardDto : list) {
                chartSeries.set(dashboardDto.getNombre(), dashboardDto.getCantidad());
            }
            barCantMedidores.addSeries(chartSeries);
        }
    }

    public PieChartModel getPieModelCantidadPagados() {
        return pieModelCantidadPagados;
    }

    public void setPieModelCantidadPagados(PieChartModel pieModelCantidadPagados) {
        this.pieModelCantidadPagados = pieModelCantidadPagados;
    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public BarChartModel getBarModelPago() {
        return barModelPago;
    }

    public void setBarModelPago(BarChartModel barModelPago) {
        this.barModelPago = barModelPago;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<String> getLstYear() {
        return lstYear;
    }

    public void setLstYear(List<String> lstYear) {
        this.lstYear = lstYear;
    }

    public List<Meses> getLstMeses() {
        return lstMeses;
    }

    public void setLstMeses(List<Meses> lstMeses) {
        this.lstMeses = lstMeses;
    }

    public Meses getMeses() {
        return meses;
    }

    public void setMeses(Meses meses) {
        this.meses = meses;
    }

    public Date getFechaFilter() {
        return fechaFilter;
    }

    public void setFechaFilter(Date fechaFilter) {
        this.fechaFilter = fechaFilter;
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public PieChartModel getPieModelMontosPagados() {
        return pieModelMontosPagados;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public BarChartModel getBarCantMedidores() {
        return barCantMedidores;
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public LineChartModel getDateModel() {
        return dateModel;
    }

    public List<PagoServicioEntity> getCantidadMedidoresEntityList() {
        return cantidadMedidoresEntityList;
    }

    public void setCantidadMedidoresEntityList(List<PagoServicioEntity> cantidadMedidoresEntityList) {
        this.cantidadMedidoresEntityList = cantidadMedidoresEntityList;
    }

    public List<PagoServicioEntity> getPagoServicioEntityListYear() {
        return pagoServicioEntityListYear;
    }

    public void setPagoServicioEntityListYear(List<PagoServicioEntity> pagoServicioEntityListYear) {
        this.pagoServicioEntityListYear = pagoServicioEntityListYear;
    }
}
