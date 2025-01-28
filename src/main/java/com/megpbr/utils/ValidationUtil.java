package com.megpbr.utils;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class ValidationUtil {

    public static void applyValidation(TextField textField) {
        textField.setAllowedCharPattern("[0-9A-Za-z.,()@/'&\\s-]");
        textField.setMinLength(0);
        textField.setMaxLength(30);
    }
    public static void applyValidation(TextField textField, int size) {
        textField.setAllowedCharPattern("[0-9A-Za-z.,()@/'&\\s-]");
        textField.setMinLength(0);
        textField.setMaxLength(size);
    }
    public static void applyTextAreaValidation(TextArea textArea) {
    	textArea.setAllowedCharPattern("[0-9A-Za-z.,()@/'&\\s-]");
    	textArea.setMinLength(0);
    	textArea.setMaxLength(50);
    }
    public static boolean applyValidation(String string) {
    	//textArea.setAllowedCharPattern("[0-9A-Za-z.,()@/-'& ]");
    	if (string == null) {
            return true;
        }

    	if (string.matches("[0-9A-Za-z.,()@/'&\\s-]*")){
    		return true;
    	}
    	return false;    	
    }
}