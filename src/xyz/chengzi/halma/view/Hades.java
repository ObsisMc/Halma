package xyz.chengzi.halma.view;

import xyz.chengzi.halma.listener.CharacterListener;
import xyz.chengzi.halma.model.Character;

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

public class Hades extends Character implements CharacterListener {//黑色
    ChooseCharacter jl;
    Boolean isSelected = false;
    JLabel c = this;

    public JLayeredPane getJl() {
        return jl;
    }

    public void setJl(ChooseCharacter jl) {
        this.jl = jl;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Hades(){
        ImageIcon hai = new ImageIcon("image/Hadesname1.4.png");
        setIcon(hai);
    }
    public Hades(ChooseCharacter j) {
        jl = j;

        ImageIcon hai = new ImageIcon("image/Hadesname.png");
        setIcon(hai);
        j.add(this, new Integer(4));
        setBounds(530, 23, 100, 100);
    }

    @Override
    public void clickCharacterPanel() {
        c.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (jl.getPlayernumber() == 2) {
                    if (!isSelected) {
                        if (jl.getPlayer1().getColor() == null) {
                            jl.remove(c);
                            setSelected(true);
                            jl.getPlayer1().setColor(Color.BLACK);
                            jl.getPlayer1().setCharacter((Character) c);
                            setBounds(0, 0, 140, 140);
                            jl.getPlayer1().add(c, new Integer(5));

                        } else if (jl.getPlayer2().getColor() == null) {
                            jl.remove(c);
                            setSelected(true);
                            jl.getPlayer2().setColor(Color.BLACK);
                            jl.getPlayer2().setCharacter((Character) c);
                            jl.getPlayer2().add(c, new Integer(5));
                            setBounds(0, 0, 140, 140);
                        }
                        jl.repaint();

                    } else {
                        if (!isConfirmed()) {
                            if (jl.getPlayer1().getColor() == Color.BLACK) {
                                jl.getPlayer1().remove(c);
                                setSelected(false);
                                jl.getPlayer1().setColor(null);
                                jl.getPlayer1().setCharacter(null);
                            } else {
                                jl.getPlayer2().remove(c);
                                setSelected(false);
                                jl.getPlayer2().setColor(null);
                                jl.getPlayer2().setCharacter(null);
                            }
                            setBounds(530, 23, 100, 100);
                            jl.add(c, new Integer(6));
                            jl.repaint();
                        }
                    }
                } else if (jl.getPlayernumber() == 4) {
                    if (!isSelected) {
                        if (jl.getPlayer1().getColor() == null) {
                            jl.remove(c);
                            setSelected(true);
                            jl.getPlayer1().setColor(Color.BLACK);
                            jl.getPlayer1().setCharacter((Character) c);
                            setBounds(0, 0, 140, 140);
                            jl.getPlayer1().add(c, new Integer(5));

                        } else if (jl.getPlayer2().getColor() == null) {
                            jl.remove(c);
                            setSelected(true);
                            jl.getPlayer2().setColor(Color.BLACK);
                            jl.getPlayer2().setCharacter((Character) c);
                            jl.getPlayer2().add(c, new Integer(5));
                            setBounds(0, 0, 140, 140);
                        } else if (jl.getPlayer3().getColor() == null) {
                            jl.remove(c);
                            setSelected(true);
                            jl.getPlayer3().setColor(Color.BLACK);
                            jl.getPlayer3().setCharacter((Character) c);
                            jl.getPlayer3().add(c, new Integer(5));
                            setBounds(0, 0, 140, 140);
                        } else if (jl.getPlayer4().getColor() == null) {
                            jl.remove(c);
                            setSelected(true);
                            jl.getPlayer4().setColor(Color.BLACK);
                            jl.getPlayer4().setCharacter((Character) c);
                            jl.getPlayer4().add(c, new Integer(5));
                            setBounds(0, 0, 140, 140);
                        }
                        jl.repaint();

                    } else {
                        if (!isConfirmed()) {
                            if (jl.getPlayer1().getColor() == Color.BLACK) {
                                jl.getPlayer1().remove(c);
                                setSelected(false);
                                jl.getPlayer1().setColor(null);
                                jl.getPlayer1().setCharacter(null);
                            } else if (jl.getPlayer2().getColor() == Color.BLACK) {
                                jl.getPlayer2().remove(c);
                                setSelected(false);
                                jl.getPlayer2().setColor(null);
                                jl.getPlayer2().setCharacter(null);
                            } else if (jl.getPlayer3().getColor() == Color.BLACK) {
                                jl.getPlayer3().remove(c);
                                setSelected(false);
                                jl.getPlayer3().setColor(null);
                                jl.getPlayer3().setCharacter(null);
                            } else if (jl.getPlayer4().getColor() == Color.BLACK) {
                                jl.getPlayer4().remove(c);
                                setSelected(false);
                                jl.getPlayer4().setColor(null);
                                jl.getPlayer4().setCharacter(null);
                            }
                            setBounds(530, 23, 100, 100);
                            jl.add(c, new Integer(6));
                            jl.repaint();
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    ImageIcon zeusi = new ImageIcon("image/Hadesname1.4.png");
                    setIcon(zeusi);
                    c.setBounds(515, 3, 140, 140);

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
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected) {
                    ImageIcon zeusi = new ImageIcon("image/Hadesname.png");
                    setIcon(zeusi);
                    c.setBounds(530, 23, 100, 100);
                    repaint();
                }
            }
        });
    }
}