package com.github.i49.hibiscus.schema.internal;

import java.util.HashSet;
import java.util.Set;

import javax.json.JsonString;

import com.github.i49.hibiscus.facets.EnumerationFacet;
import com.github.i49.hibiscus.facets.FormatFacet;
import com.github.i49.hibiscus.facets.LengthFacet;
import com.github.i49.hibiscus.facets.MaxLengthFacet;
import com.github.i49.hibiscus.facets.MinLengthFacet;
import com.github.i49.hibiscus.facets.PatternFacet;
import com.github.i49.hibiscus.formats.Format;
import com.github.i49.hibiscus.formats.StringFormat;
import com.github.i49.hibiscus.problems.StringLengthProblem;
import com.github.i49.hibiscus.problems.StringTooLongProblem;
import com.github.i49.hibiscus.problems.StringTooShortProblem;
import com.github.i49.hibiscus.schema.SchemaException;
import com.github.i49.hibiscus.schema.StringType;

/**
 * The implementation class of {@link StringType}.
 */
public class StringTypeImpl extends AbstractJsonType<JsonString, StringType> implements StringType {

	/**
	 * Constructs this type.
	 */
	public StringTypeImpl() {
	}

	@Override
	public StringType length(int length) {
		verifyLength(length);
		return facet(new LengthFacet<JsonString>(length, StringTypeImpl::getLength, StringLengthProblem::new));
	}
	
	@Override
	public StringType minLength(int length) {
		verifyLength(length);
		return facet(new MinLengthFacet<JsonString>(length, StringTypeImpl::getLength, StringTooShortProblem::new));
	}
	
	@Override
	public StringType maxLength(int length) {
		verifyLength(length);
		return facet(new MaxLengthFacet<JsonString>(length, StringTypeImpl::getLength, StringTooLongProblem::new));
	}
	
	@Override
	public StringType enumeration(String... values) {
		Set<Object> enumerators = new HashSet<>();
		int i = 0;
		for (String value: values) {
			if (value == null) {
				throw new SchemaException(Messages.ONE_OF_VALUES_IS_NULL(i));
			}
			enumerators.add(value);
			i++;
		}
		return facet(EnumerationFacet.of(enumerators, JsonString::getString));
	}
	
	@Override
	public StringType pattern(String expression) {
		if (expression == null) {
			throw new SchemaException(Messages.METHOD_PARAMETER_IS_NULL("pattern", "expression"));
		}
		return facet(new PatternFacet(expression));
	}
	
	@Override
	public StringType format(StringFormat format, StringFormat... moreFormats) {
		Set<Format<JsonString>> set = new HashSet<>();
		if (format == null) {
			throw new SchemaException(Messages.ONE_OF_FORMAT_IS_NULL(0));
		}
		set.add(format);
		int index = 1;
		for (Format<JsonString> other: moreFormats) {
			if (other == null) {
				throw new SchemaException(Messages.ONE_OF_FORMAT_IS_NULL(index));
			}
			set.add(other);
			index++;
		}
		return facet(new FormatFacet<JsonString>(set));
	}

	/**
	 * Returns the number of characters in the string.
	 * @param value the string value.
	 * @return the length of the string.
	 */
	private static int getLength(JsonString value) {
		return value.getString().length();
	}

	/**
	 * Verifies the length specified for strings.
	 * @param length the length specified for strings.
	 */
	private static void verifyLength(int length) {
		if (length < 0) {
			throw new SchemaException(Messages.STRING_LENGTH_IS_NEGATIVE(length));
		}
	}
}
