package com.ans.cda;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * tooltip class utilities
 * 
 * @author bensa Nizar
 */
public class Utils {

	private static final ObjectProperty<Locale> locale = new SimpleObjectProperty<>(Locale.getDefault());

	/**
	 * localeProperty
	 * 
	 * @return
	 */
	public static ObjectProperty<Locale> localeProperty() {
		return locale;
	}

	/**
	 * getLocale
	 * 
	 * @return
	 */
	public static Locale getLocale() {
		return locale.get();
	}

	/**
	 * setLocale
	 * 
	 * @param locale
	 */
	public static void setLocale(final Locale locale) {
		localeProperty().set(locale);
	}

	/**
	 * i18n
	 * 
	 * @param key
	 * @return
	 */
	public static String i18n(final String key) {
		return ResourceBundle.getBundle("bundleName", getLocale()).getString(key);
	}
}