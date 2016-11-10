package com.github.i49.hibiscus.schema.problems;

import java.util.Locale;

/**
 * Problem that string is too short or too long.
 */
public class StringLengthProblem extends Problem {

	private final int threshold;
	private final int instanceLength;
	
	/**
	 * Constructs this problem.
	 * @param threshold minimum or maximum number of characters allowed in string. 
	 * @param instanceLength actual number of characters in string.
	 */
	public StringLengthProblem(int threshold, int instanceLength) {
		this.threshold = threshold;
		this.instanceLength = instanceLength;
	}
	
	/**
	 * Returns minimum or maximum number of characters allowed in string.
	 * @return minimum or maximum threshold.
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * Returns actual number of characters in string.
	 * @return actual number of characters.
	 */
	public int getInstanceLength() {
		return instanceLength;
	}

	@Override
	public String getMessage(Locale locale) {
		StringBuilder b = new StringBuilder();
		if (getInstanceLength() < getThreshold()) {
			b.append("String is too short. It must have at least ");
		} else {
			b.append("String is too long. It must have at most ");
		};
		b.append(getThreshold()).append(" characters ");
		b.append("but instance has ").append(getInstanceLength()).append(".");
		return b.toString();
	}
}
