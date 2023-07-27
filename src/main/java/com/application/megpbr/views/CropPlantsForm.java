package com.application.megpbr.views;

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

import org.springframework.beans.factory.annotation.Autowired;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.LocalLanguage;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.MasterFruitSeason;
import com.application.megpbr.data.entity.MasterStatus;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;
import com.application.megpbr.data.service.CropService;
import com.application.megpbr.data.service.Dbservice;
import com.application.megpbr.views.dashboard.DashboardView;
import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.shared.Registration;

public class CropPlantsForm extends Div {
	Dbservice dbservice;
	CropService cservice;
	Crops crops;
	MasterStatus status;
	CropPlantsView cropsview;
	Binder<Crops> binder = new BeanValidationBinder<>(Crops.class);
	Checkbox masterCheck = new Checkbox("Enter Master Data");
	Checkbox scientificCheck = new Checkbox("Autofill");
	Checkbox localCheck = new Checkbox("Autofill");
	ComboBox<State> state = new ComboBox("");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	ComboBox<String> type = new ComboBox("Type");
	MultiSelectComboBox<LocalLanguage> localLanguages = new MultiSelectComboBox("");
	TextField localLanguage = new TextField();
	ComboBox<String> scientificName = new ComboBox("Scientific Name");
	TextField localName = new TextField("Local Name");
	TextField variety = new TextField("Variety");
	TextField specialFeatures = new TextField("Special Features");
	TextField area = new TextField("Approx Area Sown");
	TextField source = new TextField("Plant/Seed Source");
	ComboBox<MasterStatus> pastStatus = new ComboBox("Past Status");
	ComboBox<MasterStatus> presentStatus = new ComboBox("Present Status");
	TextField associatedTdk = new TextField("Associated TK");
	TextField commercial = new TextField("Commercial");
	TextField otherDetails = new TextField("Other Details");
	MultiSelectComboBox<MasterFruitSeason> fruitSeasons = new MultiSelectComboBox("");
	TextField fruitSeason = new TextField();
	// ComboBox<String> fruitSeason = new ComboBox("Fruit Season");
	ComboBox<String> habitat = new ComboBox("Landscape/Habitat");
	Button save = new Button("Save");
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete");
	TextField uses = new TextField("Uses");
	TextField knowledgeHolder = new TextField("Knowledge Holder");
	FormLayout formbasic = new FormLayout();
	FormLayout frommaster = new FormLayout();
	MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
	//Upload upload = new Upload(buffer);
	Upload photo = new Upload(buffer);
	TextField latitude = new TextField("Latitude");
	TextField longitude = new TextField("Longitude");
	boolean isSuperAdmin;
	MasterFormat format;
	public CropPlantsForm(Dbservice dbservice, CropService cservice) {
		super();
		this.setHeightFull();
		this.dbservice = dbservice;
		this.cservice = cservice;
		addClassName("cropplants-view");
		initForm();
		binder.bindInstanceFields(this);
		//initFields();
		add(createAccordion());
		isSuperAdmin = dbservice.isSuperAdmin();
	}

	public Component createAccordion() {
		Accordion accordion = new Accordion();
		AccordionPanel topaccordion = new AccordionPanel();
		AccordionPanel bottomaccordion = new AccordionPanel();
		topaccordion = accordion.add("Basic Info",
				new VerticalLayout(createCheckPanel(), createMasterForm(), createBasicForm()));
		bottomaccordion = accordion.add("Other Info", new VerticalLayout(createCommonForm()));
		accordion.setSizeFull();
		masterCheck.addClickListener(e -> hideFields());
		VerticalLayout vl = new VerticalLayout();
		vl.add(accordion, createButtons());
		Scroller scroller = new Scroller(vl);
		scroller.setScrollDirection(ScrollDirection.VERTICAL);
		scroller.setSizeFull();
		return scroller;
	}

	private Component createCheckPanel() {
		HorizontalLayout vl = new HorizontalLayout();
		vl.setSizeFull();
		masterCheck.addClickListener(e -> hideFields());
		scientificCheck.addClickListener(e -> populateForm());
		scientificName.addValueChangeListener(e -> populateForm());
		vl.add(masterCheck, scientificCheck);

		return vl;
	}

	private Component createButtons() {
		HorizontalLayout hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		cancel.addClickShortcut(Key.ESCAPE);
		cancel.addClickListener(e -> closeForm());
		save.addClickListener(e -> validateandsaveCrop());
		delete.addClickListener(e -> deleteCrop(crops));
		hl.add(save, cancel, delete);
		hl.setSizeFull();
		return hl;
	}

	private void validateandsaveCrop() {
		try {
			format=crops.getFormat();
			if (masterCheck.getValue()) {
				binder.writeBean(crops);
				//crops.setVillage(null);
				crops.setMaster(true);
				fireEvent(new SaveEvent(this, crops));
				clearForm(format);
				Notification.show("Saved Successfully");
				initMasterFields(format);
				removeFields();
			} else {
				if (village.getValue() == null || village.getValue().equals(null)) {
					Notification.show("Please Select The Village");
				} else {
					binder.writeBean(crops);
					crops.setMaster(false);
					//crops.setVillagedetails(village.getValue());
					fireEvent(new SaveEvent(this, crops));
					clearForm(format);
					Notification.show("Saved Successfully");
					
					removeFields();
					initMasterFields(format);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Notification.show("Error: " + e);
		}

	}
	
	public void deleteCrop(Crops crop) {
		ConfirmDialog dialog = new ConfirmDialog();
		if (crop == null) {
			dialog.open();
		} else {
			dialog.setHeader("Delete?");
			dialog.setText("Are You sure you want to delete this item.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.setRejectable(true);
			dialog.setRejectText("Discard");
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event ->fireEvent(new DeleteEvent(this, crop)));
			dialog.open();
		}
	}
	public void setCrop(Crops crop) {
		this.crops = crop;
		binder.readBean(crop);
	}

	public void closeForm() {
		this.setVisible(false);
	}

	private void clearForm(MasterFormat format) {
		this.crops = new Crops();
		crops.setFormat(format);
		
	}
	
	private void initForm() {
		binder.setBean(new Crops());
	}

	private void removeFields() {
		habitat.setValue("");
		area.setValue("");
		associatedTdk.setValue("");
		knowledgeHolder.setValue("");
		otherDetails.setValue("");
		localName.setValue("");
		pastStatus.setValue(null);
		presentStatus.setValue(null);
		commercial.setValue("");
		type.setValue("");
		variety.setValue("");
		uses.setValue("");
		source.setValue("");
		specialFeatures.setValue("");
		localLanguage.setValue("");
		fruitSeason.setValue("");
		localLanguages.setValue();
		fruitSeasons.setValue();
	}

	private void populateForm() {
		// TODO Auto-generated method stub
		if (scientificName.getValue() != null ) {
			if (scientificCheck.getValue()) {
				Crops crops = cservice.findCropBySientificName(scientificName.getValue());
				if (crops != null) {
					populateFields(crops);
				}
			} else {
				if (scientificCheck.isEnabled()) {
					removeFields();
				}
			}
		} else {
			//Notification.show("Please Enter the Scientific Name");
		}
	}

	private void populateFields(Crops crops) {
		try {
			habitat.setValue(crops.getHabitat());
			type.setValue(crops.getType());
			area.setValue(crops.getArea());
			associatedTdk.setValue(crops.getAssociatedTdk());
			otherDetails.setValue(crops.getOtherDetails());
			knowledgeHolder.setValue(crops.getKnowledgeHolder());
			localName.setValue(crops.getLocalName());
			pastStatus.setValue(crops.getPastStatus());
			presentStatus.setValue(crops.getPresentStatus());
			commercial.setValue(crops.getCommercial());
			variety.setValue(crops.getVariety());
			source.setValue(crops.getSource());
			localLanguage.setValue(crops.getLocalLanguage());
			specialFeatures.setValue(crops.getSpecialFeatures());
			fruitSeason.setValue(crops.getFruitSeason());
			uses.setValue(crops.getUses());
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

	}

	private void hideFields() {
		if (masterCheck.getValue()) {
			// state.setVisible(false);
			frommaster.setVisible(false);
			latitude.setVisible(false);
			longitude.setVisible(false);
		} else {
			// System.out.println("Not");
			frommaster.setVisible(true);
			latitude.setVisible(true);
			longitude.setVisible(true);
		}
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
		//scientificName.setPlaceholder("Scientific Name");
		formbasic.add(scientificName, 3);
		// formbasic.add(scientificCheck, 3);
		localLanguages.setPlaceholder("Local Language");
		fruitSeasons.setPlaceholder("Cropping Season");
		formbasic.add(type, 3);
		formbasic.add(habitat, 3);
		formbasic.add(localName, 3);
		formbasic.add(localLanguages, 3);
		formbasic.add(localLanguage, 3);
		formbasic.add(fruitSeasons, 3);
		formbasic.add(fruitSeason, 3);
		formbasic.add(pastStatus, 3);
		formbasic.add(presentStatus, 3);
		formbasic.add(source, 3);
		formbasic.add(specialFeatures, 3);
		formbasic.add(otherDetails, 3);
		
		formbasic.add(commercial, 3);
		formbasic.add(variety, 3);
		formbasic.add(area, 3);
		//formbasic.add(otherDetails, 3);
		//formbasic.add(associatedTdk, 3);
		formbasic.setResponsiveSteps(new ResponsiveStep("0", 6),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("500px", 6));
		// formbasic.setSizeFull();
		return formbasic;

	}

	public Component createCommonForm() {
		//photo.setT
		
		
		FormLayout formcommon = new FormLayout();
		formcommon.add(uses, 1);
		formcommon.add(associatedTdk, 1);
		formcommon.add(otherDetails, 1);
		formcommon.add(knowledgeHolder, 1);
		formcommon.add(latitude, 1);
		formcommon.add(longitude, 1);
		formcommon.add(photo, 2);
		formcommon.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("500px", 2));
		photo.addSucceededListener(event -> {
		    String fileName = event.getFileName();
		    InputStream inputStream = buffer.getInputStream(fileName);
		    formcommon.add(new TextField("Source"));
		    // Do something with the file data
		    // processFile(inputStream, fileName);
		});
		
		return formcommon;
	}

	public void initFields(MasterFormat format) {
		state.setItems(dbservice.getStates());
		State selectedState = dbservice.getState();
		state.setValue(selectedState);
		state.addValueChangeListener(e -> district.setItems(dbservice.getDistricts(selectedState)));
		state.setItemLabelGenerator(state -> state.getStateName());
		district.setItems(dbservice.getDistricts(selectedState));
		district.setItemLabelGenerator(district -> district.getDistrictName());
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), true)));
		village.setItemLabelGenerator(Village::getVillageName);
		//villagedetails.
		pastStatus.setItems(dbservice.getStatus());
		presentStatus.setItems(dbservice.getStatus());
		pastStatus.setItemLabelGenerator(status -> status.getStatus());
		presentStatus.setItemLabelGenerator(status -> status.getStatus());
		fruitSeasons.setItems(dbservice.getMasterFruitSeason());
		fruitSeasons.setItemLabelGenerator(fruitSeason -> fruitSeason.getFruitseason());
		fruitSeason.setReadOnly(true);
		fruitSeasons.addValueChangeListener(e -> {
			String selectedSeason = e.getValue().stream().map(MasterFruitSeason::getFruitseason)
					.collect(Collectors.joining(", "));
			fruitSeason.setValue(selectedSeason);
		});
		//fruitSeason.setVisible(false);
		localLanguages.setItems(dbservice.getLocalLanguage());
		localLanguages.setItemLabelGenerator(localLanguages -> localLanguages.getLanguageName());
		localLanguage.setReadOnly(true);
		localLanguages.addValueChangeListener(e -> {
			String selectedLanguage = e.getValue().stream().map(LocalLanguage::getLanguageName)
					.collect(Collectors.joining(", "));
			localLanguage.setValue(selectedLanguage);
		});
		//localLanguage.setVisible(false);
		commercial.setVisible(false);

		scientificName.setAllowCustomValue(true);
		scientificName.addCustomValueSetListener(e -> {
			String customValue = e.getDetail();
			scientificName.setItems(customValue);
			scientificName.setValue(customValue);
		});
		type.addCustomValueSetListener(e -> {
			String customName = e.getDetail();
			type.setItems(customName);
			type.setValue(customName);
		});
		habitat.addCustomValueSetListener(e -> {
			String customHabitat = e.getDetail();
			habitat.setItems(customHabitat);
			habitat.setValue(customHabitat);
		});
		initMasterFields(format);
	}

	public void initMasterFields(MasterFormat format) {
		//System.out.println(format);
		List<String> snames=cservice.findScientificNamesAsString(format);
		List<String> types=cservice.findtypesAsString(format);
		List<String> habitats=cservice.findHabitatAsString(format);
		/*
		 * List<Crops> scrops = cservice.findCropsByFormat(format); List<String> snames
		 * = scrops.stream().map(crops -> crops.getScientificName()).toList();
		 * List<String> stypes = scrops.stream().map(crops ->
		 * crops.getType()).filter(crops -> crops != null).toList(); List<Crops> hcrops
		 * = cservice.findCropsOrderByHabitat(); List<String> shabitat =
		 * hcrops.stream().map(crops -> crops.getHabitat()).filter(crops -> crops !=
		 * null).toList();
		 */
		scientificName.setItems(snames);
		type.setItems(types);
		habitat.setItems(habitats);
	}

	public static abstract class CropPlantsFormEvent extends ComponentEvent<CropPlantsForm> {
		private Crops crop;

		protected CropPlantsFormEvent(CropPlantsForm source, Crops crop) {
			super(source, false);
			this.crop = crop;
		}

		public Crops getCrops() {
			return crop;
		}
	}

	public static class SaveEvent extends CropPlantsFormEvent {
		SaveEvent(CropPlantsForm source, Crops crop) {
			super(source, crop);
		}
	}

	public static class DeleteEvent extends CropPlantsFormEvent {
		DeleteEvent(CropPlantsForm source, Crops crop) {
			super(source, crop);
		}

	}

	public static class CloseEvent extends CropPlantsFormEvent {
		CloseEvent(CropPlantsForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
