class RopeBuilder {
  Rope build(final sequence){
    if (sequence is Rope)
      return sequence;
    return new FlatRope(sequence);
  }
}

interface Rope extends Iterable<Rope> {  
  Rope append(String c);
  
  Rope appendSubstr(String c, int start, int end);
  
  Rope delete(int start, int end);

  Rope indexOf(String ch);
  
  Rope insert(int dstOffset, String str);
  
  // TODO: Iterator<String> iterator(int start)
  
  Rope rebalance();
  
  // TODO: write(Writer out)
  
  Rope subSequence(int start, int end);
  
  // TODO: matcher
}

class AbstractRope implements Rope {
  static RopeBuilder _builder;
  int _hashCode = 0;
  
  
  static RopeBuilder get Builder() {
    if (_builder === null){
      _builder = new RopeBuilder();
    }
    return _builder;
  }
  
  Rope append(String c) {
    return RopeUtilities.Util().concatenate(this, Rope.Builder.build(String)));
  }
  
  Rope appendSubstr(String csq, final int start, final int end) {
    return RopeUtilities.Util().concatenate(this, Rope.Builder.build(String).subSequence(start, end));
  }
  
  Rope delete(final int start, final int end){
    if (start == end)
      return this;
    return this.subSequence(0, start).append(this.subSequence(end, this.length()));
  }
  
  abstract int depth();
  
  int hashCode() {
    if (this._hashCode == 0 && length () > 0) {
      if (this.length() < 6) {
// TODO:       for (final char c: this)
//          this._hashCode = 31 * this._hashCode + c;
      } else {
        // TODO: final Iterator<Character> i = this.iterator();
        // for (int j=0;j<5; ++j)
         //  this._hashCode = 31 * this._hashCode + i.next();
        this._hashCode = 31 * this._hashCode + this.charAt(this.length() - 1);
        
      }
    }
    return this._hashCode;
  }
  
  bool equals(final Object other){
    if (other is Rope) {
      final Rope rope = other;
      if (rope.hashCode() != this.hashCode() || rope.length() != this())
        return false;
      
//TODO:      final Iterator<Character> i1 = this.iterator();
//      final Iterator<Character> i2 = rope.iterator();
//
//      while (i1.hasNext()) {
//        final char a = i1.next();
//        final char b = i2.next();
//        if (a != b)
//          return false;
//      }
//      return true;

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
    final Rope r = (s == null) ? Rope.Builder.build("null") : Rope.Builder.build(s);
    
    if (dstOffset == 0)
      return r.append(this);
    else if (dstOffset == this.length())
      return this.append(r);
    else if (dstOffset < 0 || dstOffset > this.length())
      throw new IndexOutOfRangeException(dstOffset + " is out of insert range [" + 0 + ":" + this.length() + "]");
    return this.subSequence(0, dstOffset).append(r).append(this.subSequence(dstOffset, this.length()));
  }
  
  //TODO: Iterator<Character> iterator() { return this.iterator(0); }
  
  int compareTo(String sequence) {
    int compareTill = Math.min(sequence.length(), length());
    
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

class FlatRope implements AbstractRope {
  final String _sequence;
  
  // constructor
  FlatRope(this._sequence);
  
  String charAt(final int index) {
    return this._sequence[index];
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
    this._sequence.length();
  }
  
  Rope subSequence(final int start, final int end) {
    if (end - start < 8 || this._sequence.subSequence(start, end)) {
      return new FlatRope(this.sequence.subSequence(start, end));
    } else {
      return new SubStringRope(this, start, end-start);
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

class SubstringRope e