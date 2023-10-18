package com.megpbr.views;

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
import com.megpbr.data.entity.pbr.Scapes;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.ScapeService;
import com.megpbr.views.MarketsForm.SaveEvent;
import com.megpbr.views.agrobiodiversity.ScapeView;
import com.megpbr.views.dashboard.DashboardView;
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
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

public class ScapeForm extends Div {
	Dbservice dbservice;
	ScapeService sservice;
	// Crops scapes;
	Scapes scape;
	MasterStatus status;
	ScapeView scapeview;
	Binder<Scapes> binder = new BeanValidationBinder<>(Scapes.class);
	public Checkbox masterCheck = new Checkbox("Enter Master Data");
	public Checkbox scientificCheck = new Checkbox("Autofill");
	Checkbox localCheck = new Checkbox("Autofill");
	public ComboBox<State> state = new ComboBox("");
	public ComboBox<District> district = new ComboBox("");
	public ComboBox<Block> block = new ComboBox("");
	public ComboBox<Village> village = new ComboBox();
	//ComboBox<MasterStatus> pastStatus = new ComboBox("Past Status");
	//ComboBox<MasterStatus> presentStatus = new ComboBox("Present Status");
	//ComboBox<MasterCommercial> commercial = new ComboBox("Commercial/Non Commercial");
	//ComboBox<MasterWildhome> wildhome = new ComboBox("Wild/Home");
	ComboBox<String> faunaPopulation = new ComboBox("General Fauna");
	ComboBox<String> floraOccupation = new ComboBox("General Flora");
	ComboBox<String> typeAgriOccupation = new ComboBox("Sub Occupation");
	ComboBox<String> landscape = new ComboBox("Depending Landscape");
	ComboBox<String> sublandscape = new ComboBox("Sub Landscape");
	ComboBox<String> forest = new ComboBox("Forest");
	ComboBox<String> fallow = new ComboBox("Major Resources & Season of Access");
	ComboBox<String> wetLand = new ComboBox("Wet Land");
	TextField features = new TextField("Features");
	ComboBox<String> ownerHouse = new ComboBox("Ownership");
	ComboBox<String> userGroups = new ComboBox("User Groups");
	//ComboBox<String> variety = new ComboBox("Variety");
	ComboBox<String> management = new ComboBox("Management Practices");
	ComboBox<String> socialCommunity = new ComboBox("Community Accessed");
	MultiSelectComboBox<MasterLocallanguage> localLanguages = new MultiSelectComboBox("");
	TextField localLanguage = new TextField();
	ComboBox<String> generalResources = new ComboBox("General Uses");
	ComboBox<String> associatedTk = new ComboBox("Associated Tk");
	TextField otherDetails = new TextField("Other Details");
	TextField remarks = new TextField("");
	FormLayout formbasic = new FormLayout();
	public FormLayout frommaster = new FormLayout();
	TextField latitude = new TextField("Latitude");
	TextField longitude = new TextField("Longitude");
	public Button save = new Button("Save");
	public Button cancel = new Button("Close");
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
	TextField photo1Source = new TextField();
	TextField photo2Source = new TextField();
	TextField photo3Source = new TextField();
	TextField photo4Source = new TextField();
	public ScapeForm(Dbservice dbservice, ScapeService sservice) {
		super();
		this.setHeightFull();
		this.dbservice = dbservice;
		this.sservice = sservice;
		addClassName("scapeplants-view");
		initForm();
		binder.bindInstanceFields(this);
		// initFields();
		add(createAccordion());
		//isSuperAdmin = dbservice.isSuperAdmin();
	}

	public Component createAccordion() {
		Accordion accordion = new Accordion();
		AccordionPanel topaccordion = new AccordionPanel();
		AccordionPanel bottomaccordion = new AccordionPanel();
		AccordionPanel imageaccordion = new AccordionPanel();
		topaccordion = accordion.add("Basic Info",
				new VerticalLayout(createCheckPanel(), createMasterForm(), createBasicForm()));
		bottomaccordion = accordion.add("Other Info", new VerticalLayout(createCommonForm()));
		bottomaccordion = accordion.add("Images & Photos",  new VerticalLayout(createImageForm()));
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
		faunaPopulation.addValueChangeListener(e -> populateForm());
		vl.add(masterCheck, scientificCheck);
		return vl;
	}

	private Component createButtons() {
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		cancel.addClickShortcut(Key.ESCAPE);
		cancel.addClickListener(e -> closeForm());
		save.addClickListener(e -> validateandsaveScape());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteScape(scape));
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save, cancel, delete);
		hl.setSizeFull();
		return hl;
	}

	private void validateandsaveScape() {
		try {
			format = scape.getFormat();
			if (masterCheck.getValue()) {
				binder.writeBean(scape);
				saveScapeAfterValidation(true);
			} else {
				if (village.getValue() == null || village.getValue().equals(null)) {
					Notification.show("Please Select The Village");
				} else {
					binder.writeBean(scape);
					saveScapeAfterValidation(false);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Notification.show("Error: " + e);
		}

	}
	private void saveScapeAfterValidation(boolean master) {
		try {
			format = scape.getFormat();
			binder.writeBean(scape);
			scape.setMaster(master);
			if (getImageAsByteArray(buffer1) != null) {
				scape.setPhoto1(getImageAsByteArray(buffer1));
			}
			if (getImageAsByteArray(buffer2) != null) {
				scape.setPhoto2(getImageAsByteArray(buffer2));
			}
			if (getImageAsByteArray(buffer3) != null) {
				scape.setPhoto3(getImageAsByteArray(buffer3));
			}
			
			if (getImageAsByteArray(buffer4) != null) {
				scape.setPhoto4(getImageAsByteArray(buffer4));
			}
			fireEvent(new SaveEvent(this, scape));
			clearForm(format);
			//Notification.show("Saved Successfully");
			initMasterFields(format);
			removeFields();
			clearBuffer();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteScape(Scapes scape) {
		ConfirmDialog dialog = new ConfirmDialog();
		if (scape == null || scape.equals(null) || scape.getFaunaPopulation() == null) {
			// dialog.open();
		} else {

			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.setRejectable(true);
			dialog.setRejectText("Discard");
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event -> fireEvent(new DeleteEvent(this, scape)));
			dialog.open();
		}
	}

	public void setScape(Scapes scape) {
		this.scape = scape;
		binder.readBean(scape);
	}

	public void closeForm() {
		this.setVisible(false);
	}

	private void clearForm(MasterFormat format) {
		this.scape = new Scapes();
		scape.setFormat(format);

	}

	private void initForm() {
		binder.setBean(new Scapes());
	}

	private void removeFields() {
		associatedTk.setValue("");
		otherDetails.setValue("");
		//variety.setValue("");
		localLanguage.setValue("");
		localLanguages.setValue();
	}

	private void populateForm() {
		// TODO Auto-generated method stub

		if (faunaPopulation.getValue() != null) {
			if (scientificCheck.getValue()) {
				Scapes scapes = sservice.getScapeByFauna(faunaPopulation.getValue());
				if (scapes != null) {
					populateFields(scapes);
				}
			} else {
				if (scientificCheck.isEnabled()) {
					removeFields();
				}
			}
		} else {
			// Notification.show("Please Enter the Scientific Name");
		}
	}

	private void populateFields(Scapes scapes) {

		try {

			faunaPopulation.setValue(scapes.getFaunaPopulation());
			floraOccupation.setValue(scapes.getFloraOccupation());
			fallow.setValue(scapes.getFallow());
			associatedTk.setValue(scapes.getAssociatedTk());
			otherDetails.setValue(scapes.getOtherDetails());
			generalResources.setValue(scapes.getGeneralResources());
			landscape.setValue(scapes.getLandscape());
			ownerHouse.setValue(scapes.getOwnerHouse());
			socialCommunity.setValue(scapes.getSocialCommunity());
			sublandscape.setValue(scapes.getSubLandscape());
			typeAgriOccupation.setValue(scapes.getTypeAgriOccupation());
			forest.setValue(scapes.getForest());
			localLanguage.setValue(scapes.getLocalLanguage());
			features.setValue(scapes.getFeatures());
			userGroups.setValue(scapes.getUserGroups());
			remarks.setValue(scapes.getRemarks());
			//partsUsed.setValue(scapes.getPartsUsed());
			management.setValue(scapes.getManagement());
		
		} catch (Exception e) { // TODO Auto-generated catch block

		}

	}

	private void hideFields() {
		if (masterCheck.getValue()) {
			// state.setVisible(false);
			frommaster.setVisible(false);
			latitude.setVisible(false);
			longitude.setVisible(false);
			approved.setVisible(false);
		} else {
			// System.out.println("Not");
			frommaster.setVisible(true);
			latitude.setVisible(true);
			longitude.setVisible(true);
			approved.setVisible(true);
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
		frommaster.setResponsiveSteps(new ResponsiveStep("0", 1),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("200px", 3));
		// frommaster.setSizeFull();
		return frommaster;
	}

	public Component createBasicForm() {
		localLanguages.setPlaceholder("Local Language");
		formbasic.add(faunaPopulation, 3);
		formbasic.add(floraOccupation, 3);
		formbasic.add(typeAgriOccupation, 3);
		formbasic.add(landscape, 3);
		formbasic.add(management, 3);
		formbasic.add(fallow, 3);
		formbasic.add(forest, 3);
		formbasic.add(wetLand, 3);
		formbasic.add(generalResources, 3);
		formbasic.add(sublandscape, 3);
		formbasic.add(ownerHouse, 3);
		formbasic.add(socialCommunity, 3);
		formbasic.add(userGroups, 3);
		formbasic.add(features, 3);
		formbasic.add(localLanguages, 3);
		formbasic.add(localLanguage, 3);
		formbasic.setResponsiveSteps(new ResponsiveStep("0", 3), new ResponsiveStep("200px", 6));
		return formbasic;
	}

	public Component createCommonForm() {
		FormLayout formcommon = new FormLayout();
		formcommon.add(associatedTk, 1);
		formcommon.add(otherDetails, 1);
		formcommon.add(latitude, 1);
		formcommon.add(longitude, 1);
		formcommon.add(approved, 1);
		formcommon.add(remarks, 1);
		formcommon.setResponsiveSteps(new ResponsiveStep("0", 1),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 2));
		
		return formcommon;
	}

	public void initFields(MasterFormat format) {
		//management.setVisible(false);
		photo1Source.setPlaceholder("Source of The Above Photo");
		photo2Source.setPlaceholder("Source of The Above Photo");
		photo3Source.setPlaceholder("Source of The Above Photo");
		photo4Source.setPlaceholder("Source of The Above Photo");
		
		wetLand.setVisible(false);
		localLanguage.setVisible(false);
		localLanguages.setVisible(false);
		remarks.setVisible(false);
		remarks.setPlaceholder("Remarks for Rejection");
		approved.addValueChangeListener(e->showRemarks());
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
		district.getStyle().set("--vaadin-combo-box-overlay-width", "200px");
		block.getStyle().set("--vaadin-combo-box-overlay-width", "200px");
		village.getStyle().set("--vaadin-combo-box-overlay-width", "200px");
		approved.setItems(dbservice.getMasterApproval());
		approved.setItemLabelGenerator(approved->approved.getApproval());
		localLanguages.setItems(dbservice.getLocalLanguage());
		localLanguages.setItemLabelGenerator(localLanguages -> localLanguages.getLanguageName());
		localLanguage.setReadOnly(true);
		localLanguages.addValueChangeListener(e -> {
			String selectedLanguage = e.getValue().stream().map(MasterLocallanguage::getLanguageName)
					.collect(Collectors.joining(", "));
			localLanguage.setValue(selectedLanguage);
		});

		faunaPopulation.setAllowCustomValue(true);
		faunaPopulation.addCustomValueSetListener(e -> {
			String customValue = e.getDetail();
			faunaPopulation.setItems(customValue);
			faunaPopulation.setValue(customValue);
		});
		floraOccupation.setAllowCustomValue(true);
		floraOccupation.addCustomValueSetListener(e -> {
			String customValuef = e.getDetail();
			floraOccupation.setItems(customValuef);
			floraOccupation.setValue(customValuef);
		});
		typeAgriOccupation.setAllowCustomValue(true);
		typeAgriOccupation.addCustomValueSetListener(e -> {
			String customtypeAgriOccupation = e.getDetail();
			typeAgriOccupation.setItems(customtypeAgriOccupation);
			typeAgriOccupation.setValue(customtypeAgriOccupation);
		});
		landscape.setAllowCustomValue(true);
		landscape.addCustomValueSetListener(e -> {
			String customlandscape = e.getDetail();
			landscape.setItems(customlandscape);
			landscape.setValue(customlandscape);
		});
		sublandscape.setAllowCustomValue(true);
		sublandscape.addCustomValueSetListener(e -> {
			String customsubLandscape = e.getDetail();
			sublandscape.setItems(customsubLandscape);
			sublandscape.setValue(customsubLandscape);
		});
		ownerHouse.setAllowCustomValue(true);
		ownerHouse.addCustomValueSetListener(e -> {
			String customownerHouse = e.getDetail();
			ownerHouse.setItems(customownerHouse);
			ownerHouse.setValue(customownerHouse);
		});
		
		userGroups.setAllowCustomValue(true);
		userGroups.addCustomValueSetListener(e -> {
			String customuserGroups = e.getDetail();
			userGroups.setItems(customuserGroups);
			userGroups.setValue(customuserGroups);
		});
		management.setAllowCustomValue(true);
		management.addCustomValueSetListener(e -> {
			String custommanagement = e.getDetail();
			management.setItems(custommanagement);
			management.setValue(custommanagement);
		});
		socialCommunity.setAllowCustomValue(true);
		socialCommunity.addCustomValueSetListener(e -> {
			String customsocialCommunity = e.getDetail();
			socialCommunity.setItems(customsocialCommunity);
			socialCommunity.setValue(customsocialCommunity);
		});
		generalResources.setAllowCustomValue(true);
		generalResources.addCustomValueSetListener(e -> {
			String customgeneralResources = e.getDetail();
			generalResources.setItems(customgeneralResources);
			generalResources.setValue(customgeneralResources);
		});
		associatedTk.setAllowCustomValue(true);
		associatedTk.addCustomValueSetListener(e -> {
			String customassociatedTk = e.getDetail();
			associatedTk.setItems(customassociatedTk);
			associatedTk.setValue(customassociatedTk);
		});
		fallow.setAllowCustomValue(true);
		fallow.addCustomValueSetListener(e -> {
			String customfallow = e.getDetail();
			fallow.setItems(customfallow);
			fallow.setValue(customfallow);
		});
		wetLand.setAllowCustomValue(true);
		wetLand.addCustomValueSetListener(e -> {
			String customwetLand = e.getDetail();
			wetLand.setItems(customwetLand);
			wetLand.setValue(customwetLand);
		});
		forest.setAllowCustomValue(true);
		forest.addCustomValueSetListener(e -> {
			String customforest = e.getDetail();
			forest.setItems(customforest);
			forest.setValue(customforest);
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
		// System.out.println(format);
		
		  List<String> fauna=sservice.getFaunaList(format);
		  List<String> flora=sservice.getFloraList(format); 
		  List<String> typeAgri=sservice.getTypeList(format); 
		  List<String> landsca=sservice.getLandscapeList(format); 
		  List<String> sublansca=sservice.getSubLandscapeList(format); 
		  List<String> owner=sservice.getOwnerList(format); 
		  List<String> userg=sservice.getUserGroupList(format); 
		  List<String> mgmt=sservice.getManagementList(format);
		  List<String> social=sservice.getSocialList(format); 
		  List<String> general=sservice.getGeneralList(format);
		  List<String> associated=sservice.getAssociatedTkList(format); 
		  List<String> fallows=sservice.getFallowList(format); 
		  List<String> forests=sservice.getForestList(format);
		  List<String> wets=sservice.getWetLandList(format);
		  faunaPopulation.setItems(fauna);
		  floraOccupation.setItems(flora);
		  typeAgriOccupation.setItems(typeAgri);
		  landscape.setItems(landsca);
		  sublandscape.setItems(sublansca);
		  ownerHouse.setItems(owner);
		  userGroups.setItems(userg);
		  management.setItems(mgmt);
		  socialCommunity.setItems(social);
		  generalResources.setItems(general);
		  associatedTk.setItems(associated);
		  fallow.setItems(fallows);
		  forest.setItems(forests);
		  wetLand.setItems(wets);
		  //scientificName.setItems(snames); localName.setItems(lnames);
		  
		 
	}

	public static abstract class ScapeFormEvent extends ComponentEvent<ScapeForm> {
		private Scapes scape;

		protected ScapeFormEvent(ScapeForm source, Scapes scape) {
			super(source, false);
			this.scape = scape;
		}

		public Scapes getScapes() {
			return scape;
		}
	}

	public static class SaveEvent extends ScapeFormEvent {
		SaveEvent(ScapeForm source, Scapes scape) {
			super(source, scape);
		}
	}

	public static class DeleteEvent extends ScapeFormEvent {
		DeleteEvent(ScapeForm source, Scapes scape) {
			super(source, scape);
		}

	}

	public static class CloseEvent extends ScapeFormEvent {
		CloseEvent(ScapeForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

	public void initFormatFields(MasterFormat format) {
		int formatNo = format.getFormat();
		setImageButtons(formatNo);
		switch (formatNo) {
		case 7:
			faunaPopulation.setLabel("Community & Poplulation");
			floraOccupation.setLabel("Families & Major Occupation");
			userGroups.setLabel("Nature of Inhabitants");
			generalResources.setLabel("Resource Management Practices");
			features.setLabel("No. of Households");
			socialCommunity.setLabel("Social Condition");
			management.setLabel("Landscape Management Practices");
			ownerHouse.setLabel("Caste/tribe");
			otherDetails.setVisible(false);
			associatedTk.setVisible(false);
			forest.setVisible(false);
			sublandscape.setVisible(false);
			break;
		case 8:
			wetLand.setVisible(true);
			typeAgriOccupation.setLabel("Agricultural Land");
			landscape.setLabel("Pond");
			fallow.setLabel("Fallow");
			wetLand.setVisible(true);
			features.setLabel("Features & Approx Area");
			break;
		case 9:
			wetLand.setVisible(true);
			sublandscape.setVisible(false);
			features.setVisible(false);
			ownerHouse.setVisible(false);
			faunaPopulation.setLabel("Element Type");
			floraOccupation.setLabel("Sub Type");
			typeAgriOccupation.setLabel("Features & Approx Area");
			landscape.setLabel("Ownership");
			fallow.setLabel("General Flora");
			forest.setLabel("General Fauna");
			wetLand.setLabel("Major Uses");
			management.setLabel("Management Practices");
			generalResources.setLabel("General Uses");
			break;

		case 10:
			wetLand.setVisible(false);
			sublandscape.setVisible(false);
			ownerHouse.setVisible(false);
			features.setVisible(false);
			userGroups.setVisible(false);
			management.setVisible(false);
			generalResources.setVisible(false);
			socialCommunity.setVisible(false);
			faunaPopulation.setLabel("Soil Type ");
			floraOccupation.setLabel("Colour & Texture");
			typeAgriOccupation.setLabel("Features");
			landscape.setLabel("Soil Management");
			fallow.setLabel("Flora & Fauna");
			forest.setLabel("Plant/Crop Suitable");
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
		upload.addFileRejectedListener(e -> Notification.show("Invalid File: Please select only image files less than 1Mb",3000, Position.TOP_END));
		//upload.addSucceededListener(event -> showPicture());
		
		return upload;
	}
	public void showPicture(HorizontalLayout imageLayout, Div imageContainer, MemoryBuffer buffer, TextField textfield) {
		try {
			//textfield.setVisible(true);
			//textfield.setPlaceholder("Source of The Above Photo");
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
			e.printStackTrace();
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
			e.printStackTrace();
			return null;
		}
	}
	BufferedImage resizeImage(BufferedImage originalImage) throws IOException {
		
		BufferedImage resizedImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, 600, 600, null);
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
