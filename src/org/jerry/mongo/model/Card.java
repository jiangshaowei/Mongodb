package org.jerry.mongo.model;

import java.io.Serializable;

public class Card implements Serializable{

	/**
	 * 
	 */
	private String bank;
	private String cardnumber;
	private String balance;
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getCardnumber() {
		return cardnumber;
	}
	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	
	
	
}
