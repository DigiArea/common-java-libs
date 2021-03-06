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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.digiarea.common.annotator.Child;
import com.digiarea.common.annotator.Children;
import com.digiarea.common.annotator.Root;

/**
 * The Interface Model.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
@Children({ @Child(clazz = Bean.class, isUnique = false) })
@Root
public @interface Model {

}
