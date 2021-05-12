package com.retail.store.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author MohamedAdel
 */
public class InvoiceCalculation {

	@JsonProperty
	private User user = null;
	@JsonProperty
	private Invoice invoice = null;

	public InvoiceCalculation() {
	}

	public InvoiceCalculation(User user, Invoice invoice) {
		this.user = user;
		this.invoice = invoice;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

}
