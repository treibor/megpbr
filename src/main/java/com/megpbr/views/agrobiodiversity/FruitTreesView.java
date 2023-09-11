package com.megpbr.views.agrobiodiversity;



import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
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
import com.megpbr.data.entity.villages.VillageDetails;
import com.megpbr.data.repository.AuditRepository;
import com.megpbr.data.service.AuditService;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
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
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
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
@PageTitle("Fruit Trees")
@Route(value = "format-2", layout = MainLayout.class)
@Uses(Icon.class)
public class FruitTreesView extends HorizontalLayout{
	private Dbservice dbservice;
	private CropService cservice;
	private UserService uservice;
	
	Crops crop;
	AuditTrail audit;
	Grid<Crops> grid=new Grid<>(Crops.class);
	CropPlantsForm form;
	MasterFormat format;
	TextField filterText=new TextField("");
	Checkbox rejectedData=new Checkbox("Show Rejected Data");
	ComboBox<State> state = new ComboBox("");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	Grid.Column<Crops> localColumn;
	RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
	Grid.Column<Crops> stateColumn;
	Grid.Column<Crops> districtColumn;
	Grid.Column<Crops> blockColumn;
	Grid.Column<Crops> villageColumn;
	Grid.Column<Crops> approvalColumn;
	Grid.Column<Crops> scientificColumn;
	Grid.Column<Crops> latitudeColumn;
	Grid.Column<Crops> longitudeColumn;
	HeaderRow headerRow;
	Button getDataButton=new Button("Get Data");
	VerticalLayout vlx=new VerticalLayout();
	@Autowired
	private Audit auditobject;
	public FruitTreesView(Dbservice service, CropService cservice, UserService uservice) {
		this.dbservice=service;
		this.cservice=cservice;
		this.uservice=uservice;
		//this.auditservice=auditservice;
		format=dbservice.getFormat(2);
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
	private void ConfigureAccess() {
		UserLogin user=uservice.getLoggedUser();
		String userLevel=uservice.getLoggedUserLevel();
		if(userLevel.startsWith("STATE")) {
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
		String data=radioGroup.getValue().trim();
		if(data=="Master Data") {
			//stateC
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
			villageColumn.setVisible(false);
			getDataButton.setVisible(false);
			approvalColumn.setVisible(false);
			updateGrid();
		}else {
			stateColumn.setVisible(true);
			districtColumn.setVisible(true);
			blockColumn.setVisible(true);
			villageColumn.setVisible(true);
			getDataButton.setVisible(true);
			approvalColumn.setVisible(true);
			headerRow.getCell(scientificColumn).setComponent(getDataButton);
			ConfigureAccess();
			updateGrid();
		}
	}
	
	private Component getMainContent() {
		
		vlx.add(getToolbar(), grid);
		grid.setSizeFull();
		vlx.setSizeFull();
		return vlx;
	}
	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(getMainContent(), form);
		//Div content = new Div(getMainContent(), form);
		content.setFlexGrow(1, vlx);
		content.setFlexGrow(1, form);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}
	private Component getToolbar() {
		filterText.setPlaceholder("Search by Scientific/Local Name/Type ");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateGrid());
		Button addButton=new Button("New");
		addButton.setPrefixComponent(LineAwesomeIcon.PLUS_CIRCLE_SOLID.create());
		String tempformatName=format.getFormatName();
		String formatName;
		var label=new H3("Format-"+format.getFormat()+" - "+tempformatName);
		addButton.addClickListener(e->addCrops(new Crops()));
		HorizontalLayout topvl=new HorizontalLayout(radioGroup,filterText,addButton,label  );
		topvl.setAlignItems(FlexComponent.Alignment.BASELINE);
		label.getStyle().set("margin-left", "auto");
		label.getStyle().set("font-size", "12px");
		rejectedData.getStyle().set("font-size", "12px");
		topvl.setWidthFull();
		return topvl; 
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
		radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		radioGroup.addValueChangeListener(e->updateList());
		filterText.setWidth("20%");
		//System.out.println("Village Exist");
	}
	
	private void ConfigureForm() {
		form= new CropPlantsForm(dbservice, cservice);
		form.setVisible(false);
		form.setWidth("50%");
		form.addListener(CropPlantsForm.SaveEvent.class, this::saveCrops);
		form.addListener(CropPlantsForm.DeleteEvent.class, this::deleteCrops);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}
	
	
	
	public void initGrid() {
		try {
			List<Crops> crops=cservice.findCropsByFormatAndMaster(format, true);
			grid.setItems(crops);
			localColumn.setFooter("Total : "+crops.size());
			grid.setSizeFull();
		} catch (Exception e) {
			localColumn.setFooter("Total : 0");
			e.printStackTrace();
		}
	}
	
	
	public void updateGrid() {
		String data = radioGroup.getValue().trim();
		if (data != "Master Data") {
			if (district.getValue() == null) {
				grid.setItems(Collections.EMPTY_LIST);
				localColumn.setFooter("Total : 0");
			} else if (village.getValue() == null && block.getValue() == null) {
				grid.setItems(Collections.EMPTY_LIST);
				localColumn.setFooter("Total : 0");
			} else if (village.getValue() == null && block.getValue() != null) {
				updateBlockList();
			} else {
				updateVillageList();
			}
		}else {
			try {
				String search =filterText.getValue();
				//List<Crops> crops=cservice.findCropsByFormatAndMaster(format, true);
				List<Crops> crops=cservice.searchCropsFilter(search, format);
				grid.setItems(crops);
				localColumn.setFooter("Total : "+crops.size());
				grid.setSizeFull();
			} catch (Exception e) {
				localColumn.setFooter("Total : 0");
				e.printStackTrace();
			}
		}
	}
	public void updateDistrictList() {
		String search =filterText.getValue();
		List<Crops> villagesList=cservice.searchCropsFilter(district.getValue(), search, format);
		grid.setItems(villagesList);
		if(villagesList.size()==0) {
			//Notification.show("No Data Found in the District"+villagesList.get(0).getVillage().getBlock().getDistrict()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			localColumn.setFooter("Total : 0");
		}else {
			localColumn.setFooter("Total : " + villagesList.size());
		}
	}
	public void updateBlockList() {
		String search =filterText.getValue();
		List<Crops> villagesList=cservice.searchCropsFilter(block.getValue(), search, format);
		grid.setItems(villagesList);
		if(villagesList.size()==0) {
			//Notification.show("No Data Found in the Selected Block +"+block.getValue().getBlockName()).addThemeVariants(NotificationVariant.LUMO_WARNING);
			localColumn.setFooter("Total : 0");
		}else {
			localColumn.setFooter("Total : " + villagesList.size());
		}
	}
	public void updateVillageList() {
		String search =filterText.getValue();
		List<Crops> villagesList=cservice.searchCropsFilter(village.getValue(), search, format);
		grid.setItems(villagesList);
		if(villagesList.size()==0) {
			Notification.show("No Data Found in the Selected Village :"+village.getValue().getVillageName()).addThemeVariants(NotificationVariant.LUMO_WARNING);
			localColumn.setFooter("Total : 0");
		}else {
			localColumn.setFooter("Total : " + villagesList.size());
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
	public void editCrops(Crops crop) {
		form.initFields(format);
		form.scientificCheck.setEnabled(false);
		form.masterCheck.setEnabled(false);
		if (crop == null) {
			form.setVisible(false);
		} else {
			if (crop.getVillage() != null) {
				// System.out.println("Village Exist");
				form.frommaster.setVisible(true);
				form.masterCheck.setValue(false);
				form.state.setValue(crop.getVillage().getBlock().getDistrict().getState());
				form.district.setValue(crop.getVillage().getBlock().getDistrict());
				form.block.setValue(crop.getVillage().getBlock());
				ConfigureFormAccess();
			} else {
				form.frommaster.setVisible(false);
				form.masterCheck.setValue(true);
			}
			form.setCrop(crop);
			byte[] picture1 = crop.getPhoto1();
			byte[] picture2 = crop.getPhoto2();
			byte[] picture3 = crop.getPhoto3();
			byte[] picture4 = crop.getPhoto4();
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
			form.setVisible(true);
		}
	}

	public void addCrops(Crops crop) {
		//form.format=format;
		form.initFields(format);
		grid.asSingleSelect().clear();
		form.setCrop(crop);
		crop.setFormat(format);
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
	}
	public void saveCrops(CropPlantsForm.SaveEvent event) {
		try {
			Crops crop=event.getCrops();
			crop.setEnteredBy(uservice.getLoggedUser());
			crop.setEnteredOn(LocalDateTime.now());
			dbservice.updateCrop(crop);
			auditobject.saveAudit(crop, "Save/Update");
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			updateGrid();
			addCrops(new Crops());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :"+e);		
		}
	}
	
	
	public void deleteCrops(CropPlantsForm.DeleteEvent event) {
		
		try {
			Crops crop=event.getCrops();
			auditobject.saveAudit(crop, "Delete");
			cservice.deleteCrop(crop);
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
			stateColumn= grid.addColumn(crop -> crop.getVillage() == null ? "": crop.getVillage().getBlock().getDistrict().getState().getStateName())
					.setHeader("State").setSortable(true).setAutoWidth(true).setResizable(true);
			districtColumn = grid.addColumn(crop -> crop.getVillage() == null ? "": crop.getVillage().getBlock().getDistrict().getDistrictName())
					.setHeader("District").setSortable(true).setAutoWidth(true).setResizable(true);
			blockColumn = grid.addColumn(crop -> crop.getVillage() == null ? "" : crop.getVillage().getBlock().getBlockName())
					.setHeader("Block").setSortable(true).setAutoWidth(true).setResizable(true);
			villageColumn = grid.addColumn(crop -> crop.getVillage() == null ? "" : crop.getVillage().getVillageName())
					.setHeader("Village").setSortable(true).setAutoWidth(true).setResizable(true);
			grid.addColumn("type").setAutoWidth(true).setHeader("Plant Type").setAutoWidth(true).setResizable(true).setSortable(true);
			
			scientificColumn=grid.addColumn("scientificName").setAutoWidth(true).setResizable(true).setSortable(true);
			localColumn=grid.addColumn("localName").setHeader("Local Name").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("localLanguage").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("habitat").setHeader("Habitat").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("variety").setAutoWidth(true).setResizable(true).setSortable(true);
			//grid.addColumn("area").setHeader("Area Sown").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn(crop-> crop.getPresentStatus()==null ? "":crop.getPresentStatus().getStatus()).setHeader("Past Status").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn(crop-> crop.getPastStatus()==null ? "":crop.getPastStatus().getStatus()).setHeader("Present Status").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("fruitSeason").setHeader("Cropping Season").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("source").setHeader("Plant/Seed Source").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("uses").setHeader("Uses").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("otherDetails").setAutoWidth(true).setResizable(true).setSortable(true);
			//grid.addColumn("specialFeatures").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("associatedTk").setHeader("Associated TK").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("knowledgeHolder").setHeader("Knowledge Holder").setAutoWidth(true).setResizable(true).setSortable(true);
			latitudeColumn=grid.addColumn("latitude");
			longitudeColumn=grid.addColumn("longitude");
			approvalColumn=grid.addColumn(crop-> crop.getApproved()==null ? "":crop.getApproved().getApproval()).setHeader("Approval Status").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn(crop-> crop.getEnteredBy()==null ? "":crop.getEnteredBy().getName()).setHeader("Entered/Updated By").setAutoWidth(true).setResizable(true).setSortable(true);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			grid.addColumn(crop->crop.getEnteredOn()==null ? "": crop.getEnteredOn().format(formatter)).setHeader("Entered/Updated On").setAutoWidth(true).setResizable(true).setSortable(true);
			stateColumn.setVisible(false);
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
			villageColumn.setVisible(false);
			approvalColumn.setVisible(false);
			latitudeColumn.setVisible(false);
			longitudeColumn.setVisible(false);
			grid.getHeaderRows().clear();
			headerRow = grid.appendHeaderRow();
			headerRow.getCell(stateColumn).setComponent(state);
			headerRow.getCell(districtColumn).setComponent(district);
			headerRow.getCell(blockColumn).setComponent(block);
			headerRow.getCell(villageColumn).setComponent(village);
			grid.asSingleSelect().addValueChangeListener(e -> editCrops(e.getValue()));
			
			return grid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			return null;
		}
	}

	

}
