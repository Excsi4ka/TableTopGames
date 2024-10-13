package excsi.tabletop.client;

import excsi.tabletop.client.gui.chess.ChessMainScreen;
import excsi.tabletop.common.tiles.TileChessBoard;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

public class ClientHelper {

    public static void openChessGui(TileEntity chessTile) {
        ChessMainScreen screen = new ChessMainScreen(((TileChessBoard)chessTile));
        Minecraft.getInstance().displayGuiScreen(screen);
    }
}
