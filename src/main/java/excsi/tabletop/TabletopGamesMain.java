package excsi.tabletop;

import excsi.tabletop.client.renderers.ChessBoardRenderer;
import excsi.tabletop.common.blocks.ModBlocks;
import excsi.tabletop.common.item.ModItems;
import excsi.tabletop.common.network.PacketChannel;
import excsi.tabletop.common.tiles.ModTileEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TabletopGamesMain.MODID)
public class TabletopGamesMain {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "tabletop";

    public TabletopGamesMain() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModTileEntities.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketChannel.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.tileChessBoard.get(), ChessBoardRenderer::new);
    }
}
