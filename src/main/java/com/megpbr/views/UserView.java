package com.megpbr.views;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.UserLoginLevel;
import com.megpbr.data.entity.UserRole;
import com.megpbr.data.entity.Village;
import com.megpbr.data.service.Dbservice;
import com.megpbr.data.service.UserService;
import com.megpbr.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class UserView {
	private Dbservice dbservice;
    private UserService userservice;
    private SecurityService securityService;
    Button saveButton=new Button("Save");
    Button savePassButton=new Button("Save");
    Button cancelButton=new Button("Close");
    Button cancelPassButton=new Button("Close");
    TextField name=new TextField("Full Name");
    TextField userName=new TextField("User Name");
    PasswordField oldpassword=new PasswordField("Old Password");
    PasswordField password=new PasswordField("Password");
    PasswordField confirmPassword=new PasswordField("Confirm Password");
    public ComboBox<UserLoginLevel> userlevel = new ComboBox("User Type");
    public ComboBox<State> state = new ComboBox("State");
	public ComboBox<District> district = new ComboBox("District");
	public ComboBox<Block> block = new ComboBox("Block");
	public ComboBox<Village> village = new ComboBox("Village");
	final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public UserView(Dbservice dbservice, UserService userservice, SecurityService securityService) {
		this.dbservice = dbservice;
		this.userservice = userservice;
		this.securityService = securityService;
	}
	public void createAbout() {
		Dialog aboutdialog=new Dialog();
		H2 headline = new H2("About");
		H3 header=new H3("Meghalaya Biodiversity Board");
		H3 header2=new H3("People's Biodiversity Register (PBR): Version 2.0");
		H5 body=new H5("Designed & Developed By National Informatics Centre, Meghalaya");
		headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0").set("font-size", "1.5em").set("font-weight",
				"bold").set("text-decoration", "underline");
		cancelButton.addClickListener(e -> aboutdialog.close());
		HorizontalLayout buttonLayout1 = new HorizontalLayout(cancelButton);
		buttonLayout1.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		VerticalLayout dialogLayout1 = new VerticalLayout(headline, header, header2, body,   buttonLayout1);
		dialogLayout1.setPadding(false);
		dialogLayout1.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout1.getStyle().set("width", "300px").set("max-width", "100%");
		aboutdialog.add(dialogLayout1);
		aboutdialog.open();
	}
	public void createUser() {
		Dialog userdialog=new Dialog();
		H2 headline = new H2("Create User");
		headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0").set("font-size", "1.5em").set("font-weight",
				"bold");
		populateCombobox();
		cancelButton.addClickListener(e -> userdialog.close());
		saveButton.addClickListener(e-> saveUser());
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		HorizontalLayout buttonLayout1 = new HorizontalLayout(cancelButton, saveButton);
		buttonLayout1.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		VerticalLayout dialogLayout1 = new VerticalLayout(headline, getFields(), buttonLayout1);
		dialogLayout1.setPadding(false);
		dialogLayout1.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout1.getStyle().set("width", "300px").set("max-width", "100%");
		userdialog.add(dialogLayout1);
		userdialog.open();
	}
	public void changePassword() {
		Dialog passdialog=new Dialog();
		H2 headline = new H2("ChangePassword");
		headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0").set("font-size", "1.5em").set("font-weight",
				"bold");
		populateCombobox();
		cancelPassButton.addClickListener(e -> passdialog.close());
		savePassButton.addClickListener(e-> saveNewPassword());
		savePassButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		HorizontalLayout buttonLayout2 = new HorizontalLayout(cancelPassButton, savePassButton);
		buttonLayout2.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		VerticalLayout dialogLayout2 = new VerticalLayout(headline, getPassFields(), buttonLayout2);
		dialogLayout2.setPadding(false);
		dialogLayout2.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout2.getStyle().set("width", "300px").set("max-width", "100%");
		passdialog.add(dialogLayout2);
		passdialog.open();
	}
	
	private void saveNewPassword() {
		String oldpass=oldpassword.getValue();
		UserLogin user=userservice.getLoggedUser();
		if(!passwordEncoder.matches(oldpass.trim(), user.getHashedPassword())) {
			Notification.show("Invalid Password").addThemeVariants(NotificationVariant.LUMO_WARNING);
		}else if(!password.getValue().trim().equals(confirmPassword.getValue().trim())){
			Notification.show("Passwords Don Not Match").addThemeVariants(NotificationVariant.LUMO_WARNING);
		}else if(!checkPasswordStrength(password.getValue())){
			Notification.show("Password is too weak. Please use a combination of Lower case, Upper case, Number and Special Charaters").addThemeVariants(NotificationVariant.LUMO_WARNING);
		}else if(password.getValue().trim().length()<=8){
			Notification.show("Password is too short").addThemeVariants(NotificationVariant.LUMO_WARNING);
		}else {
			user.setHashedPassword(passwordEncoder.encode(password.getValue()));
			userservice.update(user);
			//Notification.show("Password Changed Successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			showConfirmationDialog();
			
		}
	}
	private void showConfirmationDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Password Changed");
        dialog.setText("Password has been successfully changed. You will be now be logged out. Please Login again with your new Password");
        
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> {
            securityService.logout(); // Call the logout method
            //getUI().ifPresent(ui -> ui.navigate("login")); // Redirect to the login page
        });

        dialog.open(); // Open the dialog
    }
	public void populateCombobox() { 
		  int  level=userservice.getLoggedUser().getLevel().getLevel();
		  userlevel.setItems(userservice.getUserLevels(level));
		  userlevel.addValueChangeListener(e->ConfigureAccess());
		  state.setItems(dbservice.getStates());
		  state.addValueChangeListener(e->district.setItems(dbservice.getDistricts(e.getValue())));
		  district.addValueChangeListener(e-> block.setItems(dbservice.getBlocks(e.getValue())));
		  block.addValueChangeListener(e->village.setItems(dbservice.getVillages(e.getValue(), true)));
		  userlevel.setItemLabelGenerator(userlevel->userlevel.getLevelName());
		  state.setItemLabelGenerator(state->state.getStateName());
		  district.setItemLabelGenerator(district->district.getDistrictName());
		  block.setItemLabelGenerator(block->block.getBlockName());
		  village.setItemLabelGenerator(village->village.getVillageName());
		  state.setVisible(false);
		  district.setVisible(false);
		  block.setVisible(false);
		  village.setVisible(false);
		  password.setRevealButtonVisible(false);
		  confirmPassword.setRevealButtonVisible(false);
		  password.setMinLength(10);
	}
	public FormLayout getFields() {
		var form=new FormLayout();
		userName.setAllowedCharPattern("[0-9A-Za-z@]");
		form.add(userlevel, 2);
		form.add(state, 1);
		form.add(district, 1);
		form.add(block, 1);
		form.add(village, 1);
		form.add(name, 2);
		form.add(userName, 2);
		form.add(password, 2);
		form.add(confirmPassword, 2);
		form.setResponsiveSteps(new ResponsiveStep("0", 1),
				// Use two columns, if layout's width exceeds 300px
				new ResponsiveStep("300px", 2));
		return form;
	}
	public FormLayout getPassFields() {
		var form=new FormLayout();
		form.add(oldpassword, 2);
		form.add(password, 2);
		form.add(confirmPassword, 2);
		form.setResponsiveSteps(new ResponsiveStep("0", 1),
				// Use two columns, if layout's width exceeds 300px
				new ResponsiveStep("300px", 2));
		return form;
	}
	private void ConfigureAccess() {
		String userLevel=userlevel.getValue().getLevelName();
		state.setVisible(false);
		district.setVisible(false);
		block.setVisible(false);
		village.setVisible(false);
		if(userLevel.startsWith("STATE")||userLevel.startsWith("VERIFIER")) {
			state.setVisible(true);
		}else if(userLevel.startsWith("DISTRICT")) {
			state.setVisible(true);
			district.setVisible(true);
			
		}else if(userLevel.startsWith("BLOCK")) {
			state.setVisible(true);
			district.setVisible(true);
			block.setVisible(true);
			state.setValue(userservice.getLoggedUser().getState());
			district.setValue(userservice.getLoggedUser().getDistrict());
			block.setValue(userservice.getLoggedUser().getBlock());
		}else if(userLevel.startsWith("VILLAGE")) {
			state.setVisible(true);
			district.setVisible(true);
			block.setVisible(true);
			village.setVisible(true);
			state.setValue(userservice.getLoggedUser().getState());
			district.setValue(userservice.getLoggedUser().getDistrict());
			block.setValue(userservice.getLoggedUser().getBlock());
			village.setValue(userservice.getLoggedUser().getVillage());
		}
		//configureVisiblility();
		String loggedLevel=userservice.getLoggedUser().getLevel().getLevelName();
		if(loggedLevel.startsWith("STATE")||userLevel.startsWith("VERIFIER")) {
			state.setVisible(false);
			state.setValue(userservice.getLoggedUser().getState());
		}else if(loggedLevel.startsWith("DISTRICT")) {
			state.setVisible(false);
			district.setVisible(false);
			state.setValue(userservice.getLoggedUser().getState());
			district.setValue(userservice.getLoggedUser().getDistrict());
		}else if(loggedLevel.startsWith("BLOCK")) {
			state.setVisible(false);
			district.setVisible(false);
			block.setVisible(false);
			state.setValue(userservice.getLoggedUser().getState());
			district.setValue(userservice.getLoggedUser().getDistrict());
			block.setValue(userservice.getLoggedUser().getBlock());
		}else if(loggedLevel.startsWith("VILLAGE")) {
			state.setVisible(false);
			district.setVisible(false);
			block.setVisible(false);
			village.setVisible(false);
			state.setValue(userservice.getLoggedUser().getState());
			district.setValue(userservice.getLoggedUser().getDistrict());
			block.setValue(userservice.getLoggedUser().getBlock());
			village.setValue(userservice.getLoggedUser().getVillage());
		}
	}
	private boolean checkPasswordStrength(String password) {
		boolean containsLowerChar= false, containsUpperChar = false;
		boolean containsDigit = false, containsSpecialChar = false;
		char[] ch= password.toCharArray();
		//System.out.println(password);
		String special_chars = "!(){}[]:;<>?,@#$%^&*+=_-~`|./'";
		for (int i = 0; i < password.length(); i++) {
			if (Character.isLowerCase(ch[i])) {
				containsLowerChar= true;
			}	
			if (Character.isUpperCase(ch[i])) {
				containsUpperChar= true;
			}
			if (Character.isDigit(ch[i])) {
				containsDigit= true;
			}
			if (special_chars.contains(String.valueOf(ch[i]))) {
				containsSpecialChar=true;
			}
		}
		if(containsDigit && containsUpperChar && containsSpecialChar && containsLowerChar){
			return true;
		}
		return false;
	}
	
	private void saveUser() {
		if(userlevel.getValue()==null) {
			Notification.show("Please Select A User Type").addThemeVariants(NotificationVariant.LUMO_WARNING);
		} else {
			String userLevel=userlevel.getValue().getLevelName();
			if (userLevel.startsWith("STATE")&& state.getValue()==null) {
				Notification.show("Please Select The State").addThemeVariants(NotificationVariant.LUMO_WARNING);
			} else if (userLevel.startsWith("DISTRICT")&& district.getValue()==null) {
				Notification.show("Please Select The District").addThemeVariants(NotificationVariant.LUMO_WARNING);
			} else if (userLevel.startsWith("BLOCK")&&block.getValue()==null) {
				Notification.show("Please Select The Block").addThemeVariants(NotificationVariant.LUMO_WARNING);
			} else if (userLevel.startsWith("VILLAGE")&& village.getValue()==null) {
				Notification.show("Please Select The Village").addThemeVariants(NotificationVariant.LUMO_WARNING);
			}else if(userName.getValue().length()<=7 ){
				Notification.show("User Name should be at least 8 characters long").addThemeVariants(NotificationVariant.LUMO_WARNING);
			}else if(password.getValue().length()<=8){
				Notification.show("Password is too short").addThemeVariants(NotificationVariant.LUMO_WARNING);
			}else if(!checkPasswordStrength(password.getValue())){
				Notification.show("Password is too weak. Please use a combination of Lower case, Upper case, Number and Special Charaters").addThemeVariants(NotificationVariant.LUMO_WARNING);
			}else if(!password.getValue().trim().equals(confirmPassword.getValue().trim())){
				Notification.show("Passwords Do Not Match").addThemeVariants(NotificationVariant.LUMO_WARNING);
			}else {
				try {
					UserLogin testuser=userservice.getUserByName(userName.getValue());
					if (testuser == null) {
						UserLogin newUser = new UserLogin();
						newUser.setName(name.getValue());
						newUser.setUserName(userName.getValue());
						newUser.setLevel(userlevel.getValue());
						newUser.setState(state.getValue());
						newUser.setDistrict(district.getValue());
						newUser.setBlock(block.getValue());
						newUser.setVillage(village.getValue());
						newUser.setEnabled(true);
						newUser.setHashedPassword(passwordEncoder.encode(password.getValue().trim()));
						userservice.update(newUser);
						UserRole role = new UserRole();
						if (userLevel.startsWith("STATE") && userLevel.endsWith("ADMIN")) {
							role.setRoleName("ADMIN");
							role.setUser(newUser);
							userservice.update(role);
							//createAdminRole(newUser);
							role.setRoleName("STATEADMIN");
							role.setUser(newUser);
							//userservice.update(role);
							createAdminRole(newUser);
						} else {
							if (userLevel.endsWith("VERIFIER")) {
								role.setRoleName("VERIFIER");
							} else if (userLevel.endsWith("ADMIN")) {
								role.setRoleName("ADMIN");
							} else {
								role.setRoleName("USER");
							}
							role.setUser(newUser);
							userservice.update(role);
						}
						Notification.show("User "+ name.getValue()+" Created Successfully")
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
						clearFields();
					}else {
						Notification.show("Failed To Create User, User Already Exists").addThemeVariants(NotificationVariant.LUMO_ERROR);
						clearFields();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Notification.show("Failed To Create User").addThemeVariants(NotificationVariant.LUMO_ERROR);
					
				}
				
			}
		}
	}
	private void createAdminRole(UserLogin newUser){
		UserRole rolex = new UserRole();
		rolex.setRoleName("STATEADMIN");
		rolex.setUser(newUser);
		userservice.update(rolex);
	}
	private void clearFields() {
		//userlevel.setValue();
		name.setValue("");
		userName.setValue("");
		password.setValue("");
		confirmPassword.setValue("");
		
	}
	
	
}
