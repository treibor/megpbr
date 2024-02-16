package com.megpbr.views;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Markets;
import com.megpbr.data.entity.pbr.Scapes;
import com.megpbr.data.entity.villages.VillageAnnexure1;
import com.megpbr.data.entity.villages.VillageAnnexure2;
import com.megpbr.data.entity.villages.VillageAnnexure3;
import com.megpbr.data.entity.villages.VillageAnnexure4;
import com.megpbr.data.entity.villages.VillageAnnexure5;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.MarketsService;
import com.megpbr.data.service.ScapeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
@RolesAllowed({"ADMIN", "USER"})
@PageTitle("Reports")
@Route(value = "reports", layout = MainLayout.class)
@Uses(Icon.class)
public class ReportView extends VerticalLayout {
	private Dbservice dbservice;
	private CropService cservice;
	private final MarketsService mservice;
	private final ScapeService sservice;
	public ReportView(Dbservice dbservice, CropService cservice, MarketsService mservice, ScapeService sservice) {
		super();
		this.dbservice = dbservice;
		this.cservice = cservice;
		this.mservice=mservice;
		this.sservice=sservice;
		Button generate=new Button ("Generate Data");
		generate.addClickListener(e->generateAnnexReport());
		add(generate, new TextArea("Hi"));
	}
	private void generateAnnexReport() {
		try {
			String reportPath = "F:";
			URL res = getClass().getClassLoader().getResource("report/pbr2report.jrxml");
			File file = Paths.get(res.toURI()).toFile();
			String absolutePath = file.getAbsolutePath();
			//String reportPath = absolutePath.substring(0, absolutePath.length() - 15);
			String reportPathx = absolutePath.substring(0, absolutePath.length() - 16);
			Village selectedvillage= dbservice.getVillagesByVillageCode(279398);
			List<Village> details = dbservice.getVillages(selectedvillage.getId());
			Resource resource = new ClassPathResource("report/Annexures.jrxml");
			InputStream employeeReportStream = resource.getInputStream();
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(details);
			Map<String, Object> parameters = new HashMap<>();
			//parameters.put("SUBREPORT_DIR", reportPathx); 
			List<VillageAnnexure1> crops1=dbservice.getAnnexure1Details(selectedvillage);
			List<VillageAnnexure2> crops2=dbservice.getAnnexure2Details(selectedvillage);
			List<VillageAnnexure3> crops3=dbservice.getAnnexure3Details(selectedvillage);
			List<VillageAnnexure4> crops4=dbservice.getAnnexure4Details(selectedvillage);
			List<VillageAnnexure5> crops5=dbservice.getAnnexure5Details(selectedvillage);
			parameters.put("SUBREPORT_DIR", reportPathx+"Annexures\\");
			parameters.put("SUBREPORT_DATA1", crops1);
			parameters.put("SUBREPORT_DATA2", crops2);
			parameters.put("SUBREPORT_DATA3", crops3);
			parameters.put("SUBREPORT_DATA4", crops4);
			parameters.put("SUBREPORT_DATA5", crops5);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,jrBeanCollectionDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//annexures.pdf");
		} catch (Exception e) {
			// notify.show("Error:" + e, 5000, Position.TOP_CENTER);
			
		}

	}
	private void generatePbr2Report() {
		try {
			String reportPath = "F:";
			URL res = getClass().getClassLoader().getResource("report/pbr2report.jrxml");
			File file = Paths.get(res.toURI()).toFile();
			String absolutePath = file.getAbsolutePath();
			//String reportPath = absolutePath.substring(0, absolutePath.length() - 15);
			String reportPathx = absolutePath.substring(0, absolutePath.length() - 16);
			Village selectedvillage= dbservice.getVillagesByVillageCode(279398);
			List<Village> details = dbservice.getVillages(selectedvillage.getId());
			Resource resource = new ClassPathResource("report/pbr2report.jrxml");
			InputStream employeeReportStream = resource.getInputStream();
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(details);
			Map<String, Object> parameters = new HashMap<>();
			//parameters.put("SUBREPORT_DIR", reportPathx); 
			List<Crops> crops=cservice.findCropsByVillage(selectedvillage, true);
			List<Markets> markets=mservice.getMarketsByVillage(selectedvillage, true);
			List<Scapes> scapes=sservice.getScapesByVillage(selectedvillage, true);
			Map<Integer, List<Crops>> formatVsCropsMap = new HashMap<>();
			Map<Integer, List<Markets>> formatVsMarketsMap = new HashMap<>();
			Map<Integer, List<Scapes>> formatVsScapesMap = new HashMap<>();
			parameters.put("SUBREPORT_DIR", reportPathx+"Formats\\");
			for(Crops crop : crops) {
				formatVsCropsMap.computeIfAbsent(crop.getFormat().getFormat(), k -> new ArrayList<>());
				formatVsCropsMap.get(crop.getFormat().getFormat()).add(crop);
			}
			for(Integer format : formatVsCropsMap.keySet()) {
				parameters.put("SUBREPORT_DATA" + format.toString(), formatVsCropsMap.get(format));
			}
			
			for(Markets market : markets) {
				formatVsMarketsMap.computeIfAbsent(market.getFormat().getFormat(), k -> new ArrayList<>());
				formatVsMarketsMap.get(market.getFormat().getFormat()).add(market);
			}
			for(Integer format : formatVsMarketsMap.keySet()) {
				parameters.put("SUBREPORT_DATA" + format.toString(), formatVsMarketsMap.get(format));
			}
			
			for(Scapes scape : scapes) {
				formatVsScapesMap.computeIfAbsent(scape.getFormat().getFormat(), k -> new ArrayList<>());
				formatVsScapesMap.get(scape.getFormat().getFormat()).add(scape);
			}
			for(Integer format : formatVsScapesMap.keySet()) {
				parameters.put("SUBREPORT_DATA" + format.toString(), formatVsScapesMap.get(format));
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,jrBeanCollectionDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//pbr2.pdf");
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath + "//pbr2.xlsx"));
			// Set input and output ...
			SimpleXlsxReportConfiguration reportConfig
			  = new SimpleXlsxReportConfiguration();
			reportConfig.setSheetNames(new String[] { "Annexure1" });

			exporter.setConfiguration(reportConfig);
			exporter.exportReport();
		} catch (Exception e) {
			// notify.show("Error:" + e, 5000, Position.TOP_CENTER);
			
		}

	}
	private void generateAnnex1Report() {
		try {
			String reportPath = "F:";
			URL res = getClass().getClassLoader().getResource("report/pbr2report.jrxml");
			File file = Paths.get(res.toURI()).toFile();
			String absolutePath = file.getAbsolutePath();
			//String reportPath = absolutePath.substring(0, absolutePath.length() - 15);
			String reportPathx = absolutePath.substring(0, absolutePath.length() - 16);
			Village selectedvillage= dbservice.getVillagesByVillageCode(278505);
			List<Village> details = dbservice.getVillages(selectedvillage.getId());
			Resource resource = new ClassPathResource("report/Annexures/annexure5.jrxml");
			InputStream employeeReportStream = resource.getInputStream();
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
			List<VillageAnnexure5> villageannex1=dbservice.getAnnexure5Details(selectedvillage);
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(villageannex1);
			Map<String, Object> parameters = new HashMap<>();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,jrBeanCollectionDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//annexure1.pdf");
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath + "//annexure1.xlsx"));
			// Set input and output ...
			SimpleXlsxReportConfiguration reportConfig
			  = new SimpleXlsxReportConfiguration();
			reportConfig.setSheetNames(new String[] { "Annexure1" });

			exporter.setConfiguration(reportConfig);
			exporter.exportReport();
			
		} catch (Exception e) {
			// notify.show("Error:" + e, 5000, Position.TOP_CENTER);
			
		}

	}
	
}
