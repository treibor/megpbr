package com.megpbr.views;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterApproval;
import com.megpbr.data.entity.MasterCommercial;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.MasterLocallanguage;
import com.megpbr.data.entity.MasterSeason;
import com.megpbr.data.entity.MasterStatus;
import com.megpbr.data.entity.MasterWildhome;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.views.agrobiodiversity.CropPlantsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
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
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import elemental.json.Json;

public class CropPlantsForm extends Div {
	/**
	 * 
	 */
	
	Dbservice dbservice;
	CropService cservice;
	Crops crops;
	MasterStatus status;
	CropPlantsView cropsview;
	Binder<Crops> binder = new BeanValidationBinder<>(Crops.class);
	public Checkbox masterCheck = new Checkbox("Enter Master Data");
	public Checkbox scientificCheck = new Checkbox("Autofill");
	Checkbox localCheck = new Checkbox("Autofill");
	public ComboBox<State> state = new ComboBox("");
	public ComboBox<District> district = new ComboBox("");
	public ComboBox<Block> block = new ComboBox("");
	public ComboBox<Village> village = new ComboBox();
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
	//TextField otherDetails = new TextField("Other Details");
	ComboBox<String> otherDetails = new ComboBox("Other Details");
	//TextField specialFeatures = new TextField("Special Features");
	ComboBox<String> specialFeatures = new ComboBox("Special Features");
	TextField remarks = new TextField("");
	TextField area = new TextField("Approx Area Sown");
	MultiSelectComboBox<MasterLocallanguage> localLanguages = new MultiSelectComboBox("");
	MultiSelectComboBox<MasterSeason> fruitSeasons = new MultiSelectComboBox("");
	TextField fruitSeason = new TextField();
	TextField localLanguage = new TextField();
	FormLayout formbasic = new FormLayout();
	public FormLayout frommaster = new FormLayout();
	TextField photo1Source = new TextField();
	TextField photo2Source = new TextField();
	TextField photo3Source = new TextField();
	TextField photo4Source = new TextField();
	TextField latitude = new TextField("Latitude");
	TextField longitude = new TextField("Longitude");
	//TextField management = new TextField("Management Options");
	ComboBox<String> management = new ComboBox("Management Options");
	//TextField xfield1 = new TextField("");
	ComboBox<String> xfield1 = new ComboBox("");
	//TextField xfield2 = new TextField("");
	ComboBox<String> xfield2 = new ComboBox("");
	public Button save = new Button("Save");
	Button cancel = new Button("Close");
	public Button delete = new Button("Delete");
	boolean isSuperAdmin;
	
	//Checkbox approved = new Checkbox("Approved", true);
	public ComboBox<MasterApproval> approved = new ComboBox("Approval Status");
	MasterFormat format;
	MemoryBuffer buffer1 = new MemoryBuffer();
	MemoryBuffer buffer2 = new MemoryBuffer();
	MemoryBuffer buffer3 = new MemoryBuffer();
	MemoryBuffer buffer4 = new MemoryBuffer();
	public HorizontalLayout imageLayout1=new HorizontalLayout();
	public HorizontalLayout imageLayout2=new HorizontalLayout();
	public HorizontalLayout imageLayout3=new HorizontalLayout();
	public HorizontalLayout imageLayout4=new HorizontalLayout();
	public Div imageContainer1=new Div();
	public Div imageContainer2=new Div();
	public Div imageContainer3=new Div();
	public Div imageContainer4=new Div();
	Upload upload1 = new Upload(buffer1);
	Upload upload2= new Upload(buffer2);
	Upload upload3= new Upload(buffer3);
	Upload upload4= new Upload(buffer4);
	//boolean isAdmin;
	public CropPlantsForm(Dbservice dbservice, CropService cservice) {
		super();
		//final CaptchaField captchaField = new CaptchaField(this);
		this.setHeightFull();
		this.dbservice = dbservice;
		this.cservice = cservice;
		addClassName("cropplants-view");
		initForm();
		binder.bindInstanceFields(this);
		//isAdmin=dbservice.isAdmin();
		add(createAccordion());
		
	}
	
	
	public Component createAccordion() {
		Accordion accordion = new Accordion();
		AccordionPanel topaccordion = new AccordionPanel();
		AccordionPanel bottomaccordion = new AccordionPanel();
		AccordionPanel imageaccordion = new AccordionPanel();
		topaccordion = accordion.add("Basic Info",
				new Div(createCheckPanel(), createMasterForm(), createBasicForm()));
		bottomaccordion = accordion.add("Other Info",  new Div(createCommonForm()));
		imageaccordion = accordion.add("Images & Photos",  new Div(createImageForm()));
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
		save.addClickListener(e -> validateCrop());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteCrop(crops));
		//delete.setVisible(getAdmin());
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save, cancel, delete);
		hl.setSizeFull();
		return hl;
	}

	private void validateCrop() {
		if (masterCheck.getValue()) {
			saveCropAfterValidation(true);
		} else {
			if (village.getValue() == null || village.getValue().equals(null)) {
				Notification.show("Error. Please Select a Village").addThemeVariants(NotificationVariant.LUMO_ERROR);
			} else {
				saveCropAfterValidation(false);
			}
		}

	}
	private void saveCropAfterValidation(boolean master) {
		try {
			format=crops.getFormat();
			binder.writeBean(crops);
			crops.setMaster(master);
			crops.setCrowdData(false);
			Village vill=crops.getVillage();
			if (getImageAsByteArray(buffer1) != null) {
				crops.setPhoto1(getImageAsByteArray(buffer1));
			}
			if (getImageAsByteArray(buffer2) != null) {
				crops.setPhoto2(getImageAsByteArray(buffer2));
			}
			if (getImageAsByteArray(buffer3) != null) {
				crops.setPhoto3(getImageAsByteArray(buffer3));
			}
			
			if (getImageAsByteArray(buffer4) != null) {
				crops.setPhoto4(getImageAsByteArray(buffer4));
			}
			fireEvent(new SaveEvent(this, crops));
			//clearForm(format);
			//Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);;
			initMasterFields(format);
			removeFields();
			clearBuffer();
			if (master == false) {
				state.setValue(vill.getBlock().getDistrict().getState());
				district.setValue(vill.getBlock().getDistrict());
				block.setValue(vill.getBlock());
				village.setValue(vill);
			}
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			Notification.show("Error. Please Check").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	public void deleteCrop(Crops crop) {
		ConfirmDialog dialog = new ConfirmDialog();
		if (crop == null || crop.equals(null) || crop.getScientificName()==null) {
			Notification.show("Error. Please Select An Item To Delete").addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			
			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item? You will not be able to undo this action.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.setRejectable(true);
			dialog.setRejectText("No");
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
		imageLayout1.remove(imageContainer1);
		upload1.clearFileList();
		imageLayout2.remove(imageContainer2);
		upload2.clearFileList();
		imageLayout3.remove(imageContainer3);
		upload3.clearFileList();
		imageLayout4.remove(imageContainer4);
		upload4.clearFileList();
	}

	private void populateForm() {
		// TODO Auto-generated method stub
		
		if (scientificName.getValue() != null ) {
			if (scientificCheck.getValue()) {
				
				Crops crops = cservice.findCropBySientificName(scientificName.getValue());
				if (crops != null) {
					removeFields();
					populateFields(crops);
					//setCrop(crops);
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
			otherDetails.setValue(crops.getOtherDetails());
			specialFeatures.setValue(crops.getSpecialFeatures());
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
			//approved.setVisible(false);
		} else {
			// System.out.println("Not");
			frommaster.setVisible(true);
			latitude.setVisible(true);
			longitude.setVisible(true);
			//approved.setVisible(true);
		}
	}

	public Component createMasterForm() {
		//state.setVisible(isSuperAdmin);
		state.setPlaceholder("State");
		district.setPlaceholder("District");
		block.setPlaceholder("Block");
		village.setPlaceholder("Village");
		frommaster.add(state, 1);
		frommaster.add(district, 1);
		frommaster.add(block, 1);
		frommaster.add(village, 1);
		frommaster.setResponsiveSteps(new ResponsiveStep("0", 1),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 3));
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
		formbasic.setResponsiveSteps(new ResponsiveStep("0", 1),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 6));
		// formbasic.setSizeFull();
		return formbasic;

	}

	public Component createCommonForm() {
		FormLayout formcommon = new FormLayout();
		formcommon.add(associatedTk, 4);
		formcommon.add(knowledgeHolder, 4);
		formcommon.add(otherDetails, 2);
		formcommon.add(latitude, 2);
		formcommon.add(longitude, 2);
		formcommon.add(approved, 2);
		formcommon.add(remarks, 2);
		//formcommon.add(imageLayout, 4);
		//formcommon.add(createUpload(upload2), 2);
		//formcommon.add(imageContainer, 1);
		formcommon.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 4));
		/*
		 * photo.addSucceededListener(event -> { String fileName = event.getFileName();
		 * InputStream inputStream = buffer.getInputStream(fileName); formcommon.add(new
		 * TextField("Source")); // Do something with the file data //
		 * processFile(inputStream, fileName); });
		 */		
		return formcommon;
	}

	public void initFields(MasterFormat format) {
		photo1Source.setPlaceholder("Source of The Above Photo");
		photo2Source.setPlaceholder("Source of The Above Photo");
		photo3Source.setPlaceholder("Source of The Above Photo");
		photo4Source.setPlaceholder("Source of The Above Photo");
		remarks.setVisible(false);
		approved.addValueChangeListener(e->showRemarks());
		remarks.setPlaceholder("Remarks for Rejection");
		commercial.setVisible(false);
		wildhome.setVisible(false);
		partsUsed.setVisible(false);
		area.setVisible(false);
		specialFeatures.setVisible(false);
		management.setVisible(false);
		xfield1.setVisible(false);
		xfield2.setVisible(false);
		state.setItems(dbservice.getStates());
		//State selectedState = dbservice.getState();
		//state.setValue(selectedState);
		state.addValueChangeListener(e -> district.setItems(dbservice.getDistricts(e.getValue())));
		state.setItemLabelGenerator(state -> state.getStateName());
		district.setItems(dbservice.getDistricts(state.getValue()));
		district.setItemLabelGenerator(district -> district.getDistrictName());
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), true)));
		village.setItemLabelGenerator(Village::getVillageName);
		district.getStyle().set("--vaadin-combo-box-overlay-width", "200px");
		block.getStyle().set("--vaadin-combo-box-overlay-width", "200px");
		village.getStyle().set("--vaadin-combo-box-overlay-width", "200px");
		approved.setItems(dbservice.getMasterApproval());
		approved.setItemLabelGenerator(approved->approved.getApproval());
		//--approved.setRequired(true);
		
		commercial.setItems(dbservice.getMasterCommercial());
		commercial.setItemLabelGenerator(MasterCommercial::getCname);
		wildhome.setItems(dbservice.getMasterWildhome());
		wildhome.setItemLabelGenerator(MasterWildhome::getWname);
		pastStatus.setItems(dbservice.getStatus());
		presentStatus.setItems(dbservice.getStatus());
		pastStatus.setItemLabelGenerator(status -> status.getStatus());
		presentStatus.setItemLabelGenerator(status -> status.getStatus());
		fruitSeasons.setItems(dbservice.getMasterFruitSeason());
		fruitSeasons.setItemLabelGenerator(fruitSeason -> fruitSeason.getFruitseason());
		//fruitSeason.setReadOnly(true);
		fruitSeasons.addValueChangeListener(e -> {
			String selectedSeason = e.getValue().stream().map(MasterSeason::getFruitseason)
					.collect(Collectors.joining(", "));
			fruitSeason.setValue(selectedSeason);
		});
		//fruitSeason.setVisible(false);
		localLanguages.setItems(dbservice.getLocalLanguage());
		localLanguages.setItemLabelGenerator(localLanguages -> localLanguages.getLanguageName());
		//localLanguage.setReadOnly(true);
		localLanguages.addValueChangeListener(e -> {
			String selectedLanguage = e.getValue().stream().map(MasterLocallanguage::getLanguageName)
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
		management.setAllowCustomValue(true);
		management.addCustomValueSetListener(e -> {
			String custommanagement = e.getDetail();
			management.setItems(custommanagement);
			management.setValue(custommanagement);
		});
		xfield1.setAllowCustomValue(true);
		xfield1.addCustomValueSetListener(e -> {
			String customxfield1 = e.getDetail();
			xfield1.setItems(customxfield1);
			xfield1.setValue(customxfield1);
		});
		xfield2.setAllowCustomValue(true);
		xfield2.addCustomValueSetListener(e -> {
			String customxfield2 = e.getDetail();
			xfield2.setItems(customxfield2);
			xfield2.setValue(customxfield2);
		});
		otherDetails.setAllowCustomValue(true);
		otherDetails.addCustomValueSetListener(e -> {
			String customotherDetails = e.getDetail();
			otherDetails.setItems(customotherDetails);
			otherDetails.setValue(customotherDetails);
		});
		specialFeatures.setAllowCustomValue(true);
		specialFeatures.addCustomValueSetListener(e -> {
			String customspecialFeatures = e.getDetail();
			specialFeatures.setItems(customspecialFeatures);
			specialFeatures.setValue(customspecialFeatures);
		});
		initMasterFields(format);
		initFormatFields(format);
	}
	
	private void showRemarks() {
		if (approved.getValue() != null) {
			if (approved.getValue().getId() == 2) {
				remarks.setVisible(true);
			} else {
				remarks.setVisible(false);
				remarks.setValue("");
			}
		}
	}
	
	public void initMasterFields(MasterFormat format) {
		//imageLayout.add(createUpload(upload1));
		//imageLayout.add(createUpload(upload2));
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
		List<String> managements=cservice.findManagementAsString(format);
		List<String> xfields1=cservice.findXfield1AsString(format);
		List<String> xfields2=cservice.findXfield2AsString(format);
		List<String> otherdetails=cservice.findOtherDetailsAsString(format);
		List<String> specialfeatures=cservice.findSpFeaturesAsString(format);
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
		management.setItems(managements);
		xfield1.setItems(xfields1);
		xfield2.setItems(xfields2);
		otherDetails.setItems(otherdetails);
		specialFeatures.setItems(specialfeatures);
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
		setImageButtons(formatNo);
		switch (formatNo) {
		case 1:
			area.setVisible(true);
			specialFeatures.setVisible(true);
			break;
		case 2:
			fruitSeasons.setPlaceholder("Season of Fruiting");
			break;
		case 3:
			variety.setVisible(false);
			//specialFeatures.setVisible(true);
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
			otherDetails.setLabel("Other Details Like Exotic");
			xfield1.setVisible(true);
			xfield2.setVisible(true);
			xfield1.setLabel("Affected Crops");
			xfield2.setLabel("Impact");
			break;
		case 5:
			variety.setVisible(false);
			type.setVisible(false);
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
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			partsUsed.setVisible(true);
			break;
		case 13:
			habitat.setVisible(false);
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
			//area.setVisible(true);
			wildhome.setVisible(true);
			break;
		case 15:
			habitat.setVisible(false);
			specialFeatures.setVisible(true);
			specialFeatures.setLabel("Features");
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			xfield2.setVisible(true);
			variety.setLabel("Breed");
			xfield2.setLabel("Method of Keeping");
			type.setLabel("Animal Type");
			management.setLabel("Commercial Rearing");
			management.setVisible(true);
			break;
		case 16:
			habitat.setLabel("Waterscape");
			specialFeatures.setVisible(true);
			specialFeatures.setLabel("Features");
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			type.setLabel("Fish Type");
			management.setLabel("Commercial Rearing");
			management.setVisible(true);
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
			xfield1.setVisible(true);
			xfield1.setLabel("Habit");
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
			xfield1.setVisible(true);
			xfield2.setVisible(true);
			xfield1.setLabel("Importance: Economic/Social/Cultural");
			xfield2.setLabel("Status");
			associatedTk.setVisible(false);
			knowledgeHolder.setVisible(false);
			break;
		case 20:
			type.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			specialFeatures.setVisible(true);
			specialFeatures.setLabel("Features");
			source.setVisible(false);
			partsUsed.setVisible(true);
			break;
		case 21:
			habitat.setVisible(false);
			type.setVisible(false);
			pastStatus.setVisible(false);
			presentStatus.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			otherDetails.setVisible(false);
			uses.setVisible(false);
			xfield1.setVisible(true);
			xfield2.setVisible(true);
			xfield1.setLabel("Importance");
			xfield2.setLabel("Trends");
			associatedTk.setVisible(false);
			knowledgeHolder.setVisible(false);
			break;
		case 22:
			type.setLabel("Plant");
			partsUsed.setVisible(true);
			fruitSeason.setVisible(false);
			fruitSeasons.setVisible(false);
			source.setVisible(false);
			//xfield1.setLabel("Habit");
			//xfield1.setVisible(true);
			break;
		case 23:
			type.setLabel("Associated Crop");
			variety.setVisible(false);
			fruitSeason.setVisible(false);
			fruitSeasons.setVisible(false);
			otherDetails.setLabel("Other Details(Mode of Use)");
			source.setVisible(false);
			partsUsed.setVisible(true);
			uses.setLabel("Uses(Usage)");
			break;
		case 24:
			type.setVisible(false);
			pastStatus.setVisible(false);
			presentStatus.setVisible(false);
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			uses.setVisible(false);
			commercial.setVisible(true);
			break;
		case 25:
			type.setLabel("Plant");
			fruitSeasons.setVisible(false);
			fruitSeason.setVisible(false);
			source.setVisible(false);
			uses.setVisible(true);
			uses.setLabel("Uses (usage)");
			partsUsed.setVisible(true);
			break;
		case 26:
			type.setVisible(false);
			partsUsed.setVisible(false);
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
			xfield1.setVisible(true);
			xfield1.setLabel("Mode of Hunting");
			break;
		
		}
		
	}
	public void setImageButtons(int a) {
		if(a==11 ||a==18 ||a==22 ||a==23 ||a==25) {
			upload1.setDropLabel(new H6("Whole Tree"));
			upload2.setDropLabel(new H6("Front Leaf"));
			upload3.setDropLabel(new H6("Back Leaf_"));
			upload4.setDropLabel(new H6("__Fruit___"));
		}else if(a==12||a==13||a==14||a==19||a==24||a==26) {
			upload1.setDropLabel(new H6("Whole Tree"));
			upload2.setDropLabel(new H6("Front Leaf"));
			upload3.setDropLabel(new H6("Back Leaf_"));
			upload4.setDropLabel(new H6("__Flower__"));
		}else if(a==1||a==2||a==3||a==4||a==5||a==7||a==7||a==9||a==10||a==15||a==16||a==21) {
			upload1.setDropLabel(new H6("__Full__"));
			upload2.setDropLabel(new H6("_Front__"));
			upload3.setDropLabel(new H6("__Side__"));
			upload4.setDropLabel(new H6("Top/Back"));
		}else {
			upload1.setDropLabel(new H6("Photo1"));
			upload2.setDropLabel(new H6("Photo2"));
			upload3.setDropLabel(new H6("Photo3"));
			upload4.setDropLabel(new H6("Photo4"));
		}
	}
	private Component createImageForm(){
		var imageform = new FormLayout();
		
		imageLayout1.add(createUpload(upload1));
		upload1.addFinishedListener(e->showPicture(imageLayout1,imageContainer1, buffer1, photo1Source));
		imageLayout2.add(createUpload(upload2), imageContainer2);
		upload2.addFinishedListener(e->showPicture(imageLayout2,imageContainer2, buffer2, photo2Source));
		imageLayout3.add(createUpload(upload3), imageContainer3);
		upload3.addFinishedListener(e->showPicture(imageLayout3,imageContainer3, buffer3, photo3Source));
		imageLayout4.add(createUpload(upload4), imageContainer4);
		upload4.addFinishedListener(e->showPicture(imageLayout4,imageContainer4, buffer4, photo4Source));
		imageform.add(imageLayout1,3);
		imageform.add(photo1Source, 3);
		imageform.add(imageLayout2,3);
		imageform.add(photo2Source, 3);
		imageform.add(imageLayout3,3);
		imageform.add(photo3Source, 3);
		imageform.add(imageLayout4,3);
		imageform.add(photo4Source, 3);
		imageform.setResponsiveSteps(new ResponsiveStep("0", 3),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("500px", 3));
		return imageform;
	}
	private Component createUpload(Upload upload) {
		upload.setHeight("20%");
		upload.getStyle().set("font-size", "12px");
		upload.setMaxFiles(1);
		
		upload.setMaxFileSize(1000000);
		Button uploadButton=new Button("Upload Photo");
		//upload.setDropLabel(uploadButton);
		uploadButton.getStyle().set("font-size", "12px");
		upload.setUploadButton(uploadButton);
		//upload.setDropLabel(new Label("Drop Photo"));
		upload.setAcceptedFileTypes("image/tiff", "image/jpeg", "image/jpg");
		upload.addFileRejectedListener(e -> Notification.show("Invalid File: Please select only image files less than 1Mb",3000, Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_WARNING));
		//Notification.show("Error. Please Check").addThemeVariants(NotificationVariant.LUMO_ERROR);
		//upload.addSucceededListener(event -> showPicture());
		
		return upload;
	}
	public void showPicture(HorizontalLayout imageLayout, Div imageContainer, MemoryBuffer buffer, TextField textfield) {
		try {
			//textfield.setVisible(true);
			
			imageLayout.remove(imageContainer);
			imageContainer.removeAll();
			StreamResource resource = new StreamResource("inputimage",
					() -> new ByteArrayInputStream(getImageAsByteArray(buffer)));
			Image image = new Image(resource, "No Photo to display");
			imageContainer.setWidth("100px");
			imageContainer.setHeight("100px");
			image.setSizeFull();
			imageContainer.add(image);
			//System.out.println("Hello");
			imageLayout.add(imageContainer);
			//imageContainer.get
		} catch (Exception e) {
			
			Notification.show("Error" + e);
		}
	}
	private byte[] getImageAsByteArray(MemoryBuffer buffer) {
		try {
			BufferedImage inputImageoriginal=null;
			inputImageoriginal= ImageIO.read(buffer.getInputStream());
			if (inputImageoriginal == null) {
				return null;
			} else {
				BufferedImage inputImage = resizeImage(inputImageoriginal);
				ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
				ImageIO.write(inputImage, "jpg", pngContent);
				InputStream is = new ByteArrayInputStream(pngContent.toByteArray());
				return IOUtils.toByteArray(is);
			}
		} catch (IOException e) {
			
			return null;
		}
	}

	BufferedImage resizeImage(BufferedImage originalImage) throws IOException {

		BufferedImage resizedImage = new BufferedImage(600,600, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, 600,600, null);
		graphics2D.dispose();
		return resizedImage;

	}
	
	public void addImage(HorizontalLayout imagelayout, Image image, Div imagecontainer) {
		imagelayout.remove(imagecontainer);
		imagecontainer.removeAll();
		imagecontainer.setWidth("100px");
		imagecontainer.setHeight("100px");
		image.setSizeFull();
		imagecontainer.add(image);
		imagelayout.add(imagecontainer);
	}
	public void removeImage(HorizontalLayout imagelayout, Div imagecontainer){
		imagelayout.remove(imagecontainer);
		imagecontainer.removeAll();
		
	}
	public void removeAllImages(){
		imageLayout1.remove(imageContainer1);
		imageContainer1.removeAll();
		imageLayout2.remove(imageContainer2);
		imageContainer2.removeAll();
		imageLayout3.remove(imageContainer3);
		imageContainer3.removeAll();
		imageLayout4.remove(imageContainer4);
		imageContainer4.removeAll();
		clearBuffer();
	}
	public void clearBuffer() {
		//UI.getCurrent().getPage().reload();/// last option to clear buffer
		buffer1=new MemoryBuffer();
		buffer2=new MemoryBuffer();
		buffer3=new MemoryBuffer();
		buffer4=new MemoryBuffer();
		upload1.setReceiver(buffer1);
		upload2.setReceiver(buffer2);
		upload3.setReceiver(buffer3);
		upload4.setReceiver(buffer4);
		try {
			upload1.clearFileList();
			upload2.clearFileList();
			upload3.clearFileList();
			upload4.clearFileList();
			upload1.getElement().setPropertyJson("files", Json.createArray());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
