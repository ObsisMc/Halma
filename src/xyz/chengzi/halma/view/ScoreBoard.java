package xyz.chengzi.halma.view;

import xyz.chengzi.halma.Frame.EmptyFrame;
import xyz.chengzi.halma.Frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreBoard extends JDialog {
    ScoreBoard self=this;

    public ScoreBoard(MainFrame mainFrame) {
        super(mainFrame,"Scoreboard",false);

        setUndecorated(true);
        setSize(400,530);
        setLayout(null);
        setIconImage((new ImageIcon("image/titleicon.png")).getImage());
        setVisible(true);


        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);
        setLocationRelativeTo(mainFrame);

        JLayeredPane base=new JLayeredPane();
        add(base);
        base.setBounds(0,0,400,530);

        //标题
        JLabel title=new JLabel("Scoreboard");
        title.setBounds(153,16,200,20);
        title.setFont(new Font(Font.DIALOG, Font.BOLD, 20) );
        base.add(title,new Integer(5));


//背景
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon("image/scoreboard1.png"));
        background.setBounds(0, 0, 400, 530);
        base.add(background,new Integer(0));

//排名榜
        //读取积分存档
        File files = new File("save/scoreboard");
        ArrayList<String> savename = new ArrayList<>();
        for (String s : files.list()) {
            if (s.endsWith("txt")) savename.add(s.substring(0, s.lastIndexOf(".")));
        }
        //算积分,二人游戏第一名1分第二名0分，四人从一到四为3，2，1，0
        int[] pointlist = new int[savename.size()];
        int[][] userscore=new int[pointlist.length][4];//每一行记录各个玩家在不同模式中的获胜情况


        for (int j = 0; j < pointlist.length; j++) {
            int point = 0;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(String.format("save/scoreboard/%s.txt", savename.get(j))));
                int i = 0;
                String line;
                while (i < 4) {
                    line = reader.readLine();
                    if (i == 0) {
                        if (!(line == null)) {
                            if (Integer.parseInt(line) == 1) {
                                point += 1*Integer.parseInt(line);
                                userscore[j][i]=Integer.parseInt(line);
                            }
                        }
                    } else if (i >= 1) {
                        if (!(line == null)) {
                                point += (4 - i)*Integer.parseInt(line);
                                userscore[j][i]=Integer.parseInt(line);
                        }
                    }
                    i++;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pointlist[j] = point;
        }


//用户显示
        int width=330;
        int height=65;
        JLabel[] ranklist = new JLabel[savename.size()];
        for (int i = 0; i < ranklist.length; i++) {
            ranklist[i] = new JLabel();
//用户名,积分
            JLabel rank = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon i = new ImageIcon("image/user.png");
                    g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                }
            };
            rank.setBounds(0, 0, 65, height);
            ranklist[i].add(rank);

            JLabel namep = new JLabel();
            namep.setLayout(new BorderLayout());
            JLabel namepanel = new JLabel(String.format("%s         score: %s", savename.get(i),pointlist[i]), JLabel.CENTER);
            namep.add(namepanel, BorderLayout.CENTER);
            namepanel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            namep.setBounds(0, 0, 260, height);
            ranklist[i].add(namep);
            namep.setBounds(70,0,260,height);

            int i1=i;
            ranklist[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    JOptionPane.showMessageDialog(self,String.format("               %s's record\n" +
                                                                     "Two-player mode: first place %d times\n" +
                                                                     "Four-player mode: first place %d times\n" +
                                                                      "                                   second place %d times\n" +
                                                                     "                                   third place %d times"
                    ,savename.get(i1),userscore[i1][0],userscore[i1][1],userscore[i1][2],userscore[i1][3]),"Record",-1);
                }
            });

        }
//面板
        JPanel loadPanel = new JPanel();
        loadPanel.setPreferredSize(new Dimension(width, height * ranklist.length));
        loadPanel.setBounds(25, 0, width + 20, height * ranklist.length);
        loadPanel.setOpaque(false);
        FlowLayout f=(FlowLayout) loadPanel.getLayout();
        f.setVgap(0);//设置组件垂直间距为零
        for (int i = 0; i < ranklist.length; i++) {
            loadPanel.add(ranklist[i]);
            ranklist[i].setPreferredSize(new Dimension(width, height));
            int i1 = i;//这样才能把i传进监听器
            ranklist[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    ranklist[i1].setBorder(BorderFactory.createLineBorder(new Color(66, 132, 232), 4));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    ranklist[i1].setBorder(null);
                }
            });
        }
//滚动面板
        JScrollPane js = new JScrollPane();
        js.getViewport().setOpaque(false);
        js.setOpaque(false);

        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        js.setBorder(null);
        js.setViewportView(loadPanel);

        base.add(js,new Integer(5));
        js.setBounds(25, 85, 350, 400);

//关闭窗口
        JLabel close=new JLabel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawLine(6,6,26,26);
                 g.drawLine(26,6,6,26);

            }
        };
        base.add(close,new Integer(5));
        close.setBounds(348,54,32,32);
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                self.dispose();
//                mainFrame.setEnabled(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                close.setOpaque(true);
                close.setBackground(new Color(255, 9, 12, 166));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                close.setOpaque(false);
                close.setBackground(null);
            }
        });

    }

}
