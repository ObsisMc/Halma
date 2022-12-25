package xyz.chengzi.halma.view;

import xyz.chengzi.halma.Frame.MainFrame;
import xyz.chengzi.halma.controller.GameController;
import xyz.chengzi.halma.model.Character;
import xyz.chengzi.halma.model.PlayerMode;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

//游戏窗口
public class GameFrame extends JFrame {
    GameController game;
    JLabel currentplayer;
    JLabel totalturnpanel;
    JLabel player1win;
    JLabel player2win;
    JLabel player3win;
    JLabel player4win;
    ChessBoardComponent chessBoardComponent;
    ChooseCharacter chooseCharacter;
    int playernumber;
    String savename;
    GameFrame self = this;
    int time = 0;


    public GameFrame(GameController game, ChessBoardComponent c, ChooseCharacter chooseCharacter) {
        this.game = game;
        chessBoardComponent = c;
        this.chooseCharacter = chooseCharacter;
        playernumber = chooseCharacter.getPlayernumber();
        savename = chooseCharacter.getSavename();


        setIconImage((new ImageIcon("image/titleicon.png")).getImage());
        setTitle("Halma");
        setSize(890, 680);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        //music
        File m = new File("music/dlp.wav");
        URI uri = m.toURI();
        URL url = null;  //解析地址
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        AudioClip aau = Applet.newAudioClip(url);
        aau.loop();
//分层画板用于存放所有控件
        JLayeredPane mainpanel = new JLayeredPane();
        add(mainpanel);
        mainpanel.setBounds(0, 0, 1050, 800);

        mainpanel.add(c, new Integer(5));
        c.setBounds(197, 60, 480, 480);
//画背景

        JLabel bg = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (playernumber == 2) {
                    ImageIcon i = new ImageIcon("image/4ppurebg144100.png");
                    g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                } else if (playernumber == 4) {
                    ImageIcon i = new ImageIcon("image/Santorini750730.png");
                    g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                }
            }
        };
        mainpanel.add(bg, new Integer(3));
        bg.setBounds(0, 0, 890, 680);

        //当前模式
        JPanel modbg = new JPanel();
        String mode = "";
        if (chooseCharacter.getPlayernumber() == 2) mode = "Two-player Mode";
        else if (chooseCharacter.getPlayernumber() == 4) mode = "Four-player Mode";
        modbg.setBackground(new Color(0, 0, 0, 0));
        modbg.setLayout(new BorderLayout());
        modbg.setBounds(300, 0, 300, 60);
        mainpanel.add(modbg, new Integer(5));

        JLabel mod = new JLabel(mode, SwingConstants.CENTER);
        modbg.add(mod, BorderLayout.CENTER);
        mod.setFont(new Font(Font.DIALOG, Font.BOLD, 30));

//回合
        JLabel statusLabel = new JLabel(String.format("%s's Turn", currentPlayer(game.getCurrentPlayer())));
        if (game.isOver()) statusLabel.setText("Game is over");
        statusLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        currentplayer = statusLabel;
        mainpanel.add(statusLabel, new Integer(5));
        statusLabel.setBounds(300, 540, 300, 60);
        //回合总数
        totalturnpanel = new JLabel(String.format("Total turns: %d", game.getTotalturn()));
        mainpanel.add(totalturnpanel, new Integer(5));
        totalturnpanel.setBounds(600, 540, 100, 60);
        statusLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));

//时间
        time = game.getTime();
        int second = time % 60;
        int minute = (time % 3600) / 60;
        int hour = time / 3600;
        JLabel clock = new JLabel(String.format("%d:%d:%d", hour, minute, second));
        mainpanel.add(clock, new Integer(5));
        if (playernumber == 2) clock.setBounds(780, 580, 100, 50);
        else if (playernumber == 4) clock.setBounds(760, 290, 100, 50);
        clock.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        //线程
        Thread timethread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    boolean open = game.isOver();
                    //不加这个他反应不过来？
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!open) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time = time + 1;
                        int secondt = time % 60;
                        int minutet = (time % 3600) / 60;
                        int hourt = time / 3600;
                        clock.setText(String.format("%d:%d:%d", hourt, minutet, secondt));
                        game.setTime(time);
                    }
                }
            }
        };
        timethread.start();
//undo
        JLabel regret = new JLabel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon i=new ImageIcon("image/undo.png");
                g.drawImage(i.getImage(),0,0,getWidth(),getHeight(),i.getImageObserver());
            }
        };
        regret.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Object[] options = {"Yes", "No"};
                int n = JOptionPane.showOptionDialog(self, "Yes: undo a turn / No: undo a step", "Undo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    game.Undo(true);
                } else if (n == 1) game.Undo(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                regret.setSize(55,55);
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
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                regret.setSize(45,45);
            }
        });
        regret.setBounds(820, 10, 45, 45);
        mainpanel.add(regret,new Integer(5));


//重置
        JLabel restartbutton = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon i = new ImageIcon("image/restart.png");
                g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
            }
        };
        mainpanel.add(restartbutton, new Integer(5));
        restartbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //棋盘重置
                game.getModel().placeInitialPieces(game.getPlayernumber());
                game.resetSelectedLocation();
                //玩家win重置
                chooseCharacter.getPlayer1().setWin(false);
                chooseCharacter.getPlayer2().setWin(false);
                if (game.getPlayernumber() == 4) {
                    chooseCharacter.getPlayer3().setWin(false);
                    chooseCharacter.getPlayer4().setWin(false);
                }
                //游戏over状态重置
                game.setOver(false);
                //重置winner
                game.setWinner(0);
                //总回合重置
                game.setTotalturn(1);
                changeTotalTurns(game);
                //游戏时间重置
                game.setTime(0);
                time = game.getTime();
                //重置当前玩家
                //开局随机先手
                if(playernumber==2){
                    Random sente=new Random();
                    int n=sente.nextInt(2);
                    if(n==0) game.setCurrentPlayer(((Player) chooseCharacter.getPlayer1()).getColor());
                    else if(n==1) game.setCurrentPlayer(((Player) chooseCharacter.getPlayer2()).getColor());
                }else if(playernumber==4){
                    Random sente=new Random();
                    int n=sente.nextInt(4);
                    if(n==0) game.setCurrentPlayer(((Player) chooseCharacter.getPlayer1()).getColor());
                    else if(n==1) game.setCurrentPlayer(((Player) chooseCharacter.getPlayer2()).getColor());
                    else if(n==2) game.setCurrentPlayer(((Player) chooseCharacter.getPlayer3()).getColor());
                    else if(n==3) game.setCurrentPlayer(((Player) chooseCharacter.getPlayer4()).getColor());
                }
                changeCurrentplayer(game);

                //将玩家排名重置
                chooseCharacter.getPlayer1().setRank(0);
                chooseCharacter.getPlayer2().setRank(0);
                if (playernumber == 4) {
                    chooseCharacter.getPlayer3().setRank(0);
                    chooseCharacter.getPlayer4().setRank(0);
                    player3win.repaint();
                    player4win.repaint();
                }
                player1win.repaint();
                player2win.repaint();

                //移动记录重置
                game.getModel().setMoveFrom(new ArrayList<>());
                game.getModel().setMoveTo(new ArrayList<>());

                JOptionPane.showMessageDialog(self, "Game has been restarted!");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                restartbutton.setSize(55, 55);
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

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                restartbutton.setSize(45, 45);
            }
        });
//        if (playernumber == 4) {
//            savebutton.setBounds(730, 200, 45, 45);
//        } else if (playernumber == 2) {
//            savebutton.setBounds(720, 100, 45, 45);
//        }
        restartbutton.setBounds(760, 10, 45, 45);

//存档
        JLabel save = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon i = new ImageIcon("image/save2.png");
                g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
            }
        };
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JOptionPane.showMessageDialog(self, "Game has been saved!");
                game.saveGameToFile(game.getModel().saveGame());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                save.setSize(55, 55);

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
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                save.setSize(45, 45);
            }
        });
        mainpanel.add(save, new Integer(5));
//        if (playernumber == 2) save.setBounds(760, 50, 45, 45);
//        else if (playernumber == 4) save.setBounds(760, 350, 45, 45);
        save.setBounds(700, 10, 45, 45);

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


                Object[] options = {"Yes", "No"};
                int n = JOptionPane.showOptionDialog(self,
                        "                              Save your game ?" +
                                "\n(Whenever someone win, game will save automatically)", "Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    //存档
                    game.saveGameToFile(game.getModel().saveGame());
                    //退出游戏
                    MainFrame mainFrame = null;
                    try {
                        mainFrame = new MainFrame();
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                    timethread.stop();
                    LocalGamePanel localGamePanel = new LocalGamePanel(mainFrame);
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                    dispose();
                    aau.stop();
                } else if (n == 1) {
                    //退出游戏
                    MainFrame mainFrame = null;
                    try {
                        mainFrame = new MainFrame();
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                    timethread.stop();
                    LocalGamePanel localGamePanel = new LocalGamePanel(mainFrame);
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                    dispose();
                    aau.stop();
                }


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
        mainpanel.add(back, new Integer(6));

//不同模式的布局
        if (chooseCharacter.getPlayernumber() == 2) {
            //人物
            JLayeredPane player1 = new JLayeredPane();
            JLayeredPane player2 = new JLayeredPane();
            player1.setBounds(10, 250, 180, 150);
            player2.setBounds(700, 250, 180, 150);
            mainpanel.add(player1, new Integer(5));
            mainpanel.add(player2, new Integer(5));
            player1.add(chooseCharacter.getPlayer1().getCharacter());
            player2.add(chooseCharacter.getPlayer2().character);
            chooseCharacter.getPlayer1().character.setBounds(0, 0, 140, 140);
            chooseCharacter.getPlayer2().character.setBounds(0, 0, 140, 140);

            //胜利画板
            player1win = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (chooseCharacter.getPlayer1().getRank() == 1) {
                        ImageIcon i = new ImageIcon("image/gold.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (chooseCharacter.getPlayer1().getRank() == 2) {
                        ImageIcon i = new ImageIcon("image/silver.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());

                    }
                }
            };
            player1.add(player1win, new Integer(5));
            player1win.setBounds(100, 0, 80, 140);
            player2win = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (chooseCharacter.getPlayer2().getRank() == 1) {
                        ImageIcon i = new ImageIcon("image/gold.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (chooseCharacter.getPlayer2().getRank() == 2) {
                        ImageIcon i = new ImageIcon("image/silver.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());

                    }
                }
            };
            player2.add(player2win, new Integer(5));
            player2win.setBounds(100, 0, 80, 140);

//用户名
            JLabel j1 = new JLabel(chooseCharacter.getPlayer1().getPlayername());
            JLabel j2 = new JLabel(chooseCharacter.getPlayer2().getPlayername());
            j1.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            j2.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            mainpanel.add(j1, new Integer(5));
            mainpanel.add(j2, new Integer(5));
            j1.setBounds(30, 380, 200, 45);
            j2.setBounds(735, 380, 200, 45);

//            //打开就获胜时显示
//            if(game.isOver()){
//                JOptionPane.showMessageDialog(self,String.format("Game is over\n No.1 %s",chooseCharacter.getPlayer1().getPlayername()));
//            }


        } else if (chooseCharacter.getPlayernumber() == 4) {
            JLayeredPane player1 = new JLayeredPane();
            JLayeredPane player2 = new JLayeredPane();
            JLayeredPane player3 = new JLayeredPane();
            JLayeredPane player4 = new JLayeredPane();
            mainpanel.add(player1, new Integer(5));
            mainpanel.add(player2, new Integer(5));
            mainpanel.add(player3, new Integer(5));
            mainpanel.add(player4, new Integer(5));
            player1.setBounds(20, 70, 180, 150);
            player2.setBounds(710, 70, 180, 150);
            player3.setBounds(710, 450, 180, 150);
            player4.setBounds(20, 450, 180, 150);
            player1.add(chooseCharacter.getPlayer1().character);
            player2.add(chooseCharacter.getPlayer2().character);
            player3.add(chooseCharacter.getPlayer3().character);
            player4.add(chooseCharacter.getPlayer4().character);
            chooseCharacter.getPlayer1().character.setBounds(0, 0, 140, 140);
            chooseCharacter.getPlayer2().character.setBounds(0, 0, 140, 140);
            chooseCharacter.getPlayer3().character.setBounds(0, 0, 140, 140);
            chooseCharacter.getPlayer4().character.setBounds(0, 0, 140, 140);

            //胜利画板
            player1win = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (chooseCharacter.getPlayer1().getRank() == 1) {
                        ImageIcon i = new ImageIcon("image/gold.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (chooseCharacter.getPlayer1().getRank() == 2) {
                        ImageIcon i = new ImageIcon("image/silver.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());

                    } else if (chooseCharacter.getPlayer1().getRank() == 3) {
                        ImageIcon i = new ImageIcon("image/bronze.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    }
                }
            };
            player1.add(player1win, new Integer(6));
            player1win.setBounds(100, 0, 80, 140);
            player2win = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (chooseCharacter.getPlayer2().getRank() == 1) {
                        ImageIcon i = new ImageIcon("image/gold.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (chooseCharacter.getPlayer2().getRank() == 2) {
                        ImageIcon i = new ImageIcon("image/silver.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());

                    } else if (chooseCharacter.getPlayer2().getRank() == 3) {
                        ImageIcon i = new ImageIcon("image/bronze.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    }
                }
            };
            player2.add(player2win, new Integer(6));
            player2win.setBounds(100, 0, 80, 140);
            player3win = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (chooseCharacter.getPlayer3().getRank() == 1) {
                        ImageIcon i = new ImageIcon("image/gold.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (chooseCharacter.getPlayer3().getRank() == 2) {
                        ImageIcon i = new ImageIcon("image/silver.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());

                    } else if (chooseCharacter.getPlayer3().getRank() == 3) {
                        ImageIcon i = new ImageIcon("image/bronze.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    }
                }
            };
            player3.add(player3win, new Integer(6));
            player3win.setBounds(100, 0, 80, 140);
            player4win = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (chooseCharacter.getPlayer4().getRank() == 1) {
                        ImageIcon i = new ImageIcon("image/gold.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    } else if (chooseCharacter.getPlayer4().getRank() == 2) {
                        ImageIcon i = new ImageIcon("image/silver.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());

                    } else if (chooseCharacter.getPlayer4().getRank() == 3) {
                        ImageIcon i = new ImageIcon("image/bronze.png");
                        g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
                    }
                }
            };
            player4.add(player4win, new Integer(6));
            player4win.setBounds(100, 0, 80, 140);


            //用户名
            JLabel j1 = new JLabel(chooseCharacter.getPlayer1().getPlayername());
            JLabel j2 = new JLabel(chooseCharacter.getPlayer2().getPlayername());
            JLabel j3 = new JLabel(chooseCharacter.getPlayer3().getPlayername());
            JLabel j4 = new JLabel(chooseCharacter.getPlayer4().getPlayername());
            j1.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            j2.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            j3.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            j4.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            mainpanel.add(j1, new Integer(5));
            mainpanel.add(j2, new Integer(5));
            mainpanel.add(j3, new Integer(5));
            mainpanel.add(j4, new Integer(5));
            j1.setBounds(50, 200, 200, 45);
            j2.setBounds(750, 200, 200, 45);
            j3.setBounds(750, 585, 200, 45);
            j4.setBounds(50, 585, 200, 45);

        }
    }

    public void changeCurrentplayer(GameController gameController) {
        if (!gameController.isOver()) {
            String character = currentPlayer(gameController.getCurrentPlayer());
            currentplayer.setText(String.format("%s's Turn", character));
        } else {
            currentplayer.setText(String.format("Game is over"));
        }

    }

    public void changeTotalTurns(GameController gameController) {
        int t = gameController.getTotalturn();
        totalturnpanel.setText(String.format("Total turns: %d", game.getTotalturn()));
    }

    public String currentPlayer(Color c) {
        String character = null;
        if (c == Color.RED) {
            character = "Zeus";
        } else if (c == Color.GREEN) {
            character = "Medusa";
        } else if (c == Color.WHITE) {
            character = "Poseidon";
        } else if (c == Color.BLACK) {
            character = "Hades";
        } else if (c == Color.YELLOW) {
            character = "Artemis";
        } else if (c == Color.BLUE) {
            character = "Athena";
        }
        return character;
    }

    //胜利画板的paintComponent重写
    public void drawMedal(Player p, Graphics g) {
        if (p.getRank() == 1) {
            ImageIcon i = new ImageIcon("image/gold.png");
            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
        } else if (p.getRank() == 2) {
            ImageIcon i = new ImageIcon("image/silver.png");
            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
        } else if (p.getRank() == 3) {
            ImageIcon i = new ImageIcon("image/bronze.png");
            g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
        }
    }


    public GameController getGame() {
        return game;
    }

    public void setGame(GameController game) {
        this.game = game;
    }

    public JLabel getCurrentplayer() {
        return currentplayer;
    }

    public void setCurrentplayer(JLabel currentplayer) {
        this.currentplayer = currentplayer;
    }

    public ChessBoardComponent getChessBoardComponent() {
        return chessBoardComponent;
    }

    public void setChessBoardComponent(ChessBoardComponent chessBoardComponent) {
        this.chessBoardComponent = chessBoardComponent;
    }

    public ChooseCharacter getChooseCharacter() {
        return chooseCharacter;
    }

    public void setChooseCharacter(ChooseCharacter chooseCharacter) {
        this.chooseCharacter = chooseCharacter;
    }

    public String getSavename() {
        return savename;
    }

    public void setSavename(String savename) {
        this.savename = savename;
    }

    public JLabel getPlayer1win() {
        return player1win;
    }

    public void setPlayer1win(JLabel player1win) {
        this.player1win = player1win;
    }

    public JLabel getPlayer2win() {
        return player2win;
    }

    public void setPlayer2win(JLabel player2win) {
        this.player2win = player2win;
    }

    public JLabel getPlayer3win() {
        return player3win;
    }

    public void setPlayer3win(JLabel player3win) {
        this.player3win = player3win;
    }

    public JLabel getPlayer4win() {
        return player4win;
    }

    public void setPlayer4win(JLabel player4win) {
        this.player4win = player4win;
    }
}
