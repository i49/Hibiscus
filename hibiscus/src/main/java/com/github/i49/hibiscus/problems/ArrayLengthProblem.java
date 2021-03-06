package com.github.i49.hibiscus.problems;

import java.util.Locale;

import javax.json.JsonArray;

/**
 * Problem that the number of elements in an array differs from the specified explicitly in the schema.
 *
 * <p>This problem can be caused by {@code array()} type only.</p>
 */
public class ArrayLengthProblem extends TypedProblem<JsonArray> {

	private final int actualLength;
	private final int expectedLength;
	
	/**
	 * Constructs this problem.
	 * @param actualLength the actual number of elements in the array in JSON document.
	 * @param expectedLength the number of elements declared in the schema. 
	 */
	public ArrayLengthProblem(int actualLength, int expectedLength) {
		this.actualLength = actualLength;
		this.expectedLength = expectedLength;
	}

	/**
	 * Returns the actual number of elements in the array.
	 * @return the actual number of elements.
	 */
	public int getActualLength() {
		return actualLength;
	}
	
	/**
	 * Returns the number of elements declared in the schema. 
	 * @return the number of elements declared in the schema.
	 */
	public int getExpectedLength() {
		return expectedLength;
	}

	@Override
	protected String buildDescription(Locale locale) {
		return Messages.ARRAY_LENGTH_PROBLEM(locale, getActualLength(), getExpectedLength());
	}
}
