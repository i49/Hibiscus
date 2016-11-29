package com.github.i49.hibiscus.schema;

import org.junit.Test;

import static com.github.i49.hibiscus.schema.SchemaComponents.*;

import java.math.BigDecimal;

public class NumberTypeTest {

	public static class ValuesTest {
		
		@Test(expected = SchemaException.class)
		public void valueIsNull() {
			number().enumeration(new BigDecimal("123.45"), new BigDecimal("678.90"), null);
		}
	}
}
