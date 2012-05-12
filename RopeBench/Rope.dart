#library('Rope');

#import('RopeBuilder.dart');
  
  
interface Rope {
  
  /**
   * A factory used for constructing ropes.
   */
  RopeBuilder BUILDER;
  
  /**
   * Returns a new rope created by appending the specified character range to
   * this rope.
   * @param csq the specified character.
   * @param start the start index, inclusive.
   * @param end the end index, non-inclusive.
   * @return a new rope.
   */
    //@ requires start <= end && start > -1 && end <= csq.length();
  //@ ensures \result.length() == (length() + (end-start));
  Rope append(String str, [int start, int end]);
  
  
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
   *       greater than <code>end</code>.
   */
  //@ requires start <= end && start > -1 && end <= length();
  //@ ensures \result.length() == (length() - (end-start));
  Rope delete(int start, [int end]);
  

  /**
   * Returns the index within this rope of the first occurrence of the
   * specified character, beginning at the specified index. If a character
   * with value <code>ch</code> occurs in the character sequence
   * represented by this <code>Rope</code> object, then the index of the
   * first such occurrence is returned&#8212;that is, the smallest value k
   * such that:
   * <p>
   * <code>this.charAt(k) == ch</code>
   * <p>
   * is <code>true</code>. If no such character occurs in this string, then
   * <code>-1</code> is returned.
   * @param ch a character.
   * @param fromIndex the index to start searching from.
   * @return the index of the first occurrence of the character in the character
   * sequence represented by this object, or -1 if the character does not occur.
   */
  //@ requires fromIndex > -1 && fromIndex < length();
    //@ ensures \result >= -1 && \result < length();
  int indexOf(String str, [int fromIndex]);
 
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
  //@ requires dstOffset > -1 && dstOffset <= length();
  Rope insert(int dstOffset, String str); 
  
  /**
   * Returns an iterator positioned to start at the specified index.
   * @param start the start position.
   * @return an iterator positioned to start at the specified index.
   */
  //@ requires start > -1 && start < length();
  Iterator<String> iterator(int start);
  
  /**
   * Trims all whitespace as well as characters less than 0x20 from
   * the beginning of this string.
   * @return a rope with all leading whitespace trimmed.
   */
  //@ ensures \result.length() <= length();
  Rope trimStart();
  
  /**
   * Returns <code>true</code> if this rope matches the specified
   * <code>Pattern</code>, or <code>false</code> otherwise.
   * @see java.util.regex.Pattern
   * @param regex the specified regular expression (Pattern or String type).
   * @return <code>true</code> if this rope matches the specified
   * <code>Pattern</code>, or <code>false</code> otherwise.
   */
  bool matches(var regex);
   
  /**
   * Rebalances the current rope, returning the rebalanced rope. In general,
   * rope rebalancing is handled automatically, but this method is provided
   * to give users more control.
   *
   * @return a rebalanced rope.
   */
  Rope rebalance();

  
  /**
   * Reverses this rope.
   * @return a reversed copy of this rope.
   */
  Rope reverse();
  
  /**
   * Returns a reverse iterator positioned to start at the specified index.
   * A reverse iterator moves backwards instead of forwards through a rope.
   * @param start the start position.
   * @return a reverse iterator positioned to start at the specified index from
   * the end of the rope. For example, a value of 1 indicates the iterator 
   * should start 1 character before the end of the rope.
   * @see Rope#reverseIterator()
   */
  Iterator<String> reverseIterator([int start]);
  
  /**
   * Trims all whitespace as well as characters less than <code>0x20</code> from
   * the end of this rope.
   * @return a rope with all trailing whitespace trimmed.
   */
  //@ ensures \result.length() <= length(); 
  Rope trimEnd();

  Rope subSequence(int start, int end);

  /**
   * Trims all whitespace as well as characters less than <code>0x20</code> from
   * the beginning and end of this string.
   * @return a rope with all leading and trailing whitespace trimmed.
   */
  Rope trim();
  
  
  /**
   * Increase the length of this rope to the specified length by repeatedly 
   * prepending the specified character to this rope. If the specified length 
   * is less than or equal to the current length of the rope, the rope is 
   * returned unmodified.
   * @param toLength the desired length.
   * @param padChar the character to use for padding.
   * @return the padded rope.
   * @see #padStart(int, char)
   */
  Rope padStart(int toLength, [String padChar]);
  
  /**
   * Increase the length of this rope to the specified length by repeatedly 
   * appending the specified character to this rope. If the specified length 
   * is less than or equal to the current length of the rope, the rope is 
   * returned unmodified.
   * @param toLength the desired length.
   * @param padChar the character to use for padding.
   * @return the padded rope.
   * @see #padStart(int, char)
   */
  Rope padEnd(int toLength, [String padChar]);
  
  /**
   * Returns true if and only if the length of this rope is zero.
   * @return <code>true</code> if and only if the length of this
   * rope is zero, and <code>false</code> otherwise.
   */
  bool isEmpty();
  
  /**
   * Returns <code>true</code> if this rope, beginning from a specified
   * offset, starts with the specified prefix.
   * @param prefix the prefix to test.
   * @param offset the start offset.
   * @return <code>true</code> if this rope starts with the 
   * specified prefix and <code>false</code> otherwise.
   */
  bool startsWith(String prefix, [int offset]);
  
  /**
   * Returns <code>true</code> if this rope, terminated at a specified
   * offset, ends with the specified suffix.
   * @param suffix the suffix to test.
   * @param offset the termination offset, counted from the end of the 
   * rope. 
   * @return <code>true</code> if this rope starts with the 
   * specified prefix and <code>false</code> otherwise.
   */
  bool endsWith(String suffix, [int offset]);
}
