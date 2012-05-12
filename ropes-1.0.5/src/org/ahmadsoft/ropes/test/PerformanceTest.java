/*
 *  PerformanceTest.java
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ahmadsoft.ropes.Rope;
import org.ahmadsoft.ropes.impl.AbstractRope;

/**
 * Performs an extensive performance test comparing Ropes, Strings, and
 * StringBuffers.
 * @author aahmad
 */
public class PerformanceTest {

	private static int seed=342342;
	private static Random random = new Random(PerformanceTest.seed);
	private static int lenCC = 182029;
	private static int lenBF = 467196;
	
	private static final int ITERATION_COUNT = 7;
	

	private static String complexString=null; 
	private static StringBuffer complexStringBuffer=null; 
	private static Rope complexRope=null;

	/**
	 * @param args
	 */
	public static void main(final String[] args) throws Exception {
		
		if (args.length == 1) {
			seed = Integer.parseInt(args[0]);
		}
		
		long x,y;

		x=System.nanoTime();
		final String aChristmasCarol = PerformanceTest.readCC();
		final String bensAuto        = PerformanceTest.readBF();
		y=System.nanoTime();
		System.out.println("Read " + aChristmasCarol.length() + " bytes in " + PerformanceTest.time(x,y));

//		System.out.println();
//		System.out.println("**** DELETE PLAN TEST ****");
//		System.out.println();
//
//		int newSize = PerformanceTest.lenCC;
//		final int[][] deletePlan=new int[230][2];
//		for (int j=0;j<deletePlan.length;++j) {
//			deletePlan[j][0] = PerformanceTest.random.nextInt(newSize);
//			deletePlan[j][1] = PerformanceTest.random.nextInt(Math.min(100, newSize - deletePlan[j][0]));
//			newSize -= deletePlan[j][1];
//		}
//
//		{
//		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT];
//		for (int j=0;j<ITERATION_COUNT;++j){
//		stats0[j] = PerformanceTest.stringDeleteTest(aChristmasCarol, deletePlan);
//		stats1[j] = PerformanceTest.stringBufferDeleteTest(aChristmasCarol, deletePlan);
//		stats2[j] = PerformanceTest.ropeDeleteTest(aChristmasCarol, deletePlan);
//		}
//		stat(System.out, stats0, "ns", "[String]");
//		stat(System.out, stats1, "ns", "[StringBuffer]");
//		stat(System.out, stats2, "ns", "[Rope]");
//		}
//
//		System.out.println();
//		System.out.println("**** PREPEND PLAN TEST ****");
//		System.out.println();
//
//		final int[][] prependPlan=new int[500][2];
//		for (int j=0;j<prependPlan.length;++j) {
//			prependPlan[j][0] = PerformanceTest.random.nextInt(PerformanceTest.lenCC);
//			prependPlan[j][1] = PerformanceTest.random.nextInt(PerformanceTest.lenCC - prependPlan[j][0]);
//		}
//
//		for (int k=20; k<prependPlan.length; k+=20) {
//		System.out.println("Prepend plan length: " + k);
//		{
//		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT];
//		for (int j=0;j<ITERATION_COUNT;++j){
//		stats0[j] = PerformanceTest.stringPrependTest(aChristmasCarol, prependPlan, k);
//		stats1[j] = PerformanceTest.stringBufferPrependTest(aChristmasCarol, prependPlan, k);
//		stats2[j] = PerformanceTest.ropePrependTest(aChristmasCarol, prependPlan, k);
//		}
//		stat(System.out, stats0, "ns", "[String]");
//		stat(System.out, stats1, "ns", "[StringBuffer]");
//		stat(System.out, stats2, "ns", "[Rope]");
//		}
//		}
//
//		System.out.println();
//		System.out.println("**** APPEND PLAN TEST ****");
//		System.out.println();
//
//		final int[][] appendPlan=new int[500][2];
//		for (int j=0;j<appendPlan.length;++j) {
//			appendPlan[j][0] = PerformanceTest.random.nextInt(PerformanceTest.lenCC);
//			appendPlan[j][1] = PerformanceTest.random.nextInt(PerformanceTest.lenCC - appendPlan[j][0]);
//		}
//
//
//		for (int k=20; k<appendPlan.length; k+=20) {
//		System.out.println("Append plan length: " + k);
//		{
//		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT];
//		for (int j=0;j<ITERATION_COUNT;++j){
//		stats0[j] = PerformanceTest.stringAppendTest(aChristmasCarol, appendPlan, k);
//		stats1[j] = PerformanceTest.stringBufferAppendTest(aChristmasCarol, appendPlan, k);
//		stats2[j] = PerformanceTest.ropeAppendTest(aChristmasCarol, appendPlan, k);
//		}
//		stat(System.out, stats0, "ns", "[String]");
//		stat(System.out, stats1, "ns", "[StringBuffer]");
//		stat(System.out, stats2, "ns", "[Rope]");
//		}
//		}


		System.out.println();
		System.out.println("**** INSERT PLAN TEST ****");
		System.out.println("* Insert fragments of A Christmas Carol back into itself.\n");

		final int[][] insertPlan=new int[500][3];
		for (int j=0;j<insertPlan.length;++j) {
			insertPlan[j][0] = PerformanceTest.random.nextInt(PerformanceTest.lenCC);                      //location to insert
			insertPlan[j][1] = PerformanceTest.random.nextInt(PerformanceTest.lenCC);                      //clip from
			insertPlan[j][2] = PerformanceTest.random.nextInt(PerformanceTest.lenCC - insertPlan[j][1]);   //clip length
		}



		for (int k=20; k<insertPlan.length; k+=20) {
		System.out.println("Insert plan length: " + k);
		{
		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT];
		for (int j=0;j<ITERATION_COUNT;++j){
		stats0[j] = PerformanceTest.stringInsertTest(aChristmasCarol, insertPlan, k);
		stats1[j] = PerformanceTest.stringBufferInsertTest(aChristmasCarol, insertPlan, k);
		stats2[j] = PerformanceTest.ropeInsertTest(aChristmasCarol, insertPlan, k);
		}
		stat(System.out, stats0, "ns", "[String]");
		stat(System.out, stats1, "ns", "[StringBuffer]");
		stat(System.out, stats2, "ns", "[Rope]");
		}
		}

		System.out.println();
		System.out.println("**** INSERT PLAN TEST 2 ****");
		System.out.println("* Insert fragments of Benjamin Franklin's Autobiography into\n" +
				           "* A Christmas Carol.\n");

		final int[][] insertPlan2=new int[250][3];
		for (int j=0;j<insertPlan2.length;++j) {
			insertPlan2[j][0] = PerformanceTest.random.nextInt(PerformanceTest.lenCC);                      //location to insert
			insertPlan2[j][1] = PerformanceTest.random.nextInt(PerformanceTest.lenBF);                      //clip from
			insertPlan2[j][2] = PerformanceTest.random.nextInt(PerformanceTest.lenBF - insertPlan2[j][1]);  //clip length
		}

		{
		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT];
		for (int j=0;j<ITERATION_COUNT;++j){
		stats0[j] = PerformanceTest.stringInsertTest2(aChristmasCarol, bensAuto, insertPlan2);
		stats1[j] = PerformanceTest.stringBufferInsertTest2(aChristmasCarol, bensAuto, insertPlan2);
		stats2[j] = PerformanceTest.ropeInsertTest2(aChristmasCarol, bensAuto, insertPlan2);
		}
		stat(System.out, stats0, "ns", "[String]");
		stat(System.out, stats1, "ns", "[StringBuffer]");
		stat(System.out, stats2, "ns", "[Rope]");
		}

		System.out.println();
		System.out.println("**** TRAVERSAL TEST 1 (SIMPLY-CONSTRUCTED DATASTRUCTURES) ****");
		System.out.println("* A traversal test wherein the datastructures are simply\n" +
				           "* constructed, meaning constructed straight from the data\n" +
				           "* file with no further modifications. In this case, we expect\n" +
				           "* rope performance to be competitive, with the charAt version\n" +
				           "* performing better than the iterator version.");
		System.out.println();

		{
		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
		for (int j=0;j<ITERATION_COUNT;++j){
		stats0[j] = PerformanceTest.stringTraverseTest(aChristmasCarol);
		stats1[j] = PerformanceTest.stringBufferTraverseTest(aChristmasCarol);
		stats2[j] = PerformanceTest.ropeTraverseTest_1(aChristmasCarol);
		stats3[j] = PerformanceTest.ropeTraverseTest_2(aChristmasCarol);
		}
		stat(System.out, stats0, "ns", "[String]");
		stat(System.out, stats1, "ns", "[StringBuffer]");
		stat(System.out, stats2, "ns", "[Rope/charAt]");
		stat(System.out, stats3, "ns", "[Rope/itr]");
		}

		System.out.println();
		System.out.println("**** TRAVERSAL TEST 2 (COMPLEXLY-CONSTRUCTED DATASTRUCTURES) ****");
		System.out.println("* A traversal test wherein the datastructures are complexly\n" +
				           "* constructed, meaning constructed through hundreds of insertions,\n" +
				           "* substrings, and deletions (deletions not yet implemented). In\n" +
				           "* this case, we expect rope performance to suffer, with the\n" +
				           "* iterator version performing better than the charAt version.");
		System.out.println();

		{
		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
		for (int j=0;j<ITERATION_COUNT;++j){
		stats0[j] = PerformanceTest.stringTraverseTest2(complexString);
		stats1[j] = PerformanceTest.stringBufferTraverseTest2(complexStringBuffer);
		stats2[j] = PerformanceTest.ropeTraverseTest2_1(complexRope);
		stats3[j] = PerformanceTest.ropeTraverseTest2_2(complexRope);
		}
		stat(System.out, stats0, "ns", "[String]");
		stat(System.out, stats1, "ns", "[StringBuffer]");
		stat(System.out, stats2, "ns", "[Rope/charAt]");
		stat(System.out, stats3, "ns", "[Rope/itr]");
		}

		System.out.println();
		System.out.println("**** REGULAR EXPRESSION TEST (SIMPLY-CONSTRUCTED DATASTRUCTURES) ****");
		System.out.println("* Using a simply-constructed rope and the pattern 'Crachit'.");
		
		Pattern p1 = Pattern.compile("Cratchit");

		{
		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
		for (int j=0;j<ITERATION_COUNT;++j){
		stats0[j] = PerformanceTest.stringRegexpTest(aChristmasCarol, p1);
		stats1[j] = PerformanceTest.stringBufferRegexpTest(aChristmasCarol, p1);
		stats2[j] = PerformanceTest.ropeRegexpTest(aChristmasCarol, p1);
		stats3[j] = PerformanceTest.ropeMatcherRegexpTest(aChristmasCarol, p1);
		}
		stat(System.out, stats0, "ns", "[String]");
		stat(System.out, stats1, "ns", "[StringBuffer]");
		stat(System.out, stats2, "ns", "[Rope]");
		stat(System.out, stats3, "ns", "[Rope.matcher]");
		}

		System.out.println();
		System.out.println("**** REGULAR EXPRESSION TEST (SIMPLY-CONSTRUCTED DATASTRUCTURES) ****");
		System.out.println("* Using a simply-constructed rope and the pattern 'plea.*y'.");

		p1 = Pattern.compile("plea.*y");
		{
		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
		for (int j=0;j<ITERATION_COUNT;++j){
		stats0[j] = PerformanceTest.stringRegexpTest(aChristmasCarol, p1);
		stats1[j] = PerformanceTest.stringBufferRegexpTest(aChristmasCarol, p1);
		stats2[j] = PerformanceTest.ropeRegexpTest(aChristmasCarol, p1);
		stats3[j] = PerformanceTest.ropeMatcherRegexpTest(aChristmasCarol, p1);
		}
		stat(System.out, stats0, "ns", "[String]");
		stat(System.out, stats1, "ns", "[StringBuffer]");
		stat(System.out, stats2, "ns", "[Rope]");
		stat(System.out, stats3, "ns", "[Rope.matcher]");
		}

		System.out.println();
		System.out.println("**** REGULAR EXPRESSION TEST (COMPLEXLY-CONSTRUCTED DATASTRUCTURES) ****");
		System.out.println("* Using a complexly-constructed rope and the pattern 'Crachit'.");

		p1 = Pattern.compile("Cratchit");
		{
		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT], stats4 = new long[ITERATION_COUNT];
		for (int j=0;j<ITERATION_COUNT;++j){
		stats0[j] = PerformanceTest.stringRegexpTest2(complexString, p1);
		stats1[j] = PerformanceTest.stringBufferRegexpTest2(complexStringBuffer, p1);
		stats2[j] = PerformanceTest.ropeRegexpTest2(complexRope, p1);
		stats3[j] = PerformanceTest.ropeRebalancedRegexpTest2(complexRope, p1);
		stats4[j] = PerformanceTest.ropeMatcherRegexpTest2(complexRope, p1);
		}
		stat(System.out, stats0, "ns", "[String]");
		stat(System.out, stats1, "ns", "[StringBuffer]");
		stat(System.out, stats2, "ns", "[Rope]");
		stat(System.out, stats3, "ns", "[Reblncd Rope]");
		stat(System.out, stats4, "ns", "[Rope.matcher]");
		}

		System.out.println();
		System.out.println("**** WRITE TEST ****");
		System.out.println("* Illustrates how to write a Rope to a stream efficiently.");

		{
		long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT];
		for (int j=0;j<ITERATION_COUNT;++j){
		stats0[j] = PerformanceTest.ropeWriteBad(complexRope);
		stats1[j] = PerformanceTest.ropeWriteGood(complexRope);
		}
		stat(System.out, stats0, "ns", "[Out.write]");
		stat(System.out, stats1, "ns", "[Rope.write]");
		}
	}

	private static long ropeWriteGood(Rope complexRope) {
		long x,y;

		Writer out = new StringWriter(complexRope.length());
		x = System.nanoTime();
		try {
			complexRope.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		y = System.nanoTime();
		System.out.printf("[Rope.write]   Executed write in % ,18d ns.\n", (y-x));
		return (y-x);
	}

	private static long ropeWriteBad(Rope complexRope) {
		long x,y;

		Writer out = new StringWriter(complexRope.length());
		x = System.nanoTime();
		try {
			out.write(complexRope.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		y = System.nanoTime();
		System.out.printf("[Out.write]    Executed write in % ,18d ns.\n", (y-x));
		return (y-x);
	}

	private static String readBF() throws Exception {
		final StringWriter out = new StringWriter(182029);
		final BufferedReader in = new BufferedReader(new FileReader("AutobiographyOfBenjaminFranklin_BenjaminFranklin.txt"));

		final char[] c = new char[256];
		int x = -1;
		while (-1 != (x=in.read(c))) {
			out.write(c, 0, x);
		}
		out.close();
		return out.toString();
	}

	private static String readCC() throws Exception {
		final StringWriter out = new StringWriter(182029);
		final BufferedReader in = new BufferedReader(new FileReader("AChristmasCarol_CharlesDickens.txt"));

		final char[] c = new char[256];
		int x = -1;
		while (-1 != (x=in.read(c))) {
			out.write(c, 0, x);
		}
		out.close();
		return out.toString();
	}

	private static long ropeAppendTest(final String aChristmasCarol, final int[][] appendPlan, final int planLength) {
		long x,y;

		x = System.nanoTime();
		Rope result=Rope.BUILDER.build(aChristmasCarol);

		for (int j=0; j<planLength; ++j) {
			final int offset = appendPlan[j][0];
			final int length = appendPlan[j][1];
			result = result.append(result.subSequence(offset, offset+length));
		}
		y = System.nanoTime();
		System.out.printf("[Rope]         Executed append plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
		return (y-x);
	}

	private static long ropeDeleteTest(final String aChristmasCarol, final int[][] prependPlan) {
		long x,y;

		x = System.nanoTime();
		Rope result=Rope.BUILDER.build(aChristmasCarol);

		for (int j=0; j<prependPlan.length; ++j) {
			final int offset = prependPlan[j][0];
			final int length = prependPlan[j][1];
			result = result.delete(offset, offset + length);
		}
		y = System.nanoTime();
		System.out.printf("[Rope]         Executed delete plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
		return (y-x);
	}

	private static long ropeInsertTest(final String aChristmasCarol, final int[][] insertPlan, int planLength) {
		long x,y;

		x = System.nanoTime();
		Rope result=Rope.BUILDER.build(aChristmasCarol);

		for (int j=0; j<planLength; ++j) {
			final int into   = insertPlan[j][0];
			final int offset = insertPlan[j][1];
			final int length = insertPlan[j][2];
			result = result.insert(into, result.subSequence(offset, offset+length));
		}
		y = System.nanoTime();
		System.out.printf("[Rope]         Executed insert plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
		complexRope = result;
		return (y-x);
	}

	private static long ropeInsertTest2(final String aChristmasCarol, final String bensAuto, final int[][] insertPlan) {
		long x,y;

		x = System.nanoTime();
		Rope result=Rope.BUILDER.build(aChristmasCarol);

		for (int j=0; j<insertPlan.length; ++j) {
			final int into   = insertPlan[j][0];
			final int offset = insertPlan[j][1];
			final int length = insertPlan[j][2];
			result = result.insert(into, bensAuto.subSequence(offset, offset+length));
		}
		y = System.nanoTime();
		System.out.printf("[Rope]         Executed insert plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
		return (y-x);
	}

	private static long ropePrependTest(final String aChristmasCarol, final int[][] prependPlan, int planLength) {
		long x,y;

		x = System.nanoTime();
		Rope result=Rope.BUILDER.build(aChristmasCarol);

		for (int j=0; j<planLength; ++j) {
			final int offset = prependPlan[j][0];
			final int length = prependPlan[j][1];
			result = result.subSequence(offset, offset+length).append(result);
		}
		y = System.nanoTime();
		System.out.printf("[Rope]         Executed prepend plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
		return (y-x);
	}

	private static long ropeTraverseTest_1(final String aChristmasCarol) {
		long x,y,result=0;
		final Rope r=Rope.BUILDER.build(aChristmasCarol);

		x = System.nanoTime();

		for (int j=0; j<r.length(); ++j) result+=r.charAt(j);

		y = System.nanoTime();
		System.out.printf("[Rope/charAt]  Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), result);
		return (y-x);
	}

	private static long ropeTraverseTest_2(final String aChristmasCarol) {
		long x,y,result=0;
		final Rope r=Rope.BUILDER.build(aChristmasCarol);
		
		x = System.nanoTime();

		for (final char c: r) result+=c;

		y = System.nanoTime();
		System.out.printf("[Rope/itr]     Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), result);
		return (y-x);
	}

	private static long ropeTraverseTest2_1(Rope aChristmasCarol) {
		long x,y;

		Rope result=aChristmasCarol;
		
		int r=0;
		x = System.nanoTime();
		for (int j=0; j<result.length(); ++j) r+=result.charAt(j);
		y = System.nanoTime();
		System.out.printf("[Rope/charAt]  Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), r);
		return (y-x);
	}

	private static long ropeTraverseTest2_2(Rope aChristmasCarol) {
		long x,y;

		Rope result=aChristmasCarol;
		
		int r=0;
		x = System.nanoTime();
		for (final char c: result) r+=c;
		y = System.nanoTime();
		System.out.printf("[Rope/itr]     Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), r);
		return (y-x);
	}

	private static long stringAppendTest(final String aChristmasCarol, final int[][] appendPlan, final int planLength) {
		long x,y;

		x = System.nanoTime();
		String result=aChristmasCarol;

		for (int j=0; j<planLength; ++j) {
			final int offset = appendPlan[j][0];
			final int length = appendPlan[j][1];
			result = result.concat(result.substring(offset, offset + length));
		}
		y = System.nanoTime();
		System.out.printf("[String]       Executed append plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		return (y-x);
	}

	private static long stringBufferAppendTest(final String aChristmasCarol, final int[][] appendPlan, final int planLength) {
		long x,y;

		x = System.nanoTime();
		final StringBuffer result=new StringBuffer(aChristmasCarol);

		for (int j=0; j<planLength; ++j) {
			final int offset = appendPlan[j][0];
			final int length = appendPlan[j][1];
			result.append(result.subSequence(offset, offset+length));
		}
		y = System.nanoTime();
		System.out.printf("[StringBuffer] Executed append plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		return (y-x);
	}

	private static long stringBufferDeleteTest(final String aChristmasCarol, final int[][] prependPlan) {
		long x,y;

		x = System.nanoTime();
		final StringBuffer result=new StringBuffer(aChristmasCarol);

		for (int j=0; j<prependPlan.length; ++j) {
			final int offset = prependPlan[j][0];
			final int length = prependPlan[j][1];
			result.delete(offset, offset+length);
		}
		y = System.nanoTime();
		System.out.printf("[StringBuffer] Executed delete plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		return (y-x);
	}

	private static long stringBufferInsertTest(final String aChristmasCarol, final int[][] insertPlan, int planLength) {
		long x,y;

		x = System.nanoTime();
		final StringBuffer result=new StringBuffer(aChristmasCarol);

		for (int j=0; j<planLength; ++j) {
			final int into   = insertPlan[j][0];
			final int offset = insertPlan[j][1];
			final int length = insertPlan[j][2];
			result.insert(into, result.subSequence(offset, offset+length));
		}
		y = System.nanoTime();
		System.out.printf("[StringBuffer] Executed insert plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		complexStringBuffer = result;
		return (y-x);
	}

	private static long stringBufferInsertTest2(final String aChristmasCarol, final String bensAuto, final int[][] insertPlan) {
		long x,y;

		x = System.nanoTime();
		final StringBuffer result=new StringBuffer(aChristmasCarol);

		for (int j=0; j<insertPlan.length; ++j) {
			final int into   = insertPlan[j][0];
			final int offset = insertPlan[j][1];
			final int length = insertPlan[j][2];
			result.insert(into, bensAuto.subSequence(offset, offset+length));
		}
		y = System.nanoTime();
		System.out.printf("[StringBuffer] Executed insert plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		return (y-x);
	}

	private static long stringBufferPrependTest(final String aChristmasCarol, final int[][] prependPlan, int planLength) {
		long x,y;

		x = System.nanoTime();
		final StringBuffer result=new StringBuffer(aChristmasCarol);

		for (int j=0; j<planLength; ++j) {
			final int offset = prependPlan[j][0];
			final int length = prependPlan[j][1];
			result.insert(0, result.subSequence(offset, offset+length));
		}
		y = System.nanoTime();
		System.out.printf("[StringBuffer] Executed prepend plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		return (y-x);
	}

	private static long stringBufferTraverseTest(final String aChristmasCarol) {
		long x,y,result=0;

		x = System.nanoTime();

		final StringBuffer b=new StringBuffer(aChristmasCarol);
		for (int j=0; j<b.length(); ++j) result+=b.charAt(j);

		y = System.nanoTime();
		System.out.printf("[StringBuffer] Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), result);
		return (y-x);

	}

	private static long stringBufferTraverseTest2(final StringBuffer aChristmasCarol) {
		long x,y;

		final StringBuffer result=aChristmasCarol;
		
		int r=0;
		x = System.nanoTime();
		for (int j=0; j<result.length(); ++j) r+=result.charAt(j);
		y = System.nanoTime();
		System.out.printf("[StringBuffer] Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), r);
		return (y-x);
	}

	private static long stringDeleteTest(final String aChristmasCarol, final int[][] prependPlan) {
		long x,y;

		x = System.nanoTime();
		String result=aChristmasCarol;

		for (int j=0; j<prependPlan.length; ++j) {
			final int offset = prependPlan[j][0];
			final int length = prependPlan[j][1];
			result = result.substring(0, offset).concat(result.substring(offset+length));
		}
		y = System.nanoTime();
		System.out.printf("[String]       Executed delete plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		return (y-x);
	}

	private static long stringInsertTest(final String aChristmasCarol, final int[][] insertPlan, int planLength) {
		long x,y;

		x = System.nanoTime();
		String result=aChristmasCarol;

		for (int j=0; j<planLength; ++j) {
			final int into   = insertPlan[j][0];
			final int offset = insertPlan[j][1];
			final int length = insertPlan[j][2];
			result = result.substring(0, into).concat(result.substring(offset, offset + length)).concat(result.substring(into));
			
		}
		y = System.nanoTime();
		System.out.printf("[String]       Executed insert plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		complexString = result;
		return (y-x);
	}

	private static long stringInsertTest2(final String aChristmasCarol, final String bensAuto, final int[][] insertPlan) {
		long x,y;

		x = System.nanoTime();
		String result=aChristmasCarol;

		for (int j=0; j<insertPlan.length; ++j) {
			final int into   = insertPlan[j][0];
			final int offset = insertPlan[j][1];
			final int length = insertPlan[j][2];
			result = result.substring(0, into).concat(bensAuto.substring(offset, offset + length)).concat(result.substring(into));
		}
		y = System.nanoTime();
		System.out.printf("[String]       Executed insert plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		return (y-x);
	}

	private static long stringPrependTest(final String aChristmasCarol, final int[][] prependPlan, int planLength) {
		long x,y;

		x = System.nanoTime();
		String result=aChristmasCarol;

		for (int j=0; j<planLength; ++j) {
			final int offset = prependPlan[j][0];
			final int length = prependPlan[j][1];
			result = result.substring(offset, offset + length).concat(result);
		}
		y = System.nanoTime();
		System.out.printf("[String]       Executed prepend plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
		return (y-x);
	}

	private static long stringTraverseTest(final String aChristmasCarol) {
		long x,y,result=0;

		x = System.nanoTime();

		for (int j=0; j<aChristmasCarol.length(); ++j) result+=aChristmasCarol.charAt(j);

		y = System.nanoTime();
		System.out.printf("[String]       Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), result);
		return (y-x);
	}

	private static long stringTraverseTest2(final String aChristmasCarol) {
		long x,y;

		String result=aChristmasCarol;

		int r=0;
		x = System.nanoTime();
		for (int j=0; j<result.length(); ++j) r+=result.charAt(j);
		y = System.nanoTime();
		System.out.printf("[String]       Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), r);
		return (y-x);
	}

	private static long stringRegexpTest(final String aChristmasCarol, Pattern pattern) {
		long x,y;

		x = System.nanoTime();

		int result = 0;
		Matcher m = pattern.matcher(aChristmasCarol);
		while (m.find()) ++result;
		
		y = System.nanoTime();
		System.out.printf("[String]       Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
		return (y-x);
	}

	private static long stringBufferRegexpTest(final String aChristmasCarol, Pattern pattern) {
		long x,y;
		StringBuffer buffer = new StringBuffer(aChristmasCarol);

		x = System.nanoTime();

		int result = 0;
		Matcher m = pattern.matcher(buffer);
		while (m.find()) ++result;
		
		y = System.nanoTime();
		System.out.printf("[StringBuffer] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
		return (y-x);
	}

	private static long ropeRegexpTest(final String aChristmasCarol, Pattern pattern) {
		long x,y;
		Rope rope = Rope.BUILDER.build(aChristmasCarol);

		x = System.nanoTime();

		int result = 0;
		Matcher m = pattern.matcher(rope);
		while (m.find()) ++result;
		
		y = System.nanoTime();
		System.out.printf("[Rope]         Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
		return (y-x);
	}

	private static long ropeMatcherRegexpTest(final String aChristmasCarol, Pattern pattern) {
		long x,y;
		Rope rope = Rope.BUILDER.build(aChristmasCarol);

		x = System.nanoTime();

		int result = 0;
		Matcher m = rope.matcher(pattern); 
		while (m.find()) ++result;
		
		y = System.nanoTime();
		System.out.printf("[Rope.matcher] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
		return (y-x);
	}
	


	private static long stringRegexpTest2(final String aChristmasCarol, Pattern pattern) {
		long x,y;

		x = System.nanoTime();

		int result = 0;
		Matcher m = pattern.matcher(aChristmasCarol);
		while (m.find()) ++result;
		
		y = System.nanoTime();
		System.out.printf("[String]       Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
		return (y-x);
	}

	private static long stringBufferRegexpTest2(final StringBuffer aChristmasCarol, Pattern pattern) {
		long x,y;

		x = System.nanoTime();

		int result = 0;
		Matcher m = pattern.matcher(aChristmasCarol);
		while (m.find()) ++result;
		
		y = System.nanoTime();
		System.out.printf("[StringBuffer] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
		return (y-x);
	}

	private static long ropeRegexpTest2(final Rope aChristmasCarol, Pattern pattern) {
		long x,y;

		x = System.nanoTime();

		int result = 0;
		Matcher m = pattern.matcher(aChristmasCarol);
		while (m.find()) ++result;
		
		y = System.nanoTime();
		System.out.printf("[Rope]         Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
		return (y-x);
	}

	private static long ropeRebalancedRegexpTest2(final Rope aChristmasCarol, Pattern pattern) {
		long x,y;

		x = System.nanoTime();

		CharSequence adaptedRope = aChristmasCarol.rebalance(); //Rope.BUILDER.buildForRegexpSearching(aChristmasCarol);
		int result = 0;
		Matcher m = pattern.matcher(adaptedRope);
		while (m.find()) ++result;
		
		y = System.nanoTime();
		System.out.printf("[Reblncd Rope] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
		return (y-x);
	}

	private static long ropeMatcherRegexpTest2(final Rope aChristmasCarol, Pattern pattern) {
		long x,y;

		x = System.nanoTime();
		
		int result = 0;
		Matcher m = aChristmasCarol.matcher(pattern);
		while (m.find()) ++result;
		
		y = System.nanoTime();
		System.out.printf("[Rope.matcher] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
		return (y-x);
	}

	private static String time(final long x, final long y) {
		return (y-x) + "ns";
	}
	
	private static void stat(PrintStream out, long[] stats, String unit, String prefix) {
		if (stats.length < 3) 
			System.err.println("Cannot print stats.");
		Arrays.sort(stats);
		
		double median = ((stats.length & 1) == 1 ? stats[stats.length >> 1]: (stats[stats.length >> 1] + stats[1 + (stats.length >> 1)]) / 2);
		double average = 0;
		for (int j=1;j<stats.length-1;++j) {
			average += stats[j];
		}
		average /= stats.length - 2;
		out.printf("%-14s Average=% ,16.0f %s Median=% ,16.0f%s\n", prefix, average, unit, median, unit);
	}

}
