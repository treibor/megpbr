package com.application.megpbr.views.villages;

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
import com.application.megpbr.data.entity.MasterGender;
import com.application.megpbr.data.entity.MasterManagementRegime;
import com.application.megpbr.data.entity.MasterPosition;
import com.application.megpbr.data.entity.MasterSeason;
import com.application.megpbr.data.entity.MasterStatus;
import com.application.megpbr.data.entity.MasterWildhome;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;
import com.application.megpbr.data.entity.villages.VillageAnnexure3;
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

public class Annexure3Form extends Div {
	Dbservice dbservice;
	VillageAnnexure3 annexure3;
	VillageView villageview;
	Binder<VillageAnnexure3> binder = new BeanValidationBinder<>(VillageAnnexure3.class);
	public Village village;
	TextField name = new TextField("Name");
	TextField age = new TextField("Age");
	//DatePicker tenureDate=new DatePicker("Tenure Date");
	TextArea address=new TextArea("Address");
	TextArea specialization=new TextArea("Area of Specialization");
	ComboBox<MasterGender> gender = new ComboBox("Gender");
	//ComboBox<MasterPosition> position = new ComboBox("Position");
	FormLayout formbasic = new FormLayout();
	FormLayout frommaster = new FormLayout();
	public Button save = new Button("Add");
	Button delete = new Button("Delete");
	boolean isSuperAdmin;
	
	
	public Annexure3Form(Dbservice dbservice) {
		super();
		this.setHeightFull();
		this.setWidth("60%");
		this.dbservice = dbservice;
		initForm();
		binder.bindInstanceFields(this);
		add(createAccordion());
		
	}

	public Component createAccordion() {
		VerticalLayout vl = new VerticalLayout();
		vl.add( createBasicForm(), createButtons());
		Scroller scroller = new Scroller(vl);
		scroller.setScrollDirection(ScrollDirection.VERTICAL);
		scroller.setSizeFull();
		return scroller;

	}

	
	private Component createButtons() {
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(e -> saveVillageAnnexure3());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteVillageDetail(annexure3));
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save,  delete);
		hl.setSizeFull();
		return hl;
	}

	
	private void saveVillageAnnexure3() {

		try {
			binder.writeBean(annexure3);
			annexure3.setVillage(village);
			fireEvent(new SaveEvent(this, annexure3));
			this.setVillageAnnexure3(new VillageAnnexure3());
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			//initFields();
		} catch (ValidationException e) { // TODO
			e.printStackTrace();
		}

	}

	public void deleteVillageDetail(VillageAnnexure3 villagedetail) {
		if (villagedetail.getVillage() == null) {
			Notification.show("Error. Please Select An Item To Delete")
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			// dialog.setRejectable(true);
			// dialog.setRejectText("Cancel");
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event -> fireEvent(new DeleteEvent(this, villagedetail)));
			dialog.open();
		}
	}
	
	public void setVillageAnnexure3(VillageAnnexure3 annexure3) {
		this.annexure3 = annexure3;
		binder.readBean(annexure3);
	}

	public void closeForm() {
		//this.setVisible(false);
	}

	private void clearForm(MasterFormat format) {
		this.annexure3 = new VillageAnnexure3();
		//annexure3.setFormat(format);
		
	}
	
	private void initForm() {
		//binder.setBean(new Crops());
		gender.setItems(dbservice.getGenders());
		gender.setItemLabelGenerator(gender->gender.getGenderName());
		
	}

	

	public Component createBasicForm() {
		formbasic.add(name, 2);
		formbasic.add(age, 1);
		formbasic.add(gender, 1);
		formbasic.add(address, 4);
		formbasic.add(specialization, 4);
		//formbasic.add(otherDetails, 3);
		//formbasic.add(associatedTdk, 3);
		formbasic.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 4));
		// formbasic.setSizeFull();
		return formbasic;

	}

	
	
	

	public static abstract class VillageAnnexure3FormEvent extends ComponentEvent<Annexure3Form> {
		private VillageAnnexure3 annexure3;

		protected VillageAnnexure3FormEvent(Annexure3Form source, VillageAnnexure3 annexure3) {
			super(source, false);
			this.annexure3 = annexure3;
		}

		public VillageAnnexure3 getVillageAnnexure3() {
			return annexure3;
		}
	}

	public static class SaveEvent extends VillageAnnexure3FormEvent {
		SaveEvent(Annexure3Form source, VillageAnnexure3 annexure3) {
			super(source, annexure3);
		}
	}

	public static class DeleteEvent extends VillageAnnexure3FormEvent {
		DeleteEvent(Annexure3Form source, VillageAnnexure3 annexure3) {
			super(source, annexure3);
		}

	}

	public static class CloseEvent extends VillageAnnexure3FormEvent {
		CloseEvent(Annexure3Form source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
	
	
	
	
	
}
