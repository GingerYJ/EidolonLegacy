package elucent.eidolon.spell;

import net.minecraft.item.ItemStack;

public interface IRitualItemFocus extends IRitualItemProvider {
    void replace(ItemStack stack);
}
