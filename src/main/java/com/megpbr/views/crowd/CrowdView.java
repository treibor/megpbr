package com.megpbr.views.crowd;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.CrowdCategory;
import com.megpbr.data.entity.CrowdFormat;
import com.megpbr.data.entity.CrowdType;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterCategory;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Crowd;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.CrowdService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.views.CropPlantsForm;
import com.megpbr.views.MainLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import jakarta.annotation.security.RolesAllowed;
import software.xdev.vaadin.grid_exporter.GridExporter;

@RolesAllowed({"SUPERADMIN", "VERIFIER"})
@PageTitle("Crowd Sourcing Verify")
@Route(value = "crowdsourcingverify", layout = MainLayout.class)
public class CrowdView extends HorizontalLayout{
	@Autowired
	Audit audit;
	CrowdService service;
	Dbservice dbservice;
	CropService cservice;
	Crowd crowd;
	CropForm cropform;
	Grid<Crowd> grid=new Grid<>(Crowd.class);
	Binder<Crowd> binder = new BeanValidationBinder<>(Crowd.class);
	ComboBox<State> state = new ComboBox("");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	ComboBox<MasterCategory> mastercategory = new ComboBox("Category");
	ComboBox<MasterFormat> masterformat = new ComboBox("Format");
	TextField localName=new TextField("Local Name");
	TextField localLanguage=new TextField("Local Language");
	TextField season=new TextField("Season");
	TextField uses=new TextField("Uses");
	FormLayout form=new FormLayout();
	Button verifyButton =new Button("Verify");
	Button deleteButton =new Button("Delete");
	VerticalLayout contents = new VerticalLayout();
	Button expButton = new Button("Export");
	public CrowdView(CrowdService service, Dbservice dbservice, CropService cservice) {
		this.service=service;
		this.dbservice=dbservice;
		this.cservice=cservice;
		binder.bindInstanceFields(this);
		initFields();
		ConfigureForm();
		setSizeFull();
		add(getContent());
	}
	
	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(getMainContent(), cropform);
		// Div content = new Div(getMainContent(), form);
		content.setFlexGrow(2, contents);
		content.setFlexGrow(1, cropform);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}
	public Component getMainContent() {
		HorizontalLayout rightLayout=new HorizontalLayout();
		rightLayout.add( createForm2());
		rightLayout.setWidthFull();
		contents.add(getGrid(), rightLayout);
		contents.setSizeFull();
		return contents;
	}
	
	public void setCrowd(Crowd crowd) {
		this.crowd = crowd;
		binder.readBean(crowd);
	}
	private void ConfigureForm() {
		cropform = new CropForm(dbservice, cservice);
		cropform.setVisible(false);
		cropform.setWidth("40%");
		cropform.addListener(CropForm.SaveEvent.class, this::saveCrops);
		cropform.addListener(CropForm.DeleteEvent.class, this::deleteCrops);

	}
	public void saveCrops(CropForm.SaveEvent event) {
		try {
			//System.out.println("A");
			Crops crop = event.getCrops();
			crop.setEnteredOn(LocalDateTime.now());
			crop.setFormat(masterformat.getValue());
			crop.setEnteredBy(dbservice.getLoggedUser());
			dbservice.updateCrop(crop);
			crowd.setVerified(true);
			service.saveCrowd(crowd);
			audit.saveAudit("Crowd Data - Verify", "Id-"+crowd.getId()+"-Village"+crowd.getVillage().getVillageName());
			Notification.show("Saved Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			updateGrid();
			cropform.setVisible(false);
			//addCrops(new Crops());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :" + e);
		}
	}
	public void updateGrid() {
		grid.setItems(service.getVerifiedCrowd());
	}
	public void deleteCrops(CropForm.DeleteEvent event) {

		try {
			Crops crop = event.getCrops();
			audit.saveAudit("Crowd Data - Delete", "Id-"+crowd.getId()+"-Village"+crowd.getVillage().getVillageName());
			cservice.deleteCrop(crop);
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			//updateGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Error Encountered :" + e);
		}
	}
	public void initFields() {
		verifyButton.setEnabled(false);
		deleteButton.setEnabled(false);
		state.setItems(dbservice.getStates());
		state.setValue(dbservice.getLoggedUser().getState());
		district.setItems(dbservice.getDistricts(state.getValue()));
		state.setPlaceholder("State");
		district.setItemLabelGenerator(district -> district.getDistrictName());
		district.setPlaceholder("District");
		district.addValueChangeListener(e -> block.setItems(dbservice.getBlocks(e.getValue())));
		block.setItemLabelGenerator(block -> block.getBlockName());
		block.setPlaceholder("Block");
		block.addValueChangeListener(e -> village.setItems(dbservice.getVillages(e.getValue(), true)));
		village.setItems(dbservice.getVillages(block.getValue(), true));
		village.setItemLabelGenerator(Village::getVillageName);
		village.setPlaceholder("Village");
		state.setClearButtonVisible(true);
		district.setClearButtonVisible(true);
		block.setClearButtonVisible(true);
		mastercategory.setItems(dbservice.getCategory());
		mastercategory.addValueChangeListener(e->
		masterformat.setItems(dbservice.getFormatByCategory(e.getValue())));
		mastercategory.setItemLabelGenerator(mastercategory->mastercategory.getCategory());
		masterformat.setItemLabelGenerator(masterformat->masterformat.getFormatName());
	}
	public Component getGrid() {
		grid.removeAllColumns();
		grid.addColumn(crowd -> crowd.getVillage() == null ? "": crowd.getVillage().getBlock().getDistrict().getDistrictName())
		.setHeader("District").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn(crowd -> crowd.getVillage() == null ? "": crowd.getVillage().getBlock().getBlockName())
		.setHeader("Block").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn(crowd -> crowd.getVillage() == null ? "": crowd.getVillage().getVillageName())
		.setHeader("Village").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn("localName").setHeader("Local Name").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn(crowd -> crowd.getCrowdformat() == null ? "": crowd.getCrowdformat().getFormatName()).setHeader("Type").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn(crowd -> crowd.getType() == null ? "": crowd.getType().getTypeName()).setHeader("Wild?").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn("localLanguage").setHeader("Local Language").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn("latitude").setHeader("Latitude").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn("longitude").setHeader("longitude").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn("uses").setHeader("Uses").setSortable(true).setAutoWidth(true).setResizable(true);
		//grid.addColumn("photo1").setRenderer(new ImageRenderer()).setHeader("Photo1").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn(crowd->crowd.isVerified()==true? "Yes": "No").setHeader("Verified").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn(LitRenderer.<Crowd>of("<div><img style='height: 80px; width: 80px;' src=${item.imagedata} alt=${item.name}></div>").withProperty("imagedata", item -> getImageAsBase64(item.getPhoto1()))).setHeader("Photo1");
		grid.addColumn(LitRenderer.<Crowd>of("<div><img style='height: 80px; width: 80px;' src=${item.imagedata} alt=${item.name}></div>").withProperty("imagedata", item -> getImageAsBase64(item.getPhoto2()))).setHeader("Photo2");
		grid.addColumn(LitRenderer.<Crowd>of("<div><img style='height: 80px; width: 80px;' src=${item.imagedata} alt=${item.name}></div>").withProperty("imagedata", item -> getImageAsBase64(item.getPhoto3()))).setHeader("Photo3");
		grid.addColumn(LitRenderer.<Crowd>of("<div><img style='height: 80px; width: 80px;' src=${item.imagedata} alt=${item.name}></div>").withProperty("imagedata", item -> getImageAsBase64(item.getPhoto4()))).setHeader("Photo4");
		grid.setItems(service.getVerifiedCrowd());
		//HorizontalLayout hl1=new HorizontalLayout();
		grid.setSizeFull();
		grid.asSingleSelect().addValueChangeListener(e -> editCrowd(e.getValue()));
		return grid;
	}
	 private String getImageAsBase64(byte[] string) {
	        String mimeType = "image/png";
	        String htmlValue = null;
	        if (string == null) htmlValue = "a"; else htmlValue =
	            "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(string);
	        return htmlValue;
	    }
	public void editCrowd(Crowd crowd) {
		cropform.setVisible(false);
		if (crowd != null) {
			setCrowd(crowd);
			verifyButton.setEnabled(true);
			deleteButton.setEnabled(true);
			
		}else {
			verifyButton.setEnabled(false);
			deleteButton.setEnabled(false);
			
		}
	}
	public Component createForm() {
		form.add(localName, 2);
		form.add(localLanguage, 2);
		//form.add(category, 2);
		//form.add(format, 2);
		//form.add(type, 2);
		form.add(uses, 2);
		//form.add(state, 2);
		form.add(district, 2);
		form.add(block, 2);
		form.add(village, 2);
		form.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 4));
		//form.setWidth("40%");
		return form;
	}
	public Component createForm2() {
		FormLayout form1=new FormLayout();
		form1.add(mastercategory, 3);
		form1.add(masterformat, 3);
		form1.add(verifyButton, 1);
		form1.add(deleteButton, 1);
		form1.add(expButton, 1);
		form1.setResponsiveSteps(new ResponsiveStep("0", 3),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("500px", 9));
		//form.setWidth("40%");
		verifyButton.addClickListener(e->verifyCrowd());
		deleteButton.addClickListener(e->confirmDelete());
		expButton.addClickListener(e -> GridExporter.newWithDefaults(grid).open());
		form1.setSizeFull();
		return form1;
	}
	
	
	private void confirmDelete() {
		if(crowd==null) {
			Notification.show("Please Select an Item to Delete").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}else {
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setHeader("Delete??");
			dialog.setText("Are You sure you want to delete this item? You will not be able to undo this action.");
			dialog.setCancelable(true);
			dialog.addCancelListener(event -> dialog.close());
			dialog.setRejectable(true);
			dialog.setRejectText("No");
			dialog.addRejectListener(event -> dialog.close());
			dialog.setConfirmText("Delete");
			dialog.addConfirmListener(event ->deleteCrowd());
			dialog.open();
		}
	}
	public void verifyCrowd() {
		if(crowd==null) {
			Notification.show("Please Select an Item to Process").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}else if (mastercategory.getValue()==null|| masterformat.getValue()==null){
			Notification.show("Please Select the Category and Format").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}else {
			cropform.setCrop(new Crops());
			cropform.initFields(masterformat.getValue());
			cropform.state.setValue(crowd.getVillage().getBlock().getDistrict().getState());
			cropform.district.setValue(crowd.getVillage().getBlock().getDistrict());
			cropform.block.setValue(crowd.getVillage().getBlock());
			cropform.village.setValue(crowd.getVillage());
			cropform.localName.setValue(crowd.getLocalName());
			cropform.localLanguage.setValue(crowd.getLocalLanguage());
			cropform.latitude.setValue(crowd.getLatitude());
			cropform.longitude.setValue(crowd.getLongitude());
			cropform.fruitSeason.setValue(crowd.getSeason());
			cropform.uses.setValue(crowd.getUses());
			byte[] picture1 = crowd.getPhoto1();
			byte[] picture2 = crowd.getPhoto2();
			byte[] picture3 = crowd.getPhoto3();
			byte[] picture4 = crowd.getPhoto4();
			if (picture1 != null) {
				StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(picture1));
				Image image = new Image(resource, "No Image");
				cropform.addImage(cropform.imageLayout1, image, cropform.imageContainer1);
			} else {
				cropform.removeImage(cropform.imageLayout1, cropform.imageContainer1);
			}
			if (picture2 != null) {
				StreamResource resource2 = new StreamResource("image.jpg", () -> new ByteArrayInputStream(picture2));
				Image image2 = new Image(resource2, "No Image");
				cropform.addImage(cropform.imageLayout2, image2, cropform.imageContainer2);
			} else {
				cropform.removeImage(cropform.imageLayout2, cropform.imageContainer2);
			}
			if (picture3 != null) {
				StreamResource resource3 = new StreamResource("image.jpg", () -> new ByteArrayInputStream(picture3));
				Image image3 = new Image(resource3, "No Image");
				cropform.addImage(cropform.imageLayout3, image3, cropform.imageContainer3);
			} else {
				cropform.removeImage(cropform.imageLayout3, cropform.imageContainer3);
			}
			if (picture4 != null) {
				StreamResource resource4 = new StreamResource("image.jpg", () -> new ByteArrayInputStream(picture4));
				Image image4 = new Image(resource4, "No Image");
				cropform.addImage(cropform.imageLayout4, image4, cropform.imageContainer4);
			} else {
				cropform.removeImage(cropform.imageLayout4, cropform.imageContainer4);
			}
			cropform.setVisible(true);
		}
	}
	
	private void deleteCrowd() {
		// TODO Auto-generated method stub
		service.deleteCrowd(crowd);
		Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		updateGrid();
	}
	
}
