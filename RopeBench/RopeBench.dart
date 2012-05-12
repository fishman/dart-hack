#source("./library/rope2.dart");

void main() {
  String testStr = "hello world";
  Rope ropeSample = new FlatRope(testStr);
  String testStr2 = "world hello";
  print(ropeSample.toString());
  if(ropeSample.append(testStr2).append(testStr2) is ConcatenationRope)
 
    print("yay for concatenation"); 
}
