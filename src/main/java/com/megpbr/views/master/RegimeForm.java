package com.megpbr.views.master;

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
import com.megpbr.data.entity.MasterCommercial;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.MasterGender;
import com.megpbr.data.entity.MasterLocallanguage;
import com.megpbr.data.entity.MasterManagementRegime;
import com.megpbr.data.entity.MasterManagementRegime;
import com.megpbr.data.entity.MasterSeason;
import com.megpbr.data.entity.MasterManagementRegime;
import com.megpbr.data.entity.MasterWildhome;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.utils.ValidationUtil;
import com.megpbr.views.CropPlantsForm.DeleteEvent;
import com.megpbr.views.CropPlantsForm.SaveEvent;
import com.megpbr.views.dashboard.DashboardView;

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


public class RegimeForm extends Div {
	Dbservice dbservice;
	MasterManagementRegime masterregime;
	Binder<MasterManagementRegime> binder = new BeanValidationBinder<>(MasterManagementRegime.class);
	TextField managerregime = new TextField("Regime Code");
	TextField managementregime = new TextField("Management Regime");
	FormLayout form = new FormLayout();
	public Button save = new Button("Add");
	Button delete = new Button("Delete");
	//FormLayout form=new FormLayout();
	public RegimeForm(Dbservice dbservice) {
		super();
		this.dbservice = dbservice;
		initForm();
		binder.bindInstanceFields(this);
		add(createForm());
		
	}

	public Component createForm() {
		VerticalLayout vl = new VerticalLayout();
		vl.add( createBasicForm(), createButtons());
		Scroller scroller = new Scroller(vl);
		scroller.setScrollDirection(ScrollDirection.VERTICAL);
		scroller.setSizeFull();
		return scroller;

	}

	private void initForm() {
		binder.setBean(new MasterManagementRegime());
	}
	private Component createButtons() {
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(e -> saveRegime());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteRegime(masterregime));
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save,  delete);
		hl.setSizeFull();
		return hl;
	}

	
	private void saveRegime() {
		try {
			binder.writeBean(masterregime);
			fireEvent(new SaveEvent(this, masterregime));
			this.setMasterManagementRegime(new MasterManagementRegime());
			Notification.show("Added Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		} catch (ValidationException e) { // TODO
			
		}

	}

	public void deleteRegime(MasterManagementRegime masterregime) {
		if (masterregime == null) {
			Notification.show("Error. Please Select An Item To Delete").addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event ->fireEvent(new DeleteEvent(this, masterregime)));
			dialog.open();
		}
	}
	
	public void setMasterManagementRegime(MasterManagementRegime masterregime) {
		this.masterregime = masterregime;
		binder.readBean(masterregime);
	}

	public void closeForm() {
		//this.setVisible(false);
	}

	private void clearForm() {
		this.masterregime = new MasterManagementRegime();
		
	}
	
	

	

	public Component createBasicForm() {
		ValidationUtil.applyValidation(managerregime);
		ValidationUtil.applyValidation(managementregime);
		form.add(managerregime, 2);
		form.add(managementregime, 2);
		form.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 2));
		return form;
	}

	
	
	

	public static abstract class MasterManagementRegimeFormEvent extends ComponentEvent<RegimeForm> {
		private MasterManagementRegime masterregime;

		protected MasterManagementRegimeFormEvent(RegimeForm source, MasterManagementRegime masterregime) {
			super(source, false);
			this.masterregime = masterregime;
		}

		public MasterManagementRegime getMasterManagementRegime() {
			return masterregime;
		}
	}

	public static class SaveEvent extends MasterManagementRegimeFormEvent {
		SaveEvent(RegimeForm source, MasterManagementRegime masterregime) {
			super(source, masterregime);
		}
	}

	public static class DeleteEvent extends MasterManagementRegimeFormEvent {
		DeleteEvent(RegimeForm source, MasterManagementRegime masterregime) {
			super(source, masterregime);
		}

	}

	public static class CloseEvent extends MasterManagementRegimeFormEvent {
		CloseEvent(RegimeForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
	
	
	
	
	
}
