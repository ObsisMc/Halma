package xyz.chengzi.halma.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmptyFrame extends JFrame {
    public EmptyFrame(int width,int height){
        setUndecorated(true);
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setIconImage((new ImageIcon("image/titleicon.png")).getImage());
        setVisible(true);
    }


}
