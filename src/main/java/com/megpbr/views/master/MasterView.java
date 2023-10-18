package com.megpbr.views.master;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.CrowdCategory;
import com.megpbr.data.entity.CrowdFormat;
import com.megpbr.data.entity.CrowdType;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterCategory;
import com.megpbr.data.entity.MasterCommercial;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.MasterLocallanguage;
import com.megpbr.data.entity.MasterManagementRegime;
import com.megpbr.data.entity.MasterPosition;
import com.megpbr.data.entity.MasterSeason;
import com.megpbr.data.entity.MasterStatus;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Crowd;
import com.megpbr.data.service.CropService;
import com.megpbr.data.service.CrowdService;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.MasterService;
import com.megpbr.data.service.UserService;
import com.megpbr.views.CropPlantsForm.DeleteEvent;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"STATEADMIN", "SUPERADMIN"})
@PageTitle("Crowd Sourcing")
@Route(value = "master", layout = MainLayout.class)
public class MasterView extends HorizontalLayout{
	MasterService service;
	Dbservice dbservice;
	UserService uservice;
	UserLogin user;
	MasterPosition position;
	MasterManagementRegime regime;
	MasterStatus status;
	MasterSeason season;
	MasterCommercial commercial;
	MasterLocallanguage language;
	AuditTrail audittrail;
	Grid<UserLogin> grid=new Grid<>(UserLogin.class);
	Grid<MasterManagementRegime> regimegrid=new Grid<>(MasterManagementRegime.class);
	Grid<MasterPosition> positiongrid=new Grid<>(MasterPosition.class);
	Grid<MasterSeason> seasongrid=new Grid<>(MasterSeason.class);
	Grid<MasterCommercial> commercialgrid=new Grid<>(MasterCommercial.class);
	Grid<MasterStatus> statusgrid=new Grid<>(MasterStatus.class);
	Grid<MasterLocallanguage> languagegrid=new Grid<>(MasterLocallanguage.class);
	Grid<AuditTrail> auditgrid=new Grid<>(AuditTrail.class);
	//Binder<MasterPosition> binder = new BeanValidationBinder<>(MasterPosition.class);
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
	PositionForm positionform;
	StatusForm statusform;
	SeasonForm seasonform;
	RegimeForm regimeform;
	UserForm userform;
	LocalLanguageForm languageform;
	TextField uses=new TextField("Uses");
	//FormLayout form=new FormLayout();
	Button verifyButton =new Button("Verify");
	Button deleteButton =new Button("Delete");
	@Autowired
	private Audit auditobject;
	public MasterView(MasterService service, Dbservice dbservice, UserService uservice) {
		this.service=service;
		this.dbservice=dbservice;
		this.uservice=uservice;
		ConfigureForm();
		setSizeFull();
		add(getTabs());
	}
	private boolean isStateAdmin() {
		//UserLogin user=uservice.getLoggedUser();
		String userLevel=uservice.getLoggedUserLevel();
		if(userLevel.startsWith("STATE") && userLevel.endsWith("ADMIN")) {
			return true;
		}else if(userLevel.startsWith("SUPER") && userLevel.endsWith("ADMIN")){
			return true;
		}else {
			return false;
		}
	}
	public Component getTabs() {
		//Tab tab1=new Tab("Users");
		TabSheet tabSheet = new TabSheet();
		tabSheet.add("Audit Trail",getAuditTab()).setVisible(isStateAdmin());;
		tabSheet.add("Position",getPositionTab());
		tabSheet.add("Season",getSeasonTab());
		tabSheet.add("Status",getStatusTab());
		tabSheet.add("Management Regime",getRegimeTab());
		tabSheet.add("Local Language",getLanguageTab());
		tabSheet.add("Users",getUserTab()).setVisible(isStateAdmin());;
		tabSheet.setSizeFull();
		return tabSheet;
	}
	
	private void ConfigureForm() {
		positionform= new PositionForm(dbservice);
		positionform.setWidth("30%");
		positionform.addListener(PositionForm.SaveEvent.class, this::savePosition);
		positionform.addListener(PositionForm.DeleteEvent.class, this::deletePosition);
		positionform.setPosition(new MasterPosition());
		statusform= new StatusForm(dbservice);
		statusform.setWidth("30%");
		statusform.addListener(StatusForm.SaveEvent.class, this::saveStatus);
		statusform.addListener(StatusForm.DeleteEvent.class, this::deleteStatus);
		statusform.setMasterStatus(new MasterStatus());
		seasonform= new SeasonForm(dbservice);
		seasonform.setWidth("30%");
		seasonform.addListener(SeasonForm.SaveEvent.class, this::saveSeason);
		seasonform.addListener(SeasonForm.DeleteEvent.class, this::deleteSeason);
		seasonform.setMasterSeason(new MasterSeason());
		regimeform= new RegimeForm(dbservice);
		regimeform.setWidth("30%");
		regimeform.addListener(RegimeForm.SaveEvent.class, this::saveRegime);
		regimeform.addListener(RegimeForm.DeleteEvent.class, this::deleteRegime);
		regimeform.setMasterManagementRegime(new MasterManagementRegime());
		languageform= new LocalLanguageForm(dbservice);
		languageform.setWidth("30%");
		languageform.addListener(LocalLanguageForm.SaveEvent.class, this::saveLanguage);
		languageform.addListener(LocalLanguageForm.DeleteEvent.class, this::deleteLanguage);
		languageform.setMasterLocallanguage(new MasterLocallanguage());
		userform= new UserForm(dbservice);
		userform.setWidth("30%");
		userform.addListener(UserForm.SaveEvent.class, this::saveUser);
		userform.addListener(UserForm.DeleteEvent.class, this::deleteUser);
		userform.setUserLogin(new UserLogin());
	}
	public Component getAuditTab() {
		auditgrid.removeAllColumns();
		auditgrid.addColumn(audittrail->audittrail.getAction()).setHeader("Action").setSortable(true).setResizable(true);
		auditgrid.addColumn(audittrail->audittrail.getDetails()).setHeader("Details").setSortable(true).setResizable(true);
		auditgrid.addColumn(audittrail->audittrail.getActionBy()).setHeader("Performed By").setSortable(true).setResizable(true);
		auditgrid.addColumn(audittrail->audittrail.getIpAddress()).setHeader("IP Address").setSortable(true).setResizable(true);
		auditgrid.addColumn(audittrail->audittrail.getActionOn()).setHeader("Date /Time").setSortable(true).setResizable(true);
		auditgrid.setItems(service.getAuditTrail());
		auditgrid.setSizeFull();
		return auditgrid;
	}
	public Component getPositionTab() {
		positiongrid.removeAllColumns();
		positiongrid.addColumn(position->position.getPosition()).setHeader("Short Name");
		positiongrid.addColumn(position->position.getPositionName()).setHeader("Position Name");
		positiongrid.setItems(service.getPositions());
		positiongrid.asSingleSelect().addValueChangeListener(e -> editPosition(e.getValue()));
		positiongrid.setSizeFull();
		var positionlayout=new HorizontalLayout();
		positionlayout.setSizeFull();
		positionlayout.add(positiongrid, positionform);
		return positionlayout;
	}
	public void savePosition(PositionForm.SaveEvent event) {
		MasterPosition masterPosition=event.getMasterPosition();
		service.savePosition(masterPosition);
		auditobject.saveAudit("Add/Update", "Master Position:"+masterPosition.getId()+"-"+masterPosition.getPositionName());
		refreshPositionGrid();
	}
	public void deletePosition(PositionForm.DeleteEvent event) {
		try {
			MasterPosition masterPosition=event.getMasterPosition();
			service.deletePosition(masterPosition);
			refreshPositionGrid();
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			auditobject.saveAudit("Delete", "Master Position:"+masterPosition.getId()+"-"+masterPosition.getPositionName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Failed. The Item is Linked with Some Village Data").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	
	public void editPosition(MasterPosition position) {
		if (position != null) {
			positionform.setMasterPosition(position);
			positionform.save.setText("Update");
		}else {
			positionform.setMasterPosition(new MasterPosition());
			positionform.save.setText("Add");
		}
	}
	public void refreshPositionGrid() {
		positiongrid.setItems(service.getPositions());
	}
	
	public Component getSeasonTab() {
		seasongrid.removeAllColumns();
		seasongrid.addColumn(season->season.getId()).setHeader("Id");
		seasongrid.addColumn(season->season.getFruitseason()).setHeader("Season");
		seasongrid.setItems(service.getSeasons());
		seasongrid.setSizeFull();
		seasongrid.asSingleSelect().addValueChangeListener(e -> editSeason(e.getValue()));
		var seasonlayout=new HorizontalLayout();
		seasonlayout.setSizeFull();
		seasonlayout.add(seasongrid, seasonform);
		return seasonlayout;
		//return seasongrid;
	}
	public void saveSeason(SeasonForm.SaveEvent event) {
		MasterSeason masterSeason=event.getMasterSeason();
		service.saveSeason(masterSeason);
		auditobject.saveAudit("Add/Update", "Master Season:"+masterSeason.getId()+"-"+masterSeason.getFruitseason());
		refreshSeasonGrid();
	}
	public void deleteSeason(SeasonForm.DeleteEvent event) {
		try {
			MasterSeason masterSeason=event.getMasterSeason();
			service.deleteSeason(masterSeason);
			refreshSeasonGrid();
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			auditobject.saveAudit("Delete", "Master Season:"+masterSeason.getId()+"-"+masterSeason.getFruitseason());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Failed. The Item is Linked with PBR Data").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	
	public void editSeason(MasterSeason position) {
		if (position != null) {
			seasonform.setMasterSeason(position);
			seasonform.save.setText("Update");
		}else {
			seasonform.setMasterSeason(new MasterSeason());
			seasonform.save.setText("Add");
		}
	}
	public void refreshSeasonGrid() {
		seasongrid.setItems(service.getSeasons());
	}
	public Component getStatusTab() {
		statusgrid.removeAllColumns();
		statusgrid.addColumn(status->status.getStatus()).setHeader("Status Code");
		statusgrid.addColumn(season->season.getStatusName()).setHeader("Status");
		statusgrid.setItems(service.getStatus());
		statusgrid.setSizeFull();
		statusgrid.asSingleSelect().addValueChangeListener(e -> editStatus(e.getValue()));
		var statuslayout=new HorizontalLayout();
		statuslayout.setSizeFull();
		statuslayout.add(statusgrid, statusform);
		return statuslayout;
		
	}
	public void saveStatus(StatusForm.SaveEvent event) {
		MasterStatus masterStatus=event.getMasterStatus();
		service.saveStatus(masterStatus);
		auditobject.saveAudit("Add/Update", "Master Status:"+masterStatus.getId()+"-"+masterStatus.getStatusName());
		refreshStatusGrid();
	}
	public void deleteStatus(StatusForm.DeleteEvent event) {
		try {
			MasterStatus masterStatus=event.getMasterStatus();
			service.deleteStatus(masterStatus);
			refreshStatusGrid();
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			auditobject.saveAudit("Delete", "Master Status:"+masterStatus.getId()+"-"+masterStatus.getStatusName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Failed. The Item is Linked with PBR Data").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	
	public void editStatus(MasterStatus position) {
		if (position != null) {
			statusform.setMasterStatus(position);
			statusform.save.setText("Update");
		}else {
			statusform.setMasterStatus(new MasterStatus());
			statusform.save.setText("Add");
		}
	}
	public void refreshStatusGrid() {
		statusgrid.setItems(service.getStatus());
	}
	public Component getRegimeTab() {
		regimegrid.removeAllColumns();
		
		regimegrid.addColumn(regime->regime.getManagerregime()).setHeader("Regime Code");
		regimegrid.addColumn(regime->regime.getManagementregime()).setHeader("Management Regime");
		regimegrid.setItems(service.getMasterManagementRegime());
		regimegrid.setSizeFull();
		regimegrid.asSingleSelect().addValueChangeListener(e -> editRegime(e.getValue()));
		var regimelayout=new HorizontalLayout();
		regimelayout.setSizeFull();
		regimelayout.add(regimegrid, regimeform);
		return regimelayout;
	}
	public void saveRegime(RegimeForm.SaveEvent event) {
		MasterManagementRegime masterRegime=event.getMasterManagementRegime();
		service.saveRegime(masterRegime);
		auditobject.saveAudit("Add/Update", "Master Regime:"+masterRegime.getId()+"-"+masterRegime.getManagementregime());
		refreshRegimeGrid();
	}
	public void deleteRegime(RegimeForm.DeleteEvent event) {
		try {
			MasterManagementRegime masterRegime=event.getMasterManagementRegime();
			service.deleteRegime(masterRegime);
			refreshRegimeGrid();
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			auditobject.saveAudit("Delete", "Master Regime:"+masterRegime.getId()+"-"+masterRegime.getManagementregime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Failed. The Item is Linked with PBR Data").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	
	public void editRegime(MasterManagementRegime position) {
		if (position != null) {
			regimeform.setMasterManagementRegime(position);
			regimeform.save.setText("Update");
		}else {
			regimeform.setMasterManagementRegime(new MasterManagementRegime());
			regimeform.save.setText("Add");
		}
	}
	public void refreshRegimeGrid() {
		regimegrid.setItems(service.getMasterManagementRegime());
	}

	public Component getLanguageTab() {
		languagegrid.removeAllColumns();
		languagegrid.addColumn(language -> language.getId()).setHeader("Id");
		languagegrid.addColumn(language -> language.getLanguageName()).setHeader("Language");
		languagegrid.setItems(service.getMasterLocalLanguages());
		languagegrid.setSizeFull();
		languagegrid.asSingleSelect().addValueChangeListener(e -> editLanguage(e.getValue()));
		var languagelayout=new HorizontalLayout();
		languagelayout.setSizeFull();
		languagelayout.add(languagegrid, languageform);
		return languagelayout;
	}
	public void saveLanguage(LocalLanguageForm.SaveEvent event) {
		MasterLocallanguage language=event.getMasterLocallanguage();
		service.saveLanguage(language);
		auditobject.saveAudit("Add/Update", "Master Language:"+language.getId()+"-"+language.getLanguageName());
		refreshLanguageGrid ();
	}
	public void deleteLanguage(LocalLanguageForm.DeleteEvent event) {
		try {
			MasterLocallanguage language=event.getMasterLocallanguage();
			service.deleteLanguage(language);
			refreshLanguageGrid();
			Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			auditobject.saveAudit("Delete", "Master Language:"+language.getId()+"-"+language.getLanguageName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Failed. The Item is Linked with PBR Data").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	
	public void editLanguage(MasterLocallanguage position) {
		if (position != null) {
			languageform.setMasterLocallanguage(position);
			languageform.save.setText("Update");
		}else {
			languageform.setMasterLocallanguage(new MasterLocallanguage());
			languageform.save.setText("Add");
		}
	}
	public void refreshLanguageGrid() {
		languagegrid.setItems(service.getMasterLocalLanguages());
	}

	public Component getUserTab() {

		grid.removeAllColumns();
		grid.addColumn(user -> user.getName()).setHeader("Name").setAutoWidth(true).setSortable(true);
		grid.addColumn(user -> user.getUserName()).setHeader("User Name").setAutoWidth(true).setSortable(true);
		grid.addColumn(user -> user.getEmail()).setHeader("Email").setAutoWidth(true).setSortable(true);
		grid.addColumn(user -> user.getEnabled()==true? "Yes":"No").setHeader("Enabled?").setAutoWidth(true).setSortable(true);
		grid.addColumn(user -> user.getLevel().getLevelName()).setHeader("User Type").setAutoWidth(true)
				.setSortable(true);
		grid.setItems(service.getUsers());
		grid.setSizeFull();
		grid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));
		var userlayout = new HorizontalLayout();
		userlayout.setSizeFull();
		userlayout.add(grid, userform);
		return userlayout;
	}
	public void saveUser(UserForm.SaveEvent event) {
		UserLogin user=event.getUserLogin();
		service.saveUser(user);
		auditobject.saveAudit("Update", "Master User:"+user.getId()+"-"+user.getUserName());
		refreshUserGrid ();
	}
	public void deleteUser(UserForm.DeleteEvent event) {
		try {
			UserLogin user = event.getUserLogin();
			if (uservice.getLoggedUser().getUserName().equals(user.getUserName())) {
				Notification.show("You Are Logged In. You Cannot Delete Yourself").addThemeVariants(NotificationVariant.LUMO_ERROR);
			}else {
				service.deleteUser(user);
				refreshUserGrid();
				Notification.show("Deleted Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				auditobject.saveAudit("Delete", "Master User:" + user.getId() + "-" + user.getUserName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Failed. The Item is Linked with PBR Data").addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
	
	public void editUser(UserLogin user) {
		if (user != null) {
			userform.setUserLogin(user);
			userform.save.setText("Update");
			userform.save.setEnabled(true);
		}else {
			userform.setUser(new UserLogin());
			userform.save.setEnabled(false);
		}
	}
	public void refreshUserGrid() {
		grid.setItems(service.getUsers());
	}
}
