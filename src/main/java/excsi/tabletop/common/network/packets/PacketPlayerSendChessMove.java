package excsi.tabletop.common.network.packets;

import com.mojang.datafixers.util.Pair;
import excsi.tabletop.TabletopGamesMain;
import excsi.tabletop.common.tiles.TileChessBoard;
import excsi.tabletop.gameengines.chess.*;
import excsi.tabletop.gameengines.chess.pieces.AbstractChessPiece;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPlayerSendChessMove {

    public String lastMove;

    public int x,y,z;

    public PacketPlayerSendChessMove(String move, int x, int y, int z) {
        this.lastMove = move;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(PacketPlayerSendChessMove message, PacketBuffer buffer){
        buffer.writeString(message.lastMove);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    public static PacketPlayerSendChessMove decode(PacketBuffer buffer){
        return new PacketPlayerSendChessMove(buffer.readString(),buffer.readInt(),buffer.readInt(),buffer.readInt());
    }

    public static void handle(PacketPlayerSendChessMove message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null)
                return;
            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            TileEntity tile = player.world.getTileEntity(pos);
            if (!(tile instanceof TileChessBoard))
                return;
            TileChessBoard chessTile = (TileChessBoard) tile;
            Board board = new Board(chessTile);
            try {
                Move messageMove = board.readMove(message.lastMove);
                AbstractChessPiece piece = board.grid[messageMove.oldRow][messageMove.oldColumn];
                boolean gameOver = false;
                if (piece != null && piece.canMoveTo(messageMove.newRow, messageMove.newColumn) && !chessTile.gameOver) {
                    piece.setPosition(messageMove.newRow, messageMove.newColumn);
                    piece = board.handleSpecialCases(piece, messageMove);
                    PieceColor checkedColor = board.isCheck(piece) ? (piece.pieceColor.reverse()) : PieceColor.NONE;
                    PieceColor promotionLockedColor = messageMove.moveType == MoveType.PROMOTION ? piece.pieceColor : PieceColor.NONE;
                    board.moveList.add(messageMove);
                    if (board.hasNoMovesLeft(piece.pieceColor)) {
                        if (checkedColor != PieceColor.NONE)
                            chessTile.winner = player.getName().getString();
                        else
                            chessTile.winner = "DRAW";
                        gameOver = true;
                    }
                    if(messageMove.isIrreversibleMove()) {
                        board.halfMoveCount = 0;
                        chessTile.boardStates = "";
                    }else {
                        board.halfMoveCount++;
                        String s = board.createFENString().split(" ")[0];
                        chessTile.boardStates = chessTile.boardStates.concat(s).concat(";");
                    }
                    if (board.onlyKingsLeft() || board.threeRepetitionsFound() || board.fiftyMoveRule()) {
                        chessTile.winner = "DRAW";
                        gameOver = true;
                    }
                    if (messageMove.moveType != MoveType.DOUBLE_PAWN_MOVE)
                        board.enPassantPos = null;
                    else
                        board.enPassantPos = new Pair<>(messageMove.newColumn, messageMove.newRow);
                    board.currentTurn = promotionLockedColor != PieceColor.NONE ? promotionLockedColor : piece.pieceColor.reverse();
                    board.fullMoveCount++;
                    chessTile.moveList = board.stringifyMoveList();
                    chessTile.gameData = board.createFENString();
                    chessTile.checkedColor = checkedColor;
                    chessTile.promotionLockedColor = promotionLockedColor;
                    chessTile.gameOver = gameOver;
                    if(gameOver)
                        chessTile.timestamp = player.world.getGameTime()+200;
                    player.world.notifyBlockUpdate(pos, tile.getBlockState(), tile.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                } else if (piece != null && !piece.canMoveTo(messageMove.newRow, messageMove.newColumn)) {
                    TabletopGamesMain.LOGGER.warn(player.getName().getString() + " had made an illegal move! Cheating?");
                }
            } catch (Exception e) {
                TabletopGamesMain.LOGGER.error("A severe error occurred while handling the move. Please report this");
                TabletopGamesMain.LOGGER.trace(e);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
