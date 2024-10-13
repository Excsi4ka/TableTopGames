package excsi.tabletop.gameengines.chess.pieces;

import excsi.tabletop.gameengines.chess.*;

import java.util.ArrayList;
import java.util.List;

public class King extends AbstractChessPiece {

    public final Move checkLeft = new Move(this.getType(), row, column, row, column-1,MoveType.NONE);

    public final Move checkRight = new Move(this.getType(), row, column, row, column+1,MoveType.NONE);

    public boolean castleLeft, castleRight;

    public King(int row, int column, Board board, PieceColor pieceColor) {
        super(row, column, board, pieceColor);
    }

    @Override
    public List<Move> getPseudoLegalMoves() {
        castleLeft = pieceColor.isWhite()?chessBoard.castleLWhite:chessBoard.castleLBlack;
        castleRight = pieceColor.isWhite()?chessBoard.castleRWhite:chessBoard.castleRBlack;
        List<Move> moves = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isWithinBoard(row + i, column + j)) {
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

        if (chessBoard.checkedColor == pieceColor)
            return moves;

        if (castleLeft && !isEmptyTile(row,column - 4) && chessBoard.grid[row][column - 4].getType() == PieceType.ROOK) {
            AbstractChessPiece piece = chessBoard.grid[row][column - 4];
            boolean emptyPath = true;
            for (int i = piece.column + 1; i < column; i++) {
                if (!isEmptyTile(row, i))
                    emptyPath = false;
            }
            if (emptyPath)
                moves.add(new Move(this.getType(), row, column, row, column - 2, MoveType.CASTLING_LEFT));
        }
        if (castleRight && !isEmptyTile(row,column + 3) && chessBoard.grid[row][column + 3].getType() == PieceType.ROOK) {
            AbstractChessPiece piece = chessBoard.grid[row][column + 3];
            boolean emptyPath = true;
            for (int i = piece.column - 1; i > column; i--) {
                if (!isEmptyTile(row, i))
                    emptyPath = false;
            }
            if (emptyPath)
                moves.add(new Move(this.getType(), row, column, row, column + 2, MoveType.CASTLING_RIGHT));
        }
        return moves;
    }

    @Override
    public List<Move> getLegalMoves() {
        List<Move> moves = this.getPseudoLegalMoves();
        List<Move> illegalMoves = this.getIllegalMoves(moves);
        moves.removeAll(illegalMoves);
        for(Move m:moves){
            if(m.moveType==MoveType.CASTLING_LEFT && !moves.contains(checkLeft))
                illegalMoves.add(m);
            if(m.moveType==MoveType.CASTLING_RIGHT && !moves.contains(checkRight))
                illegalMoves.add(m);
        }
        moves.removeAll(illegalMoves);
        return moves;
    }

    @Override
    public PieceType getType() {
        return PieceType.KING;
    }

    @Override
    public char toChar() {
        return pieceColor.isWhite()?'K':'k';
    }

    @Override
    public PieceRenderInfo getRenderInfo() {
        return PieceRenderInfo.KING;
    }
}
