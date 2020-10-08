/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package model;

import java.io.Serializable;
import java.net.InetAddress;

// TODO: Auto-generated Javadoc
/**
 * Class Device. Represents device, containing information about that device
 */
public class Device implements Serializable{
	
	/**
	 * I dont know why I created it!?
	 */
	private static final long serialVersionUID = 1L;

	/** is registered state. */
	private boolean isRegistered;
	
	/** The local address. */
	private InetAddress address;
	
	/** operating system, manufacturer, model. */
	private String os, manufacturer, model;
	
	/**
	 * Instantiates a new device.
	 *
	 * @param address -local address
	 * @param os -operating system
	 * @param manufacturer -manufacturer
	 * @param model -machine model
	 * @param isRegistered - is registered state
	 */
	public Device(InetAddress address, String os, String manufacturer, String model, boolean isRegistered) {
		super();
		this.address=address;
		this.os=os;
		this.manufacturer=manufacturer;
		this.model=model;
		this.isRegistered = isRegistered;
	}
	
	/**
	 * Instantiates a new device.
	 */
	public Device() {
		super();
	}
	
	/**
	 * Checks if is registered.
	 *
	 * @return true, if is registered
	 */
	public boolean isRegistered() {
		return isRegistered;
	}
	
	/**
	 * Sets the registered.
	 *
	 * @param isRegistered the new registered
	 */
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public InetAddress getAddress() {
		return address;
	}
	
	/**
	 * Gets the os.
	 *
	 * @return the os
	 */
	public String getOS() {
		return os;
	}
	
	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
	/**
	 * Gets the manufacturer.
	 *
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}
}
