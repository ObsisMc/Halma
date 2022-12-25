package xyz.chengzi.halma.model;

public class Square {
    private ChessBoardLocation location;
    private ChessPiece piece;
//    private boolean isSelected;
//    private  boolean isConfirmed;

    public Square(ChessBoardLocation location) {
        this.location = location;
    }

    public ChessBoardLocation getLocation() {
        return location;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

//    public boolean isSelected() {
//        return isSelected;
//    }
//
//    public void setSelected(boolean selected) {
//        isSelected = selected;
//    }
//
//    public boolean isConfirmed() {
//        return isConfirmed;
//    }
//
//    public void setConfirmed(boolean confirmed) {
//        isConfirmed = confirmed;
//    }
}
