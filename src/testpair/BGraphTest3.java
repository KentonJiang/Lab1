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
public class BGraphTest3 {

  @Test
  public void testRandomWalk() {
    String expected = "hello world";
    Graph aGraph = new Graph();
    try {
      aGraph.createGraph("/Users/jiangzhenfei/Desktop/testcase3.txt");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String result = aGraph.randomWalk();
    System.out.println(result);
    if (result.equals("hello world hello world") || result.equals("world hello world hello")) {
      System.out.println("Congratulations!!!!");
    }
    else{
      fail("fail in testcase 3:有没有重复的");
    }
    
  }

}
