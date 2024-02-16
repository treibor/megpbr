package com.megpbr.views.master;

import com.megpbr.data.entity.District;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.District;
import com.megpbr.data.service.Dbservice;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;


public class DistrictForm extends Div {
	Dbservice dbservice;
	District district;
	Binder<District> binder = new BeanValidationBinder<>(District.class);
	TextField districtCode = new TextField("District Code");
	TextField districtName = new TextField("District Name");
	public ComboBox<State> state = new ComboBox("State");
	//TextField state = new TextField("District Name");
	FormLayout form = new FormLayout();
	public Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button newButton = new Button("New");
	//FormLayout form=new FormLayout();
	public DistrictForm(Dbservice dbservice) {
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

	public void setDistrict(District district) {
		this.district=district;
		binder.readBean(district);
	}
	private void initForm() {
		state.setItems(dbservice.getStates());
		state.setItemLabelGenerator(state->state.getStateName());
		binder.setBean(new District());
	}
	private Component createButtons() {
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(e -> saveDistrict());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteDistrict(district));
		newButton.addClickListener(e->newDistrict());
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save,newButton,  delete);
		hl.setSizeFull();
		return hl;
	}

	public void newDistrict() {
		setDistrict(new District());
	}
	private void saveDistrict() {
		try {
			binder.writeBean(district);
			fireEvent(new SaveEvent(this, district));
			this.setDistrict(new District());
			//Notification.show("Added Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		} catch (ValidationException e) { // TODO
			Notification.show("Error. ").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}

	public void deleteDistrict(District district) {
		if (district == null) {
			Notification.show("Error. Please Select An Item To Delete").addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event ->fireEvent(new DeleteEvent(this, district)));
			dialog.open();
		}
	}
	
	

	public void closeForm() {
		//this.setVisible(false);
	}

	private void clearForm() {
		this.district = new District();
		
	}
	
	

	

	public Component createBasicForm() {
		form.add(state, 2);
		form.add(districtCode, 2);
		form.add(districtName, 2);
		form.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 2));
		return form;
	}

	
	
	

	public static abstract class DistrictFormEvent extends ComponentEvent<DistrictForm> {
		private District district;

		protected DistrictFormEvent(DistrictForm source, District district) {
			super(source, false);
			this.district = district;
		}

		public District getDistrict() {
			return district;
		}
	}

	public static class SaveEvent extends DistrictFormEvent {
		SaveEvent(DistrictForm source, District district) {
			super(source, district);
		}
	}

	public static class DeleteEvent extends DistrictFormEvent {
		DeleteEvent(DistrictForm source, District district) {
			super(source, district);
		}

	}

	public static class CloseEvent extends DistrictFormEvent {
		CloseEvent(DistrictForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
	
	
	
	
	
}
