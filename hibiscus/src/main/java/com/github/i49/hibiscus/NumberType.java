package com.github.i49.hibiscus;

public class NumberType extends ValueType {

	@Override
	public Type getType() {
		return Type.NUMBER;
	}

	@Override
	public boolean isTypeOf(Type type) {
		return (type == Type.NUMBER || type == Type.INTEGER);
	}
}