#library('Rupe');

#import('RupeIterableImpl.dart');

void testRupeIterableImpl() {  
  RupeIterableImpl rupeItImpl = new RupeIterableImpl();  
  
  var chars = rupeItImpl.iterator();
  
  while (chars.hasNext()) {
    print(chars.next());
  }
}


main() {
  
  testRupeIterableImpl();
}
