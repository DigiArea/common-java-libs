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

import com.digiarea.common.annotator.Child;
import com.digiarea.common.annotator.Children;

/**
 * Packet annotation. All classes annotated with this annotation considered as a
 * transportable data structure.
 * 
 * @author Norb Beaver
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Children({ @Child(clazz = Packet.class, isUnique = false),
		@Child(clazz = Sector.class, isUnique = false),
		@Child(clazz = Factory.class, isUnique = true) })
public @interface Packet {

	/**
	 * The Enum Type.
	 */
	enum Owner {

		/** The SERVER. */
		SERVER,
		/** The CLIENT. */
		CLIENT,
		/** The ANYONE. */
		ANYONE
	}

	/**
	 * The Enum Scope.
	 */
	enum Scope {

		/** The EXTERNAL. */
		EXTERNAL,
		/** The INTERNAL. */
		INTERNAL
	}

	/**
	 * Packet direction. ANYONE means both SERVER and CLIENT.
	 * 
	 * @return the type
	 */
	Owner owner() default Owner.ANYONE;

	/**
	 * Scope of the packet. INTERNAL means for internal use only.
	 * 
	 * @return the scope
	 */
	Scope scope() default Scope.EXTERNAL;

	/**
	 * Integer id of the packet.
	 * 
	 * @return the id
	 */
	int id() default 0;

}
