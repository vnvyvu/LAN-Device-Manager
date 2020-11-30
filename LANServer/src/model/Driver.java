/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package model;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Driver.
 */
public class Driver implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2764332945233712870L;
	
	/** The description. */
	private String name, category, manufacturer, description;

	/**
	 * Instantiates a new driver.
	 */
	public Driver() {
		super();
	}

	/**
	 * Instantiates a new driver.
	 *
	 * @param name the name
	 * @param category the category
	 * @param manufacturer the manufacturer
	 * @param description the description
	 */
	public Driver(String name, String category, String manufacturer, String description) {
		super();
		this.name = name;
		this.category = category;
		this.manufacturer = manufacturer;
		this.description = description;
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
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
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
	 * Sets the manufacturer.
	 *
	 * @param manufacturer the new manufacturer
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.category+"|"+this.name+"|"+this.manufacturer+"|"+this.description;
	}
	
	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Driver) {
			Driver temp=(Driver) obj;
			if(temp.getCategory().equals(this.getCategory())&&
					temp.getName().equals(this.getName())&&
					temp.getManufacturer().equals(this.getManufacturer())&&
					temp.getDescription().equals(this.getDescription())) return true;
		}
		return false;
	}

	/**
	 * Hash code.
	 *
	 * @return the hash
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
}
