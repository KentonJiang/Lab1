/**
 * 
 */
package testpair;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @author jiangzhenfei
 *
 */
public class BGraphTest1 {

  @Test
  public void testRandomWalk() {
    Graph aGraph = new Graph();
//    try {
//      aGraph.createGraph("/Users/jiangzhenfei/Desktop/testcase1.txt");
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
    if (aGraph.randomWalk() != null) {
      fail("fail in randomwalk-------null graph case1!!!!!!");
    }
    
  }

}
