package xyz.chengzi.halma.model;

import xyz.chengzi.halma.Frame.MainFrame;

import javax.swing.*;
import java.applet.AudioClip;

public class PlayerMode extends JLayeredPane {
    MainFrame jFrame;
    AudioClip aau;
    private static int savenumber2 = 0;
    private static int savenumber4 = 0;

    public JFrame getjFrame() {
        return jFrame;
    }

    public void setjFrame(MainFrame jFrame) {
        this.jFrame = jFrame;
    }

    public int getSavenumber2() {
        return savenumber2;
    }

    public void setSavenumber2(int savenumber2) {
        this.savenumber2 = savenumber2;
    }

    public static int getSavenumber4() {
        return savenumber4;
    }

    public static void setSavenumber4(int savenumber4) {
        PlayerMode.savenumber4 = savenumber4;
    }

    public AudioClip getAau() {
        return aau;
    }

    public void setAau(AudioClip aau) {
        this.aau = aau;
    }
}
