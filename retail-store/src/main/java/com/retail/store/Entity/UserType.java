package com.retail.store.Entity;

/**
 * @author MohamedAdel
 */
public enum UserType {

	EMPLOYEE(1, 30), AFFILIATE(2, 10), OLD_CUSTOMER(3, 5);

	private int ID;
	private int Percentage;

	private UserType(int ID, int Percentage) {
		this.ID = ID;
		this.Percentage = Percentage;
	}

	public int getPercentage() {
		return this.Percentage;
	}

	public int getID() {
		return this.ID;
	}
}
