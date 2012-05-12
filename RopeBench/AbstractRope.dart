#library('Rope');

#import('Rope.dart');


/*
This class will be abstract when Dart will support abstract classes and abstrat methods
*/

class AbstractRope implements Rope {
  
  int hashcode = 0;
  
  Rope append(String str, [int start = 0, int end = 0]) {
    //@todo
    
  }
  
  Rope delete(int start, [int end]) {
    
  }
  
}
