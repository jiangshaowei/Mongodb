package org.jerry.mongo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

public class Person extends BaseBean implements Serializable{ 

	/**
	 * 
	 */
	
	public static final String _collection = "person";
	
	private String name;
	private String password;
	private Integer age;
	private List<Address> addresses;
	private ZipCode zipcode;
	private Cards cards;
	private Map<Integer,Cards> mapCards ;
	public Person(){
		
	}

	
	public Person(String name,String password,Integer age){
		this.name = name;
		this.password = password;
		this.age = age;
	}
	

	
	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}


	public ZipCode getZipcode() {
		return zipcode;
	}


	public void setZipcode(ZipCode zipcode) {
		this.zipcode = zipcode;
	}


	public Cards getCards() {
		return cards;
	}


	public void setCards(Cards cards) {
		this.cards = cards;
	}


	public Map<Integer, Cards> getMapCards() {
		return mapCards;
	}


	public void setMapCards(Map<Integer, Cards> mapCards) {
		this.mapCards = mapCards;
	}
	
	
	
}
