package com.retail.store.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author MohamedAdel
 */
public class Invoice implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Product> products;
	private BigDecimal grandTotal;
	private BigDecimal netTotal;
	private BigDecimal totalInvoice;
	
	public Invoice(){
		
	}

	public Invoice(List<Product> products, BigDecimal GrandTotal, BigDecimal NetTotal) {
		setProducts(products);
		setGrandTotal(GrandTotal);
		setNetTotal(NetTotal);
	}

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
		return grandTotal;
	}

	public void setGrandTotal(BigDecimal GrandTotal) {
		this.grandTotal = GrandTotal;
	}

	/**
	 * Total minus from discounts
	 **/
	public BigDecimal getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(BigDecimal NetTotal) {
		this.netTotal = NetTotal;
	}

	/**
	 * Total of Invoice
	 **/
	public BigDecimal getTotalInvoice() {
		return totalInvoice;
	}

	public void setTotalInvoice(BigDecimal TotalInvoice) {
		this.totalInvoice = TotalInvoice;
	}

}
