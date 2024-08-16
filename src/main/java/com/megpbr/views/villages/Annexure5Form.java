package com.megpbr.views.villages;

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
import com.megpbr.data.entity.MasterPosition;
import com.megpbr.data.entity.MasterSeason;
import com.megpbr.data.entity.MasterStatus;
import com.megpbr.data.entity.MasterWildhome;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.villages.VillageAnnexure5;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.utils.ValidationUtil;
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

import elemental.json.Json;

public class Annexure5Form extends Div {
	Dbservice dbservice;
	VillageAnnexure5 annexure5;
	VillageView villageview;
	Binder<VillageAnnexure5> binder = new BeanValidationBinder<>(VillageAnnexure5.class);
	public Village village;
	TextField name = new TextField("Name of the Person");
	IntegerField quantity=new IntegerField("Quantity");
	TextArea address=new TextArea("Address");
	//TextArea specialization=new TextArea("Area of Specialization");
	TextField localName = new TextField("Local Name ");
	TextField scientificName = new TextField("Scientific Name");
	DatePicker resolutionDate=new DatePicker("Resolution Date");
	TextField feeCollection = new TextField("Fee Collection Details");
	TextField anticipatedMode = new TextField("Anticipated Mode of Sharing Benefits");
	FormLayout formbasic = new FormLayout();
	MemoryBuffer buffer1 = new MemoryBuffer();
	MemoryBuffer buffer2 = new MemoryBuffer();
	Upload feeCollectionPhoto=new Upload(buffer1);
	Upload resolutionPhoto=new Upload(buffer2);
	public Button save = new Button("Add");
	Button delete = new Button("Delete");
	boolean isSuperAdmin;
	
	
	public Annexure5Form(Dbservice dbservice) {
		super();
		this.setHeightFull();
		this.setWidth("60%");
		this.dbservice = dbservice;
		binder.bindInstanceFields(this);
		resolutionPhoto.setDropLabel(new H6("Resolution/Endorsement"));
		feeCollectionPhoto.setDropLabel(new H6("Details of Collection Fee"));
		add(createAccordion());
		//isSuperAdmin = dbservice.isSuperAdmin();
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
		save.addClickListener(e -> saveVillageAnnexure5());
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.addClickListener(e -> deleteVillageDetail(annexure5));
		hl.setJustifyContentMode(JustifyContentMode.CENTER);
		hl.add(save,  delete);
		hl.setSizeFull();
		return hl;
	}

	
	private void saveVillageAnnexure5() {

		try {
			binder.writeBean(annexure5);
			annexure5.setVillage(village);
			if (getImageAsByteArray(buffer1) != null) {
				annexure5.setResolutionPhoto(getImageAsByteArray(buffer1));
			}
			if (getImageAsByteArray(buffer2) != null) {
				annexure5.setFeeCollectionPhoto(getImageAsByteArray(buffer2));
			}
			fireEvent(new SaveEvent(this, annexure5));
			this.setVillageAnnexure5(new VillageAnnexure5());
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			//initFields();
			buffer1=new MemoryBuffer();
			buffer2=new MemoryBuffer();
			resolutionPhoto.setReceiver(buffer1);
			feeCollectionPhoto.setReceiver(buffer2);
			try {
				resolutionPhoto.clearFileList();
				feeCollectionPhoto.clearFileList();
			}catch(Exception e) {
				
			}
		} catch (ValidationException e) { // TODO
			
		}

	}

	public void deleteVillageDetail(VillageAnnexure5 villagedetail) {
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
	
	public void setVillageAnnexure5(VillageAnnexure5 annexure5) {
		this.annexure5 = annexure5;
		binder.readBean(annexure5);
	}

	public void closeForm() {
		//this.setVisible(false);
	}

	private void clearForm(MasterFormat format) {
		this.annexure5 = new VillageAnnexure5();
		//annexure5.setFormat(format);
		
	}
	
	
	

	public Component createBasicForm() {
		name.setTooltipText("Name of the Person");
		localName.setTooltipText("Local Name of the biological material accessed");
		scientificName.setTooltipText("Scientific Name of the biological material accessed");
		quantity.setTooltipText("Quantity of the biological material accessed");
		anticipatedMode.setTooltipText("Anticipated mode of sharing benefits or quantum of benefits shared");
		feeCollection.setTooltipText("Details of collection fee imposed");
		resolutionDate.setTooltipText("Date and resolution of the BMC and endorsement by the village");
		//resolutionPhoto.setDropLabel("ABC");
		//feeCollectionPhoto
		ValidationUtil.applyTextAreaValidation(address);
		ValidationUtil.applyValidation(name);
		ValidationUtil.applyValidation(anticipatedMode);
		ValidationUtil.applyValidation(feeCollection);
		ValidationUtil.applyValidation(localName);
		ValidationUtil.applyValidation(scientificName);
		formbasic.add(name, 2);
		formbasic.add(localName, 2);
		formbasic.add(scientificName, 2);
		formbasic.add(quantity, 2);
		formbasic.add(address, 4);
		formbasic.add(anticipatedMode, 2);
		formbasic.add(resolutionDate, 2);
		formbasic.add(feeCollection, 2);
		formbasic.add(new Div(), 2);
		formbasic.add(createUpload(resolutionPhoto), 2);
		formbasic.add(createUpload(feeCollectionPhoto), 2);
		
		//formbasic.add(otherDetails, 3);
		//formbasic.add(associatedTdk, 3);
		formbasic.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 4));
		// formbasic.setSizeFull();
		return formbasic;

	}

	
	
	

	public static abstract class VillageAnnexure5FormEvent extends ComponentEvent<Annexure5Form> {
		private VillageAnnexure5 annexure5;

		protected VillageAnnexure5FormEvent(Annexure5Form source, VillageAnnexure5 annexure5) {
			super(source, false);
			this.annexure5 = annexure5;
		}

		public VillageAnnexure5 getVillageAnnexure5() {
			return annexure5;
		}
	}

	public static class SaveEvent extends VillageAnnexure5FormEvent {
		SaveEvent(Annexure5Form source, VillageAnnexure5 annexure5) {
			super(source, annexure5);
		}
	}

	public static class DeleteEvent extends VillageAnnexure5FormEvent {
		DeleteEvent(Annexure5Form source, VillageAnnexure5 annexure5) {
			super(source, annexure5);
		}

	}

	public static class CloseEvent extends VillageAnnexure5FormEvent {
		CloseEvent(Annexure5Form source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
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
		BufferedImage resizedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, 200, 200, null);
		graphics2D.dispose();
		return resizedImage;
	}
	
	
	
}
