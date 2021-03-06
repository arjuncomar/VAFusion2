/*
 * Encog(tm) Core v2.5 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */

package org.encog.persist.persistors.generic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.encog.EncogError;

/**
 * Used to map objects to reference numbers. This is where reference numbers are
 * resolved. This class is used by Encog generic persistence.
 */
public class ObjectMapper {

	/**
	 * A map from reference numbers to objects.
	 */
	private final Map<Integer, Object> objectMap = 
		new HashMap<Integer, Object>();

	/**
	 * A list of all of the field mappings.
	 */
	private final List<FieldMapping> list = new ArrayList<FieldMapping>();

	/**
	 * Add a field mapping to be resolved later. This builds a list of
	 * references to be resolved later when the resolve method is called.
	 * 
	 * @param ref
	 *            The reference number.
	 * @param field
	 *            The field to map.
	 * @param target
	 *            The target object that holds the field.
	 */
	public void addFieldMapping(final int ref, final Field field,
			final Object target) {
		this.list.add(new FieldMapping(ref, field, target));
	}

	/**
	 * Add an object mapping to be resolved later.
	 * 
	 * @param ref
	 *            The object reference.
	 * @param obj
	 *            The object.
	 */
	public void addObjectMapping(final int ref, final Object obj) {
		this.objectMap.put(ref, obj);
	}

	/**
	 * Clear the map and reference list.
	 */
	public void clear() {
		this.objectMap.clear();
		this.list.clear();
	}

	/**
	 * Resolve all references and place the correct objects.
	 */
	public void resolve() {
		try {
			for (final FieldMapping field : this.list) {
				final Object obj = this.objectMap.get(field.getRef());
				field.getField().setAccessible(true);
				field.getField().set(field.getTarget(), obj);
			}
		} catch (final IllegalArgumentException e) {
			throw new EncogError(e);
		} catch (final IllegalAccessException e) {
			throw new EncogError(e);
		}
	}
}
