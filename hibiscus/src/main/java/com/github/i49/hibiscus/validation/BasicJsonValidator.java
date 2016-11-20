package com.github.i49.hibiscus.validation;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

import com.github.i49.hibiscus.schema.ComplexType;
import com.github.i49.hibiscus.schema.JsonType;

/**
 * Implementation class of {@link JsonValidator} and base class of all custom JSON validators.  
 */
public class BasicJsonValidator implements JsonValidator {

	private final JsonType rootType;
	
	private final JsonParserFactory parserFactory;
	private final JsonBuilderFactory builderFactory;
	
	/**
	 * Constructs this validator.
	 * 
	 * @param rootType the expected JSON type to be found at root of JSON document, which must be array or object.
	 * 
	 * @exception IllegalArgumentException if rootType is {@code null}.
	 * @exception IllegalStateException if one of internal objects was not configured properly.
	 */
	public BasicJsonValidator(ComplexType rootType) {
		if (rootType == null) {
			throw new IllegalArgumentException("rootType is null.");
		}
		this.rootType = rootType;
		this.parserFactory = createParserFactory();
		if (this.parserFactory == null) {
			throw new IllegalStateException("Failed to create a JsonParserFactory object.");
		}
		this.builderFactory = createBuilderFactory();
		if (this.builderFactory == null) {
			throw new IllegalStateException("Failed to create a JsonBuilderFactory object.");
		}
	}

	@Override
	public JsonType getRootType() {
		return rootType;
	}

	@Override
	public ValidationResult validate(Reader reader) {
		if (reader == null) {
			throw new IllegalArgumentException("reader is null.");
		}
		try (JsonParser parser = this.parserFactory.createParser(reader)) {
			return parse(parser);
		}
	}
	
	@Override
	public ValidationResult validate(InputStream stream, Charset charset) {
		if (stream == null) {
			throw new IllegalArgumentException("stream is null.");
		}
		if (charset == null) {
			throw new IllegalArgumentException("charset is null.");
		}
		try (JsonParser parser = this.parserFactory.createParser(stream, charset)) {
			return parse(parser);
		}
	}
	
	/**
	 * Parses JSON document with specified parser and produces result.
	 * @param parser the parser.
	 * @return validation result.
	 */
	private ValidationResult parse(JsonParser parser) {
		JsonValidatingReader reader = new JsonValidatingReader(parser, this.builderFactory);
		JsonValue value = reader.readAll(getRootType());
		return new ValidationResultImpl(value, reader.getProblems());
	}
	
	/**
	 * Creates and configures {@link javax.json.stream.JsonParserFactory} object which implements JSON Processing API.
	 * @return created {@link javax.json.stream.JsonParserFactory} object.
	 */
	protected JsonParserFactory createParserFactory() {
		Map<String, ?> config = new HashMap<>();
		return Json.createParserFactory(config);
	}
	
	/**
	 * Creates and configures {@link javax.json.JsonBuilderFactory} object which implements JSON Processing API.
	 * @return created {@link javax.json.JsonBuilderFactory} object.
	 */
	protected JsonBuilderFactory createBuilderFactory() {
		Map<String, ?> config = new HashMap<>();
		return Json.createBuilderFactory(config);
	}
}