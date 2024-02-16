package com.megpbr.views.master;

import com.megpbr.data.entity.State;
import com.megpbr.data.service.Dbservice;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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


public class StateForm extends Div {
	Dbservice dbservice;
	//State state;
	State state;
	Binder<State> binder = new BeanValidationBinder<>(State.class);
	TextField stateCode = new TextField("State Code");
	TextField stateName = new TextField("State Name");
	FormLayout form = new FormLayout();
	public Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button newButton = new Button("New");
	//FormLayout form=new FormLayout();
	public StateForm(Dbservice dbservice) {
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

	public void setState(State state) {
		this.state=state;
		binder.readBean(state);
	}
	private void initForm() {
		binder.setBean(new State());
	}
	private Component createButtons() {
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(e -> saveState());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteState(state));
		newButton.addClickListener(e->newState());
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save,newButton,  delete);
		hl.setSizeFull();
		return hl;
	}

	public void newState() {
		setState(new State());
	}
	private void saveState() {
		try {
			binder.writeBean(state);
			fireEvent(new SaveEvent(this, state));
			this.setState(new State());
			//Notification.show("Added Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		} catch (ValidationException e) { // TODO
			Notification.show("Error. ").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}

	public void deleteState(State state) {
		if (state == null) {
			Notification.show("Error. Please Select An Item To Delete").addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event ->fireEvent(new DeleteEvent(this, state)));
			dialog.open();
		}
	}
	
	

	public void closeForm() {
		//this.setVisible(false);
	}

	private void clearForm() {
		this.state = new State();
		
	}
	
	

	

	public Component createBasicForm() {
		form.add(stateCode, 2);
		form.add(stateName, 2);
		form.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 2));
		return form;
	}

	
	
	

	public static abstract class StateFormEvent extends ComponentEvent<StateForm> {
		private State state;

		protected StateFormEvent(StateForm source, State state) {
			super(source, false);
			this.state = state;
		}

		public State getState() {
			return state;
		}
	}

	public static class SaveEvent extends StateFormEvent {
		SaveEvent(StateForm source, State state) {
			super(source, state);
		}
	}

	public static class DeleteEvent extends StateFormEvent {
		DeleteEvent(StateForm source, State state) {
			super(source, state);
		}

	}

	public static class CloseEvent extends StateFormEvent {
		CloseEvent(StateForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
	
	
	
	
	
}
