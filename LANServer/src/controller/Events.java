/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Events {
	/** The property change support. Use to fire an event when a value changes*/
	public static PropertyChangeSupport event=new PropertyChangeSupport(new Object());
	
	/**
	 * Adds the property change listener.
	 *
	 * @param listener the listener
	 */
	public static void addPropertyChangeListener(PropertyChangeListener listener) {
        event.addPropertyChangeListener(listener);
    }
}
