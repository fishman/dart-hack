package org.ahmadsoft.ropes.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.ahmadsoft.ropes.Rope;

/**
 * Comprehensive performance test on Ropes. The test is broken to two: Ropes
 * created from java.lang.String and Ropes created from char[]. Those two cases
 * exercise different paths in the code, so performance test need to account for
 * both.
 * 
 * This is a fast test that can be used routinely in performance improvements,
 * in contrast to the big tests in this package that may be executed in major
 * milestones. Results should be consistent among all tests, IE if numbers go up
 * or down in this tests, similar changes are expected in the longer test.
 * 
 * EXECUTE THIS TEST WITH NO MORE THAN 32M HEAP TO EXCERSISE GARBAGE COLLECTION.
 * 
 * @author Elli Albek
 */
public class StringPerformanceTest {

	private static final int ITERATION_COUNT = 50;
	Rope lastResult;
	Map<String, Object> results = new LinkedHashMap<String, Object>();

	public static void main(final String[] args) throws Exception {
		new StringPerformanceTest().run(args);
	}

	void run(final String[] args) throws IOException {
		// load test data
		char[] test_RAW = PerformanceTest.readCC();
		final Rope charArrayRope = Rope.BUILDER.build(test_RAW);
		final String testString = new String(test_RAW);
		// release temp memory
		test_RAW = null;

		// long[] stats = new long[ITERATION_COUNT];
		ArrayList<Long> stats = new ArrayList<Long>(ITERATION_COUNT);
		long totalTime = System.currentTimeMillis();

		// ---------------- Java String ---------------------
		// for reference

		// indexOf
		for (int j = 0; j < ITERATION_COUNT * 10; ++j) {
			stats.add(indexOf(testString, "Scrooge"));
		}
		printStats(stats, "JavaString.indexOf", false);

		for (int j = 0; j < ITERATION_COUNT * 10; ++j) {
			stats.add(indexOfChar(testString, 'o'));
		}
		printStats(stats, "JavaString.indexOfChar", false);

		// ---------------- Rope String ---------------------
		ropeTestList(Rope.BUILDER.build(testString), "String");

		// ------------- Char array Rope --------------
		ropeTestList(charArrayRope, "Array");

		totalTime = System.currentTimeMillis() - totalTime;
		System.out.println("END " + (totalTime / 1000f) + " sec");

		String fileName = "StringPerformanceTest.csv";
		if (args.length > 0)
			fileName = args[0];
		StatsFile statsFile = StatsFile.createSVNStatsFile(fileName,
				new ArrayList<String>(results.keySet()));
		statsFile.writeCSVLine(new ArrayList<Object>(results.values()));
	}

	/**
	 * @param testStringRope
	 */
	private void ropeTestList(final Rope testStringRope, String testName) {

		ArrayList<Long> stats = new ArrayList<Long>(ITERATION_COUNT);

		System.out.println();
		System.out.println("--- " + testName + " Rope ---");
		// indexOf
		for (int j = 0; j < ITERATION_COUNT; ++j) {
			stats.add(indexOf(testStringRope, "Scrooge"));
		}
		printStats(stats, testName + ".indexOf", true);

		for (int j = 0; j < ITERATION_COUNT; ++j) {
			stats.add(indexOfChar(testStringRope, 'o'));
		}
		printStats(stats, testName + ".indexOfChar", true);

		// padReplace
		for (int j = 0; j < ITERATION_COUNT; ++j) {
			stats.add(padReplace(testStringRope, "Scrooge"));
		}
		printStats(stats, testName + ".padReplace", true);

		// searchReplace
		for (int j = 0; j < ITERATION_COUNT; ++j) {
			stats.add(searchReplace(testStringRope, "Scrooge",
					"XXXXXXXX YYYYYYYY ZZZZZZZZ"));
		}
		printStats(stats, testName + ".searchReplace", true);

		// Complex String Rope indexOf
		for (int j = 0; j < ITERATION_COUNT; ++j) {
			stats.add(indexOf(lastResult, "Scrooge"));
		}
		printStats(stats, "Complex "+ testName + ".indexOf", true);

		// Complex String Rope indexOf
		for (int j = 0; j < ITERATION_COUNT; ++j) {
			stats.add(indexOfChar(lastResult, 'o'));
		}
		printStats(stats, "Complex "+ testName + ".indexOfChar", true);

		// Complex String Rope searchReplace
		Rope last = lastResult;
		for (int j = 0; j < ITERATION_COUNT; ++j) {
			stats.add(searchReplace(last, "XXXXXXX", "Scrooge"));
		}
		printStats(stats, "Complex "+ testName + ".searchReplace", true);

		// padReplace Complex
		// need to keep last results since it is constantly changing, so each
		// test will run on different text and aggregate statistics will not be
		// meaningful.
		last = lastResult;
		for (int j = 0; j < ITERATION_COUNT; ++j) {
			stats.add(padReplace(last, "Scrooge"));
		}
		printStats(stats, "Complex "+ testName + ".padReplace", true);
	}

	long indexOf(final Rope rope, final String toFind) {
		long x, y;
		x = System.nanoTime();
		int found = 0;
		int index = 0;
		while ((index = rope.indexOf(toFind, index)) > 0) {
			found++;
			index += toFind.length();
		}

		y = System.nanoTime();
		return (y - x);
	}

	long indexOfChar(final Rope rope, final char toFind) {
		long x, y;
		x = System.nanoTime();
		int found = 0;
		int index = 0;
		while ((index = rope.indexOf(toFind, index)) > 0) {
			found++;
			index++;
		}

		y = System.nanoTime();
		return (y - x);
	}

	long indexOf(final String s, final String toFind) {
		long x, y;
		x = System.nanoTime();
		int found = 0;
		int index = 0;
		while ((index = s.indexOf(toFind, index)) > 0) {
			found++;
			index += toFind.length();
		}

		y = System.nanoTime();
		return (y - x);
	}

	long indexOfChar(final String s, final char toFind) {
		long x, y;
		x = System.nanoTime();
		int found = 0;
		int index = 0;
		while ((index = s.indexOf(toFind, index)) > 0) {
			found++;
			index++;
		}

		y = System.nanoTime();
		return (y - x);
	}

	long searchReplace(Rope rope, final String toFind, final String replace) {
		long x, y;
		x = System.nanoTime();
		int index = 0;
		int found = 0;
		while ((index = rope.indexOf(toFind, index)) > 0) {
			found++;
			rope = rope.subSequence(0, index).append(replace).append(
					rope.subSequence(index + toFind.length(), rope.length()));
			index = index + replace.length() - toFind.length();
		}
		y = System.nanoTime();

		lastResult = rope;
		return (y - x);
	}

	long padReplace(Rope rope, final String toFind) {
		long x, y;
		x = System.nanoTime();
		int index = 0;
		int found = 0;
		while ((index = rope.indexOf(toFind, index)) > 0) {
			found++;
			Rope subSequence = rope.subSequence(index, index + toFind.length());
			subSequence = subSequence.append("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			rope = rope.subSequence(0, index).append(subSequence).append(
					rope.subSequence(index + toFind.length(), rope.length()));
			index = index + toFind.length();
		}
		y = System.nanoTime();

		lastResult = rope;
		return (y - x);
	}

	/**
	 * Logs stats, and returns three numbers: [Average,Median,Stdev]
	 */
	double[] printStats(long[] samples, String prefix, boolean log) {
		double[] stats = PerformanceTest.stats(samples);
		System.out.printf("%-30s Average=% ,16.2f ns Median=% ,16.2f ns\n", prefix,
				stats[0], stats[1]);

		if (log) {
			// results logged in ms
			results.put(prefix, stats[0] / 1000000f);
		}
		return stats;
	}

	double[] printStats(ArrayList<Long> samples, String prefix, boolean log) {
		long[] longs = new long[samples.size()];
		for (int i = 0; i < samples.size(); i++)
			longs[i] = samples.get(i);
		samples.clear();

		return printStats(longs, prefix, log);
	}
}
