package elucent.eidolon.spell;

import net.minecraft.item.ItemStack;

public interface IRitualItemProvider {
    ItemStack provide();

    void take();
}
