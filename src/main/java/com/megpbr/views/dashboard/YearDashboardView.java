package com.megpbr.views.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.Village;
import com.megpbr.data.service.DashboardService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.UserService;
import com.megpbr.views.MainLayout;
import com.storedobject.chart.BarChart;
import com.storedobject.chart.CategoryData;
import com.storedobject.chart.ChartException;
import com.storedobject.chart.Data;
import com.storedobject.chart.DataType;
import com.storedobject.chart.NightingaleRoseChart;
import com.storedobject.chart.Position;
import com.storedobject.chart.RectangularCoordinate;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.Size;
import com.storedobject.chart.Title;
import com.storedobject.chart.Toolbox;
import com.storedobject.chart.XAxis;
import com.storedobject.chart.YAxis;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Dashboard")
@Route(value = "yeardashboard", layout = MainLayout.class)
//@RouteAlias(value = "yeardashboard", layout = MainLayout.class)
public class YearDashboardView extends VerticalLayout {
	DashboardService dservice;
	Dbservice dbservice;
	UserService uservice;
	TreeGrid<District> grid=new TreeGrid<>(District.class);
	District district;
	ComboBox<String> yearCombo = new ComboBox("Select Year");
	//SOChart soChart = new SOChart();
	//SOChart soChart2 = new SOChart();
	HorizontalLayout hlayout=new HorizontalLayout();
    //getCharts.
	Data data = new Data();
    public YearDashboardView(DashboardService dservice, Dbservice dbservice, UserService uservice) {
    	this.dservice=dservice;
    	this.dbservice=dbservice;
    	this.uservice=uservice;
    	//yearCombo.setItems("All", "2022", "2023");
    	
    	add( getYearCharts());
    	//loadCharts();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        
    }
    private Component getFilterBar() {
    	
    	yearCombo.setValue("2022");
    	//yearCombo.addValueChangeListener(e->loadCharts());
    	var filterLayout=new HorizontalLayout(yearCombo);
    	filterLayout.setWidthFull();
    	return filterLayout;
    	
    }

	
    public Component getYearCharts() {
    	SOChart soChart = new SOChart();
    	SOChart soChartf = new SOChart();
    	CategoryData labels = new CategoryData();
    	Data data = new Data();
        int i=dservice.getYearList().size();
        //System.out.println("Formats "+i);
        //System.out.println("YEAR "+dservice.getYearList());
        for(int index=0; index<i; index++) {
        	labels.add(dservice.getYearList().get(index));
        	int year=Integer.parseInt(dservice.getYearList().get(index));
        	int yeardata=dservice.getYearData(year);
        	data.add(yeardata);
        }
        BarChart bc = new BarChart(labels, data);
        
        RectangularCoordinate rc;
        rc  = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        Position p = new Position();
        bc.plotOn(rc); // Bar chart needs to be plotted on a coordinate system
        bc.setName("PBR Entered");
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());
        Title title = new Title("Year Wise PBR");
        
        NightingaleRoseChart nc = new NightingaleRoseChart(labels, data);
        nc.setName("PBR Entered");
        nc.setPosition(p); // Position it leaving 50% space at the top
        soChart.add(nc, toolbox);
        soChartf.add(bc, toolbox, title);
        HorizontalLayout getCharts=new HorizontalLayout();
        //getCharts.
        getCharts.addClassName("chartsLayout1");
        getCharts.setWidthFull();
        //getCharts.setHeight("10px");
        soChartf.setWidthFull();
        getCharts.add(soChartf);
        getCharts.add(soChartf, soChart);
        return getCharts;
    }
    public Component getYearGrid() {
        List<DtoClass> data = getYearData();

        Grid<DtoClass> grid = new Grid<>();
        grid.setItems(data);
        //grid.addColumn(DtoClass::getTotalCrops).setHeader("Total Crops");
        //grid.addColumn(DtoClass::getTotalScapes).setHeader("Total Scapes");
        //grid.addColumn(DtoClass::getTotalMarkets).setHeader("Total Markets");
        grid.addColumn(DtoClass::getYear).setHeader("Year");
        grid.addColumn(DtoClass::getTotal).setHeader("Data");

        HorizontalLayout layout = new HorizontalLayout(grid);
        layout.addClassName("gridLayout");
        layout.setWidthFull();

        return layout;
    }

    private List<DtoClass> getYearData() {
        List<DtoClass> yearData = new ArrayList<>();

        // Populate your data here, e.g., from a service
        // For demonstration purposes, I'll create some sample data
        for (int i = 2020; i <= 2023; i++) {
            //DtoClass dto = new DtoClass(i * 1000, i * 2000, i * 3000, String.valueOf(i), i * 10);
            //yearData.add(dto);
        }

        return yearData;
    }
}
