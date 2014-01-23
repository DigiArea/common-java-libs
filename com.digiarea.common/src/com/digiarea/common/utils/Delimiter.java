/**
 * Copyright (c) 2009-2011 DigiArea, Inc. All rights reserved.
 * 
 * @author norb <norb.beaver@digi-area.com>
 */
package com.digiarea.common.utils;

/**
 * The Enum InnerDelimiter.
 */
public enum Delimiter {

	/** The DOLLAR. */
	DOLLAR("$"),
	/** The UNDERSCORE. */
	UNDERSCORE("_"),
	/** The DOT. */
	DOT("."),
	/** The NONE. */
	NONE(" "),
	/** The EMPTY. */
	EMPTY("empty");

	/** The delimiter. */
	private String delimiter;

	/**
	 * Instantiates a new inner delimiter.
	 * 
	 * @param delimiter
	 *            the delimiter
	 */
	private Delimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return delimiter;
	}

}