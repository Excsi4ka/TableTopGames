package excsi.tabletop.gameengines.chess.pieces;

import excsi.tabletop.gameengines.chess.*;

import java.util.ArrayList;
import java.util.List;

public class Queen extends AbstractChessPiece {

    public Queen(int row, int column, Board board, PieceColor pieceColor) {
        super(row, column, board, pieceColor);
    }

    @Override
    public List<Move> getPseudoLegalMoves() {
        List<Move> moves = new ArrayList<>();
        this.fillLineResults(moves);
        this.fillDiagonalResults(moves);
        return moves;
    }

    @Override
    public PieceType getType() {
        return PieceType.QUEEN;
    }

    @Override
    public char toChar() {
        return pieceColor.isWhite()?'Q':'q';
    }

    @Override
    public PieceRenderInfo getRenderInfo() {
        return PieceRenderInfo.QUEEN;
    }
}
