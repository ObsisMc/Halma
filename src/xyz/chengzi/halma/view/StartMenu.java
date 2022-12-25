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

public class StartMenu extends JPanel {
    MainFrame startFrame;
    StartMenu self = this;

    public StartMenu(MainFrame startFrame) {
        this.startFrame = startFrame;
        startFrame.add(self);

        setLayout(null);

        //音乐
        File f = new File("music/krom.wav");
        URI uri = f.toURI();
        URL url = null;  //解析地址
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        AudioClip aau = Applet.newAudioClip(url);
        aau.loop();

//本地游戏开始按钮
        JLabel startgame = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                ImageIcon starticon = new ImageIcon("image/localgamestart.png");
                g.drawImage(starticon.getImage(), 0, 0, getWidth(), getHeight(), starticon.getImageObserver());
            }
        };

        startgame.setBounds(160, 250, 130, 35);
        startgame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                startFrame.remove(self);
                LocalGamePanel localgame = new LocalGamePanel(startFrame);

                aau.stop();
                startFrame.setVisible(true);//为啥还要setVisible
                startFrame.repaint();
                startFrame.revalidate();

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                startgame.setBounds(130, 245, 180, 45);
                startgame.repaint();

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
                startgame.setBounds(160, 250, 130, 35);
                startgame.repaint();
            }
        });
        add(startgame);
//退出按钮
        JLabel exitgame = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                ImageIcon exiticon = new ImageIcon("image/exit.png");
                g.drawImage(exiticon.getImage(), 0, 0, getWidth(), getHeight(), exiticon.getImageObserver());
            }
        };

        exitgame.setBounds(140, 550, 70, 50);
        exitgame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Object[] options = {"Yes!", "Wait!"};
                int n = JOptionPane.showOptionDialog(self, "Exit game ?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (n == 0) {
                    System.exit(0);

                    startFrame.remove(self);
                    startFrame.repaint();
                }


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exitgame.setBounds(130, 540, 100, 70);

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

                exitgame.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitgame.setBounds(140, 550, 70, 50);
                exitgame.repaint();
            }
        });
        add(exitgame);
//标题
        JLabel title=new JLabel(){
            @Override
            public void paintComponent(Graphics g) {
                ImageIcon ticon = new ImageIcon("image/halma.png");
                g.drawImage(ticon.getImage(), 0, 0, getWidth(), getHeight(), ticon.getImageObserver());
            }
        };
        title.setBounds(30,20,250,80);
        add(title);
    }

    @Override
    public void paintComponent(Graphics g) {
        ImageIcon map = new ImageIcon("image/greecemapps2.png");
        Image mapimage = map.getImage();
        g.drawImage(mapimage, 0, 0, 750, 700, null);
    }

}
