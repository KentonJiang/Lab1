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
public class GraphTest3 {

  @Test
  public void testQueryBridgeWords() {
    String expected = "explore ";
    Graph aGraph = new Graph();
    try {
      aGraph.createGraph("/Users/jiangzhenfei/Downloads/Lab1-lab6b/myfile.txt");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    assertEquals("fail --一个桥接词测试失败", expected,aGraph.queryBridgeWords("to", "strange"));
    
  }

}
