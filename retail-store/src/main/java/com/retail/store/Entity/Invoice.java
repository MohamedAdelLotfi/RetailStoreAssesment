package com.retail.store.Entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author MohamedAdel
 */
public class Invoice {

	private List<Product> products;
	private BigDecimal GrandTotal;
	private BigDecimal NetTotal;
	private BigDecimal TotalInvoice;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	/*
	 * Total without any deductions
	 **/
	public BigDecimal getGrandTotal() {
		return GrandTotal;
	}

	public void setGrandTotal(BigDecimal GrandTotal) {
		this.GrandTotal = GrandTotal;
	}

	/**
	 * Total minus from discounts
	 **/
	public BigDecimal getNetTotal() {
		return NetTotal;
	}

	public void setNetTotal(BigDecimal NetTotal) {
		this.NetTotal = NetTotal;
	}

	/**
	 * Total of Invoice
	 **/
	public BigDecimal getTotalInvoice() {
		return TotalInvoice;
	}

	public void setTotalInvoice(BigDecimal TotalInvoice) {
		this.TotalInvoice = TotalInvoice;
	}

}
