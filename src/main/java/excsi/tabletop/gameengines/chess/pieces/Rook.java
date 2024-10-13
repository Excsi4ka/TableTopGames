package excsi.tabletop.gameengines.chess.pieces;

import excsi.tabletop.gameengines.chess.*;

import java.util.ArrayList;
import java.util.List;

public class Rook extends AbstractChessPiece {

    public Rook(int row, int column, Board board, PieceColor pieceColor) {
        super(row, column, board, pieceColor);
    }

    @Override
    public List<Move> getPseudoLegalMoves() {
        List<Move> moves = new ArrayList<>();
        this.fillLineResults(moves);
        return moves;
    }

    @Override
    public PieceType getType() {
        return PieceType.ROOK;
    }

    @Override
    public char toChar() {
        return pieceColor.isWhite()?'R':'r';
    }

    @Override
    public PieceRenderInfo getRenderInfo() {
        return PieceRenderInfo.ROOK;
    }
}
