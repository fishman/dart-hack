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

import java.io.BufferedInputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javolution.text.Text;

import org.ahmadsoft.ropes.Rope;
import org.ahmadsoft.ropes.impl.AbstractRope;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 * Performs an extensive performance test comparing Ropes, Strings, 
 * StringBuffers, and Javolution Text.
 * <pre>
 * usage: PerformanceTest.java
 * -h,--help                    display this help message.
 *    --include-all             default, include all data structures in the
 *                              test
 *    --include-rope            include org.ahmadsoft.ropes.Rope in the test
 *    --include-string          include java.lang.String in the test
 *    --include-string-buffer   include java.lang.StringBuffer in the test
 *    --include-text            include javolution.text.Text in the test
 * -p,--plan &lt;min:max:step&gt;     sets the test plan size
 * -s,--seed &lt;seed&gt;             specify a integer seed for random number
 *                              generation.
 *    --test-all                default, run all performance test suites
 *    --test-mutation           test mutations (append, prepend, insert, and
 *                              delete)
 *    --test-serialization      test serialization performance
 *    --test-traversal          test traversals (charAt, indexOf)
 * -v,--verbose                 displays verbose test output.
 *    --version                 display version string and exit
 * </pre>
 * @author aahmad
 */
public class PerformanceTest {

    private static final int seed=342342;
    private final Random random;
    
    static int ITERATION_COUNT = 7;
    private int stepSize = 20, startSize = 20, planLength = 500;

    private String complexString = null; 
    private StringBuffer complexStringBuffer = null; 
    Rope complexRope = null;
    private Text complexText = null;

    /** Test input data **/
    String aChristmasCarol, bensAuto;
    /** Test input data **/
    char[] aChristmasCarol_RAW, bensAuto_RAW;
    
    private boolean testSB=true, testS=true, testT=true, testR=true, verb=false, testSer=true, testMut=true, testTra=true;
    
    @SuppressWarnings("static-access")
    public static void main(final String[] args) throws Exception {
        Options options = new Options();
        Option seed  = OptionBuilder.withArgName( "seed" )
                                    .hasArgs(3)
                                    .withValueSeparator(':')
                                    .withDescription( "specify a integer seed for random number generation." )
                                    .withLongOpt("seed")
                                    .withType(Integer.class)
                                    .create( "s" );
        options.addOption(seed);
        Option plan  = OptionBuilder.withArgName( "min:max:step" )
                                    .hasArgs(3)
                                    .withValueSeparator(':')
                                    .withDescription( "sets the test plan size" )
                                    .create( "p" );
        plan.setLongOpt("plan");
        options.addOption(plan);
        Option verb  = OptionBuilder.withDescription( "displays verbose test output." )
                                    .withLongOpt("verbose")
                                    .create( "v" );
        options.addOption(verb);
        Option incSb  = OptionBuilder.withDescription( "include java.lang.StringBuffer in the test" )
                                     .withLongOpt("include-string-buffer")
                                     .create();
        options.addOption(incSb);
        Option incSt  = OptionBuilder.withDescription( "include java.lang.String in the test" )
                                     .withLongOpt("include-string")
                                     .create();
        options.addOption(incSt);
        Option incRp  = OptionBuilder.withDescription( "include org.ahmadsoft.ropes.Rope in the test" )
                                     .withLongOpt("include-rope")
                                     .create();
        options.addOption(incRp);
        Option incTx  = OptionBuilder.withDescription( "include javolution.text.Text in the test" )
                                     .withLongOpt("include-text")
                                     .create();
        options.addOption(incTx);
        Option incAl  = OptionBuilder.withDescription( "default, include all data structures in the test" )
                                     .withLongOpt("include-all")
                                     .create();
        options.addOption(incAl);
        Option tstMu  = OptionBuilder.withDescription( "test mutations (append, prepend, insert, and delete)" )
                                     .withLongOpt("test-mutation")
                                     .create();
        options.addOption(tstMu);
        Option tstTr  = OptionBuilder.withDescription( "test traversals (charAt, indexOf)" )
                                     .withLongOpt("test-traversal")
                                     .create();
        options.addOption(tstTr);
        Option tstSr  = OptionBuilder.withDescription( "test serialization performance" )
                                     .withLongOpt("test-serialization")
                                     .create();
        options.addOption(tstSr);
        Option tstAl  = OptionBuilder.withDescription( "default, run all performance test suites" )
                                     .withLongOpt("test-all")
                                     .create();
        options.addOption(tstAl);
        Option vers  = OptionBuilder.withDescription( "display version string and exit" )
                                    .withLongOpt("version")
                                    .create();
        options.addOption(vers);
        options.addOption("h", "help", false, "display this help message.");
        
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse( options, args);
        if (cmd.hasOption('h')) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "PerformanceTest.java", options );
            return;
        }
        if (cmd.hasOption("version")) {
            System.out.printf("PerformanceTest.java 1.0%n" +
                    "Copyright (C) 2007 Amin Ahmad%n" +
                    "License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>%n" +
                    "This is free software: you are free to change and redistribute it.%n" +
                    "There is NO WARRANTY, to the extent permitted by law.");
            return;
        }
        
        new PerformanceTest(cmd).run();
    }
    
    PerformanceTest(final CommandLine cmd){
        int newSeed = seed;
        if (cmd.hasOption('s'))
            newSeed = Integer.parseInt(cmd.getOptionValue('s'));
        if (!cmd.hasOption("include-all")) {
            testR = testS = testSB = testT = false;
            testR = cmd.hasOption("include-rope");
            testS = cmd.hasOption("include-string");
            testSB= cmd.hasOption("include-string-buffer");
            testT = cmd.hasOption("include-text");
            if (!testR && !testS && !testSB && !testT)
            	testR = testS = testSB = testT = true;
        }
        if (!cmd.hasOption("test-all")) {
            testMut = testSer = testTra = false;
            testMut = cmd.hasOption("test-mutation");
            testSer = cmd.hasOption("test-serialization");
            testTra = cmd.hasOption("test-traversal");
            if (!testMut && !testSer && !testTra)
                testMut = testSer = testTra = true;
            	
        }
        verb = cmd.hasOption('v');
        if (cmd.hasOption('p')) {
            startSize = Integer.parseInt(cmd.getOptionValues('p')[0]);
            planLength = Integer.parseInt(cmd.getOptionValues('p')[1]);
            stepSize = Integer.parseInt(cmd.getOptionValues('p')[2]);
            
            if (startSize > planLength)
                throw new IllegalArgumentException("Minimum size must be less or equal to maximum.");
        }
        random = new Random(newSeed);
    }
    
    void run() throws IOException {
        
        // load test data
        loadTestFiles();
        
        if (isTestMutation()) {
            System.out.printf("%n**** DELETE PLAN TEST ****%n%n");
    
            final int[][] deletePlan = deleteTestPlan(aChristmasCarol.length());
    
            for (int k=startSize; k<=deletePlan.length; k+=stepSize) {
                System.out.println("Delete plan length: " + k);
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringDeleteTest(aChristmasCarol, deletePlan);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferDeleteTest(aChristmasCarol, deletePlan);
                    if (isTestRope())
                        stats2[j] = ropeDeleteTest(aChristmasCarol, deletePlan);
                    if (isTestText())
                        stats3[j] = textDeleteTest(aChristmasCarol, deletePlan);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope())
                    printStats(System.out, stats2, "ns", "[Rope]");
                if (isTestText())
                    printStats(System.out, stats3, "ns", "[Text]");
            }
    
            System.out.printf("%n**** PREPEND PLAN TEST ****%n%n");
    
            final int[][] prependPlan = prependTestPlan(aChristmasCarol.length());
    
            for (int k=startSize; k<=prependPlan.length; k+=stepSize) {
                System.out.println("Prepend plan length: " + k);
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringPrependTest(aChristmasCarol, prependPlan, k);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferPrependTest(aChristmasCarol, prependPlan, k);
                    if (isTestRope())
                        stats2[j] = ropePrependTest(aChristmasCarol, prependPlan, k);
                    if (isTestText())
                        stats3[j] = textPrependTest(aChristmasCarol, prependPlan, k);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope())
                    printStats(System.out, stats2, "ns", "[Rope]");
                if (isTestText())
                    printStats(System.out, stats3, "ns", "[Text]");
            }
    
            System.out.printf("%n**** APPEND PLAN TEST ****%n%n");
    
            final int[][] appendPlan = prependTestPlan(aChristmasCarol.length());
    
            for (int k=startSize; k<=appendPlan.length; k+=stepSize) {
                System.out.println("Append plan length: " + k);
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringAppendTest(aChristmasCarol, appendPlan, k);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferAppendTest(aChristmasCarol, appendPlan, k);
                    if (isTestRope())
                        stats2[j] = ropeAppendTest(aChristmasCarol, appendPlan, k);
                    if (isTestText())
                        stats3[j] = textAppendTest(aChristmasCarol, prependPlan, k);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope())
                    printStats(System.out, stats2, "ns", "[Rope]");
                if (isTestText())
                    printStats(System.out, stats3, "ns", "[Text]");
            }
    
    
            System.out.printf("%n**** INSERT PLAN TEST ****%n");
            System.out.printf("* Insert fragments of A Christmas Carol back into itself.%n");
    
            final int[][] insertPlan = insertTestPlan(aChristmasCarol.length());
    
            for (int k=startSize; k<=insertPlan.length; k+=stepSize) {
                System.out.println("Insert plan length: " + k);
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringInsertTest(aChristmasCarol, insertPlan, k);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferInsertTest(aChristmasCarol, insertPlan, k);
                    if (isTestRope())
                        stats2[j] = ropeInsertTest(Rope.BUILDER.build(aChristmasCarol_RAW), insertPlan, k);
                    if (isTestText())
                        stats3[j] = textInsertTest(aChristmasCarol, insertPlan, k);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope())
                    printStats(System.out, stats2, "ns", "[Rope]");
                if (isTestText())
                    printStats(System.out, stats3, "ns", "[Text]");
            }
    
            System.out.printf("%n**** INSERT PLAN TEST 2 ****%n");
            System.out.printf("* Insert fragments of Benjamin Franklin's Autobiography into%n" +
                              "* A Christmas Carol.%n");
    
            final int[][] insertPlan2 = createInsertPlan(aChristmasCarol.length(), bensAuto.length());
    
            {
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringInsertTest2(aChristmasCarol, bensAuto, insertPlan2);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferInsertTest2(aChristmasCarol, bensAuto, insertPlan2);
                    if (isTestRope())
                        stats2[j] = ropeInsertTest2(aChristmasCarol, bensAuto, insertPlan2);
                    if (isTestText())
                        stats3[j] = textInsertTest2(aChristmasCarol, bensAuto, insertPlan2);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope())
                    printStats(System.out, stats2, "ns", "[Rope]");
                if (isTestText())
                    printStats(System.out, stats3, "ns", "[Text]");
            }
        }

        if (isTestTraveral()) {
            System.out.printf("%n**** TRAVERSAL TEST 1 (SIMPLY-CONSTRUCTED DATASTRUCTURES) ****%n");
            System.out.printf("* A traversal test wherein the datastructures are simply%n" +
                              "* constructed, meaning constructed straight from the data%n" +
                              "* file with no further modifications. In this case, we expect%n" +
                              "* rope performance to be competitive, with the charAt version%n" +
                              "* performing better than the iterator version.%n%n");
    
            {
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT], stats4 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringTraverseTest(aChristmasCarol);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferTraverseTest(aChristmasCarol);
                    if (isTestRope()) {
                        stats2[j] = ropeTraverseTest_1(Rope.BUILDER.build(aChristmasCarol_RAW));
                        stats3[j] = ropeTraverseTest_2(Rope.BUILDER.build(aChristmasCarol_RAW));
                    }
                    if (isTestText())
                        stats4[j] = textTraverseTest_1(new Text(aChristmasCarol));
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope()) {
                    printStats(System.out, stats2, "ns", "[Rope/charAt]");
                    printStats(System.out, stats3, "ns", "[Rope/itr]");
                }
                if (isTestText())
                    printStats(System.out, stats4, "ns", "[Text/charAt]");
            }
    
            System.out.printf("%n**** TRAVERSAL TEST 2 (COMPLEXLY-CONSTRUCTED DATASTRUCTURES) ****%n");
            System.out.printf("* A traversal test wherein the datastructures are complexly\n" +
                               "* constructed, meaning constructed through hundreds of insertions,\n" +
                               "* substrings, and deletions (deletions not yet implemented). In\n" +
                               "* this case, we expect rope performance to suffer, with the\n" +
                               "* iterator version performing better than the charAt version.%n%n");
    
            {
                setupComplexRopes();
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT], stats4 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringTraverseTest2(complexString);
                    if (isTestStringBuffer())
                    	stats1[j] = stringBufferTraverseTest2(complexStringBuffer);
                    if (isTestRope()) {
                        stats2[j] = ropeTraverseTest2_1(complexRope);
                        stats3[j] = ropeTraverseTest2_2(complexRope);
                    }
                    if (isTestText())
                        stats4[j] = textTraverseTest2(complexText);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope()) {
                    printStats(System.out, stats2, "ns", "[Rope/charAt]");
                    printStats(System.out, stats3, "ns", "[Rope/itr]");
                }
                if (isTestText())
                    printStats(System.out, stats4, "ns", "[Text/charAt]");
            }
    
            System.out.printf("%n**** REGULAR EXPRESSION TEST (SIMPLY-CONSTRUCTED DATASTRUCTURES) ****%n");
            System.out.printf("* Using a simply-constructed rope and the pattern 'Crachit'.%n");
            
            Pattern p1 = Pattern.compile("Cratchit");
    
            {
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT], stats4 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringRegexpTest(aChristmasCarol, p1);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferRegexpTest(aChristmasCarol, p1);
                    if (isTestRope()) {
                        stats2[j] = ropeRegexpTest(Rope.BUILDER.build(aChristmasCarol_RAW), p1);
                        stats3[j] = ropeMatcherRegexpTest(Rope.BUILDER.build(aChristmasCarol_RAW), p1);
                    }
                    if (isTestText())
                        stats4[j] = textRegexpTest(new Text(aChristmasCarol), p1);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope()) {
                    printStats(System.out, stats2, "ns", "[Rope]");
                    printStats(System.out, stats3, "ns", "[Rope.matcher]");
                }
                if (isTestText())
                    printStats(System.out, stats4, "ns", "[Text]");
            }
    
            System.out.printf("%n**** REGULAR EXPRESSION TEST (SIMPLY-CONSTRUCTED DATASTRUCTURES) ****%n");
            System.out.printf("* Using a simply-constructed rope and the pattern 'plea.*y'.%n");
    
            p1 = Pattern.compile("plea.*y");
            {
                setupComplexRopes();
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT], stats4 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringRegexpTest(aChristmasCarol, p1);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferRegexpTest(aChristmasCarol, p1);
                    if (isTestRope()) {
                        stats2[j] = ropeRegexpTest(Rope.BUILDER.build(aChristmasCarol_RAW), p1);
                        stats3[j] = ropeMatcherRegexpTest(Rope.BUILDER.build(aChristmasCarol_RAW), p1);
                    }
                    if (isTestText())
                        stats4[j] = textRegexpTest(new Text(aChristmasCarol), p1);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope()) {
                    printStats(System.out, stats2, "ns", "[Rope]");
                    printStats(System.out, stats3, "ns", "[Rope.matcher]");
                }
                if (isTestText())
                    printStats(System.out, stats4, "ns", "[Text]");
            }
    
            System.out.printf("%n**** REGULAR EXPRESSION TEST (COMPLEXLY-CONSTRUCTED DATASTRUCTURES) ****%n");
            System.out.printf("* Using a complexly-constructed rope and the pattern 'Crachit'.%n");
    
            p1 = Pattern.compile("Cratchit");
            {
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT], stats4 = new long[ITERATION_COUNT], stats5 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringRegexpTest2(complexString, p1);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferRegexpTest2(complexStringBuffer, p1);
                    if (isTestRope()) { 
                        stats2[j] = ropeRegexpTest2(complexRope, p1);
                        stats3[j] = ropeRebalancedRegexpTest2(complexRope, p1);
                        stats4[j] = ropeMatcherRegexpTest2(complexRope, p1);
                    }
                    if (isTestText())
                        stats5[j] = textRegexpTest2(complexText, p1);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope()) { 
                    printStats(System.out, stats2, "ns", "[Rope]");
                    printStats(System.out, stats3, "ns", "[Reblncd Rope]");
                    printStats(System.out, stats4, "ns", "[Rope.matcher]");
                }
                if (isTestText())
                    printStats(System.out, stats5, "ns", "[Text]");
            }
    
            System.out.printf("%n**** STRING SEARCH TEST ****%n");
            System.out.printf("* Using a simply constructed rope and the pattern 'consumes %n" +
                              "* faster than Labor wears; while the used key is always bright,'.%n");
    
            String toFind = "consumes faster than Labor wears; while the used key is always bright,";
            {
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringFindTest(bensAuto, toFind);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferFindTest(new StringBuffer(bensAuto), toFind);
                    if (isTestRope()) 
                        stats2[j] = ropeFindTest(Rope.BUILDER.build(bensAuto_RAW), toFind);
                    if (isTestText()) 
                        stats3[j] = textFindTest(new Text(bensAuto), toFind);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope()) 
                    printStats(System.out, stats2, "ns", "[Rope]");
                if (isTestText()) 
                    printStats(System.out, stats3, "ns", "[Text]");
            }
    
            System.out.printf("%n**** STRING SEARCH TEST (COMPLEXLY-CONSTRUCTED DATASTRUCTURES) ****%n");
            System.out.printf("* Using a complexly constructed rope and the pattern 'Bob was very%n" +
                              "* cheerful with them, and spoke pleasantly to'.%n");
    
            toFind = "Bob was very cheerful with them, and spoke pleasantly to";
            {
                setupComplexRopes();
                long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT], stats3 = new long[ITERATION_COUNT];
                for (int j=0;j<ITERATION_COUNT;++j){
                    if (isTestString())
                        stats0[j] = stringFindTest(complexString, toFind);
                    if (isTestStringBuffer())
                        stats1[j] = stringBufferFindTest(complexStringBuffer, toFind);
                    if (isTestRope()) 
                        stats2[j] = ropeFindTest(complexRope, toFind);
                    if (isTestText()) 
                        stats3[j] = textFindTest(complexText, toFind);
                }
                if (isTestString())
                    printStats(System.out, stats0, "ns", "[String]");
                if (isTestStringBuffer())
                    printStats(System.out, stats1, "ns", "[StringBuffer]");
                if (isTestRope()) 
                    printStats(System.out, stats2, "ns", "[Rope]");
                if (isTestText()) 
                    printStats(System.out, stats3, "ns", "[Text]");
            }
        }

        if (isTestSerialization()) {
            System.out.printf("%n**** WRITE TEST ****%n");
            System.out.printf("* Illustrates how to write a Rope to a stream efficiently.%n");
            setupComplexRopes();
            long[] stats0 = new long[ITERATION_COUNT], stats1 = new long[ITERATION_COUNT], stats2 = new long[ITERATION_COUNT];
            for (int j=0;j<ITERATION_COUNT;++j){
                if (isTestRope()) {
                    stats0[j] = ropeWriteBad(complexRope);
                    stats1[j] = ropeWriteGood(complexRope);
                }
                if (isTestText())
                    stats2[j] = textWrite(complexText);
            }
            if (isTestRope()) {
                printStats(System.out, stats0, "ns", "[Out(Rope)]");
                printStats(System.out, stats1, "ns", "[Rope.write]");    
            }
            if (isTestText())
                printStats(System.out, stats2, "ns", "[Out(Text)]");    
        }
    }
    
    private void setupComplexRopes() {
        if (complexRope == null || complexText == null || complexString == null || complexStringBuffer == null) {
            final int[][] insertPlan = insertTestPlan(aChristmasCarol.length());
            if (complexRope == null)
                ropeInsertTest(Rope.BUILDER.build(aChristmasCarol_RAW), insertPlan, insertPlan.length);
            if (complexText == null)
                textInsertTest(aChristmasCarol, insertPlan, insertPlan.length);
            if (complexString == null)
                stringInsertTest(aChristmasCarol, insertPlan, insertPlan.length);
            if (complexStringBuffer == null)
                stringBufferInsertTest(aChristmasCarol, insertPlan, insertPlan.length);
        }
    }

    
    private boolean isVerbose() {
        return verb;
    }
    
    private boolean isTestText() {
        return testT;
    }
    
    private boolean isTestString() {
        return testS;
    }
    
    private boolean isTestStringBuffer() {
        return testSB;
    }
    
    private boolean isTestRope() {
        return testR;
    }
    
    private boolean isTestSerialization() {
        return testSer;
    }
    
    private boolean isTestMutation() {
        return testMut;
    }
    
    private boolean isTestTraveral() {
        return testTra;
    }

    /**
     * Creates an insert test plane for two strings, one string inserted into the other.
     */
    int[][] createInsertPlan(int originalSize, int insertSize) {
        final int[][] insertPlan2=new int[planLength][3];
        for (int j=0;j<insertPlan2.length;++j) {
            insertPlan2[j][0] = random.nextInt(originalSize);                    //location to insert
            insertPlan2[j][1] = random.nextInt(insertSize);                      //clip from
            insertPlan2[j][2] = random.nextInt(insertSize - insertPlan2[j][1]);  //clip length
        }
        return insertPlan2;
    }

    /**
     * Creates insert test plan for a string at the specified size.
     */
    int[][] insertTestPlan(int len) {
        final int[][] insertPlan=new int[planLength][3];
        for (int j=0;j<insertPlan.length;++j) {
            insertPlan[j][0] = random.nextInt(len);                      //location to insert
            insertPlan[j][1] = random.nextInt(len);                      //clip from
            insertPlan[j][2] = random.nextInt(len - insertPlan[j][1]);   //clip length
        }
        return insertPlan;
    }

    /**
     * Creates prepend/append test plan for a string at the specified size.
     */
    int[][] prependTestPlan(final int size) {
        final int[][] prependPlan=new int[planLength][2];
        for (int j=0;j<prependPlan.length;++j) {
            prependPlan[j][0] = random.nextInt(size);
            prependPlan[j][1] = random.nextInt(size - prependPlan[j][0]);
        }
        return prependPlan;
    }

    /**
     * Creates delete plan for text at the specified size.
     */
    int[][] deleteTestPlan(int size) {
        final int[][] deletePlan=new int[planLength][2];
        for (int j=0;j<deletePlan.length;++j) {
            deletePlan[j][0] = random.nextInt(size);
            deletePlan[j][1] = random.nextInt(Math.min(100, size - deletePlan[j][0]));
            size -= deletePlan[j][1];
        }
        return deletePlan;
    }

    private long stringFindTest(final String text, String toFind) {
        long x,y;

        String b = text;
        x = System.nanoTime();
        int loc = b.indexOf(toFind);
        y = System.nanoTime();
        verbose("[String.find]       indexOf needle length %d found at index %d in % ,18d ns.\n", toFind.length(), loc, (y-x));
        return (y-x);
    }

    /**
     * Loads the text test files into strings and arrays. 
     */
    void loadTestFiles() throws IOException{
//        long x,y;
//        x=System.nanoTime();
        aChristmasCarol_RAW = PerformanceTest.readCC();
        bensAuto_RAW        = PerformanceTest.readBF();
        aChristmasCarol = new String(aChristmasCarol_RAW);
        bensAuto        = new String(bensAuto_RAW);
//        y=System.nanoTime();
//        System.out.println("Read " + aChristmasCarol.length() + " bytes in " + PerformanceTest.time(x,y));
    }
    
    long stringBufferFindTest(final StringBuffer text, String toFind) {
        long x,y;

        x = System.nanoTime();
        int loc = text.indexOf(toFind);
        y = System.nanoTime();
        verbose("[StringBuffer.find] indexOf needle length %d found at index %d in % ,18d ns.\n", toFind.length(), loc, (y-x));
        return (y-x);
    }

    long ropeFindTest(final Rope rope, String toFind) {
        long x,y;

        Rope b = rope;
        x = System.nanoTime();
        int loc = b.indexOf(toFind);
        y = System.nanoTime();
        verbose("[Rope.find]         indexOf needle length %d found at index %d in % ,18d ns.\n", toFind.length(), loc, (y-x));
        return (y-x);
    }

    long ropeWriteGood(Rope complexRope) {
        long x,y;

        Writer out = new StringWriter(complexRope.length());
        x = System.nanoTime();
        try {
            complexRope.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        y = System.nanoTime();
        verbose("[Rope.write]   Executed write in % ,18d ns.\n", (y-x));
        return (y-x);
    }

    long ropeWriteBad(Rope complexRope) {
        long x,y;

        Writer out = new StringWriter(complexRope.length());
        x = System.nanoTime();
        try {
            out.write(complexRope.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        y = System.nanoTime();
        verbose("[Out.write]    Executed write in % ,18d ns.\n", (y-x));
        return (y-x);
    }

    static char[] readBF() throws IOException {
        return readFile("/AutobiographyOfBenjaminFranklin_BenjaminFranklin.txt");
    }

    static char[] readCC() throws IOException {
        return readFile("/AChristmasCarol_CharlesDickens.txt");
    }

    private static char[] readFile(String file) throws UnsupportedEncodingException,
            IOException {
        InputStream is = PerformanceTest.class.getResourceAsStream(file);
        try{
            is = new BufferedInputStream(is, 4096);
            Reader in = new InputStreamReader(is, "ISO-8859-1"); 
    
            final CharArrayWriter out = new CharArrayWriter(4096);
            final char[] c = new char[256];
            int x = -1;
            while (-1 != (x=in.read(c))) {
                out.write(c, 0, x);
            }
            out.close();
            return out.toCharArray();
        }finally{
            is.close();
        }
    }

    long ropeAppendTest(final String aChristmasCarol, final int[][] appendPlan, final int planLength) {
        long x,y;

        x = System.nanoTime();
        Rope result=Rope.BUILDER.build(aChristmasCarol);

        for (int j=0; j<planLength; ++j) {
            final int offset = appendPlan[j][0];
            final int length = appendPlan[j][1];
            result = result.append(result.subSequence(offset, offset+length));
        }
        y = System.nanoTime();
        verbose("[Rope]         Executed append plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
        return (y-x);
    }

    long ropeDeleteTest(final String aChristmasCarol, final int[][] prependPlan) {
        long x,y;

        x = System.nanoTime();
        Rope result=Rope.BUILDER.build(aChristmasCarol);

        for (int j=0; j<prependPlan.length; ++j) {
            final int offset = prependPlan[j][0];
            final int length = prependPlan[j][1];
            result = result.delete(offset, offset + length);
        }
        y = System.nanoTime();
        verbose("[Rope]         Executed delete plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
        return (y-x);
    }
    
    long ropeInsertTest(final Rope rope, final int[][] insertPlan, int planLength) {
        long x,y;
        Rope result=rope;

        x = System.nanoTime();

        for (int j=0; j<planLength; ++j) {
            final int into   = insertPlan[j][0];
            final int offset = insertPlan[j][1];
            final int length = insertPlan[j][2];
            result = result.insert(into, result.subSequence(offset, offset+length));
        }
        y = System.nanoTime();
        verbose("[Rope]         Executed insert plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
        complexRope = result;
        return (y-x);
    }

    long textInsertTest(final String text, final int[][] insertPlan, int planLength) {
        long x,y;
        Text result=new Text(text);

        x = System.nanoTime();

        for (int j=0; j<planLength; ++j) {
            final int into   = insertPlan[j][0];
            final int offset = insertPlan[j][1];
            final int length = insertPlan[j][2];
            result = result.insert(into, result.subtext(offset, offset+length));
        }
        y = System.nanoTime();
        verbose("[Text]         Executed insert plan in % ,18d ns. Result has length: %d.%n", (y-x), result.length());
        complexText = result;
        return (y-x);
    }

    long textInsertTest2(final String aChristmasCarol, final String bensAuto, final int[][] insertPlan) {
        long x,y;

        x = System.nanoTime();
        Text result=new Text(aChristmasCarol);

        for (int j=0; j<insertPlan.length; ++j) {
            final int into   = insertPlan[j][0];
            final int offset = insertPlan[j][1];
            final int length = insertPlan[j][2];
            result = result.insert(into, new Text(bensAuto.substring(offset, offset+length)));
        }
        y = System.nanoTime();
        verbose("[Text]         Executed insert plan in % ,18d ns. Result has length: %d.%n", (y-x), result.length());
        return (y-x);
    }

    long textTraverseTest_1(final Text r) {
        long x,y,result=0;

        x = System.nanoTime();

        for (int j=0; j<r.length(); ++j) result+=r.charAt(j);

        y = System.nanoTime();
        verbose("[Text/charAt]  Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), result);
        return (y-x);
    }

    long textWrite(Text complexRope) {
        long x,y;

        Writer out = new StringWriter(complexRope.length());
        x = System.nanoTime();
        try {
            out.write(complexRope.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        y = System.nanoTime();
        verbose("[Text.write]   Executed write in % ,18d ns.\n", (y-x));
        return (y-x);
    }

    long ropeInsertTest2(final String aChristmasCarol, final String bensAuto, final int[][] insertPlan) {
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
        verbose("[Rope]         Executed insert plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
        return (y-x);
    }

    long ropePrependTest(final String aChristmasCarol, final int[][] prependPlan, int planLength) {
        long x,y;

        x = System.nanoTime();
        Rope result=Rope.BUILDER.build(aChristmasCarol);

        for (int j=0; j<planLength; ++j) {
            final int offset = prependPlan[j][0];
            final int length = prependPlan[j][1];
            result = result.subSequence(offset, offset+length).append(result);
        }
        y = System.nanoTime();
        verbose("[Rope]         Executed prepend plan in % ,18d ns. Result has length: %d. Rope Depth: %d\n", (y-x), result.length(), ((AbstractRope)result).depth());
        return (y-x);
    }

    long textPrependTest(final String aChristmasCarol, final int[][] prependPlan, int planLength) {
        long x,y;

        x = System.nanoTime();
        Text result=new Text(aChristmasCarol);

        for (int j=0; j<planLength; ++j) {
            final int offset = prependPlan[j][0];
            final int length = prependPlan[j][1];
            result = result.subtext(offset, offset+length).concat(result);
        }
        y = System.nanoTime();
        verbose("[Text]         Executed prepend plan in % ,18d ns. Result has length: %d.\n", (y-x), result.length());
        return (y-x);
    }

    long textAppendTest(final String aChristmasCarol, final int[][] appendPlan, int planLength) {
        long x,y;

        x = System.nanoTime();
        Text result=new Text(aChristmasCarol);

        for (int j=0; j<planLength; ++j) {
            final int offset = appendPlan[j][0];
            final int length = appendPlan[j][1];
            result = result.concat(result.subtext(offset, offset+length));
        }
        y = System.nanoTime();
        verbose("[Text]         Executed append plan in % ,18d ns. Result has length: %d.%n", (y-x), result.length());
        return (y-x);
    }

    long textDeleteTest(final String aChristmasCarol, final int[][] prependPlan) {
        long x,y;

        x = System.nanoTime();
        Text result = new Text(aChristmasCarol);

        for (int j=0; j<prependPlan.length; ++j) {
            final int offset = prependPlan[j][0];
            final int length = prependPlan[j][1];
            result = result.delete(offset, offset + length);
        }
        y = System.nanoTime();
        verbose("[Text]         Executed delete plan in % ,18d ns. Result has length: %d.%n", (y-x), result.length());
        return (y-x);
    }

    long ropeTraverseTest_1(final Rope r) {
        long x,y,result=0;

        x = System.nanoTime();

        for (int j=0; j<r.length(); ++j) result+=r.charAt(j);

        y = System.nanoTime();
        verbose("[Rope/charAt]  Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), result);
        return (y-x);
    }

    long ropeTraverseTest_2(final Rope r) {
        long x,y,result=0;
        
        x = System.nanoTime();

        for (final char c: r) result+=c;

        y = System.nanoTime();
        verbose("[Rope/itr]     Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), result);
        return (y-x);
    }

    long ropeTraverseTest2_1(Rope aChristmasCarol) {
        long x,y;

        Rope result=aChristmasCarol;
        
        int r=0;
        x = System.nanoTime();
        for (int j=0; j<result.length(); ++j) r+=result.charAt(j);
        y = System.nanoTime();
        verbose("[Rope/charAt]  Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), r);
        return (y-x);
    }

    long textTraverseTest2(Text aChristmasCarol) {
        long x,y;

        Text result=aChristmasCarol;
        
        int r=0;
        x = System.nanoTime();
        for (int j=0; j<result.length(); ++j) r+=result.charAt(j);
        y = System.nanoTime();
        verbose("[Text/charAt]  Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), r);
        return (y-x);
    }

    long ropeTraverseTest2_2(Rope aChristmasCarol) {
        long x,y;

        Rope result=aChristmasCarol;
        
        int r=0;
        x = System.nanoTime();
        for (final char c: result) r+=c;
        y = System.nanoTime();
        verbose("[Rope/itr]     Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), r);
        return (y-x);
    }

    long stringAppendTest(final String aChristmasCarol, final int[][] appendPlan, final int planLength) {
        long x,y;

        x = System.nanoTime();
        String result=aChristmasCarol;

        for (int j=0; j<planLength; ++j) {
            final int offset = appendPlan[j][0];
            final int length = appendPlan[j][1];
            result = result.concat(result.substring(offset, offset + length));
        }
        y = System.nanoTime();
        verbose("[String]       Executed append plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        return (y-x);
    }

    long stringBufferAppendTest(final String aChristmasCarol, final int[][] appendPlan, final int planLength) {
        long x,y;

        x = System.nanoTime();
        final StringBuffer result=new StringBuffer(aChristmasCarol);

        for (int j=0; j<planLength; ++j) {
            final int offset = appendPlan[j][0];
            final int length = appendPlan[j][1];
            result.append(result.subSequence(offset, offset+length));
        }
        y = System.nanoTime();
        verbose("[StringBuffer] Executed append plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        return (y-x);
    }

    long stringBufferDeleteTest(final String aChristmasCarol, final int[][] prependPlan) {
        long x,y;

        x = System.nanoTime();
        final StringBuffer result=new StringBuffer(aChristmasCarol);

        for (int j=0; j<prependPlan.length; ++j) {
            final int offset = prependPlan[j][0];
            final int length = prependPlan[j][1];
            result.delete(offset, offset+length);
        }
        y = System.nanoTime();
        verbose("[StringBuffer] Executed delete plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        return (y-x);
    }

    long stringBufferInsertTest(final String text, final int[][] insertPlan, int planLength) {
        long x,y;
        final StringBuffer result=new StringBuffer(text);

        x = System.nanoTime();

        for (int j=0; j<planLength; ++j) {
            final int into   = insertPlan[j][0];
            final int offset = insertPlan[j][1];
            final int length = insertPlan[j][2];
            result.insert(into, result.subSequence(offset, offset+length));
        }
        y = System.nanoTime();
        verbose("[StringBuffer] Executed insert plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        complexStringBuffer = result;
        return (y-x);
    }

    long stringBufferInsertTest2(final String aChristmasCarol, final String bensAuto, final int[][] insertPlan) {
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
        verbose("[StringBuffer] Executed insert plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        return (y-x);
    }

    long stringBufferPrependTest(final String aChristmasCarol, final int[][] prependPlan, int planLength) {
        long x,y;

        x = System.nanoTime();
        final StringBuffer result=new StringBuffer(aChristmasCarol);

        for (int j=0; j<planLength; ++j) {
            final int offset = prependPlan[j][0];
            final int length = prependPlan[j][1];
            result.insert(0, result.subSequence(offset, offset+length));
        }
        y = System.nanoTime();
        verbose("[StringBuffer] Executed prepend plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        return (y-x);
    }

    long stringBufferTraverseTest(final String text) {
        long x,y,result=0;
        final StringBuffer b=new StringBuffer(text);

        x = System.nanoTime();

        for (int j=0; j<b.length(); ++j) result+=b.charAt(j);

        y = System.nanoTime();
        verbose("[StringBuffer] Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), result);
        return (y-x);

    }

    long stringBufferTraverseTest2(final StringBuffer aChristmasCarol) {
        long x,y;

        final StringBuffer result=aChristmasCarol;
        
        int r=0;
        x = System.nanoTime();
        for (int j=0; j<result.length(); ++j) r+=result.charAt(j);
        y = System.nanoTime();
        verbose("[StringBuffer] Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), r);
        return (y-x);
    }

    long stringDeleteTest(final String aChristmasCarol, final int[][] prependPlan) {
        long x,y;

        x = System.nanoTime();
        String result=aChristmasCarol;

        for (int j=0; j<prependPlan.length; ++j) {
            final int offset = prependPlan[j][0];
            final int length = prependPlan[j][1];
            result = result.substring(0, offset).concat(result.substring(offset+length));
        }
        y = System.nanoTime();
        verbose("[String]       Executed delete plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        return (y-x);
    }

    long stringInsertTest(final String text, final int[][] insertPlan, int planLength) {
        long x,y;
        String result=text;

        x = System.nanoTime();

        for (int j=0; j<planLength; ++j) {
            final int into   = insertPlan[j][0];
            final int offset = insertPlan[j][1];
            final int length = insertPlan[j][2];
            result = result.substring(0, into).concat(result.substring(offset, offset + length)).concat(result.substring(into));
            
        }
        y = System.nanoTime();
        verbose("[String]       Executed insert plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        complexString = result;
        return (y-x);
    }

    long stringInsertTest2(final String aChristmasCarol, final String bensAuto, final int[][] insertPlan) {
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
        verbose("[String]       Executed insert plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        return (y-x);
    }

    long stringPrependTest(final String aChristmasCarol, final int[][] prependPlan, int planLength) {
        long x,y;

        x = System.nanoTime();
        String result=aChristmasCarol;

        for (int j=0; j<planLength; ++j) {
            final int offset = prependPlan[j][0];
            final int length = prependPlan[j][1];
            result = result.substring(offset, offset + length).concat(result);
        }
        y = System.nanoTime();
        verbose("[String]       Executed prepend plan in % ,18d ns. Result has length: %d\n", (y-x), result.length());
        return (y-x);
    }

    long stringTraverseTest(final String text) {
        long x,y,result=0;
        String s = text;

        x = System.nanoTime();

        for (int j=0; j<s.length(); ++j) result+=s.charAt(j);

        y = System.nanoTime();
        verbose("[String]       Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), result);
        return (y-x);
    }

    long stringTraverseTest2(final String aChristmasCarol) {
        long x,y;

        String result=aChristmasCarol;

        int r=0;
        x = System.nanoTime();
        for (int j=0; j<result.length(); ++j) r+=result.charAt(j);
        y = System.nanoTime();
        verbose("[String]       Executed traversal in % ,18d ns. Result checksum: %d\n", (y-x), r);
        return (y-x);
    }

    long stringRegexpTest(final String text, Pattern pattern) {
        long x,y;
        String s = text;

        x = System.nanoTime();

        int result = 0;
        Matcher m = pattern.matcher(s);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[String]       Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    long textRegexpTest(final Text text, Pattern pattern) {
        long x,y;

        x = System.nanoTime();

        int result = 0;
        Matcher m = pattern.matcher(text);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[Text]         Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    long stringBufferRegexpTest(final String text, Pattern pattern) {
        long x,y;
        StringBuffer buffer = new StringBuffer(text);

        x = System.nanoTime();

        int result = 0;
        Matcher m = pattern.matcher(buffer);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[StringBuffer] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    long ropeRegexpTest(Rope rope, Pattern pattern) {
        long x,y;

        x = System.nanoTime();

        int result = 0;
        Matcher m = pattern.matcher(rope);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[Rope]         Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    long ropeMatcherRegexpTest(Rope rope, Pattern pattern) {
        long x,y;

        x = System.nanoTime();

        int result = 0;
        Matcher m = rope.matcher(pattern); 
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[Rope.matcher] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    long stringRegexpTest2(final String aChristmasCarol, Pattern pattern) {
        long x,y;

        x = System.nanoTime();

        int result = 0;
        Matcher m = pattern.matcher(aChristmasCarol);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        System.out.printf("[String]       Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }
    
    long textRegexpTest2(final Text aChristmasCarol, Pattern pattern) {
        long x,y;

        x = System.nanoTime();

        int result = 0;
        Matcher m = pattern.matcher(aChristmasCarol);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[Text]         Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    long textFindTest(final Text text, String toFind) {
        long x,y;

        Text b = text;
        x = System.nanoTime();
        int loc = b.indexOf(toFind);
        y = System.nanoTime();
        verbose("[Text.find]         indexOf needle length %d found at index %d in % ,18d ns.\n", toFind.length(), loc, (y-x));
        return (y-x);
    }

    long stringBufferRegexpTest2(final StringBuffer aChristmasCarol, Pattern pattern) {
        long x,y;

        x = System.nanoTime();

        int result = 0;
        Matcher m = pattern.matcher(aChristmasCarol);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[StringBuffer] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    long ropeRegexpTest2(final Rope aChristmasCarol, Pattern pattern) {
        long x,y;

        x = System.nanoTime();

        int result = 0;
        Matcher m = pattern.matcher(aChristmasCarol);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[Rope]         Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    long ropeRebalancedRegexpTest2(final Rope aChristmasCarol, Pattern pattern) {
        long x,y;

        x = System.nanoTime();

        CharSequence adaptedRope = aChristmasCarol.rebalance(); //Rope.BUILDER.buildForRegexpSearching(aChristmasCarol);
        int result = 0;
        Matcher m = pattern.matcher(adaptedRope);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[Reblncd Rope] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    long ropeMatcherRegexpTest2(final Rope aChristmasCarol, Pattern pattern) {
        long x,y;

        x = System.nanoTime();
        
        int result = 0;
        Matcher m = aChristmasCarol.matcher(pattern);
        while (m.find()) ++result;
        
        y = System.nanoTime();
        verbose("[Rope.matcher] Executed regexp test in % ,18d ns. Found %d matches.\n", (y-x), result);
        return (y-x);
    }

    String time(final long x, final long y) {
        return (y-x) + "ns";
    }
    
    /**
     * Logs stats, and returns two numbers: [Average,Median]
     */
    static void printStats(PrintStream out, long[] samples, String unit, String prefix) {
        double [] stats = stats(samples);
        out.printf("%-14s Average=% ,16.2f %s Median=% ,16.0f %s Stdev = % ,16.2f %s\n", prefix, stats[0], unit, stats[1], unit, stats[2], unit);
    }

    static double[] stats(long[] stats) {
        if (stats.length < 3) 
            System.err.println("Cannot print stats.");
        Arrays.sort(stats);
        
        double median = ((stats.length & 1) == 1 ? stats[stats.length >> 1]: (stats[stats.length >> 1] + stats[1 + (stats.length >> 1)]) / 2);
        double average = 0;
        for (int j=1;j<stats.length-1;++j) {
            average += stats[j];
        }
        average /= stats.length - 2;
        double stddev = 0;
        for (int j=1; j<stats.length-1;++j) {
            double delta = stats[j]-average;
            stddev += delta * delta;
            
        }
        stddev /= stats.length - 2;
        stddev = Math.sqrt(stddev);
        return new double[]{average, median, stddev};
    }

    private void verbose(String message, Object ... substitutions) {
        if (isVerbose())
            System.out.printf(message, substitutions);
    }
}
