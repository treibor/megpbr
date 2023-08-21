package com.application.megpbr.views.master;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.MasterLocallanguage;
import com.application.megpbr.data.entity.MasterCommercial;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.MasterManagementRegime;
import com.application.megpbr.data.entity.MasterSeason;
import com.application.megpbr.data.entity.MasterStatus;
import com.application.megpbr.data.entity.MasterWildhome;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;
import com.application.megpbr.data.entity.villages.VillageDetails;
import com.application.megpbr.data.service.CropService;
import com.application.megpbr.data.service.Dbservice;
import com.application.megpbr.views.CropPlantsForm.SaveEvent;
import com.application.megpbr.views.dashboard.DashboardView;
import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import elemental.json.Json;

public class VillagesForm extends Div {
	Dbservice dbservice;
	VillageDetails villagedetails;
	VillageView villageview;
	Binder<VillageDetails> binder = new BeanValidationBinder<>(VillageDetails.class);
	ComboBox<State> state = new ComboBox("");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox("");
	ComboBox<MasterManagementRegime> managementregime = new ComboBox("Management Regime");
	//TextField geographicArea = new TextField("Geographic Area(Ha)");
	BigDecimalField  geographicArea=new BigDecimalField("Geographic Area");
	TextField habitat = new TextField("Habitat & Topography)");
	//IntegerField
	IntegerField malePopn=new IntegerField("Male Population");
	IntegerField femalePopn=new IntegerField("Female Population");
	NumberField totalPopn=new NumberField("Total Population");
	TextField rainfall = new TextField("Rainfall (mm)");
	//BigDecimalField rainfall = new BigDecimalField("Rainfall(mm)");
	//BigDecimalField temperature = new BigDecimalField("Temperature(C)");
	TextField temperature = new TextField("Temperature (C)");
	TextField weatherPatterns = new TextField("Other Weather Patterns");
	BigDecimalField landUse = new BigDecimalField("Land Use(Ha)");
	BigDecimalField forest = new BigDecimalField("Forest Land(Ha)");
	BigDecimalField barrenArea = new BigDecimalField("Barren Land(Ha)");
	BigDecimalField nonagriArea = new BigDecimalField("Non Agricultural Use(Ha)");
	BigDecimalField pastureArea = new BigDecimalField("Pasture Area(Ha)");
	BigDecimalField miscArea = new BigDecimalField("Misc Land(Ha)");
	BigDecimalField wasteArea = new BigDecimalField("Waste Land(Ha)");
	BigDecimalField fallowArea = new BigDecimalField("Fallow Lands(Ha)");
	BigDecimalField currentFallowArea = new BigDecimalField("Current Fallow(Ha)");
	BigDecimalField sownArea = new BigDecimalField("Net Sown Area");
	DatePicker pbrDate=new DatePicker("PBR Date");
	FormLayout formbasic = new FormLayout();
	FormLayout frommaster = new FormLayout();
	Button save = new Button("Save");
	Button cancel = new Button("Close");
	Button delete = new Button("Delete");
	boolean isSuperAdmin;
	Checkbox approved = new Checkbox("Approved", true);
	MasterFormat format;
	MemoryBuffer buffer1 = new MemoryBuffer();
	MemoryBuffer buffer2 = new MemoryBuffer();
	MemoryBuffer buffer3 = new MemoryBuffer();
	MemoryBuffer buffer4 = new MemoryBuffer();
	HorizontalLayout imageLayout1=new HorizontalLayout();
	HorizontalLayout imageLayout2=new HorizontalLayout();
	HorizontalLayout imageLayout3=new HorizontalLayout();
	HorizontalLayout imageLayout4=new HorizontalLayout();
	Div imageContainer1=new Div();
	Div imageContainer2=new Div();
	Div imageContainer3=new Div();
	Div imageContainer4=new Div();
	Upload upload1 = new Upload(buffer1);
	Upload upload2= new Upload(buffer2);
	Upload upload3= new Upload(buffer3);
	Upload upload4= new Upload(buffer4);
	
	public VillagesForm(Dbservice dbservice) {
		super();
		this.setHeightFull();
		this.setWidth("60%");
		this.dbservice = dbservice;
		//this.cservice = cservice;
		initForm();
		binder.bindInstanceFields(this);
		initFields();
		add(createAccordion());
		isSuperAdmin = dbservice.isSuperAdmin();
	}

	public Component createAccordion() {
		VerticalLayout vl = new VerticalLayout();
		vl.add(createMasterForm(), createBasicForm(), createButtons());
		Scroller scroller = new Scroller(vl);
		scroller.setScrollDirection(ScrollDirection.VERTICAL);
		scroller.setSizeFull();
		return scroller;

	}

	
	private Component createButtons() {
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		cancel.addClickShortcut(Key.ESCAPE);
		cancel.addClickListener(e -> closeForm());
		save.addClickListener(e -> saveVillageDetails());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteVillageDetail(villagedetails));
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save, cancel, delete);
		hl.setSizeFull();
		return hl;
	}

	
	
	private void saveVillageDetails() {
		try {
			binder.writeBean(villagedetails);
			fireEvent(new SaveEvent(this, villagedetails));
			Village village=villagedetails.getVillage();
			village.setInUse(true);
			dbservice.updateVillage(village);
			this.setVillageDetails(new VillageDetails());
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			initFields();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void deleteVillageDetail(VillageDetails villagedetail) {
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setHeader("Delete??");
		dialog.setText("Are You sure you want to delete this item.");
		dialog.setCancelable(true);
		dialog.addCancelListener(event -> dialog.close());
		//dialog.setRejectable(true);
		//dialog.setRejectText("Cancel");
		dialog.addRejectListener(event -> dialog.close());
		dialog.setConfirmText("Delete");
		dialog.addConfirmListener(event -> fireEvent(new DeleteEvent(this, villagedetail)));
		dialog.open();
		
	}
	
	public void setVillageDetails(VillageDetails villagedetails) {
		this.villagedetails = villagedetails;
		binder.readBean(villagedetails);
	}

	public void closeForm() {
		this.setVisible(false);
	}

	private void clearForm(MasterFormat format) {
		this.villagedetails = new VillageDetails();
		//villagedetails.setFormat(format);
		
	}
	
	private void initForm() {
		//binder.setBean(new Crops());
	}

	public Component createMasterForm() {
		state.setVisible(isSuperAdmin);
		state.setPlaceholder("State");
		district.setPlaceholder("District");
		block.setPlaceholder("Block");
		village.setPlaceholder("Village");
		frommaster.add(state, 1);
		frommaster.add(district, 1);
		frommaster.add(block, 1);
		frommaster.add(village, 1);
		frommaster.setResponsiveSteps(new ResponsiveStep("0", 3),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("500px", 3));
		// frommaster.setSizeFull();
		return frommaster;
	}

	public Component createBasicForm() {
		formbasic.add(geographicArea, 2);
		formbasic.add(habitat, 2);
		formbasic.add(pbrDate, 2);
		formbasic.add(malePopn, 2);
		formbasic.add(femalePopn, 2);
		formbasic.add(totalPopn, 2);
		formbasic.add(rainfall, 2);
		formbasic.add(temperature, 2);
		formbasic.add(weatherPatterns, 2);
		formbasic.add(landUse, 2);
		formbasic.add(forest, 2);
		formbasic.add(barrenArea, 2);
		formbasic.add(nonagriArea, 2);
		formbasic.add(pastureArea, 2);
		formbasic.add(miscArea, 2);
		formbasic.add(wasteArea, 2);
		formbasic.add(fallowArea, 2);
		formbasic.add(currentFallowArea, 2);
		formbasic.add(sownArea, 2);
		formbasic.add(managementregime, 4);
		//formbasic.add(otherDetails, 3);
		//formbasic.add(associatedTdk, 3);
		formbasic.setResponsiveSteps(new ResponsiveStep("0", 6),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("500px", 6));
		// formbasic.setSizeFull();
		return formbasic;

	}

	

	public void initFields() {
		geographicArea.setTooltipText("Geographic Population in Ha");
		totalPopn.setReadOnly(true);
		state.setItems(dbservice.getStates());
		State selectedState = dbservice.getState();
		state.setValue(selectedState);
		state.addValueChangeListener(e -> district.setItems(dbservice.getDistricts(selectedState)));
		state.setItemLabelGenerator(state -> state.getStateName());
		district.setItems(dbservice.getDistricts(selectedState));
		district.setItemLabelGenerator(district -> district.getDistrictName());
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), false)));
		village.setItemLabelGenerator(Village::getVillageName);
		managementregime.setItems(dbservice.getMasterManagementRegime());
		managementregime.setItemLabelGenerator(managementregime-> managementregime.getManagementregime());
		//villagedetails.
		
	}
	public void initFields2() {
		geographicArea.setTooltipText("Geographic Population in Ha");
		totalPopn.setReadOnly(true);
		state.setItems(dbservice.getStates());
		State selectedState = dbservice.getState();
		state.setValue(selectedState);
		state.addValueChangeListener(e -> district.setItems(dbservice.getDistricts(selectedState)));
		state.setItemLabelGenerator(state -> state.getStateName());
		district.setItems(dbservice.getDistricts(selectedState));
		district.setItemLabelGenerator(district -> district.getDistrictName());
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), false)));
		village.setItemLabelGenerator(Village::getVillageName);
		//villagedetails.
		
	}
	
	
	

	public static abstract class VillageDetailsFormEvent extends ComponentEvent<VillagesForm> {
		private VillageDetails villagedetails;

		protected VillageDetailsFormEvent(VillagesForm source, VillageDetails villagedetails) {
			super(source, false);
			this.villagedetails = villagedetails;
		}

		public VillageDetails getVillageDetails() {
			return villagedetails;
		}
	}

	public static class SaveEvent extends VillageDetailsFormEvent {
		SaveEvent(VillagesForm source, VillageDetails villagedetails) {
			super(source, villagedetails);
		}
	}

	public static class DeleteEvent extends VillageDetailsFormEvent {
		DeleteEvent(VillagesForm source, VillageDetails villagedetails) {
			super(source, villagedetails);
		}

	}

	public static class CloseEvent extends VillageDetailsFormEvent {
		CloseEvent(VillagesForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
	
	
	
	
	
}
