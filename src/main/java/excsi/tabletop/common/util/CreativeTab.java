package excsi.tabletop.common.util;

import excsi.tabletop.common.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTab {

    public static final ItemGroup TableTop = new ItemGroup("TABLE_TOP") {

        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.ChessBoardItem.get());
        }
    };
}
