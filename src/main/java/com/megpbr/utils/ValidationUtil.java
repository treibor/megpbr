package com.megpbr.utils;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class ValidationUtil {
	//private static final String ALLOWED_PATTERN = "[0-9A-Za-z.()@/'&]";
	 public static void applyValidation(TextField textField) {
		 textField.setAllowedCharPattern("[-0-9A-Za-zñÑïÏ.*%_:+,();@/'&\\s]");
		 //textField.setP
	        textField.setMinLength(0);
	        textField.setMaxLength(30);
	    }
	    public static void applyValidation(TextField textField, int size) {
	    	textField.setAllowedCharPattern("[-0-9A-Za-zñÑïÏ.*%_:+,();@/'&\\s]");
	        textField.setMinLength(0);
	        textField.setMaxLength(size);
	    }
	    public static void applyTextAreaValidation(TextArea textArea) {
	    	textArea.setAllowedCharPattern("[-0-9A-Za-zñÑïÏ.*%_:+,();@/'&\\s]");
	    	textArea.setMinLength(0);
	    	textArea.setMaxLength(50);
	    }
	    public static boolean applyValidation(String string) {
	        if (string == null) {
	            return true;
	        }

	        return string.matches("[-0-9A-Za-zñÑïÏ.*%_:+,();@/'&\\s]*");
	    }
    
}