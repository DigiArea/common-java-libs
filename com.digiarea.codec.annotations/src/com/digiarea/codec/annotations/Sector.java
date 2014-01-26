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
package com.digiarea.codec.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sector annotation. All fields and methods annotated with this annotation
 * considered as a {@link com.dagxp.amm.codec.codec.Packet} sector (field)
 * of a transportable data structure.
 * 
 * @author Norb Beaver
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.SOURCE)
public @interface Sector {

	/**
	 * The Enum Signed.
	 */
	enum Signed {

		/** The TRUE. */
		TRUE,
		/** The FALSE. */
		FALSE,
		/** The DEFAULT. */
		DEFAULT;

	}

	/**
	 * For integral types only. If TRUE an integral type value is considered
	 * signed if FALSE then unsigned and DEFAULT means signed by type but not by
	 * value.
	 * 
	 * @return true, if successful
	 */
	Signed signed() default Signed.DEFAULT;

	/**
	 * For integral types only. If false then variable-length encoding is used
	 * for integral types, otherwise fixed-length encoding used. Fixed means
	 * always four bytes for int and eight bytes for long, and is more efficient
	 * if values are often greater than 2^28 for int and 2^56 for long.
	 * 
	 * @return true, if successful
	 */
	boolean fixed() default false;
	
	/**
	 * Integer id of the sector.
	 *
	 * @return the id
	 */
	int id() default 0;
}
