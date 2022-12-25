package xyz.chengzi.halma.model;

import xyz.chengzi.halma.listener.GameListener;
import xyz.chengzi.halma.listener.Listenable;
import xyz.chengzi.halma.view.ChooseCharacter;
import xyz.chengzi.halma.view.GameFrame;
import xyz.chengzi.halma.view.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//棋盘的初始化和棋子移动
public class ChessBoard implements Listenable<GameListener> {
    private List<GameListener> listenerList = new ArrayList<>();
    private Square[][] grid;
    private int dimension;
    private ChooseCharacter chooseCharacter;
    private ArrayList<ChessBoardLocation> moveTo;
    private ArrayList<ChessBoardLocation> moveFrom;

    public ChessBoard(int dimension) {
        this.grid = new Square[dimension][dimension];
        this.dimension = dimension;
        this.moveTo = new ArrayList<>();
        this.moveFrom = new ArrayList<>();

        initGrid();
    }

    public ChessBoard(int dimension, ChooseCharacter chooseCharacter) {
        this(dimension);
        this.chooseCharacter = chooseCharacter;

    }

    private void initGrid() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grid[i][j] = new Square(new ChessBoardLocation(i, j));
            }
        }
    }

    public void placeInitialPieces(int playernumber) {  //游戏棋盘初始化，放棋子到棋盘
        // TODO: This is only a demo implementation
        Color color1 = ((Player) chooseCharacter.getPlayer1()).getColor();
        Color color2 = ((Player) chooseCharacter.getPlayer2()).getColor();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grid[i][j].setPiece(null);
            }
        }

        if (playernumber == 4) {
            Color color3 = ((Player) chooseCharacter.getPlayer3()).getColor();
            Color color4 = ((Player) chooseCharacter.getPlayer4()).getColor();

            for (int i = 0; i < 4; i++) {
                grid[i][0].setPiece(new ChessPiece(color1));
            }
            for (int j = 1; j < 4; j++) {
                for (int i = 0; i < 5 - j; i++) {
                    grid[i][j].setPiece(new ChessPiece(color1));
                }
            }//第一个玩家
            for (int i = 1; i < 5; i++) {
                grid[dimension - i][0].setPiece(new ChessPiece(color2));
            }
            for (int j = 1; j < 4; j++) {
                for (int i = 1; i < 6 - j; i++) {
                    grid[dimension - i][j].setPiece(new ChessPiece(color2));
                }
            }//第二个玩家
            for (int i = 4; i > 0; i--) {
                grid[dimension - i][dimension - 1].setPiece(new ChessPiece(color3));
            }
            for (int j = 2; j < 5; j++) {
                for (int i = 6 - j; i > 0; i--) {
                    grid[dimension - i][dimension - j].setPiece(new ChessPiece(color3));
                }
            }//第三个玩家
            for (int i = 0; i < 4; i++) {
                grid[i][dimension - 1].setPiece(new ChessPiece(color4));
            }
            for (int j = 2; j < 5; j++) {
                for (int i = 0; i < 6 - j; i++) {
                    grid[i][dimension - j].setPiece(new ChessPiece(color4));
                }
            }//第四个玩家
        }
        if (playernumber == 2) {
            for (int i = 0; i < 5; i++) {
                grid[i][0].setPiece(new ChessPiece(color1));
            }
            for (int j = 1; j < 5; j++) {
                for (int i = 0; i < 6 - j; i++) {
                    grid[i][j].setPiece(new ChessPiece(color1));
                }
            }

            for (int i = 5; i > 0; i--) {
                grid[dimension - i][dimension - 1].setPiece(new ChessPiece(color2));
            }
            for (int j = 2; j < 6; j++) {
                for (int i = 7 - j; i > 0; i--) {
                    grid[dimension - i][dimension - j].setPiece(new ChessPiece(color2));
                }
            }
        }
        listenerList.forEach(listener -> listener.onChessBoardReload(this));
    }


    public Square getGridAt(ChessBoardLocation location) {
        return grid[location.getRow()][location.getColumn()];
    }

    public ChessPiece getChessPieceAt(ChessBoardLocation location) {
        return getGridAt(location).getPiece();
    }

    public void setChessPieceAt(ChessBoardLocation location, ChessPiece piece) {
        getGridAt(location).setPiece(piece);
        listenerList.forEach(listener -> listener.onChessPiecePlace(location, piece));
    }

    public ChessPiece removeChessPieceAt(ChessBoardLocation location) {
        ChessPiece piece = getGridAt(location).getPiece();
        getGridAt(location).setPiece(null);
        listenerList.forEach(listener -> listener.onChessPieceRemove(location));
        return piece;
    }

    public void moveChessPiece(ChessBoardLocation src, ChessBoardLocation dest) {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal halma move");
        }
        setChessPieceAt(dest, removeChessPieceAt(src));
    }

    public int getDimension() {
        return dimension;
    }

    public ArrayList<ChessBoardLocation> getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(ArrayList<ChessBoardLocation> moveTo) {
        this.moveTo = moveTo;
    }

    public ArrayList<ChessBoardLocation> getMoveFrom() {
        return moveFrom;
    }

    public void setMoveFrom(ArrayList<ChessBoardLocation> moveFrom) {
        this.moveFrom = moveFrom;
    }


    public boolean isValidMove(ChessBoardLocation src, ChessBoardLocation dest) {
        if (getChessPieceAt(src) == null || getChessPieceAt(dest) != null) {
            return false;
        }
        int srcRow = src.getRow(), srcCol = src.getColumn(), destRow = dest.getRow(), destCol = dest.getColumn();
        int rowDistance = destRow - srcRow, colDistance = destCol - srcCol;

        if (rowDistance == 0 && colDistance == 0) {
            return false;
        }

        ArrayList<ChessBoardLocation> validLocation = new ArrayList<>();
        ArrayList<ChessBoardLocation> needToJudge = new ArrayList<>();
        boolean stop = false;
        for (int i = srcRow - 1; i < srcRow + 2; i++) {
            for (int j = srcCol - 1; j < srcCol + 2; j++) {
                if (i >= 0 && i < getDimension() && j >= 0 && j < getDimension()) {
                    if (grid[i][j].getPiece() == null) {
                        validLocation.add(new ChessBoardLocation(i, j));
                    } else {
                        if (!(i == srcRow && j == srcCol)) {
                            int r = i * 2 - srcRow;
                            int c = j * 2 - srcCol;
                            if (r >= 0 && r < getDimension() && c >= 0 && c < getDimension()) {
                                if (grid[r][c].getPiece() == null) {
                                    validLocation.add(new ChessBoardLocation(r, c));
                                    needToJudge.add(new ChessBoardLocation(r, c));
                                }
                            }
                        }
                    }
                }
            }
        }

        while (!stop) {
            int size = validLocation.size();
            ArrayList<ChessBoardLocation> judge = new ArrayList<>(needToJudge);

            for (ChessBoardLocation cbl : judge) {
                srcRow = cbl.getRow();
                srcCol = cbl.getColumn();
                for (int i = srcRow - 1; i < srcRow + 2; i++) {
                    for (int j = srcCol - 1; j < srcCol + 2; j++) {
                        if (i >= 0 && i < getDimension() && j >= 0 && j < getDimension()) {
                            if (grid[i][j].getPiece() != null && !(i == srcRow && j == srcCol)) {
                                int r = i * 2 - srcRow;
                                int c = j * 2 - srcCol;
                                if (r >= 0 && r < getDimension() && c >= 0 && c < getDimension()) {
                                    if (grid[r][c].getPiece() == null) {
                                        boolean exist = false;
                                        for (ChessBoardLocation old : validLocation) {
                                            if (old.equals(new ChessBoardLocation(r, c))) {
                                                exist = true;
                                                break;
                                            }
                                        }
                                        if (!exist) {
                                            validLocation.add(new ChessBoardLocation(r, c));
                                            needToJudge.remove(cbl);
                                            needToJudge.add(new ChessBoardLocation(r, c));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            if (size == validLocation.size()) {
                stop = true;
            }
        }
        for (ChessBoardLocation location : validLocation) {
            if (dest.equals(location)) {
                return true;
            }
        }
        return false;
    }

    //undo
    public boolean undo(boolean all, int playerNumber) {
        ArrayList<ChessBoardLocation> before = new ArrayList<>(getMoveFrom());
        ArrayList<ChessBoardLocation> after = new ArrayList<>();
        ArrayList<ChessBoardLocation> newTo = new ArrayList<>(getMoveTo());
        if (getMoveFrom().size() > 0 && getMoveTo().size() > 0 && getMoveTo().size() == getMoveFrom().size()*2) {
            if (all) {
                if (playerNumber == 2) {
                    if (getMoveFrom().size()==1) {
                        return false;
                    }
                }
            }
            for (int i = 0; i < getMoveTo().size(); i += 2) {
                after.add(getMoveTo().get(i));
            }
            if (all && before.size() >= playerNumber) {
                for (int i = 0; i < playerNumber; i++) {
                    System.out.println(i);
                    ChessBoardLocation from = before.get(before.size() - 1);
                    ChessBoardLocation to = after.get(after.size() - 1);
                    Color color = getChessPieceAt(to).getColor();
                    getGridAt(from).setPiece(new ChessPiece(color));
                    removeChessPieceAt(to);
                    before.remove(from);
                    after.remove(to);
                    newTo.remove(to);
                    newTo.remove(to);
                    setMoveTo(newTo);
                    setMoveFrom(before);
                    listenerList.forEach(listener -> listener.onChessBoardReload(this));
                }
            }
            if (!all) {
                ChessBoardLocation from = before.get(before.size() - 1);
                ChessBoardLocation to = after.get(after.size() - 1);
                if(to==null) System.out.println(false);
                Color color = getChessPieceAt(to).getColor();
                getGridAt(from).setPiece(new ChessPiece(color));
                removeChessPieceAt(to);
                before.remove(from);
                after.remove(to);
                newTo.remove(to);
                newTo.remove(to);
                setMoveTo(newTo);
                setMoveFrom(before);
                listenerList.forEach(listener -> listener.onChessBoardReload(this));
            }
            return true;
        }
        return false;
    }
/*
    public boolean undo(boolean all, int playerNumber) {
        ArrayList<ChessBoardLocation> before = new ArrayList<>(getMoveFrom());
        ArrayList<ChessBoardLocation> after = new ArrayList<>(getMoveTo());
//        ArrayList<ChessBoardLocation> newFrom = new ArrayList<>(getMoveFrom());
//        ArrayList<ChessBoardLocation> newTo = new ArrayList<>(getMoveTo());
        if (getMoveFrom().size() > 0 && getMoveTo().size() > 0 && getMoveTo().size() == getMoveFrom().size()) {
            if (all) {
                if (playerNumber == 2) {
                    if (!(before.size() % 2 == 0) || !(after.size() % 2 == 0)) {
                        return false;
                    }
                }
            }
//            for (int i = 0; i < getMoveTo().size(); i += 2) {
//                after.add(getMoveTo().get(i));
//            }
            if (all && before.size() >= playerNumber) {
                for (int i = 0; i < playerNumber; i++) {
                    System.out.println(i);
                    ChessBoardLocation from = before.get(before.size() - 1);
                    ChessBoardLocation to = after.get(after.size() - 1);
                    Color color = getChessPieceAt(to).getColor();
                    getGridAt(from).setPiece(new ChessPiece(color));
                    removeChessPieceAt(to);
                    before.remove(from);
                    after.remove(to);
//                    newFrom.remove(from);
//                    newTo.remove(to);
                    //  newTo.remove(to);
                    setMoveTo(after);
                    setMoveFrom(before);
                    listenerList.forEach(listener -> listener.onChessBoardReload(this));
                }
            }
            if (!all) {
                ChessBoardLocation from = before.get(before.size() - 1);
                ChessBoardLocation to = after.get(after.size() - 1);
                if(to==null) System.out.println(false);
                Color color = getChessPieceAt(to).getColor();
                getGridAt(from).setPiece(new ChessPiece(color));
                removeChessPieceAt(to);
                before.remove(from);
                after.remove(to);
//                newFrom.remove(from);
//                newTo.remove(to);
//                newTo.remove(to);
                setMoveTo(after);
                setMoveFrom(before);
                listenerList.forEach(listener -> listener.onChessBoardReload(this));
            }
            return true;
        }
        return false;
    }
*/
    //存棋盘
    public char[][] saveGame() {
        char[][] saveGame = new char[getDimension()][getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            for (int j = 0; j < getDimension(); j++) {
                if (grid[i][j].getPiece() == null) saveGame[i][j] = '0';
                else {
                    Color color = grid[i][j].getPiece().getColor();
                    if (color.equals(Color.RED)) saveGame[i][j] = '1';
                    else if (color.equals(Color.GREEN)) saveGame[i][j] = '2';
                    else if (color.equals(Color.BLACK)) saveGame[i][j] = '3';
                    else if (color.equals(Color.WHITE)) saveGame[i][j] = '4';
                    else if (color.equals(Color.YELLOW)) saveGame[i][j] = '5';
                    else if (color.equals(Color.BLUE)) saveGame[i][j] = '6';
                }
            }
        }
        return saveGame;
    }

    //加载棋盘
    public void loadGame(char[][] game) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                removeChessPieceAt(new ChessBoardLocation(i, j));
                if (game[i][j] == '0') grid[i][j].setPiece(null);
                else if (game[i][j] == '1') grid[i][j].setPiece(new ChessPiece(Color.RED));
                else if (game[i][j] == '2') grid[i][j].setPiece(new ChessPiece(Color.GREEN));
                else if (game[i][j] == '3') grid[i][j].setPiece(new ChessPiece(Color.BLACK));
                else if (game[i][j] == '4') grid[i][j].setPiece(new ChessPiece(Color.WHITE));
                else if (game[i][j] == '5') grid[i][j].setPiece(new ChessPiece(Color.YELLOW));
                else if (game[i][j] == '6') grid[i][j].setPiece(new ChessPiece(Color.BLUE));
                listenerList.forEach(listener -> listener.onChessBoardReload(this));
            }
        }
    }

    @Override
    public void registerListener(GameListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void unregisterListener(GameListener listener) {
        listenerList.remove(listener);
    }
}
