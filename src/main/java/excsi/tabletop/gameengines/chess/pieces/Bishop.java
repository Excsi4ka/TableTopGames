package excsi.tabletop.gameengines.chess.pieces;

import excsi.tabletop.gameengines.chess.*;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends AbstractChessPiece {

    public Bishop(int row, int column, Board board, PieceColor pieceColor) {
        super(row, column, board, pieceColor);
    }

    @Override
    public List<Move> getPseudoLegalMoves() {
        List<Move> moves = new ArrayList<>();
        this.fillDiagonalResults(moves);
        return moves;
    }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }

    @Override
    public char toChar() {
        return pieceColor.isWhite()?'B':'b';
    }

    @Override
    public PieceRenderInfo getRenderInfo() {
        return PieceRenderInfo.BISHOP;
    }
}
