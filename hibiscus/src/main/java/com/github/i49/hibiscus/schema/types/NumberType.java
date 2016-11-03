package com.github.i49.hibiscus.schema.types;

import com.github.i49.hibiscus.schema.TypeId;

/**
 * JSON number including integer.
 */
public class NumberType extends SimpleType {

	private static final NumberType DEFAULT = new NumberType();
	
	/**
	 * Returns this type with default settings.
	 * @return immutable type with default settings.
	 */
	public static NumberType getDefault() {
		return DEFAULT;
	}
	
	@Override
	public TypeId getTypeId() {
		return TypeId.NUMBER;
	}
}
