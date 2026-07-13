package elucent.eidolon.registries;

import elucent.eidolon.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class ModCreativeTabs {
    public static final CreativeTabs EIDOLON = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.CODEX);
        }
    };

    private ModCreativeTabs() {
    }
}
