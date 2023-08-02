package com.application.megpbr.views;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.pbr.Crops;
import com.application.megpbr.data.service.CropService;
import com.application.megpbr.data.service.Dbservice;
import com.application.megpbr.views.wilddiversity.WildDiversityView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;


@PageTitle("Fodder Crops")
@Route(value = "foddercrops", layout = MainLayout.class)
@Uses(Icon.class)
public class FodderCropsView extends VerticalLayout{
	private Dbservice dbservice;
	private CropService cservice;
	Crops crop;
	Grid<Crops> grid=new Grid<>(Crops.class);
	CropPlantsForm form;
	MasterFormat format;
	TextField filterText=new TextField("");
	Checkbox masterData=new Checkbox("Master Data");
	Checkbox villageData=new Checkbox("Village Data");
	public FodderCropsView(Dbservice service, CropService cservice) {
		this.dbservice=service;
		this.cservice=cservice;
		format=dbservice.getFormat(3);
		setSizeFull();
		ConfigureGrid();
		ConfigureForm();
		updateGrid();
		add(getToolbar(),getContent());
	}
	
	private void ConfigureForm() {
		form= new CropPlantsForm(dbservice, cservice);
		form.setVisible(false);
		form.setWidth("50%");
		form.addListener(CropPlantsForm.SaveEvent.class, this::saveCrops);
		form.addListener(CropPlantsForm.DeleteEvent.class, this::deleteCrops);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}

	private Component getToolbar() {
		filterText.setPlaceholder("Search by Scientific Name or Local Name ");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		masterData.setValue(true);
		villageData.setValue(true);
		masterData.addValueChangeListener(e -> updateList(filterText.getValue(), masterData.getValue(), villageData.getValue()));
		villageData.addValueChangeListener(e -> updateList(filterText.getValue(), masterData.getValue(), villageData.getValue()));
		filterText.addValueChangeListener(e -> updateList(e.getValue(), masterData.getValue(), villageData.getValue()));
		filterText.setWidth("20%");
		Button addButton=new Button("New");
		var label=new H3("Format-"+format.getFormat());
		addButton.addClickListener(e->addCrops(new Crops()));
		HorizontalLayout topvl=new HorizontalLayout(filterText,masterData,villageData,addButton, label);
		topvl.setAlignItems(FlexComponent.Alignment.BASELINE);
		label.getStyle().set("margin-left", "auto");
		label.getStyle().set("font-weight", "bold");
		//label.getStyle().set("font-color", "hsla(120, 62%, 39%, 0.5)");
		topvl.setWidthFull();
		return topvl; 
	}
	public void updateList(String search, boolean master, boolean village) {
		grid.setItems(cservice.searchCropsByFormat(search, format, master, village));
	}
	private void openForm() {
		form.setVisible(true);
	}
	
	public void editCrops(Crops crop) {
		//form.format=format;
		//form.approved.setValue(true);
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
			form.setVisible(true);
		}
	}

	public void addCrops(Crops crop) {
		//form.format=format;
		form.initFields(format);
		grid.asSingleSelect().clear();
		form.setCrop(crop);
		crop.setFormat(format);
		form.scientificCheck.setEnabled(true);
		form.masterCheck.setEnabled(true);
		form.setVisible(true);
	}
	public void saveCrops(CropPlantsForm.SaveEvent event) {
		dbservice.updateCrop(event.getCrops());
		updateGrid();
		
	}
	
	public void deleteCrops(CropPlantsForm.DeleteEvent event) {
		//dbservice.updateCrop(event.getCrops());
		cservice.deleteCrop(event.getCrops());
		updateGrid();
	}
	
	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(grid, form);
		content.setFlexGrow(1, grid);
		content.setFlexGrow(1, form);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}
	
	public Component ConfigureGrid() {
		try {
			grid.setSizeFull();
			//grid.setColumns("scientificName","type",  "variety","localName","habitat","source", "uses","associatedTdk","otherDetails","localLanguage");
			grid.setColumns("scientificName");
			grid.addColumn("localName").setAutoWidth(true).setHeader("Local Name");
			grid.addColumn("localLanguage");
			grid.addColumn("habitat").setHeader("Habitat").setAutoWidth(true);
			grid.addColumn("type").setAutoWidth(true).setHeader("Plant Type");
			grid.addColumn("variety").setAutoWidth(true);
			grid.addColumn("fruitSeason").setHeader("Cropping Season");
			grid.addColumn("source").setHeader("Plant/Seed Source").setAutoWidth(true);
			grid.addColumn("uses").setHeader("Uses").setAutoWidth(true);
			grid.addColumn("associatedTk").setHeader("Associated TK");
			//grid.addColumn("partsUsed");
			grid.addColumn("otherDetails");
			grid.addColumn("specialFeatures");
			grid.addColumn("latitude");
			grid.addColumn("longitude");
			//grid.addColumn(Crops::getPastStatus);
			grid.addColumn(crop-> crop.getPresentStatus()==null ? "":crop.getPresentStatus().getStatus()).setHeader("Past Status").setSortable(true);
			grid.addColumn(crop-> crop.getPastStatus()==null ? "":crop.getPastStatus().getStatus()).setHeader("Present Status").setSortable(true);
			grid.addColumn(crop-> crop.getVillage()==null ? "":crop.getVillage().getVillageName()).setHeader("Village").setSortable(true);
			grid.addColumn(crop-> crop.getVillage()==null ? "":crop.getVillage().getBlock().getBlockName()).setHeader("Block").setSortable(true);
			grid.addColumn(crop-> crop.getVillage()==null ? "":crop.getVillage().getBlock().getDistrict().getDistrictName()).setHeader("District").setSortable(true);
			grid.addColumn("enteredBy");
			grid.addColumn("enteredOn");
			grid.addColumn(crop -> crop.isApproved() ? "Yes" : "No")
	        .setHeader("Verified").setFlexGrow(0);
			grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
			grid.asSingleSelect().addValueChangeListener(e -> editCrops(e.getValue()));
			return grid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			return null;
		}
	}

	public void updateGrid() {
		try {
			//grid.setItems(cservice.findCrops());
			grid.setItems(cservice.findCropsByFormat(format));
			// System.out.println("Liah");
			grid.setSizeFull();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
