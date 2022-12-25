package xyz.chengzi.halma.view;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

//棋子设计
public class ChessComponent extends JComponent {
    private Color color;
    private boolean selected;


    public ChessComponent(Color color) {
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintChess(g);
    }

    private void paintChess(Graphics g) {
        if (!selected) {
            if (color == Color.RED) {
                ImageIcon zeus = new ImageIcon("image/lightningballps.png");
                g.drawImage(zeus.getImage(), 0, 0, null);
            } else if (color == Color.WHITE) {
                ImageIcon po = new ImageIcon("image/waveps.png");
                g.drawImage(po.getImage(), 0, 0, null);
            } else if (color == Color.YELLOW) {
                ImageIcon ar = new ImageIcon("image/deerps.png");
                g.drawImage(ar.getImage(), 0, 0, null);
            } else if (color == Color.BLUE) {
                ImageIcon at = new ImageIcon("image/owlps.png");
                g.drawImage(at.getImage(), 0, 0, null);
            } else if (color == Color.BLACK) {
                ImageIcon ha = new ImageIcon("image/ghostps.png");
                g.drawImage(ha.getImage(), 0, 0, null);
            } else if (color == Color.GREEN) {
                ImageIcon ha = new ImageIcon("image/snakesleep.png");
                g.drawImage(ha.getImage(), 0, 0, null);
            }
        } else if (selected) { // Draw a + sign in the center of the piece
            if (color == Color.BLACK) {
                ImageIcon at = new ImageIcon("image/cloud.png");
                g.drawImage(at.getImage(), 0, 0, null);
            } else if(color==Color.RED){
                ImageIcon at = new ImageIcon("image/lightning.png");
                g.drawImage(at.getImage(), 0, 0, null);
            }else if(color==Color.GREEN){
                ImageIcon at = new ImageIcon("image/snakeps.png");
                g.drawImage(at.getImage(), 0, 0, null);
            }else if(color==Color.WHITE){
                ImageIcon at = new ImageIcon("image/vortex.png");
                g.drawImage(at.getImage(), 0, 0, null);
            }else if(color==Color.BLUE){
                ImageIcon at = new ImageIcon("image/owlfly.png");
                g.drawImage(at.getImage(), 0, 0, null);
            }else if(color==Color.YELLOW){
                ImageIcon at = new ImageIcon("image/deermove.png");
                g.drawImage(at.getImage(), 0, 0, null);
            }
            else{
                g.setColor(new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()));
                g.drawLine(getWidth() / 2, getHeight() / 4, getWidth() / 2, getHeight() * 3 / 4);
                g.drawLine(getWidth() / 4, getHeight() / 2, getWidth() * 3 / 4, getHeight() / 2);
            }


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
    }
}
