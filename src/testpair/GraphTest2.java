/**
 * 
 */
package testpair;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

/**
 * @author jiangzhenfei
 *
 */
public class GraphTest2 {

  @Test
  public void testQueryBridgeWords() {
    Graph aGraph = new Graph();
    try {
      aGraph.createGraph("/Users/jiangzhenfei/Downloads/Lab1-lab6b/myfile.txt");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String result = aGraph.queryBridgeWords("life", "new");
    if (!result.equals("his and ") && !result.equals("and his ")) { 
      fail("fail ---多个桥接词失败");
    }
  }
}
