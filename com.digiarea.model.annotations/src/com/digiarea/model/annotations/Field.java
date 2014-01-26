/*******************************************************************************
 * Copyright (c) 2011 - 2014 DigiArea, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     DigiArea, Inc. - initial API and implementation
 *******************************************************************************/
package com.digiarea.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field annotation for model equipment.
 * 
 * @author Norb Beaver <norb.beaver@digi-area.com>
 * 
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.SOURCE)
public @interface Field {

	/**
	 * The Enum Kind.
	 */
	public enum Kind {

		/**
		 * The ordinal.
		 */
		ORDINAL,
		/**
		 * The parent.
		 */
		PARENT,
		/**
		 * The cyclic.
		 */
		CYCLIC,
		/**
		 * The free.
		 */
		FREE
	}

	/**
	 * With getter.
	 * 
	 * @return true, if successful
	 */
	boolean withGetter() default true;

	/**
	 * With setter.
	 * 
	 * @return true, if successful
	 */
	boolean withSetter() default true;

	/**
	 * With add remove.
	 * 
	 * @return true, if successful
	 */
	boolean withAddRemove() default true;

	/**
	 * Kind.
	 * 
	 * @return the kind
	 */
	Kind kind() default Kind.ORDINAL;

	/**
	 * Flags.
	 * 
	 * @return the string[]
	 */
	String[] flags() default {};

}
