package com.github.i49.hibiscus.examples;

import static com.github.i49.hibiscus.schema.JsonTypes.*;

import com.github.i49.hibiscus.schema.ObjectType;
import com.github.i49.hibiscus.validation.BasicJsonValidator;
import com.github.i49.hibiscus.validation.JsonValidator;

public class PedigreeValidator extends BasicJsonValidator {

	// Creates blank object first.
	private static final ObjectType horse = object(); 
	
	static {
		// Added properties to the object above, referencing the object itself.
		horse.properties(
			required("name", string()),
			required("birthYear", integer()),
			optional("sire", horse),
			optional("dam", horse)
			);
	}

	public PedigreeValidator() {
		super(horse);
	}

	public static void main(String[] args) {
		JsonValidator validator = new PedigreeValidator();
		JsonLoader.load("pedigree.json", validator);
	}
}
