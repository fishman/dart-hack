#library('Rope');

#import('Rope.dart');
#import('FlatStringRope.dart');

class RopeBuilder {
  
  Rope build(String str) {
    return new FlatStringRope(str); 
  }
}