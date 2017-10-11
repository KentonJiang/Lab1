package testpair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import javax.security.auth.callback.LanguageCallback;

public class MyGraphTest
{
	public static void main(String args[])throws IOException
	{
		Scanner myTxt = new Scanner(Paths.get("myfile.txt"));
		StringBuilder myStringBuilder = new StringBuilder();
		while (myTxt.hasNextLine()) {
			myStringBuilder.append(myTxt.nextLine());
			myStringBuilder.append(" ");
		}
		myTxt.close();
		String myString = myStringBuilder.toString();
		myString = myString.toLowerCase();
		myString = myString.replaceAll("[^a-z^A-Z^\\s]"," ");
        myString = myString.replaceAll("\\^"," ");
        String[] myStringList = myString.split("\\s+");
        
        for (String e : myStringList) {
            System.out.println(e);
        }

        Graph aGraph = new Graph();
        
        //aGraph.CreateGraph("myfile.txt");
        //aGraph.check();
        aGraph.queryBridgeWords("new", "and");
        System.out.println(aGraph.generateNewText("Seek to explore new and exciting synergies"));
        
        System.out.println(aGraph.calcShortestPath("civilizations", "new"));
        System.out.println(aGraph.randomWalk());
	}
	//在塞四上改的
}
class edgeNode
{
	private int weight; //权值，由在文本中相邻的次数确定
	private String word; //单字
	public edgeNode(String mWord)
	{
		word = mWord;
		weight = 1;
	}
	public int getWeight() 
	{
		return weight;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public void incWeight() 
	{
		weight++;
	}
}

class Graph
{
	private Map<String,ArrayList<edgeNode>> head;
	private int shortsum;
	public String getshortsum() {
		return ""+shortsum;
	}
	public Graph() {
		head = new HashMap<String,ArrayList<edgeNode>>();
	}
	public Map<String, ArrayList<edgeNode>> getHead() {
		return head;
	}
	public void CreateGraph(String filepath) throws IOException {
		Scanner myTxt = new Scanner(Paths.get(filepath));
		StringBuilder myStringBuilder = new StringBuilder();
		while (myTxt.hasNextLine()) {
			myStringBuilder.append(myTxt.nextLine());
			myStringBuilder.append(" ");
		}
		myTxt.close();
		String myString = myStringBuilder.toString();
		myString = myString.toLowerCase();
		myString = myString.replaceAll("[^a-z^A-Z^\\s]"," ");
        myString = myString.replaceAll("\\^"," ");
        String[] myStringList = myString.split("\\s+");
        
		int n = myStringList.length;
		ArrayList<edgeNode> m;
		for(int i = 0; i < n -1;i++)
		{
			m = head.get(myStringList[i]);
			if (m == null) {
				head.put(myStringList[i], new ArrayList<edgeNode>());
				head.get(myStringList[i]).add(new edgeNode(myStringList[i+1]));
			}
			else {
				ListIterator<edgeNode> eIter = m.listIterator();
				boolean added = false;
				while (eIter.hasNext()) {
					edgeNode mEdgeNode = eIter.next();
					if (myStringList[i+1].equals(mEdgeNode.getWord())){
						added = true;
						mEdgeNode.incWeight();
					}
				}
				if (!added) {
					eIter.add(new edgeNode(myStringList[i+1]));
				}
			}
		}
		head.put(myStringList[n-1], null);
		//TO DO generate the image
		check();
	}
	public static void createDotGraph(String dotFormat,String fileName)
	{
	    GraphViz gv=new GraphViz();
	    gv.addln(gv.start_graph());
	    gv.add(dotFormat);
	    gv.addln(gv.end_graph());
	   // String type = "gif";
	    String type = "png";
	  // gv.increaseDpi();
	    gv.decreaseDpi();
	    gv.decreaseDpi();
	    File out = new File(fileName+"."+ type); 
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}
	public void check() {
		String dot = "";
		Iterator<Map.Entry<String, ArrayList<edgeNode>>> i = head.entrySet().iterator();
		while(i.hasNext())
		{	 
			Map.Entry<String,ArrayList<edgeNode>> ahead = (Map.Entry<String,ArrayList<edgeNode>>) i.next();
			//System.out.print(ahead.getKey()+":");
			
			ArrayList<edgeNode> m = (ArrayList<edgeNode>)ahead.getValue();
			if (m != null) {
				Iterator<edgeNode> j = m.iterator();
				while(j.hasNext())
				{
					//System.out.print(j.next().getWord() + " ");
					edgeNode t = j.next();
					dot = dot + ahead.getKey() + "->" + t.getWord()+"[label=\""+t.getWeight()+"\"];";
				}
			}	
		}
		createDotGraph(dot, "graph01");
	}
	public String queryBridgeWords(String word1,String word2) 
	{
		boolean keyFlag = false;
		for (String e : head.keySet()) {
			if(e.equals(word1))
			{
				for (String e2 : head.keySet()) {
					if(e2.equals(word2))
					{
						keyFlag = true;
						break;
					}
				}
			
			}
			
		}
		if (!keyFlag)
		{
			System.out.println("No word1 or word2 in the graph!");
			return null;
		}
		else if(head.get(word1) == null)
		{
			System.out.println("No bridge words from word1 to word2!");			
			return null;
		}
		else 
		{
			ListIterator<edgeNode> bridge = head.get(word1).listIterator();
			String bridgeWords = "";
			boolean findBridge = false;
			while(bridge.hasNext())
			{
				String word3 = bridge.next().getWord();//就是临时变量
				if(head.get(word3) != null)
				{
					ListIterator<edgeNode> abridge = head.get(word3).listIterator();
					while(abridge.hasNext())
					{
						if(abridge.next().getWord().equals(word2))
						{
							findBridge = true;
							bridgeWords = bridgeWords + word3 +" ";
						}
					}
				}
			}
			if (findBridge)
			{
				System.out.println("The bridge words from word1 to word2 are:" + bridgeWords);
				return bridgeWords;
			}
			else
			{
				System.out.println("No bridge words from word1 to word2!");
				return null;
			}
		}
	}
	public String generateNewText(String inputText)
	{
		inputText = inputText.toLowerCase();
		inputText = inputText.replaceAll("[^a-z^A-Z^\\s]"," ");
        inputText = inputText.replaceAll("\\^"," ");
        String[] inputTextList = inputText.split("\\s+");
        int n = inputTextList.length;
        String aBridge = null;
        StringBuilder newTextBuilder = new StringBuilder();
        
        for(int i = 0;i < n - 1;i++)
        {
        	aBridge = queryBridgeWords(inputTextList[i], inputTextList[i + 1]);
        	newTextBuilder.append(inputTextList[i]);
        	newTextBuilder.append(" ");
        	if(aBridge != null)
        	{
        		String[] aBridgeWords = aBridge.split("\\s+");
        		int rand = (int)(Math.random()*aBridgeWords.length);
        		
        		newTextBuilder.append(aBridgeWords[rand]);
        		newTextBuilder.append(" ");
        	}
        }
        newTextBuilder.append(inputTextList[n - 1]);
        return newTextBuilder.toString();
	}
	public String calcShortestPath(String word1, String word2)
	{
		try {
			Map<String,Integer> distance = new HashMap<String,Integer>();
			Map<String, String> path = new HashMap<String,String>();
			String[] arr = new String[head.keySet().size()];
			
			arr = head.keySet().toArray(arr);
			for (String vertex : arr)
			{
				distance.put(vertex, new Integer(Integer.MAX_VALUE - 2000));
				path.put(vertex, word1);
			}
			if (head.get(word1) != null)
				for (edgeNode e : head.get(word1))
					distance.put(e.getWord(), e.getWeight());
			for (int i = 0; i < arr.length; i++) 
				if (arr[i].equals(word1)) 
					arr[i] = null;
			
			for (int i = 1; i < arr.length; i++)
			{
				Integer temp = new Integer(Integer.MAX_VALUE - 2000); 
				int way = 0;
				for (int j = 0; j < arr.length; j++) {
					if (arr[j] != null) {
						if ((distance.get(arr[j]).compareTo(temp)) < 0) {
							temp = distance.get(arr[j]);
							way = j;
						}
					}
				}
				String propath = arr[way];
				arr[way] = null;
				
				if (head.get(propath) != null) {
					for (edgeNode e : head.get(propath)) {	
						boolean find = false;
						for (String s : arr) {
							if (s!= null && s.equals(e.getWord())) 
								find = true;
						}
						if (find ) {//if e in arr
							Integer sum = new Integer(distance.get(propath) + e.getWeight());
							if (sum.compareTo(distance.get(e.getWord())) <0) {
								distance.put(e.getWord(), sum);
								path.put(e.getWord(), propath);
							}
						}
					}
				}
			}
			String wordTemp = word2;
			String shortestPath = "";
			while (wordTemp.equals(word1) == false) {
				shortestPath = " " + wordTemp + shortestPath ;
				wordTemp = path.get(wordTemp);
			}
			shortestPath = word1 + shortestPath;
			if (distance.get(word2) < Integer.MAX_VALUE - 2000){
				shortsum = distance.get(word2);
				return shortestPath;
			}
			else 
				return "No path";
		} catch (java.lang.NullPointerException nullPointerException) {
			// TODO: handle exception
			return "No path";
		}
		
	}
	public  String edgeStart;
	public  String edgeEnd;
	public  String path;
	public  Map<String, ArrayList<String>> visited;
	public  boolean flag;
	public boolean randomWalkAssist() {
		if (head.get(edgeStart) == null) {
			path = path + edgeStart;
			return true;//something to stop
			//break;
		}
		else{
			int randNext = (int)(Math.random()*(head.get(edgeStart).size()));
			edgeEnd = head.get(edgeStart).get(randNext).getWord();
			path = path + edgeStart + " ";
			if(visited.get(edgeStart) == null){
				ArrayList<String> arr = new ArrayList<String>();
				arr.add(edgeEnd);
				visited.put(edgeStart, arr);
				edgeStart = edgeEnd;
			}
			else{	
				for (String e : visited.get(edgeStart)) 
					if(e.equals(edgeEnd)){
						flag = true;
						return true;
						//break walk;//return something
					}
				visited.get(edgeStart).add(edgeEnd);
				edgeStart = edgeEnd;
				
			}
		}
		return false;
	}
	public volatile boolean exit = false;
	public String randomWalk() {
		if (head.isEmpty())
			return null;
		String[] keyArr = new String[head.keySet().size()];
		keyArr = head.keySet().toArray(keyArr);
		visited = new HashMap<String,ArrayList<String>>();
		int randStart = (int)(Math.random()*(keyArr.length));
		edgeStart = keyArr[randStart];
		edgeEnd = null;
		path = "";
		flag = false;
		//walk:while(true)
		//{
		//	
		//}
		Runnable assist = () -> {
			boolean needEnd = false;
			while (!exit&&!needEnd) {
				needEnd = randomWalkAssist();
			}
			if(flag == true && edgeEnd != null)path = path + edgeEnd;
		};
		Thread thread = new Thread(assist);
		thread.start();
//		for(int i = 0; i < 10000; i++){}
//		exit = true;
		try {
			thread.join(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return path;
	}
}
