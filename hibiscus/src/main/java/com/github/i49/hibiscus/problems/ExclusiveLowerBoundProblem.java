package com.github.i49.hibiscus.problems;

import java.math.BigDecimal;
import java.util.Locale;

import javax.json.JsonNumber;

import com.github.i49.hibiscus.common.Bound;

/**
 * Problem that numeric value is less than or equal to exclusive lower bound of allowed range.
 */
public class ExclusiveLowerBoundProblem extends NumericRangeProblem {

	/**
	 * Constructs this problem.
	 * @param value the actual value in JSON document.
	 * @param bound the lower bound of the range allowed for the number type.
	 */
	public ExclusiveLowerBoundProblem(JsonNumber value, Bound<BigDecimal> bound) {
		super(value, bound);
	}

	@Override
	public String buildDescription(Locale locale) {
		return Messages.EXCLUSIVE_LOWER_BOUND_PROBLEM(locale, getActualValue(), getBound().getValue());
	}
}