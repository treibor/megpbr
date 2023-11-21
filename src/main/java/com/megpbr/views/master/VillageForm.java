package com.megpbr.views.master;

import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
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


public class VillageForm extends Div {
	Dbservice dbservice;
	Village village;
	Binder<Village> binder = new BeanValidationBinder<>(Village.class);
	TextField villageCode = new TextField("Village Code");
	TextField villageName = new TextField("Village Name");
	public ComboBox<State> state = new ComboBox("State");
	public ComboBox<District> district = new ComboBox("District");
	public ComboBox<Block> block = new ComboBox("Block");
	//TextField state = new TextField("Village Name");
	FormLayout form = new FormLayout();
	public Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button newButton = new Button("New");
	//FormLayout form=new FormLayout();
	public VillageForm(Dbservice dbservice) {
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

	public void setVillage(Village village) {
		this.village=village;
		binder.readBean(village);
	}
	private void initForm() {
		state.setItems(dbservice.getStates());
		state.setItemLabelGenerator(state->state.getStateName());
		district.setItems(dbservice.getDistricts());
		district.setItemLabelGenerator(district->district.getDistrictName());
		block.setItems(dbservice.getBlocks());
		block.setItemLabelGenerator(block->block.getBlockName());
		binder.setBean(new Village());
		state.addValueChangeListener(e->district.setItems(dbservice.getDistricts(state.getValue())));
		district.addValueChangeListener(e->block.setItems(dbservice.getBlocks(district.getValue())));
		//block.addValueChangeListener(e->block.setItems(dbservice.getBlocks(e.getValue())));
	}
	private Component createButtons() {
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(e -> saveVillage());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteVillage(village));
		newButton.addClickListener(e->newVillage());
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save,newButton,  delete);
		hl.setSizeFull();
		return hl;
	}

	public void newVillage() {
		setVillage(new Village());
	}
	private void saveVillage() {
		try {
			binder.writeBean(village);
			fireEvent(new SaveEvent(this, village));
			this.setVillage(new Village());
			//Notification.show("Added Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		} catch (ValidationException e) { // TODO
			//e.printStackTrace();
		}

	}

	public void deleteVillage(Village village) {
		if (village == null) {
			Notification.show("Error. Please Select An Item To Delete").addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event ->fireEvent(new DeleteEvent(this, village)));
			dialog.open();
		}
	}
	
	

	public void closeForm() {
		//this.setVisible(false);
	}

	private void clearForm() {
		this.village = new Village();
		
	}
	
	

	

	public Component createBasicForm() {
		form.add(state, 2);
		form.add(district, 2);
		form.add(block, 2);
		form.add(villageCode, 2);
		form.add(villageName, 2);
		form.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 2));
		return form;
	}

	
	
	

	public static abstract class VillageFormEvent extends ComponentEvent<VillageForm> {
		private Village village;

		protected VillageFormEvent(VillageForm source, Village village) {
			super(source, false);
			this.village = village;
		}

		public Village getVillage() {
			return village;
		}
	}

	public static class SaveEvent extends VillageFormEvent {
		SaveEvent(VillageForm source, Village village) {
			super(source, village);
		}
	}

	public static class DeleteEvent extends VillageFormEvent {
		DeleteEvent(VillageForm source, Village village) {
			super(source, village);
		}

	}

	public static class CloseEvent extends VillageFormEvent {
		CloseEvent(VillageForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
	
	
	
	
	
}
