package xyz.chengzi.halma.view;

import xyz.chengzi.halma.Frame.MainFrame;
import xyz.chengzi.halma.controller.GameController;
import xyz.chengzi.halma.model.ChessBoard;
import xyz.chengzi.halma.model.PlayerMode;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class TwoPlayerStartPanel extends PlayerMode {
    JLayeredPane self = this;


    public TwoPlayerStartPanel(MainFrame jFrame) {
        jFrame.add(this);
        setLayout(null);
        //音乐
        File m = new File("music/dp.wav");
        URI uri = m.toURI();
        URL url = null;  //解析地址
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        AudioClip aau = Applet.newAudioClip(url);
        setAau(aau);
        aau.loop();

//读取存档区
// 读取txt文档
        //删除null文件
        File[] files = (new File("save/twoplayer")).listFiles();
        for (File f : files) {
            if (f.getName().equals("null.txt")) {
                f.delete();
            }
        }

        File preload = new File("save/twoplayer");
        ArrayList<String> savename = new ArrayList<>();
        for (String s : preload.list()) {
            if (s.endsWith("txt")) savename.add(s);
        }
        setSavenumber2(savename.size());

        //创建新建和读取存档按钮

        JLabel[] savebutton = new JLabel[savename.size() + 1];
        int width = 300;
        int height = 100;
        for (int i = 0; i < savebutton.length; i++) {
            savebutton[i] = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon i = new ImageIcon("image/savepillar.png");
                    g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                }
            };

        }
        //新建存档按钮
        savebutton[0].setPreferredSize(new Dimension(width, height));
        savebutton[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String name = JOptionPane.showInputDialog("Please input the name of your save:");
                if (name != null) {
                    ChooseCharacter c = new ChooseCharacter(jFrame, 2, (PlayerMode) self, name);
                    jFrame.remove(self);
                    jFrame.setVisible(true);
                    jFrame.repaint();
                }
            }
        });
        JLabel plus = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon i = new ImageIcon("image/plus.png");
                g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
            }
        };

        savebutton[0].add(plus);
        plus.setBounds(125, 23, 50, 50);
        //读取存档按钮
        String[] fs = (new File("save/twoplayer")).list();

        for (int i = 1; i <= savename.size(); i++) {
            int index = i;
            String txtname = fs[index - 1];
            String name = txtname.substring(0, txtname.lastIndexOf("."));

            savebutton[i].setPreferredSize(new Dimension(width, height));
            JLabel namep = new JLabel();
            namep.setLayout(new BorderLayout());
            JLabel namepanel = new JLabel(name, JLabel.CENTER);
            namep.add(namepanel, BorderLayout.CENTER);
            namepanel.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
            namep.setBounds(0, 0, 300, 100);
            savebutton[i].add(namep);

            savebutton[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    ChooseCharacter chooseCharacter = new ChooseCharacter(2, name);
                    ChessBoard chessBoard = new ChessBoard(16, chooseCharacter);
                    //判断存档是否有效
                    GameController controlvalid = new GameController(chooseCharacter, chessBoard);
                    if (controlvalid.isIsvalid()) {
                        ChessBoardComponent chessBoardComponent = new ChessBoardComponent(480, 16);

                        GameController controller = null;
                        try {
                            controller = new GameController(chooseCharacter, chessBoardComponent, chessBoard);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        GameFrame mainFrame = new GameFrame(controller, chessBoardComponent, chooseCharacter);
                        controller.setGameFrame(mainFrame);
                        mainFrame.setVisible(true);
                        jFrame.dispose();
                        aau.stop();
                        //打开就获胜时显示
                        if (controller.isOver()) {
                            String first = null;
                            String second = null;
                            if (chooseCharacter.getPlayer1().getRank() == 1) {
                                first = chooseCharacter.getPlayer1().getPlayername();
                                second = chooseCharacter.getPlayer2().getPlayername();
                            } else if (chooseCharacter.getPlayer1().getRank() == 2) {
                                first = chooseCharacter.getPlayer2().getPlayername();
                                second = chooseCharacter.getPlayer1().getPlayername();
                            }
                            JOptionPane.showMessageDialog(chessBoardComponent, String.format("Game is over\n No.1: %s\n No.2: %s",
                                    first, second));
                        }
                    }
                }
            });
        }

        //按钮面板
        JPanel loadPanel = new JPanel();
        loadPanel.setPreferredSize(new Dimension(width, height * (savebutton.length)));
        loadPanel.setBounds(0, 0, width + 30, height * (savebutton.length));
        loadPanel.setOpaque(false);
        FlowLayout f=(FlowLayout) loadPanel.getLayout();
        f.setVgap(0);//设置组件垂直间距为零
        for (int i = 0; i < savebutton.length; i++) {
            loadPanel.add(savebutton[i]);
            savebutton[i].setBounds(0, height * i, width, height);
            int i1 = i;//这样才能把i传进监听器
            savebutton[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    savebutton[i1].setBorder(BorderFactory.createLineBorder(new Color(232, 184, 49), 4));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    savebutton[i1].setBorder(null);
                }
            });
        }

        JScrollPane js = new JScrollPane();
        js.getViewport().setOpaque(false);
        js.setOpaque(false);
        js.setBounds(200, 200, 350, 400);
        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        js.setBorder(null);
        js.setViewportView(loadPanel);

        add(js);


        //返回
        JLabel back = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon i = new ImageIcon("image/back.png");
                Image ic = i.getImage();
                g.drawImage(ic, 0, 0, getWidth(), getHeight(), i.getImageObserver());
            }
        };
        back.setBounds(10, 10, 40, 40);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                LocalGamePanel localGamePanel = new LocalGamePanel(jFrame);
                jFrame.add(localGamePanel);
                jFrame.remove(self);
                jFrame.repaint();
                jFrame.revalidate();
                aau.stop();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                back.setBounds(6, 6, 48, 48);

                File f = new File("music/click.wav");
                URI uri = f.toURI();
                URL url = null;  //解析地址
                try {
                    url = uri.toURL();
                } catch (MalformedURLException er) {
                    er.printStackTrace();
                }
                AudioClip cm = Applet.newAudioClip(url);
                cm.play();

                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                back.setBounds(10, 10, 40, 40);
                repaint();
            }
        });

        add(back);

        //装饰
        //寺庙
        JPanel jp = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                ImageIcon bg = new ImageIcon("image/templeps.png");
                Image i = bg.getImage();
                g.drawImage(i, 0, 0, getWidth(), getHeight() + 20, null);
            }
        };

        add(jp);
        jp.setBounds(0, 0, 750, 200);
        //柱子
        JLabel pillar = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon exiticon = new ImageIcon("image/pillar.png");
                g.drawImage(exiticon.getImage(), 0, 0, getWidth(), getHeight(), exiticon.getImageObserver());
            }
        };

        add(pillar, new Integer(5));
        pillar.setBounds(30, 200, 150, 500);

        JLabel pillar2 = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon exiticon = new ImageIcon("image/pillar.png");
                g.drawImage(exiticon.getImage(), 0, 0, getWidth(), getHeight(), exiticon.getImageObserver());
            }
        };

        add(pillar2, new Integer(5));
        pillar2.setBounds(580, 200, 150, 500);
        //地图名
        JLabel partheno = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon exiticon = new ImageIcon("image/partheno.png");
                g.drawImage(exiticon.getImage(), 0, 0, getWidth(), getHeight(), exiticon.getImageObserver());
            }
        };

        add(partheno, new Integer(5));
        partheno.setBounds(130, 600, 500, 80);


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon map = new ImageIcon("image/4ppurebg.png");
        Image mapimage = map.getImage();
        g.drawImage(mapimage, 0, 0, getWidth(), getHeight(), self);
    }


    public JLayeredPane getSelf() {
        return self;
    }

    public void setSelf(JLayeredPane self) {
        this.self = self;
    }


}