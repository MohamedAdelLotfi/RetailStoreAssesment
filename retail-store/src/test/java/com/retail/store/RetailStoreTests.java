package com.retail.store;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.retail.store.DAO.InvoiceDiscountOperations;
import com.retail.store.Entity.Invoice;
import com.retail.store.Entity.Product;
import com.retail.store.Entity.ProductCategory;
import com.retail.store.Entity.User;
import com.retail.store.Entity.UserType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RetailStoreTests {

	private TestRestTemplate template = new TestRestTemplate();

	@LocalServerPort
	int randomServerPort;

	final String baseUrl = "http://localhost:8000";

	private final String INVOICE_CONTROLLER_API = baseUrl + "/invoice";
	
	@Test
	public void testCalculateTotalAllCategory() {
		List<Product> products = getProducts("mix");
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateInvoiceTotal(products, null);
		assertEquals(40.00, Double.valueOf(total.toString()), 0);
	}

	@Test
	public void testCalculateTotalGroceryCategory() {
		List<Product> products = getProducts("groceries");
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateInvoiceTotal(products, ProductCategory.GROCERIES);
		assertEquals(40.00, Double.valueOf(total.toString()), 0);
	}

	@Test
	public void testCalculateTotalNonGroceryCategory() {
		List<Product> products = getProducts("cloths");
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateInvoiceTotal(products, ProductCategory.CLOTHS);
		assertEquals(40.00, Double.valueOf(total.toString()), 0);
	}

	@Test
	public void testCalculateTotalGroceryOnlyCategory() {
		List<Product> products = getProducts("other");
		InvoiceDiscountOperations invD = new InvoiceDiscountOperations();
		BigDecimal total = invD.calculateInvoiceTotal(products, ProductCategory.GROCERIES);
		assertFalse(40.00 == Double.valueOf(total.toString()));
	}

	@Test
	public void testCalculateTotalNonGroceryTypeCategory() {
		List<Product> products = getProducts("other");
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

	@Test
	public void testInvoiceAPIAffiliateUserWithMixProduct() throws IOException {
		JSONObject json = createInvoiceBody(UserType.AFFILIATE, LocalDate.now(), getProducts("mix"),
				new BigDecimal(1000), new BigDecimal(100));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Object> request = new HttpEntity<>(json.toString(), headers);
		ResponseEntity<String> restAPI = template.postForEntity(INVOICE_CONTROLLER_API, request, String.class);
		Assert.assertEquals(201, restAPI.getStatusCodeValue());
	}
	
	@Test
	public void testInvoiceAPIOLDCustomerUserWithGroceryProduct() throws IOException {
		JSONObject json = createInvoiceBody(UserType.OLD_CUSTOMER, LocalDate.now(), getProducts("groceries"),
				new BigDecimal(1000), new BigDecimal(100));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Object> request = new HttpEntity<>(json.toString(), headers);
		ResponseEntity<String> restAPI = template.postForEntity(INVOICE_CONTROLLER_API, request, String.class);
		Assert.assertFalse(201 == restAPI.getStatusCodeValue());
	}
	
	@Test
	public void testInvoiceAPIOLDCustomerUserWithGroceryProductThreeYearsAgo() throws IOException {
		LocalDate created = LocalDate.of(2018, 5, 12);
		JSONObject json = createInvoiceBody(UserType.OLD_CUSTOMER, created, getProducts("groceries"),
				new BigDecimal(1000), new BigDecimal(100));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Object> request = new HttpEntity<>(json.toString(), headers);
		ResponseEntity<String> restAPI = template.postForEntity(INVOICE_CONTROLLER_API, request, String.class);
		Assert.assertTrue(201 == restAPI.getStatusCodeValue());
	}
	
	@Test
	public void testInvoiceAPIEmployeeUserWithOtherProduct() throws IOException {
		JSONObject json = createInvoiceBody(UserType.EMPLOYEE, LocalDate.now(), getProducts("other"),
				new BigDecimal(1000), new BigDecimal(100));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Object> request = new HttpEntity<>(json.toString(), headers);
		ResponseEntity<String> restAPI = template.postForEntity(INVOICE_CONTROLLER_API, request, String.class);
		Assert.assertTrue(201 == restAPI.getStatusCodeValue());
	}

	private JSONObject createInvoiceBody(UserType userType, LocalDate created, List<Product> products,
			BigDecimal GrandTotal, BigDecimal NetTotal) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("user", new JSONObject(new Gson().toJson(getUserObj(userType, created))));
			obj.put("invoice", new JSONObject(new Gson().toJson(getInvoice(products, GrandTotal, NetTotal))));
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}
		return obj;
	}
	
	private Map<String, String> getUserObj(UserType userType, LocalDate created) {
		User user = new User("Mohamed", "0123456789", userType, created);
		HashMap<String, String> userMap = new HashMap<String, String>();
		userMap.put("name", user.getName());
		userMap.put("phone", user.getPhone());
		userMap.put("created", created.toString());
		userMap.put("userType", userType.name());
		return userMap;
	}

	private Invoice getInvoice(List<Product> products, BigDecimal GrandTotal, BigDecimal NetTotal) {
		Invoice invoice = new Invoice(products, GrandTotal, NetTotal);
		return invoice;
	}

	private List<Product> getProducts(String type) {

		List<Product> products = new ArrayList<Product>();

		if (type.equals("mix")) {
			products.add(new Product("Product 1", new BigDecimal(10), ProductCategory.GROCERIES));
			products.add(new Product("Product 2", new BigDecimal(10), ProductCategory.CLOTHS));
			products.add(new Product("Product 3", new BigDecimal(10), ProductCategory.ELECTRONICS));
			products.add(new Product("Product 4", new BigDecimal(10), ProductCategory.CLOTHS));
		} else if (type.equals("groceries")) {
			products.add(new Product("Product 1", new BigDecimal(10), ProductCategory.GROCERIES));
			products.add(new Product("Product 2", new BigDecimal(10), ProductCategory.GROCERIES));
			products.add(new Product("Product 3", new BigDecimal(10), ProductCategory.GROCERIES));
			products.add(new Product("Product 4", new BigDecimal(10), ProductCategory.GROCERIES));
		} else if (type.equals("cloths")) {
			products.add(new Product("Product 1", new BigDecimal(10), ProductCategory.CLOTHS));
			products.add(new Product("Product 2", new BigDecimal(10), ProductCategory.CLOTHS));
			products.add(new Product("Product 3", new BigDecimal(10), ProductCategory.CLOTHS));
			products.add(new Product("Product 4", new BigDecimal(10), ProductCategory.CLOTHS));
		} else {
			products.add(new Product("Product 1", new BigDecimal(10), ProductCategory.GROCERIES));
			products.add(new Product("Product 2", new BigDecimal(10), ProductCategory.CLOTHS));
			products.add(new Product("Product 3", new BigDecimal(10), ProductCategory.GROCERIES));
			products.add(new Product("Product 4", new BigDecimal(10), ProductCategory.ELECTRONICS));
		}

		return products;
	}
}
