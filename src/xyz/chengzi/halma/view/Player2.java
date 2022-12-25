package xyz.chengzi.halma.view;

import xyz.chengzi.halma.model.Character;
import xyz.chengzi.halma.model.ChessBoard;
import xyz.chengzi.halma.model.ChessBoardLocation;

import java.awt.*;
import java.util.ArrayList;

public class Player2 extends Player {
    Color color;
    Character character;

    public Player2() {
    }

    public Player2(Color color, Character character) {
        this.color = color;
        this.character = character;
    }
    public boolean inDestination(ChessBoard model, int playerNumber, ChessBoardLocation from, ChessBoardLocation to) {
        ArrayList<ChessBoardLocation> dest = new ArrayList<>();
        if (playerNumber == 4) {
            for (int i = 2; i < 5; i++) {
                int col = model.getDimension() - i;
                for (int j = 0; j < 6 - i; j++) {
                    dest.add(new ChessBoardLocation(j, col));
                }
            }
            for (int i = 0; i < 4; i++) {
                int col = model.getDimension() - 1;
                dest.add(new ChessBoardLocation(i, col));
            }
        }
        if (playerNumber == 2) {
            for (int i = 1; i < 5; i++) {
                for (int j = 0; j < 6 - i; j++) {
                    dest.add(new ChessBoardLocation(i, j));
                }
            }
            for (int i = 0; i < 5; i++) {
                dest.add(new ChessBoardLocation(0, i));
            }
        }
        boolean fromIn = false;
        boolean toIn = false;
        for (ChessBoardLocation l : dest) {
            if (l.equals(from)) fromIn = true;
            if (l.equals(to)) toIn = true;
        }
        return !fromIn || toIn;
    }

    public boolean isWinner(ChessBoard model, int playerNumber){
        int n = 0;
        if (playerNumber==4){
            for (int i = 2; i < 5; i++) {
                int col = model.getDimension() - i;
                for (int j = 0; j < 6 - i; j++) {
                    if (model.getChessPieceAt(new ChessBoardLocation(j, col)) != null &&
                            model.getGridAt(new ChessBoardLocation(j, col)).getPiece().getColor() == getColor()) {
                        n++;
                    }
                }
            }
            if (n == 9) {
                for (int i = 0; i < 4; i++) {
                    int col = model.getDimension() - 1;
                    if (model.getChessPieceAt(new ChessBoardLocation(i, col)) != null &&
                            model.getGridAt(new ChessBoardLocation(i, col)).getPiece().getColor() == getColor()) {
                        n++;
                    }
                }
            }
            if (n == 13) {
                setWin(true);
            }
        }
        if (playerNumber==2){
            for (int i = 1; i < 5; i++) {
                for (int j = 0; j < 6 - i; j++) {
                    if (model.getChessPieceAt(new ChessBoardLocation(i, j)) != null
                            && model.getGridAt(new ChessBoardLocation(i, j)).getPiece().getColor() == getColor()) {
                        n++;
                    }
                }
            }
            if (n == 14) {
                for (int i = 0; i < 5; i++) {
                    if (model.getChessPieceAt(new ChessBoardLocation(0, i)) != null
                            && model.getGridAt(new ChessBoardLocation(0, i)).getPiece().getColor() == getColor()) {
                        n++;
                    }
                }
            }
            if (n == 19) {
                setWin(true);
            }
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

