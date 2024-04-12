package com.megpbr.views.dashboard;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterApproval;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Markets;
import com.megpbr.data.entity.villages.VillageDetails;
import com.megpbr.data.repository.AuditRepository;
import com.megpbr.data.service.AuditService;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.MarketsService;
import com.megpbr.data.service.ScapeService;
import com.megpbr.data.service.UserService;
import com.megpbr.views.CropPlantsForm;
import com.megpbr.views.MainLayout;
import com.megpbr.views.CropPlantsForm.DeleteEvent;
import com.megpbr.views.CropPlantsForm.SaveEvent;
//import com.application.megpbr.views.wilddiversity.WildDiversityView;
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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.shared.ThemeVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import software.xdev.vaadin.grid_exporter.GridExporter;

@RolesAllowed({ "ADMIN", "USER" })
@PageTitle("Stats")
@Route(value = "stats", layout = MainLayout.class)
@Uses(Icon.class)
public class StatsView extends HorizontalLayout {
	private Dbservice dbservice;
	private CropService cservice;
	private ScapeService sservice;
	private MarketsService mservice;
	private UserService uservice;
	Grid<DtoClass> grid = new Grid<>();
	Crops crop;
	AuditTrail audit;
	// Grid<Crops> grid = new Grid<>(Crops.class);

	MasterFormat format;
	TextField filterText = new TextField("");
	Checkbox rejectedData = new Checkbox("Show Rejected Data");
	ComboBox<State> state = new ComboBox("");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	// RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
	HeaderRow headerRow;
	Button getDataButton = new Button("Search");
	Button expButton = new Button("Export");
	VerticalLayout vlx = new VerticalLayout();
	H6 totallabel = new H6();
	@Autowired
	private Audit auditobject;

	public StatsView(Dbservice service, ScapeService sservice, CropService cservice, UserService uservice,
			MarketsService mservice) {
		this.dbservice = service;
		this.cservice = cservice;
		this.mservice = mservice;
		this.sservice = sservice;
		this.uservice = uservice;
		// this.auditservice=auditservice;
		format = dbservice.getFormat(1);
		setSizeFull();
		ConfigureCombo();
		ConfigureAccess();
		initGrid();
		add(getContent());

	}

	private boolean isAdmin() {
		// UserLogin user=uservice.getLoggedUser();
		String userLevel = uservice.getLoggedUserLevel();
		if (userLevel.endsWith("ADMIN")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isStateAdmin() {
		// UserLogin user=uservice.getLoggedUser();
		String userLevel = uservice.getLoggedUserLevel();
		if (userLevel.startsWith("STATE") || userLevel.startsWith("SUPER")) {
			return true;
		} else {
			return false;
		}
	}

	private void ConfigureAccess() {

	}

	public void updateList() {

	}

	private Component getMainContent() {

		vlx.add(getToolBar2(), initGrid(), getToolBar3());
		// grid.setSizeFull();
		vlx.setSizeFull();
		return vlx;
	}

	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(getMainContent());
		// Div content = new Div(getMainContent(), form);
		content.setFlexGrow(1, vlx);
		// content.setFlexGrow(1, form);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}

	private Component getToolbar() {

		filterText.setPlaceholder("Search by Scientific/Local Name/Type ");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		// filterText.addValueChangeListener(e -> updateGrid());
		filterText.setWidth("400px");
		Button addButton = new Button("New");
		addButton.setPrefixComponent(LineAwesomeIcon.PLUS_CIRCLE_SOLID.create());
		String tempformatName = format.getFormatName();
		var label = new H3("Format-" + format.getFormat() + " - " + tempformatName);
		// addButton.addClickListener(e -> addCrops(new Crops()));
		HorizontalLayout topvl = new HorizontalLayout(label, addButton);
		topvl.setAlignItems(FlexComponent.Alignment.BASELINE);
		label.getStyle().set("margin-left", "auto");
		label.getStyle().set("font-size", "12px");
		rejectedData.getStyle().set("font-size", "12px");
		topvl.setWidthFull();
		return topvl;
	}

	public Component getToolBar2() {
		FormLayout formx = new FormLayout();
		formx.add(state, 2);
		formx.add(district, 2);
		formx.add(block, 2);
		formx.add(village, 2);
		formx.add(getDataButton, 1);
		// formx.add(expButton, 1);
		formx.setResponsiveSteps(new ResponsiveStep("0", 4),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("800px", 10));
		// VerticalLayout abc=new VerticalLayout(formx);

		return formx;
	}

	public Component getToolBar3() {
		HorizontalLayout bottom = new HorizontalLayout();
		bottom.add(totallabel, expButton);
		bottom.setAlignItems(FlexComponent.Alignment.BASELINE);
		expButton.getStyle().set("margin-left", "auto");
		bottom.setWidthFull();
		return bottom;
	}

	private void ConfigureCombo() {
		state.setItems(dbservice.getStates());
		state.setItemLabelGenerator(state -> state.getStateName());
		state.addValueChangeListener(e -> district.setItems(dbservice.getDistricts(e.getValue())));
		state.setPlaceholder("State");
		district.setItemLabelGenerator(district -> district.getDistrictName());
		// System.out.println("B");
		district.setPlaceholder("District");
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.setPlaceholder("Block");
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), true)));
		village.setItems(dbservice.getVillages(block.getValue(), true));
		village.setItemLabelGenerator(Village::getVillageName);
		village.setPlaceholder("Village");
		getDataButton.addClickListener(e -> populateGrid());
		state.setClearButtonVisible(true);
		district.setClearButtonVisible(true);
		block.setClearButtonVisible(true);
		village.setClearButtonVisible(true);
		village.setVisible(false);
		// Text text1=new Text("Village Data");
		// radioGroup.setItems("Master Data", "Village Data");
		// radioGroup.setValue("Master Data");
		// radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		// radioGroup.addValueChangeListener(e -> updateList());

		expButton.addClickListener(e -> GridExporter.newWithDefaults(grid).open());

	}

	public void populateGrid() {
		Block selblock = block.getValue();
		if (selblock == null) {
			Notification.show("Please Select Block");
		} else {
			grid.removeAllColumns();
			List<DtoClass> data = getData(block.getValue());
			// List<DtoClass> data = getDummyData();
			grid.addColumn(DtoClass::getDistrictName).setHeader("District");
			grid.addColumn(DtoClass::getBlockName).setHeader("Block").setSortable(true);
			grid.addColumn(DtoClass::getVillageName).setHeader("Village").setSortable(true);
			// grid.addColumn(DtoClass::getYear).setHeader("Year");
			// grid.addColumn(DtoClass::getTotal).setHeader("Total Crops");
			// grid.addColumn(DtoClass::getTotalScapes).setHeader("Total Scapes");
			// grid.addColumn(DtoClass::getTotalMarkets).setHeader("Total Markets");
			grid.addColumn(DtoClass::getTotal).setHeader("Total").setSortable(true);
			grid.setItems(data);
		}
	}

	public Component initGrid() {

		return grid;
	}

	private List<DtoClass> getData(Block block) {
		List<DtoClass> data = new ArrayList<>();
		List<Village> villages = dbservice.getVillages(block);
		for (Village village : villages) {
			try {
				// System.out.println(village.getBlock().getDistrict().getDistrictName()+"-"+village.getBlock().getBlockName()+"-"+village.getVillageName());
				long totalCrops = calculateTotalCrops(village);
				long totalScapes = calculateTotalScapes(village);
				long totalMarkets = calculateTotalMarkets(village);
				int total = (int) (totalCrops + totalMarkets + totalScapes);
				data.add(new DtoClass("", total, village.getVillageName(), village.getBlock().getBlockName(),
						village.getBlock().getDistrict().getDistrictName()));
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		
		return data;
	}

	private long calculateTotalCrops(Village village) {
		// Your logic to calculate total crops for the village
		long count = cservice.findCropsByVillage(village, false).size();
		return count; // Dummy value for demonstration
	}

	private long calculateTotalScapes(Village village) {
		long count = sservice.getScapesByVillage(village, false).size();
		return count;
		// Dummy value for demonstration
	}

	private long calculateTotalMarkets(Village village) {
		long count = mservice.getMarketsByVillage(village, false).size();
		return count;
	}

}
