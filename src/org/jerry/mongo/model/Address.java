package org.jerry.mongo.model;

import java.io.Serializable;

public class Address implements Serializable{

	/**
	 * 
	 */
	private String street;
	private String number;
	
	public Address(){
		
	}
	public Address(String street,String number){
		this.street = street;
		this.number = number;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
