/*
 * 
 */
package com.digiarea.common;

public interface Arrow<I, O> {

	public O arrow(I input) throws Exception;

}
