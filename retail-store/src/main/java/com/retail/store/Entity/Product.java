package com.retail.store.Entity;

import java.math.BigDecimal;

/**
 * @author MohamedAdel
 */
public class Product {

	private String Name;
	private BigDecimal Price;
	private ProductCategory Category;
	private BigDecimal Amount;

	public Product(String Name, BigDecimal Price, ProductCategory Category) {
		setName(Name);
		setPrice(Price);
		setCategory(Category);
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal Price) {
		this.Price = Price;
	}

	public ProductCategory getCategory() {
		return Category;
	}

	public void setCategory(ProductCategory Category) {
		this.Category = Category;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal Amount) {
		this.Amount = Amount;
	}
}
