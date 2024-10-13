package excsi.tabletop.gameengines.chess;

import excsi.tabletop.gameengines.chess.pieces.*;

public enum PieceType {
    PAWN,
    BISHOP,
    ROOK,
    KNIGHT,
    QUEEN,
    KING;

    public static AbstractChessPiece build(char c, int row, int column, Board board) {
        PieceColor color = Character.isUpperCase(c)?PieceColor.WHITE:PieceColor.BLACK;
        switch (Character.toLowerCase(c)) {
            case 'b' : return new Bishop(row,column,board,color);
            case 'k' : return new King(row,column,board,color);
            case 'n' : return new Knight(row,column,board,color);
            case 'p' : return new Pawn(row,column,board,color);
            case 'q' : return new Queen(row,column,board,color);
            case 'r' : return new Rook(row,column,board,color);
            default : return null;
        }
    }
}
