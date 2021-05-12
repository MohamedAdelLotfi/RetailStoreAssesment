package com.retail.store.DAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.retail.store.Entity.Product;
import com.retail.store.Entity.ProductCategory;
import com.retail.store.Entity.User;

/**
 * @author MohamedAdel
 */
public interface IDiscount {

	public BigDecimal calculateInvoiceTotal(List<Product> products, ProductCategory category);

	public BigDecimal calculateDiscount(BigDecimal amount, BigDecimal discount);

	public BigDecimal getUserDiscountPercentage(User user);

	public boolean isCustomerBefore(LocalDate createdDate, long years);

	public BigDecimal calculateInvoiceDiscount(BigDecimal total, BigDecimal amount, BigDecimal discount);

}
