package excsi.tabletop.gameengines.chess;

public enum PieceColor {
    WHITE,
    BLACK,
    NONE;

    public boolean isWhite(){
        return this==WHITE;
    }

    public PieceColor reverse(){
        return this==WHITE?BLACK:WHITE;
    }
}
