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

import com.digiarea.common.annotator.Child;
import com.digiarea.common.annotator.Children;

/**
 * Bean annotation for model equipment.
 * 
 * @author Norb Beaver
 * @since 1.3.0
 * 
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
@Children({ @Child(clazz = Field.class, isUnique = false) })
public @interface Bean {

	/**
	 * Standard visibilities.
	 * 
	 * <ul>
	 * <li>first - from the bean</li>
	 * <li>second - from the root</li>
	 * <li>third - the defaults: PUBLIC</li>
	 * </ul>
	 * 
	 * @author Norb Beaver
	 * 
	 */
	public enum Visibility {

		/**
		 * The public.
		 */
		PUBLIC,
		/**
		 * The protected.
		 */
		PROTECTED,
		/**
		 * The private.
		 */
		PRIVATE,
		/**
		 * The package.
		 */
		PACKAGE,
		/**
		 * The none.
		 */
		NONE
	}

	/**
	 * If true the default constructor will be generated, otherwise will not.
	 * 
	 * @return true, if successful
	 */
	boolean defaultConstructor() default true;

	/**
	 * If true the maximum constructor will be generated, otherwise will not.
	 * 
	 * @return true, if successful
	 */
	boolean maximumConstructor() default true;

	/**
	 * If true the bean is the root one.
	 * 
	 * @return true, if is root
	 */
	boolean isRoot() default false;

	/**
	 * Visibility for the default constructor.
	 * 
	 * @return the visibility
	 */
	Visibility defaultVisibility() default Visibility.NONE;

	/**
	 * Visibility for the maximum constructor.
	 * 
	 * @return the visibility
	 */
	Visibility maximumVisibility() default Visibility.NONE;

}
