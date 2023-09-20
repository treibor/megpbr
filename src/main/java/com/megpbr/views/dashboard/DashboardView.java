package com.megpbr.views.dashboard;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.megpbr.audit.CaptchaCheck;
import com.megpbr.data.entity.District;
import com.megpbr.data.service.DashboardService;
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
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@PermitAll
@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
	DashboardService dservice;
    public DashboardView(DashboardService dservice) {
    	this.dservice=dservice;
    	
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
    
    
	
}
