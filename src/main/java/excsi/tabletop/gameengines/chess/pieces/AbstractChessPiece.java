package excsi.tabletop.gameengines.chess.pieces;

import excsi.tabletop.gameengines.chess.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractChessPiece {

    public int row, column;

    public Board chessBoard;


    public PieceColor pieceColor;

    public static char[] promotionChars = new char[]{'b','r','n','q'};

    public AbstractChessPiece(int row, int column, Board board, PieceColor pieceColor) {
        this.row = row;
        this.column = column;
        this.chessBoard = board;
        this.pieceColor = pieceColor;
    }

    public abstract List<Move> getPseudoLegalMoves();

    public abstract PieceType getType();

    public abstract char toChar();

    @OnlyIn(Dist.CLIENT)
    public abstract PieceRenderInfo getRenderInfo();

    public boolean isSamePosition(int posX, int posZ) {
        return row == posX && column == posZ;
    }

    public boolean isWithinBoard(int posX, int posZ) {
        return posX >= 0 && posX < 8 && posZ >= 0 && posZ < 8;
    }

    public boolean canMoveTo(int posX, int posZ) {
        return this.getLegalMoves().contains(new Move(this.getType(), this.row, this.column, posX, posZ, MoveType.NONE));
    }

    public Move getSpecificMovePlayed(int posX, int posZ) {
        List<Move> possibleMoves = this.getLegalMoves();
        for (Move m : possibleMoves) {
            if (m.newRow == posX && m.newColumn == posZ)
                return m;
        }
        return null;
    }

    public List<Move> getLegalMoves() {
        List<Move> moves = this.getPseudoLegalMoves();
        moves.removeAll(getIllegalMoves(moves));
        return moves;
    }

    public void setPosition(int x,int z){
        chessBoard.grid[row][column]=null;
        row = x;
        column = z;
        chessBoard.grid[row][column]=this;
    }

    public void fillLineResults(List<Move> list) {
        //right
        for (int i = column + 1; i < 8; i++) {
            if (isEmptyTile(row, i)) {
                list.add(new Move(this.getType(), this.row, this.column, row, i, MoveType.REGULAR_MOVE));
            } else {
                if (isEnemyPiece(row, i)) {
                    MoveType result = chessBoard.grid[row][i].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                    list.add(new Move(this.getType(), this.row, this.column, row, i, result));
                }
                break;
            }
        }
        //left
        for (int i = column - 1; i >= 0; i--) {
            if (isEmptyTile(row, i)) {
                list.add(new Move(this.getType(), this.row, this.column, row, i, MoveType.REGULAR_MOVE));
            } else {
                if (isEnemyPiece(row, i)) {
                    MoveType result = chessBoard.grid[row][i].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                    list.add(new Move(this.getType(), this.row, this.column, row, i, result));
                }
                break;
            }
        }
        //up
        for (int i = row + 1; i < 8; i++) {
            if (isEmptyTile(i, column)) {
                list.add(new Move(this.getType(), this.row, this.column, i, column, MoveType.REGULAR_MOVE));
            } else {
                if (isEnemyPiece(i, column)) {
                    MoveType result = chessBoard.grid[i][column].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                    list.add(new Move(this.getType(), this.row, this.column, i, column, result));
                }
                break;
            }
        }
        //down
        for (int i = row - 1; i >= 0; i--) {
            if (isEmptyTile(i, column)) {
                list.add(new Move(this.getType(), this.row, this.column, i, column, MoveType.REGULAR_MOVE));
            } else {
                if (isEnemyPiece(i, column)) {
                    MoveType result = chessBoard.grid[i][column].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                    list.add(new Move(this.getType(), this.row, this.column, i, column, result));
                }
                break;
            }
        }
    }

    public void fillDiagonalResults(List<Move> list) {
        //up right
        for (int i = 1; i < 8; i++) {
            if (isWithinBoard(row + i, column + i)) {
                if (isEmptyTile(row + i, column + i)) {
                    list.add(new Move(this.getType(), row, column, row + i, column + i, MoveType.REGULAR_MOVE));
                } else {
                    if (isEnemyPiece(row + i, column + i)) {
                        MoveType result = chessBoard.grid[row + i][column + i].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                        list.add(new Move(this.getType(), row, column, row + i, column + i, result));
                    }
                    break;
                }
            }else break;
        }
        //down right
        for (int i = 1; i < 8; i++) {
            if (isWithinBoard(row - i, column + i)) {
                if (isEmptyTile(row - i, column + i)) {
                    list.add(new Move(this.getType(), row, column, row - i, column + i, MoveType.REGULAR_MOVE));
                } else {
                    if (isEnemyPiece(row - i, column + i)) {
                        MoveType result = chessBoard.grid[row - i][column + i].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                        list.add(new Move(this.getType(), row, column, row - i, column + i, result));
                    }
                    break;
                }
            }else break;
        }
        //up left
        for (int i = 1; i < 8; i++) {
            if (isWithinBoard(row + i, column - i)) {
                if (isEmptyTile(row + i, column - i)) {
                    list.add(new Move(this.getType(), row, column, row + i, column - i, MoveType.REGULAR_MOVE));
                } else {
                    if (isEnemyPiece(row + i, column - i)) {
                        MoveType result = chessBoard.grid[row + i][column - i].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                        list.add(new Move(this.getType(), row, column, row + i, column - i, result));
                    }
                    break;
                }
            }else break;
        }
        //down left
        for (int i = 1; i < 8; i++) {
            if (isWithinBoard(row - i, column - i)) {
                if (isEmptyTile(row - i, column - i)) {
                    list.add(new Move(this.getType(), row, column, row - i, column - i, MoveType.REGULAR_MOVE));
                } else {
                    if (isEnemyPiece(row - i, column - i)) {
                        MoveType result = chessBoard.grid[row - i][column - i].getType() == PieceType.KING ? MoveType.CHECK : MoveType.CAPTURE;
                        list.add(new Move(this.getType(), row, column, row - i, column - i, result));
                    }
                    break;
                }
            }else break;
        }
    }
    
    public boolean isEmptyTile(int posX,int posZ) {
        return chessBoard.grid[posX][posZ] == null;
    }

    public boolean isEnemyPiece(int posX,int posZ){
         return chessBoard.grid[posX][posZ].pieceColor != this.pieceColor;
    }

    public List<Move> getAllPossibleOpponentsMoves(Board simulatedBoard){
        List<Move> list = new ArrayList<>();
        for(AbstractChessPiece[] piecesRow:simulatedBoard.grid){
            for(AbstractChessPiece piece:piecesRow) {
                if(piece!=null && piece.pieceColor!=this.pieceColor)
                    list.addAll(piece.getPseudoLegalMoves());
            }
        }
        return list;
    }

    public List<Move> getIllegalMoves(List<Move> pieceMoves) {
        List<Move> illegalMoves = new ArrayList<>();
        Board simulatedBoard = new Board(this.chessBoard);
        String fen = simulatedBoard.createFENString();
        for(Move move:pieceMoves) {
            AbstractChessPiece piece = simulatedBoard.grid[move.oldRow][move.oldColumn];
            piece.setPosition(move.newRow,move.newColumn);
            List<Move> simulatedMoves = getAllPossibleOpponentsMoves(simulatedBoard);
            for(Move m:simulatedMoves){
                if(m.moveType == MoveType.CHECK)
                    illegalMoves.add(move);
            }
            simulatedBoard.parseFENString(fen);
        }
        return illegalMoves;
    }

    @Override
    public String toString(){
        return this.getType()+"("+pieceColor+")";
    }
}