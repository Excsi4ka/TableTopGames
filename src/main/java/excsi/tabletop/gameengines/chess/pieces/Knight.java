package excsi.tabletop.gameengines.chess.pieces;

import excsi.tabletop.gameengines.chess.*;

import java.util.ArrayList;
import java.util.List;

public class Knight extends AbstractChessPiece {

    public Knight(int row, int column, Board board, PieceColor pieceColor) {
        super(row, column, board, pieceColor);
    }

    @Override
    public List<Move> getPseudoLegalMoves() {
        List<Move> moves = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) != Math.abs(j) && i != 0 && j != 0 && isWithinBoard(row + i, column + j)) {
                    if (isEmptyTile(row + i, column + j)) {
                        moves.add(new Move(this.getType(), row, column, row + i, column + j, MoveType.REGULAR_MOVE));
                    } else {
                        if (isEnemyPiece(row + i, column + j)) {
                            MoveType result = chessBoard.grid[row + i][column + j].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                            moves.add(new Move(this.getType(), row, column, row + i, column + j, result));
                        }
                    }
                }
            }
        }
        return moves;
    }

    @Override
    public PieceType getType() {
        return PieceType.KNIGHT;
    }

    @Override
    public char toChar() {
        return pieceColor.isWhite()?'N':'n';
    }

    @Override
    public PieceRenderInfo getRenderInfo() {
        return PieceRenderInfo.KNIGHT;
    }
}
