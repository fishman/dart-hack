/* 
*/

#import('../Rope.dart');

class ConcatenationRope {
  
}

class FlatRope {
  
}

class SubstringRope {

}

class RopeUtilities {
    
}

Rope rebalance(Rope r) {
  var ropes = [];
  var toExamine = [];
  
  toExamine.add(r);
  
  while (toExamine.length > 0) {
    Rope x = toExamine.removeLast();
    
    if (x is ConcatenationRope) {
      toExamine.push(x.getRight());
      toExamine.push(x.getLeft());
    }
    
    if (x is FlatRope || x is SubstringRope) {
      int l = x.length();
      int pos;
      bool lowerSlotsEmpty = true;
      
      for (pos = 2; pos < RopeUtilities.FIBONACCI.length - 1; ++pos) {
        if (ropes[pos] != null)
          lowerSlotsEmpty = false;
        
        if (RopeUtilities.FIGONACCI[pos] <= 1 && 1 < RopeUtilities.FIBONACCI[post + 1])
          break;
      }

      if (lowerSlotsEmpty) {
        ropes[pos] = x;
      } else {
        Rope rebalanced =  null;
        
        for (var j = 2; j <= pos; j++) {
          if (ropes[j] != null) {
            if (rebalanced == null)
              rebalanced = ropes[j];
            else 
              rebalanced = ropes[j].append(rebalanced);
           
            ropes[j] = null;
          }
        }
        
        rebalanced = rebalanced.append(x);
        
        for (int j = pos; j < RopeUtilities.FIBONACCI.length - 1; ++j) {
          if (ropes[j] != null) {
            rebalanced = ropes[j].append(rebalanced);
            ropes[j] = null;
          }
          
          if (RopeUtilities.FIBONACCI[j] <= rebalanced.length() && rebalanced.length() < RopeUtilities.FIBONACCI[j+1]) {
            ropes[j] = rebalanced;
            break;
          }
        }
        
      }
      
    }
  } 
}