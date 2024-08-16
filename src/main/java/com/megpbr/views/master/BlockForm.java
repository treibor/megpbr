package com.megpbr.views.master;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.State;
import com.megpbr.data.service.Dbservice;
import com.megpbr.utils.ValidationUtil;
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


public class BlockForm extends Div {
	Dbservice dbservice;
	Block block;
	Binder<Block> binder = new BeanValidationBinder<>(Block.class);
	TextField blockCode = new TextField("Block Code");
	TextField blockName = new TextField("Block Name");
	public ComboBox<State> state = new ComboBox("State");
	public ComboBox<District> district = new ComboBox("District");
	//TextField state = new TextField("Block Name");
	FormLayout form = new FormLayout();
	public Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button newButton = new Button("New");
	//FormLayout form=new FormLayout();
	public BlockForm(Dbservice dbservice) {
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

	public void setBlock(Block block) {
		this.block=block;
		binder.readBean(block);
	}
	private void initForm() {
		state.setItems(dbservice.getStates());
		state.setItemLabelGenerator(state->state.getStateName());
		district.setItems(dbservice.getDistricts());
		district.setItemLabelGenerator(district->district.getDistrictName());
		binder.setBean(new Block());
	}
	private Component createButtons() {
		var hl = new HorizontalLayout();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(e -> saveBlock());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteBlock(block));
		newButton.addClickListener(e->newBlock());
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save,newButton,  delete);
		hl.setSizeFull();
		return hl;
	}

	public void newBlock() {
		setBlock(new Block());
	}
	private void saveBlock() {
		try {
			binder.writeBean(block);
			fireEvent(new SaveEvent(this, block));
			this.setBlock(new Block());
			//Notification.show("Added Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		} catch (ValidationException e) { // TODO
			Notification.show("Error. ").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}

	public void deleteBlock(Block block) {
		if (block == null) {
			Notification.show("Error. Please Select An Item To Delete").addThemeVariants(NotificationVariant.LUMO_ERROR);
		} else {
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event ->fireEvent(new DeleteEvent(this, block)));
			dialog.open();
		}
	}
	
	

	public void closeForm() {
		//this.setVisible(false);
	}

	private void clearForm() {
		this.block = new Block();
		
	}
	
	

	

	public Component createBasicForm() {
		ValidationUtil.applyValidation(blockName);
		ValidationUtil.applyValidation(blockCode);
		form.add(state, 2);
		form.add(district, 2);
		form.add(blockCode, 2);
		form.add(blockName, 2);
		
		form.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 2));
		return form;
	}

	
	
	

	public static abstract class BlockFormEvent extends ComponentEvent<BlockForm> {
		private Block block;

		protected BlockFormEvent(BlockForm source, Block block) {
			super(source, false);
			this.block = block;
		}

		public Block getBlock() {
			return block;
		}
	}

	public static class SaveEvent extends BlockFormEvent {
		SaveEvent(BlockForm source, Block block) {
			super(source, block);
		}
	}

	public static class DeleteEvent extends BlockFormEvent {
		DeleteEvent(BlockForm source, Block block) {
			super(source, block);
		}

	}

	public static class CloseEvent extends BlockFormEvent {
		CloseEvent(BlockForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
	
	
	
	
	
}
