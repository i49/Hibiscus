package com.github.i49.hibiscus.validation;

import static com.github.i49.hibiscus.schema.JsonTypes.*;
import static org.junit.Assert.*;
import org.junit.Test;

import com.github.i49.hibiscus.common.Bound;
import com.github.i49.hibiscus.common.TypeId;
import com.github.i49.hibiscus.problems.LessThanMinimumProblem;
import com.github.i49.hibiscus.problems.MoreThanMaximumProblem;
import com.github.i49.hibiscus.problems.NotLessThanMaximumProblem;
import com.github.i49.hibiscus.problems.NotMoreThanMinimumProblem;
import com.github.i49.hibiscus.problems.Problem;
import com.github.i49.hibiscus.problems.TypeMismatchProblem;
import com.github.i49.hibiscus.schema.JsonType;

import java.io.StringReader;
import java.math.BigDecimal;

public class NumberValidationTest extends BaseValidationTest {

	@Test
	public void postiveNumber() {
		String json = "[123.45]";
		JsonType schema = array(number());
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void negativeNumber() {
		String json = "[-123.45]";
		JsonType schema = array(number());
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void integralNumber() {
		String json = "[123]";
		JsonType schema = array(number());
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void notNumberButString() {
		String json = "[\"123.45\"]";
		JsonType schema = array(bool());
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		Problem p = result.getProblems().get(0);
		assertTrue(p instanceof TypeMismatchProblem);
		assertEquals(TypeId.STRING, ((TypeMismatchProblem)p).getActualType());
		assertNotNull(p.getMessage());
	}

	@Test
	public void numberOfMinimum() {
		String json = "[12.340]";
		JsonType schema = array(number().min(new BigDecimal("12.34")));
		
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void numberLessThanMinimum() {
		String json = "[12.33]";
		JsonType schema = array(number().min(new BigDecimal("12.34")));
		
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof LessThanMinimumProblem);
		LessThanMinimumProblem p = (LessThanMinimumProblem)result.getProblems().get(0);
		assertEquals(new BigDecimal("12.33"), p.getActualValue().bigDecimalValue());
		Bound<BigDecimal> bound = p.getBound();
		assertFalse(bound.isExclusive());
		assertEquals(new BigDecimal("12.34"), bound.getValue());
		assertNotNull(p.getMessage());
	}

	@Test
	public void numberMoreThanExlusiveMinimum() {
		String json = "[12.35]";
		JsonType schema = array(number().min(new BigDecimal("12.34")).exclusiveMin(true));
		
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void numberEqualToExlusiveMinimum() {
		String json = "[12.340]";
		JsonType schema = array(number().min(new BigDecimal("12.34")).exclusiveMin(true));
		
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof NotMoreThanMinimumProblem);
		NotMoreThanMinimumProblem p = (NotMoreThanMinimumProblem)result.getProblems().get(0);
		assertEquals(new BigDecimal("12.340"), p.getActualValue().bigDecimalValue());
		Bound<BigDecimal> bound = p.getBound();
		assertTrue(bound.isExclusive());
		assertEquals(new BigDecimal("12.34"), bound.getValue());
		assertNotNull(p.getMessage());
	}

	@Test
	public void numberOfMaximum() {
		String json = "[56.780]";
		JsonType schema = array(number().max(new BigDecimal("56.78")));
		
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void numberGreaterThanMaximum() {
		String json = "[56.79]";
		JsonType schema = array(number().max(new BigDecimal("56.78")));
		
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof MoreThanMaximumProblem);
		MoreThanMaximumProblem p = (MoreThanMaximumProblem)result.getProblems().get(0);
		assertEquals(new BigDecimal("56.79"), p.getActualValue().bigDecimalValue());
		Bound<BigDecimal> bound = p.getBound();
		assertFalse(bound.isExclusive());
		assertEquals(new BigDecimal("56.78"), bound.getValue());
		assertNotNull(p.getMessage());
	}

	@Test
	public void numberLessThanExclusiveMaximum() {
		String json = "[56.77]";
		JsonType schema = array(number().max(new BigDecimal("56.78")).exclusiveMax(true));
		
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void numberEqualToExclusiveMaximum() {
		String json = "[56.780]";
		JsonType schema = array(number().max(new BigDecimal("56.78")).exclusiveMax(true));
		
		JsonValidator validator = new JsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof NotLessThanMaximumProblem);
		NotLessThanMaximumProblem p = (NotLessThanMaximumProblem)result.getProblems().get(0);
		assertEquals(new BigDecimal("56.780"), p.getActualValue().bigDecimalValue());
		Bound<BigDecimal> bound = p.getBound();
		assertTrue(bound.isExclusive());
		assertEquals(new BigDecimal("56.78"), bound.getValue());
		assertNotNull(p.getMessage());
	}
}
