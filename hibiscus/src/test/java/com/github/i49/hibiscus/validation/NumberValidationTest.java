package com.github.i49.hibiscus.validation;

import static com.github.i49.hibiscus.schema.types.JsonTypes.*;
import static org.junit.Assert.*;
import org.junit.Test;

import com.github.i49.hibiscus.schema.TypeId;
import com.github.i49.hibiscus.schema.problems.Problem;
import com.github.i49.hibiscus.schema.problems.TypeMismatchProblem;
import com.github.i49.hibiscus.schema.types.JsonType;

import java.io.StringReader;

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
		assertEquals(TypeId.STRING, ((TypeMismatchProblem)p).getActualType());
	}
}
