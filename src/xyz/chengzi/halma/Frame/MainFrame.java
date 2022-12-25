package xyz.chengzi.halma.Frame;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainFrame extends JFrame {


    public MainFrame() throws MalformedURLException {
        setTitle("Halma");
        setSize(752,732);
        setResizable(false);
        setIconImage((new ImageIcon("image/titleicon.png")).getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


//        File f = new File("music/ua.wav");
//                URI uri = f.toURI();
//                URL url = null;  //解析地址
//                try {
//                    url = uri.toURL();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                AudioClip aau = Applet.newAudioClip(url);
//                aau.play();
    }

}
