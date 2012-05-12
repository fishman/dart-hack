#library('Rope');

#import('FlatRope.dart');

class FlatStringRope implements FlatRope {
  
  String _text;
  
  FlatStringRope(String str) {
    _text = str;
  }
}
