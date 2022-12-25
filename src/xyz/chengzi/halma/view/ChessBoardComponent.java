package xyz.chengzi.halma.view;

import xyz.chengzi.halma.listener.GameListener;
import xyz.chengzi.halma.listener.InputListener;
import xyz.chengzi.halma.listener.Listenable;
import xyz.chengzi.halma.model.ChessBoard;
import xyz.chengzi.halma.model.ChessBoardLocation;
import xyz.chengzi.halma.model.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
//整个棋盘画出来
public class ChessBoardComponent extends JLayeredPane implements Listenable<InputListener>, GameListener {
    private static final Color BOARD_COLOR_1 = new Color(135, 206, 235);
    private static final Color BOARD_COLOR_2 = new Color(245,245,245);

    private List<InputListener> listenerList = new ArrayList<>();
    private SquareComponent[][] gridComponents;
    private int dimension;
    private int gridSize;

    public ChessBoardComponent(int size, int dimension) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLayout(null); // Use absolute layout
        setSize(size, size);

        this.gridComponents = new SquareComponent[dimension][dimension];
        this.dimension = dimension;
        this.gridSize = size / dimension;
        initGridComponents();
    }

    private void initGridComponents() {//将格子放入画板中
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                gridComponents[row][col] = new SquareComponent(gridSize,
                        (row + col) % 2 == 0 ? BOARD_COLOR_1 : BOARD_COLOR_2);
                gridComponents[row][col].setLocation(row * gridSize, col * gridSize);
                add(gridComponents[row][col]);
            }
        }
    }

    public SquareComponent getGridAt(ChessBoardLocation location) {
        return gridComponents[location.getRow()][location.getColumn()];
    }

    public void setChessAtGrid(ChessBoardLocation location, Color color) {
        removeChessAtGrid(location);
        getGridAt(location).add(new ChessComponent(color));
    }

    public void removeChessAtGrid(ChessBoardLocation location) {
        // Note: re-validation is required after remove / removeAll
        getGridAt(location).removeAll();
        getGridAt(location).revalidate();
    }

    public void reSelectGrid(ChessBoardLocation location){
        getGridAt(location).setSelected(false);
    }


    private ChessBoardLocation getLocationByPosition(int x, int y) {
        System.out.printf("%d ,%d\n",x,y);
        return new ChessBoardLocation(x / gridSize, y / gridSize);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            JComponent clickedComponent = (JComponent) getComponentAt(e.getX(), e.getY());
            ChessBoardLocation location = getLocationByPosition(e.getX(), e.getY());
            for (InputListener listener : listenerList) {
                if (clickedComponent.getComponentCount() == 0) {
                    if (listener.onPlayerClickSquare(location, (SquareComponent) clickedComponent)){
                        listener.getModel().getMoveTo().add(location);
                    }
                    if (listener.getPlayer().isWin()){
                        listener.getModel().getMoveFrom().remove(listener.getModel().getMoveFrom().get(listener.getModel().getMoveFrom().size()-1));
                        listener.getModel().getMoveTo().remove(location);
                        listener.getModel().getMoveTo().remove(location);
                    }
                    if (listener.getModel().getMoveTo().size() > listener.getModel().getMoveFrom().size()*2){
                        int difference = listener.getModel().getMoveTo().size() - listener.getModel().getMoveFrom().size()*2;
                        for (int i = 0; i < difference; i++) {
                            listener.getModel().getMoveTo().remove(listener.getModel().getMoveTo().size() - 1);
                        }
                    }
                    System.out.print("To");
                    for (ChessBoardLocation to : listener.getModel().getMoveTo()) {
                        System.out.printf("(%d,%d)", to.getRow(), to.getColumn());
                    }
                    System.out.println();
                } else {
                    if (listener.onPlayerClickChessPiece(location, (ChessComponent) clickedComponent.getComponent(0))){
                        listener.getModel().getMoveFrom().add(location);
                        if (listener.getModel().getMoveFrom().size() *2- listener.getModel().getMoveTo().size() == 3){
                            for (int i = 0; i < 2; i++) {
                                listener.getModel().getMoveFrom().remove(listener.getModel().getMoveFrom().size() - 1);
                            }
                            listener.getModel().getMoveTo().remove(listener.getModel().getMoveTo().get(listener.getModel().getMoveTo().size()-1));
                        }else if (listener.getModel().getMoveFrom().size()*2 - listener.getModel().getMoveTo().size() == 4){
                            listener.getModel().getMoveFrom().remove(location);
                            listener.getModel().getMoveFrom().remove(location);
                        }
                    }
                    System.out.print("From");
                    for (ChessBoardLocation L : listener.getModel().getMoveFrom()) {
                        System.out.printf("(%d,%d)", L.getRow(), L.getColumn());
                    }
                    System.out.println();
                }
                System.out.printf("location:(%d,%d)", location.getRow(), location.getColumn());
                System.out.println();            }
        }
    }

/*    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            JComponent clickedComponent = (JComponent) getComponentAt(e.getX(), e.getY());
            ChessBoardLocation location = getLocationByPosition(e.getX(), e.getY());
            for (InputListener listener : listenerList) {
                if (clickedComponent.getComponentCount() == 0) {
                    if (listener.onPlayerClickSquare(location, (SquareComponent) clickedComponent)){
                        listener.getModel().getMoveTo().add(location);
                    }
                    if (listener.getPlayer().isWin()){
                        listener.getModel().getMoveFrom().remove(listener.getModel().getMoveFrom().get(listener.getModel().getMoveFrom().size()-1));
                        listener.getModel().getMoveTo().remove(location);
          //              listener.getModel().getMoveTo().remove(location);
                    }
                    if (listener.getModel().getMoveTo().size() > listener.getModel().getMoveFrom().size()){
                        int difference = listener.getModel().getMoveTo().size() - listener.getModel().getMoveFrom().size();
                        for (int i = 0; i < difference; i++) {
                            listener.getModel().getMoveTo().remove(listener.getModel().getMoveTo().size() - 1);
                        }
                    }
                    System.out.print("To");
                    for (ChessBoardLocation to : listener.getModel().getMoveTo()) {
                        System.out.printf("(%d,%d)", to.getRow(), to.getColumn());
                    }
                    System.out.println();
                } else {
                    if (listener.onPlayerClickChessPiece(location, (ChessComponent) clickedComponent.getComponent(0))){
                        listener.getModel().getMoveFrom().add(location);
                        if (listener.getModel().getMoveFrom().size() - listener.getModel().getMoveTo().size() > 1){
                            int difference = listener.getModel().getMoveFrom().size() - listener.getModel().getMoveTo().size();
                            for (int i = 0; i < difference; i++) {
                                listener.getModel().getMoveFrom().remove(listener.getModel().getMoveFrom().size() - 1);
                            }
//                            listener.getModel().getMoveFrom().remove(location);
//                            listener.getModel().getMoveFrom().remove(location);
                        }
                    }
                    System.out.print("From");
                    for (ChessBoardLocation L : listener.getModel().getMoveFrom()) {
                        System.out.printf("(%d,%d)", L.getRow(), L.getColumn());
                    }
                    System.out.println();
                }
                System.out.printf("location:(%d,%d)", location.getRow(), location.getColumn());
                System.out.println();            }
        }
    }
*/
    @Override
    public void onChessPiecePlace(ChessBoardLocation location, ChessPiece piece) {
        setChessAtGrid(location, piece.getColor());
        repaint();
    }

    @Override
    public void onChessPieceRemove(ChessBoardLocation location) {
        removeChessAtGrid(location);
        repaint();
    }

    @Override
    public void onChessBoardReload(ChessBoard board) {
        for (int row = 0; row < board.getDimension(); row++) {
            for (int col = 0; col < board.getDimension(); col++) {
                ChessBoardLocation location = new ChessBoardLocation(row, col);
                ChessPiece piece = board.getChessPieceAt(location);
                if (piece != null) {
                    setChessAtGrid(location, piece.getColor());
                }else{
                    removeChessAtGrid(location);
                    reSelectGrid(location);
                }
            }
        }
        repaint();
    }

    @Override
    public void registerListener(InputListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void unregisterListener(InputListener listener) {
        listenerList.remove(listener);
    }

}
