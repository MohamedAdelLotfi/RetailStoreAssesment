package com.retail.store.DAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.retail.store.Entity.Product;
import com.retail.store.Entity.ProductCategory;
import com.retail.store.Entity.User;
import com.retail.store.Entity.UserType;

/**
 * @author MohamedAdel
 */
public class InvoiceDiscountOperations implements IDiscount {

	/**
	 * this method to calculate total of invoice depend on category if there is no
	 * specific category calculate total for all products with all category
	 * 
	 * @param products (list of products)
	 * @param category (product category)
	 */
	@Override
	public BigDecimal calculateInvoiceTotal(List<Product> products, ProductCategory category) {
		if (products == null) {
			throw new NullPointerException("no available products");
		}

		BigDecimal Total = BigDecimal.ZERO;
		for (Product product : products) {
			if (category != null && category.equals(product.getCategory())) {
				Total = Total.add(product.getPrice()); // calculate total for products of specific category
			} else {
				Total = Total.add(product.getPrice()); // calculate total for all products
			}
		}

		return Total;
	}

	/**
	 * this method to calculate discount of total amount of invoice
	 * 
	 * @param amount
	 * @param discount
	 */
	@Override
	public BigDecimal calculateDiscount(BigDecimal amount, BigDecimal discount) {
		if (discount.compareTo(BigDecimal.ONE) > 0) {
			throw new IllegalArgumentException("Discount cannot be more than 100 %");
		}

		BigDecimal dis = amount.multiply(discount);
		if (dis != null) {
			return dis;
		}
		return null;
	}

	/**
	 * this method to get user percentage depend on type of user
	 * 
	 * @param user
	 */
	@Override
	public BigDecimal getUserDiscountPercentage(User user) {
		if (user == null) {
			throw new IllegalArgumentException("cannot get percentage without user");
		}
		UserType userType = user.getUserType();

		if (userType != null) {
			if (userType.equals(UserType.AFFILIATE)) {
				return new BigDecimal(0.1); // if the user is an affiliate of the store, he gets a 10% discount
			} else if (userType.equals(UserType.EMPLOYEE)) {
				return new BigDecimal(0.3); // if the user is an employee of the store, he gets a 30% discount
			} else if (userType.equals(UserType.OLD_CUSTOMER) && isCustomerBefore(user.getCreated(), 2)) {
				return new BigDecimal(0.05); // if the user has been a customer for over 2 years, he gets a 5% discount
			}
		}
		throw new IllegalArgumentException("cannot get percentage without type of user");
	}

	/**
	 * this method to get difference between two dates
	 * 
	 * @param createdDate
	 * @param years
	 */
	@Override
	public boolean isCustomerBefore(LocalDate createdDate, long years) {
		LocalDate current = LocalDate.now();
		Period period = Period.between(createdDate, current);
		int diffYears = period.getYears();
		if (diffYears >= years) {
			return true;
		}
		return false;
	}

	/**
	 * this method to get net total of invoice
	 * 
	 * @param total
	 * @param amount
	 * @param discount
	 */
	@Override
	public BigDecimal calculateInvoiceDiscount(BigDecimal total, BigDecimal amount, BigDecimal discount) {
		return total.divide(amount, 2, RoundingMode.HALF_UP).multiply(discount);
	}

}
