package com.megpbr.views.agrobiodiversity;



import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

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
import com.megpbr.data.entity.pbr.Scapes;
import com.megpbr.data.entity.villages.VillageDetails;
import com.megpbr.data.repository.AuditRepository;
import com.megpbr.data.service.AuditService;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.ScapeService;
import com.megpbr.data.service.UserService;
import com.megpbr.views.CropPlantsForm;
import com.megpbr.views.MainLayout;
import com.megpbr.views.ScapeForm;
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

@RolesAllowed({"ADMIN", "USER"})
@PageTitle("Waterscape")
@Route(value = "format-9", layout = MainLayout.class)
@Uses(Icon.class)
public class WaterScapeView extends HorizontalLayout{
	private Dbservice dbservice;
	private ScapeService cservice;
	private UserService uservice;
	
	Scapes scape;
	AuditTrail audit;
	Grid<Scapes> grid=new Grid<>(Scapes.class);
	ScapeForm form;
	MasterFormat format;
	TextField filterText=new TextField("");
	Checkbox rejectedData=new Checkbox("Show Rejected Data");
	ComboBox<State> state = new ComboBox("");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	Grid.Column<Scapes> localColumn;
	RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
	Grid.Column<Scapes> stateColumn;
	Grid.Column<Scapes> districtColumn;
	Grid.Column<Scapes> blockColumn;
	Grid.Column<Scapes> villageColumn;
	Grid.Column<Scapes> approvalColumn;
	//Grid.Column<Scape> scientificColumn;
	Grid.Column<Scapes> latitudeColumn;
	Grid.Column<Scapes> longitudeColumn;
	HeaderRow headerRow;
	Button getDataButton=new Button("Search");
	Button expButton = new Button("Export ");
	VerticalLayout vlx = new VerticalLayout();
	H6 totallabel = new H6();
	@Autowired
	private Audit auditobject;
	public WaterScapeView(Dbservice service, ScapeService cservice, UserService uservice) {
		this.dbservice=service;
		this.cservice=cservice;
		this.uservice=uservice;
		//this.auditservice=auditservice;
		format=dbservice.getFormat(9);
		setSizeFull();
		ConfigureGrid();
		ConfigureForm();
		ConfigureCombo();
		ConfigureAccess();
		initGrid();
		add(getContent());
		
	}
	private boolean isAdmin() {
		//UserLogin user=uservice.getLoggedUser();
		String userLevel=uservice.getLoggedUserLevel();
		if(userLevel.endsWith("ADMIN")) {
			return true;
		}else {
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
		UserLogin user=uservice.getLoggedUser();
		String userLevel=uservice.getLoggedUserLevel();
		if (userLevel.startsWith("SUPER")) {
			state.setValue(user.getState());
			stateColumn.setVisible(true);
			state.setVisible(true);
		} else if(userLevel.startsWith("STATE")) {
			//System.out.println("C");
			state.setValue(user.getState());
			stateColumn.setVisible(false);
		}else if(userLevel.startsWith("DISTRICT")) {
			//System.out.println("C");
			state.setValue(user.getState());
			district.setValue(user.getDistrict());
			stateColumn.setVisible(false);
			districtColumn.setVisible(false);
		}else if(userLevel.startsWith("BLOCK")) {
			state.setValue(user.getState());
			district.setValue(user.getDistrict());
			block.setValue(user.getBlock());
			stateColumn.setVisible(false);
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
		}else if(userLevel.startsWith("VILLAGE")) {
			state.setValue(user.getState());
			district.setValue(user.getDistrict());
			block.setValue(user.getBlock());
			village.setValue(user.getVillage());
			stateColumn.setVisible(false);
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
			villageColumn.setVisible(false);
			getDataButton.setVisible(false);
		}
	}
	
	public void updateList() {
		String data = radioGroup.getValue().trim();
		if (data == "Master Data") {
			// stateC
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
			villageColumn.setVisible(false);
			getDataButton.setVisible(false);
			approvalColumn.setVisible(false);
			district.setVisible(false);
			block.setVisible(false);
			village.setVisible(false);
			updateGrid();
		} else {
			districtColumn.setVisible(true);
			blockColumn.setVisible(true);
			villageColumn.setVisible(true);
			getDataButton.setVisible(true);
			approvalColumn.setVisible(true);
			district.setVisible(true);
			block.setVisible(true);
			village.setVisible(true);
			ConfigureAccess();
			updateGrid();
		}
	}
	
	private Component getMainContent() {

		vlx.add(getToolbar(), getToolBar2(), grid, getToolBar3());
		grid.setSizeFull();
		vlx.setSizeFull();
		return vlx;
	}

	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(getMainContent(), form);
		// Div content = new Div(getMainContent(), form);
		content.setFlexGrow(1, vlx);
		content.setFlexGrow(1, form);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}

	private Component getToolbar() {
		state.setVisible(false);
		district.setVisible(false);
		block.setVisible(false);
		village.setVisible(false);
		getDataButton.setVisible(false);
		filterText.setPlaceholder("Search by Scientific/Local Name/Type ");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateGrid());
		filterText.setWidth("400px");
		Button addButton = new Button("New");
		addButton.setPrefixComponent(LineAwesomeIcon.PLUS_CIRCLE_SOLID.create());
		String tempformatName = format.getFormatName();
		var label = new H3("Format-" + format.getFormat() + " - " + tempformatName);
		addButton.addClickListener(e -> addScape(new Scapes()));
		HorizontalLayout topvl = new HorizontalLayout(radioGroup, filterText, label, addButton);
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
		state.setItemLabelGenerator(state->state.getStateName());
		state.addValueChangeListener(e->district.setItems(dbservice.getDistricts(e.getValue())));
		state.setPlaceholder("State");
		district.setItemLabelGenerator(district -> district.getDistrictName());
		//System.out.println("B");
		district.setPlaceholder("District");
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.setPlaceholder("Block");
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), true)));
		village.setItems(dbservice.getVillages(block.getValue(), true));
		village.setItemLabelGenerator(Village::getVillageName);
		village.setPlaceholder("Village");
		getDataButton.addClickListener(e-> updateGrid());
		state.setClearButtonVisible(true);
		district.setClearButtonVisible(true);
		block.setClearButtonVisible(true);
		village.setClearButtonVisible(true);
		//Text text1=new Text("Village Data");
		radioGroup.setItems("Master Data", "Village Data");
		radioGroup.setValue("Master Data");
		//radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		radioGroup.addValueChangeListener(e->updateList());
		filterText.setWidth("20%");
		//System.out.println("Village Exist");
	}
	
	private void ConfigureForm() {
		form= new ScapeForm(dbservice, cservice);
		form.setVisible(false);
		form.setWidth("50%");
		form.addListener(ScapeForm.SaveEvent.class, this::saveScape);
		form.addListener(ScapeForm.DeleteEvent.class, this::deleteScape);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}
	
	
	
	public void initGrid() {
		try {
			List<Scapes> scapes=cservice.getScapesByFormatAndMaster(format, true);
			grid.setItems(scapes);
			totallabel.setText("Total : "+scapes.size());
			grid.setSizeFull();
		} catch (Exception e) {
			totallabel.setText("Total : 0");
			e.printStackTrace();
		}
	}
	
	
	public void updateGrid() {
		String data = radioGroup.getValue().trim();
		if (data != "Master Data") {
			if (district.getValue() == null) {
				grid.setItems(Collections.EMPTY_LIST);
				totallabel.setText("Total : 0");
			} else if (village.getValue() == null && block.getValue() == null) {
				grid.setItems(Collections.EMPTY_LIST);
				totallabel.setText("Total : 0");
			} else if (village.getValue() == null && block.getValue() != null) {
				updateBlockList();
			} else {
				updateVillageList();
			}
		}else {
			try {
				String search =filterText.getValue();
				//List<Scape> scapes=cservice.findScapeByFormatAndMaster(format, true);
				List<Scapes> scapes=cservice.searchScapeFilter(search, format);
				grid.setItems(scapes);
				totallabel.setText("Total : "+scapes.size());
				grid.setSizeFull();
			} catch (Exception e) {
				totallabel.setText("Total : 0");
				e.printStackTrace();
			}
		}
	}
	public void updateDistrictList() {
		String search =filterText.getValue();
		List<Scapes> villagesList=cservice.searchScapeFilter(district.getValue(), search, format);
		grid.setItems(villagesList);
		if(villagesList.size()==0) {
			//Notification.show("No Data Found in the District"+villagesList.get(0).getVillage().getBlock().getDistrict()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			totallabel.setText("Total : 0");
		}else {
			totallabel.setText("Total : " + villagesList.size());
		}
	}
	public void updateBlockList() {
		String search =filterText.getValue();
		List<Scapes> villagesList=cservice.searchScapeFilter(block.getValue(), search, format);
		grid.setItems(villagesList);
		if(villagesList.size()==0) {
			//Notification.show("No Data Found in the Selected Block +"+block.getValue().getBlockName()).addThemeVariants(NotificationVariant.LUMO_WARNING);
			totallabel.setText("Total : 0");
		}else {
			totallabel.setText("Total : " + villagesList.size());
		}
	}
	public void updateVillageList() {
		String search =filterText.getValue();
		List<Scapes> villagesList=cservice.searchScapeFilter(village.getValue(), search, format);
		grid.setItems(villagesList);
		if(villagesList.size()==0) {
			Notification.show("No Data Found in the Selected Village :"+village.getValue().getVillageName()).addThemeVariants(NotificationVariant.LUMO_WARNING);
			totallabel.setText("Total : 0");
		}else {
			totallabel.setText("Total : " + villagesList.size());
		}
	}
	
	private void ConfigureFormAccess() {
		UserLogin user=uservice.getLoggedUser();
		String userLevel=uservice.getLoggedUserLevel();
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
		UserLogin user=uservice.getLoggedUser();
		String userLevel=uservice.getLoggedUserLevel();
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
	public void editScape(Scapes scape) {
		form.initFields(format);
		form.scientificCheck.setEnabled(false);
		form.masterCheck.setEnabled(false);
		
		if (scape == null) {
			form.setVisible(false);
		} else {
			if (scape.getVillage() != null) {
				form.frommaster.setVisible(true);
				form.masterCheck.setValue(false);
				form.state.setValue(scape.getVillage().getBlock().getDistrict().getState());
				form.district.setValue(scape.getVillage().getBlock().getDistrict());
				form.block.setValue(scape.getVillage().getBlock());
				ConfigureFormAccess();
			} else {
				form.frommaster.setVisible(false);
				form.masterCheck.setValue(true);
			}
			form.setScape(scape);
			byte[] picture1 = scape.getPhoto1();
			byte[] picture2 = scape.getPhoto2();
			byte[] picture3 = scape.getPhoto3();
			byte[] picture4 = scape.getPhoto4();
			if (picture1 != null) {
				StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(picture1));
				Image image = new Image(resource, "No Image");
				form.addImage(form.imageLayout1, image, form.imageContainer1);
			}else {
				form.removeImage(form.imageLayout1, form.imageContainer1);
			}

			if (picture2 != null) {
				StreamResource resource2 = new StreamResource("image.jpg", () -> new ByteArrayInputStream(picture2));
				Image image2 = new Image(resource2, "No Image");
				form.addImage(form.imageLayout2, image2, form.imageContainer2);
			}else {
				form.removeImage(form.imageLayout2, form.imageContainer2);
			}
			if (picture3 != null) {
				StreamResource resource3 = new StreamResource("image.jpg", () -> new ByteArrayInputStream(picture3));
				Image image3 = new Image(resource3, "No Image");
				form.addImage(form.imageLayout3, image3, form.imageContainer3);
			}else {
				form.removeImage(form.imageLayout3, form.imageContainer3);
			}
			if (picture4 != null) {
				StreamResource resource4 = new StreamResource("image.jpg", () -> new ByteArrayInputStream(picture4));
				Image image4 = new Image(resource4, "No Image");
				form.addImage(form.imageLayout4, image4, form.imageContainer4);
			}else {
				form.removeImage(form.imageLayout4, form.imageContainer4);
			}
			form.save.setText("Update");
			form.delete.setVisible(isAdmin());
			stateAdminCheck();
			form.setVisible(true);
		}
	}

	public void addScape(Scapes scape) {
		//form.format=format;
		form.initFields(format);
		grid.asSingleSelect().clear();
		form.setScape(scape);
		scape.setFormat(format);
		form.removeAllImages();
		form.scientificCheck.setEnabled(true);
		form.masterCheck.setEnabled(true);
		form.frommaster.setVisible(true);
		form.masterCheck.setValue(false);
		MasterApproval abc=dbservice.getMasterApprovalApproved();
		form.approved.setValue(abc);
		ConfigureNewFormAccess();
		form.setVisible(true);
		form.save.setText("Save");
		stateAdminCheck();
	}
	public void stateAdminCheck() {
		if (!isStateAdmin()) {
			form.masterCheck.setVisible(false);
		} else if (form.masterCheck.getValue() && !isStateAdmin()) {
			form.save.setEnabled(false);
			form.delete.setEnabled(false);
			form.masterCheck.setVisible(false);
		} else {
			form.save.setEnabled(true);
			form.delete.setEnabled(true);
			form.masterCheck.setVisible(true);
		}
	}
	public void saveScape(ScapeForm.SaveEvent event) {
		try {
			Scapes scape=event.getScapes();
			scape.setEnteredBy(uservice.getLoggedUser());
			scape.setEnteredOn(LocalDateTime.now());
			cservice.saveScape(scape);
			auditobject.saveAudit(scape, "Save/Update");
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			updateGrid();
			addScape(new Scapes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :"+e);		
		}
	}
	
	
	public void deleteScape(ScapeForm.DeleteEvent event) {
		
		try {
			Scapes scape=event.getScapes();
			auditobject.saveAudit(scape, "Delete");
			cservice.deleteScape(scape);
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			updateGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :"+e);
		}
	}
	
	
	
	public Component ConfigureGrid() {
		try {
			grid.setSizeFull();
			grid.removeAllColumns();
			stateColumn= grid.addColumn(scape -> scape.getVillage() == null ? "": scape.getVillage().getBlock().getDistrict().getState().getStateName())
					.setHeader("State").setSortable(true).setAutoWidth(true).setResizable(true);
			districtColumn = grid.addColumn(scape -> scape.getVillage() == null ? "": scape.getVillage().getBlock().getDistrict().getDistrictName())
					.setHeader("District").setSortable(true).setAutoWidth(true).setResizable(true);
			blockColumn = grid.addColumn(scape -> scape.getVillage() == null ? "" : scape.getVillage().getBlock().getBlockName())
					.setHeader("Block").setSortable(true).setAutoWidth(true).setResizable(true);
			villageColumn = grid.addColumn(scape -> scape.getVillage() == null ? "" : scape.getVillage().getVillageName())
					.setHeader("Village").setSortable(true).setAutoWidth(true).setResizable(true);
			//grid.addColumn("type").setAutoWidth(true).setHeader("Plant Type").setAutoWidth(true).setResizable(true).setSortable(true);
			localColumn=grid.addColumn(scape->scape.getFaunaPopulation()).setHeader("Element Type").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("floraOccupation").setHeader("Sub Type").setAutoWidth(true).setResizable(true);
			grid.addColumn("typeAgriOccupation").setHeader("Features & Approx Area").setAutoWidth(true).setResizable(true);
			grid.addColumn("landscape").setHeader("Ownership").setAutoWidth(true).setResizable(true);
			grid.addColumn("fallow").setHeader("General Flora").setAutoWidth(true).setResizable(true);
			grid.addColumn("forest").setHeader("General Fauna").setAutoWidth(true).setResizable(true);
			grid.addColumn("wetLand").setHeader("Major Uses").setAutoWidth(true).setResizable(true);
			//grid.addColumn("subLandscape").setHeader("Sub Landscapes").setAutoWidth(true).setResizable(true);
			//grid.addColumn("features").setHeader("Features & Approx Area").setAutoWidth(true).setResizable(true);
			//grid.addColumn("ownerHouse").setHeader("Ownership").setAutoWidth(true).setResizable(true);
			grid.addColumn("userGroups").setHeader("User Groups").setAutoWidth(true).setResizable(true);
			grid.addColumn("management").setHeader("Management Practices").setAutoWidth(true).setResizable(true);
			grid.addColumn("generalResources").setHeader("General Uses").setAutoWidth(true).setResizable(true);
			grid.addColumn("associatedTk").setHeader("Associated TK").setAutoWidth(true).setResizable(true);
			grid.addColumn("otherDetails").setHeader("Other Details").setAutoWidth(true).setResizable(true);
			grid.addColumn("socialCommunity").setHeader("Community Accessed").setAutoWidth(true).setResizable(true);
			//grid.addColumn("features").setHeader("Sources of Fish").setAutoWidth(true).setResizable(true);
			grid.addColumn("remarks").setHeader("Remarks").setAutoWidth(true).setResizable(true);
			latitudeColumn=grid.addColumn("latitude");
			longitudeColumn=grid.addColumn("longitude");
			approvalColumn=grid.addColumn(scape-> scape.getApproved()==null ? "":scape.getApproved().getApproval()).setHeader("Approval Status").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn(scape-> scape.getEnteredBy()==null ? "":scape.getEnteredBy().getName()).setHeader("Entered/Updated By").setAutoWidth(true).setResizable(true).setSortable(true);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			grid.addColumn(scape->scape.getEnteredOn()==null ? "": scape.getEnteredOn().format(formatter)).setHeader("Entered/Updated On").setAutoWidth(true).setResizable(true).setSortable(true);
			stateColumn.setVisible(false);
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
			villageColumn.setVisible(false);
			approvalColumn.setVisible(false);
			latitudeColumn.setVisible(false);
			longitudeColumn.setVisible(false);
			grid.asSingleSelect().addValueChangeListener(e -> editScape(e.getValue()));
			return grid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			return null;
		}
	}

	

}
