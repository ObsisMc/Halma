package xyz.chengzi.halma;

import xyz.chengzi.halma.Frame.MainFrame;
import xyz.chengzi.halma.controller.GameController;
import xyz.chengzi.halma.model.ChessBoard;
import xyz.chengzi.halma.view.*;

import javax.swing.*;
import java.net.MalformedURLException;

public class Halma {
    public static void main(String[] args) {
        Thread beforestart = new Thread(() -> {
                BeforeStart before = new BeforeStart();
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                before.dispose();
            });
            beforestart.start();

        SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            MainFrame mainFrame = null;
            try {
                mainFrame = new MainFrame();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            StartMenu start = new StartMenu(mainFrame);
//                TwoPlayerStartPanel t=new TwoPlayerStartPanel(mainFrame);
//            LocalGamePanel l=new LocalGamePanel(mainFrame);
//                FourPlayerStartPanel f=new FourPlayerStartPanel(mainFrame);

//            mainFrame.remove(f);
//             ChooseCharacter c=new ChooseCharacter(mainFrame,4,f,"test");


        });
    }
}
