// join strings
// create a substring for substringropes
// benchmark you run  it per seconds. so loop see how many iterations per 2 seconds

Rope build(final sequence){
  if (sequence is Rope)
    return sequence;
  return new FlatRope(sequence);
}

/**
 * Rope utilities
 */
final FIBONACCI = const [ 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169, 63245986, 102334155, 165580141, 267914296, 433494437, 701408733, 1134903170, 1836311903, 2971215073, 4807526976, 7778742049, 12586269025, 20365011074, 32951280099, 53316291173, 86267571272, 139583862445, 225851433717, 365435296162, 591286729879, 956722026041, 1548008755920, 2504730781961, 4052739537881, 6557470319842, 10610209857723, 17167680177565, 27777890035288, 44945570212853, 72723460248141, 117669030460994, 190392490709135, 308061521170129, 498454011879264, 806515533049393, 1304969544928657, 2111485077978050, 3416454622906707, 5527939700884757, 8944394323791464, 14472334024676221, 23416728348467685, 37889062373143906, 61305790721611591, 99194853094755497, 160500643816367088, 259695496911122585, 420196140727489673, 679891637638612258, 1100087778366101931, 1779979416004714189, 2880067194370816120, 4660046610375530309, 7540113804746346429];
final int max_depth = 96;
final String spaces = "                                                                                                    ";

Rope Rebalance(final Rope r) {
  // TODO:
  return r;
}

Rope concatenate(final Rope left, final Rope right) {
  // TODO:
  if (left is FlatRope && right is FlatRope) {
    final FlatRope fLeft = left;
    final FlatRope fRight = right;
    // TODO: we need to check how big a string should be for it to be feasible
    // to be turned into a rope
    if (fLeft.length + fRight.length < 16) {
      return new FlatRope(fLeft.toString() + fRight.toString());
    }
  }
  if (left is ConcatenationRope && right is FlatRope) {
    final ConcatenationRope cLeft = left;
    final FlatRope fRight = right;

    if (cLeft.right is FlatRope) {
      final FlatRope fLeftRight = cLeft.right;
      if (fLeftRight.length + fRight.length < 16) {
        return autoRebalance(new ConcatenationRope(cLeft.left, new FlatRope(fLeftRight.toString() + fRight.toString())));
      }
    }
  }
  if (left is FlatRope && right is ConcatenationRope) {
    final FlatRope fLeft = left;
    final ConcatenationRope cRight  = right;

    if (cRight.left is FlatRope) {
      final FlatRope cRightLeft = cRight.left;
      if (fLeft.length + cRightLeft.length < 16) {
        return autoRebalance(new ConcatenationRope(new FlatRope(fLeft.toString() + cRightLeft.toString()), cRight.right));
      }
    }
  }
  return autoRebalance(new ConcatenationRope(left, right));

}

int Depth(final Rope r) {
  if (r is Rope) {
    return r.depth();
  } else {
    throw new IllegalArgumentException("Bad rope");
  }
}
  
bool isBalanced(final Rope r) {
  final int dep = Depth(r);
  if (dep >= FIBONACCI.length - 2)
    return false;
  return FIBONACCI[dep +2] <= r.length;
}

Rope autoRebalance(final Rope r) {
  if (r is Rope && r.depth() > max_depth) {
    return Rebalance(r);
  } else {
    return r;
  }
}

visualize(final Rope r) {
  visualizeDepth(r, 0);
}

visualizeDepth(final Rope r, final int depth) {
  if (r is FlatRope) {
    // TODO:
    print("hello");
  }
  if (r is SubstringRope) {
    // TODO:
    print("hello");
  }
  if (r is ConcatenationRope) {
    // TODO:
    print("hello");
  }
}

interface Rope {// extends Iterable<Rope> {
  Rope append(String c);
  
  Rope appendSubstr(String c, int start, int end);
  
  Rope appendRope(Rope r);
  
  Rope delete(int start, int end);

  int indexOf(String ch);
  
  Rope insert(int dstOffset, String str);
  
  int get length();
  
  int charCodeAt(int index);
  // TODO: Iterator<String> iterator(int start)
  
  int depth();
  
  int hashCode();
  
  Rope rebalance();
  
  // TODO: implement charcodes();
//  List<int> charCodes();
  
  // TODO: write(Writer out)
  
  Rope subSequence(int start, int end);
  
  // TODO: matcher
}

class AbstractRope implements Rope {
  int _hashCode = 0;
  int _length;

  int get length() => _length;
  
  abstract int charCodeAt(int index);
  
  Rope append(String c) {
    return concatenate(this, build(c));
  }
  
  Rope appendSubstr(String c, final int start, final int end) {
    return concatenate(this, build(c).subSequence(start, end));
  }
  
  Rope appendRope(Rope r) {
    return concatenate(this, r);
  }
  
  Rope delete(final int start, final int end){
    if (start == end)
      return this;
    return this.subSequence(0, start).appendRope(this.subSequence(end, this.length));
  }
  
  
  // TODO: add comparison later
  int hashCode() {
    if (this._hashCode == 0 && length > 0) {
      List<int> cc = this.charCodes();

      if (this is FlatRope) {
//        this._hashCode = this.
      } else if (this.length < 6) {
        //TODO: fix hashCode generation
//        for (int j = 0; j < length ; ++j) {
//          this._hashCode = 31 * this._hashCode + cc[charCodes[i]];
//        }
        // TODO: check if charcodes list is an efficient way to generate the hash
       // for ()
// TODO:       for (final char c: this)
//          this._hashCode = 31 * this._hashCode + c;
      } else {
        // TODO: final Iterator<Character> i = this.iterator();
        // for (int j=0;j<5; ++j)
         //  this._hashCode = 31 * this._hashCode + i.next();
        this._hashCode = 31 * this._hashCode + this.charCodeAt(this.length - 1);
        
      }
    }
    return this._hashCode;
  }
  
  bool equals(final Object other){
    if (other is Rope) {
      final Rope rope = other;
      //TODO: lazy comparison, no hash compare if (rope.hashCode() != this.hashCode() || rope.length != this.length)
      if (rope.length != this.length)
        return false;
    
      for (int j = 0; j<this.length; ++j){
        int a = this.charCodeAt(j);
        int b = rope.charCodeAt(j);
        
        if (a != b)
          return false;
      }
      return true;
    }
    return false;
  }
  
  int indexOf(final String ch) {
    int index = -1;

    
// TODO:   for (final char c: this) {
//      ++index;
//      if (c == ch)
//        return index;
//    }
    return -1;

  }
  
  Rope insert(final int dstOffset, final String s) {
    final Rope r = (s == null) ? build("null") : build(s);
    
    if (dstOffset == 0)
      return r.appendRope(this);
    else if (dstOffset == this.length)
      return this.appendRope(r);
    else if (dstOffset < 0 || dstOffset > this.length)
      throw new IndexOutOfRangeException(this.length);
    return this.subSequence(0, dstOffset).appendRope(r).appendRope(this.subSequence(dstOffset, this.length));
  }
  
  //TODO: Iterator<Character> iterator() { return this.iterator(0); }
  
  int compareTo(String sequence) {
    int compareTill = Math.min(sequence.length, length);
    
    List<int> cc = sequence.charCodes();
    for (int j=0; j<compareTill; ++j) {
      int x = this.charCodeAt(j);
      int y = cc[j];
      if (x != y)
        return x - y;
    }
    return length - sequence.length;
    
//TODO:    Iterator<Character> i = iterator();
//    for (int j=0; j<compareTill; ++j) {
//      char x = i.next();
//      char y = sequence.charAt(j);
//      if (x != y)
//        return x - y;
//    }
//    return length() - sequence.length();

  }
  
  String toString() {
// TODO:   StringWriter out = new StringWriter(length());
//    try {
//      write(out);
//      out.close();
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//    return out.toString();
//  
  }
}

class FlatRope extends AbstractRope {
  final String _sequence;
  
  // constructor
  FlatRope(this._sequence);
  
  String charAt(final int index) {
    return this._sequence[index];
  }

  int charCodeAt(int index) {
    return this._sequence.charCodeAt(index);
  }

  int depth() {
    return 0;
  }
  
  // TODO: iterator
//  public Iterator<Character> iterator(final int start) {
//    if (start < 0 || start >= length())
//      throw new IndexOutOfBoundsException("Rope index out of range: " + start);
//    return new Iterator<Character>() {
//      int current = start;
//      @Override
//      public boolean hasNext() {
//        return this.current < FlatRope.this.length();
//      }
//
//      @Override
//      public Character next() {
//        return FlatRope.this.sequence.charAt(this.current++);
//      }
//
//      @Override
//      public void remove() {
//        throw new UnsupportedOperationException("Rope iterator is read-only.");
//      }
//    };
//  }
  int get length() {
    this._sequence.length;
  }
  
  Rope subSequence(final int start, final int end) {
    if (end - start < 8 || end - start >= 0) {
      return new FlatRope(this._sequence.substring(start, end));
    } else {
      return new SubstringRope(this, start, end-start);
    }
  }
  
  String toString() {
    return this._sequence.toString();
  }
  
  Rope rebalance() {
    return this;
  }
  
  // TODO: write
  // TODO: matcher

}

class SubstringRope extends AbstractRope {
  final FlatRope _rope;
  final int _offset;
  final int _length;
  int _depth;
  
  SubstringRope(FlatRope this._rope, int this._offset, int this._length) {
    if (this._length < 0 || this._offset < 0 || this._offset + this.length > this._rope.length)
      throw new IndexOutOfRangeException(this._offset);
    
    this._depth  = this._rope.depth() + 1;
  }

  int charCodeAt(final int index) {
    return this._rope.charCodeAt(this._offset + index);
  }
  
  int depth(){
    return _depth;
  }
  
  int getOffset(){
    return _offset;
  }
  
  Rope getRope(){
    return _rope;
  }
  
  Iterator<Rope> iterator(int start) {
    
  }
  // TODO: iterator
//    public Iterator<Character> iterator(final int start) {
//    if (start < 0 || start >= length())
//      throw new IndexOutOfBoundsException("Rope index out of range: " + start);
//    return this.rope.iterator(this.offset + start);
//  }

  Rope subSequence(final int start, final int end) {
    return new SubstringRope(_rope, _offset + start, end-start);
  }
  
  String toString() {
    return _rope.toString(_offset, _length);
  }
  
  Rope rebalance() {
    return this;
  }
  
  //TODO: write
  //TODO: matcher
}

class ConcatenationRope extends AbstractRope {
  final Rope _left;
  final Rope _right;
  int _depth;
  int _length;
  
  ConcatenationRope(this._left, this._right) {
    this._depth = (Math.max(Depth(this._left), Depth(this._right)));
  }
  
  
  int charCodeAt(final int index) {
    if (index < 0 || index >= this._length)
      throw new IndexOutOfRangeException(index);
    
    if (index < this._left.length)
      return this._left.charCodeAt(index);
    else
      return this._right.charCodeAt(index - this._left.length);
  }
  
  Rope get left() => this._left;
  
  Rope get right() => this._right; 
  
  // TODO: iterator
//    public Iterator<Character> iterator(final int start) {
//    if (start < 0 || start >= length())
//      throw new IndexOutOfBoundsException("Rope index out of range: " + start);
//    if (start >= this.left.length()) {
//      return this.right.iterator(start - this.left.length());
//    } else {
//      return new ConcatenationRopeIteratorImpl(this, start);
//    }
//  }

  Rope subSequence(final int start, final int end) {
    if (start < 0 || end > this.length)
      throw new IllegalArgumentException("Illegal subsequence (" + start + "," + end + ")");
    final int l = this.left.length;
    if (end <= l)
      return this.left.subSequence(start, end);
    if (start >= l)
      return this.right.subSequence(start - l, end - l);
    return concatenate(
      this.left.subSequence(start, l),
      this.right.subSequence(0, end - l));
  }
  
  Rope rebalance() {
    return Rebalance(this);
  }
  
  int depth(){
    return _depth;
  }

//  void write(Writer out) throws IOException {
//    left.write(out);
//    right.write(out);
//  }
//
//  void write(Writer out, int offset, int length) throws IOException {
//    if (offset + length < left.length()) {
//      left.write(out, offset, length);
//    } else if (offset >= left.length()) {
//      right.write(out, offset - left.length(), length);
//    } else {
//      int writeLeft = left.length() - offset;
//      left.write(out, offset, writeLeft);
//      right.write(out, 0, right.length() - writeLeft);
//    }
//  }


}

class RopeIterator implements Iterator<Rope> {
  bool hasNext(){
    
  }
  
  Rope next(){
    
  }
}
