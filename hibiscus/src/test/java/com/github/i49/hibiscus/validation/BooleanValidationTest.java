package com.github.i49.hibiscus.validation;

import static com.github.i49.hibiscus.schema.JsonTypes.*;
import static org.junit.Assert.*;
import org.junit.Test;

import com.github.i49.hibiscus.common.TypeId;
import com.github.i49.hibiscus.problems.TypeMismatchProblem;
import com.github.i49.hibiscus.problems.UnknownValueProblem;
import com.github.i49.hibiscus.schema.ComplexType;

import java.io.StringReader;
import java.util.Set;

import javax.json.JsonValue;

public class BooleanValidationTest extends BaseValidationTest {

	@Test
	public void booleanOfTrue() {
		String json = "[true]";
		ComplexType schema = array(bool());
		JsonValidator validator = new BasicJsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void booleanOfFalse() {
		String json = "[false]";
		ComplexType schema = array(bool());
		JsonValidator validator = new BasicJsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void notBooleanButString() {
		String json = "[\"true\"]";
		ComplexType schema = array(bool());
		JsonValidator validator = new BasicJsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof TypeMismatchProblem);
		TypeMismatchProblem p = (TypeMismatchProblem)result.getProblems().get(0);
		assertEquals(TypeId.STRING, p.getActualType());
		assertNotNull(p.getDescription());
	}
	
	@Test
	public void booleanOfAllowedValue() {
		String json = "[true]";
		ComplexType schema = array(bool().values(true));
		JsonValidator validator = new BasicJsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void booleanOfNotAllowedValue() {
		String json = "[false]";
		ComplexType schema = array(bool().values(true));
		JsonValidator validator = new BasicJsonValidator(schema);
		result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof UnknownValueProblem);
		UnknownValueProblem p = (UnknownValueProblem)result.getProblems().get(0);
		assertEquals(JsonValue.FALSE, p.getActualValue());
		Set<JsonValue> expected = p.getExpectedValues();
		assertEquals(1, expected.size());
		assertNotNull(p.getDescription());
	}
}
