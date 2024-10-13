package excsi.tabletop.gameengines.chess;

public class Move {

    public int oldRow, oldColumn, newRow, newColumn;

    public PieceType pieceType;

    public MoveType moveType;

    public Move(PieceType piece, int xOld, int zOld, int xNew, int zNew, MoveType result) {
        this.pieceType = piece;
        this.oldRow = xOld;
        this.oldColumn = zOld;
        this.newRow = xNew;
        this.newColumn = zNew;
        this.moveType = result;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Move) {
            Move move = (Move) o;
            return move.pieceType ==this.pieceType && move.oldRow ==this.oldRow && move.oldColumn ==this.oldColumn && move.newRow ==this.newRow && move.newColumn ==this.newColumn;
        }
        return false;
    }

    public boolean isIrreversibleMove() {
        return pieceType == PieceType.PAWN || moveType == MoveType.CAPTURE;
    }

    @Override
    public String toString() {
        return pieceType +" from("+ oldRow +","+ oldColumn +") to ("+ newRow +","+ newColumn +") type: "+moveType;
    }

    public String toAlgebraicNotation(){
        return "";
    }
}