/*
 *  RopeUtilities.java
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

import java.io.PrintStream;
import java.util.ArrayDeque;

import org.ahmadsoft.ropes.Rope;

/**
 * Contains utlities for manipulating ropes.
 * @author aahmad
 */
class RopeUtilities {

	private static final long[] FIBONACCI = { 0l, 1l, 1l, 2l, 3l, 5l, 8l, 13l, 21l, 34l, 55l, 89l, 144l, 233l, 377l, 610l, 987l, 1597l, 2584l, 4181l, 6765l, 10946l, 17711l, 28657l, 46368l, 75025l, 121393l, 196418l, 317811l, 514229l, 832040l, 1346269l, 2178309l, 3524578l, 5702887l, 9227465l, 14930352l, 24157817l, 39088169l, 63245986l, 102334155l, 165580141l, 267914296l, 433494437l, 701408733l, 1134903170l, 1836311903l, 2971215073l, 4807526976l, 7778742049l, 12586269025l, 20365011074l, 32951280099l, 53316291173l, 86267571272l, 139583862445l, 225851433717l, 365435296162l, 591286729879l, 956722026041l, 1548008755920l, 2504730781961l, 4052739537881l, 6557470319842l, 10610209857723l, 17167680177565l, 27777890035288l, 44945570212853l, 72723460248141l, 117669030460994l, 190392490709135l, 308061521170129l, 498454011879264l, 806515533049393l, 1304969544928657l, 2111485077978050l, 3416454622906707l, 5527939700884757l, 8944394323791464l, 14472334024676221l, 23416728348467685l, 37889062373143906l, 61305790721611591l, 99194853094755497l, 160500643816367088l, 259695496911122585l, 420196140727489673l, 679891637638612258l, 1100087778366101931l, 1779979416004714189l, 2880067194370816120l, 4660046610375530309l, 7540113804746346429l};
	private static final short MAX_ROPE_DEPTH = 96;
	private static final String SPACES = "                                                                                                    ";
	public static RopeUtilities INSTANCE = new RopeUtilities();

	/**
	 * Rope rebalancing implementation.
	 * @param r
	 * @return
	 */
	public Rope rebalance(final Rope r) {
		final Rope[] ropes = new Rope[RopeUtilities.FIBONACCI.length];
	
		final ArrayDeque<Rope> toExamine = new ArrayDeque<Rope>();
		// begin a depth first loop.
		toExamine.add(r);
		while (toExamine.size() > 0) {
			final Rope x = toExamine.pop();
			if (x instanceof ConcatenationRope) {
				toExamine.push(((ConcatenationRope) x).getRight());
				toExamine.push(((ConcatenationRope) x).getLeft());
				continue;
			}
			if (x instanceof FlatRope || x instanceof SubstringRope) {
				final int l = x.length();
				int pos;
				boolean lowerSlotsEmpty=true;
				for (pos=2; pos<RopeUtilities.FIBONACCI.length-1; ++pos) {
					if (ropes[pos] != null)
						lowerSlotsEmpty = false;
					if (RopeUtilities.FIBONACCI[pos] <= l && l < RopeUtilities.FIBONACCI[pos+1])  // l is in [F(pos), F(pos+1))
						break;
				}
				if (lowerSlotsEmpty) {
					ropes[pos] = x;
				} else {
					Rope rebalanced = null;
					for (int j=2; j<=pos; ++j) {
						if (ropes[j] != null) {
							if (rebalanced == null)
								rebalanced=ropes[j];
							else
								rebalanced=ropes[j].append(rebalanced);
							ropes[j] = null;
						}
					}
					rebalanced = rebalanced.append(x);
					for (int j=pos; j<RopeUtilities.FIBONACCI.length-1; ++j) {
						if (ropes[j] != null) {
							rebalanced = ropes[j].append(rebalanced);
							ropes[j] = null;
						}
						if (RopeUtilities.FIBONACCI[j] <= rebalanced.length() && rebalanced.length() < RopeUtilities.FIBONACCI[j+1]) {
							ropes[j] = rebalanced;
							break;
						}
					}
				}
			}
		}
	
		// perform the final concatenation
		Rope result = null;
		for (int j=2; j<RopeUtilities.FIBONACCI.length; ++j) {
			if (ropes[j] != null) {
				result = (result == null) ? ropes[j]: ropes[j].append(result);
			}
		}
		return result;
	}

	/**
	 * Concatenate two ropes. Implements all recommended optimizations in "Ropes: an
	 * Alternative to Strings".
	 * @param left the first rope.
	 * @param right the second rope.
	 * @return the concatenation of the specified ropes.
	 */
	Rope concatenate(final Rope left, final Rope right) {
		if (left instanceof FlatRope && right instanceof FlatRope) {
			final FlatRope fLeft  = (FlatRope) left;
			final FlatRope fRight = (FlatRope) right;
			if (fLeft.length() + fRight.length() < 16) {
				return new FlatRope(fLeft.toString() + fRight.toString());
			}
		}
		if (left instanceof ConcatenationRope && right instanceof FlatRope) {
			final ConcatenationRope cLeft  = (ConcatenationRope) left;
			final FlatRope fRight = (FlatRope) right;
	
			if (cLeft.getRight() instanceof FlatRope) {
				final FlatRope fLeftRight = (FlatRope) cLeft.getRight();
				if (fLeftRight.length() + fRight.length() < 16) {
					return this.autoRebalance(new ConcatenationRope(cLeft.getLeft(), new FlatRope(fLeftRight.toString() + fRight.toString())));
				}
			}
		}
		if (left instanceof FlatRope && right instanceof ConcatenationRope) {
			final FlatRope fLeft = (FlatRope) left;
			final ConcatenationRope cRight  = (ConcatenationRope) right;
	
			if (cRight.getLeft() instanceof FlatRope) {
				final FlatRope cRightLeft = (FlatRope) cRight.getLeft();
				if (fLeft.length() + cRightLeft.length() < 16) {
					return this.autoRebalance(new ConcatenationRope(new FlatRope(fLeft.toString() + cRightLeft.toString()), cRight.getRight()));
				}
			}
		}
		return this.autoRebalance(new ConcatenationRope(left, right));
	}

	/**
	 * Returns the depth of the specified rope.
	 * @param r the rope.
	 * @return the depth of the specified rope.
	 */
	byte depth(final Rope r) {
		if (r instanceof AbstractRope) {
			return ((AbstractRope)r).depth();
		} else {
			throw new IllegalArgumentException("Bad rope");
		}
	}

	boolean isBalanced(final Rope r) {
		final byte depth = this.depth(r);
		if (depth >= RopeUtilities.FIBONACCI.length - 2)
			return false;
		return (RopeUtilities.FIBONACCI[depth + 2] <= r.length());
	}

	/**
	 * Rebalance a rope if the depth has exceeded MAX_ROPE_DEPTH. If the
	 * rope depth is less than MAX_ROPE_DEPTH or if the rope is of unknown
	 * type, no rebalancing will occur.
	 * @param r the rope to rebalance.
	 * @return a rebalanced copy of the specified rope.
	 */
	public Rope autoRebalance(final Rope r) {
		if (r instanceof AbstractRope && ((AbstractRope) r).depth() > RopeUtilities.MAX_ROPE_DEPTH) {
			return this.rebalance(r);
		} else {
			return r;
		}
	}

	/**
	 * Visualize a rope.
	 * @param r
	 * @param out
	 */
	void visualize(final Rope r, final PrintStream out) {
		this.visualize(r, out, (byte) 0);
	}

	private void visualize(final Rope r, final PrintStream out, final int depth) {
		if (r instanceof FlatRope) {
			out.print(RopeUtilities.SPACES.substring(0,depth*2));
			out.println("\"" + r + "\"");
		}
		if (r instanceof SubstringRope) {
			out.print(RopeUtilities.SPACES.substring(0,depth*2));
			out.println("substring");
			this.visualize(((SubstringRope)r).getRope(), out, depth+1);
		}
		if (r instanceof ConcatenationRope) {
			out.print(RopeUtilities.SPACES.substring(0,depth*2));
			out.println("concat[left]");
			this.visualize(((ConcatenationRope)r).getLeft(), out, depth+1);
			out.print(RopeUtilities.SPACES.substring(0,depth*2));
			out.println("concat[right]");
			this.visualize(((ConcatenationRope)r).getRight(), out, depth+1);
		}
	}

}
