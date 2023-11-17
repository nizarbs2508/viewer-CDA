package com.ans.cda;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;

/**
 * I18N utility class
 * 
 * @author bensa Nizar
 */
public final class I18N {

	/**
	 * the current selected Locale
	 */
	private static final ObjectProperty<Locale> locale;

	/**
	 * set default local
	 */
	static {
		locale = new SimpleObjectProperty<>(getDefaultLocale());
		locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
	}

	/**
	 * get the supported Locales.
	 *
	 * @return List of Locale objects.
	 */
	public static List<Locale> getSupportedLocales() {

		return new ArrayList<>(
				Arrays.asList(Locale.FRENCH, Locale.ENGLISH, Locale.GERMAN, Locale.forLanguageTag("nl-NL"),
						Locale.forLanguageTag("es-ES"), Locale.forLanguageTag("pt-PT"), Locale.ITALIAN));
	}

	/**
	 * get the default locale. This is the systems default if contained in the
	 * supported locales, english otherwise.
	 *
	 * @return
	 */
	public static Locale getDefaultLocale() {
		final Locale sysDefault = Locale.getDefault();
		return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.FRENCH;
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
		Locale.setDefault(locale);
	}

	/**
	 * localeProperty
	 * 
	 * @return
	 */
	public static ObjectProperty<Locale> localeProperty() {
		return locale;
	}

	/**
	 * gets the string with the given key from the resource bundle for the current
	 * locale and uses it as first argument to MessageFormat.format, passing in the
	 * optional args and returning the result.
	 *
	 * @param key  message key
	 * @param args optional arguments for the message
	 * @return localized formatted string
	 */
	public static String get(final String key, final Object... args) {
		final ResourceBundle bundle = ResourceBundle.getBundle("messages", getLocale());
		return MessageFormat.format(bundle.getString(key), args);
	}

	/**
	 * creates a String binding to a localized String for the given message bundle
	 * key
	 *
	 * @param key key
	 * @return String binding
	 */
	public static StringBinding createStringBinding(final String key, Object... args) {
		return Bindings.createStringBinding(() -> get(key, args), locale);
	}

	/**
	 * creates a String Binding to a localized String that is computed by calling
	 * the given func
	 *
	 * @param func function called on every change
	 * @return StringBinding
	 */
	public static StringBinding createStringBinding(final Callable<String> func) {
		return Bindings.createStringBinding(func, locale);
	}

	/**
	 * creates a bound Label whose value is computed on language change.
	 *
	 * @param func the function to compute the value
	 * @return Label
	 */
	public static Label labelForValue(final Callable<String> func) {
		final Label label = new Label();
		label.textProperty().bind(createStringBinding(func));
		return label;
	}

	/**
	 * creates a bound Button for the given resourcebundle key
	 *
	 * @param key  ResourceBundle key
	 * @param args optional arguments for the message
	 * @return Button
	 */
	public static Button buttonForKey(final String key, final Object... args) {
		final Button button = new Button();
		button.textProperty().bind(createStringBinding(key, args));
		return button;
	}

	/**
	 * creates a bound MenuItem for the given resourcebundle key
	 *
	 * @param key  ResourceBundle key
	 * @param args optional arguments for the message
	 * @return Button
	 */
	public static MenuItem menuBarForKey(final String key, final Object... args) {
		final MenuItem button = new MenuItem();
		button.textProperty().bind(createStringBinding(key, args));
		return button;
	}

	/**
	 * creates a bound MenuItem for the given resourcebundle key
	 *
	 * @param key  ResourceBundle key
	 * @param args optional arguments for the message
	 * @return Button
	 */
	public static Menu menuForKey(final String key, final Object... args) {
		final Menu button = new Menu();
		button.textProperty().bind(createStringBinding(key, args));
		return button;
	}

	/**
	 * getString value
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(final String key) {
		final ResourceBundle bundle = ResourceBundle.getBundle("messages", getLocale());
		return bundle.getString(key);
	}

	/**
	 * createBoundTooltip
	 * 
	 * @param key
	 * @return
	 */
	public static Tooltip createBoundTooltip(final String key) {
		final Tooltip tooltip = new Tooltip();
		tooltip.textProperty().bind(createStringBinding(key, locale));
		return tooltip;
	}
}