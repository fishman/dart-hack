/*
 *  RopeTest.java
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
package org.ahmadsoft.ropes.test;

import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.ahmadsoft.ropes.Rope;
import org.ahmadsoft.ropes.impl.ConcatenationRope;
import org.ahmadsoft.ropes.impl.FlatRope;
import org.ahmadsoft.ropes.impl.SubstringRope;

public class RopeTest extends TestCase {
	
	public void testConcatenationFlatFlat() {
		Rope r1 = Rope.BUILDER.build("alpha");
		final Rope r2 = Rope.BUILDER.build("beta");
		Rope r3 = r1.append(r2);
		Assert.assertEquals("alphabeta", r3.toString());

		r1 = Rope.BUILDER.build("The quick brown fox jumped over");
		r3 = r1.append(r1);
		Assert.assertEquals("The quick brown fox jumped overThe quick brown fox jumped over", r3.toString());
	}
	
	public void testIterator() {
		Rope x1 = new FlatRope("0123456789");
		Rope x2 = new FlatRope("0123456789");
		Rope x3 = new FlatRope("0123456789");
		ConcatenationRope c1 = new ConcatenationRope(x1, x2);
		ConcatenationRope c2 = new ConcatenationRope(c1, x3);
		
		Iterator<Character> i = c2.iterator();
		for (int j = 0; j < c2.length(); ++j) {
			assertTrue("Has next (" + j + "/" + c2.length() + ")", i.hasNext());
			i.next();
		}
		assertTrue(!i.hasNext());
	}

	public void testCreation() {
		try {
			Rope.BUILDER.build("The quick brown fox jumped over");
		} catch (final Exception e) {
			Assert.fail("Nonempty string: " + e.getMessage());
		}
		try {
			Rope.BUILDER.build("");
		} catch (final Exception e) {
			Assert.fail("Empty string: " + e.getMessage());
		}
	}

	public void testEquals() {
		final Rope r1 = Rope.BUILDER.build("alpha");
		final Rope r2 = Rope.BUILDER.build("beta");
		final Rope r3 = Rope.BUILDER.build("alpha");

		Assert.assertEquals(r1, r3);
		Assert.assertFalse(r1.equals(r2));
	}

	public void testHashCode() {
		final Rope r1 = Rope.BUILDER.build("alpha");
		final Rope r2 = Rope.BUILDER.build("beta");
		final Rope r3 = Rope.BUILDER.build("alpha");

		Assert.assertEquals(r1.hashCode(), r3.hashCode());
		Assert.assertFalse(r1.hashCode() == r2.hashCode());
	}
	
	public void testHashCode2() {
		Rope r1 = new FlatRope(new StringBuffer("The quick brown fox."));
		Rope r2 = new ConcatenationRope(new FlatRope(""), new FlatRope("The quick brown fox."));

		assertTrue(r1.equals(r2));
		assertTrue(r1.equals(r2));
	}

	public void testIndexOf() {
		final Rope r1 = Rope.BUILDER.build("alpha");
		final Rope r2 = Rope.BUILDER.build("beta");
		final Rope r3 = r1.append(r2);
		Assert.assertEquals(1, r3.indexOf('l'));
		Assert.assertEquals(6, r3.indexOf('e'));
		

		Rope r = Rope.BUILDER.build("abcdef");
		assertEquals(-1, r.indexOf('z'));
		assertEquals(0, r.indexOf('a'));
		assertEquals(1, r.indexOf('b'));
		assertEquals(5, r.indexOf('f'));
	}

	public void testInsert() {
		final Rope r1 = Rope.BUILDER.build("alpha");
		Assert.assertEquals("betaalpha", r1.insert(0, "beta").toString());
		Assert.assertEquals("alphabeta", r1.insert(r1.length(), "beta").toString());
		Assert.assertEquals("abetalpha", r1.insert(1, "beta").toString());
	}

	public void testPrepend() {
		Rope r1 = Rope.BUILDER.build("alphabeta");
		for (int j=0;j<2;++j)
			r1 = r1.subSequence(0, 5).append(r1);
		Assert.assertEquals("alphaalphaalphabeta", r1.toString());
		r1 = r1.append(r1.subSequence(5, 15));
		Assert.assertEquals("alphaalphaalphabetaalphaalpha", r1.toString());
	}
	
	public void testCompareTo() {
		final Rope r1 = Rope.BUILDER.build("alpha");
		final Rope r2 = Rope.BUILDER.build("beta");
		final Rope r3 = Rope.BUILDER.build("alpha");
		final Rope r4 = Rope.BUILDER.build("alpha1");
		final String s2 = "beta"; 

		assertTrue(r1.compareTo(r3) == 0);
		assertTrue(r1.compareTo(r2) < 0);
		assertTrue(r2.compareTo(r1) > 0);
		assertTrue(r1.compareTo(r4) < 0);
		assertTrue(r4.compareTo(r1) > 0);
		assertTrue(r1.compareTo(s2) < 0);
		assertTrue(r2.compareTo(s2) == 0);
	}
	
	public void testToString() {
		String phrase = "The quick brown fox jumped over the lazy brown dog. Boy am I glad the dog was asleep.";
		final Rope r1 = Rope.BUILDER.build(phrase);
		assertTrue(phrase.equals(r1.toString()));
		assertTrue(phrase.subSequence(7, 27).equals(r1.subSequence(7, 27).toString()));
	}

	public void testAppend() {
		Rope r = Rope.BUILDER.build("");
		r=r.append('a');
		assertEquals("a", r.toString());
		r=r.append("boy");
		assertEquals("aboy", r.toString());
		r=r.append("test", 0, 4);
		assertEquals("aboytest", r.toString());
	}
	
	public void testCharAt() {
		FlatRope r1 = new FlatRope("0123456789");
		SubstringRope r2 = new SubstringRope(r1,0,1);
		SubstringRope r3 = new SubstringRope(r1,9,1);
		ConcatenationRope r4 = new ConcatenationRope(r1, r3);

		assertEquals('0', r1.charAt(0));
		assertEquals('9', r1.charAt(9));
		assertEquals('0', r2.charAt(0));
		assertEquals('9', r3.charAt(0));
		assertEquals('0', r4.charAt(0));
		assertEquals('9', r4.charAt(9));
		assertEquals('9', r4.charAt(10));
	}
	
	public void testRegexp() {
		ConcatenationRope r = new ConcatenationRope(new FlatRope("012345"), new FlatRope("6789"));
		CharSequence c = r.getRegexpCharSeq(r);
		for (int j=0; j<10; ++j) {
			assertEquals(r.charAt(j), c.charAt(j));
		}
		c = r.getRegexpCharSeq(r);
		
		int[] indices={1,2,1,3,5,0,6,7,8,1,7,7,7};
		for (int i: indices) {
			assertEquals("Index: " + i, r.charAt(i), c.charAt(i));
		}
	}
}
