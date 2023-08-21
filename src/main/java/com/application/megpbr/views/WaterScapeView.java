package com.application.megpbr.views;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;
import com.application.megpbr.data.entity.pbr.Scapes;
import com.application.megpbr.data.service.CropService;
import com.application.megpbr.data.service.Dbservice;
import com.application.megpbr.data.service.ScapeService;
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
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import jakarta.annotation.PostConstruct;

@PageTitle("Waterscape")
@Route(value = "waterscape", layout = MainLayout.class)
@Uses(Icon.class)
public class WaterScapeView extends VerticalLayout {
	private Dbservice dbservice;
	private ScapeService sservice;
	// Scapes crop;
	Grid<Scapes> grid = new Grid<>(Scapes.class);
	ScapeForm form;
	MasterFormat format;
	TextField filterText = new TextField("");
	Checkbox masterData = new Checkbox("Master Data");
	Checkbox villageData = new Checkbox("Village Data");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	Grid.Column<Scapes> floraColumn;

	public WaterScapeView(Dbservice service, ScapeService cservice) {
		this.dbservice = service;
		this.sservice = cservice;
		format = dbservice.getFormat(9);
		setSizeFull();
		ConfigureGrid();
		ConfigureForm();
		ConfigureCombo();
		updateGrid();
		add(getToolbar(), getContent());
	}

	private void ConfigureCombo() {
		// TODO Auto-generated method stub
		State state = dbservice.getState();
		district.setItems(dbservice.getDistricts(state));
		district.setItemLabelGenerator(district -> district.getDistrictName());
		district.setPlaceholder("District");
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		// district.addValueChangeListener(e -> updateList(filterText.getValue(),
		// false));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.setPlaceholder("Block");
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), true)));
		village.setItems(dbservice.getVillages(block.getValue(), true));
		village.setItemLabelGenerator(Village::getVillageName);
		village.setPlaceholder("Village");
		village.addValueChangeListener(e -> updateList());
		village.setEnabled(false);
	}

	private void ConfigureForm() {
		form = new ScapeForm(dbservice, sservice);
		form.setVisible(false);
		form.setWidth("50%");
		form.addListener(ScapeForm.SaveEvent.class, this::saveScape);
		form.addListener(ScapeForm.DeleteEvent.class, this::deleteScape);
		// form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
	}

	private Component getToolbar() {
		filterText.setPlaceholder("Search");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		masterData.setValue(true);
		villageData.setValue(true);
		masterData.addValueChangeListener(e -> updateList());
		villageData.addValueChangeListener(e -> updateList());
		filterText.addValueChangeListener(e -> updateList());
		filterText.setWidth("20%");
		Button addButton = new Button("New");
		addButton.setPrefixComponent(LineAwesomeIcon.PLUS_CIRCLE_SOLID.create());
		String tempformatName = format.getFormatName();
		var label = new H3("Format-" + format.getFormat() + " - " + tempformatName);
		addButton.addClickListener(e -> addCrops(new Scapes()));
		HorizontalLayout topvl = new HorizontalLayout(filterText, masterData, villageData, addButton, label);
		topvl.setAlignItems(FlexComponent.Alignment.BASELINE);
		label.getStyle().set("margin-left", "auto");
		// label.getStyle().set("font-weight", "bold");
		label.getStyle().set("font-size", "12px");
		masterData.getStyle().set("font-size", "12px");
		villageData.getStyle().set("font-size", "12px");
		// label.getStyle().set("font-color", "hsla(120, 62%, 39%, 0.5)");
		topvl.setWidthFull();
		return topvl;
	}

	public void updateList() {
		Village villageValue=village.getValue();
		String search =filterText.getValue();
		boolean masterCheck=masterData.getValue();
		boolean villageCheck=villageData.getValue();
		List<Scapes> scapeData=null;
		if(masterCheck && villageCheck) {
			scapeData=sservice.searchScapesFilter(villageValue, search, format);
			grid.setItems(scapeData);
			village.setEnabled(true);
			//localColumn.setFooter("Total :"+cropsData.size());
		}else if(!villageCheck && !masterCheck) {
			//System.out.println("Empty");
			village.setEnabled(false);
			grid.setItems(Collections.emptyList());
			//localColumn.setFooter("");
		}else if(!masterCheck && villageCheck) {
			scapeData=sservice.searchScapesFilter(villageValue, search, format, masterCheck);
			grid.setItems(scapeData);
			//localColumn.setFooter("Total :"+cropsData.size());
			village.setEnabled(true);
		}else if( villageCheck) {
			village.setEnabled(true);
		}else if(masterCheck || !masterCheck) {
			scapeData=sservice.searchScapesFilter(villageValue, search, format, masterCheck);
			grid.setItems(scapeData);
			//localColumn.setFooter("Total :"+cropsData.size());
			if(masterCheck) {
				village.setEnabled(false);
			}
		}
		if(scapeData==null) {
			floraColumn.setFooter("Total : 0");
		}else {
			floraColumn.setFooter("Total : "+scapeData.size());
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
				// System.out.println("Village Exist");
				form.frommaster.setVisible(true);
				form.masterCheck.setValue(false);
				form.district.setValue(scape.getVillage().getBlock().getDistrict());
				form.block.setValue(scape.getVillage().getBlock());
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
			form.setVisible(true);
		}
	}

	public void addCrops(Scapes scape) {
		// form.format=format;
		form.initFields(format);
		grid.asSingleSelect().clear();
		form.setScape(scape);
		scape.setFormat(format);
		form.removeAllImages();
		form.scientificCheck.setEnabled(true);
		form.masterCheck.setEnabled(true);
		form.setVisible(true);
	}

	public void saveScape(ScapeForm.SaveEvent event) {
		try {
			sservice.saveScape(event.getScapes());
			updateGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :" + e);
		}

	}

	public void deleteScape(ScapeForm.DeleteEvent event) {
		// dbservice.updateCrop(event.getCrops());
		try {
			sservice.deleteScape(event.getScapes());
			updateGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// Notification.show("Error Encountered :"+e);
		}
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
			grid.setColumns("id");
			 grid.addColumn(Scapes::getFaunaPopulation).setHeader("Element Type").setAutoWidth(true).setResizable(true).setSortable(true);
			floraColumn = grid.addColumn("floraOccupation").setHeader("Subtype").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("typeAgriOccupation").setAutoWidth(true).setHeader("Features & Approx Area").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("landscape").setAutoWidth(true).setHeader("Ownership").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("fallow").setHeader("General Fauna").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("forest").setHeader("General Fauna").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("wetLand").setHeader("Major Uses").setAutoWidth(true).setResizable(true).setSortable(true);
			//grid.addColumn("subLandscape").setHeader("Sub Landscape").setAutoWidth(true).setResizable(true).setSortable(true);;

			//grid.addColumn("ownerHouse").setHeader("Ownership").setAutoWidth(true).setResizable(true).setSortable(true);
			//grid.addColumn("features").setAutoWidth(true).setHeader("Features & Appox Area").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("userGroups").setAutoWidth(true).setHeader("User Groups").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("management").setHeader("Management Practices").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("generalResources").setHeader("General Uses").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("otherDetails").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("socialCommunity").setHeader("Community Accessed").setAutoWidth(true).setResizable(true).setSortable(true);
			//grid.addColumn("localLanguage").setHeader("Uses");
			//grid.addColumn("remarks").setHeader("Uses");
			grid.addColumn("associatedTk").setHeader("Associated TK").setAutoWidth(true).setResizable(true).setSortable(true);
			// grid.addColumn("knowledgeHolder").setHeader("Knowledge
			// Holder").setAutoWidth(true);
			grid.addColumn("latitude").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("longitude").setAutoWidth(true).setResizable(true).setSortable(true);
			// grid.addColumn(Scapes::getPastStatus);
			Grid.Column<Scapes> districtColumn = grid
					.addColumn(crop -> crop.getVillage() == null ? ""
							: crop.getVillage().getBlock().getDistrict().getDistrictName())
					.setHeader("District").setSortable(true).setAutoWidth(true).setResizable(true).setSortable(true);
			Grid.Column<Scapes> blockColumn = grid
					.addColumn(crop -> crop.getVillage() == null ? "" : crop.getVillage().getBlock().getBlockName())
					.setHeader("Block").setSortable(true).setAutoWidth(true).setResizable(true).setSortable(true);
			Grid.Column<Scapes> villageColumn = grid
					.addColumn(crop -> crop.getVillage() == null ? "" : crop.getVillage().getVillageName())
					.setHeader("Village").setSortable(true).setAutoWidth(true).setResizable(true).setSortable(true);

			// grid.addColumn(crop-> crop.getVillage()==null ?
			// "":crop.getVillage().getBlock().getBlockName()).setHeader("Block").setSortable(true);
			// grid.addColumn(crop-> crop.getVillage()==null ?
			// "":crop.getVillage().getVillageName()).setHeader("Village").setSortable(true);
			grid.addColumn("enteredBy").setAutoWidth(true).setResizable(true).setSortable(true);
			grid.addColumn("enteredOn").setAutoWidth(true).setResizable(true).setSortable(true);
			//grid.addColumn(crop -> crop.isApproved() ? "Yes" : "No").setHeader("Verified").setFlexGrow(0).setAutoWidth(true).setResizable(true).setSortable(true);
			grid.getHeaderRows().clear();
			HeaderRow headerRow = grid.appendHeaderRow();
			headerRow.getCell(districtColumn).setComponent(district);
			headerRow.getCell(blockColumn).setComponent(block);
			headerRow.getCell(villageColumn).setComponent(village);
			grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
			grid.asSingleSelect().addValueChangeListener(e -> editScape(e.getValue()));
			//grid.getColumnByKey("id").removeFromParent();
			return grid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateGrid() {
		try {
			List<Scapes> scape = sservice.getScapesByFormat(format);
			// crops.size();
			grid.setItems(scape);
			floraColumn.setFooter("Total : " + scape.size());
			grid.setSizeFull();
		} catch (Exception e) {
			floraColumn.setFooter("Total : 0");
			e.printStackTrace();
		}
	}

}
