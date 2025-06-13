package com.validation.engine.poc;

import com.validation.engine.poc.model.ValidationResult;
import com.validation.engine.poc.model.ValidationRule;
import com.validation.engine.poc.strategy.ComputedExpressionValidationStrategy;
import com.validation.engine.poc.strategy.ExpectedTypeValidationStrategy;
import com.validation.engine.poc.strategy.LengthValidationStrategy;
import com.validation.engine.poc.strategy.RegexValidationStrategy;
import com.validation.engine.poc.strategy.RequiredValidationStrategy;
import com.validation.engine.poc.strategy.ValidationStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@SpringBootApplication
public class PocApplication {

	public static void main(String[] args) {
		System.out.println("=== Validation Engine POC Demo ===\n");

		// Initialize validation strategies
		List<ValidationStrategy> strategies = Arrays.asList(
				new RequiredValidationStrategy(),
				new RegexValidationStrategy(),
				new LengthValidationStrategy(),
				new ExpectedTypeValidationStrategy(),
				new ComputedExpressionValidationStrategy()
		);

		ValidationEngine engine = new ValidationEngine(strategies);

		// Run different test scenarios
		testBasicValidations(engine);
		testComplexPayload(engine);
		testComputedExpressions(engine);
		testInvalidScenarios(engine);
	}

	private static void testBasicValidations(ValidationEngine engine) {
		System.out.println("1. BASIC VALIDATIONS TEST");
		System.out.println("========================");

		// Create test payload
		Map<String, Object> payload = new HashMap<>();
		payload.put("email", "user@example.com");
		payload.put("age", 25);
		payload.put("name", "John Doe");
		payload.put("phone", "+923325523913");
		payload.put("password", "abcefghi");

		// Define validation rules
		Map<String, List<ValidationRule>> ruleMap = new HashMap<>();

		// Email validation
		ValidationRule emailRule = new ValidationRule();
		emailRule.setRequired(true);
		emailRule.setExpectedType("string");
		emailRule.setRegexPattern("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
		ruleMap.put("email", List.of(emailRule));

		// Age validation
		ValidationRule ageRule = new ValidationRule();
		ageRule.setRequired(true);
		ageRule.setExpectedType("integer");
		ruleMap.put("age", List.of(ageRule));

		// Name validation
		ValidationRule nameRule = new ValidationRule();
		nameRule.setRequired(true);
		nameRule.setMinLength(2);
		nameRule.setMaxLength(50);
		ruleMap.put("name", List.of(nameRule));

		// Phone validation (optional but if present, must be valid)
		ValidationRule phoneRule = new ValidationRule();
		phoneRule.setRequired(true);
		phoneRule.setRegexPattern("^\\+?[1-9]\\d{1,14}$");
		ruleMap.put("phone", List.of(phoneRule));

		// Password validation
		ValidationRule passwordRule = new ValidationRule();
		passwordRule.setRequired(true);
		passwordRule.setMinLength(8);
		ruleMap.put("password", List.of(passwordRule));

		// Run validation
		Map<String, ValidationResult> results = engine.validate(payload, ruleMap);

		// Print results
		printValidationResults(payload, results);
		System.out.println();
	}

	private static void testComplexPayload(ValidationEngine engine) {
		System.out.println("2. COMPLEX PAYLOAD TEST");
		System.out.println("=======================");

		// Create complex nested payload
		Map<String, Object> payload = new HashMap<>();
		payload.put("userId", 123);
		payload.put("username", "johndoe");

		Map<String, Object> profile = new HashMap<>();
		profile.put("firstName", "John");
		profile.put("lastName", "Doe");
		profile.put("email", "john.doe@example.com");
		payload.put("profile", profile);

		Map<String, Object> settings = new HashMap<>();
		settings.put("notifications", true);
		settings.put("theme", "dark");
		payload.put("settings", settings);

		// Define validation rules for complex payload
		Map<String, List<ValidationRule>> ruleMap = new HashMap<>();

		// User ID validation
		ValidationRule userIdRule = new ValidationRule();
		userIdRule.setRequired(true);
		userIdRule.setExpectedType("integer");
		ruleMap.put("userId", List.of(userIdRule));

		// Username validation
		ValidationRule usernameRule = new ValidationRule();
		usernameRule.setRequired(true);
		usernameRule.setMinLength(3);
		usernameRule.setMaxLength(20);
		usernameRule.setRegexPattern("^[a-zA-Z0-9_]+$");
		ruleMap.put("username", List.of(usernameRule));

		// Profile email validation (nested field)
		ValidationRule profileEmailRule = new ValidationRule();
		profileEmailRule.setRequired(true);
		profileEmailRule.setRegexPattern("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
		ruleMap.put("profile.email", List.of(profileEmailRule));

		// Settings theme validation
		ValidationRule themeRule = new ValidationRule();
		themeRule.setRequired(true);
		themeRule.setRegexPattern("^(light|dark)$");
		ruleMap.put("settings.theme", List.of(themeRule));

		// Run validation
		Map<String, ValidationResult> results = engine.validate(payload, ruleMap);

		// Print results
		printValidationResults(payload, results);
		System.out.println();
	}

	private static void testComputedExpressions(ValidationEngine engine) {
		System.out.println("3. COMPUTED EXPRESSIONS TEST");
		System.out.println("============================");

		// Create payload with data for computed expressions
		Map<String, Object> payload = new HashMap<>();
		payload.put("startDate", "2024-01-01");
		payload.put("endDate", "2024-12-31");
		payload.put("price", 100.0);
		payload.put("quantity", 5);
		payload.put("discount", 10.0);
		payload.put("minOrderAmount", 400.0);

		// Create order items for complex calculations
		List<Map<String, Object>> items = new ArrayList<>();
		Map<String, Object> item1 = new HashMap<>();
		item1.put("price", 50.0);
		item1.put("quantity", 2);
		items.add(item1);

		Map<String, Object> item2 = new HashMap<>();
		item2.put("price", 75.0);
		item2.put("quantity", 4);
		items.add(item2);

		payload.put("items", items);

		// Define validation rules with computed expressions
		Map<String, List<ValidationRule>> ruleMap = new HashMap<>();

		// Price validation - must be positive
		ValidationRule priceRule = new ValidationRule();
		priceRule.setComputedExpression("price > 0");
		ruleMap.put("price", List.of(priceRule));

		// Quantity validation - must be between 1 and 10
		ValidationRule quantityRule = new ValidationRule();
		quantityRule.setComputedExpression("quantity >= 1 && quantity <= 10");
		ruleMap.put("quantity", List.of(quantityRule));

		// Total validation - total should meet minimum order amount
		ValidationRule totalRule = new ValidationRule();
		totalRule.setComputedExpression("(price * quantity) - discount >= minOrderAmount");
		ruleMap.put("price", Arrays.asList(priceRule, totalRule));

		// Date validation - end date should be after start date (simplified string comparison)
		ValidationRule dateRule = new ValidationRule();
		dateRule.setComputedExpression("endDate > startDate");
		ruleMap.put("endDate", List.of(dateRule));

		// Item validation - their sum product should be greater than 100
		ValidationRule itemRule = new ValidationRule();
		itemRule.setComputedExpression("math:sumProjection('items[*].price * items[*].quantity') > 100");
		ruleMap.put("items", List.of(itemRule));

		// Run validation
		Map<String, ValidationResult> results = engine.validate(payload, ruleMap);

		// Print results
		printValidationResults(payload, results);
		System.out.println();
	}

	private static void testInvalidScenarios(ValidationEngine engine) {
		System.out.println("4. INVALID SCENARIOS TEST");
		System.out.println("=========================");

		// Create payload with various invalid data
		Map<String, Object> payload = new HashMap<>();
		payload.put("email", "invalid-email");
		payload.put("age", "not-a-number");
		payload.put("name", "A"); // Too short
		payload.put("phone", null);
		payload.put("longText", "A".repeat(101)); // Too long

		// Define strict validation rules
		Map<String, List<ValidationRule>> ruleMap = new HashMap<>();

		// Email validation
		ValidationRule emailRule = new ValidationRule();
		emailRule.setRequired(true);
		emailRule.setExpectedType("string");
		emailRule.setRegexPattern("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
		ruleMap.put("email", List.of(emailRule));

		// Age validation
		ValidationRule ageRule = new ValidationRule();
		ageRule.setRequired(true);
		ageRule.setExpectedType("integer");
		ruleMap.put("age", List.of(ageRule));

		// Name validation
		ValidationRule nameRule = new ValidationRule();
		nameRule.setRequired(true);
		nameRule.setMinLength(2);
		nameRule.setMaxLength(50);
		ruleMap.put("name", List.of(nameRule));

		// Phone validation
		ValidationRule phoneRule = new ValidationRule();
		phoneRule.setRequired(true);
		ruleMap.put("phone", List.of(phoneRule));

		// Long text validation
		ValidationRule longTextRule = new ValidationRule();
		longTextRule.setMaxLength(100);
		ruleMap.put("longText", List.of(longTextRule));

		// Run validation
		Map<String, ValidationResult> results = engine.validate(payload, ruleMap);

		// Print results
		printValidationResults(payload, results);
		System.out.println();
	}

	private static void printValidationResults(Map<String, Object> payload, Map<String, ValidationResult> results) {
		System.out.println("Payload: " + payload);
		System.out.println("\nValidation Results:");
		System.out.println("------------------");

		boolean overallValid = true;
		for (Map.Entry<String, ValidationResult> entry : results.entrySet()) {
			String fieldPath = entry.getKey();
			ValidationResult result = entry.getValue();

			System.out.printf("Field: %s\n", fieldPath);
			System.out.printf("  Valid: %s\n", result.isValid());

			if (!result.isValid()) {
				overallValid = false;
				System.out.println("  Errors:");
				for (String error : result.getErrors()) {
					System.out.printf("    - %s\n", error);
				}
			}
			System.out.println();
		}

		System.out.println("Overall Result: " + (overallValid ? "✅ VALID" : "❌ INVALID"));
	}

//    public static void main(String[] args) {
////        SpringApplication.run(PocApplication.class, args);
//
//		List<Map<String, Object>> items = List.of(
//				Map.of("price", 10, "qty", 3),
//				Map.of("price", 20, "qty", 4)
//		);
//
//		Map<String, Object> data = ExpressionEvaluator.flattenItems(items);
//
//		String expr = "total < 100"; // derived from: items[*].price * items[*].qty > 100
//
//		ExpressionEvaluator evaluator = new ExpressionEvaluator();
//		Object result = evaluator.evaluate(expr, data);
//
//		System.out.println("Result: " + result); // Should print: true
//
//
//		data = Map.of(
//				"car", List.of(
//						Map.of("tire", Map.of("price", 10, "qty", 3)),
//						Map.of("tire", Map.of("price", 20, "qty", 2))
//				)
//				,
//				"shopping", Map.of(
//						"item", List.of(
//								Map.of("price", 30, "qty", 1),
//								Map.of("price", 15, "qty", 2)
//						)
//				)
//		);
//
//		evaluator = new ExpressionEvaluator(data);
//
////         evaluator = new ExpressionEvaluator();
//
//		String expr1 = "math:sumProjection('shopping.item[*].price * shopping.item[*].qty') == 60";
//		String expr2 = "math:sumProjection('car[*].tire.price * car[*].tire.qty') > 50";
//
//		System.out.println("Expr1 = " + evaluator.evaluate(expr1, data)); // true
//		System.out.println("Expr2 = " + evaluator.evaluate(expr2, data)); // true
//
//    }

}
