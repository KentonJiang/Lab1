package testpair;

import java.io.IOException;
import java.util.*;

/**
 * 
 */
public class GraphControl {

    /**
     * Default constructor
     */
    public GraphControl() {
      graph = new Graph();
    }

    /**
     * 
     */
    public Graph graph;



    /**
     * @param filename
     */
    public void showGraph(String filename) {
        // TODO implement here
      try {
        graph.createGraph(filename);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    /**
     * @param word1 
     * @param word2
     */
    public String queryBridgeWords(String word1, String word2) {
        // TODO implement here
      return graph.queryBridgeWords(word1, word2) + "\n";
    }

    /**
     * @param text
     */
    public String generateNewText(String text) {
      return graph.generateNewText(text);
    }

    /**
     * @param word1 
     * @param word2
     */
    public String calcShortestpath(String word1, String word2) {
        return graph.calcShortestpath(word1, word2);
    }

    /**
     * 
     */
    public String randomWalk() {
      graph.exit = false;
      return graph.randomWalk();
    }

}