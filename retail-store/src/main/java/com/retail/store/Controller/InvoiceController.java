package com.retail.store.Controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.retail.store.DAO.DiscountInvoiceCalculation;
import com.retail.store.Entity.Invoice;
import com.retail.store.Entity.InvoiceCalculation;
import com.retail.store.Entity.User;

/**
 * @author MohamedAdel
 */

@RequestMapping("invoice")
@RestController
public class InvoiceController {

	@Autowired
	private DiscountInvoiceCalculation discountInvoiceCalculation;

	@PostMapping(consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public BigDecimal createUserInvoiceDiscount(@RequestBody InvoiceCalculation invCal) {
		User user = invCal.getUser();
		Invoice inv = invCal.getInvoice();

		if (user != null && inv != null) {
			return discountInvoiceCalculation.calculateDiscountInvoice(user, inv);
		}
		return BigDecimal.ZERO;
	}
}
