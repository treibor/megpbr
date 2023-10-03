package com.megpbr.views;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.megpbr.views.CropPlantsForm.DeleteEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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

import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ADMIN", "VERIFIER"})
@PageTitle("Crowd Sourcing")
@Route(value = "crowdsourcing", layout = MainLayout.class)
public class CrowdView extends HorizontalLayout{
	CrowdService service;
	Dbservice dbservice;
	Crowd crowd;
	Grid<Crowd> grid=new Grid<>(Crowd.class);
	Binder<Crowd> binder = new BeanValidationBinder<>(Crowd.class);
	ComboBox<State> state = new ComboBox("");
	ComboBox<District> district = new ComboBox("");
	ComboBox<Block> block = new ComboBox("");
	ComboBox<Village> village = new ComboBox();
	ComboBox<CrowdCategory> category = new ComboBox("Item");
	ComboBox<CrowdFormat> format = new ComboBox("Item Type");
	ComboBox<CrowdType> type = new ComboBox("Wild");
	ComboBox<MasterCategory> mastercategory = new ComboBox("Category");
	ComboBox<MasterFormat> masterformat = new ComboBox("Format");
	TextField localName=new TextField("Local Name");
	TextField localLanguage=new TextField("Local Language");
	TextField season=new TextField("Season");
	TextField uses=new TextField("Uses");
	FormLayout form=new FormLayout();
	Button verifyButton =new Button("Verify");
	Button deleteButton =new Button("Delete");
	public CrowdView(CrowdService service, Dbservice dbservice) {
		this.service=service;
		this.dbservice=dbservice;
		binder.bindInstanceFields(this);
		initFields();
		setSizeFull();
		VerticalLayout rightLayout=new VerticalLayout(createForm(), createForm2(), getButtons());
		rightLayout.setWidth("40%");
		add(getGrid(), rightLayout);
	}
	public void setCrowd(Crowd crowd) {
		this.crowd = crowd;
		binder.readBean(crowd);
	}
	public void initFields() {
		verifyButton.setEnabled(false);
		deleteButton.setEnabled(false);
		category.setItems(service.getCategory());
		category.setItemLabelGenerator(category-> category.getCategory());
		category.addValueChangeListener(e->	populateComboBox());
		format.setItemLabelGenerator(format->format.getFormatName());
		type.setItemLabelGenerator(type->type.getTypeName());
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
			masterformat.setItems(dbservice.getFormatByCategory(e.getValue()))
		);
		mastercategory.setItemLabelGenerator(mastercategory->mastercategory.getCategory());
		masterformat.setItemLabelGenerator(masterformat->masterformat.getFormatName());
	}
	private void populateComboBox() {
		// TODO Auto-generated method stub
		format.setItems(service.getFormatByCategory(category.getValue()));
		type.setItems(service.getTypeByCategory(category.getValue()));
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
		grid.addColumn("verified").setHeader("Verified").setSortable(true).setAutoWidth(true).setResizable(true);
		grid.addColumn(
	            LitRenderer
	                .<Crowd>of(
	                    "<div><img style='height: 80px; width: 80px;' src=${item.imagedata} alt=${item.name}></div>"
	                )
	                .withProperty("imagedata", item -> getImageAsBase64(item.getPhoto1()))
	        );
		grid.setItems(service.getCrowd());
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
		if (crowd != null) {
			this.category.setValue(crowd.getCrowdformat().getCategory());
			this.format.setValue(crowd.getCrowdformat());
			this.district.setValue(crowd.getVillage().getBlock().getDistrict());
			this.block.setValue(crowd.getVillage().getBlock());
			this.setCrowd(crowd);
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
		form.add(category, 2);
		form.add(format, 2);
		form.add(type, 2);
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
		form1.add(mastercategory, 2);
		form1.add(masterformat, 2);
		form1.setResponsiveSteps(new ResponsiveStep("0", 2),
				// Use two columns, if layout's width exceeds 500px
				new ResponsiveStep("300px", 4));
		//form.setWidth("40%");
		return form1;
	}
	
	private Component getButtons() {
		
		deleteButton.addClickListener(e->confirmDelete());
		verifyButton.addClickListener(e->verifyCrowd());
		HorizontalLayout hlz=new HorizontalLayout(verifyButton, deleteButton);
		hlz.setWidthFull();
		hlz.setJustifyContentMode(JustifyContentMode.CENTER);
		return hlz;
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
			//do something
		}
	}
	private void convertImage() {
		// TODO Auto-generated method stub
		
		try {
			List<Crowd> abd=service.getCrowd();
			String ytr="UklGRswZAABXRUJQVlA4IMAZAAAQpQCdASoGAgYCPjEYikQiIaEQijU4IAMEtLd8HPT+mj9bsh0w\r\n"
					+ "A//8fdJ+o8o8P8F/L/6Z4X/zn+hfjB+5/rH4efF3sv/cP/X9tN1fq1/sfQv+Q/X36//b/2U/un7J\r\n"
					+ "/jX+h/sv5I/ib7U/GHwA+Aj8n/k/9r/sf7MfIy9Mw5+oF6lfN/8d/dv3s/zfywfLf6z0H+rX+j9w\r\n"
					+ "D+M/yr++fmJ/mf//9eeBL9t/33sCfzH+u/9T/A/lH9Rv+N/5f83/jf3C9230r/z/9B8CP8x/rH+1\r\n"
					+ "/wX70f4f//+Nb90vapE08CXek+ZGQbDG3TMOHX84kmZkDOjvKrO6Tq6awRRse3rb7EgSdRhJeJBG\r\n"
					+ "O43IQaxaT5yvebZ3ZM2Pq+JGVXQ5LA4E1h3SXmpFIQ+yXB2GBJbVXL4HXS9zXGfoAXVZt+tHuZv3\r\n"
					+ "6zhfYqxVVU3P47vxfvvwKiXvRYqrLDX6aqDpe8Xu5z6aAb9/Q04hrOdL3i93G3w98lIG4/B+qZjv\r\n"
					+ "mc5PJ7gz88SqqqqqqqqqqqqqqqqquBtVVVVVVVVVVVVVVVVVVVVVVTxvHBuUIlJVHmc7NCwLclmh\r\n"
					+ "YFuSzQsC3JZoWBb7KqqqqqqqqqqqqqqqqqqqqNgk3tfcDLdK/Wt+2jz5PKuCLHtEVne/o97OesTP\r\n"
					+ "jMjaJz6o79kS4c7u7u7u7u7u7u7agkZH3nIBDRFvpy8AtmAxHYBaWT3n8qSeYoj7YbFV8zHJCFlH\r\n"
					+ "6KUzZww2sgJK48fpPsrcTYYICRJqbV8oJYMLkAAAAAAAAAAAAAABydLcKf/52Dn13d3d3d3d3d3d\r\n"
					+ "3d3d3d3d3d3d3d3d3d3d3d19es20Nzpe8Xu50veL3c6XvF7udL3i93Ol7xe7xKqqqqqqqqqqqqqq\r\n"
					+ "qqqqqqeJLQYNzpe8Xu50veL3c6XvF7udL3i93Ol7xe7xKqqqqqqqqqqqqqqqqqqqqeIKbSXyZEyD\r\n"
					+ "gDWEf0FArk9ZJF7hpc8UYJ8rNL5itefQBSz3D9Hd3d3d3d3d3d3MEhXUNNajoM53aywoSotdb52c\r\n"
					+ "rmEvZdVwvmGySCRsyrqHi7sHnmt4GqIKqpDg07glM5AdHhBLEbEjJfFdHBzkgRIIlrUMIMryT/YK\r\n"
					+ "qqqqqqqqqqqqqiElnqIiILBOqCPfJuTMzMzMzMzMzMzMzMzMzMzopmZmZmZmZmZmZkDQidhKSqPM\r\n"
					+ "52aFgW5LNCwLclmhYFuSzQsC3JaMREREREREREREREREREREQWofOeXvF7udL3i93Ol7xe7nS94v\r\n"
					+ "dzpe8Xu50xbVVVVVVVVVVVVVVVVVVVVVUk9eHdmmYr3WH////////////////2SL4ZOcGcqOKsiF\r\n"
					+ "uTVdrTsqkY04mnx7Y5NWBWBAyGQ+qKiKM9bwE9qjFfJkM/EuBOGCVDBSy1bvSiIiIiIiIiIiIiIi\r\n"
					+ "IMGx9Ze9pRJAkREREREREREREREREREREREREREREREREREREQ9Tb01kTkomhHdyuh5dhVHmc7NC\r\n"
					+ "wLclmhYFuSzQsC3JZo11VVVVVVVVVVVVVVVVVVVVPwDMb3kkEia77un3ZMMJLxwm7kvHCbuke49Z\r\n"
					+ "k4hQFLVmFsq+UBxkvoTTJfQmmS+hNMl9CacSIiIiIiIiCPrU6OkxpLVVVVVVVVVVVVVVVVVVVLCH\r\n"
					+ "kM3OXMzMzMzMyBgbJ97UJC+5+V9Eytz1br6qeeenVSNd8zBT9lNmZmZQbHdJr9kJSSZDPXeRUWvg\r\n"
					+ "eNJ2Ko4MOEqK6jeHm9JZvqhXuvYmNpmJHxW2Ss9+Asgt7yRnzwAA/hFpxRFBP4ieqLcAjN+TDQ7S\r\n"
					+ "hR+3dk9lhF9L9xSOxIpLAzmIvGF0+21s6jfslQEU1l1pdR6WJjplfdeb1VeX1k70R6y1zdmYNcb/\r\n"
					+ "uHOKUmrXtJPUPJjwTGATNiMEfPYJGPXijcVx3Jr9KU79J/zRU4wUzalW8fSp5x0ip1cCJYdRX7Ks\r\n"
					+ "ERjdTAU2vbdRq/UTXGJYUuooB2NbEHAklkdIO/6HBnFzHE3ZSvhcZz5P/tF1+t1yqZzllMhvO/ww\r\n"
					+ "dsHnMWRAuiml7Tgb4I5/phthjT1uoqOWtaQ/x99cRQ4SgzD9aV4nZCTg+9/01/f4lMTrvcUnILl5\r\n"
					+ "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\r\n"
					+ "";
			//byte[] decodedString = Base64.getDecoder().decode(new String(ytr).getBytes("UTF-8"));
			//System.out.println(decodedString);
			for (int a = 0; a < abd.size(); a++) {
				byte[] bytes = abd.get(a).getPhoto1();
				System.out.println(bytes);
				String s = new String(bytes, StandardCharsets.UTF_8);
				//byte[] decodedString = Base64.getDecoder().decode(new String(s).getBytes("UTF-8"));
				System.out.println(s);
				abd.get(a).setPhoto1(bytes);
				service.saveCrowd(abd.get(a));
				System.out.println(a);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void deleteCrowd() {
		// TODO Auto-generated method stub
		service.deleteCrowd(crowd);
		Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}
	
}
