package testpair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
//import java.util.*;
import java.util.logging.Logger;

//import javax.swing.text.html.HTMLDocument.Iterator;

//import javax.security.auth.callback.LanguageCallback;

public class MyGraphTest {
  static Logger log = Logger.getLogger(MyGraphTest.class.getName());
  public static void main(String args[]) throws IOException {
    final Scanner myTxt = new Scanner(Paths.get("myfile.txt"));
    final StringBuilder myStringBuilder = new StringBuilder();
    while (myTxt.hasNextLine()) {
      myStringBuilder.append(myTxt.nextLine());
      myStringBuilder.append(" ");
    }
    myTxt.close();
    String myString = myStringBuilder.toString();
    myString = myString.toLowerCase();
    myString = myString.replaceAll("[^a-z^A-Z^\\s]", " ");
    myString = myString.replaceAll("\\^", " ");
    final String[] myStringList = myString.split("\\s+");

    for (String e : myStringList) {
      System.out.println(e);
    }

    Graph agraph = new Graph();

    // agraph.createGraph("myfile.txt");
    // agraph.check();
    agraph.queryBridgeWords("new", "and");
    log.fine(agraph.generateNewText("Seek to explore new and exciting synergies"));

    log.fine(agraph.calcShortestpath("civilizations", "new"));
    log.fine(agraph.randomWalk());
  }
}

class EdgeNode {
  private int weight; // 权值，由在文本中相邻的次数确定
  private String word; // 单字

  public EdgeNode(final String mword) {
    word = mword;
    weight = 1;
  }

  public int getWeight() {
    return weight;
  }

  public String getWord() {
    return word;
  }

  public void incWeight() {
    weight++;
  }
}

class Graph {
  private Map<String, ArrayList<EdgeNode>> head;
  private int shortsum;
  Logger log = Logger.getLogger(Graph.class.getName());
  public String edgeStart;
  public String edgeEnd;
  public String path;
  public Map<String, ArrayList<String>> visited;
  public boolean flag;
  public volatile boolean exit = false;

  public String getshortsum() {
    return Integer.toString(shortsum);
  }

  public Graph() {
    head = new HashMap<String, ArrayList<EdgeNode>>();
  }

  public Map<String, ArrayList<EdgeNode>> getHead() {
    return head;
  }

  public void createGraph(String filepath) throws IOException {
    final Scanner myTxt = new Scanner(Paths.get(filepath));
    final StringBuilder myStringBuilder = new StringBuilder();
    while (myTxt.hasNextLine()) {
      myStringBuilder.append(myTxt.nextLine());
      myStringBuilder.append(" ");
    }
    myTxt.close();
    String myString = myStringBuilder.toString();
    myString = myString.toLowerCase();
    myString = myString.replaceAll("[^a-z^A-Z^\\s]", " ");
    myString = myString.replaceAll("\\^", " ");
    final String[] myStringList = myString.split("\\s+");

    final int nlonger = myStringList.length;
    ArrayList<EdgeNode> mlonger;
    for (int i = 0; i < nlonger - 1; i++) {
      mlonger = head.get(myStringList[i]);
      if (mlonger == null) {
        head.put(myStringList[i], new ArrayList<EdgeNode>());
        head.get(myStringList[i]).add(new EdgeNode(myStringList[i + 1]));
      } else {
        final ListIterator<EdgeNode> elter = mlonger.listIterator();
        boolean added = false;
        while (elter.hasNext()) {
          final EdgeNode medgenode = elter.next();
          if (myStringList[i + 1].equals(medgenode.getWord())) {
            added = true;
            medgenode.incWeight();
          }
        }
        if (!added) {
          elter.add(new EdgeNode(myStringList[i + 1]));
        }
      }
    }
    head.put(myStringList[nlonger - 1], null);
    // TO DO generate the image
    check();
  }

  public static void createDotGraph(String dotFormat, String fileName) {
    final GraphViz gvlonger = new GraphViz();
    gvlonger.addln(gvlonger.start_graph());
    gvlonger.add(dotFormat);
    gvlonger.addln(gvlonger.end_graph());
    // String type = "gif";
    final String type = "png";
    // gv.increaseDpi();
    gvlonger.decreaseDpi();
    gvlonger.decreaseDpi();
    final File out = new File(fileName + "." + type);
    gvlonger.writeGraphToFile(gvlonger.getGraph(gvlonger.getDotSource(), type), out);
  }

  public void check() {
    String dot = "";
    final java.util.Iterator<Entry<String, ArrayList<EdgeNode>>> ilonger = head.entrySet().iterator();
    while (ilonger.hasNext()) {
      final Map.Entry<String, ArrayList<EdgeNode>> ahead 
          = (Map.Entry<String, ArrayList<EdgeNode>>) ilonger.next();
      // System.out.print(ahead.getKey()+":");

      final ArrayList<EdgeNode> mlonger = (ArrayList<EdgeNode>) ahead.getValue();
      if (mlonger != null) {
        final java.util.Iterator<EdgeNode> jlonger = mlonger.iterator();
        while (jlonger.hasNext()) {
          // System.out.print(j.next().getWord() + " ");
          final EdgeNode tlonger = jlonger.next();
          dot = dot + ahead.getKey() + "->" + tlonger.getWord() + "[label=\"" + tlonger.getWeight() + "\"];";
        }
      }
    }
    createDotGraph(dot, "graph01");
  }

  public String queryBridgeWords(final String word1, String word2) {
    boolean keyFlag = false;
    for (String e : head.keySet()) {
      if (e.equals(word1)) {
        for (String e2 : head.keySet()) {
          if (e2.equals(word2)) {
            keyFlag = true;
            break;
          }
        }

      }

    }
    if (!keyFlag) {
      log.fine("No word1 or word2 in the graph!");
      return null;
    } else if (head.get(word1) == null) {
      log.fine("No bridge words from word1 to word2!");
      return null;
    } else {
      final ListIterator<EdgeNode> bridge = head.get(word1).listIterator();
      StringBuffer bridgeWords = new StringBuffer();
      boolean findBridge = false;
      while (bridge.hasNext()) {
        final String word3 = bridge.next().getWord();// 就是临时变量
        if (head.get(word3) != null) {
          final ListIterator<EdgeNode> abridge = head.get(word3).listIterator();
          while (abridge.hasNext()) {
            if (abridge.next().getWord().equals(word2)) {
              findBridge = true;
              //bridgeWords = bridgeWords + word3 + " ";
              bridgeWords.append(word3);
              bridgeWords.append(' ');
            }
          }
        }
      }
      if (findBridge) {
        log.fine("The bridge words from word1 to word2 are:" + bridgeWords);
        return bridgeWords.toString();
      } else {
        log.fine("No bridge words from word1 to word2!");
        return null;
      }
    }
  }

  public String generateNewText(String inputText) {
    inputText = inputText.toLowerCase();
    inputText = inputText.replaceAll("[^a-z^A-Z^\\s]", " ");
    inputText = inputText.replaceAll("\\^", " ");
    final String[] inputTextList = inputText.split("\\s+");
    final int nlonger = inputTextList.length;
    String abridge = null;
    final StringBuilder newTextBuilder = new StringBuilder();

    for (int i = 0; i < nlonger - 1; i++) {
      abridge = queryBridgeWords(inputTextList[i], inputTextList[i + 1]);
      newTextBuilder.append(inputTextList[i]);
      newTextBuilder.append(' ');
      if (abridge != null) {
        final String[] abridgewords = abridge.split("\\s+");
        final int rand = (int) (Math.random() * abridgewords.length);

        newTextBuilder.append(abridgewords[rand]);
        newTextBuilder.append(' ');
      }
    }
    newTextBuilder.append(inputTextList[nlonger - 1]);
    return newTextBuilder.toString();
  }

  public String calcShortestpath(String word1, String word2) {
    try {
      final Map<String, Integer> distance = new HashMap<String, Integer>();
      final Map<String, String> path = new HashMap<String, String>();
      String[] arr = new String[head.keySet().size()];

      arr = head.keySet().toArray(arr);
      for (String vertex : arr) {
        distance.put(vertex, Integer.valueOf(Integer.MAX_VALUE - 2000));
        path.put(vertex, word1);
      }
      if (head.get(word1) != null) {
        for (EdgeNode e : head.get(word1)) {
          distance.put(e.getWord(), e.getWeight());
        }         
      }
      for (int i = 0; i < arr.length; i++) {
        if (arr[i].equals(word1)) {
          arr[i] = null;
        }
      }       
      for (int i = 1; i < arr.length; i++) {
        Integer temp = Integer.valueOf(Integer.MAX_VALUE - 2000);
        int way = 0;
        for (int j = 0; j < arr.length; j++) {
          if (arr[j] != null) {
            if ((distance.get(arr[j]).compareTo(temp)) < 0) {
              temp = distance.get(arr[j]);
              way = j;
            }
          }
        }
        final String propath = arr[way];
        arr[way] = null;

        if (head.get(propath) != null) {
          for (EdgeNode e : head.get(propath)) {
            boolean find = false;
            for (String s : arr) {
              if (s != null && s.equals(e.getWord())) {
                find = true;
              }               
            }
            if (find) {
              final Integer sum = new Integer(distance.get(propath) + e.getWeight());
              if (sum.compareTo(distance.get(e.getWord())) < 0) {
                distance.put(e.getWord(), sum);
                path.put(e.getWord(), propath);
              }
            }
          }
        }
      }
      String wordTemp = word2;
      final StringBuffer shortestpath = new StringBuffer();
      while (!wordTemp.equals(word1)) {
        //shortestpath = " " + wordTemp + shortestpath;
        shortestpath.insert(0, wordTemp);
        shortestpath.insert(0, ' ');
        wordTemp = path.get(wordTemp);
      }
      //shortestpath = word1 + shortestpath;
      shortestpath.insert(0, word1);
      if (distance.get(word2) < Integer.MAX_VALUE - 2000) {
        shortsum = distance.get(word2);
        return shortestpath.toString();
      } else {
        return "No path";
      }       
    } catch (java.lang.NullPointerException nullPEshorter) {
      // TODO: handle exception
      return "No path";
    }

  }

  public boolean randomWalkAssist() {
    if (head.get(edgeStart) == null) {
      path = path + edgeStart;
      return true;// something to stop
      // break;
    } else {
      final int randNext = (int) (Math.random() * (head.get(edgeStart).size()));
      edgeEnd = head.get(edgeStart).get(randNext).getWord();
      path = path + edgeStart + " ";
      if (visited.get(edgeStart) == null) {
        final ArrayList<String> arr = new ArrayList<String>();
        arr.add(edgeEnd);
        visited.put(edgeStart, arr);
        edgeStart = edgeEnd;
      } else {
        for (String e : visited.get(edgeStart)) {
          if (e.equals(edgeEnd)) {
            flag = true;
            return true;
            // break walk;//return something
          }         
        }
        visited.get(edgeStart).add(edgeEnd);
        edgeStart = edgeEnd;

      }
    }
    return false;
  }


  public String randomWalk() {
    if (head.isEmpty()) {
      return null;
    }     
    String[] keyArr = new String[head.keySet().size()];
    keyArr = head.keySet().toArray(keyArr);
    visited = new HashMap<String, ArrayList<String>>();
    final int randStart = (int) (Math.random() * (keyArr.length));
    edgeStart = keyArr[randStart];
    edgeEnd = null;
    path = "";
    flag = false;
    // walk:while(true)
    // {
    //
    // }
    final Runnable assist = () -> {
      boolean needEnd = false;
      while (!exit && !needEnd) {
        needEnd = randomWalkAssist();
      }
      if (flag && edgeEnd != null) {
        path = path + edgeEnd;
      }       
    };
    final Thread thread = new Thread(assist);
    thread.start();
    // for(int i = 0; i < 10000; i++){}
    // exit = true;
    try {
      thread.join(0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return path;
  }
}
