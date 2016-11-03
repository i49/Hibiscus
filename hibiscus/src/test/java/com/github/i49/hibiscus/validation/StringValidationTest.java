package com.github.i49.hibiscus.validation;

import static com.github.i49.schema.types.SchemaComponents.*;
import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import com.github.i49.schema.TypeId;
import com.github.i49.schema.problems.Problem;
import com.github.i49.schema.problems.StringLengthProblem;
import com.github.i49.schema.problems.TypeMismatchProblem;
import com.github.i49.schema.types.ValueType;

public class StringValidationTest {

	@Test
	public void testValidateString() {
		String json = "[\"abc\"]";
		ValueType schema = array(string());
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testValidateBlankString() {
		String json = "[\"\"]";
		ValueType schema = array(string());
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testTypeMismatch() {
		String json = "[123]";
		ValueType schema = array(string());
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		Problem p = result.getProblems().get(0);
		assertTrue(p instanceof TypeMismatchProblem);
		assertEquals(TypeId.INTEGER, ((TypeMismatchProblem)p).getActualType());
	}
	
	@Test
	public void testMinLength() {
		
		String json = "[\"abc\"]";
		ValueType schema = array(string().minLength(3));
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testMinLength2() {
		
		String json = "[\"ab\"]";
		ValueType schema = array(string().minLength(3));
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof StringLengthProblem);
		StringLengthProblem p = (StringLengthProblem)result.getProblems().get(0);
		assertEquals(3, p.getThreshold());
		assertEquals(2, p.getActualLength());
	}

	@Test
	public void testMaxLength() {
		
		String json = "[\"abcd\"]";
		ValueType schema = array(string().maxLength(4));
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}

	@Test
	public void testMaxLength2() {
		
		String json = "[\"abcde\"]";
		ValueType schema = array(string().maxLength(4));
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertEquals(1, result.getProblems().size());
		assertTrue(result.getProblems().get(0) instanceof StringLengthProblem);
		StringLengthProblem p = (StringLengthProblem)result.getProblems().get(0);
		assertEquals(4, p.getThreshold());
		assertEquals(5, p.getActualLength());
	}
	
	@Test
	public void testMinAndMaxLength() {
		String json = "[\"abcd\"]";
		ValueType schema = array(string().minLength(3).maxLength(4));
		JsonValidator validator = new JsonValidator(schema);
		ValidationResult result = validator.validate(new StringReader(json));

		assertFalse(result.hasProblems());
	}
}
