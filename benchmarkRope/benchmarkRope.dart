#import('dart:html');
//#import("dart:io");
//#import("dart:core");
//#import("dart:coreimpl");
//#import("dart:json");

class benchmarkRope {

  benchmarkRope() {
  }
  
  void run() {
    OperationCBIF cb = new DefaultConcatOperation();
    ropeTest(500, cb);
  }

  void ropeTest(int iterations, OperationCBIF operationFunction) {
    Stopwatch timer = new Stopwatch();
    String st = 'a';
    timer.start();
    for (int i = 0; i < iterations; i++) {
      int num = (Math.random().toDouble() * 10).toInt().toString();
      st = operationFunction.operate(num);
//      write("string is " + st);
    }
    timer.stop();
    write(iterations.toString() + " iterations took ${timer.elapsedInUs()} us");
  }

  void write(String message) {
    // the HTML library defines a global "document" variable
    document.query('#status').innerHTML += ' ' + message;
  }
}

class OperationCBIF {
  String operate(String inputSt) {
    return inputSt;
  }
}

class DefaultConcatOperation extends OperationCBIF {
  String localSt = '';
  // @overides
  String operate(String inputSt) {
    localSt = localSt.concat(inputSt);
    return localSt;
  }
}

void main() {
  new benchmarkRope().run();
}
