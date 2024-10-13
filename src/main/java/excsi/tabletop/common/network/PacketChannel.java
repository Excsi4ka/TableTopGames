package excsi.tabletop.common.network;

import excsi.tabletop.TabletopGamesMain;
import excsi.tabletop.common.network.packets.PacketPlayerReady;
import excsi.tabletop.common.network.packets.PacketPlayerSendChessMove;
import excsi.tabletop.common.network.packets.PacketSendPromotionChoice;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketChannel {

    public static final String NETWORK_VERSION = "0.1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TabletopGamesMain.MODID, "chess_network"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init(){
        int id = 0;
        CHANNEL.registerMessage(id++, PacketPlayerSendChessMove.class, PacketPlayerSendChessMove::encode, PacketPlayerSendChessMove::decode, PacketPlayerSendChessMove::handle);
        CHANNEL.registerMessage(id++, PacketSendPromotionChoice.class, PacketSendPromotionChoice::encode, PacketSendPromotionChoice::decode, PacketSendPromotionChoice::handle);
        CHANNEL.registerMessage(id++, PacketPlayerReady.class, PacketPlayerReady::encode, PacketPlayerReady::decode, PacketPlayerReady::handle);
    }
}
