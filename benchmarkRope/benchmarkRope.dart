#import('dart:html');
//#import("dart:io");
//#import("dart:core");
//#import("dart:coreimpl");
//#import("dart:json");

class benchmarkRope {

  benchmarkRope() {
  }
  
  //static final TOTAL_TEST_COUNT = 10;
  
  void run() {
    CanvasElement canvas = document.query("#canvas");
    ctx = canvas.getContext("2d");
    results = new List();
    
    OperationCBIF cb = new DefaultConcatOperation();
    ropeTest(100, cb);
    ropeTest(500, cb);
    ropeTest(700, cb);
    ropeTest(1000, cb);
    ropeTest(2000, cb);
    ropeTest(3000, cb);
    ropeTest(4000, cb);
    ropeTest(5000, cb);
    OperationCBIF cb2 = new DefaultConcatOperation();
    ropeTest(5000, cb2);
    OperationCBIF cb3 = new DefaultConcatOperation();
    ropeTest(5000, cb3);
    ropeTest(5000, cb3);
    ropeTest(5000, cb3);
    ropeTest(5000, cb3);
//    OperationCBIF cb4 = new DefaultConcatOperation();
//    ropeTest(8000, cb4);
//    ropeTest(8000, cb4);
//    ropeTest(8000, cb4);
    
    for (TestResult eachResult in results) {
      drawCircle(eachResult.iter/maxIteration * CANVAS_WIDTH - SEED_RADIUS, 
        CANVAS_HEIGHT - (eachResult.time/maxTestTime * CANVAS_HEIGHT));
    }
    
  }

  void ropeTest(int iterations, OperationCBIF operationFunction) {
    Stopwatch timer = new Stopwatch();
    String st = 'a';
    timer.start();
    for (int i = 0; i < iterations; i++) {
      int num = "1";//(Math.random().toDouble() * 10).toInt().toString();
      st = operationFunction.operate(num);
    }
    timer.stop();
    int testTime = timer.elapsedInUs();
    write(iterations.toString() + " iterations took ${testTime} us");
//    write("string is " + st);
    testCount++;
    
    TestResult result = new TestResult();
    result.iter = iterations;
    result.time = testTime;
    results.add(result);
    
    if (testTime > maxTestTime) {
      maxTestTime = testTime;
    }
    if (iterations > maxIteration) {
      maxIteration = iterations;
    }
  }

  void write(String message) {
    // the HTML library defines a global "document" variable
    document.query('#status').innerHTML += '<br>' + message;
  }
  
  // Draw a small circle representing a seed centered at (x,y).
  void drawCircle(num x, num y) {
    ctx.beginPath();
    ctx.lineWidth = 2;
    ctx.fillStyle = ORANGE;
    ctx.strokeStyle = ORANGE;
    ctx.arc(x, y, SEED_RADIUS, 0, TAU, false);
    ctx.fill();
    ctx.closePath();
    ctx.stroke();
  }

  CanvasRenderingContext2D ctx;
  List<TestResult> results;
  int testCount = 0;
  int maxTestTime = 0;
  int maxIteration = 0;
  static final SEED_RADIUS = 2;
  static final TAU = Math.PI * 2;
  static final String ORANGE = "orange";
  static final int CANVAS_HEIGHT = 300;
  static final int CANVAS_WIDTH = 300;
  
}

interface OperationCBIF {
  String operate(String inputSt);
}

class DefaultConcatOperation implements OperationCBIF {
  String localSt = '';

  String operate(String inputSt) {
    localSt = localSt.concat(inputSt);
    return localSt;
  }
}

class TestResult {
  int iter;
  int time;
}

void main() {
  new benchmarkRope().run();
}
