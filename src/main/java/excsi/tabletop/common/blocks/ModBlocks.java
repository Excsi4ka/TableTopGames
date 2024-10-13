package excsi.tabletop.common.blocks;

import excsi.tabletop.TabletopGamesMain;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TabletopGamesMain.MODID);

    public static final RegistryObject<Block> BlockChessBoard = BLOCKS.register("chess_board",BlockChessBoard::new);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
