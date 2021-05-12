package com.retail.store.Entity;

import java.time.LocalDate;

/**
 * @author MohamedAdel
 */
public class User {

	private String Name;
	private String Phone;
	private UserType userType;
	private LocalDate created;

	public User(String Name, String Phone, UserType userType, LocalDate created) {
		setName(Name);
		setPhone(Phone);
		setUserType(userType);
		setCreated(created);
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String Phone) {
		this.Phone = Phone;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}
}
