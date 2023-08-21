package com.application.megpbr.views;



import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Markets;
import com.application.megpbr.data.entity.pbr.Scapes;
import com.application.megpbr.data.entity.pbr.Markets;
import com.application.megpbr.data.service.CropService;
import com.application.megpbr.data.service.Dbservice;
import com.application.megpbr.data.service.MarketsService;
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


@PageTitle("Markets||Domesticated Animals")
@Route(value = "marketsfordomesticatesanimals", layout = MainLayout.class)
@Uses(Icon.class)
public class MarketsView extends VerticalLayout{
	private Dbservice dbservice;
	private MarketsService cservice;
	Markets market;
	Grid<Markets> grid=new Grid<>(Markets.class);
	MarketsForm form;
	MasterFormat format;
	TextField filterText=new TextField("");
	Checkbox masterData=new Checkbox("Master Data");
	Checkbox villageData=new Checkbox("Village Data");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	Grid.Column<Markets> localColumn;
	public MarketsView(Dbservice service, MarketsService cservice) {
		this.dbservice=service;
		this.cservice=cservice;
		format=dbservice.getFormat(6);
		setSizeFull();
		ConfigureGrid();
		ConfigureForm();
		ConfigureCombo();
		updateGrid();
		add(getToolbar(),getContent());
	}
	
	private void ConfigureCombo() {
		// TODO Auto-generated method stub
		State state=dbservice.getState();
		district.setItems(dbservice.getDistricts(state));
		district.setItemLabelGenerator(district -> district.getDistrictName());
		district.setPlaceholder("District");
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		//district.addValueChangeListener(e -> updateList(filterText.getValue(), false));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.setPlaceholder("Block");
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), true)));
		village.setItems(dbservice.getVillages(block.getValue(), true));
		village.setItemLabelGenerator(Village::getVillageName);
		village.setPlaceholder("Village");
		village.addValueChangeListener(e->updateList());
		village.setEnabled(false);
	}

	private void ConfigureForm() {
		form= new MarketsForm(dbservice, cservice);
		form.setVisible(false);
		form.setWidth("50%");
		form.addListener(MarketsForm.SaveEvent.class, this::saveMarkets);
		form.addListener(MarketsForm.DeleteEvent.class, this::deleteMarkets);
		//form.addListener(CropPlantsForm.CloseEvent.class, e -> closeEditor());
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
		Button addButton=new Button("New");
		String tempformatName=format.getFormatName();
		var label=new H3("Format-"+format.getFormat()+" - "+tempformatName);
		addButton.addClickListener(e->addMarkets(new Markets()));
		HorizontalLayout topvl=new HorizontalLayout(filterText,masterData,villageData,addButton, label);
		topvl.setAlignItems(FlexComponent.Alignment.BASELINE);
		label.getStyle().set("margin-left", "auto");
		//label.getStyle().set("font-weight", "bold");
		label.getStyle().set("font-size", "12px");
		masterData.getStyle().set("font-size", "12px");
		villageData.getStyle().set("font-size", "12px");
		//label.getStyle().set("font-color", "hsla(120, 62%, 39%, 0.5)");
		topvl.setWidthFull();
		return topvl; 
	}
	
	
	public void updateList() {
		//System.out.println(grid.getPageSize());
		Village villageValue=village.getValue();
		String search =filterText.getValue();
		boolean masterCheck=masterData.getValue();
		boolean villageCheck=villageData.getValue();
		List<Markets> marketsData=null;
		if(masterCheck && villageCheck) {
			marketsData=cservice.searchMarketsFilter(villageValue, search, format);
			grid.setItems(marketsData);
			village.setEnabled(true);
			//localColumn.setFooter("Total :"+marketsData.size());
		}else if(!villageCheck && !masterCheck) {
			//System.out.println("Empty");
			grid.setItems(Collections.emptyList());
			village.setEnabled(false);
			//localColumn.setFooter("");
		}else if(!masterCheck && villageCheck) {
			marketsData=cservice.searchMarketsFilter(villageValue, search, format, masterCheck);
			grid.setItems(marketsData);
			//localColumn.setFooter("Total :"+marketsData.size());
			village.setEnabled(true);
		}else if( villageCheck) {
			village.setEnabled(true);
		}else if(masterCheck || !masterCheck) {
			marketsData=cservice.searchMarketsFilter(villageValue, search, format, masterCheck);
			grid.setItems(marketsData);
			//localColumn.setFooter("Total :"+marketsData.size());
			if(masterCheck) {
				village.setEnabled(false);
			}
		}
		if(marketsData==null) {
			localColumn.setFooter("Total : 0");
		}else {
			localColumn.setFooter("Total : "+marketsData.size());
		}
	}
	
	
	
	public void editMarkets(Markets market) {
		//form.format=format;
		//form.approved.setValue(true);
		form.initFields(format);
		form.scientificCheck.setEnabled(false);
		form.masterCheck.setEnabled(false);
		if (market == null) {
			form.setVisible(false);
		} else {
			if (market.getVillage() != null) {
				// System.out.println("Village Exist");
				form.frommaster.setVisible(true);
				form.masterCheck.setValue(false);
				form.district.setValue(market.getVillage().getBlock().getDistrict());
				form.block.setValue(market.getVillage().getBlock());
			} else {
				form.frommaster.setVisible(false);
				form.masterCheck.setValue(true);
			}
			form.setMarket(market);
			byte[] picture1 = market.getPhoto1();
			byte[] picture2 = market.getPhoto2();
			byte[] picture3 = market.getPhoto3();
			byte[] picture4 = market.getPhoto4();
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

	public void addMarkets(Markets market) {
		//form.format=format;
		form.initFields(format);
		grid.asSingleSelect().clear();
		form.setMarket(market);
		market.setFormat(format);
		form.removeAllImages();
		form.scientificCheck.setEnabled(true);
		form.masterCheck.setEnabled(true);
		form.setVisible(true);
	}
	public void saveMarkets(MarketsForm.SaveEvent event) {
		try {
			cservice.saveMarket(event.getMarkets());
			updateGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//Notification.show("Error Encountered :"+e);		
		}
	}
	
	public void deleteMarkets(MarketsForm.DeleteEvent event) {
		
		try {
			cservice.deleteMarket(event.getMarkets());
			updateGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :"+e);
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
			localColumn = grid.addColumn("name").setHeader("Market Name").setAutoWidth(true).setResizable(true);
			grid.addColumn("frequency").setHeader("Frequency").setAutoWidth(true).setResizable(true);
			grid.addColumn("animalType").setHeader("Types of Animals Bought & Sold").setAutoWidth(true).setResizable(true);
			grid.addColumn("transactions").setHeader("Type & Avg Transactions Per Day").setAutoWidth(true).setResizable(true);
			grid.addColumn("placesFrom").setHeader("Places From Which Animals Are Brought").setAutoWidth(true).setResizable(true);
			grid.addColumn("placesTo").setHeader("Places To Which Animals Are Sold").setAutoWidth(true).setResizable(true);
			grid.addColumn("fishLocation").setHeader("Name & Location of Fish Market").setAutoWidth(true).setResizable(true);
			grid.addColumn("fishType").setHeader("Types of Fishes Sold").setAutoWidth(true).setResizable(true);
			grid.addColumn("fishSource").setHeader("Sources of Fish").setAutoWidth(true).setResizable(true);
			grid.addColumn("latitude");
			grid.addColumn("longitude");
			Grid.Column<Markets> districtColumn = grid.addColumn(market -> market.getVillage() == null ? ""	: market.getVillage().getBlock().getDistrict().getDistrictName())
					.setHeader("District").setSortable(true).setAutoWidth(true).setResizable(true);
			Grid.Column<Markets> blockColumn = grid
					.addColumn(market -> market.getVillage() == null ? "" : market.getVillage().getBlock().getBlockName()).setHeader("Block").setSortable(true).setAutoWidth(true).setResizable(true);
			Grid.Column<Markets> villageColumn = grid.addColumn(market -> market.getVillage() == null ? "" : market.getVillage().getVillageName())
					.setHeader("Village").setSortable(true).setAutoWidth(true).setResizable(true);
			
			// grid.addColumn(Scapes::getPastStatus);
			grid.addColumn("enteredBy");
			grid.addColumn("enteredOn");
			//grid.addColumn(crop -> crop.isApproved() ? "Yes" : "No").setHeader("Verified").setFlexGrow(0);
			grid.getHeaderRows().clear();
			HeaderRow headerRow = grid.appendHeaderRow();
			headerRow.getCell(districtColumn).setComponent(district);
			headerRow.getCell(blockColumn).setComponent(block);
			headerRow.getCell(villageColumn).setComponent(village);
			grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
			grid.asSingleSelect().addValueChangeListener(e -> editMarkets(e.getValue()));
			//grid.getColumnByKey("id").removeFromParent();
			return grid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateGrid() {
		try {
			List<Markets> markets=cservice.getMarketsByFormat(format);
			//markets.size();
			grid.setItems(markets);
			localColumn.setFooter("Total : "+markets.size());
			grid.setSizeFull();
		} catch (Exception e) {
			localColumn.setFooter("Total : 0");
			e.printStackTrace();
		}
	}

}
