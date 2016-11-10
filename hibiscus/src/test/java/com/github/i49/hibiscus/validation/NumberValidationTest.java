package com.github.i49.hibiscus.validation;

import static com.github.i49.hibiscus.schema.types.JsonTypes.*;
import static org.junit.Assert.*;
import org.junit.Test;

import com.github.i49.hibiscus.schema.Range;
import com.github.i49.hibiscus.schema.TypeId;
import com.github.i49.hibiscus.schema.problems.NotMoreThanMinimumProblem;
import com.github.i49.hibiscus.schema.problems.NotLessThanMaximumProblem;
import com.github.i49.hibiscus.schema.problems.LessThanMinimumProblem;
import com.github.i49.hibiscus.schema.problems.Problem;
import com.github.i49.hibiscus.schema.problems.TypeMismatchProblem;
import com.github.i49.hibiscus.schema.problems.MoreThanMaximumProblem;
import com.github.i49.hibiscus.schema.types.JsonType;

import java.io.StringReader;
import java.math.BigDecimal;

public class NumberValidationTest {

	@Test
	public void testValidateNumber() {
		String json = "[123.45]";
		JsonType schema = array(number());
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testValidateNegativeNumber() {
		String json = "[-123.45]";
		JsonType schema = array(number());
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testValidateIntegralNumber() {
		String json = "[123]";
		JsonType schema = array(number());
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testTypeMismatch() {
		String json = "[\"123.45\"]";
		JsonType schema = array(bool());
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		Problem p = result.getProblems().get(0);
		assertTrue(p instanceof TypeMismatchProblem);
		assertEquals(TypeId.STRING, ((TypeMismatchProblem)p).getInstanceType());
	}

	@Test
	public void testMinimum() {
		String json = "[12.340]";
		JsonType schema = array(number().min(new BigDecimal("12.34")));
		
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testLessThanMinimum() {
		String json = "[12.33]";
		JsonType schema = array(number().min(new BigDecimal("12.34")));
		
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof LessThanMinimumProblem);
		LessThanMinimumProblem p = (LessThanMinimumProblem)result.getProblems().get(0);
		assertEquals(new BigDecimal("12.33"), p.getInstanceValue());
		Range<BigDecimal> range = p.getAllowedRange();
		assertTrue(range.hasMinimum());
		assertFalse(range.hasExlusiveMinimum());
		assertFalse(range.hasMaximum());
		assertFalse(range.hasExclusiveMaximum());
		assertEquals(new BigDecimal("12.34"), range.getMinimum());
		String m = p.getMessage();
		assertNotNull(m);
	}

	@Test
	public void testExclusiveMinimum() {
		String json = "[12.35]";
		JsonType schema = array(number().min(new BigDecimal("12.34")).exclusiveMin(true));
		
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testEqualToExclusiveMinimum() {
		String json = "[12.340]";
		JsonType schema = array(number().min(new BigDecimal("12.34")).exclusiveMin(true));
		
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof NotMoreThanMinimumProblem);
		NotMoreThanMinimumProblem p = (NotMoreThanMinimumProblem)result.getProblems().get(0);
		assertEquals(new BigDecimal("12.340"), p.getInstanceValue());
		Range<BigDecimal> range = p.getAllowedRange();
		assertTrue(range.hasMinimum());
		assertTrue(range.hasExlusiveMinimum());
		assertFalse(range.hasMaximum());
		assertFalse(range.hasExclusiveMaximum());
		assertEquals(new BigDecimal("12.34"), range.getMinimum());
		String m = p.getMessage();
		assertNotNull(m);
	}

	@Test
	public void testMaximum() {
		String json = "[56.780]";
		JsonType schema = array(number().max(new BigDecimal("56.78")));
		
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testGreaterThanMaximum() {
		String json = "[56.79]";
		JsonType schema = array(number().max(new BigDecimal("56.78")));
		
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof MoreThanMaximumProblem);
		MoreThanMaximumProblem p = (MoreThanMaximumProblem)result.getProblems().get(0);
		assertEquals(new BigDecimal("56.79"), p.getInstanceValue());
		Range<BigDecimal> range = p.getAllowedRange();
		assertFalse(range.hasMinimum());
		assertFalse(range.hasExlusiveMinimum());
		assertTrue(range.hasMaximum());
		assertFalse(range.hasExclusiveMaximum());
		assertEquals(new BigDecimal("56.78"), range.getMaximum());
		String m = p.getMessage();
		assertNotNull(m);
	}

	@Test
	public void testExclusiveMaximum() {
		String json = "[56.77]";
		JsonType schema = array(number().max(new BigDecimal("56.78")).exclusiveMax(true));
		
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testEqualToExclusiveMaximum() {
		String json = "[56.780]";
		JsonType schema = array(number().max(new BigDecimal("56.78")).exclusiveMax(true));
		
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof NotLessThanMaximumProblem);
		NotLessThanMaximumProblem p = (NotLessThanMaximumProblem)result.getProblems().get(0);
		assertEquals(new BigDecimal("56.780"), p.getInstanceValue());
		Range<BigDecimal> range = p.getAllowedRange();
		assertFalse(range.hasMinimum());
		assertFalse(range.hasExlusiveMinimum());
		assertTrue(range.hasMaximum());
		assertTrue(range.hasExclusiveMaximum());
		assertEquals(new BigDecimal("56.78"), range.getMaximum());
		String m = p.getMessage();
		assertNotNull(m);
	}
}
