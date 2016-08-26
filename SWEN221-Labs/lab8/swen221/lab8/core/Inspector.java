package swen221.lab8.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import swen221.lab8.util.Rectangle;

/**
 * Responsible for interacting with widgets via reflection. This includes
 * reading and writing attributes on the widgets, and creating instances of
 * them.
 * 
 * @author David J. Pearce
 *
 */
public class Inspector {
	/**
	 * Provides the master list of widgets from which we can create new widgets
	 */
	private Class<? extends Widget>[] widgets;

	public Inspector(Class<? extends Widget>... widgets) {
		this.widgets = widgets;
	}

	/**
	 * Return the array of all valid widgets being considered by this inspector.
	 * 
	 * @return
	 */
	public Class<? extends Widget>[] getWidgets() {
		return widgets;
	}

	/**
	 * Construct a new widget of a particular kind with given dimensions.
	 * 
	 * @param name
	 *            The kind of widget to create
	 * @param dimensions
	 *            The dimensions of the widget
	 * @return
	 */
	public Widget newWidget(String name, Rectangle dimensions) {
		Class<?> widgetClass = findWidget(name);
		// HINT: You should look in the widget class (using reflection) for a
		// constructor which accepts a Rectangle object as a parameter. Then you
		// can just invoke this method passing in the dimensions parameter.
		try {
			Constructor<?> ccc = widgetClass.getConstructor(Rectangle.class);
			try {
				Widget w = (Widget) ccc.newInstance(dimensions);
				return w;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the value of a given attribute for this widget. This is done my
	 * calling the corresponding (public) getter method.
	 * 
	 * @param widget
	 *            The widget whose attribute we wish to read
	 * @param name
	 *            The name of the attribute to be read
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttributeValue(Widget widget, String name) {
		// To get the value of a given attribute in the widget, you need to use
		// its corresponding "getter" method. For example, for an attribute
		// "color" the getter would be "getColor".
		String getterName = "get" + capitalise(name);
		// What you need to do is find the appropriate getter method and invoke
		// it. This doesn't accept any parameters. Furthermore, you should just
		// through an IllegalArgumentException for all the various error cases
		// which arise when doing this (SecurityException,
		// NoSuchMethodException, etc).
		try {
			Method method = widget.getClass().getMethod(getterName, new Class[] {});
			Object o = null;
			try {
				o = method.invoke(widget, new Object[] {});
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return o;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Set the value of a given attribute for this widget
	 * 
	 * @param widget
	 *            The widget whose attribute we wish to update
	 * @param name
	 *            The name of the attribute to be updated
	 * @param value
	 *            The value of the attribute to be updated
	 */
	public void setAttribute(Widget widget, String name, Object value) {
		// To set the value of a given attribute in the widget, you need to use
		// its corresponding "setter" method. For example, for an attribute
		// "color" the getter would be "SetColor".
		String setterName = "set" + capitalise(name);
		//
		//
		// . To work this out, you need to get the "type" of the corresponding
		// field using Class.getDeclaredField().
		
		try {
			widget.getClass().getMethod(setterName, widget.getClass().getDeclaredField(name).getType())
					.invoke(widget, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Make the first letter of the string a captial.
	 * 
	 * @param str
	 * @return
	 */
	private static String capitalise(String str) {
		String rest = str.substring(1);
		char c = Character.toUpperCase(str.charAt(0));
		return c + rest;
	}

	private Class<? extends Widget> findWidget(String widgetName) {
		for (Class<? extends Widget> w : widgets) {
			if (w.getSimpleName().equals(widgetName)) {
				return w;
			}
		}
		throw new IllegalArgumentException("Widget " + widgetName + " does not exist");
	}
}
