package com.megpbr.views.crowd;

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
import com.megpbr.data.entity.pbr.Crowd;
import com.megpbr.data.service.CrowdService;
import com.megpbr.data.service.Dbservice;
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


public class CrowdForm extends Div {
	Dbservice dbservice;
	CrowdService cservice;
	Crowd crowd;
	Binder<Crowd> binder = new BeanValidationBinder<>(Crowd.class);
	public ComboBox<State> state = new ComboBox("");
	public ComboBox<District> district = new ComboBox("");
	public ComboBox<Block> block = new ComboBox("");
	public ComboBox<Village> village = new ComboBox();
	FormLayout formbasic = new FormLayout();
	public FormLayout frommaster = new FormLayout();
	TextField latitude = new TextField("Latitude");
	TextField longitude = new TextField("Longitude");
	public Button save = new Button("Save");
	Button cancel = new Button("Close");
	boolean isSuperAdmin;

		// boolean isAdmin;
	public CrowdForm(Dbservice dbservice, CrowdService cservice) {
		
		this.dbservice = dbservice;
		this.cservice = cservice;
		addClassName("crowdplants-view");
		initForm();
		binder.bindInstanceFields(this);
		//add(new VerticalLayout(createMasterForm(), createButtons(), createMap()));
		add(new VerticalLayout(createMasterForm(), createButtons()));
		this.setHeightFull();
	}

	private Component createButtons() {
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		cancel.addClickShortcut(Key.ESCAPE);
		cancel.addClickListener(e -> closeForm());
		save.addClickListener(e -> validatecrowd());
		Button delete=new Button("Delete");
		delete.addClickListener(e-> deletecrowd(crowd));
		// delete.setVisible(getAdmin());
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save, cancel, delete);
		hl.setSizeFull();
		return hl;
	}
	public void initFields() {
		state.setItems(dbservice.getStates());
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
		
	}
	
	private void validatecrowd() {

		if (village.getValue() == null || village.getValue().equals(null)) {
			Notification.show("Error. Please Select a Village").addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			savecrowdAfterValidation();
		}

	}

	private void savecrowdAfterValidation() {
		try {
			
			binder.writeBean(crowd);
			fireEvent(new SaveEvent(this, crowd));
		
		} catch (ValidationException e) {
			Notification.show("Error. Please Check").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	public void deletecrowd(Crowd crowd) {
		ConfirmDialog dialog = new ConfirmDialog();
		if (crowd == null || crowd.equals(null) ) {
			Notification.show("Error. Please Select An Item To Delete")
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item? You will not be able to undo this action.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.setRejectable(true);
			dialog.setRejectText("No");
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event -> fireEvent(new DeleteEvent(this, crowd)));
			dialog.open();
		}
	}

	public void setCrowd(Crowd crowd) {
		this.crowd = crowd;
		binder.readBean(crowd);
	}

	public void closeForm() {
		this.setVisible(false);
	}

	private void clearForm(MasterFormat format) {
		this.crowd = new Crowd();
		//Crowd.setFormat(format);

	}

	private void initForm() {
		binder.setBean(new Crowd());
	}

	

	

	

	private Component createMap() {
		
		
			return null;
			
		
	}
	public Component createMasterForm() {
		// state.setVisible(isSuperAdmin);
		state.setPlaceholder("State");
		district.setPlaceholder("District");
		block.setPlaceholder("Block");
		village.setPlaceholder("Village");
		frommaster.add(state, 1);
		frommaster.add(district, 1);
		frommaster.add(block, 1);
		frommaster.add(village, 1);
		frommaster.add(latitude, 1);
		frommaster.add(longitude, 1);
		frommaster.setResponsiveSteps(new ResponsiveStep("0", 1),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 3));
		// frommaster.setSizeFull();
		return frommaster;
	}
	public static abstract class CrowdFormEvent extends ComponentEvent<CrowdForm> {
		private Crowd crowd;

		protected CrowdFormEvent(CrowdForm source, Crowd crowd) {
			super(source, false);
			this.crowd = crowd;
		}

		public Crowd getCrowd() {
			return crowd;
		}
	}

	public static class SaveEvent extends CrowdFormEvent {
		SaveEvent(CrowdForm source, Crowd crowd) {
			super(source, crowd);

		}
	}

	public static class DeleteEvent extends CrowdFormEvent {
		DeleteEvent(CrowdForm source, Crowd crowd) {
			super(source, crowd);
		}

	}

	public static class CloseEvent extends CrowdFormEvent {
		CloseEvent(CrowdForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

	
}
