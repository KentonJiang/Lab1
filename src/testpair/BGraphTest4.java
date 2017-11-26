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
public class BGraphTest4 {

  @Test
  public void testRandomWalk() {
    String expected = "hello hello hello";
    Graph aGraph = new Graph();
    try {
      aGraph.createGraph("/Users/jiangzhenfei/Desktop/testcase4.txt");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String result = aGraph.randomWalk();
    System.out.println(result);
    assertEquals("fail --all ---case4", expected,aGraph.randomWalk());
  }

}
