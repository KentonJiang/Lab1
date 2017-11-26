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
public class BGraphTest2 {

  @Test
  public void testRandomWalk() {
    String expected = "hello";
    Graph aGraph = new Graph();
    try {
      aGraph.createGraph("/Users/jiangzhenfei/Desktop/testcase2.txt");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    assertEquals("fail --no successor---case2", expected,aGraph.randomWalk());
    
  }

}