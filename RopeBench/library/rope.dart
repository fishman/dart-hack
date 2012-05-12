interface Rope {
  // we can dispatch string
  int indexOf(String ch);
  int indexOfFromIndex(String ch, int fromIndex);
  Rope insert(int dstOffset, String s);
  Rope trimStart();
  bool matches(String regex);
  Rope rebalance();
  Rope reverse();
  Rope trimEnd();
  Rope subSequence(int start, int end);
  Rope trim();
  Rope padStart(int toLength);
  Rope paEnd(int toLength);
  bool isEmpty();
  bool startsWith(String pref);
  bool endsWith(String suffix);
}



class AbstractRope implements Rope {
  int _hashCode = 0;
  String _ropeString;
  
  AbstractRope(String this._ropeString);
  
  Rope append(String c) {
    return RopeUtilities.Util().concatenate(this, new AbstractRope());
  }
  
 // Rope.appendSubStr(String )
}

class FlatRope implements AbstractRope {
}
