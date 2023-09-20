package com.megpbr.audit;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Tag("form")
public class FormComponent extends Component implements HasComponents, HasSize, HasStyle {
	public FormComponent() {
		setId("loginForm");
		getElement().setAttribute("method", "post");
		getElement().setAttribute("autocomplete", "on");
		Input userNameField = new Input();
		userNameField.setWidth("100%");
		userNameField.setId("username");
		userNameField.getElement().setAttribute("autocomplete", "username");
		userNameField.getClassNames().add("login-textfield");

		Input passwordField = new Input();
		passwordField.setWidth("100%");
		passwordField.setId("password");
		passwordField.getElement().setAttribute("autocomplete", "current-password");
		passwordField.getElement().setAttribute("type", "password");
		passwordField.getClassNames().add("login-textfield");

		final VerticalLayout content = new VerticalLayout();
		content.add(new Div(),new Div() , new com.vaadin.flow.component.html.Label("User name"), userNameField,
				new com.vaadin.flow.component.html.Label("Password"), passwordField);

		add(content);
	}
}
