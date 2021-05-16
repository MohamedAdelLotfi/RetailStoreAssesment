package com.retail.store;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.retail.store.DAO.InvoiceDiscountOperations;
import com.retail.store.Entity.Product;
import com.retail.store.Entity.ProductCategory;
import com.retail.store.Entity.User;
import com.retail.store.Entity.UserType;

public class RetailStoreTests {

	@Test
	public void testCalculateTotalAllCategory() {
		List<Product> products = new ArrayList<Product>();
		products.add(new Product("Product 1", new BigDecimal(10), ProductCategory.GROCERIES));
		products.add(new Product("Product 2", new BigDecimal(10), ProductCategory.CLOTHS));
		products.add(new Product("Product 3", new BigDecimal(10), ProductCategory.ELECTRONICS));
		products.add(new Product("Product 4", new BigDecimal(10), ProductCategory.CLOTHS));

		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateInvoiceTotal(products, null);
		assertEquals(40.00, Double.valueOf(total.toString()), 0);
	}

	@Test
	public void testCalculateTotalGroceryCategory() {
		List<Product> products = new ArrayList<Product>();
		products.add(new Product("Product 1", new BigDecimal(10), ProductCategory.GROCERIES));
		products.add(new Product("Product 2", new BigDecimal(10), ProductCategory.GROCERIES));
		products.add(new Product("Product 3", new BigDecimal(10), ProductCategory.GROCERIES));
		products.add(new Product("Product 4", new BigDecimal(10), ProductCategory.GROCERIES));

		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateInvoiceTotal(products, ProductCategory.GROCERIES);
		assertEquals(40.00, Double.valueOf(total.toString()), 0);
	}

	@Test
	public void testCalculateTotalNonGroceryCategory() {
		List<Product> products = new ArrayList<Product>();
		products.add(new Product("Product 1", new BigDecimal(10), ProductCategory.CLOTHS));
		products.add(new Product("Product 2", new BigDecimal(10), ProductCategory.CLOTHS));
		products.add(new Product("Product 3", new BigDecimal(10), ProductCategory.CLOTHS));
		products.add(new Product("Product 4", new BigDecimal(10), ProductCategory.CLOTHS));

		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateInvoiceTotal(products, ProductCategory.CLOTHS);
		assertEquals(40.00, Double.valueOf(total.toString()), 0);
	}

	@Test
	public void testCalculateTotalGroceryOnlyCategory() {
		List<Product> products = new ArrayList<Product>();
		products.add(new Product("Product 1", new BigDecimal(10), ProductCategory.GROCERIES));
		products.add(new Product("Product 2", new BigDecimal(10), ProductCategory.CLOTHS));
		products.add(new Product("Product 3", new BigDecimal(10), ProductCategory.GROCERIES));
		products.add(new Product("Product 4", new BigDecimal(10), ProductCategory.ELECTRONICS));

		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateInvoiceTotal(products, ProductCategory.GROCERIES);
		assertEquals(40.00, Double.valueOf(total.toString()), 0);
	}

	@Test
	public void testCalculateTotalNonGroceryTypeCategory() {
		List<Product> products = new ArrayList<Product>();
		products.add(new Product("Product 1", new BigDecimal(10), ProductCategory.GROCERIES));
		products.add(new Product("Product 2", new BigDecimal(10), ProductCategory.CLOTHS));
		products.add(new Product("Product 3", new BigDecimal(10), ProductCategory.GROCERIES));
		products.add(new Product("Product 4", new BigDecimal(10), ProductCategory.ELECTRONICS));

		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateInvoiceTotal(products, ProductCategory.CLOTHS);
		assertEquals(40.00, Double.valueOf(total.toString()), 0);
	}

	// give pointer exception for no products
	@Test
	public void testCalculateTotalCategoryWithoutProducts() {
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		try {
			BigDecimal total = invD.calculateInvoiceTotal(null, ProductCategory.CLOTHS);
			assertEquals(40.00, Double.valueOf(total.toString()), 0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// give exception for discount greater than 1
	@Test
	public void testCalculateDiscount() {
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		try {
			BigDecimal total = invD.calculateDiscount(new BigDecimal(1000), new BigDecimal(5));
			assertEquals(900.00, Double.valueOf(total.toString()), 0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testCalculateDiscount_1() {
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateDiscount(new BigDecimal(1000), new BigDecimal(0.1));
		assertFalse(900.00 == Double.valueOf(total.toString()));
	}

	@Test
	public void testCalculateDiscount_5() {
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateDiscount(new BigDecimal(1000), new BigDecimal(0.5));
		assertFalse(900.00 == Double.valueOf(total.toString()));
	}

	@Test
	public void testCalculateDiscount_3() {
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateDiscount(new BigDecimal(1000), new BigDecimal(0.3));
		assertFalse(900.00 == Double.valueOf(total.toString()));
	}

	@Test
	public void testGetUserSpecificDiscount_AFFILIATE() {
		User user = new User("Mohamed", "0123456789", UserType.AFFILIATE, LocalDate.now());
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal discount = invD.getUserDiscountPercentage(user);
		assertEquals(0.1, Double.valueOf(discount.toString()), 0);
	}

	@Test
	public void testGetUserSpecificDiscount_EMPLOYEE() {
		User user = new User("Mohamed", "0123456789", UserType.EMPLOYEE, LocalDate.now());
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal discount = invD.getUserDiscountPercentage(user);
		assertFalse(0.1 == Double.valueOf(discount.toString()));
	}

	@Test
	public void testGetUserSpecificDiscount_OLD_CUSTOMER() {
		LocalDate created = LocalDate.of(2018, 5, 12);
		User user = new User("Mohamed", "0123456789", UserType.OLD_CUSTOMER, created);
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal discount = invD.getUserDiscountPercentage(user);
		assertFalse(0.1 == Double.valueOf(discount.toString()));
	}

	// give exception for years less than 2
	@Test
	public void testGetUserSpecificDiscount_OLD_CUSTOMER_Years() {
		try {
			LocalDate created = LocalDate.of(2020, 5, 12);
			User user = new User("Mohamed", "0123456789", UserType.OLD_CUSTOMER, created);
			InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
			BigDecimal discount = invD.getUserDiscountPercentage(user);
			assertEquals(0.1, Double.valueOf(discount.toString()), 0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// give exception for null user
	@Test
	public void testGetUserSpecificDiscountNonUser() {
		try {
			InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
			invD.getUserDiscountPercentage(null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testCalculateInvoiceDiscount() {
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal amount = invD.calculateInvoiceDiscount(new BigDecimal(1000), new BigDecimal(100), new BigDecimal(5));
		assertEquals(50, Double.valueOf(amount.toString()), 0);
	}

	@Test
	public void testCalculateInvoiceDiscount2() {
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal amount = invD.calculateInvoiceDiscount(new BigDecimal(2000), new BigDecimal(50), new BigDecimal(5));
		assertEquals(200, Double.valueOf(amount.toString()), 0);
	}
}
