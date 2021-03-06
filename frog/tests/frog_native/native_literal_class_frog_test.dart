// Copyright (c) 2012, the Dart project authors.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

class A native "= {log: function() { return 42 } }" {
  void log() native;
}

getA() native """
  return A;
""";

bar() {
  new A();
}

main() {
  var a = getA();
  // Make a call to bar to make sure A gets generated.
  if (a is int) bar();
  Expect.equals(42, a.log());
}
