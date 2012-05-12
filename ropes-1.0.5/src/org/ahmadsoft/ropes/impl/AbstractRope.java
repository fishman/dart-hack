/*
 *  AbstractRope.java
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
import java.io.StringWriter;
import java.util.Iterator;

import org.ahmadsoft.ropes.Rope;

/**
 * Abstract base class for ropes that implements many of the common operations. 
 * @author Amin Ahmad
 */
public abstract class AbstractRope implements Rope {
	
	protected int hashCode = 0;

	@Override
	public Rope append(final char c) {
		return RopeUtilities.INSTANCE.concatenate(this, Rope.BUILDER.build(String.valueOf(c)));
	}

	@Override
	public Rope append(final CharSequence suffix) {
		return RopeUtilities.INSTANCE.concatenate(this, Rope.BUILDER.build(suffix));
	}

	@Override
	public Rope append(final CharSequence csq, final int start, final int end) {
		return RopeUtilities.INSTANCE.concatenate(this, Rope.BUILDER.build(csq).subSequence(start, end));
	}

	@Override
	public Rope delete(final int start, final int end) {
		if (start == end)
			return this;
		return this.subSequence(0, start).append(this.subSequence(end, this.length()));
	}

	/*
	 * The depth of the current rope, as defined in "Ropes: an Alternative
	 * to Strings".
	 */
	public abstract byte depth();

	@Override
	public int hashCode() {
		if (this.hashCode == 0 && length() > 0) {
			if (this.length() < 6) {
				for (final char c: this)
					this.hashCode = 31 * this.hashCode + c;
			} else {
				final Iterator<Character> i = this.iterator();
				for (int j=0;j<5; ++j)
					this.hashCode = 31 * this.hashCode + i.next();
				this.hashCode = 31 * this.hashCode + this.charAt(this.length() - 1);
			}
		}
		return this.hashCode;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (other instanceof Rope) {
			final Rope rope = (Rope) other;
			if (rope.hashCode() != this.hashCode() || rope.length() != this.length())
				return false;
			final Iterator<Character> i1 = this.iterator();
			final Iterator<Character> i2 = rope.iterator();

			while (i1.hasNext()) {
				final char a = i1.next();
				final char b = i2.next();
				if (a != b)
					return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public int indexOf(final char ch) {
		int index = -1;
		for (final char c: this) {
			++index;
			if (c == ch)
				return index;
		}
		return -1;
	}

	@Override
	public Rope insert(final int dstOffset, final CharSequence s) {
		final Rope r = (s == null) ? Rope.BUILDER.build("null"): Rope.BUILDER.build(s);
		if (dstOffset == 0)
			return r.append(this);
		else if (dstOffset == this.length())
			return this.append(r);
		else if (dstOffset < 0 || dstOffset > this.length())
			throw new IndexOutOfBoundsException(dstOffset + " is out of insert range [" + 0 + ":" + this.length() + "]");
		return this.subSequence(0, dstOffset).append(r).append(this.subSequence(dstOffset, this.length()));
	}

	@Override
	public Iterator<Character> iterator() {
		return this.iterator(0);
	}
	
	@Override
	public int compareTo(CharSequence sequence) {
		int compareTill = Math.min(sequence.length(), length());
		Iterator<Character> i = iterator();
		for (int j=0; j<compareTill; ++j) {
			char x = i.next();
			char y = sequence.charAt(j);
			if (x != y)
				return x - y;
		}
		return length() - sequence.length();
	}
	
	public String toString() {
		StringWriter out = new StringWriter(length());
		try {
			write(out);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return out.toString();
	}
}
