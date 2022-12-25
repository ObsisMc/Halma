package xyz.chengzi.halma.view;

import xyz.chengzi.halma.model.Character;
import xyz.chengzi.halma.model.ChessBoard;
import xyz.chengzi.halma.model.ChessBoardLocation;

import java.awt.*;
import java.util.ArrayList;

public class Player1 extends Player {

    public Player1() {
    }

    public Player1(Color color, Character character) {
        this.color = color;
        this.character = character;
    }
    public boolean inDestination(ChessBoard model, int playerNumber, ChessBoardLocation from, ChessBoardLocation to) {
        ArrayList<ChessBoardLocation> dest = new ArrayList<>();
        if (playerNumber == 4) {
            for (int i = 2; i < 5; i++) {
                for (int j = 1; j < 7 - i; j++) {
                    int row = model.getDimension() - i;
                    int col = model.getDimension() - j;
                    dest.add(new ChessBoardLocation(row, col));
                }
            }
            for (int i = 1; i < 5; i++) {
                int row = model.getDimension() - 1;
                int col = model.getDimension() - i;
                dest.add(new ChessBoardLocation(row, col));
            }
        }
        if (playerNumber == 2) {
            for (int i = 2; i < 6; i++) {
                for (int j = 1; j < 8 - i; j++) {
                    int row = model.getDimension() - i;
                    int col = model.getDimension() - j;
                    dest.add(new ChessBoardLocation(row, col));
                }
            }
            for (int i = 1; i < 6; i++) {
                int row = model.getDimension() - 1;
                int col = model.getDimension() - i;
                dest.add(new ChessBoardLocation(row, col));
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

    public boolean isWinner(ChessBoard model, int playerNumber) {
        int n = 0;
        if (playerNumber == 4) {
            for (int i = 2; i < 5; i++) {
                for (int j = 1; j < 7 - i; j++) {
                    int row = model.getDimension() - i;
                    int col = model.getDimension() - j;
                    if (model.getChessPieceAt(new ChessBoardLocation(row, col)) != null
                            && model.getGridAt(new ChessBoardLocation(row, col)).getPiece().getColor() == color) {
                        n++;
                    }
                }
            }
            if (n == 9) {
                for (int i = 1; i < 5; i++) {
                    int row = model.getDimension() - 1;
                    int col = model.getDimension() - i;
                    if (model.getChessPieceAt(new ChessBoardLocation(row, col)) != null
                            && model.getGridAt(new ChessBoardLocation(row, col)).getPiece().getColor() == color) {
                        n++;
                    }
                }
            }
            if (n==13)
                setWin(true);
        }
        if (playerNumber == 2) {
            for (int i = 2; i < 6; i++) {
                for (int j = 1; j < 8 - i; j++) {
                    int row = model.getDimension() - i;
                    int col = model.getDimension() - j;
                    if (model.getChessPieceAt(new ChessBoardLocation(row, col)) != null
                            && model.getGridAt(new ChessBoardLocation(row, col)).getPiece().getColor() == color) {
                        n++;
                    }
                }
            }
            if (n == 14) {
                for (int i = 1; i < 6; i++) {
                    int row = model.getDimension() - 1;
                    int col = model.getDimension() - i;
                    if (model.getChessPieceAt(new ChessBoardLocation(row, col)) != null
                            && model.getGridAt(new ChessBoardLocation(row, col)).getPiece().getColor() == color) {
                        n++;
                    }
                }
            }
            if (n==19)
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

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
