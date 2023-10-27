package com.megpbr.views.villages;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Markets;
import com.megpbr.data.entity.pbr.Scapes;
import com.megpbr.data.entity.villages.VillageAnnexure1;
import com.megpbr.data.entity.villages.VillageAnnexure2;
import com.megpbr.data.entity.villages.VillageAnnexure3;
import com.megpbr.data.entity.villages.VillageAnnexure4;
import com.megpbr.data.entity.villages.VillageAnnexure5;
import com.megpbr.data.entity.villages.VillageDetails;
import com.megpbr.data.service.AuditService;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.MarketsService;
import com.megpbr.data.service.ScapeService;
import com.megpbr.data.service.UserService;
import com.megpbr.views.MainLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style.Position;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
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
import software.xdev.vaadin.grid_exporter.GridExporter;


@PermitAll
@PageTitle("Village Details")
@Route(value = "villagesdetails", layout = MainLayout.class)
@Uses(Icon.class)
public class VillageView extends Div{
	private Dbservice dbservice;
	private UserService userservice;
	private UserService uservice;
	private AuditService auditservice;
	private CropService cservice;
	private final MarketsService mservice;
	private final ScapeService sservice;
	Village currentvillage;
	Audit auditobject;
	VerticalLayout vlx=new VerticalLayout();
	Grid<VillageDetails> grid=new Grid<>(VillageDetails.class);
	Grid<VillageAnnexure1> annex1grid=new Grid<>(VillageAnnexure1.class);
	Grid<VillageAnnexure2> annex2grid=new Grid<>(VillageAnnexure2.class);
	Grid<VillageAnnexure3> annex3grid=new Grid<>(VillageAnnexure3.class);
	Grid<VillageAnnexure4> annex4grid=new Grid<>(VillageAnnexure4.class);
	Grid<VillageAnnexure5> annex5grid=new Grid<>(VillageAnnexure5.class);
	VillagesForm form;
	Annexure1Form form1;
	Annexure2Form form2;
	Annexure3Form form3;
	Annexure4Form form4;
	Annexure5Form form5;
	VillageDetails villagedetails;
	VillageAnnexure1 villageannexure1;
	//MasterFormat format;
	TextField filterText=new TextField("");
	ComboBox<State> state = new ComboBox("");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	
	Tab tab1=new Tab("Annexure 1");
	Tab tab2=new Tab("Annexure 2");
	Tab tab3=new Tab("Annexure 3");
	Tab tab4=new Tab("Annexure 4");
	Tab tab5=new Tab("Annexure 5");
	//Grid.Column<Crops> localColumn;
	Grid.Column<VillageDetails> stateColumn;
	Grid.Column<VillageDetails> districtColumn;
	Grid.Column<VillageDetails> blockColumn;
	Grid.Column<VillageDetails> villageColumn;
	Grid.Column<VillageDetails> geographicColumn;
	Button reportbutton=new Button("Generate");
	Anchor link = new Anchor();
	HorizontalLayout reportpanel;
	ComboBox<String> reporttype = new ComboBox("Select Report");
	ComboBox<String> formattype = new ComboBox("Format");
	public VillageView(Dbservice service, UserService userservice, UserService uservice, AuditService auditservice, CropService cservice, MarketsService mservice, ScapeService sservice) {
		this.dbservice=service;
		this.userservice=userservice;
		this.uservice=uservice;
		this.cservice = cservice;
		this.mservice=mservice;
		this.sservice=sservice;
		this.auditservice=auditservice;
		//format=dbservice.getFormat(1);
		setSizeFull();
		ConfigureGrid();
		ConfigureForm();
		ConfigureForm1();
		ConfigureForm2();
		ConfigureForm3();
		ConfigureForm4();
		ConfigureForm5();
		ConfigureCombo();
		ConfigureAccess();
		add(getTabs());
		auditobject=new Audit(auditservice, userservice);
	}
	private boolean isAdmin() {
		//UserLogin user=uservice.getLoggedUser();
		String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.endsWith("ADMIN")) {
			return true;
		}else {
			return false;
		}
	}
	private void ConfigureAccess() {
		UserLogin user=userservice.getLoggedUser();
		String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.startsWith("STATE")) {
			//System.out.println("C");
			state.setValue(user.getState());
			stateColumn.setVisible(false);
			state.setVisible(false);
		}else if(userLevel.startsWith("DISTRICT")) {
			//System.out.println("C");
			state.setValue(user.getState());
			district.setValue(user.getDistrict());
			stateColumn.setVisible(false);
			districtColumn.setVisible(false);
			state.setVisible(false);
			district.setVisible(false);
		}else if(userLevel.startsWith("BLOCK")) {
			state.setValue(user.getState());
			district.setValue(user.getDistrict());
			block.setValue(user.getBlock());
			stateColumn.setVisible(false);
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
			state.setVisible(false);
			district.setVisible(false);
			block.setVisible(false);
		}else if(userLevel.startsWith("VILLAGE")) {
			state.setValue(user.getState());
			district.setValue(user.getDistrict());
			block.setValue(user.getBlock());
			village.setValue(user.getVillage());
			stateColumn.setVisible(false);
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
			villageColumn.setVisible(false);
			state.setVisible(false);
			district.setVisible(false);
			block.setVisible(false);
			village.setVisible(false);
		}
	}
	private void ConfigureFormAccess() {
		//UserLogin user=uservice.getLoggedUser();
		String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.startsWith("STATE")) {
			form.state.setVisible(false);
		}else if(userLevel.startsWith("DISTRICT")) {
			form.state.setVisible(false);
			form.district.setVisible(false);
		}else if(userLevel.startsWith("BLOCK")) {
			form.state.setVisible(false);
			form.district.setVisible(false);
			form.block.setVisible(false);
		}else if(userLevel.startsWith("VILLAGE")) {
			form.state.setVisible(false);
			form.district.setVisible(false);
			form.block.setVisible(false);
			form.village.setVisible(false);
		}
	}
	private void ConfigureNewFormAccess() {
		UserLogin user=userservice.getLoggedUser();
		String userLevel=userservice.getLoggedUserLevel();
		if(userLevel.startsWith("STATE")) {
			form.state.setValue(user.getState());
			form.state.setVisible(false);
		}else if(userLevel.startsWith("DISTRICT")) {
			//
			form.state.setValue(user.getState());
			form.district.setValue(user.getDistrict());
			form.state.setVisible(false);
			form.district.setVisible(false);
		}else if(userLevel.startsWith("BLOCK")) {
			form.state.setValue(user.getState());
			form.district.setValue(user.getDistrict());
			form.block.setValue(user.getBlock());
			form.state.setVisible(false);
			form.district.setVisible(false);
			form.block.setVisible(false);
		}else if(userLevel.startsWith("VILLAGE")) {
			state.setValue(user.getState());
			district.setValue(user.getDistrict());
			block.setValue(user.getBlock());
			village.setValue(user.getVillage());
			form.state.setVisible(false);
			form.district.setVisible(false);
			form.block.setVisible(false);
			form.village.setVisible(false);
		}
	}
	public Component getTabs() {
		TabSheet tabSheet = new TabSheet();
		tabSheet.add("Villages",getContent());
		tabSheet.add(tab1, getAnnexure1());
		tabSheet.add(tab2, getAnnexure2());
		tabSheet.add(tab3, getAnnexure3());
		tabSheet.add(tab4, getAnnexure4());
		tabSheet.add(tab5, getAnnexure5());
		tab1.setEnabled(false);
		tab2.setEnabled(false);
		tab3.setEnabled(false);
		tab4.setEnabled(false);
		tab5.setEnabled(false);
		tabSheet.setSizeFull();
		return tabSheet;
	}
	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(getMainContent(), form);
		content.setFlexGrow(1, vlx);
		content.setFlexGrow(1, form);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}
	private Component getMainContent() {
		vlx.add(getToolbar(), grid, getReportBar());
		vlx.setSizeFull();		
		return vlx;
	}
	private Component getToolbar() {
		filterText.setPlaceholder("Search Village Name ");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList());
		//filterText.setWidth("20%");
		district.addValueChangeListener(e->updateList());
		block.addValueChangeListener(e->updateList());
		village.addValueChangeListener(e->updateList());
		Button addButton=new Button("New");
		addButton.setPrefixComponent(LineAwesomeIcon.PLUS_CIRCLE_SOLID.create());
		addButton.addClickListener(e->addVillageDetails(new VillageDetails()));
		FormLayout formx = new FormLayout();
		formx.add(state, 2);
		formx.add(district, 2);
		formx.add(block, 2);
		formx.add(village, 2);
		formx.add(filterText, 2);
		formx.add(addButton, 1);
		// formx.add(expButton, 1);
		formx.setResponsiveSteps(new ResponsiveStep("0", 4),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("800px", 11));
		// VerticalLayout abc=new VerticalLayout(formx);
		return formx;
		
//		HorizontalLayout topvl=new HorizontalLayout(state, district, block, village, filterText,addButton);
//		topvl.setAlignItems(FlexComponent.Alignment.BASELINE);
//		topvl.setWidthFull();
//		return topvl; 
	}
	private Component getReportBar() {
		reporttype.setItems("Pbr1", "Pbr2", "Annexures");
		formattype.setItems("Pdf", "Excel");
		reporttype.setWidth("10%");
		formattype.setWidth("10%");
		reportbutton.addClickListener(e->generateReport());
		var expButton=new Button("Export"); 
		expButton.addClickListener(e->GridExporter
				.newWithDefaults(grid)
				.open());
		expButton.getStyle().set("margin-left", "auto");
		reportpanel=new HorizontalLayout(reporttype,formattype, reportbutton, expButton);
		reportpanel.setAlignItems(FlexComponent.Alignment.BASELINE);
		reportpanel.setWidthFull();
		//reportpanel.setEnabled(false);
		reportbutton.setEnabled(false);
		expButton.setEnabled(true);
		return reportpanel;
	}
	
	private void generateReport() {
		try {
			if ("".equals(reporttype.getValue()) || "".equals(formattype.getValue())) {
				Notification.show("Error. Please Select the Report type and Format")
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			} else if (grid.asSingleSelect().getValue().getVillage().equals(null)
					|| grid.asSingleSelect().getValue().getVillage() == null) {
				Notification.show("Error. Please Select a Village from the Table")
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			} else {
				String type = reporttype.getValue().trim();
				String fileformat = formattype.getValue().trim();

				Village selectedvillage = grid.asSingleSelect().getValue().getVillage();

				URL res = getClass().getClassLoader().getResource("report/Part1.jrxml");
				File file = Paths.get(res.toURI()).toFile();
				String absolutePath = file.getAbsolutePath();
				String reportPath = absolutePath.substring(0, absolutePath.length() - 11);
				if (type.trim() == "Pbr1") {
					List<VillageDetails> details = dbservice.getVillageDetails(selectedvillage, "");
					Resource resource = new ClassPathResource("report/Part1.jrxml");
					InputStream ReportStream = resource.getInputStream();
					JasperReport jasperReport = JasperCompileManager.compileReport(ReportStream);
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(details);
					Map<String, Object> parameters = new HashMap<>();
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							jrBeanCollectionDataSource);
					File downloadfile=null;
					if (fileformat == "Pdf") {
						JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//Pbr1.pdf");
						downloadfile = new File(reportPath + "//Pbr1.pdf");
					}else {
						JRXlsxExporter exporter = new JRXlsxExporter();
						exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
						exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath + "//Pbr1.xlsx"));
						SimpleXlsxReportConfiguration reportConfig  = new SimpleXlsxReportConfiguration();
						reportConfig.setSheetNames(new String[] { "Pbr1" });
						exporter.setConfiguration(reportConfig);
						exporter.exportReport();
						downloadfile = new File(reportPath + "//Pbr1.xlsx");
					}
					addLinkToFile(downloadfile);
				} else if (type.trim() == "Pbr2") {
					List<Village> details = dbservice.getVillages(selectedvillage.getId());
					Resource resource = new ClassPathResource("report/pbr2report.jrxml");
					InputStream employeeReportStream = resource.getInputStream();
					JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(details);
					Map<String, Object> parameters = new HashMap<>();
					// parameters.put("SUBREPORT_DIR", reportPathx);
					List<Crops> crops = cservice.findCropsByVillage(selectedvillage, true);
					List<Markets> markets = mservice.getMarketsByVillage(selectedvillage, true);
					List<Scapes> scapes = sservice.getScapesByVillage(selectedvillage, true);
					Map<Integer, List<Crops>> formatVsCropsMap = new HashMap<>();
					Map<Integer, List<Markets>> formatVsMarketsMap = new HashMap<>();
					Map<Integer, List<Scapes>> formatVsScapesMap = new HashMap<>();
					parameters.put("SUBREPORT_DIR", reportPath + "Formats\\");
					for (Crops crop : crops) {
						formatVsCropsMap.computeIfAbsent(crop.getFormat().getFormat(), k -> new ArrayList<>());
						formatVsCropsMap.get(crop.getFormat().getFormat()).add(crop);
					}
					for (Integer format : formatVsCropsMap.keySet()) {
						parameters.put("SUBREPORT_DATA" + format.toString(), formatVsCropsMap.get(format));
					}

					for (Markets market : markets) {
						formatVsMarketsMap.computeIfAbsent(market.getFormat().getFormat(), k -> new ArrayList<>());
						formatVsMarketsMap.get(market.getFormat().getFormat()).add(market);
					}
					for (Integer format : formatVsMarketsMap.keySet()) {
						parameters.put("SUBREPORT_DATA" + format.toString(), formatVsMarketsMap.get(format));
					}

					for (Scapes scape : scapes) {
						formatVsScapesMap.computeIfAbsent(scape.getFormat().getFormat(), k -> new ArrayList<>());
						formatVsScapesMap.get(scape.getFormat().getFormat()).add(scape);
					}
					for (Integer format : formatVsScapesMap.keySet()) {
						parameters.put("SUBREPORT_DATA" + format.toString(), formatVsScapesMap.get(format));
					}
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							jrBeanCollectionDataSource);
					
					File downloadfile =null;
					if (fileformat == "Pdf") {
						JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//Pbr2.pdf");
						downloadfile = new File(reportPath + "//Pbr2.pdf");
					}else {
						JRXlsxExporter exporter = new JRXlsxExporter();
						exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
						exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath + "//Pbr2.xlsx"));
						SimpleXlsxReportConfiguration reportConfig  = new SimpleXlsxReportConfiguration();
						reportConfig.setSheetNames(new String[] { "Pbr2" });
						exporter.setConfiguration(reportConfig);
						exporter.exportReport();
						downloadfile = new File(reportPath + "//Pbr2.xlsx");
					}
					addLinkToFile(downloadfile);
				} else {
					Resource resource = new ClassPathResource("report/Annexures.jrxml");
					InputStream employeeReportStream = resource.getInputStream();
					JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
					List<VillageDetails> details = dbservice.getVillageDetails(selectedvillage, "");
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(details);
					Map<String, Object> parameters = new HashMap<>();
					List<VillageAnnexure1> annex1=dbservice.getAnnexure1Details(selectedvillage);
					List<VillageAnnexure2> annex2=dbservice.getAnnexure2Details(selectedvillage);
					List<VillageAnnexure3> annex3=dbservice.getAnnexure3Details(selectedvillage);
					List<VillageAnnexure4> annex4=dbservice.getAnnexure4Details(selectedvillage);
					List<VillageAnnexure5> annex5=dbservice.getAnnexure5Details(selectedvillage);
					parameters.put("SUBREPORT_DIR", reportPath+"Annexures\\");
					parameters.put("SUBREPORT_DATA1", annex1);
					parameters.put("SUBREPORT_DATA2", annex2);
					parameters.put("SUBREPORT_DATA3", annex3);
					parameters.put("SUBREPORT_DATA4", annex4);
					parameters.put("SUBREPORT_DATA5", annex5);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,jrBeanCollectionDataSource);
					JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//Annexures.pdf");

					File downloadfile =null;
					if (fileformat == "Pdf") {
						JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//Annexures.pdf");
						downloadfile = new File(reportPath + "//Annexures.pdf");
					}else {
						JRXlsxExporter exporter = new JRXlsxExporter();
						exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
						exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath + "//Annexures.xlsx"));
						SimpleXlsxReportConfiguration reportConfig  = new SimpleXlsxReportConfiguration();
						reportConfig.setSheetNames(new String[] { "Annexures" });
						exporter.setConfiguration(reportConfig);
						exporter.exportReport();
						downloadfile = new File(reportPath + "//Annexures.xlsx");
					}
					addLinkToFile(downloadfile);
				}

			}
			Notification.show("Report Generated Succesfully. Click on the generated link to download")
			.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		}catch(NullPointerException npe) {
			Notification.show("Error. Please Select a Village from the Table and Select the Report Type and Format")
			.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}catch (Exception e) {
			Notification.show("Error Encountered While Generating Report")
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}

	private void addLinkToFile(File file) {
		if (link != null) {
			reportpanel.remove(link);
		}
		StreamResource streamResource = new StreamResource(file.getName(), () -> getStream(file));
		link = new Anchor(streamResource,
				String.format("%s (%d KB)", file.getName(), (int) file.length() / 1024));
		link.getElement().setAttribute("download", true);
		
		reportpanel.add(link);
	}
	private InputStream getStream(File file) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return stream;
    }
	
	private void ConfigureCombo() {
		state.setItems(dbservice.getStates());
		state.setPlaceholder("State");
		state.setItemLabelGenerator(state->state.getStateName());
		state.addValueChangeListener(e->district.setItems(dbservice.getDistricts(e.getValue())));
		//district.setItems(dbservice.getDistricts(state));
		district.setItemLabelGenerator(district -> district.getDistrictName());
		district.setPlaceholder("District");
		district.addValueChangeListener(e -> getBlocks());
		//district.addValueChangeListener(e -> updateList(filterText.getValue(), false));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.setPlaceholder("Block");
		block.addValueChangeListener(e->getVillages());
		village.setItems(dbservice.getVillages(block.getValue(), true));
		village.setItemLabelGenerator(Village::getVillageName);
		village.setPlaceholder("Village");
		village.addValueChangeListener(e->updateList());
		district.setClearButtonVisible(true);
		block.setClearButtonVisible(true);
		village.setClearButtonVisible(true);
		//village.setEnabled(false);
	}
	private void getBlocks() {
		block.setItems(dbservice.getBlocks(district.getValue()));
		updateList();
	}
	private void getVillages() {
		village.setItems(dbservice.getVillages(block.getValue(), true));
		updateList();
	}
	private void ConfigureForm() {
		form= new VillagesForm(dbservice);
		form.setVisible(false);
		form.setWidth("50%");
		form.addListener(VillagesForm.SaveEvent.class, this::saveVillageDetails);
		form.addListener(VillagesForm.DeleteEvent.class, this::deleteVillageDetails);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}
	
	
	
	
	public void updateBlockList() {
		Village villageValue=village.getValue();
		String search =filterText.getValue();
		//List<VillageDetails> villagesList=dbservice.getVillageDetails(district.getValue(),block.getValue(), villageValue, search);
		List<VillageDetails> villagesList=dbservice.getVillageDetails(block.getValue(), search);
		//List<VillageDetails> villagesList=dbservice.getVillageDetails(district.getValue());
		grid.setItems(villagesList);
		if(villagesList==null) {
			geographicColumn.setFooter("Total : 0");
		}else {
			geographicColumn.setFooter("Total : " + villagesList.size());
		}
	}
	public void updateDistrictList() {
		Village villageValue=village.getValue();
		String search =filterText.getValue();
		List<VillageDetails> villagesList=dbservice.getVillageDetails(district.getValue(), search);
		grid.setItems(villagesList);
		if(villagesList==null) {
			geographicColumn.setFooter("Total : 0");
		}else {
			geographicColumn.setFooter("Total : " + villagesList.size());
		}
	}
	public void updateVillageList() {
		Village villageValue=village.getValue();
		String search =filterText.getValue();
		List<VillageDetails> villagesList=dbservice.getVillageDetails( villageValue, search);
		grid.setItems(villagesList);
		if(villagesList==null) {
			geographicColumn.setFooter("Total : 0");
		}else {
			geographicColumn.setFooter("Total : " + villagesList.size());
		}
	}
	
	public void updateList() {
		if (district.getValue()==null){
			grid.setItems(Collections.EMPTY_LIST);
			geographicColumn.setFooter("Total : 0");
		}else if(village.getValue()==null && block.getValue()==null) {
			updateDistrictList();
		}else if(village.getValue()==null && block.getValue()!=null) {
			updateBlockList();
		}else {
			updateVillageList();
		}
		//System.out.println(district.getValue()+"-"+ block.getValue()+"-"+village.getValue());
	}
	public void enableTabs() {
		tab1.setEnabled(true);
		tab2.setEnabled(true);
		tab3.setEnabled(true);
		tab4.setEnabled(true);
		tab5.setEnabled(true);
		//ConfigureOtherForms();
	}
	public void disableTabs() {
		tab1.setEnabled(false);
		tab2.setEnabled(false);
		tab3.setEnabled(false);
		tab4.setEnabled(false);
		tab5.setEnabled(false);
	}
	public void disableMasterFields(){
		form.village.setEnabled(false);
		form.state.setEnabled(false);
		form.district.setEnabled(false);
		form.block.setEnabled(false);
	}
	public void enableMasterFields(){
		form.village.setEnabled(true);
		form.state.setEnabled(true);
		form.district.setEnabled(true);
		form.block.setEnabled(true);
	}
	public void editVillageDetails(VillageDetails crop) {
		form.save.setText("Update");
		form.delete.setVisible(isAdmin());
		form.initFields();
		form.setVisible(true);
		if (crop == null) {
			form.setVisible(false);
			disableTabs();
			reportbutton.setEnabled(false);
		} else {
			form.district.setValue(crop.getVillage().getBlock().getDistrict());
			form.block.setValue(crop.getVillage().getBlock());
			form.setVillageDetails(crop);
			form1.village=crop.getVillage();
			form2.village=crop.getVillage();
			form3.village=crop.getVillage();
			form4.village=crop.getVillage();
			form5.village=crop.getVillage();
			ConfigureFormAccess();
			disableMasterFields();
			ConfigureGrid1(crop.getVillage());
			ConfigureGrid2(crop.getVillage());
			ConfigureGrid3(crop.getVillage());
			ConfigureGrid4(crop.getVillage());
			ConfigureGrid5(crop.getVillage());
			enableTabs();
			reportbutton.setEnabled(true);
		}
	}
	
	public void addVillageDetails(VillageDetails crop) {
		//form.format=format;
		form.initFields();
		grid.asSingleSelect().clear();
		form.setVillageDetails(crop);
		enableMasterFields();
		form.save.setText("Save");
		form.setVisible(true);
		ConfigureNewFormAccess();
	}
	
	public void saveVillageDetails(VillagesForm.SaveEvent event) {
		try {
			VillageDetails vill=event.getVillageDetails();
			vill.setEnteredBy(uservice.getLoggedUser());
			vill.setEnteredOn(LocalDateTime.now());
			dbservice.updateVillageDetails(vill);
			auditobject.saveAudit(vill, "Save/Update");
			updateList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//Notification.show("Error Encountered :"+e);		
		}
	}
	
	public void deleteVillageDetails(VillagesForm.DeleteEvent event) {

		try {
			Village village = event.getVillageDetails().getVillage();
			VillageDetails details=event.getVillageDetails();
			village.setInUse(false);
			dbservice.updateVillage(village);
			auditobject.saveAudit(details, "Delete");
			dbservice.deleteVillageDetails(details);
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			updateList();
		} catch (NullPointerException npe) {
			Notification.show("Please Select A Village To Delete").addThemeVariants(NotificationVariant.LUMO_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :" + e);
		}

	}
	public void editAnnexure1(VillageAnnexure1 annex1) {
		if (annex1 == null) {
			form1.save.setText("Add");
			form1.setVillageAnnexure1(new VillageAnnexure1());
		} else {
			form1.setVillageAnnexure1(annex1);
			form1.save.setText("Update");
		}
	}
	public void saveAnnexure1Details(Annexure1Form.SaveEvent event) {
		try {
			VillageAnnexure1 annex1=event.getVillageAnnexure1();
			annex1.setEnteredBy(uservice.getLoggedUser());
			annex1.setEnteredOn(LocalDateTime.now());
			dbservice.updateAnnexure1Details(annex1);
			String username=annex1.getEnteredBy()== null ? "null" : annex1.getEnteredBy().getUserName();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+username+", Previous Entry Date-"+annex1.getEnteredOn();
			auditobject.saveAudit("Save/Update Annexure 1", details);
			form1.save.setText("Add");
			ConfigureGrid1(event.getVillageAnnexure1().getVillage());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//Notification.show("Error Encountered :"+e);		
		}
	}
	public void deleteAnnexure1Details(Annexure1Form.DeleteEvent event) {
		try {
			Village deletedvillage=event.getVillageAnnexure1().getVillage();
			VillageAnnexure1 annex1=event.getVillageAnnexure1();
			String username=annex1.getEnteredBy()== null ? "null" : annex1.getEnteredBy().getUserName();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+username+", Previous Entry Date-"+annex1.getEnteredOn();
			auditobject.saveAudit("Delete Annexure 1", details);
			dbservice.deleteAnnexure1Details(annex1);
			ConfigureGrid1(deletedvillage);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :"+e);
		}
	}
	public void editAnnexure2(VillageAnnexure2 annex2) {
		if (annex2 == null) {
			form2.save.setText("Add");
			form2.setVillageAnnexure2(new VillageAnnexure2());
		} else {
			form2.setVillageAnnexure2(annex2);
			form2.save.setText("Update");
		}
	}
	public void saveAnnexure2Details(Annexure2Form.SaveEvent event) {
		try {
			VillageAnnexure2 annex1=event.getVillageAnnexure2();
			annex1.setEnteredBy(uservice.getLoggedUser());
			annex1.setEnteredOn(LocalDateTime.now());
			dbservice.updateAnnexure2Details(annex1);
			String username=annex1.getEnteredBy()== null ? "null" : annex1.getEnteredBy().getUserName();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+username+", Previous Entry Date-"+annex1.getEnteredOn();
			
			auditobject.saveAudit("Save/Update Annexure 2", details);
			form2.save.setText("Add");
			ConfigureGrid2(event.getVillageAnnexure2().getVillage());
		} catch (Exception e) {
			e.printStackTrace();
					
		}
	}
	public void deleteAnnexure2Details(Annexure2Form.DeleteEvent event) {
		try {
			Village deletedvillage=event.getVillageAnnexure2().getVillage();
			VillageAnnexure2 annex1=event.getVillageAnnexure2();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+annex1.getEnteredBy()+", Previous Entry Date-"+annex1.getEnteredOn();
			auditobject.saveAudit("Delete Annexure 2", details);
			dbservice.deleteAnnexure2Details(annex1);
			ConfigureGrid2(deletedvillage);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :"+e);
		}
	}
	public void editAnnexure3(VillageAnnexure3 annex3) {
		if (annex3 == null) {
			form3.save.setText("Add");
			form3.setVillageAnnexure3(new VillageAnnexure3());
		} else {
			form3.setVillageAnnexure3(annex3);
			form3.save.setText("Update");
		}
	}
	public void saveAnnexure3Details(Annexure3Form.SaveEvent event) {
		try {
			VillageAnnexure3 annex1=event.getVillageAnnexure3();
			annex1.setEnteredBy(uservice.getLoggedUser());
			annex1.setEnteredOn(LocalDateTime.now());
			dbservice.updateAnnexure3Details(annex1);
			String username=annex1.getEnteredBy()== null ? "null" : annex1.getEnteredBy().getUserName();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+username+", Previous Entry Date-"+annex1.getEnteredOn();
			
			auditobject.saveAudit("Save/Update Annexure 3", details);
			form3.save.setText("Add");
			ConfigureGrid3(event.getVillageAnnexure3().getVillage());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//Notification.show("Error Encountered :"+e);		
		}
	}
	public void deleteAnnexure3Details(Annexure3Form.DeleteEvent event) {
		try {
			Village deletedvillage=event.getVillageAnnexure3().getVillage();
			VillageAnnexure3 annex1=event.getVillageAnnexure3();
			String username=annex1.getEnteredBy()== null ? "null" : annex1.getEnteredBy().getUserName();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+username+", Previous Entry Date-"+annex1.getEnteredOn();
			
			auditobject.saveAudit("Delete Annexure 3", details);
			dbservice.deleteAnnexure3Details(annex1);
			
			ConfigureGrid3(deletedvillage);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :"+e);
		}
	}
	public void editAnnexure4(VillageAnnexure4 annex4) {
		if (annex4 == null) {
			form4.save.setText("Add");
			form4.setVillageAnnexure4(new VillageAnnexure4());
		} else {
			form4.setVillageAnnexure4(annex4);
			form4.save.setText("Update");
		}
	}
	public void saveAnnexure4Details(Annexure4Form.SaveEvent event) {
		try {
			VillageAnnexure4 annex1=event.getVillageAnnexure4();
			annex1.setEnteredBy(uservice.getLoggedUser());
			annex1.setEnteredOn(LocalDateTime.now());
			dbservice.updateAnnexure4Details(annex1);
			String username=annex1.getEnteredBy()== null ? "null" : annex1.getEnteredBy().getUserName();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+username+", Previous Entry Date-"+annex1.getEnteredOn();
			
			auditobject.saveAudit("Save/Update Annexure 4", details);
			form4.save.setText("Add");
			ConfigureGrid4(event.getVillageAnnexure4().getVillage());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//Notification.show("Error Encountered :"+e);		
		}
	}
	public void deleteAnnexure4Details(Annexure4Form.DeleteEvent event) {
		try {
			Village deletedvillage=event.getVillageAnnexure4().getVillage();
			
			VillageAnnexure4 annex1=event.getVillageAnnexure4();
			String username=annex1.getEnteredBy()== null ? "null" : annex1.getEnteredBy().getUserName();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+username+", Previous Entry Date-"+annex1.getEnteredOn();
			
			auditobject.saveAudit("Delete Annexure 4", details);
			dbservice.deleteAnnexure4Details(annex1);
			ConfigureGrid4(deletedvillage);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :"+e);
		}
	}
	public void editAnnexure5(VillageAnnexure5 annex5) {
		if (annex5 == null) {
			form5.save.setText("Add");
			form5.setVillageAnnexure5(new VillageAnnexure5());
		} else {
			form5.setVillageAnnexure5(annex5);
			form5.save.setText("Update");
		}
	}
	public void saveAnnexure5Details(Annexure5Form.SaveEvent event) {
		try {
			VillageAnnexure5 annex1=event.getVillageAnnexure5();
			annex1.setEnteredBy(uservice.getLoggedUser());
			annex1.setEnteredOn(LocalDateTime.now());
			dbservice.updateAnnexure5Details(annex1);
			String username=annex1.getEnteredBy()== null ? "null" : annex1.getEnteredBy().getUserName();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+username+", Previous Entry Date-"+annex1.getEnteredOn();
			
			auditobject.saveAudit("Save/Update Annexure 5", details);
			form5.save.setText("Add");
			ConfigureGrid5(event.getVillageAnnexure5().getVillage());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//Notification.show("Error Encountered :"+e);		
		}
	}
	public void deleteAnnexure5Details(Annexure5Form.DeleteEvent event) {
		try {
			Village deletedvillage=event.getVillageAnnexure5().getVillage();
			VillageAnnexure5 annex1=event.getVillageAnnexure5();
			String username=annex1.getEnteredBy()== null ? "null" : annex1.getEnteredBy().getUserName();
			String details=annex1.getId()+"-Name-"+annex1.getName()+", Village-"+annex1.getVillage().getVillageName()+", Previous User:"+username+", Previous Entry Date-"+annex1.getEnteredOn();
			
			auditobject.saveAudit("Delete Annexure 5", details);
			dbservice.deleteAnnexure5Details(annex1);
			ConfigureGrid5(deletedvillage);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :"+e);
		}
	}
	
	
	

	
	private Component getAnnexure1() { 
		VerticalLayout vla=new VerticalLayout();
		Button exp1Button=new Button("Export");
		exp1Button.addClickListener(e -> GridExporter.newWithDefaults(annex1grid).open());
		Div annex1div=new Div(new
			Text("Details of Biodiversity Management Committee (BMC) of the village (One elected Chairperson and six persons nominated by the local body, not less than one third to be women and not less than 18% belonging to SC/ST) "
					), exp1Button); 
		//test.addClickListener(e->test());
		vla.setSizeFull();
		vla.add(annex1div, ConfigureGrid1(currentvillage)); 
		HorizontalLayout annex1 = new HorizontalLayout(); 
		annex1.setFlexGrow(2, vla); 
		annex1.setFlexGrow(1, form1);
		annex1.add(vla, form1); 
		annex1.setSizeFull();
		return annex1; 
	} 
	public void test() {
		GridExporter
		.newWithDefaults(this.annex1grid)
		.open();
	}
	private Component getAnnexure2() { 
		VerticalLayout vlb=new VerticalLayout();
		Button exp2Button=new Button("Export");
		exp2Button.addClickListener(e -> GridExporter.newWithDefaults(annex2grid).open());
		Div annex2div=new Div(new
			Text("List of Vaids, hakims and traditional health care (human and livestock) practitioners residing and or using biological resources occurring within the jurisdiction of the village. "
					), exp2Button); 
		//ConfigureOtherGrids();
		vlb.setSizeFull();
		vlb.add(annex2div, ConfigureGrid2(currentvillage)); 
		HorizontalLayout annex2 = new HorizontalLayout(); 
		annex2.setFlexGrow(2, vlb); 
		annex2.setFlexGrow(1, form2);
		annex2.add(vlb, form2); 
		annex2.setSizeFull();
		return annex2; 
	} 
	private Component getAnnexure3() { 
		VerticalLayout vlc=new VerticalLayout();
		Button exp3Button=new Button("Export");
		exp3Button.addClickListener(e -> GridExporter.newWithDefaults(annex3grid).open());
		Div annex3div=new Div(new
				Text("List of individuals perceived by the villagers to possess Traditional Knowledge (TK) related to biodiversity in agriculture, fisheries and forestry.  "), exp3Button); 
		vlc.setSizeFull();
		vlc.add(annex3div, ConfigureGrid3(currentvillage)); 
		HorizontalLayout annex3 = new HorizontalLayout(); 
		annex3.setFlexGrow(2, vlc); 
		annex3.setFlexGrow(1, form3);
		annex3.add(vlc, form3); 
		annex3.setSizeFull();
		return annex3; 
	} 
	private Component getAnnexure4() { 
		VerticalLayout vld=new VerticalLayout();
		Button exp4Button=new Button("Export");
		exp4Button.addClickListener(e -> GridExporter.newWithDefaults(annex4grid).open());
		Div annex4div=new Div(new
			Text("Details of schools, colleges, departments, universities, government institutions, non-governmental organization and individuals involved in the preparation of the PBR. "
					), exp4Button); 
		//ConfigureOtherGrids();
		vld.setSizeFull();
		vld.add(annex4div, ConfigureGrid4(currentvillage)); 
		HorizontalLayout annex4 = new HorizontalLayout(); 
		annex4.setFlexGrow(2, vld); 
		annex4.setFlexGrow(1, form4);
		annex4.add(vld, form4); 
		annex4.setSizeFull();
		return annex4; 
	} 
	private Component getAnnexure5() { 
		VerticalLayout vle=new VerticalLayout();
		Button exp5Button=new Button("Export");
		exp5Button.addClickListener(e -> GridExporter.newWithDefaults(annex5grid).open());
		Div annex5div=new Div(new
			Text("Details of access to biological resources and traditional knowledge granted, details of collection fee imposed and details of the benefits derived and the mode of sharing. "
					), exp5Button); 
		//ConfigureOtherGrids();
		vle.setSizeFull();
		vle.add(annex5div, ConfigureGrid5(currentvillage)); 
		HorizontalLayout annex5 = new HorizontalLayout(); 
		annex5.setFlexGrow(2, vle); 
		annex5.setFlexGrow(1, form5);
		annex5.add(vle, form5); 
		annex5.setSizeFull();
		return annex5; 
	} 
	private Component ConfigureGrid() {
		try {
			grid.removeAllColumns();
			//grid.addColumn(VillageDetails::getHabitat).setHeader("Test");
			stateColumn = grid.addColumn(villagedetails-> villagedetails.getVillage()==null ? "":villagedetails.getVillage().getBlock().getDistrict().getState().getStateName())
					.setHeader("State").setSortable(true).setAutoWidth(true).setResizable(true);
			
			districtColumn = grid.addColumn(villagedetails-> villagedetails.getVillage()==null ? "":villagedetails.getVillage().getBlock().getDistrict().getDistrictName())
					.setHeader("District").setSortable(true).setAutoWidth(true).setResizable(true);
			blockColumn = grid.addColumn(villagedetails-> villagedetails.getVillage()==null ? "":villagedetails.getVillage().getBlock().getBlockName())
					.setHeader("Block").setSortable(true).setAutoWidth(true).setResizable(true);
			villageColumn = grid.addColumn(villagedetails-> villagedetails.getVillage()==null ? "":villagedetails.getVillage().getVillageName())
					.setHeader("Village").setSortable(true).setAutoWidth(true).setResizable(true);
			geographicColumn=grid.addColumn("geographicArea").setHeader("Geographic Area").setAutoWidth(true).setResizable(true);
			grid.addColumn("habitat").setHeader("Habitat & Topography").setAutoWidth(true).setResizable(true);
			grid.addColumn(villagedetails->villagedetails.getMalePopn()).setHeader("Male Population").setAutoWidth(true).setResizable(true);
			grid.addColumn("femalePopn").setHeader("Female Population").setAutoWidth(true).setResizable(true);
			grid.addColumn(villagedetails->villagedetails.getMalePopn()+villagedetails.getFemalePopn()).setHeader("Total Population").setAutoWidth(true).setResizable(true);
			grid.addColumn("rainfall").setHeader("Rainfall(mm)").setAutoWidth(true).setResizable(true);
			grid.addColumn("temperature").setHeader("Temperature(C)").setAutoWidth(true).setResizable(true);
			grid.addColumn("weatherPatterns").setHeader("Other Weather Patterns").setAutoWidth(true).setResizable(true);
			grid.addColumn("landUse").setHeader("Land Use").setAutoWidth(true).setResizable(true);
			grid.addColumn("forestArea").setHeader("Forest Area").setAutoWidth(true).setResizable(true);
			grid.addColumn("barrenArea").setHeader("Barren Area").setAutoWidth(true).setResizable(true);
			grid.addColumn("nonagriArea").setHeader("Non Agricultural Area").setAutoWidth(true).setResizable(true);
			grid.addColumn("pastureArea").setHeader("Pasture Area").setAutoWidth(true).setResizable(true);
			grid.addColumn("miscArea").setHeader("Misc. Land(Ha)").setAutoWidth(true).setResizable(true);
			grid.addColumn("wasteArea").setHeader("Waste Land(Ha)").setAutoWidth(true).setResizable(true);
			grid.addColumn("fallowArea").setHeader("Fallow Land(Ha)").setAutoWidth(true).setResizable(true);
			grid.addColumn("currentFallowArea").setHeader("Current Fallow Land(Ha)").setAutoWidth(true).setResizable(true);
			grid.addColumn("sownArea").setHeader("Net Sown Area").setAutoWidth(true).setResizable(true);
			grid.addColumn(villagedetails->villagedetails.getManagementregime()==null?"":villagedetails.getManagementregime().getManagementregime()).setHeader("Management Regime").setAutoWidth(true).setResizable(true);
			grid.addColumn(villagedetails->villagedetails.getEnteredBy()==null? "":villagedetails.getEnteredBy().getUserName()).setHeader("Entered/Updated By").setAutoWidth(true).setResizable(true);
			grid.addColumn("enteredOn").setHeader("Entered/Updated On").setAutoWidth(true).setResizable(true);
			grid.asSingleSelect().addValueChangeListener(e -> editVillageDetails(e.getValue()));
//			HeaderRow headerRow = grid.appendHeaderRow();
//			headerRow.getCell(stateColumn).setComponent(state);
//			headerRow.getCell(districtColumn).setComponent(district);
//			headerRow.getCell(blockColumn).setComponent(block);
//			headerRow.getCell(villageColumn).setComponent(village);
			return grid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			return null;
		}
	}

	public void updateGrid() {
		try {
			List<VillageDetails> crops=dbservice.getVillageDetails();
			//grid.setItems(crops);
			geographicColumn.setFooter("Total : " + crops.size());
			grid.setSizeFull();
		} catch (Exception e) {
			geographicColumn.setFooter("Total : 0");
			e.printStackTrace();
		}
	}
	
	private void ConfigureForm1() {
		form1= new Annexure1Form(dbservice);
		form1.setVisible(true);
		form1.setWidth("50%");
		form1.setVillageAnnexure1(new VillageAnnexure1());
		form1.addListener(Annexure1Form.SaveEvent.class, this::saveAnnexure1Details);
		form1.addListener(Annexure1Form.DeleteEvent.class, this::deleteAnnexure1Details);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}
	private void ConfigureForm2() {
		form2= new Annexure2Form(dbservice);
		form2.setVisible(true);
		form2.setWidth("50%");
		form2.setVillageAnnexure2(new VillageAnnexure2());
		form2.addListener(Annexure2Form.SaveEvent.class, this::saveAnnexure2Details);
		form2.addListener(Annexure2Form.DeleteEvent.class, this::deleteAnnexure2Details);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}
	private void ConfigureForm3() {
		form3= new Annexure3Form(dbservice);
		form3.setVisible(true);
		form3.setWidth("50%");
		form3.setVillageAnnexure3(new VillageAnnexure3());
		form3.addListener(Annexure3Form.SaveEvent.class, this::saveAnnexure3Details);
		form3.addListener(Annexure3Form.DeleteEvent.class, this::deleteAnnexure3Details);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}
	private void ConfigureForm4() {
		form4= new Annexure4Form(dbservice);
		form4.setVisible(true);
		form4.setWidth("50%");
		form4.setVillageAnnexure4(new VillageAnnexure4());
		form4.addListener(Annexure4Form.SaveEvent.class, this::saveAnnexure4Details);
		form4.addListener(Annexure4Form.DeleteEvent.class, this::deleteAnnexure4Details);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}
	private void ConfigureForm5() {
		form5= new Annexure5Form(dbservice);
		form5.setVisible(true);
		form5.setWidth("50%");
		form5.setVillageAnnexure5(new VillageAnnexure5());
		form5.addListener(Annexure5Form.SaveEvent.class, this::saveAnnexure5Details);
		form5.addListener(Annexure5Form.DeleteEvent.class, this::deleteAnnexure5Details);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}
	private Component ConfigureGrid1(Village cvillage) {
		try {
			annex1grid.setItems(dbservice.getAnnexure1Details(cvillage));
			annex1grid.setColumns("name","age");
			annex1grid.addColumn(villageannexure1->villageannexure1.getGender()==null ? "": villageannexure1.getGender().getGenderName()).setHeader("Gender").setResizable(true).setSortable(true);
			annex1grid.addColumn(villageannexure1->villageannexure1.getAddress()==null ? "": villageannexure1.getAddress()).setHeader("Address").setResizable(true).setSortable(true);
			annex1grid.addColumn(villageannexure1->villageannexure1.getSpecialization()).setHeader("Area of Specialization").setResizable(true).setSortable(true);
			annex1grid.addColumn(villageannexure1->villageannexure1.getPosition()==null ? "": villageannexure1.getPosition().getPositionName()).setHeader("Position").setResizable(true).setSortable(true);
			annex1grid.addColumn(villageannexure1->villageannexure1.getTenureDate()==null ? "": villageannexure1.getPosition().getPositionName()).setHeader("Tenure Start Date").setResizable(true).setSortable(true);
			annex1grid.addColumn(villageannexure1->villageannexure1.getRemarks()).setHeader("Remarks").setResizable(true).setSortable(true);
			annex1grid.setSizeFull();
			annex1grid.asSingleSelect().addValueChangeListener(e -> editAnnexure1(e.getValue()));
			return annex1grid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			return null;
		}
	}
	private Component ConfigureGrid2(Village cvillage) {
		try {
			annex2grid.setItems(dbservice.getAnnexure2Details(cvillage));
			annex2grid.setColumns("name","age");
			annex2grid.addColumn(villageannexure2->villageannexure2.getGender()==null ? "": villageannexure2.getGender().getGenderName()).setHeader("Position").setResizable(true).setSortable(true);
			annex2grid.addColumn(villageannexure2->villageannexure2.getAddress()==null ? "": villageannexure2.getAddress()).setHeader("Address").setResizable(true).setSortable(true);
			annex2grid.addColumn(villageannexure2->villageannexure2.getSpecialization()).setHeader("Area of Specialization").setResizable(true).setSortable(true);
			annex2grid.addColumn(villageannexure2->villageannexure2.getLocation()==null ? "": villageannexure2.getLocation()).setHeader("Location from which the person accesses biological material ").setResizable(true).setSortable(true);
			annex2grid.addColumn(villageannexure2->villageannexure2.getPerception()==null ? "": villageannexure2.getPerception()).setHeader("Perception of the practitioner on the resource status").setResizable(true).setSortable(true);
			annex2grid.addColumn(villageannexure2->villageannexure2.getMedicinalUse()).setHeader("Medicinal Use").setResizable(true).setSortable(true);
			annex2grid.addColumn(villageannexure2->villageannexure2.getRemarks()).setHeader("Remarks").setResizable(true).setSortable(true);
			annex2grid.setSizeFull();
			annex2grid.asSingleSelect().addValueChangeListener(e -> editAnnexure2(e.getValue()));
			return annex2grid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			return null;
		}
	}
	private Component ConfigureGrid3(Village cvillage) {
		try {
			annex3grid.setItems(dbservice.getAnnexure3Details(cvillage));
			annex3grid.setColumns("name","age");
			annex3grid.addColumn(villageannexure3->villageannexure3.getGender()==null ? "": villageannexure3.getGender().getGenderName()).setHeader("Position").setResizable(true).setSortable(true);
			annex3grid.addColumn(villageannexure3->villageannexure3.getAddress()==null ? "": villageannexure3.getAddress()).setHeader("Address").setResizable(true).setSortable(true);
			annex3grid.addColumn(villageannexure3->villageannexure3.getSpecialization()).setHeader("Area of Specialization").setResizable(true).setSortable(true);
			annex3grid.setSizeFull();
			annex3grid.asSingleSelect().addValueChangeListener(e -> editAnnexure3(e.getValue()));
			return annex3grid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			return null;
		}
	}
	private Component ConfigureGrid4(Village cvillage) {
		try {
			annex4grid.setItems(dbservice.getAnnexure4Details(cvillage));
			annex4grid.setColumns("name","address");
			annex4grid.addColumn(villageannexure4->villageannexure4.getInstitute()).setHeader("Institution/NGO Name").setResizable(true).setSortable(true);
			annex4grid.setSizeFull();
			annex4grid.asSingleSelect().addValueChangeListener(e -> editAnnexure4(e.getValue()));
			return annex4grid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			return null;
		}
	}
	private Component ConfigureGrid5(Village fvillage) {
		try {
			annex5grid.setItems(dbservice.getAnnexure5Details(fvillage));
			annex5grid.setColumns("name","address","localName","scientificName","quantity");
			annex5grid.addColumn(villageannexure5->villageannexure5.getResolutionDate()).setHeader("Resolution Date of The BMC & Endorsement by the Village").setResizable(true).setSortable(true);
			annex5grid.addColumn(villageannexure5->villageannexure5.getFeeCollection()).setHeader("Fee Collection Details").setResizable(true).setSortable(true);
			annex5grid.addColumn(villageannexure5->villageannexure5.getAnticipatedMode()).setHeader("Anticipated mode of sharing benefits or quantum of benefits shared").setResizable(true).setSortable(true);
			annex5grid.setSizeFull();
			
			annex5grid.asSingleSelect().addValueChangeListener(e -> editAnnexure5(e.getValue()));
			return annex5grid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			return null;
		}
	}
}
