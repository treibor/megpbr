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
import com.vaadin.flow.component.html.Div;
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


@PageTitle("Crop Plants")
@Route(value = "cropplants", layout = MainLayout.class)
@Uses(Icon.class)
public class CropPlantsView extends VerticalLayout{
	private Dbservice dbservice;
	private CropService cservice;
	Crops crop;
	Grid<Crops> grid=new Grid<>(Crops.class);
	CropPlantsForm form;
	MasterFormat format;
	TextField filterText=new TextField("");
	Checkbox masterData=new Checkbox("Master Data");
	Checkbox villageData=new Checkbox("Village Data");
	public CropPlantsView(Dbservice service, CropService cservice) {
		this.dbservice=service;
		this.cservice=cservice;
		format=dbservice.getFormat("1");
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
		addButton.addClickListener(e->addCrops(new Crops()));
		HorizontalLayout topvl=new HorizontalLayout(filterText,masterData,villageData,addButton);
		topvl.setAlignItems(FlexComponent.Alignment.BASELINE);
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
		grid.setSizeFull();
		grid.setColumns("id","scientificName","type", "localName", "variety","habitat","pastStatus", "presentStatus","source", "fruitSeason","uses","associatedTdk","otherDetails","localLanguage","village");
		grid.addColumn(crop-> crop.getFormat().getFormatName()).setHeader("Format");
		grid.asSingleSelect().addValueChangeListener(e -> editCrops(e.getValue()));
		return grid;
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
