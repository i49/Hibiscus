package com.github.i49.hibiscus.validation;

import java.util.function.Predicate;

import javax.json.JsonObject;

import com.github.i49.hibiscus.facets.Facet;
import com.github.i49.hibiscus.problems.ProblemDescriber;
import com.github.i49.hibiscus.schema.ObjectType;
import com.github.i49.hibiscus.schema.Property;

/**
 * A internal helper class which represents an unknown object found during validation.
 */
class UnknownObjectType implements ObjectType {
	
	public static final ObjectType INSTANCE = new UnknownObjectType();

	private UnknownObjectType() {
	}

	@Override
	public ObjectType properties(Property... properties) {
		return this;
	}

	@Override
	public ObjectType moreProperties() {
		return this;
	}
	
	@Override
	public ObjectType facet(Facet<JsonObject> facet) {
		return this;
	}

	@Override
	public ObjectType assertion(Predicate<JsonObject> predicate, ProblemDescriber<JsonObject> description) {
		return this;
	}

	@Override
	public Property getProperty(String name) {
		return null;
	}

	@Override
	public boolean allowsMoreProperties() {
		return true;
	}
}
