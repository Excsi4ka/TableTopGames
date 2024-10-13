package excsi.tabletop.common.network.packets;

import excsi.tabletop.TabletopGamesMain;
import excsi.tabletop.common.tiles.TileChessBoard;
import excsi.tabletop.gameengines.chess.*;
import excsi.tabletop.gameengines.chess.pieces.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSendPromotionChoice {

    public char c;

    public int x,y,z;

    public PacketSendPromotionChoice(char pieceType, int x, int y, int z) {
        this.c = pieceType;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(PacketSendPromotionChoice message, PacketBuffer buffer){
        buffer.writeChar(message.c);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    public static PacketSendPromotionChoice decode(PacketBuffer buffer){
        return new PacketSendPromotionChoice(buffer.readChar(),buffer.readInt(),buffer.readInt(),buffer.readInt());
    }

    public static void handle(PacketSendPromotionChoice message, Supplier<NetworkEvent.Context> ctx){
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
                Move lastMove = board.moveList.get(board.moveList.size() - 1);
                AbstractChessPiece pawn = board.grid[lastMove.newRow][lastMove.newColumn];
                AbstractChessPiece piece = null;
                int posX = pawn.row;
                int posZ = pawn.column;
                char c = pawn.pieceColor.isWhite() ? Character.toUpperCase(message.c) : message.c;
                if (lastMove.moveType == MoveType.PROMOTION) {
                    piece = PieceType.build(c, posX, posZ, board);
                    if (piece == null)
                        return;
                    boolean gameOver = false;
                    PieceColor checkedColor = board.isCheck(piece) ? (piece.pieceColor.reverse()) : PieceColor.NONE;
                    if (board.hasNoMovesLeft(piece.pieceColor)) {
                        if (checkedColor != PieceColor.NONE)
                            chessTile.winner = player.getName().getString();
                        else
                            chessTile.winner = "DRAW";
                        gameOver = true;
                    }
                    board.grid[posX][posZ] = piece;
                    board.enPassantPos = null;
                    board.currentTurn = piece.pieceColor.reverse();
                    board.halfMoveCount = 0;
                    chessTile.boardStates = "";
                    chessTile.gameData = board.createFENString();
                    chessTile.checkedColor = checkedColor;
                    chessTile.promotionLockedColor = PieceColor.NONE;
                    chessTile.gameOver = gameOver;
                    if(gameOver)
                        chessTile.timestamp = player.world.getGameTime()+200;
                    player.world.notifyBlockUpdate(pos, tile.getBlockState(), tile.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                }
            } catch (Exception e) {
                TabletopGamesMain.LOGGER.error("A severe error occurred while handling promotion. Please report this");
                TabletopGamesMain.LOGGER.trace(e);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
