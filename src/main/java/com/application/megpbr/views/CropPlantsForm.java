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
import com.application.megpbr.data.entity.MasterCommercial;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.MasterSeason;
import com.application.megpbr.data.entity.MasterStatus;
import com.application.megpbr.data.entity.MasterWildhome;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
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
	ComboBox<MasterStatus> pastStatus = new ComboBox("Past Status");
	ComboBox<MasterStatus> presentStatus = new ComboBox("Present Status");
	ComboBox<MasterCommercial> commercial= new ComboBox("Commercial/Non Commercial");
	ComboBox<MasterWildhome> wildhome= new ComboBox("Wild/Home");
	ComboBox<String> habitat = new ComboBox("Landscape/Habitat");
	ComboBox<String> type = new ComboBox("Plant Type");
	ComboBox<String> scientificName = new ComboBox("Scientific Name");
	ComboBox<String> localName = new ComboBox("Local Name");
	ComboBox<String> source = new ComboBox("Plant/Seed Source");
	ComboBox<String> variety = new ComboBox("Variety");
	ComboBox<String> associatedTk = new ComboBox("Associated TK");
	ComboBox<String> knowledgeHolder = new ComboBox("Knowledge Holder");
	ComboBox<String> uses = new ComboBox("Uses");
	ComboBox<String> partsUsed = new ComboBox("Parts Used");
	TextField otherDetails = new TextField("Other Details");
	TextField specialFeatures = new TextField("Special Features");
	TextField area = new TextField("Approx Area Sown");
	MultiSelectComboBox<LocalLanguage> localLanguages = new MultiSelectComboBox("");
	MultiSelectComboBox<MasterSeason> fruitSeasons = new MultiSelectComboBox("");
	TextField fruitSeason = new TextField();
	TextField localLanguage = new TextField();
	FormLayout formbasic = new FormLayout();
	FormLayout frommaster = new FormLayout();
	MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
	Upload photo = new Upload(buffer);
	TextField latitude = new TextField("Latitude");
	TextField longitude = new TextField("Longitude");
	TextField management = new TextField("Management Options");
	TextField xfield1 = new TextField("");
	TextField xfield2 = new TextField("");
	Button save = new Button("Save");
	Button cancel = new Button("Close");
	Button delete = new Button("Delete");
	boolean isSuperAdmin;
	Checkbox approved = new Checkbox("Approved", true);
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
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		cancel.addClickShortcut(Key.ESCAPE);
		cancel.addClickListener(e -> closeForm());
		save.addClickListener(e -> validateandsaveCrop());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteCrop(crops));
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
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
		if (crop == null || crop.equals(null) || crop.getScientificName()==null) {
			//dialog.open();
		} else {
			
			dialog.setHeader("Delete??");
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
		associatedTk.setValue("");
		knowledgeHolder.setValue("");
		otherDetails.setValue("");
		localName.setValue("");
		pastStatus.setValue(null);
		presentStatus.setValue(null);
		commercial.setValue(null);
		wildhome.setValue(null);
		type.setValue("");
		variety.setValue("");
		uses.setValue("");
		source.setValue("");
		specialFeatures.setValue("");
		localLanguage.setValue("");
		fruitSeason.setValue("");
		partsUsed.setValue("");
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
			associatedTk.setValue(crops.getAssociatedTk());
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
			partsUsed.setValue(crops.getPartsUsed());
			management.setValue(crops.getManagement());
			xfield1.setValue(crops.getXfield1());
			xfield2.setValue(crops.getXfield2());
			
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
		formbasic.add(variety, 3);
		formbasic.add(source, 3);
		formbasic.add(specialFeatures, 3);
		formbasic.add(otherDetails, 3);
		formbasic.add(commercial, 3);
		formbasic.add(uses, 3);
		formbasic.add(area, 3);
		formbasic.add(partsUsed, 3);
		formbasic.add(management, 3);
		formbasic.add(commercial, 3);
		formbasic.add(wildhome, 3);
		formbasic.add(xfield1, 3);
		formbasic.add(xfield2, 3);
		//formbasic.add(otherDetails, 3);
		//formbasic.add(associatedTdk, 3);
		formbasic.setResponsiveSteps(new ResponsiveStep("0", 6),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("500px", 6));
		// formbasic.setSizeFull();
		return formbasic;

	}

	public Component createCommonForm() {
		FormLayout formcommon = new FormLayout();
		formcommon.add(associatedTk, 1);
		formcommon.add(knowledgeHolder, 1);
		formcommon.add(otherDetails, 1);
		formcommon.add(latitude, 1);
		formcommon.add(longitude, 1);
		formcommon.add(approved, 1);
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
		commercial.setVisible(false);
		wildhome.setVisible(false);
		partsUsed.setVisible(false);
		area.setVisible(false);
		specialFeatures.setVisible(false);
		management.setVisible(false);
		xfield1.setVisible(false);
		xfield2.setVisible(false);
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
		commercial.setItems(dbservice.getMasterCommercial());
		wildhome.setItems(dbservice.getMasterWildhome());
		pastStatus.setItems(dbservice.getStatus());
		presentStatus.setItems(dbservice.getStatus());
		pastStatus.setItemLabelGenerator(status -> status.getStatus());
		presentStatus.setItemLabelGenerator(status -> status.getStatus());
		fruitSeasons.setItems(dbservice.getMasterFruitSeason());
		fruitSeasons.setItemLabelGenerator(fruitSeason -> fruitSeason.getFruitseason());
		fruitSeason.setReadOnly(true);
		fruitSeasons.addValueChangeListener(e -> {
			String selectedSeason = e.getValue().stream().map(MasterSeason::getFruitseason)
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
		localName.setAllowCustomValue(true);
		localName.addCustomValueSetListener(e -> {
			String customLocal = e.getDetail();
			localName.setItems(customLocal);
			localName.setValue(customLocal);
		});
		variety.setAllowCustomValue(true);
		variety.addCustomValueSetListener(e -> {
			String customVariety = e.getDetail();
			variety.setItems(customVariety);
			variety.setValue(customVariety);
		});
		source.setAllowCustomValue(true);
		source.addCustomValueSetListener(e -> {
			String customsource = e.getDetail();
			source.setItems(customsource);
			source.setValue(customsource);
		});
		associatedTk.setAllowCustomValue(true);
		associatedTk.addCustomValueSetListener(e -> {
			String customassociatedTk = e.getDetail();
			associatedTk.setItems(customassociatedTk);
			associatedTk.setValue(customassociatedTk);
		});
		knowledgeHolder.setAllowCustomValue(true);
		knowledgeHolder.addCustomValueSetListener(e -> {
			String customknowledgeHolder = e.getDetail();
			knowledgeHolder.setItems(customknowledgeHolder);
			knowledgeHolder.setValue(customknowledgeHolder);
		});
		uses.setAllowCustomValue(true);
		uses.addCustomValueSetListener(e -> {
			String customuses = e.getDetail();
			uses.setItems(customuses);
			uses.setValue(customuses);
		});
		partsUsed.setAllowCustomValue(true);
		partsUsed.addCustomValueSetListener(e -> {
			String custompartsUsed = e.getDetail();
			partsUsed.setItems(custompartsUsed);
			partsUsed.setValue(custompartsUsed);
		});
		initMasterFields(format);
		initFormatFields(format);
	}
	
	
	
	public void initMasterFields(MasterFormat format) {
		//System.out.println(format);
		List<String> snames=cservice.findScientificNamesAsString(format);
		List<String> lnames=cservice.findLocalNamesAsString(format);
		List<String> types=cservice.findtypesAsString(format);
		List<String> habitats=cservice.findHabitatAsString(format);
		List<String> varietys=cservice.findVarietyAsString(format);
		List<String> sources=cservice.findSourcesAsString(format);
		List<String> assoctk=cservice.findAssociatedtkAsString(format);
		List<String> knowledge=cservice.findKnowledgeHolderAsString(format);
		List<String> usess=cservice.findUsesAsString(format);
		List<String> usedparts=cservice.findPartsUsedAsString(format);
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
		localName.setItems(lnames);
		type.setItems(types);
		habitat.setItems(habitats);
		variety.setItems(varietys);
		source.setItems(sources);
		associatedTk.setItems(assoctk);
		knowledgeHolder.setItems(knowledge);
		uses.setItems(usess);
		partsUsed.setItems(usedparts);
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
	
	
	
	public void initFormatFields(MasterFormat format) {
		int formatNo=format.getFormat();
		switch (formatNo) {
		case 1:
			area.setVisible(true);
			break;
		case 2:
			fruitSeasons.setPlaceholder("Season of Fruiting");
			break;
		case 3:
			variety.setVisible(false);
			specialFeatures.setVisible(true);
			partsUsed.setVisible(true);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			uses.setVisible(false);
			break;
			//System.out.println(formatNo);
		case 4:
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			variety.setVisible(false);
			source.setVisible(false);
			management.setVisible(true);
			xfield1.setVisible(true);
			xfield2.setVisible(true);
			xfield1.setLabel("Affected Crops");
			xfield2.setLabel("Impact");
			break;
		case 5:
			variety.setVisible(false);
			pastStatus.setVisible(false);
			presentStatus.setVisible(false);
			fruitSeasons.setPlaceholder("Season of Attack");
			source.setVisible(false);
			uses.setVisible(false);
			management.setVisible(true);
			management.setLabel("Management Mechanism");
			xfield1.setVisible(true);
			xfield2.setVisible(true);
			xfield1.setLabel("Hosts");
			xfield2.setLabel("Insect/Animal");
			break;
		case 11:
			fruitSeasons.setPlaceholder("Season of Fruiting");
			break;
		case 12:
			fruitSeasons.setPlaceholder("Season of Fruiting");
			break;
		case 13:
			pastStatus.setVisible(false);
			presentStatus.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			commercial.setVisible(true);
			break;
		case 14:
			variety.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);	
			area.setVisible(true);
			wildhome.setVisible(true);
			break;
		case 15:
			variety.setVisible(false);
			habitat.setVisible(false);
			specialFeatures.setVisible(true);
			specialFeatures.setLabel("Features");
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			xfield1.setVisible(true);
			xfield2.setVisible(true);
			xfield1.setLabel("Breed");
			xfield2.setLabel("Method of Keeping");
			type.setValue("Animal Type");
			break;
		case 16:
			habitat.setLabel("Waterscape");
			specialFeatures.setVisible(true);
			specialFeatures.setLabel("Features");
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			type.setValue("Fish Type");
			break;
		case 18:
			variety.setVisible(false);
			partsUsed.setVisible(true);
			partsUsed.setLabel("Parts Collected");
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			uses.setVisible(false);
			commercial.setVisible(true);
			break;
		case 19:
			habitat.setVisible(false);
			type.setVisible(false);
			pastStatus.setVisible(false);
			presentStatus.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			otherDetails.setVisible(false);
			uses.setVisible(false);
			break;
		case 20:
			type.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			specialFeatures.setVisible(true);
			specialFeatures.setLabel("Features");
			source.setVisible(false);
			break;
		case 21:
			type.setVisible(false);
			pastStatus.setVisible(false);
			presentStatus.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			otherDetails.setVisible(false);
			uses.setVisible(false);
			break;
		case 22:
			type.setLabel("Plant");
			fruitSeason.setVisible(false);
			fruitSeasons.setVisible(false);
			source.setVisible(false);
			xfield1.setLabel("Habit");
			xfield1.setVisible(true);
			break;
		case 23:
			type.setLabel("Associated Crop");
			variety.setVisible(false);
			fruitSeason.setVisible(false);
			fruitSeasons.setVisible(false);
			otherDetails.setLabel("Other Details(Mode of Use)");
			source.setVisible(false);
			uses.setLabel("Uses(Usage)");
			break;
		case 24:
			pastStatus.setVisible(false);
			presentStatus.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			uses.setVisible(false);
			break;
		case 25:
			type.setLabel("Plant");
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			uses.setVisible(false);
			break;
		case 26:
			type.setLabel("Plant Type");
			variety.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			uses.setLabel("Other Uses(if any)");
			break;
		case 27:
			type.setLabel("Animal Type");
			variety.setVisible(false);
			fruitSeasons.setPlaceholder("Season When Seen");
			source.setVisible(false);
			xfield1.setLabel("Mode of Hunting");
			break;
		
		}
		
	}
}
