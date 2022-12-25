package xyz.chengzi.halma.view;


import xyz.chengzi.halma.Frame.MainFrame;
import xyz.chengzi.halma.controller.GameController;
import xyz.chengzi.halma.listener.CharacterListener;
import xyz.chengzi.halma.model.ChessBoard;
import xyz.chengzi.halma.model.PlayerMode;
import xyz.chengzi.halma.model.Character;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ChooseCharacter extends JLayeredPane {
    private int playernumber;
    JLayeredPane self = this;
    PlayerMode playerMode;
    MainFrame jframe;
    Player1 player1;
    Player2 player2;
    Player3 player3;
    Player4 player4;
    String savename;

    public ChooseCharacter(int playernumber, String savename) {
        this.playernumber = playernumber;
        this.savename = savename;
    }


    //选角色
    public ChooseCharacter(MainFrame jf, int playernumber, PlayerMode playerMode, String savename) {
        jframe = jf;
        this.playernumber = playernumber;
        this.playerMode = playerMode;
        this.savename = savename;

        jf.add(this);

        //二人游戏
        if (playernumber == 2) {
            //中央大寺庙
            JLabel temple = new JLabel();
            ImageIcon t = new ImageIcon("image/temple0_8.png");
            temple.setIcon(t);
            add(temple, new Integer(4));
            temple.setBounds(20, 180, 700, 300);

            //玩家选择角色
            player1 = new Player1();
            player2 = new Player2();
            this.player1 = player1;
            this.player2 = player2;
            add(player1, new Integer(6));
            add(player2, new Integer(6));
            player1.setBounds(10, 280, 140, 140);
            player2.setBounds(610, 280, 140, 140);
            JLabel j1 = new JLabel("Player 1");
            JLabel j2 = new JLabel("Player 2");
            player1.add(j1, new Integer(0));
            player2.add(j2, new Integer(0));
            j1.setBounds(10, 0, 140, 140);
            j2.setBounds(40, 0, 140, 140);

            //人物
            Character zeus = new Zeus(this);
            Poseidon po = new Poseidon(this);
            Artemis ar = new Artemis(this);
            Athena at = new Athena(this);
            Hades ha = new Hades(this);
            Medusa me = new Medusa(this);
            CharacterListener[] characterlist = new CharacterListener[6];
            characterlist[0] = zeus;
            characterlist[1] = po;
            characterlist[2] = me;
            characterlist[3] = ar;
            characterlist[4] = at;
            characterlist[5] = ha;
            for (CharacterListener c : characterlist) {
                c.clickCharacterPanel();
            }

            //确认按钮

            JLabel confirm1 = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (player1.getColor() == null) {
                        ImageIcon i = new ImageIcon("image/confirm.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (player1.getColor() != null) {
                        if (player1.getCharacter().isConfirmed()) {
                            ImageIcon i = new ImageIcon("image/cancel.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        } else {
                            ImageIcon i = new ImageIcon("image/confirm.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        }
                    }

                }
            };
            JLabel confirm2 = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (player2.getColor() == null) {
                        ImageIcon i = new ImageIcon("image/confirm.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (player2.getColor() != null) {
                        if (player2.getCharacter().isConfirmed()) {
                            ImageIcon i = new ImageIcon("image/cancel.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        } else {
                            ImageIcon i = new ImageIcon("image/confirm.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        }
                    }

                }
            };
//输入玩家名:但名字不能有空格（还没对此异常处理）
            JTextField player1name = new JTextField("Player1");
            JTextField player2name = new JTextField("Player2");
            add(player1name, new Integer(6));
            add(player2name, new Integer(6));
            player1name.setBounds(280, 150, 200, 45);
            player2name.setBounds(280, 500, 200, 45);


            add(confirm1, new Integer(6));
            add(confirm2, new Integer(6));
            confirm1.setBounds(500, 150, 45, 45);
            confirm2.setBounds(500, 500, 45, 45);
            confirm1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (player1.getColor() != null) {
                        if (!player1.getCharacter().isConfirmed()) {
                            confirm1.repaint();
                            player1.getCharacter().setConfirmed(!player1.getCharacter().isConfirmed());
                            String playername = player1name.getText();
                            player1.setPlayername(playername);
                            player1name.setEnabled(false);
                            if (player2.getCharacter() != null) {
                                if (player2.getCharacter().isConfirmed()) {
                                    beginGame(jf);
                                    jframe.remove(self);
                                    jf.repaint();
                                    playerMode.getAau().stop();
                                }
                            }
                        } else {
                            player1.getCharacter().setConfirmed(!player1.getCharacter().isConfirmed());
                            confirm1.repaint();
                            player1name.setEnabled(true);
                        }
                    }
                }
            });
            confirm2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (player2.getColor() != null) {
                        if (!player2.getCharacter().isConfirmed()) {
                            confirm2.repaint();
                            player2.getCharacter().setConfirmed(!player2.getCharacter().isConfirmed());
                            String playername = player2name.getText();
                            player2.setPlayername(playername);
                            player2name.setEnabled(false);


                            if (player1.getCharacter() != null) {
                                if (player1.getCharacter().isConfirmed()) {
                                    beginGame(jf);
                                    jframe.remove(self);
                                    jf.repaint();
                                    playerMode.getAau().stop();
                                }
                            }
                        } else {
                            confirm2.repaint();
                            player2.getCharacter().setConfirmed(!player2.getCharacter().isConfirmed());
                            player2name.setEnabled(true);
                        }
                    }
                }
            });
        }

        //四人游戏
        if (playernumber == 4) {
            //玩家选择角色
            player1 = new Player1();
            player2 = new Player2();
            player3 = new Player3();
            player4 = new Player4();
            this.player1 = player1;
            this.player2 = player2;
            this.player3 = player3;
            this.player4 = player4;
            add(player1, new Integer(3));
            add(player2, new Integer(3));
            add(player3, new Integer(3));
            add(player4, new Integer(3));
            player1.setBounds(50, 280, 140, 140);
            player2.setBounds(320, 120, 140, 140);
            player3.setBounds(560, 280, 140, 140);
            player4.setBounds(320, 430, 140, 140);
            JLabel j1 = new JLabel("Player 1");
            JLabel j2 = new JLabel("Player 2");
            JLabel j3 = new JLabel("Player 3");
            JLabel j4 = new JLabel("Player 4");
            player1.add(j1, new Integer(0));
            player2.add(j2, new Integer(0));
            player3.add(j3, new Integer(0));
            player4.add(j4, new Integer(0));
            j1.setBounds(40, 0, 140, 140);
            j2.setBounds(40, 0, 140, 140);
            j3.setBounds(40, 0, 140, 140);
            j4.setBounds(40, 0, 140, 140);

            //人物
            Character zeus = new Zeus(this);
            Poseidon po = new Poseidon(this);
            Artemis ar = new Artemis(this);
            Athena at = new Athena(this);
            Hades ha = new Hades(this);
            Medusa me = new Medusa(this);
            CharacterListener[] characterlist = new CharacterListener[6];
            characterlist[0] = zeus;
            characterlist[1] = po;
            characterlist[2] = me;
            characterlist[3] = ar;
            characterlist[4] = at;
            characterlist[5] = ha;
            for (CharacterListener c : characterlist) {
                c.clickCharacterPanel();
            }

            //确认按钮

            JLabel confirm1 = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (player1.getColor() == null) {
                        ImageIcon i = new ImageIcon("image/confirm.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (player1.getColor() != null) {
                        if (player1.getCharacter().isConfirmed()) {
                            ImageIcon i = new ImageIcon("image/cancel.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        } else {
                            ImageIcon i = new ImageIcon("image/confirm.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        }
                    }

                }
            };
            JLabel confirm2 = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (player2.getColor() == null) {
                        ImageIcon i = new ImageIcon("image/confirm.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (player2.getColor() != null) {
                        if (player2.getCharacter().isConfirmed()) {
                            ImageIcon i = new ImageIcon("image/cancel.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        } else {
                            ImageIcon i = new ImageIcon("image/confirm.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        }
                    }

                }
            };
            JLabel confirm3 = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (player3.getColor() == null) {
                        ImageIcon i = new ImageIcon("image/confirm.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (player3.getColor() != null) {
                        if (player3.getCharacter().isConfirmed()) {
                            ImageIcon i = new ImageIcon("image/cancel.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        } else {
                            ImageIcon i = new ImageIcon("image/confirm.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        }
                    }

                }
            };
            JLabel confirm4 = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (player4.getColor() == null) {
                        ImageIcon i = new ImageIcon("image/confirm.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (player4.getColor() != null) {
                        if (player4.getCharacter().isConfirmed()) {
                            ImageIcon i = new ImageIcon("image/cancel.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        } else {
                            ImageIcon i = new ImageIcon("image/confirm.png");
                            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                        }
                    }

                }
            };
            //输入玩家名:但名字不能有空格（还没对此异常处理）
            JTextField player1name = new JTextField("Player1");
            JTextField player2name = new JTextField("Player2");
            JTextField player3name = new JTextField("Player3");
            JTextField player4name = new JTextField("Player4");
            add(player1name, new Integer(6));
            add(player2name, new Integer(6));
            add(player3name, new Integer(6));
            add(player4name, new Integer(6));
            player1name.setBounds(30, 160, 200, 45);
            player2name.setBounds(510, 160, 200, 45);
            player3name.setBounds(510, 460, 200, 45);
            player4name.setBounds(30, 460, 200, 45);

            add(confirm1, new Integer(6));
            add(confirm2, new Integer(6));
            add(confirm3, new Integer(6));
            add(confirm4, new Integer(6));
            confirm1.setBounds(100, 215, 45, 45);
            confirm2.setBounds(580,215,45,45);
            confirm3.setBounds(580, 515, 45, 45);
            confirm4.setBounds(100, 515, 45, 45);



            confirm1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (player1.getColor() != null) {
                        if (!player1.getCharacter().isConfirmed()) {
                            confirm1.repaint();
                            player1.getCharacter().setConfirmed(!player1.getCharacter().isConfirmed());
                            String playername = player1name.getText();
                            player1.setPlayername(playername);
                            player1name.setEnabled(false);
                            if (player2.getCharacter() != null&&player3.getCharacter() != null&&player4.getCharacter() != null) {
                                if (player2.getCharacter().isConfirmed()&&player3.getCharacter().isConfirmed()&&player4.getCharacter().isConfirmed()) {
                                    beginGame(jf);
                                    jframe.remove(self);
                                    jf.repaint();
                                    playerMode.getAau().stop();
                                }
                            }
                        } else {
                            player1.getCharacter().setConfirmed(!player1.getCharacter().isConfirmed());
                            confirm1.repaint();
                            player1name.setEnabled(true);
                        }
                    }
                }
            });
             confirm2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (player2.getColor() != null) {
                        if (!player2.getCharacter().isConfirmed()) {
                            confirm2.repaint();
                            player2.getCharacter().setConfirmed(!player2.getCharacter().isConfirmed());

                            String playername = player2name.getText();
                            player2.setPlayername(playername);
                            player2name.setEnabled(false);
                            if (player1.getCharacter() != null&&player3.getCharacter() != null&&player4.getCharacter() != null) {
                                if (player1.getCharacter().isConfirmed()&&player3.getCharacter().isConfirmed()&&player4.getCharacter().isConfirmed()) {
                                    beginGame(jf);
                                    jframe.remove(self);
                                    jf.repaint();
                                    playerMode.getAau().stop();
                                }
                            }
                        } else {
                            player2.getCharacter().setConfirmed(!player2.getCharacter().isConfirmed());
                            confirm2.repaint();
                            player2name.setEnabled(true);
                        }
                    }
                }
            });
             confirm3.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (player3.getColor() != null) {
                        if (!player3.getCharacter().isConfirmed()) {
                            confirm3.repaint();
                            player3.getCharacter().setConfirmed(!player3.getCharacter().isConfirmed());

                            String playername = player3name.getText();
                            player3.setPlayername(playername);
                            player3name.setEnabled(false);
                            if (player2.getCharacter() != null&&player1.getCharacter() != null&&player4.getCharacter() != null) {
                                if (player2.getCharacter().isConfirmed()&&player1.getCharacter().isConfirmed()&&player4.getCharacter().isConfirmed()) {
                                    beginGame(jf);
                                    jframe.remove(self);
                                    jf.repaint();
                                    playerMode.getAau().stop();
                                }
                            }
                        } else {
                            player3.getCharacter().setConfirmed(!player3.getCharacter().isConfirmed());
                            confirm1.repaint();
                            player3name.setEnabled(true);
                        }
                    }
                }
            });
             confirm4.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (player4.getColor() != null) {
                        if (!player4.getCharacter().isConfirmed()) {
                            confirm4.repaint();
                            player4.getCharacter().setConfirmed(!player4.getCharacter().isConfirmed());

                            String playername = player4name.getText();
                            player4.setPlayername(playername);
                            player4name.setEnabled(false);
                            if (player2.getCharacter() != null&&player3.getCharacter() != null&&player1.getCharacter() != null) {
                                if (player2.getCharacter().isConfirmed()&&player3.getCharacter().isConfirmed()&&player1.getCharacter().isConfirmed()) {
                                    beginGame(jf);
                                    jframe.remove(self);
                                    jf.repaint();
                                    playerMode.getAau().stop();
                                }
                            }
                        } else {
                            player4.getCharacter().setConfirmed(!player4.getCharacter().isConfirmed());
                            confirm1.repaint();
                            player4name.setEnabled(false);
                        }
                    }
                }
            });


        }


//返回localgame
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
                LocalGamePanel localGamePanel = new LocalGamePanel(jframe);
                jf.add(localGamePanel);
                jf.remove(self);
                jf.repaint();
                jf.revalidate();
                playerMode.getAau().stop();
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

    }

    public void beginGame(JFrame jf) {
        ChessBoardComponent chessBoardComponent = new ChessBoardComponent(480, 16);
        ChessBoard chessBoard = new ChessBoard(16, this);
        GameController controller = new GameController(chessBoardComponent, chessBoard, this);

        GameFrame mainFrame = new GameFrame(controller, chessBoardComponent, this);
        controller.setGameFrame(mainFrame);
        mainFrame.savename = savename;
        mainFrame.setVisible(true);
        jf.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (playernumber == 2) {
            ImageIcon i = new ImageIcon("image/4ppurebg.png");
            Image im = i.getImage();
            g.drawImage(im, 0, 0, getWidth(), getHeight(), self);
        } else if (playernumber == 4) {
            ImageIcon i = new ImageIcon("image/Santorini750730.png");
            Image im = i.getImage();
            g.drawImage(im, 0, 0, getWidth(), getHeight(), self);
        }
    }

    public Player1 getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player1 player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player2 player2) {
        this.player2 = player2;
    }

    public Player2 getPlayer2() {
        return player2;
    }

    public int getPlayernumber() {
        return playernumber;
    }

    public void setPlayernumber(int playernumber) {
        this.playernumber = playernumber;
    }

    public Player3 getPlayer3() {
        return player3;
    }

    public void setPlayer3(Player3 player3) {
        this.player3 = player3;
    }

    public Player4 getPlayer4() {
        return player4;
    }

    public void setPlayer4(Player4 player4) {
        this.player4 = player4;
    }

    public JFrame getJframe() {
        return jframe;
    }

    public void setJframe(MainFrame jframe) {
        this.jframe = jframe;
    }

    public PlayerMode getPlayerMode() {
        return playerMode;
    }

    public void setPlayerMode(PlayerMode playerMode) {
        this.playerMode = playerMode;
    }

    public String getSavename() {
        return savename;
    }

    public void setSavename(String savename) {
        this.savename = savename;
    }
}

