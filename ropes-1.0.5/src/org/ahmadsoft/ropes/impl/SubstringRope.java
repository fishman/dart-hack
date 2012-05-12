/*
 *  SubstringRope.java
 *  Copyright (C) 2007 Amin Ahmad. 
 *  
 *  This file is part of Java Ropes.
 *  
 *  Java Ropes is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Java Ropes is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Java Ropes.  If not, see <http://www.gnu.org/licenses/>.
 *  	
 *  Amin Ahmad can be contacted at amin.ahmad@gmail.com or on the web at 
 *  www.ahmadsoft.org.
 */
package org.ahmadsoft.ropes.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ahmadsoft.ropes.Rope;

/**
 * Represents a lazily-evaluated substring of another rope. For performance
 * reasons, the target rope must be a <code>FlatRope</code>.
 * @author aahmad
 */
public class SubstringRope extends AbstractRope {

	private final FlatRope rope;
	private final int offset;
	private final int length;
	private final byte depth;

	public SubstringRope(final FlatRope rope, final int offset, final int length) {
		if (length < 0 || offset < 0 || offset + length > rope.length())
			throw new IndexOutOfBoundsException("Invalid substring offset (" + offset + ") and length (" + length + ") for underlying rope with length " + rope.length());
		
		this.rope = rope;
		this.offset = offset;
		this.length = length;
		this.depth = (byte) (RopeUtilities.INSTANCE.depth(rope) + 1);
	}

	@Override
	public char charAt(final int index) {
		return this.rope.charAt(this.offset + index);
	}

	@Override
	public byte depth() {
		return this.depth;
	}

	int getOffset() {
		return this.offset;
	}

	/**
	 * Returns the rope underlying this one.
	 * @return the rope underlying this one.
	 */
	public Rope getRope() {
		return this.rope;
	}

	@Override
	public Iterator<Character> iterator(final int start) {
		if (start < 0 || start >= length())
			throw new IndexOutOfBoundsException("Rope index out of range: " + start);
		return this.rope.iterator(this.offset + start);
	}

	@Override
	public int length() {
		return this.length;
	}

	@Override
	public Rope subSequence(final int start, final int end) {
		return new SubstringRope(this.rope, this.offset + start, end-start);
	}

	@Override
	public String toString() {
		return rope.toString(offset, length);
	}

	@Override
	public Rope rebalance() {
		return this;
	}

	@Override
	public void write(Writer out) throws IOException {
		rope.write(out, offset, length);
	}

	@Override
	public void write(Writer out, int offset, int length) throws IOException {
		rope.write(out, this.offset + offset, this.length + length);
	}

	@Override
	public Matcher matcher(Pattern pattern) {
		return pattern.matcher(this);
	}
}
