package com.github.i49.hibiscus.problems;

import javax.json.stream.JsonLocation;

public class UnknownPropertyProblem extends Problem {

	private final String propertyName;
	
	public UnknownPropertyProblem(String propertyName, JsonLocation location) {
		super(location);
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}
	
	@Override
	public String getMessage() {
		return "Property \"" + getPropertyName() + "\" is not allowed.";
	}

}
