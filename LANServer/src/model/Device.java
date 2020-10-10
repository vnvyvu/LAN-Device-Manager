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
	 */
	public Device(InetAddress address, String os, String manufacturer, String model) {
		super();
		this.address=address;
		this.os=os;
		this.manufacturer=manufacturer;
		this.model=model;
	}
	
	/**
	 * Instantiates a new device.
	 */
	public Device() {
		super();
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
