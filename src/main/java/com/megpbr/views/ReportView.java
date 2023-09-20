package com.megpbr.views;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
	public ReportView(Dbservice dbservice, CropService cservice) {
		super();
		this.dbservice = dbservice;
		this.cservice = cservice;
		Button generate=new Button ();
		generate.addClickListener(e->generatePbr2Report());
		add(generate);
	}
	private void generatePbr2Report() {
		try {
			String reportPath = "F:";
			URL res = getClass().getClassLoader().getResource("report/mainreport.jrxml");
			File file = Paths.get(res.toURI()).toFile();
			String absolutePath = file.getAbsolutePath();
			//String reportPath = absolutePath.substring(0, absolutePath.length() - 15);
			String reportPathx = absolutePath.substring(0, absolutePath.length() - 16);
			Village selectedvillage= dbservice.getVillagesByVillageCode(279438);
			List<Village> details = dbservice.getVillages(selectedvillage.getId());
			List<Crops> crop1=cservice.findCropsByFormatAndVillage(3, selectedvillage);
			List<Crops> crop2=cservice.findCropsByFormatAndVillage(4, selectedvillage);
			System.out.println(crop1.size());
			System.out.println(crop2.size());
			Resource resource = new ClassPathResource("report/mainreport.jrxml");
			InputStream employeeReportStream = resource.getInputStream();
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(details);
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("SUBREPORT_DIR", reportPathx); 
			parameters.put("SUBREPORT_DATA", crop1);
			parameters.put("SUBREPORT_DATA2", crop2); 
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
			System.out.println("Sexess");

		} catch (Exception e) {
			// notify.show("Error:" + e, 5000, Position.TOP_CENTER);
			e.printStackTrace();
		}

	}
	
}
