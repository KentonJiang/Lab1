package testpair;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;


public class TestDemo {
  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      final SimpleFrame frame = new SimpleFrame();
      frame.setTitle("TestDemo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
    });
  }
}

/**
* 
*
* @author student
*/

class SimpleFrame extends JFrame {
  JTextField textField, textField1, textField2, textField3, textField4;
  String fname;

  public SimpleFrame() {
    // 获取分辨率
    final Toolkit kit = Toolkit.getDefaultToolkit();
    final Dimension screenSize = kit.getScreenSize();
    final int screenHeight = screenSize.height;
    final int screenWidth = screenSize.width;
    // 设置框架长宽及位置
    setSize(screenWidth * 3 / 4, screenHeight * 2 / 3);
    setLocationByPlatform(true);
    // 添加按钮和文本框
    final JPanel northpanel = new JPanel();
    northpanel.add(new JLabel("文件名:"));
    textField = new JTextField(20);
    northpanel.add(textField);
    final JButton scanButton = new JButton("浏览");
    final JButton okButton = new JButton("读取");
    northpanel.add(scanButton);
    northpanel.add(okButton);
    add(northpanel, BorderLayout.NORTH);

    final JPanel southPanel = new JPanel();

    add(southPanel, BorderLayout.SOUTH);

    final JLabel centerPanel = new JLabel();
    final JScrollPane scollPane1 = new JScrollPane(centerPanel);
    add(scollPane1, BorderLayout.WEST);

    final JTextArea textArea = new JTextArea(8, 40);
    final JScrollPane scollPane = new JScrollPane(textArea);
    add(scollPane, BorderLayout.EAST);
    // 设置监听
    final ScanAction scanAction = new ScanAction();
    scanButton.addActionListener(scanAction);

    okButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        southPanel.removeAll();
        final Graph graph = new Graph();
        try {
          graph.createGraph(textField.getText());
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        final JButton showButton = new JButton("展示有向图");
        final JButton spButton = new JButton("最短路径查询");
        final JButton rdButton = new JButton("随机游走");
        final JButton qbButton = new JButton("查询桥接词");
        final JButton gtButton = new JButton("生成新文本");
        final JButton stopButton = new JButton("停止游走");
        southPanel.add(showButton);
        southPanel.add(spButton);
        southPanel.add(rdButton);
        southPanel.add(stopButton);
        southPanel.add(qbButton);
        southPanel.add(gtButton);
        pack();
        showButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {

            centerPanel.setIcon(new ImageIcon((System.getProperty("user.dir") + "/graph01.png")));
            pack();
          }
        });
        qbButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            northpanel.add(new JLabel("word1:"));
            textField1 = new JTextField(8);
            northpanel.add(textField1);

            northpanel.add(new JLabel("word2:"));
            textField2 = new JTextField(8);
            northpanel.add(textField2);

            final JButton obutton = new JButton("确定");
            northpanel.add(obutton);

            validate();
            obutton.addActionListener(new ActionListener() {

              @Override
              public void actionPerformed(ActionEvent event) {
                final String word1 = textField1.getText();
                final String word2 = textField2.getText();

                textArea.append(graph.queryBridgeWords(word1, word2) + "\n");
              }
            });
          }
        });
        spButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            final JFrame spFrame = new JFrame("计算最短路径");
            spFrame.setVisible(true);
            spFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            final Toolkit kit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = kit.getScreenSize();
            final int screenHeight = screenSize.height;
            final int screenWidth = screenSize.width;
            // 设置框架长宽及位置
            // spFrame.setSize(900,900);

            final JPanel spPanel = new JPanel();

            spPanel.add(new JLabel("word1:"));
            textField3 = new JTextField(8);
            spPanel.add(textField3);

            spPanel.add(new JLabel("word2:"));
            textField4 = new JTextField(8);
            spPanel.add(textField4);

            final JButton sbutton = new JButton("确定");
            spPanel.add(sbutton);

            spFrame.add(spPanel, BorderLayout.NORTH);
            final JLabel spicon = new JLabel();
            final JScrollPane spscollPane1 = new JScrollPane(spicon);
            // spscollPane1.setSize(2560, 2560);
            // spicon.setPreferredSize(new Dimension(2560, 2560));
            spFrame.add(spscollPane1, BorderLayout.WEST);
            spFrame.pack();
            sbutton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent event) {
                // TODO Auto-generated method stub
                final String aword = textField3.getText();
                final String bword = textField4.getText();
                String result;
                result = graph.calcShortestpath(aword, bword);
                if (result.equals("No path")) {
                  textArea.removeAll();
                  textArea.append("这俩单词之间没有最短路径\n");
                } else {
                  String dot = "";
                  final String[] results = result.split("\\s+");
                  final int nlonger = results.length;
                  Iterator<Map.Entry<String, ArrayList<EdgeNode>>> 
                      i = graph.getHead().entrySet().iterator();
                  while (i.hasNext()) {
                    final Map.Entry<String, ArrayList<EdgeNode>> ahead = 
                        (Map.Entry<String, ArrayList<EdgeNode>>) i.next();
                    // System.out.print(ahead.getKey()+":");

                    final ArrayList<EdgeNode> mlonger = (ArrayList<EdgeNode>) ahead.getValue();
                    if (mlonger != null) {
                      final Iterator<EdgeNode> j = mlonger.iterator();
                      while (j.hasNext()) {
                        // System.out.print(j.next().getWord() + " ");
                        final EdgeNode tlonger = j.next();
                        boolean findEdge = false;
                        for (int k = 0; k < nlonger - 1; k++) {
                          if (results[k].equals(ahead.getKey()) 
                              && results[k + 1].equals(tlonger.getWord())) {
                            findEdge = true;
                          }
                        }
                        if (findEdge) {
                          dot = dot + ahead.getKey() + "->" + tlonger.getWord() 
                              + "[label=\"" + tlonger.getWeight()
                              + "\",color = red];";
                          //太长了 不改了
                        } else {
                          dot = dot + ahead.getKey() + "->" + tlonger.getWord() 
                              + "[label=\"" + tlonger.getWeight() + "\"];";
                        }

                      }
                    }
                  }
                  Graph.createDotGraph(dot, "graph_shortest_road");
                  final String nowpath = System.getProperty("user.dir");
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
          public void actionPerformed(ActionEvent event) {
            final JFrame gtFrame = new JFrame("生成新文本");
            gtFrame.setVisible(true);
            gtFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            final Toolkit kit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = kit.getScreenSize();
            final int screenHeight = screenSize.height;
            final int screenWidth = screenSize.width;
            // 设置框架长宽及位置
            gtFrame.setSize(screenWidth * 2 / 3, screenHeight * 1 / 2);

            final JPanel gtnPanel = new JPanel();
            gtnPanel.add(new JLabel("输入文本："));
            final JTextField gtnText = new JTextField(50);
            gtnPanel.add(gtnText);
            final JButton gtnButton = new JButton("生成");
            gtnPanel.add(gtnButton);
            gtFrame.add(gtnPanel, BorderLayout.NORTH);

            final JPanel gtsPanel = new JPanel();
            gtsPanel.add(new JLabel("新文本"));
            final JTextArea gtsText = new JTextArea(20, 50);
            gtsPanel.add(gtsText);
            gtFrame.add(gtsPanel, BorderLayout.SOUTH);

            gtnButton.addActionListener(new ActionListener() {

              @Override
              public void actionPerformed(ActionEvent event) {
                final String gtnString = gtnText.getText();
                final String gtsString = graph.generateNewText(gtnString);
                gtsText.setText(gtsString);
                validate();
              }
            });

          }
        });

        rdButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(final ActionEvent eventlonger) {
            graph.exit = false;
            validate();
            final Runnable traversal = () -> {
              String tpath = graph.randomWalk();
              try {
                PrintWriter olonger = new PrintWriter("random.txt");
                olonger.print(tpath);
                // o.write(tpath);
                olonger.close();
              } catch (FileNotFoundException e1) {
                e1.printStackTrace();
              }
            };
            final Thread thread = new Thread(traversal);
            thread.start();
            stopButton.addActionListener(event -> {
              graph.exit = true;
            });
            try {
              thread.join();
            } catch (InterruptedException e1) {
              e1.printStackTrace();
            }
            final String nowDir = System.getProperty("user.dir");
            // 在屏幕上显示已经写入到JVM运行目录下文件 random.txt
            textArea.removeAll();
            textArea.append(nowDir + "游走结果已正常写入此地址\n");
          }
        });
      }
    });
  }

  private class ScanAction implements ActionListener {
    final private JFileChooser chooser = new JFileChooser();

    public void actionPerformed(ActionEvent event) {
      final FileSystemView fsv = FileSystemView.getFileSystemView();
      chooser.setCurrentDirectory(fsv.getHomeDirectory());
      final int result = chooser.showOpenDialog(SimpleFrame.this);
      if (result == JFileChooser.APPROVE_OPTION) {
        String name = chooser.getSelectedFile().getPath();
        name = name.replaceAll("\\\\", "/");
        textField.setText(name);
        fname = name.substring(name.lastIndexOf('/') + 1, name.lastIndexOf('.'));
        //;
      }
    }
  }
}
