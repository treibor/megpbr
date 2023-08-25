package com.application.megpbr.views.agrobiodiversity;



import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.MasterApproval;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;
import com.application.megpbr.data.entity.villages.VillageDetails;
import com.application.megpbr.data.service.CropService;
import com.application.megpbr.data.service.Dbservice;
import com.application.megpbr.views.CropPlantsForm;
import com.application.megpbr.views.MainLayout;
import com.application.megpbr.views.CropPlantsForm.DeleteEvent;
import com.application.megpbr.views.CropPlantsForm.SaveEvent;
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

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Crop Plants")
@Route(value = "cropplants", layout = MainLayout.class)
@Uses(Icon.class)
public class CropPlantsView extends HorizontalLayout{
	private Dbservice dbservice;
	private CropService cservice;
	Crops crop;
	Grid<Crops> grid=new Grid<>(Crops.class);
	CropPlantsForm form;
	MasterFormat format;
	TextField filterText=new TextField("");
	Checkbox rejectedData=new Checkbox("Show Rejected Data");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	Grid.Column<Crops> localColumn;
	RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
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
	boolean isStateUser=true;
	boolean isDistrictUser=false;
	boolean isVillageUser=false;
	
	public CropPlantsView(Dbservice service, CropService cservice) {
		this.dbservice=service;
		this.cservice=cservice;
		format=dbservice.getFormat(1);
		setSizeFull();
		ConfigureGrid();
		ConfigureForm();
		ConfigureCombo();
		initGrid();
		add(getContent());
		
	}
	private boolean getAdmin() {
		String role=dbservice.getRole();
		if(role.endsWith("ADMIN")) {
			return true;
		}else {
			return false;
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
		State state=dbservice.getState();
		district.setItems(dbservice.getDistricts(state));
		district.setItemLabelGenerator(district -> district.getDistrictName());
		district.setPlaceholder("District");
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.setPlaceholder("Block");
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), true)));
		village.setItems(dbservice.getVillages(block.getValue(), true));
		village.setItemLabelGenerator(Village::getVillageName);
		village.setPlaceholder("Village");
		getDataButton.addClickListener(e-> updateGrid());
		district.setClearButtonVisible(true);
		block.setClearButtonVisible(true);
		village.setClearButtonVisible(true);
		//Text text1=new Text("Village Data");
		radioGroup.setItems("Master Data", "Village Data");
		radioGroup.setValue("Master Data");
		radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		radioGroup.addValueChangeListener(e->updateList());
		filterText.setWidth("20%");
	}
	
	private void ConfigureForm() {
		form= new CropPlantsForm(dbservice, cservice);
		form.setVisible(false);
		form.setWidth("50%");
		form.addListener(CropPlantsForm.SaveEvent.class, this::saveCrops);
		form.addListener(CropPlantsForm.DeleteEvent.class, this::deleteCrops);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}
	
	
	public void updateList() {
		String data=radioGroup.getValue().trim();
		if(data=="Master Data") {
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
			villageColumn.setVisible(false);
			getDataButton.setVisible(false);
			approvalColumn.setVisible(false);
			updateGrid();
		}else {
			districtColumn.setVisible(true);
			blockColumn.setVisible(true);
			villageColumn.setVisible(true);
			getDataButton.setVisible(true);
			approvalColumn.setVisible(true);
			headerRow.getCell(scientificColumn).setComponent(getDataButton);
			updateGrid();
		}
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
				form.district.setValue(crop.getVillage().getBlock().getDistrict());
				form.block.setValue(crop.getVillage().getBlock());
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
		MasterApproval abc=dbservice.getMasterApprovalApproved();
		form.approved.setValue(abc);
		form.setVisible(true);
		form.save.setText("Save");
	}
	public void saveCrops(CropPlantsForm.SaveEvent event) {
		try {
			dbservice.updateCrop(event.getCrops());
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
			cservice.deleteCrop(event.getCrops());
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
			grid.addColumn("area").setHeader("Area Sown").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn(crop-> crop.getPresentStatus()==null ? "":crop.getPresentStatus().getStatus()).setHeader("Past Status").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn(crop-> crop.getPastStatus()==null ? "":crop.getPastStatus().getStatus()).setHeader("Present Status").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("fruitSeason").setHeader("Cropping Season").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("source").setHeader("Plant/Seed Source").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("uses").setHeader("Uses").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("otherDetails").setAutoWidth(true).setResizable(true).setSortable(true);
			
			grid.addColumn("specialFeatures").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("associatedTk").setHeader("Associated TK").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("knowledgeHolder").setHeader("Knowledge Holder").setAutoWidth(true).setResizable(true).setSortable(true);
			latitudeColumn=grid.addColumn("latitude");
			longitudeColumn=grid.addColumn("longitude");
			approvalColumn=grid.addColumn(crop-> crop.getApproved()==null ? "":crop.getApproved().getApproval()).setHeader("Approval Status").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("enteredBy").setAutoWidth(true).setResizable(true).setSortable(true);;
			grid.addColumn("enteredOn").setAutoWidth(true).setResizable(true).setSortable(true);;
			districtColumn.setVisible(false);
			blockColumn.setVisible(false);
			villageColumn.setVisible(false);
			approvalColumn.setVisible(false);
			latitudeColumn.setVisible(false);
			longitudeColumn.setVisible(false);
			grid.getHeaderRows().clear();
			headerRow = grid.appendHeaderRow();
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
