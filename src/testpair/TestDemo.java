package testpair;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;
import javax.swing.filechooser.*;

public class TestDemo{
	public static void main(String[] args) {
		EventQueue.invokeLater(()->
		{
			SimpleFrame frame = new SimpleFrame();
			frame.setTitle("TestDemo");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
}
//在塞四上改的
class SimpleFrame extends JFrame {
	JTextField textField, textField1, textField2, textField3, textField4;
	String fName;
	public SimpleFrame()
	{
		//获取分辨率
		//change two
			Toolkit kit= Toolkit.getDefaultToolkit();
			Dimension screenSize=kit.getScreenSize();
			int screenHeight=screenSize.height;
			int screenWidth = screenSize.width;
		//设置框架长宽及位置
			setSize(screenWidth*3/4,screenHeight*2/3);
			setLocationByPlatform(true);
		//添加按钮和文本框
			JPanel northpanel = new JPanel();
			northpanel.add(new JLabel("文件名:"));
			textField = new JTextField(20);
			northpanel.add(textField);
			JButton scanButton = new JButton("浏览");
			JButton okButton = new JButton("读取");
			northpanel.add(scanButton);
			northpanel.add(okButton);
			add(northpanel,BorderLayout.NORTH);
			
			JPanel southPanel = new JPanel();
			
			add(southPanel,BorderLayout.SOUTH);
			
			JLabel centerPanel = new JLabel();
			JScrollPane scollPane1=new JScrollPane(centerPanel);
			add(scollPane1,BorderLayout.WEST);
			
			JTextArea textArea=new JTextArea(8,40);
			JScrollPane scollPane=new JScrollPane(textArea);
			add(scollPane,BorderLayout.EAST);
		//设置监听
			ScanAction scanAction = new ScanAction();
			scanButton.addActionListener(scanAction);
			
			okButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					southPanel.removeAll();
					Graph graph = new Graph();
					try {
						graph.CreateGraph(textField.getText());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					JButton showButton= new JButton("展示有向图");
					JButton spButton = new JButton("最短路径查询");
					JButton rdButton = new JButton("随机游走");
					JButton qbButton = new JButton("查询桥接词");
					JButton gtButton = new JButton("生成新文本");
					JButton stopButton = new JButton("停止游走");
					southPanel.add(showButton);
					southPanel.add(spButton);
					southPanel.add(rdButton);
					southPanel.add(stopButton);
					southPanel.add(qbButton);
					southPanel.add(gtButton);
					pack();
					showButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
					
							centerPanel.setIcon(new ImageIcon((System.getProperty("user.dir") + "/graph01.png")));
							pack();
						}
					});
					qbButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							northpanel.add(new JLabel("word1:"));
							textField1 = new JTextField(8);
							northpanel.add(textField1);
							
							northpanel.add(new JLabel("word2:"));
							textField2 = new JTextField(8);
							northpanel.add(textField2);
							
							JButton oButton = new JButton("确定");
							northpanel.add(oButton);
							
							validate();
							oButton.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									String word1 = textField1.getText();
									String word2 = textField2.getText();
									
									textArea.append(graph.queryBridgeWords(word1, word2) + "\n");
								}
							});
						}
					});
					spButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							JFrame spFrame = new JFrame("计算最短路径");
							spFrame.setVisible(true);
							spFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
							
							Toolkit kit= Toolkit.getDefaultToolkit();
							Dimension screenSize=kit.getScreenSize();
							int screenHeight=screenSize.height;
							int screenWidth = screenSize.width;
						//设置框架长宽及位置
							//spFrame.setSize(900,900);
							
							JPanel spPanel = new JPanel();
							
							spPanel.add(new JLabel("word1:"));
							textField3 = new JTextField(8);
							spPanel.add(textField3);
							
							spPanel.add(new JLabel("word2:"));
							textField4 = new JTextField(8);
							spPanel.add(textField4);
							
							JButton sButton = new JButton("确定");
							spPanel.add(sButton);
							
							spFrame.add(spPanel,BorderLayout.NORTH);
							JLabel spicon = new JLabel();
							JScrollPane spscollPane1=new JScrollPane(spicon);
							//spscollPane1.setSize(2560, 2560);
							//spicon.setPreferredSize(new Dimension(2560, 2560));
							spFrame.add(spscollPane1,BorderLayout.WEST);
							spFrame.pack();
							sButton.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
									String aWord = textField3.getText();
									String bWord = textField4.getText();
									String result;
									result = graph.calcShortestPath(aWord, bWord);
									if (result.equals("No path")) {
										textArea.removeAll();
										textArea.append("这俩单词之间没有最短路径\n");
									}
									else {
										String dot = "";
										String[] results = result.split("\\s+");
										int n = results.length;
										Iterator<Map.Entry<String, ArrayList<edgeNode>>> i = graph.getHead().entrySet().iterator();
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
													boolean findEdge = false;
													for(int k = 0; k < n - 1; k++)
													{
														if (results[k].equals(ahead.getKey()) && results[k + 1] .equals(t.getWord())) {
															findEdge = true;
														}
													}
													if (findEdge) {
														dot = dot + ahead.getKey() + "->" + t.getWord()+"[label=\""+t.getWeight()+"\",color = red];";
													} else {
														dot = dot + ahead.getKey() + "->" + t.getWord()+"[label=\""+t.getWeight()+"\"];";
													}
													
												}
											}	
										}
										Graph.createDotGraph(dot, "graph_shortest_road");
										String nowpath = System.getProperty("user.dir");
										spicon.setIcon(new ImageIcon(nowpath + "/graph_shortest_road.png"));
										textArea.append(graph.getshortsum());
										spFrame.pack();
									}
								}
							});
							
							
						}
					});
					gtButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							JFrame gtFrame = new JFrame("生成新文本");
							gtFrame.setVisible(true);
							gtFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
							
							Toolkit kit= Toolkit.getDefaultToolkit();
							Dimension screenSize=kit.getScreenSize();
							int screenHeight=screenSize.height;
							int screenWidth = screenSize.width;
						//设置框架长宽及位置
							gtFrame.setSize(screenWidth*2/3,screenHeight*1/2);
							
							
							JPanel gtnPanel = new JPanel();
							gtnPanel.add(new JLabel("输入文本："));
							JTextField gtnText = new JTextField(50);
							gtnPanel.add(gtnText);
							JButton gtnButton = new JButton("生成");
							gtnPanel.add(gtnButton);
							gtFrame.add(gtnPanel,BorderLayout.NORTH);
							
							JPanel gtsPanel = new JPanel();
							gtsPanel.add(new JLabel("新文本"));
							JTextArea gtsText = new JTextArea(20,50);
							gtsPanel.add(gtsText);
							gtFrame.add(gtsPanel, BorderLayout.SOUTH);
							
							gtnButton.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									String gtnString = gtnText.getText();
									String gtsString = graph.generateNewText(gtnString);
									gtsText.setText(gtsString);
									validate();
								}
							});
							
						}
					});
					
					rdButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							graph.exit = false;
							validate();
							Runnable traversal = () ->{
								String tPath = graph.randomWalk();
								try {
									PrintWriter o = new PrintWriter("random.txt");
									o.print(tPath);
									//o.write(tPath);
									o.close();
								} catch (FileNotFoundException e1) {
									e1.printStackTrace();
								}
							};
							Thread thread = new Thread(traversal);
							thread.start();
							stopButton.addActionListener(event -> {
								graph.exit = true;
							});
							try {
								thread.join();
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							String nowDir = System.getProperty("user.dir");
							//在屏幕上显示已经写入到JVM运行目录下文件 random.txt
							textArea.removeAll();
							textArea.append(nowDir + "游走结果已正常写入此地址\n");
						}
					});
				}
			});	
	}
	private class ScanAction implements ActionListener
	{
		private JFileChooser chooser=new JFileChooser();
		public void actionPerformed(ActionEvent event)
		{
			FileSystemView fsv = FileSystemView.getFileSystemView(); 
			chooser.setCurrentDirectory(fsv.getHomeDirectory());
			int result=chooser.showOpenDialog(SimpleFrame.this);
			if(result==JFileChooser.APPROVE_OPTION)
			{
				String name=chooser.getSelectedFile().getPath();
				name=name.replaceAll("\\\\", "/");
				textField.setText(name);
				fName =  name.substring(name.lastIndexOf("/")+1, name.lastIndexOf("."));;
			}
		}
	}
}
