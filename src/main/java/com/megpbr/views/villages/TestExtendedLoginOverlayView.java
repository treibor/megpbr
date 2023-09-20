package com.megpbr.views.villages;

import com.flowingcode.vaadin.addons.extendedlogin.ExtendedLoginOverlay;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import java.util.Arrays;

/**
 * View that provides a demo of using an ExtendedLoginOverlay.
 *
 * @author mlopez
 */
public class TestExtendedLoginOverlayView extends Div {

  public TestExtendedLoginOverlayView() {
    ExtendedLoginOverlay elo = new ExtendedLoginOverlay();
    elo.replaceFormComponents(
        new TextField("Email", "someone@company.com"),
        new ComboBox<String>("Branch", Arrays.asList("Santa Fe", "Rosario")),
        new PasswordField("Password"),
        new Button("Sign In", ev -> Notification.show("Login successfull")));
    Image image = new Image("/img/LogoChicoGlow.png", "Login image");
    image.setClassName("logo-image");
    image.setWidth("fit-content");
    image.setHeight("fit-content");
    elo.replaceHeaderComponent(image);
    elo.replaceForgotPassword(new Anchor("https://www.flowingcode.com", "Flowing Code Site"));
    elo.setOpened(true);
    add(elo);
    
  }
}