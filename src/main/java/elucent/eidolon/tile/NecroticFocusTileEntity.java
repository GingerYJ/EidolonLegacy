package elucent.eidolon.tile;

import elucent.eidolon.spell.IRitualItemFocus;
import net.minecraft.item.ItemStack;

public class NecroticFocusTileEntity extends ItemHolderTileEntity implements IRitualItemFocus {
    @Override
    public void replace(ItemStack stack) {
        removeStack();
        if (!stack.isEmpty()) {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            addStack(copy);
        }
    }
}
