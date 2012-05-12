/*
 *  Rope.java
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
package org.ahmadsoft.ropes;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 * A rope represents character strings. Ropes are immutable which 
 * means that once they are created, they cannot be changed. This
 * makes them suitable for sharing in multi-threaded environments.
 * </p><p>
 * Rope operations, unlike string operations, scale well to very
 * long character strings. Most mutation operations run in O(log n)
 * time or better. However, random-access character retrieval is 
 * generally slower than for a String. By traversing consecutive
 * characters with an iterator instead, performance improves to
 * O(1).
 * </p><p>
 * This rope implementation implements all performance optimizations
 * outlined in "<a href="http://www.cs.ubc.ca/local/reading/proceedings/spe91-95/spe/vol25/issue12/spe986.pdf">Ropes: an Alternative to Strings</a>"
 * by Hans-J. Boehm, Russ Atkinson and Michael Plass, including,
 * notably, deferred evaluation of long substrings and automatic
 * rebalancing.
 * </p>
 * <h4>Immutability (a Caveat)</h4>
 * A rope is immutable. Specifically, calling any mutator function
 * on a rope always returns a modified copy; the original rope is
 * left untouched. However, care must be taken to build ropes from
 * immutable <code>CharSequences</code> such as <code>Strings</code>,
 * or else from mutable <code>CharSequences</code> that your program 
 * <emph>guarantees will not change</emph>. Failure to do so will result in 
 * logic errors.
 * <h4>Serialization</h4>
 * Clients should manually serialize ropes as strings. Since Java's 
 * serialization mechanism does not allow delegation of object creation
 * to a factory, it is impossible to code an efficient serialization
 * routine within the Rope class. 
 * 
 * @author Amin Ahmad
 */
public interface Rope extends CharSequence, Iterable<Character>, Comparable<CharSequence> {
	
	/**
	 * A factory used for constructing ropes.
	 */
	RopeBuilder BUILDER = new RopeBuilder();

	/**
	 * Returns a new rope created by appending the specified character to
	 * this rope.
	 * @param c the specified character.
	 * @return a new rope.
	 */
	Rope append(char c);

	/**
	 * Returns a new rope created by appending the specified character sequence to
	 * this rope.
	 * @param suffix the specified suffix.
	 * @return a new rope.
	 */
	Rope append(CharSequence suffix);

	/**
	 * Returns a new rope created by appending the specified character range to
	 * this rope.
	 * @param csq the specified character.
	 * @param start the start index, inclusive.
	 * @param end the end index, non-inclusive.
	 * @return a new rope.
	 */
	Rope append(CharSequence csq, int start, int end);

	/**
     * Creats a new rope by delete the specified character substring.
     * The substring begins at the specified <code>start</code> and extends to
     * the character at index <code>end - 1</code> or to the end of the
     * sequence if no such character exists. If
     * <code>start</code> is equal to <code>end</code>, no changes are made.
     *
     * @param      start  The beginning index, inclusive.
     * @param      end    The ending index, exclusive.
     * @return     This object.
     * @throws     StringIndexOutOfBoundsException  if <code>start</code>
     *             is negative, greater than <code>length()</code>, or
     *		   greater than <code>end</code>.
     */
    Rope delete(int start, int end);

	/**
	 * Returns the index within this rope of the first occurrence of the
	 * specified character. If a character with value <code>ch</code> occurs
	 * in the character sequence represented by this <code>String</code>
	 * object, then the index of the first such occurrence is returned --
	 * that is, the smallest value k such that:
	 * <p>
	 * <code>this.charAt(k) == ch</code>
	 * <p>
	 * is <code>true</code>. If no such character occurs in this string, then
	 * <code>-1</code> is returned.
	 * @param ch a character.
	 * @return the index of the first occurrence of the character in the character
	 * sequence represented by this object, or <code>-1</code> if the character
	 * does not occur.
	 */
	int indexOf(char ch);

	/**
     * Creates a new rope by inserting the specified <code>CharSequence</code>
     * into this rope.
     * <p>
     * The characters of the <code>CharSequence</code> argument are inserted,
     * in order, into this rope at the indicated offset.
     *
     * <p>If <code>s</code> is <code>null</code>, then the four characters
     * <code>"null"</code> are inserted into this sequence.
     *
     * @param      dstOffset the offset.
     * @param      s the sequence to be inserted
     * @return     a reference to the new Rope.
     * @throws     IndexOutOfBoundsException  if the offset is invalid.
     */
    Rope insert(int dstOffset, CharSequence s);

    /**
     * Returns an iterator positioned to start at the specified index.
     * @param start the start position.
     * @return an iterator positioned to start at the specified index.
     */
    Iterator<Character> iterator(int start);
    
    /**
     * Rebalances the current rope, returning the rebalanced rope. In general,
     * rope rebalancing is handled automatically, but this method is provided
     * to give users more control.
     * 
     * @return a rebalanced rope.
     */
    public Rope rebalance();
    
    /**
     * Write this rope.
     * @param out
     */
    public void write(Writer out) throws IOException;
    
    /**
     * Write this rope.
     * @param out
     */
    public void write(Writer out, int offset, int length) throws IOException;


    @Override
	Rope subSequence(int start, int end);
    
    /**
     * Creates a matcher that will match this rope against the 
     * specified pattern. This method produces a higher performance
     * matcher than:
     * <pre>
     * Matcher m = pattern.matcher(this);
     * </pre>
     * The difference may be asymptotically better in many cases.
     * @param pattern the pattern to match this rope against.
     * @return a matcher.
     */
    Matcher matcher(Pattern pattern);
}
