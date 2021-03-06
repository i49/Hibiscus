package com.github.i49.hibiscus.validation;

import static com.github.i49.hibiscus.schema.SchemaComponents.*;
import static org.junit.Assert.*;
import org.junit.Test;

import com.github.i49.hibiscus.common.Bound;
import com.github.i49.hibiscus.common.TypeId;
import com.github.i49.hibiscus.problems.InclusiveLowerBoundProblem;
import com.github.i49.hibiscus.problems.InclusiveUpperBoundProblem;
import com.github.i49.hibiscus.problems.ExclusiveUpperBoundProblem;
import com.github.i49.hibiscus.problems.ExclusiveLowerBoundProblem;
import com.github.i49.hibiscus.problems.Problem;
import com.github.i49.hibiscus.problems.TypeMismatchProblem;
import com.github.i49.hibiscus.problems.NoSuchEnumeratorProblem;
import com.github.i49.hibiscus.schema.Schema;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Set;

import javax.json.JsonNumber;

import static com.github.i49.hibiscus.validation.CustomAssertions.*;

public class NumberValidationTest {

	/**
	 * Tests of various kinds of values.
	 */
	public static class NumberValueTest {
	
		@Test
		public void postiveNumber() {
			String json = "[123.45]";
			Schema schema = schema(array(number()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void negativeNumber() {
			String json = "[-123.45]";
			Schema schema = schema(array(number()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void zero() {
			String json = "[0.0]";
			Schema schema = schema(array(number()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}

		@Test
		public void integralNumber() {
			String json = "[123]";
			Schema schema = schema(array(number()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	}

	public static class TypeMismatchTest {
	
		@Test
		public void notNumberButString() {
			String json = "[\"123.45\"]";
			Schema schema = schema(array(bool()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			Problem p = result.getProblems().get(0);
			assertTrue(p instanceof TypeMismatchProblem);
			assertEquals(TypeId.STRING, ((TypeMismatchProblem)p).getActualType());
			assertNotNull(p.getDescription());
		}
	}

	public static class EnumerationTest {

		@Test
		public void notExistInNone() {
			String json = "[12.34]";
			Schema schema = schema(array(number().enumeration()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof NoSuchEnumeratorProblem);
			NoSuchEnumeratorProblem p = (NoSuchEnumeratorProblem)result.getProblems().get(0);
			assertEquals(new BigDecimal("12.34"), ((JsonNumber)p.getCauseValue()).bigDecimalValue());
			Set<Object> expected = p.getEnumerators();
			assertEquals(0, expected.size());
			assertNotNull(p.getDescription());
		}

		@Test
		public void existInOne() {
			String json = "[12.34]";
			Schema schema = schema(array(number().enumeration(new BigDecimal("12.34"))));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}

		@Test
		public void notExistInOne() {
			String json = "[12.34]";
			Schema schema = schema(array(number().enumeration(new BigDecimal("56.78"))));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof NoSuchEnumeratorProblem);
			NoSuchEnumeratorProblem p = (NoSuchEnumeratorProblem)result.getProblems().get(0);
			assertEquals(new BigDecimal("12.34"), ((JsonNumber)p.getCauseValue()).bigDecimalValue());
			Set<Object> expected = p.getEnumerators();
			assertEquals(1, expected.size());
			assertNotNull(p.getDescription());
		}

		@Test
		public void existInMany() {
			String json = "[12.34]";
			Schema schema = schema(array(number().enumeration(new BigDecimal("56.78"), new BigDecimal("12.34"))));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void notExistInMany() {
			String json = "[3.14]";
			Schema schema = schema(array(number().enumeration(new BigDecimal("12.34"), new BigDecimal("56.78"))));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof NoSuchEnumeratorProblem);
			NoSuchEnumeratorProblem p = (NoSuchEnumeratorProblem)result.getProblems().get(0);
			assertEquals(new BigDecimal("3.14"), ((JsonNumber)p.getCauseValue()).bigDecimalValue());
			Set<Object> expected = p.getEnumerators();
			assertEquals(2, expected.size());
			assertNotNull(p.getDescription());
		}
	}
	
	public static class MinInclusiveTest {

		@Test
		public void lessThanMinimum() {
			String json = "[12.33]";
			Schema schema = schema(array(number().minInclusive(new BigDecimal("12.34"))));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof InclusiveLowerBoundProblem);
			InclusiveLowerBoundProblem p = (InclusiveLowerBoundProblem)result.getProblems().get(0);
			assertEquals(new BigDecimal("12.33"), p.getCauseValue().bigDecimalValue());
			Bound<BigDecimal> bound = p.getBound();
			assertFalse(bound.isExclusive());
			assertEquals(new BigDecimal("12.34"), bound.getValue());
			assertNotNull(p.getDescription());
		}

		@Test
		public void equalToMinimum() {
			String json = "[12.340]";
			Schema schema = schema(array(number().minInclusive(new BigDecimal("12.34"))));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}

		@Test
		public void moreThanMinimum() {
			String json = "[12.35]";
			Schema schema = schema(array(number().minInclusive(new BigDecimal("12.34"))));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	}

	public static class MinExclusiveTest {

		@Test
		public void equalToMinimum() {
			String json = "[12.340]";
			Schema schema = schema(array(number().minExclusive(new BigDecimal("12.34"))));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof ExclusiveLowerBoundProblem);
			ExclusiveLowerBoundProblem p = (ExclusiveLowerBoundProblem)result.getProblems().get(0);
			assertEquals(new BigDecimal("12.340"), p.getCauseValue().bigDecimalValue());
			Bound<BigDecimal> bound = p.getBound();
			assertTrue(bound.isExclusive());
			assertEquals(new BigDecimal("12.34"), bound.getValue());
			assertNotNull(p.getDescription());
		}

		@Test
		public void moreThanMinimum() {
			String json = "[12.35]";
			Schema schema = schema(array(number().minExclusive(new BigDecimal("12.34"))));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	}

	public static class MaxInclusiveTest {

		@Test
		public void equalToMaximum() {
			String json = "[56.780]";
			Schema schema = schema(array(number().maxInclusive(new BigDecimal("56.78"))));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void moreThanMaximum() {
			String json = "[56.79]";
			Schema schema = schema(array(number().maxInclusive(new BigDecimal("56.78"))));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof InclusiveUpperBoundProblem);
			InclusiveUpperBoundProblem p = (InclusiveUpperBoundProblem)result.getProblems().get(0);
			assertEquals(new BigDecimal("56.79"), p.getCauseValue().bigDecimalValue());
			Bound<BigDecimal> bound = p.getBound();
			assertFalse(bound.isExclusive());
			assertEquals(new BigDecimal("56.78"), bound.getValue());
			assertNotNull(p.getDescription());
		}
	}
	
	public static class MaxExclusiveTest {

		@Test
		public void lessThanMaximum() {
			String json = "[56.77]";
			Schema schema = schema(array(number().maxExclusive(new BigDecimal("56.78"))));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void equalToMaximum() {
			String json = "[56.780]";
			Schema schema = schema(array(number().maxExclusive(new BigDecimal("56.78"))));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof ExclusiveUpperBoundProblem);
			ExclusiveUpperBoundProblem p = (ExclusiveUpperBoundProblem)result.getProblems().get(0);
			assertEquals(new BigDecimal("56.780"), p.getCauseValue().bigDecimalValue());
			Bound<BigDecimal> bound = p.getBound();
			assertTrue(bound.isExclusive());
			assertEquals(new BigDecimal("56.78"), bound.getValue());
			assertNotNull(p.getDescription());
		}
	}
}
