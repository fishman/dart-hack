#import('dart:html');
//#import("dart:io");
//#import("dart:core");
//#import("darts:coreimpl");
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
    bulkTestDataTemplate(cb, "Default", "orange");

//    OperationCBIF cb = new DefaultConcatOperation();
    bulkTestDataTemplate(cb, "Default", "green");
  }
  
  void bulkTestDataTemplate(OperationCBIF cb, String concatType, String dotColour) {
    timedTest(20480, cb, concatType);
    timedTest(15000, cb, concatType);
    timedTest(10240, cb, concatType);
    timedTest(5120, cb, concatType);
    timedTest(2560, cb, concatType);
    timedTest(1280, cb, concatType);
    timedTest(640, cb, concatType);

    timedTest(20480, cb, concatType);
    timedTest(15000, cb, concatType);
    timedTest(10240, cb, concatType);
    timedTest(5120, cb, concatType);
    timedTest(2560, cb, concatType);
    timedTest(1280, cb, concatType);
    timedTest(640, cb, concatType);
    
    timedTest(320, cb, concatType);
    timedTest(640, cb, concatType);
    timedTest(1280, cb, concatType);
    timedTest(2560, cb, concatType);
    timedTest(5120, cb, concatType);
    timedTest(10240, cb, concatType);
    timedTest(15000, cb, concatType);
    timedTest(20480, cb, concatType);
    timedTest(15000, cb, concatType);
    timedTest(10240, cb, concatType);
    timedTest(5120, cb, concatType);
    timedTest(2560, cb, concatType);
    timedTest(1280, cb, concatType);
    timedTest(640, cb, concatType);
    timedTest(320, cb, concatType);

    timedTest(640, cb, concatType);
    timedTest(1280, cb, concatType);
    timedTest(2560, cb, concatType);
    timedTest(5120, cb, concatType);
    timedTest(10240, cb, concatType);
    timedTest(15000, cb, concatType);
    timedTest(20480, cb, concatType);
    timedTest(15000, cb, concatType);
    timedTest(10240, cb, concatType);
    timedTest(5120, cb, concatType);
    timedTest(2560, cb, concatType);
    timedTest(1280, cb, concatType);
    timedTest(640, cb, concatType);
    timedTest(320, cb, concatType);
    
    colour = dotColour;
    
    for (TestResult eachResult in results) {
      drawCircle(eachResult.size/maxSize * CANVAS_WIDTH - SEED_RADIUS, 
        CANVAS_HEIGHT - (eachResult.count/maxCount * CANVAS_HEIGHT));
    }
    
    results = new List();
  }
  
  int timedTest(int oriSize, OperationCBIF cb, String concatType) {
    testCount = 0;
    String st = '';
    for (int i=0; i<oriSize; i++) {
      st.concat("1");
    }
    st = ropeTimeTest(st, cb);
    write(testCount.toString() + " iterations for " + concatType + " concat String size " + oriSize);
    
    TestResult result = new TestResult();
    result.count = testCount;
    result.size = oriSize;
    results.add(result);
    
    if (oriSize > maxSize) {
      maxSize = oriSize;
    }
    if (testCount > maxCount) {
      maxCount = testCount;
    }
    
    return testCount;
  }

  String ropeTimeTest(String st, OperationCBIF operationFunction) {
    int startTime = Clock.now();
//    String st = 'a';
    while (true) {
      String toAppend = "1"; //(Math.random().toDouble() * 10).toInt().toString();
      st = operationFunction.operate(toAppend);
      int now = Clock.now();
//      write(now.toString());
      testCount++;
      if (now > (TEST_TIME + startTime)) {
        break;
      }
    }
    
    return st;
  }

  void write(String message) {
    // the HTML library defines a global "document" variable
    document.query('#status').innerHTML += '<br>' + message;
  }
  
  // Draw a small circle representing a seed centered at (x,y).
  void drawCircle(num x, num y) {
    ctx.beginPath();
    ctx.lineWidth = 2;
    ctx.fillStyle = colour;
    ctx.strokeStyle = colour;
    ctx.arc(x, y, SEED_RADIUS, 0, TAU, false);
    ctx.fill();
    ctx.closePath();
    ctx.stroke();
  }

  CanvasRenderingContext2D ctx;
  List<TestResult> results;
  int testCount = 0;
  int maxSize = 0;
  int maxCount = 0;
  static final SEED_RADIUS = 2;
  static final TAU = Math.PI * 2;
  static String colour = "orange";
  static final int CANVAS_HEIGHT = 400;
  static final int CANVAS_WIDTH = 600;
  
  static final int TEST_TIME = 5000;
  
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
  int count;
  int size;
}

void main() {
  new benchmarkRope().run();
}
