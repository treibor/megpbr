package com.megpbr.views;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterApproval;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.MasterStatus;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Markets;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.MarketsService;
import com.megpbr.utils.ValidationUtil;
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
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
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

public class MarketsForm extends Div {
	Dbservice dbservice;
	MarketsService sservice;
	Markets market;
	MasterStatus status;
	//MarketsView marketview;
	Binder<Markets> binder = new BeanValidationBinder<>(Markets.class);
	public Checkbox masterCheck = new Checkbox("Enter Master Data");
	public Checkbox scientificCheck = new Checkbox("Autofill");
	Checkbox localCheck = new Checkbox("Autofill");
	public ComboBox<State> state = new ComboBox<>("");
	public ComboBox<District> district = new ComboBox<>("");
	public ComboBox<Block> block = new ComboBox<>("");
	public ComboBox<Village> village = new ComboBox<>();
	public ComboBox<MasterApproval> approved = new ComboBox<>("Approval Status");
	ComboBox<String> name = new ComboBox<>("Market Name & Location");
	ComboBox<String> frequency = new ComboBox<>("Day Month");
	ComboBox<String> month = new ComboBox<>("Month (in case of Annual/Bi Annual)");
	ComboBox<String> day = new ComboBox<>("Day Held");
	ComboBox<String> animalType = new ComboBox<>("Types of Animal Bought & Sold");
	ComboBox<String> transactions = new ComboBox<>("Types & Avg Transactions Per Day");
	ComboBox<String> placesFrom = new ComboBox<>("Places From Which Animals are Brought");
	ComboBox<String> placesTo = new ComboBox<>("Places To Which Animals are Sold");
	ComboBox<String> fishLocation = new ComboBox<>("Name & Location of Fish Market");
	ComboBox<String> fishType = new ComboBox<>("Types of Fishes Sold");
	ComboBox<String> fishSource = new ComboBox<>("Sources of Fish");
	FormLayout formbasic = new FormLayout();
	public FormLayout frommaster = new FormLayout();
	TextField latitude = new TextField("Latitude");
	TextField longitude = new TextField("Longitude");
	TextField remarks = new TextField("");
	public Button save = new Button("Save");
	public Button cancel = new Button("Close");
	public Button delete = new Button("Delete");
	boolean isSuperAdmin;
	//Checkbox approved = new Checkbox("Approved", true);
	
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
	public MarketsForm(Dbservice dbservice, MarketsService sservice) {
		super();
		this.setHeightFull();
		this.dbservice = dbservice;
		this.sservice = sservice;
		addClassName("marketplants-view");
		initForm();
		binder.bindInstanceFields(this);
		// initFields();
		add(createAccordion());
		//isSuperAdmin = u//.isSuperAdmin();
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
		name.addValueChangeListener(e -> populateForm());
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
		delete.addClickListener(e -> deleteScape(market));
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save, cancel, delete);
		hl.setSizeFull();
		return hl;
	}

	private void validateandsaveScape() {
		try {
			format = market.getFormat();
			if (masterCheck.getValue()) {
				saveMarketAfterValidation(true);
			} else {
				if (village.getValue() == null || village.getValue().equals(null)) {
					Notification.show("Please Select The Village");
				} else {
					saveMarketAfterValidation(false);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			Notification.show("Error: " + e);
		}

	}
	
	private void saveMarketAfterValidation(boolean master) {
		
		if (!ValidationUtil.applyValidation(animalType.getValue())
				|| !ValidationUtil.applyValidation(day.getValue())
				|| !ValidationUtil.applyValidation(fishLocation.getValue())
				|| !ValidationUtil.applyValidation(fishSource.getValue())
				|| !ValidationUtil.applyValidation(fishType.getValue())
				|| !ValidationUtil.applyValidation(frequency.getValue())
				|| !ValidationUtil.applyValidation(month.getValue())
				|| !ValidationUtil.applyValidation(name.getValue())
				|| !ValidationUtil.applyValidation(placesFrom.getValue())
				|| !ValidationUtil.applyValidation(placesTo.getValue())
				|| !ValidationUtil.applyValidation(transactions.getValue())) {
			Notification.show("Validation Error: Special Characters like *, ?, ^,%, $ ,#  are not allowed")
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			try {
				format = market.getFormat();
				binder.writeBean(market);
				market.setMaster(master);
				market.setCrowdData(false);
				Village vill = market.getVillage();
				if (getImageAsByteArray(buffer1) != null) {
					market.setPhoto1(getImageAsByteArray(buffer1));
				}
				if (getImageAsByteArray(buffer2) != null) {
					market.setPhoto2(getImageAsByteArray(buffer2));
				}
				if (getImageAsByteArray(buffer3) != null) {
					market.setPhoto3(getImageAsByteArray(buffer3));
				}

				if (getImageAsByteArray(buffer4) != null) {
					market.setPhoto4(getImageAsByteArray(buffer4));
				}
				fireEvent(new SaveEvent(this, market));
				clearForm(format);
				// Notification.show("Saved Successfully");
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

			}
		}
	}
	public void deleteScape(Markets market) {
		ConfirmDialog dialog = new ConfirmDialog();
		if (market == null || market.equals(null) || market.getName() == null) {
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
			dialog.addConfirmListener(event -> fireEvent(new DeleteEvent(this, market)));
			dialog.open();
		}
	}

	public void setMarket(Markets market) {
		this.market = market;
		binder.readBean(market);
	}

	public void closeForm() {
		this.setVisible(false);
	}

	private void clearForm(MasterFormat format) {
		this.market = new Markets();
		market.setFormat(format);

	}

	private void initForm() {
		binder.setBean(new Markets());
	}

	private void removeFields() {
		/*
		 * associatedTk.setValue(""); otherDetails.setValue(""); //variety.setValue("");
		 * localLanguage.setValue(""); localLanguages.setValue();
		 */
	}

	private void populateForm() {
		// TODO Auto-generated method stub

		if (name.getValue() != null) {
			if (scientificCheck.getValue()) {
				Markets markets = sservice.getMarketByName(name.getValue());
				if (markets != null) {
					populateFields(markets);
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

	private void populateFields(Markets markets) {
		
		try {

			name.setValue(markets.getName());
			frequency.setValue(markets.getFrequency());
			month.setValue(markets.getMonth());
			day.setValue(markets.getDay());
			animalType.setValue(markets.getAnimalType());
			transactions.setValue(markets.getTransactions());
			placesFrom.setValue(markets.getPlacesFrom());
			placesTo.setValue(markets.getPlacesTo());
			fishLocation.setValue(markets.getFishLocation());
			fishType.setValue(markets.getFishType());
			fishSource.setValue(markets.getFishSource());
			remarks.setValue(markets.getRemarks());
			
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
				new ResponsiveStep("300px", 3));
		// frommaster.setSizeFull();
		return frommaster;
	}

	public Component createBasicForm() {
		ValidationUtil.applyValidation(latitude);
		ValidationUtil.applyValidation(longitude);
		ValidationUtil.applyValidation(photo1Source);
		ValidationUtil.applyValidation(photo2Source);
		ValidationUtil.applyValidation(photo3Source);
		ValidationUtil.applyValidation(photo4Source);
		
		formbasic.add(name, 1);
		formbasic.add(frequency, 1);
		formbasic.add(month, 1);
		formbasic.add(day, 1);
		formbasic.add(animalType, 1);
		formbasic.add(transactions, 1);
		formbasic.add(placesFrom, 2);
		formbasic.add(placesTo, 2);
		formbasic.add(fishLocation, 1);
		formbasic.add(fishType, 1);
		formbasic.add(fishSource, 1);
		formbasic.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("300px", 2));
		return formbasic;
	}

	public Component createCommonForm() {
		FormLayout formcommon = new FormLayout();
		formcommon.add(latitude, 1);
		formcommon.add(longitude, 1);
		formcommon.add(approved, 1);
		
		formcommon.setResponsiveSteps(new ResponsiveStep("0", 1),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 2));
		
		return formcommon;
	}

	public void initFields(MasterFormat format) {
		photo1Source.setPlaceholder("Source of The Above Photo");
		photo2Source.setPlaceholder("Source of The Above Photo");
		photo3Source.setPlaceholder("Source of The Above Photo");
		photo4Source.setPlaceholder("Source of The Above Photo");
		
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
		approved.setValue(dbservice.getMasterApproval().get(1));
		name.setAllowCustomValue(true);
		name.addCustomValueSetListener(e -> {
			String customValue = e.getDetail();
			name.setItems(customValue);
			name.setValue(customValue);
		});
		frequency.setAllowCustomValue(true);
		frequency.addCustomValueSetListener(e -> {
			String customValuef = e.getDetail();
			frequency.setItems(customValuef);
			frequency.setValue(customValuef);
		});
		month.setAllowCustomValue(true);
		month.addCustomValueSetListener(e -> {
			String custommonth = e.getDetail();
			month.setItems(custommonth);
			month.setValue(custommonth);
		});
		day.setAllowCustomValue(true);
		day.addCustomValueSetListener(e -> {
			String customday = e.getDetail();
			day.setItems(customday);
			day.setValue(customday);
		});
		animalType.setAllowCustomValue(true);
		animalType.addCustomValueSetListener(e -> {
			String customsubLandmarket = e.getDetail();
			animalType.setItems(customsubLandmarket);
			animalType.setValue(customsubLandmarket);
		});
		transactions.setAllowCustomValue(true);
		transactions.addCustomValueSetListener(e -> {
			String customtransactions = e.getDetail();
			transactions.setItems(customtransactions);
			transactions.setValue(customtransactions);
		});

		placesFrom.setAllowCustomValue(true);
		placesFrom.addCustomValueSetListener(e -> {
			String customplacesFrom = e.getDetail();
			placesFrom.setItems(customplacesFrom);
			placesFrom.setValue(customplacesFrom);
		});
		placesTo.setAllowCustomValue(true);
		placesTo.addCustomValueSetListener(e -> {
			String customplacesTo = e.getDetail();
			placesTo.setItems(customplacesTo);
			placesTo.setValue(customplacesTo);
		});
		fishLocation.setAllowCustomValue(true);
		fishLocation.addCustomValueSetListener(e -> {
			String customfishLocation = e.getDetail();
			fishLocation.setItems(customfishLocation);
			fishLocation.setValue(customfishLocation);
		});
		fishType.setAllowCustomValue(true);
		fishType.addCustomValueSetListener(e -> {
			String customfishType = e.getDetail();
			fishType.setItems(customfishType);
			fishType.setValue(customfishType);
		});
		fishSource.setAllowCustomValue(true);
		fishSource.addCustomValueSetListener(e -> {
			String customfishSource = e.getDetail();
			fishSource.setItems(customfishSource);
			fishSource.setValue(customfishSource);
		});
//		addCustomValueSetListener(name);
//		addCustomValueSetListener(frequency);
//		addCustomValueSetListener(month);
//		addCustomValueSetListener(day);
//		addCustomValueSetListener(animalType);
//		addCustomValueSetListener(transactions);
//		addCustomValueSetListener(placesFrom);
//		addCustomValueSetListener(placesTo);
//		addCustomValueSetListener(fishLocation);
//		addCustomValueSetListener(fishType);
//		addCustomValueSetListener(fishSource);
		initMasterFields(format);
		initFormatFields(format);
	}
	private void addCustomValueSetListener(ComboBox<String> comboBox) {
		comboBox.setAllowCustomValue(true);
		comboBox.addCustomValueSetListener(event -> {
			String customValue = event.getDetail();
			if (customValue != null && !customValue.matches("[0-9A-Za-z@./- ]+")) {
				// Show an error notification or reset the value
				Notification.show("Invalid input: Only letters, numbers, and '@', '.', '/', '-'  are allowed").addThemeVariants(NotificationVariant.LUMO_WARNING);
				comboBox.clear();
			} else {
				comboBox.setItems(customValue);
				comboBox.setValue(customValue);
			}
		});
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
		List<String> marketname = sservice.getMarketNameList(format);
		List<String> freq = sservice.getFrequencyList(format);
		List<String> monthname = sservice.getMonthList(format);
		List<String> dayname = sservice.getDayList(format);
		List<String> animalname = sservice.getAnimalTypeList(format);
		List<String> transact = sservice.getTransactionsList(format);
		List<String> pfrom = sservice.getPlacesFromList(format);
		List<String> pto = sservice.getPlacesToList(format);
		List<String> flocation = sservice.getFishLocationList(format);
		List<String> ftype = sservice.getFishTypeList(format);
		List<String> fsource = sservice.getFishSourceList(format);
		name.setItems(marketname);
		frequency.setItems(freq);
		month.setItems(monthname);
		day.setItems(dayname);
		animalType.setItems(animalname);
		transactions.setItems(transact);
		placesFrom.setItems(pfrom);
		placesTo.setItems(pto);
		fishLocation.setItems(flocation);
		fishType.setItems(ftype);
		fishSource.setItems(fsource);
	}

	public static abstract class ScapeFormEvent extends ComponentEvent<MarketsForm> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Markets market;
		protected ScapeFormEvent(MarketsForm source, Markets market) {
			super(source, false);
			this.market = market;
		}

		public Markets getMarkets() {
			return market;
		}
	}

	public static class SaveEvent extends ScapeFormEvent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		SaveEvent(MarketsForm source, Markets market) {
			super(source, market);
		}
	}

	public static class DeleteEvent extends ScapeFormEvent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		DeleteEvent(MarketsForm source, Markets market) {
			super(source, market);
		}

	}

	public static class CloseEvent extends ScapeFormEvent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		CloseEvent(MarketsForm source) {
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
		case 6:
			month.setVisible(false);
			day.setVisible(false);
			transactions.setPlaceholder("");
			break;
		case 17:
			fishType.setVisible(false);
			fishSource.setVisible(false);
			name.setLabel("Weekly Market Name");
			frequency.setLabel("Frequency: Weekly/Fortnight");
			fishLocation.setLabel("Location");
			placesFrom.setLabel("Places from where animals Arrive");
			placesTo.setLabel("Places from where animals are transported");
			transactions.setLabel("No of animals (average) transacted in a day");
			transactions.setPlaceholder("No of animals (average) transacted in a day");
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
		Button uploadButton=new Button("Select Image");
		//upload.setDropLabel(uploadButton);
		uploadButton.getStyle().set("font-size", "12px");
		upload.setUploadButton(uploadButton);
		//upload.setDropLabel(new Label("Drop Photo"));
		upload.setAcceptedFileTypes("image/tiff", "image/jpeg", "image/jpg");
		upload.addFileRejectedListener(e -> Notification.show("Invalid File: Please select only image files which are less than 1Mb",3000, Position.TOP_END));
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
