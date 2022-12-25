package xyz.chengzi.halma.view;

import xyz.chengzi.halma.Frame.MainFrame;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class LocalGamePanel extends JPanel {
    MainFrame startFrame;
    LocalGamePanel self = this;
    AudioClip aau;


    public LocalGamePanel(MainFrame startFrame) {
        this.startFrame = startFrame;
        startFrame.add(self);

        setLayout(null);
        //音乐
        File f = new File("music/mt.wav");
        URI uri = f.toURI();
        URL url = null;  //解析地址
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        AudioClip aau = Applet.newAudioClip(url);
        aau.loop();
//积分榜按钮
        JLabel scoreboard = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon i = new ImageIcon("image/score.png");
                g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), i.getImageObserver());
            }
        };
        add(scoreboard);
        scoreboard.setBounds(640, 30, 50, 50);
        scoreboard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ScoreBoard scoreBoard = new ScoreBoard(startFrame);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
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
                scoreboard.setBounds(635, 20, 65, 65);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                scoreboard.setBounds(640, 30, 50, 50);
            }
        });
//二人游戏开始按钮
        JPanel twoplayer = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                ImageIcon starticon = new ImageIcon("image/two.png");
                Image startimage = starticon.getImage();
                g.drawImage(startimage, 0, 0, getWidth(), getHeight(), starticon.getImageObserver());
            }
        };
        twoplayer.setBackground(new Color(0, 0, 0, 0));
        twoplayer.setBounds(200, 380, 200, 35);
        twoplayer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                startFrame.remove(self);

                TwoPlayerStartPanel tp = new TwoPlayerStartPanel(startFrame);

                aau.stop();
                startFrame.setVisible(true);//为啥还要setVisible
                startFrame.repaint();

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                twoplayer.setBounds(180, 375, 260, 46);

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
                twoplayer.setBounds(200, 380, 200, 35);
                repaint();
            }
        });
        add(twoplayer);

        //四人游戏开始按钮
        JPanel fourplayer = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                ImageIcon starticon = new ImageIcon("image/four.png");
                Image startimage = starticon.getImage();
                g.drawImage(startimage, 0, 0, getWidth(), getHeight(), starticon.getImageObserver());
            }
        };
        fourplayer.setBackground(new Color(0, 0, 0, 0));
        fourplayer.setBounds(510, 350, 200, 35);
        fourplayer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                startFrame.remove(self);
                FourPlayerStartPanel tp = new FourPlayerStartPanel(startFrame);

                aau.stop();
                startFrame.setVisible(true);//为啥还要setVisible
                startFrame.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                fourplayer.setBounds(500, 345, 240, 42);

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
                fourplayer.setBounds(510, 350, 200, 35);
                repaint();
            }
        });
        add(fourplayer);

        //返回主菜单
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
                startFrame.add(new StartMenu(startFrame));
                startFrame.remove(self);
                startFrame.repaint();
                startFrame.revalidate();
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
    }

    @Override
    public void paintComponent(Graphics g) {
        ImageIcon local = new ImageIcon("image/localgameps.png");
        Image localgame = local.getImage();
        g.drawImage(localgame, 0, 0, 750, 700, null);
    }
}
