package com.github.i49.schema;

/**
 * Type identifiers of primitive types in JSON schema.
 */
public enum TypeId {
	/** A JSON array. */
	ARRAY,
	/** A JSON boolean. */
	BOOLEAN,
	/** A JSON number without a fraction or exponent part. */
	INTEGER,
	/** Any JSON number including integer. */
	NUMBER,
	/** The JSON null value. */
	NULL,
	/** A JSON object. */
	OBJECT,
	/** A JSON string. */
	STRING
}