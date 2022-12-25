package xyz.chengzi.halma.view;

import xyz.chengzi.halma.model.Character;
import xyz.chengzi.halma.model.ChessBoard;
import xyz.chengzi.halma.model.ChessBoardLocation;

import java.awt.*;
import java.util.ArrayList;

public class Player4 extends Player {
    Color color;
    Character character;

    public Player4() {
    }

    public Player4(Color color, Character character) {
        this.color = color;
        this.character = character;
    }
    public boolean inDestination(ChessBoard model, int playerNumber, ChessBoardLocation from, ChessBoardLocation to) {
        ArrayList<ChessBoardLocation> dest = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 6 - i; j++) {
                int row = model.getDimension() - j;
                dest.add(new ChessBoardLocation(row, i));
            }
        }
        for (int i = 1; i < 5; i++) {
            int row = model.getDimension() - i;
            dest.add(new ChessBoardLocation(row, 0));
        }
        boolean fromIn = false;
        boolean toIn = false;
        for (ChessBoardLocation l : dest) {
            if (l.equals(from)) fromIn = true;
            if (l.equals(to)) toIn = true;
        }
        return !fromIn || toIn;
    }

    public boolean isWinner(ChessBoard model){
        int n = 0;
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 6 - i; j++) {
                int row = model.getDimension() - j;
                if (model.getChessPieceAt(new ChessBoardLocation(row, i)) != null &&
                        model.getGridAt(new ChessBoardLocation(row, i)).getPiece().getColor() == getColor()) {
                    n++;
                }
            }
        }
        if (n == 9) {
            for (int i = 1; i < 5; i++) {
                int row = model.getDimension() - i;
                if (model.getChessPieceAt(new ChessBoardLocation(row, 0)) != null &&
                        model.getGridAt(new ChessBoardLocation(row, 0)).getPiece().getColor() == getColor()) {
                    n++;
                }
            }
        }
        if (n == 13) {
            setWin(true);
        }
        return isWin();
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
}