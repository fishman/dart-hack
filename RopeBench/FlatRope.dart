#library('Rope');

#import('Rope.dart');

interface FlatRope extends Rope {
  
  /**
   * Returns a <code>String</code> representation of a range
   * in this rope.
   * @param offset the offset.
   * @param length the length.
   * @return a <code>String</code> representation of a range
   * in this rope.
   */
  String toString([int offset, int length]);
}
