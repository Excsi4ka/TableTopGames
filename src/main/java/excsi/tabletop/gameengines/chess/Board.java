package excsi.tabletop.gameengines.chess;

import com.mojang.datafixers.util.Pair;
import excsi.tabletop.common.network.PacketChannel;
import excsi.tabletop.common.network.packets.PacketPlayerReady;
import excsi.tabletop.common.network.packets.PacketPlayerSendChessMove;
import excsi.tabletop.common.network.packets.PacketSendPromotionChoice;
import excsi.tabletop.common.tiles.TileChessBoard;
import excsi.tabletop.gameengines.chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public AbstractChessPiece[][] grid = new AbstractChessPiece[8][8];

    public TileChessBoard tileChessBoard;

    public PieceColor checkedColor, currentTurn;

    public AbstractChessPiece currentlySelected;

    public List<Move> moveList = new ArrayList<>();

    public boolean castleLWhite, castleRWhite, castleLBlack, castleRBlack;

    public Pair<Integer, Integer> enPassantPos;

    public int halfMoveCount, fullMoveCount;

    public static final String StartingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/ w KQkq - 0 0";

    public Board(TileChessBoard tile) {
        this.readMoveList(tile.moveList);
        this.parseFENString(tile.gameData);
        this.tileChessBoard = tile;
        this.checkedColor = tile.checkedColor;
    }

    public Board(Board board) {
        this.moveList = board.moveList;
        this.parseFENString(board.createFENString());
        this.tileChessBoard = board.tileChessBoard;
        this.checkedColor = board.checkedColor;
    }

    public void selectPiece(int x, int z) {
        currentlySelected = grid[x][z];
    }

    public boolean handleMove(int posX, int posZ) {
        if (currentlySelected != null && currentlySelected.canMoveTo(posX, posZ)) {
            Move move = currentlySelected.getSpecificMovePlayed(posX, posZ);
            this.sendMovePacket(move);
            return true;
        }
        return false;
    }

    public void sendMovePacket(Move m) {
        String move = this.stringifyMove(m);
        PacketChannel.CHANNEL.sendToServer(new PacketPlayerSendChessMove(move,
                tileChessBoard.getPos().getX(), tileChessBoard.getPos().getY(), tileChessBoard.getPos().getZ()));
    }

    public void sendPromotionChoice(char c) {
        PacketChannel.CHANNEL.sendToServer(new PacketSendPromotionChoice(c, tileChessBoard.getPos().getX(), tileChessBoard.getPos().getY(), tileChessBoard.getPos().getZ()));
    }

    public void sendReady(int i) {
        PacketChannel.CHANNEL.sendToServer(new PacketPlayerReady(i, tileChessBoard.getPos().getX(), tileChessBoard.getPos().getY(), tileChessBoard.getPos().getZ()));
    }

    public void parseFENString(String fen) {
        String[] positions = fen.split("/");
        String[] additionalData = fen.split(" ");
        AbstractChessPiece[][] gridNew = new AbstractChessPiece[8][8];
        for (int row = 0; row < 8; ++row) {
            char[] rowData = positions[row].toCharArray();
            int column = 0;
            for (char c : rowData) {
                if (Character.isLetter(c)) {
                    AbstractChessPiece piece = PieceType.build(c, 7 - row, column, this);
                    column++;
                    gridNew[piece.row][piece.column] = piece;
                } else {
                    column += (c - '0');
                }
            }
        }
        grid = gridNew;
        String s = additionalData[1];
        currentTurn = s.charAt(0) == 'w' ? PieceColor.WHITE : PieceColor.BLACK;
        s = additionalData[2];
        castleRBlack = castleLBlack = castleLWhite = castleRWhite = false;
        for (char c : s.toCharArray()) {
            castleRWhite = castleRWhite || c == 'K';
            castleLWhite = castleLWhite || c == 'Q';
            castleRBlack = castleRBlack || c == 'k';
            castleLBlack = castleLBlack || c == 'q';
        }
        s = additionalData[3];
        enPassantPos = s.equals("-") ? null : new Pair<>(s.charAt(0) - 'a', s.charAt(1) - '0');
        halfMoveCount = additionalData[4].charAt(0) - '0';
        fullMoveCount = additionalData[5].charAt(0) - '0';
    }

    public String createFENString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 7; i >= 0; --i) {
            int offset = 0;
            for (int j = 0; j < 8; ++j) {
                if (grid[i][j] == null)
                    offset++;
                else {
                    if (offset > 0) {
                        builder.append(offset);
                        offset = 0;
                    }
                    builder.append(grid[i][j].toChar());
                }
            }
            if (offset > 0)
                builder.append(offset);
            builder.append("/");
        }
        builder.append(" ");
        builder.append(currentTurn.isWhite() ? 'w' : 'b').append(" ");
        builder.append(castleRWhite ? 'K' : "").append(castleLWhite ? 'Q' : "").append(castleRBlack ? 'k' : "").append(castleLBlack ? 'q' : "").append(" ");
        if (enPassantPos != null) {
            char c = (char) ('a' + enPassantPos.getFirst());
            builder.append(c).append(enPassantPos.getSecond());
        } else {
            builder.append("-");
        }
        builder.append(" ").append(halfMoveCount).append(" ").append(fullMoveCount);
        return builder.toString();
    }

    public AbstractChessPiece handleSpecialCases(AbstractChessPiece currentlySelected, Move move) {
        AbstractChessPiece pieceReturn = currentlySelected;
        MoveType type = move.moveType;
        if (type == MoveType.EN_PASSANT) {
            int direction = currentlySelected.pieceColor == PieceColor.WHITE ? -1 : 1;
            grid[currentlySelected.row + direction][currentlySelected.column] = null;
        }

        if (type == MoveType.CASTLING_RIGHT || type == MoveType.CASTLING_LEFT) {
            if (pieceReturn.pieceColor.isWhite())
                castleRWhite = castleLWhite = false;
            else
                castleRBlack = castleLBlack = false;
        }

        if (currentlySelected.pieceColor.isWhite() && (castleRWhite || castleLWhite)) {
            if (pieceReturn.getType() == PieceType.KING)
                castleRWhite = castleLWhite = false;
            if (castleRWhite && pieceReturn.getType() == PieceType.ROOK && pieceReturn.column == 7)
                castleRWhite = false;
            if (castleLWhite && pieceReturn.getType() == PieceType.ROOK && pieceReturn.column == 0)
                castleLWhite = false;
        }

        if (!currentlySelected.pieceColor.isWhite() && (castleRBlack || castleLBlack)) {
            if (pieceReturn.getType() == PieceType.KING)
                castleRBlack = castleLBlack = false;
            if (castleRBlack && pieceReturn.getType() == PieceType.ROOK && pieceReturn.column == 7)
                castleRBlack = false;
            if (castleLBlack && pieceReturn.getType() == PieceType.ROOK && pieceReturn.column == 0)
                castleLBlack = false;
        }

        if (type == MoveType.CASTLING_LEFT) {
            AbstractChessPiece piece = grid[move.oldRow][move.oldColumn - 4];
            piece.setPosition(move.oldRow, move.oldColumn - 1);
            pieceReturn = piece;
        }

        if (type == MoveType.CASTLING_RIGHT) {
            AbstractChessPiece piece = grid[move.oldRow][move.oldColumn + 3];
            piece.setPosition(move.oldRow, move.oldColumn + 1);
            pieceReturn = piece;
        }
        return pieceReturn;
    }

    public void syncInformation(TileChessBoard tile) {
        this.parseFENString(tile.gameData);
        this.readMoveList(tile.moveList);
        this.tileChessBoard = tile;
        this.checkedColor = tile.checkedColor;
    }


    public String stringifyMove(Move m) {
        return m.pieceType.ordinal() + "." + m.oldRow + "." + m.oldColumn + "." + m.newRow +
                "." + m.newColumn + "." + m.moveType.ordinal();
    }

    public Move readMove(String move) {
        String[] moveData = move.split("\\.");
        PieceType type = PieceType.values()[Integer.parseInt(moveData[0])];
        int oldX = Integer.parseInt(moveData[1]);
        int oldZ = Integer.parseInt(moveData[2]);
        int newX = Integer.parseInt(moveData[3]);
        int newZ = Integer.parseInt(moveData[4]);
        MoveType result = MoveType.values()[Integer.parseInt(moveData[5])];
        return new Move(type, oldX, oldZ, newX, newZ, result);
    }

    public boolean onlyKingsLeft() {
        boolean onlyKingLeft = true;
        for (AbstractChessPiece[] row : grid) {
            for (AbstractChessPiece piece : row) {
                if (piece != null && piece.getType() != PieceType.KING && onlyKingLeft) {
                    onlyKingLeft = false;
                    break;
                }
                if (!onlyKingLeft)
                    break;
            }
        }
        return onlyKingLeft;
    }

    public void readMoveList(String data) {
        if (data.equals(""))
            return;
        String[] entries = data.split(";");
        moveList = new ArrayList<>();
        for (String entry : entries) {
            moveList.add(this.readMove(entry));
        }
    }

    public String stringifyMoveList() {
        StringBuilder s = new StringBuilder();
        for (Move m : moveList) {
            s.append(this.stringifyMove(m)).append(";");
        }
        return s.toString();
    }

    public boolean threeRepetitionsFound() {
        int count = 0;
        String[] boardStates = tileChessBoard.boardStates.split(";");
        String lastBoardState = createFENString().split(" ")[0];
        for (String state : boardStates)
            if (lastBoardState.equals(state))
                count++;
        return count >= 3;
    }

    public boolean fiftyMoveRule() {
        return halfMoveCount >= 50;
    }

    public boolean isCheck(AbstractChessPiece piece) {
        List<Move> nextMoveList = piece.getLegalMoves();
        for (Move m : nextMoveList) {
            if (m.moveType == MoveType.CHECK)
                return true;
        }
        return false;
    }

    public boolean hasNoMovesLeft(PieceColor side) {
        List<Move> moves = new ArrayList<>();
        for (AbstractChessPiece[] row : grid) {
            for (AbstractChessPiece piece : row) {
                if (piece != null && piece.pieceColor != side) {
                    moves.addAll(piece.getLegalMoves());
                }
            }
        }
        return moves.isEmpty();
    }
}
