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
		generate.addClickListener(e->generateAnnex1Report());
		add(generate, new TextArea("Hi"));
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
			//File a = new File(reportPath + "//detailreport.pdf");
			/*StreamResource resourcerange = new StreamResource("DetailedReport.pdf", () -> createResource(a));
			PdfViewer pdfViewerrange = new PdfViewer();
			pdfViewerrange.setSrc(resourcerange);279376
			hl4.setVisible(true);
			hl4.setSizeFull();
			hl4.add(pdfViewerrange);
			*/
			Anchor anchor = new Anchor(reportPathx + "//pbr2.pdf", "Download PDF");
			anchor.getElement().setAttribute("download", "pbr2.pdf");
			add(anchor);
			System.out.println("Sexess");

		} catch (Exception e) {
			// notify.show("Error:" + e, 5000, Position.TOP_CENTER);
			e.printStackTrace();
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
			Village selectedvillage= dbservice.getVillagesByVillageCode(273043);
			List<Village> details = dbservice.getVillages(selectedvillage.getId());
			Resource resource = new ClassPathResource("report/annexure1.jrxml");
			InputStream employeeReportStream = resource.getInputStream();
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
			List<VillageAnnexure1> villageannex1=dbservice.getAnnexure1Details(selectedvillage);
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(villageannex1);
			Map<String, Object> parameters = new HashMap<>();
			//parameters.put("SUBREPORT_DIR", reportPathx); 
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,jrBeanCollectionDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//annexure1.pdf");
			//File a = new File(reportPath + "//detailreport.pdf");
			/*StreamResource resourcerange = new StreamResource("DetailedReport.pdf", () -> createResource(a));
			PdfViewer pdfViewerrange = new PdfViewer();
			pdfViewerrange.setSrc(resourcerange);279376
			hl4.setVisible(true);
			hl4.setSizeFull();
			hl4.add(pdfViewerrange);
			*/
			Anchor anchor = new Anchor(reportPathx + "//pbr2.pdf", "Download PDF");
			anchor.getElement().setAttribute("download", "pbr2.pdf");
			add(anchor);
			System.out.println("Sexess");

		} catch (Exception e) {
			// notify.show("Error:" + e, 5000, Position.TOP_CENTER);
			e.printStackTrace();
		}

	}
	
}
