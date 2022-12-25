package xyz.chengzi.halma.model;

import javax.swing.*;
import java.awt.*;
//棋子
public class ChessPiece  {
    private Color color;


    public ChessPiece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
