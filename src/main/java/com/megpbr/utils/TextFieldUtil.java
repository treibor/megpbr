package com.megpbr.utils;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class TextFieldUtil {

    public static void applyValidation(TextField textField) {
        textField.setAllowedCharPattern("[0-9A-Za-z@ ]");
        textField.setMinLength(0);
        textField.setMaxLength(30);
    }
    public static void applyTextAreaValidation(TextArea textArea) {
    	textArea.setAllowedCharPattern("[0-9A-Za-z@ ]");
    	textArea.setMinLength(0);
    	textArea.setMaxLength(50);
    }
}