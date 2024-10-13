package excsi.tabletop.common.network.packets;

import excsi.tabletop.common.tiles.TileChessBoard;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class PacketPlayerReady {

    public int ordinal,x,y,z;

    public final Random random;

    public PacketPlayerReady(int ordinal, int x, int y, int z) {
        random = new Random();
        this.ordinal = ordinal;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(PacketPlayerReady message, PacketBuffer buffer){
        buffer.writeInt(message.ordinal);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    public static PacketPlayerReady decode(PacketBuffer buffer){
        return new PacketPlayerReady(buffer.readInt(),buffer.readInt(),buffer.readInt(),buffer.readInt());
    }

    public static void handle(PacketPlayerReady message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null)
                return;
            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            TileEntity tileEntity = player.world.getTileEntity(pos);
            String name = player.getName().getString();
            if (!(tileEntity instanceof TileChessBoard))
                return;
            TileChessBoard tile = (TileChessBoard) tileEntity;
            String[] queue = tile.playerQueue.split(";");
            boolean alreadyReady = false, otherPlayerReady = false;
            String newQueue = "";
            int index = 0;
            for(int i = 0; i < queue.length; i++) {
                if(!queue[i].equals(" ") && !queue[i].equals(name)) {
                    otherPlayerReady = true;
                    if(message.ordinal == i) {
                        return;
                    }
                    continue;
                }
                if(queue[i].equals(name)) {
                    alreadyReady = true;
                    index = i;
                }
            }
            if(!alreadyReady)
                queue[message.ordinal] = name;
            else
                queue[index] = " ";
            if(!alreadyReady && otherPlayerReady) {
                tile.gameStarted = true;
                tile.playerWhite = queue[0];
                tile.playerBlack = queue[1];
            }else {
                for (String s : queue)
                    newQueue = newQueue.concat(s).concat(";");
                tile.playerQueue = newQueue;
            }
            player.world.notifyBlockUpdate(pos, tile.getBlockState(), tile.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        });
        ctx.get().setPacketHandled(true);
    }
}
