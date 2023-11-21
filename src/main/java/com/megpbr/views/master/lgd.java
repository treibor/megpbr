package com.megpbr.views.master;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.CrowdCategory;
import com.megpbr.data.entity.CrowdFormat;
import com.megpbr.data.entity.CrowdType;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterCategory;
import com.megpbr.data.entity.MasterCommercial;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.MasterLocallanguage;
import com.megpbr.data.entity.MasterManagementRegime;
import com.megpbr.data.entity.MasterPosition;
import com.megpbr.data.entity.MasterSeason;
import com.megpbr.data.entity.MasterStatus;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Crowd;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.CrowdService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.MasterService;
import com.megpbr.data.service.UserService;
import com.megpbr.views.CropPlantsForm.DeleteEvent;
import com.megpbr.views.CropPlantsForm;
import com.megpbr.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"SUPERADMIN"})
@PageTitle("lgd")
@Route(value = "lgd", layout = MainLayout.class)
public class lgd extends HorizontalLayout{
	MasterService service;
	Dbservice dbservice;
	UserService uservice;
	AuditTrail audittrail;
	Grid<State> stategrid=new Grid<>(State.class);
	Grid<District> districtgrid=new Grid<>(District.class);
	Grid<Block> blockgrid=new Grid<>(Block.class);
	Grid<Village> villagegrid=new Grid<>(Village.class);
	//ComboBox<State> state = new ComboBox("");
	//ComboBox<District> district = new ComboBox("");
	//ComboBox<Block> block = new ComboBox("");
	//ComboBox<Village> village = new ComboBox();
	TextField localName=new TextField("Local Name");
	TextField localLanguage=new TextField("Local Language");
	StateForm stateform;
	DistrictForm distform;
	BlockForm blockform;
	VillageForm villageform;
	LocalLanguageForm languageform;
	@Autowired
	private Audit auditobject;
	public lgd(MasterService service, Dbservice dbservice, UserService uservice) {
		this.service=service;
		this.dbservice=dbservice;
		this.uservice=uservice;
		configureGrids();
		ConfigureForm();
		setSizeFull();
		add(getGrids());
	}
	private void ConfigureForm() {
		stateform = new StateForm(dbservice);
		stateform.setVisible(false);
		stateform.setWidth("50%");
		stateform.addListener(StateForm.SaveEvent.class, this::saveState);
		stateform.addListener(StateForm.DeleteEvent.class, this::deleteState);
		distform = new DistrictForm(dbservice);
		distform.setVisible(false);
		distform.setWidth("50%");
		distform.addListener(DistrictForm.SaveEvent.class, this::saveDistrict);
		distform.addListener(DistrictForm.DeleteEvent.class, this::deleteDistrict);
		blockform = new BlockForm(dbservice);
		blockform.setVisible(false);
		blockform.setWidth("50%");
		blockform.addListener(BlockForm.SaveEvent.class, this::saveBlock);
		blockform.addListener(BlockForm.DeleteEvent.class, this::deleteBlock);
		villageform = new VillageForm(dbservice);
		villageform.setVisible(false);
		villageform.setWidth("50%");
		villageform.addListener(VillageForm.SaveEvent.class, this::saveVillage);
		villageform.addListener(VillageForm.DeleteEvent.class, this::deleteVillage);
	}
	private boolean isStateAdmin() {
		String userLevel=uservice.getLoggedUserLevel();
		if(userLevel.startsWith("STATE") && userLevel.endsWith("ADMIN")) {
			return true;
		}else if(userLevel.startsWith("SUPER") && userLevel.endsWith("ADMIN")){
			return true;
		}else {
			return false;
		}
	}
	public void saveState(StateForm.SaveEvent event) {

		try {
			State state = event.getState();
			dbservice.saveState(state);
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshStateGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Unable to Update+"+e).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}
	public void deleteState(StateForm.DeleteEvent event) {
		try {
			State state=event.getState();
			dbservice.deleteState(state);
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshStateGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Unable To Delete"+e).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	public void saveDistrict(DistrictForm.SaveEvent event) {

		try {
			District dist = event.getDistrict();
			dbservice.saveDistrict(dist);
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshDistrictGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Unable to Update+"+e).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}
	public void deleteDistrict(DistrictForm.DeleteEvent event) {
		try {
			District dist=event.getDistrict();
			dbservice.deleteDistrict(dist);
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshDistrictGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Unable To Delete"+e).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	public void saveBlock(BlockForm.SaveEvent event) {

		try {
			Block dist = event.getBlock();
			dbservice.saveBlock(dist);
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshBlockGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Unable to Update+"+e).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}
	public void deleteBlock(BlockForm.DeleteEvent event) {
		try {
			Block dist=event.getBlock();
			dbservice.deleteBlock(dist);
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshBlockGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Unable To Delete"+e).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	public void saveVillage(VillageForm.SaveEvent event) {

		try {
			Village dist = event.getVillage();
			dbservice.saveVillage(dist);
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshVillageGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Unable to Update+"+e).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}
	public void deleteVillage(VillageForm.DeleteEvent event) {
		try {
			Village dist=event.getVillage();
			dbservice.deleteVillage(dist);
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshVillageGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Unable To Delete"+e).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	private Component getGrids() {
		var mainlayout=new HorizontalLayout();
		mainlayout.add(stategrid, districtgrid, blockgrid, villagegrid, stateform, distform, blockform, villageform);
		mainlayout.setSizeFull();
		return mainlayout;
	}
	public void configureGrids() {
		configureStateGrid();
	}
	public void configureStateGrid() {
		stategrid.setSizeFull();
		stategrid.removeAllColumns();
		stategrid.addColumn("stateCode").setAutoWidth(true).setResizable(true).setSortable(true);
		stategrid.addColumn("stateName").setAutoWidth(true).setResizable(true).setSortable(true);
		stategrid.setItems(dbservice.getStates());
		stategrid.asSingleSelect().addValueChangeListener(e -> configureDistrictGrid(e.getValue()));
	}
	public void configureDistrictGrid(State state) {
		if (state != null) {
			stateform.setState(state);
			stateform.setVisible(true);
			distform.setVisible(false);
			blockform.setVisible(false);
			villageform.setVisible(false);
		}else {
			stateform.setVisible(false);
		}
		districtgrid.setSizeFull();
		districtgrid.removeAllColumns();
		districtgrid.addColumn("districtCode").setAutoWidth(true).setResizable(true).setSortable(true);
		districtgrid.addColumn("districtName").setAutoWidth(true).setResizable(true).setSortable(true);
		districtgrid.setItems(dbservice.getDistricts(state));
		districtgrid.asSingleSelect().addValueChangeListener(e -> configureBlockGrid(e.getValue()));
	}
	public void configureBlockGrid(District district) {
		if(district!=null) {
			distform.setDistrict(district);
			distform.setVisible(true);
			stateform.setVisible(false);
			blockform.setVisible(false);
			villageform.setVisible(false);
		}else {
			
			distform.setVisible(false);
		}
		blockgrid.setSizeFull();
		blockgrid.removeAllColumns();
		blockgrid.addColumn("blockCode").setAutoWidth(true).setResizable(true).setSortable(true);
		blockgrid.addColumn("blockName").setAutoWidth(true).setResizable(true).setSortable(true);
		blockgrid.setItems(dbservice.getBlocks(district));
		blockgrid.asSingleSelect().addValueChangeListener(e -> configureVillageGrid(e.getValue()));
	}
	public void configureVillageGrid(Block block) {
		if(block!=null) {
			//blockform.set
			blockform.setBlock(block);
			blockform.state.setValue(block.getDistrict().getState());
			blockform.setVisible(true);
			stateform.setVisible(false);
			distform.setVisible(false);
			villageform.setVisible(false);
		}else {
			
			blockform.setVisible(false);
		}
		villagegrid.setSizeFull();
		villagegrid.removeAllColumns();
		villagegrid.addColumn("villageCode").setAutoWidth(true).setResizable(true).setSortable(true);
		villagegrid.addColumn("villageName").setAutoWidth(true).setResizable(true).setSortable(true);
		villagegrid.setItems(dbservice.getVillages(block));
		villagegrid.asSingleSelect().addValueChangeListener(e -> getVillageDetails(e.getValue()));
	}
	private void getVillageDetails(Village village) {
		if(village!=null) {
			villageform.setVillage(village);
			villageform.state.setValue(village.getBlock().getDistrict().getState());
			villageform.district.setValue(village.getBlock().getDistrict());
			villageform.block.setValue(village.getBlock());
			villageform.setVisible(true);
			stateform.setVisible(false);
			distform.setVisible(false);
			blockform.setVisible(false);
		}
	}
	public void refreshStateGrid() {
		stategrid.setItems(dbservice.getStates());
	}
	public void refreshDistrictGrid() {
		districtgrid.setItems(dbservice.getDistricts());
	}
	public void refreshBlockGrid() {
		blockgrid.setItems(dbservice.getBlocks());
	}
	public void refreshVillageGrid() {
		villagegrid.setItems(dbservice.getVillages());
	}
}
