package com.github.i49.hibiscus.validation;

import static com.github.i49.hibiscus.schema.SchemaComponents.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.i49.hibiscus.common.Bound;
import com.github.i49.hibiscus.common.TypeId;
import com.github.i49.hibiscus.problems.AssertionFailureProblem;
import com.github.i49.hibiscus.problems.InclusiveLowerBoundProblem;
import com.github.i49.hibiscus.problems.InclusiveUpperBoundProblem;
import com.github.i49.hibiscus.problems.ExclusiveUpperBoundProblem;
import com.github.i49.hibiscus.problems.ExclusiveLowerBoundProblem;
import com.github.i49.hibiscus.problems.TypeMismatchProblem;
import com.github.i49.hibiscus.problems.NoSuchEnumeratorProblem;
import com.github.i49.hibiscus.schema.Schema;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Set;

import javax.json.JsonNumber;

import static com.github.i49.hibiscus.validation.CustomAssertions.*;

public class IntegerValidationTest {

	/**
	 * Tests of various kinds of values.
	 */
	public static class IntegerValueTest {

		@Test
		public void positiveInteger() {
			String json = "[123]";
			Schema schema = schema(array(integer()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}

		@Test
		public void negativeInteger() {
			String json = "[-456]";
			Schema schema = schema(array(integer()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void zero() {
			String json = "[0]";
			Schema schema = schema(array(integer()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}

		@Test
		public void integerOfMaxInteger() {
			String json = "[2147483647]";
			Schema schema = schema(array(integer()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void integerOfMinInteger() {
			String json = "[-2147483648]";
			Schema schema = schema(array(integer()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void integerOfMaxLong() {
			String json = "[9223372036854775807]";
			Schema schema = schema(array(integer()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void integerOfMinLong() {
			String json = "[-9223372036854775808]";
			Schema schema = schema(array(integer()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	}
	
	public static class TypeMismatchTest {

		@Test
		public void notIntegerButNumber() {
			String json = "[123.45]";
			Schema schema = schema(array(integer()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof TypeMismatchProblem);
			TypeMismatchProblem p = (TypeMismatchProblem)result.getProblems().get(0);
			assertEquals(TypeId.NUMBER, p.getActualType());
			assertNotNull(p.getDescription());
		}
	
		@Test
		public void notIntegerButString() {
			String json = "[\"123\"]";
			Schema schema = schema(array(integer()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof TypeMismatchProblem);
			TypeMismatchProblem p = (TypeMismatchProblem)result.getProblems().get(0);
			assertEquals(TypeId.STRING, p.getActualType());
			assertNotNull(p.getDescription());
		}
	}
	
	public static class EnumerationTest {
	
		@Test
		public void notExistInNone() {
			String json = "[1]";
			Schema schema = schema(array(integer().enumeration()));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof NoSuchEnumeratorProblem);
			NoSuchEnumeratorProblem p = (NoSuchEnumeratorProblem)result.getProblems().get(0);
			assertEquals(1, ((JsonNumber)p.getCauseValue()).intValue());
			Set<Object> expected = p.getEnumerators();
			assertEquals(0, expected.size());
			assertNotNull(p.getDescription());
		}

		@Test
		public void existInOne() {
			String json = "[123]";
			Schema schema = schema(array(integer().enumeration(123)));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}

		@Test
		public void notExistInOne() {
			String json = "[12]";
			Schema schema = schema(array(integer().enumeration(123)));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof NoSuchEnumeratorProblem);
			NoSuchEnumeratorProblem p = (NoSuchEnumeratorProblem)result.getProblems().get(0);
			assertEquals(12, ((JsonNumber)p.getCauseValue()).intValue());
			Set<Object> expected = p.getEnumerators();
			assertEquals(1, expected.size());
			assertNotNull(p.getDescription());
		}

		@Test
		public void existInMany() {
			String json = "[12]";
			Schema schema = schema(array(integer().enumeration(1, 12, 123)));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void notExistInMany() {
			String json = "[42]";
			Schema schema = schema(array(integer().enumeration(1, 12, 123)));
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof NoSuchEnumeratorProblem);
			NoSuchEnumeratorProblem p = (NoSuchEnumeratorProblem)result.getProblems().get(0);
			assertEquals(42, ((JsonNumber)p.getCauseValue()).intValue());
			Set<Object> expected = p.getEnumerators();
			assertEquals(3, expected.size());
			assertNotNull(p.getDescription());
		}
	}
	
	public static class MinInclusiveTest {
		
		@Test
		public void lessThanMinimum() {
			String json = "[27]";
			Schema schema = schema(array(integer().minInclusive(28)));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof InclusiveLowerBoundProblem);
			InclusiveLowerBoundProblem p = (InclusiveLowerBoundProblem)result.getProblems().get(0);
			assertEquals(27, p.getCauseValue().intValue());
			Bound<BigDecimal> bound = p.getBound();
			assertFalse(bound.isExclusive());
			assertEquals(28, bound.getValue().intValue());
			assertNotNull(p.getDescription());
		}

		@Test
		public void equalToMinimum() {
			String json = "[28]";
			Schema schema = schema(array(integer().minInclusive(28)));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}

		@Test
		public void moreThanMinimum() {
			String json = "[29]";
			Schema schema = schema(array(integer().minInclusive(28)));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	}
	
	public static class MinExclusiveTest {
		
		@Test
		public void equalToMinimum() {
			String json = "[28]";
			Schema schema = schema(array(integer().minExclusive(28)));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof ExclusiveLowerBoundProblem);
			ExclusiveLowerBoundProblem p = (ExclusiveLowerBoundProblem)result.getProblems().get(0);
			assertEquals(28, p.getCauseValue().intValue());
			Bound<BigDecimal> bound = p.getBound();
			assertTrue(bound.isExclusive());
			assertEquals(28, bound.getValue().intValue());
			assertNotNull(p.getDescription());
		}

		@Test
		public void moreThanMinimum() {
			String json = "[29]";
			Schema schema = schema(array(integer().minExclusive(28)));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	}

	public static class MaxInclusiveTest {
		
		@Test
		public void equalToMaximum() {
			String json = "[31]";
			Schema schema = schema(array(integer().maxInclusive(31)));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void moreThanMaximum() {
			String json = "[32]";
			Schema schema = schema(array(integer().maxInclusive(31)));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof InclusiveUpperBoundProblem);
			InclusiveUpperBoundProblem p = (InclusiveUpperBoundProblem)result.getProblems().get(0);
			assertEquals(32, p.getCauseValue().intValue());
			Bound<BigDecimal> bound = p.getBound();
			assertFalse(bound.isExclusive());
			assertEquals(31, bound.getValue().intValue());
			assertNotNull(p.getDescription());
		}
	}
	
	public static class MaxExclusiveTest {
		
		@Test
		public void lessThanMaximum() {
			String json = "[30]";
			Schema schema = schema(array(integer().maxExclusive(31)));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}
	
		@Test
		public void equalToMaximum() {
			String json = "[31]";
			Schema schema = schema(array(integer().maxExclusive(31)));
			
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof ExclusiveUpperBoundProblem);
			ExclusiveUpperBoundProblem p = (ExclusiveUpperBoundProblem)result.getProblems().get(0);
			assertEquals(31, p.getCauseValue().intValue());
			Bound<BigDecimal> bound = p.getBound();
			assertTrue(bound.isExclusive());
			assertEquals(31, bound.getValue().intValue());
			assertNotNull(p.getDescription());
		}
	}

	public static class AssertionTest {

		private Schema schema;
		
		@Before
		public void setUp() {
			schema = schema(array(integer().assertion(
					v->((v.intValue() % 2) == 0), 
					(v, l)->"Value must be a even number."
					)));
		}
		
		@Test
		public void success() {
			String json = "[30]";
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertFalse(result.hasProblems());
		}

		@Test
		public void failure() {
			String json = "[31]";
			JsonValidator validator = new BasicJsonValidator(schema);
			ValidationResult result = validator.validate(new StringReader(json));
	
			assertResultValid(result, json);
			assertEquals(1, result.getProblems().size());
			assertTrue(result.getProblems().get(0) instanceof AssertionFailureProblem);
			AssertionFailureProblem<?> p = (AssertionFailureProblem<?>)result.getProblems().get(0);
			assertEquals(31, ((JsonNumber)p.getCauseValue()).intValue());
			assertEquals("Value must be a even number.", p.getDescription());
		}
	}
}
