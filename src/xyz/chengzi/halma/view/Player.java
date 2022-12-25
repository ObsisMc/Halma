package xyz.chengzi.halma.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import xyz.chengzi.halma.model.Character;
import xyz.chengzi.halma.model.ChessBoardLocation;

public class Player extends JLayeredPane {
    Color color;
    Character character;
    String playername;
    boolean win=false;
    int rank=0;
    ArrayList<ChessBoardLocation> destination;


    public ArrayList<ChessBoardLocation> getDestination() {
        return destination;
    }

    public void setDestination(ArrayList<ChessBoardLocation> destination) {
        this.destination = destination;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

