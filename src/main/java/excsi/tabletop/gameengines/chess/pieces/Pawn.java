package excsi.tabletop.gameengines.chess.pieces;

import com.mojang.datafixers.util.Pair;
import excsi.tabletop.gameengines.chess.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends AbstractChessPiece {

    public int startingRow;

    public Pawn(int row, int column, Board board, PieceColor pieceColor) {
        super(row, column, board, pieceColor);
        startingRow = pieceColor.isWhite()?1:6;
    }

    @Override
    public List<Move> getPseudoLegalMoves() {
        List<Move> moves = new ArrayList<>();
        int direction = pieceColor==PieceColor.WHITE?1:-1;
        if(isWithinBoard(row +direction, column) && isEmptyTile(row +direction, column)) {
            if((pieceColor==PieceColor.WHITE && row +direction==7) || (pieceColor==PieceColor.BLACK && row +direction==0))
                moves.add(new Move(this.getType(), row, column, row + direction, column, MoveType.PROMOTION));
            else
                moves.add(new Move(this.getType(), row, column, row + direction, column, MoveType.REGULAR_MOVE));
        }
        if(row == startingRow && isEmptyTile(row + direction*2, column) && isEmptyTile(row + direction, column)) {
            moves.add(new Move(this.getType(), row, column, row + direction*2, column, MoveType.DOUBLE_PAWN_MOVE));
        }
        for(int i = -1;i<2;i+=2) {
            if (isWithinBoard(row +direction, column +i) && !isEmptyTile(row +direction, column +i) && isEnemyPiece(row +direction, column +i)) {
                MoveType result = chessBoard.grid[row + direction][column + i].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                if(result==MoveType.CAPTURE && ((pieceColor==PieceColor.WHITE && row +direction==7) || (pieceColor==PieceColor.BLACK && row +direction==0)))
                    result = MoveType.PROMOTION;
                moves.add(new Move(this.getType(), row, column, row + direction, column + i, result));
            }
            if (isWithinBoard(row, column +i) && !isEmptyTile(row, column +i) && isEnemyPiece(row, column +i)) {
                AbstractChessPiece piece = chessBoard.grid[row][column+i];
                if(piece.getType() == PieceType.PAWN && chessBoard.enPassantPos != null && chessBoard.enPassantPos.equals(new Pair<>(column+i,row)))
                    moves.add(new Move(this.getType(), row, column, row + direction, column + i, MoveType.EN_PASSANT));
            }
        }
        return moves;
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }

    @Override
    public char toChar() {
        return pieceColor.isWhite()?'P':'p';
    }

    @Override
    public PieceRenderInfo getRenderInfo() {
        return PieceRenderInfo.PAWN;
    }
}
