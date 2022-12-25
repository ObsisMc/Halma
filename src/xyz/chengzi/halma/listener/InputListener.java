package xyz.chengzi.halma.listener;

import xyz.chengzi.halma.model.ChessBoard;
import xyz.chengzi.halma.model.ChessBoardLocation;
import xyz.chengzi.halma.view.ChessComponent;
import xyz.chengzi.halma.view.Player;
import xyz.chengzi.halma.view.SquareComponent;

public interface InputListener extends Listener {

    boolean onPlayerClickSquare(ChessBoardLocation location, SquareComponent component);

    boolean onPlayerClickChessPiece(ChessBoardLocation location, ChessComponent component);

    ChessBoard getModel();

    Player getPlayer();
}
