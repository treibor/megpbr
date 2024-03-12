package com.megpbr.views.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.storedobject.chart.Data;
import com.storedobject.chart.DataType;
import com.storedobject.chart.NightingaleRoseChart;
import com.storedobject.chart.Position;
import com.storedobject.chart.RectangularCoordinate;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.Title;
import com.storedobject.chart.Toolbox;
import com.storedobject.chart.XAxis;
import com.storedobject.chart.YAxis;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
	DashboardService dservice;
	Dbservice dbservice;
	UserService uservice;
	//Grid<District> grid=new Grid<>(District.class);
	District district;
    public DashboardView(DashboardService dservice, Dbservice dbservice, UserService uservice) {
    	this.dservice=dservice;
    	this.dbservice=dbservice;
    	this.uservice=uservice;
    	add(getCharts());
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        
    }
    public Component getCharts() {
    	SOChart soChart = new SOChart();
    	SOChart soChart2 = new SOChart();
    	CategoryData labels = new CategoryData();
    	Data data = new Data();
        int i=dservice.getDistricts().size();
        for(int index=0; index<i; index++) {
        	labels.add(dservice.getDistricts().get(index).getDistrictName());
        	District dist=dservice.getDistricts().get(index);
        	data.add(dservice.getDistrictCount(dist, false));
        }
        BarChart bc = new BarChart(labels, data);
        RectangularCoordinate rc;
        rc  = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        Position p = new Position();
        bc.plotOn(rc); // Bar chart needs to be plotted on a coordinate system
        bc.setName("PBR Entered");
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());
        Title title = new Title("District Wise PBR");
        
        NightingaleRoseChart nc = new NightingaleRoseChart(labels, data);
        nc.setName("PBR Entered");
        nc.setPosition(p); // Position it leaving 50% space at the top
        soChart.add(nc, toolbox);
        soChart2.add(bc, toolbox, title);
        HorizontalLayout getCharts=new HorizontalLayout();
        //getCharts.
        getCharts.addClassName("chartsLayout1");
        getCharts.setWidthFull();
        //getCharts.setHeight("100px");
        getCharts.add(soChart2, soChart);
        return getCharts;
    }
    public Component getCharts2() {
    	//SOChart soChart = new SOChart();
    	SOChart soChart2 = new SOChart();
    	CategoryData labels = new CategoryData();
    	Data data = new Data();
        int i=dservice.getFormats().size();
        for(int index=0; index<i; index++) {
        	labels.add(dservice.getFormats().get(index).getFormatName());
			MasterFormat dist = dservice.getFormats().get(index);
			if (index == 5 || index == 16) {
				data.add(dservice.getMarketsCount(dist));
			} else if (index ==6 || index == 7 || index == 8 || index == 9) {
				data.add(dservice.getScapesCount(dist));
			} else {
				data.add(dservice.getCropsCount(dist));
			}
        }
        BarChart bc = new BarChart(labels, data);
        RectangularCoordinate rc;
        
        rc  = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        Position p = new Position();
        bc.plotOn(rc); // Bar chart needs to be plotted on a coordinate system
        bc.setName("PBR Entered");
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());
        Title title = new Title("PBR Data");
        soChart2.add(bc, toolbox, title);
        soChart2.setWidthFull();
        return soChart2;
    }    
    public Component getCharts3() {
    	SOChart soChartf = new SOChart();
    	//SOChart soChartf = new SOChart();
    	CategoryData labels = new CategoryData();
    	Data data = new Data();
        int i=dservice.getFormats().size();
       // System.out.println("Formats "+i);
        for(int index=0; index<i; index++) {
        	labels.add(dservice.getFormats().get(index).getFormat()+" - "+dservice.getFormats().get(index).getFormatName());
        	        	//District dist=dservice.getDistricts().get(index);
        	MasterFormat format=dservice.getFormats().get(index);
        	data.add(dservice.getFormatCount(format, false));
        }
        BarChart bc = new BarChart(labels, data);
        
        RectangularCoordinate rc;
        rc  = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        Position p = new Position();
        bc.plotOn(rc); // Bar chart needs to be plotted on a coordinate system
        bc.setName("PBR Entered");
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());
        Title title = new Title("Format Wise PBR");
        
        NightingaleRoseChart nc = new NightingaleRoseChart(labels, data);
        nc.setName("PBR Entered");
        nc.setPosition(p); // Position it leaving 50% space at the top
        //soChartf.add(nc, toolbox);
        soChartf.add(bc, toolbox, title);
        HorizontalLayout getCharts=new HorizontalLayout();
        //getCharts.
        getCharts.addClassName("chartsLayout1");
        getCharts.setWidthFull();
        //getCharts.setHeight("10px");
        soChartf.setWidthFull();
        getCharts.add(soChartf);
        return soChartf;
    }
    private Component getGrid() {
    	return null;
    }
}
