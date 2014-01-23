/**
 * Copyright (c) 2009-2011 DigiArea, Inc. All rights reserved.
 * 
 * @author norb <norb.beaver@digi-area.com>
 */
package com.digiarea.common.utils;

/**
 * The Class StringUtils.
 */
public class StringUtils {

	/**
	 * Turns CamelCased string into Underscored one
	 * 
	 * @param camelCase
	 * @return
	 */
	public static String camelCaseToUnderscore(String camelCase) {
		StringBuilder builder = new StringBuilder();
		char[] chars = camelCase.toCharArray();
		for (int j = 0; j < chars.length; j++) {
			if (Character.isUpperCase(chars[j])) {
				if (j != 0) {
					builder.append('_');
				}
				builder.append(Character.toLowerCase(chars[j]));
			} else {
				builder.append(chars[j]);
			}
		}
		return builder.toString();
	}

	/**
	 * Turns Underscored string into CamelCased one
	 * 
	 * @param underscore
	 * @return
	 */
	public static String underscoreToCamelCase(String underscore) {
		StringBuilder builder = new StringBuilder();
		boolean prevIsUnderscore = false;
		char[] chars = underscore.toCharArray();
		for (char c : chars) {
			if (c == '_') {
				prevIsUnderscore = true;
			} else if (prevIsUnderscore) {
				prevIsUnderscore = false;
				builder.append(Character.toUpperCase(c));
			} else {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	/**
	 * Turns first character to upper case.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	public static String firstToUpper(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * Turns first character to lower case.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	public static String firstToLower(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

}
