package com.github.i49.hibiscus.schema;

import java.util.regex.PatternSyntaxException;

import org.junit.Test;

import static com.github.i49.hibiscus.schema.SchemaComponents.*;

public class StringTypeTest {
	
	public static class MinLengthTest {

		@Test
		public void positiveLength() {
			string().minLength(1);
		}

		@Test
		public void zeroLength() {
			string().minLength(0);
		}

		@Test(expected = SchemaException.class)
		public void negativeLength() {
			string().minLength(-1);
		}
	}

	public static class MaxLengthTest {

		@Test
		public void positiveLength() {
			string().maxLength(1);
		}

		@Test
		public void zeroLength() {
			string().maxLength(0);
		}

		@Test(expected = SchemaException.class)
		public void negativeLength() {
			string().maxLength(-1);
		}
	}
	
	public static class EnumerationTest {
	
		@Test(expected = SchemaException.class)
		public void enumeratorIsNull() {
			string().enumeration("January", null, "March");
		}
	}

	public static class PatternTest {
		
		@Test(expected = SchemaException.class)
		public void patternIsNull() {
			string().pattern(null);
		}
		
		@Test(expected = PatternSyntaxException.class)
		public void invalidSyntax() {
			string().pattern("\\");
		}
	}
}
