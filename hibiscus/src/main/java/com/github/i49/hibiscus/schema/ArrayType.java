package com.github.i49.hibiscus.schema;

import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonValue;

import com.github.i49.hibiscus.common.IntRange;
import com.github.i49.hibiscus.common.TypeId;
import com.github.i49.hibiscus.problems.ArrayTooLongProblem;
import com.github.i49.hibiscus.problems.ArrayTooShortProblem;
import com.github.i49.hibiscus.problems.Problem;

/**
 * JSON array which can have zero or more values as elements.
 */
public class ArrayType extends ComplexType {

	private TypeSet typeSet;
	private int minItems = -1;
	private int maxItems = -1;
	
	/**
	 * Constructs this array type.
	 */
	public ArrayType() {
		typeSet = TypeSet.empty();
	}
	
	@Override
	public TypeId getTypeId() {
		return TypeId.ARRAY;
	}
	
	@Override
	public void validateInstance(JsonValue value, List<Problem> problems) {
		JsonArray array = (JsonArray)value;
		int size = array.size();
		if (minItems != -1 && size < minItems) {
			problems.add(new ArrayTooShortProblem(size, IntRange.of(minItems, maxItems)));
		}
		if (maxItems != -1 && size > maxItems) {
			problems.add(new ArrayTooLongProblem(size, IntRange.of(minItems, maxItems)));
		}
	}
	
	/**
	 * Specifies allowed types for elements of this array. 
	 * @param types the types allowed.
	 * @return this array.
	 */
	public ArrayType items(JsonType[] types) {
		this.typeSet = TypeSet.of(types);
		return this;
	}

	/**
	 * Returns types allowed for elements of this array.
	 * @return set of types.
	 */
	public TypeSet getItemTypes() {
		return typeSet;
	}

	/**
	 * Specifies minimum number of elements in this array. 
	 * @param size minimum number of elements.
	 * @return this array.
	 */
	public ArrayType minItems(int size) {
		this.minItems = size;
		return this;
	}

	/**
	 * Specifies maximum number of elements in this array. 
	 * @param size maximum number of elements.
	 * @return this array.
	 */
	public ArrayType maxItems(int size) {
		this.maxItems = size;
		return this;
	}
}
