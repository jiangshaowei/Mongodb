package org.jerry.mongo.model;

import java.io.Serializable;

public class ZipCode extends BaseBean implements Serializable{

	/**
	 * 
	 */
	
	private String state;
	private String city;
	private Integer zipcode;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getZipcode() {
		return zipcode;
	}
	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}
	
	
	
	
}



