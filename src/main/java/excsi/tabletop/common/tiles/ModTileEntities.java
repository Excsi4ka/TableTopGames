package excsi.tabletop.common.tiles;

import excsi.tabletop.TabletopGamesMain;
import excsi.tabletop.common.blocks.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TabletopGamesMain.MODID);

    public static final RegistryObject<TileEntityType<TileChessBoard>> tileChessBoard = TILES.register("chess_board_tile",
            () -> TileEntityType.Builder.create(TileChessBoard::new, ModBlocks.BlockChessBoard.get()).build(null));

    public static void register(IEventBus eventBus){
        TILES.register(eventBus);
    }
}
