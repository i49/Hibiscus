package com.github.i49.hibiscus.validation;

import java.util.List;

import javax.json.JsonValue;

import com.github.i49.hibiscus.problems.Problem;

/**
 * Implementation class of {@code ValidationResult}.
 */
class ValidationResultImpl implements ValidationResult {

	private final JsonValue value;
	private final List<Problem> problems;

	/**
	 * Constructs this result.
	 * @param value the JSON value found at root of JSON document.
	 * @param problems the problems detected by validation.
	 */
	public ValidationResultImpl(JsonValue value, List<Problem> problems) {
		this.value = value;
		this.problems = problems;
	}

	@Override
	public JsonValue getValue() {
		return value;
	}
	
	@Override
	public boolean hasProblems() {
		return !getProblems().isEmpty();
	}

	@Override
	public List<Problem> getProblems() {
		return problems;
	}
}
