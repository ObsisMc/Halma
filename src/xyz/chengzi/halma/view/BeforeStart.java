package xyz.chengzi.halma.view;

import xyz.chengzi.halma.Frame.EmptyFrame;

import javax.swing.*;
import java.awt.*;

public class BeforeStart extends EmptyFrame {
    public BeforeStart() {
        super(480, 400);
        setBackground(new Color(255, 255, 255, 171));
        ImageIcon image = new ImageIcon("image/beforestart.png");
        JLabel jl=new JLabel();
        jl.setIcon(image);
        jl.setBounds(0,0,480,400);
        add(jl);
        setBackground(new Color(255, 255, 255, 171));
    }
}
