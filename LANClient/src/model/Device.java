/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package model;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * Class Device. Represents device, containing information about that device
 */
public class Device implements Serializable{
	
	/** I dont know why I created it!?. */
	private static final long serialVersionUID = 1L;
	
	/** name, ip, operating system, manufacturer, model. */
	private String name, ip, os, manufacturer, model;
	
	/**
	 * Instantiates a new device.
	 *
	 * @param name the name machine
	 * @param ip the ip
	 * @param os the os
	 * @param manufacturer the manufacturer
	 * @param model the model
	 */
	public Device(String name, String ip, String os, String manufacturer, String model) {
		super();
		this.name = name;
		this.ip = ip;
		this.os = os;
		this.manufacturer = manufacturer;
		this.model = model;
	}

	/**
	 * Instantiates a new device.
	 */
	public Device() {
		super();
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Gets the os.
	 *
	 * @return the os
	 */
	public String getOs() {
		return os;
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
