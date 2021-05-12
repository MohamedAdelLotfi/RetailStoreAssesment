package com.retail.store.DAO;

import java.math.BigDecimal;

import com.retail.store.Entity.Invoice;
import com.retail.store.Entity.User;

/**
 * @author MohamedAdel
 */
public interface IDiscountInvoiceCalculation {

	public BigDecimal calculateDiscountInvoice(User user, Invoice invoice);
}
