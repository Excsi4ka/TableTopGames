package excsi.tabletop.common.item;

import excsi.tabletop.TabletopGamesMain;
import excsi.tabletop.common.blocks.ModBlocks;
import excsi.tabletop.common.util.CreativeTab;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TabletopGamesMain.MODID);

    public static final RegistryObject<Item> ChessBoardItem = ITEMS.register("chess_board",
            () -> new BlockItem(ModBlocks.BlockChessBoard.get(),new Item.Properties().group(CreativeTab.TableTop)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
