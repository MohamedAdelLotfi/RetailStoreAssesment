package com.retail.store.DAO;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.retail.store.Entity.Invoice;
import com.retail.store.Entity.ProductCategory;
import com.retail.store.Entity.User;

/**
 * @author MohamedAdel
 */

@Service
public class DiscountInvoiceCalculation implements IDiscountInvoiceCalculation {

	@Override
	public BigDecimal calculateDiscountInvoice(User user, Invoice invoice) {
		InvoiceDiscountOperations invOperations = new InvoiceDiscountOperations();

		BigDecimal totalAmountCategory = invOperations.calculateInvoiceTotal(invoice.getProducts(), null);
		BigDecimal totalAmountGrocery = invOperations.calculateInvoiceTotal(invoice.getProducts(),
				ProductCategory.GROCERIES);
		BigDecimal totalAmountNonGrocery = totalAmountCategory.subtract(totalAmountGrocery);

		BigDecimal userDiscount = invOperations.getUserDiscountPercentage(user);

		if (totalAmountNonGrocery.compareTo(BigDecimal.ZERO) > 0) {
			totalAmountNonGrocery = invOperations.calculateDiscount(totalAmountNonGrocery, userDiscount);
		}

		BigDecimal invoiceDiscount = invOperations.calculateInvoiceDiscount(totalAmountCategory, new BigDecimal(100),
				new BigDecimal(5));

		BigDecimal total = totalAmountNonGrocery.add(totalAmountGrocery).subtract(invoiceDiscount);

		if (total != null) {
			return total;
		}

		return BigDecimal.ZERO;
	}

}
